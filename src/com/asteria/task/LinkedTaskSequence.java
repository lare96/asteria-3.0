package com.asteria.task;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Queue;

import com.asteria.game.World;
import com.google.common.base.Preconditions;

/**
 * A sequence of {@link LinkedTask}s that are ran in <i>FIFO</i> order, and the
 * solution to scheduling tasks in bulk. The problem with scheduling many tasks
 * in succession is that math (a pain) or nesting tasks (very ugly) will have to
 * be used in order to time the delays properly. Another issue is having 10s,
 * 100s, and in some cases maybe even 1000s of tasks running at once for one
 * single context!
 * <p>
 * <p>
 * Linked task sequences work by utilizing an internal queue to keep a record of
 * tasks, in the order they were added. They can be configured to {@code replay}
 * so that when the sequence finishes, it starts over again. When a sequence is
 * executed, it acts as a single task determining when links need to be executed
 * (and subsequently removed) which solves the issue of having too many tasks
 * running for a single context. The way that execution for links is done is in
 * succession, meaning this solves this issue of having to nest tasks or use
 * math to figure out the delays; For instance, take these examples:
 * 
 * <pre>
 * World.submit(new Task(5, false) {
 *     &#064;Override
 *     public void execute() {
 *         System.out.println(&quot;#1: executed after 5 ticks&quot;);
 *     }
 * });
 * 
 * World.submit(new Task(7, false) {
 *     &#064;Override
 *     public void execute() {
 *         System.out.println(&quot;#2: executed 2 ticks after #1&quot;);
 *     }
 * });
 * </pre>
 * 
 * Or instead of the math, the nesting tasks alternative.
 * 
 * <pre>
 * World.submit(new Task(5, false) {
 *     &#064;Override
 *     public void execute() {
 *         System.out.println(&quot;#1: executed after 5 ticks&quot;);
 * 
 *         World.submit(new Task(2, false) {
 *             &#064;Override
 *             public void execute() {
 *                 System.out.println(&quot;#2: executed 2 ticks after #1&quot;);
 *             }
 *         });
 *     }
 * })
 * </pre>
 * 
 * Both very ugly, and extremely verbose. Now the same thing with a linked task
 * sequence...
 * 
 * <pre>
 * LinkedTaskSequence queue = new LinkedTaskSequence();
 * queue.connect(5, () -&gt; System.out.println(&quot;#1: executed after 5 ticks&quot;));
 * queue.connect(2, () -&gt; System.out.println(&quot;#2: executed 2 ticks after #1&quot;));
 * queue.start();
 * </pre>
 * 
 * @author lare96 <http://github.org/lare96>
 */
public final class LinkedTaskSequence extends Task {

    /**
     * The queue that contains a cache of the connected links. This cache is
     * maintained in order to ensure that the original connection of links is
     * never lost no matter how many times the sequence is executed. It can be
     * modified while this sequence is running, although links will not be added
     * to the main queue until the next sequence or replay.
     */
    private final Queue<LinkedTask> linkCache = new ArrayDeque<>();

    /**
     * The main queue that is actually modified when this sequence is ran. If
     * links are added to the cache when this sequence is running, they will not
     * be added to this queue until the next sequence or replay.
     */
    private final Queue<LinkedTask> linkQueue = new ArrayDeque<>();

    /**
     * The flag that determines if this sequence should replay; as in, if this
     * connection should keep on being executed once it completes.
     */
    private final boolean replay;

    /**
     * The counter that determines when each individual link will be ran.
     */
    private int tickCounter = -1;

    /**
     * Creates a new {@link LinkedTaskSequence}.
     *
     * @param replay
     *            determines if this sequence should replay.
     */
    public LinkedTaskSequence(boolean replay) {
        super(1, false);
        this.replay = replay;
    }

    /**
     * Creates a new {@link LinkedTaskSequence} that does not replay upon
     * completion.
     */
    public LinkedTaskSequence() {
        this(false);
    }

    @Override
    public void onSubmit() {
        Preconditions.checkState(linkCache.size() > 0, "linkCache.size() == 0");
        Preconditions.checkState(tickCounter == -1, "tickCounter != -1");
        tickCounter = 0;
        linkQueue.addAll(linkCache);
    }

    @Override
    public void onSequence() {
        if (linkQueue.peek() == null && replay)
            linkQueue.addAll(linkCache);
    }

    @Override
    public void execute() {
        LinkedTask link = linkQueue.peek();

        if (link == null) {
            this.cancel();
            return;
        }

        if (++tickCounter == link.getDelay()) {
            try {
                link.execute();
            } finally {
                linkQueue.poll();
                tickCounter = 0;
            }
        }
    }

    @Override
    public void onCancel() {
        if (linkQueue.size() > 0)
            linkQueue.clear();
        tickCounter = -1;
    }

    /**
     * Starts this sequence by submitting itself as a task. The equivalent to:
     * <p>
     * <p>
     * {@code World.submit(this);}
     * <p>
     * This is just an easier alternative.
     * 
     * @return an instance of itself, for chaining.
     */
    public LinkedTaskSequence start() {
        World.submit(this);
        return this;
    }

    /**
     * Clears all of the links in the backing link cache.
     * 
     * @return an instance of itself, for chaining.
     */
    public LinkedTaskSequence clear() {
        linkCache.clear();
        return this;
    }

    /**
     * Connects {@code link} to this sequence.
     * 
     * @param link
     *            the link to connect, cannot be {@code null}.
     * @return an instance of itself, for chaining.
     */
    public LinkedTaskSequence connect(LinkedTask link) {
        linkCache.add(Objects.requireNonNull(link));
        return this;
    }

    /**
     * Connects a link to this sequence. The difference between this function
     * and {@code connect(LinkedTask)} is that this function is designed for
     * very small links that do not need their own full fledged linked task; for
     * instance, take this verbose code:
     * 
     * <pre>
     * connect(new LinkedTask(2) {
     *     &#064;Override
     *     public void execute() {
     *         System.out.println(&quot;very small link&quot;);
     *     }
     * });
     * </pre>
     * 
     * We can use this method, since the link only has one small operation. This
     * method will convert the code into the above anyway under-the-hood, making
     * things less verbose.
     * 
     * <pre>
     * connect(2, () -&gt; System.out.println(&quot;very small link&quot;));
     * </pre>
     * 
     * 
     * @param link
     *            the link to connect, cannot be {@code null}.
     * @return an instance of itself, for chaining.
     */
    public LinkedTaskSequence connect(int delay, Runnable task) {
        connect(new LinkedTask(delay) {
            @Override
            public void execute() {
                task.run();
            }
        });
        return this;
    }

    /**
     * Connects a series of links to this sequence.
     * 
     * @param links
     *            the links to connect.
     * @return an instance of itself, for chaining.
     */
    public LinkedTaskSequence connectAll(Collection<LinkedTask> links) {
        links.forEach(this::connect);
        return this;
    }

    /**
     * Connects a series of links to this sequence.
     * 
     * @param links
     *            the links to connect.
     * @return an instance of itself, for chaining.
     */
    public LinkedTaskSequence connectAll(LinkedTask... links) {
        connectAll(Arrays.asList(links));
        return this;
    }
}
