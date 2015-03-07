package com.asteria.game.character;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Optional;

import com.asteria.game.NodeType;
import com.asteria.game.character.combat.Combat;
import com.asteria.game.character.player.Player;
import com.asteria.game.location.Position;
import com.asteria.task.Task;
import com.asteria.task.TaskHandler;
import com.asteria.utility.RandomGen;

/**
 * The movement queue sequencer that handles the entire movement process for
 * characters.
 * 
 * @author lare96 <http://github.com/lare96>
 */
public final class MovementQueue {

    /**
     * The direction delta {@code X} coordinates for movement.
     */
    public static final byte[] DIRECTION_DELTA_X = new byte[] { -1, 0, 1, -1, 1, -1, 0, 1 };

    /**
     * The direction delta {@code Y} coordinates for movement.
     */
    public static final byte[] DIRECTION_DELTA_Y = new byte[] { 1, 1, 1, 0, 0, -1, -1, -1 };

    /**
     * The character this movement queue is for.
     */
    private final CharacterNode character;

    /**
     * A double ended queue of waypoints in this movement queue.
     */
    private Deque<Point> waypoints = new LinkedList<>();

    /**
     * The task ran when following another character.
     */
    private Optional<Task> followTask = Optional.empty();

    /**
     * The flag that determines if the run button is toggled.
     */
    private boolean running = false;

    /**
     * The flag that determines if the current path is a run path.
     */
    private boolean runPath = false;

    /**
     * The flag that determines if movement is locked.
     */
    private boolean lockMovement;

    /**
     * Creates a new {@link MovementQueue}.
     * 
     * @param character
     *            the character this movement queue is for.
     */
    public MovementQueue(CharacterNode character) {
        this.character = character;
    }

    /**
     * Executes movement processing which primarily consists of polling
     * waypoints, and updating the map region.
     * 
     * @throws Exception
     *             if any errors occur while sequencing movement.
     */
    public void sequence() throws Exception {
        if (lockMovement || character.isFrozen()) {
            return;
        }

        Point walkPoint = null;
        Point runPoint = null;

        walkPoint = waypoints.poll();

        if (running) {
            runPoint = waypoints.poll();
        }

        runPath = runPoint != null;

        if (walkPoint != null && walkPoint.getDirection() != -1) {
            int x = MovementQueue.DIRECTION_DELTA_X[walkPoint.getDirection()];
            int y = MovementQueue.DIRECTION_DELTA_Y[walkPoint.getDirection()];

            if (character.isFollowing() && character.getFollowCharacter() != null) {
                if (character.getPosition().copy().move(x, y).equals(character.getFollowCharacter().getPosition())) {
                    return;
                }
            }

            character.getPosition().move(x, y);
            character.setPrimaryDirection(walkPoint.getDirection());
            character.setLastDirection(walkPoint.getDirection());

            if (character.getType() == NodeType.PLAYER) {
                ((Player) character).sendInterfaces();
            }
        }

        if (runPoint != null && runPoint.getDirection() != -1) {
            int x = MovementQueue.DIRECTION_DELTA_X[runPoint.getDirection()];
            int y = MovementQueue.DIRECTION_DELTA_Y[runPoint.getDirection()];

            if (character.isFollowing() && character.getFollowCharacter() != null) {
                if (character.getPosition().copy().move(x, y).equals(character.getFollowCharacter().getPosition())) {
                    return;
                }
            }

            if (character.getType() == NodeType.PLAYER) {
                Player player = (Player) character;
                if (player.getRunEnergy().getAndDecrement() > 0) {
                    player.sendInterfaces();
                    player.getEncoder().sendString(player.getRunEnergy() + "%", 149);
                } else {
                    running = false;
                    player.getEncoder().sendByteState(173, 0);
                }
            }

            character.setLastPosition(character.getPosition());
            character.getPosition().move(x, y);
            character.setSecondaryDirection(runPoint.getDirection());
            character.setLastDirection(runPoint.getDirection());
        }

        if (character.getType() == NodeType.PLAYER) {
            int deltaX = character.getPosition().getX() - character.getCurrentRegion().getRegionX() * 8;
            int deltaY = character.getPosition().getY() - character.getCurrentRegion().getRegionY() * 8;

            if (deltaX < 16 || deltaX >= 88 || deltaY < 16 || deltaY > 88) {
                ((Player) character).getEncoder().sendMapRegion();
            }
        }
    }

    /**
     * Forces the character to walk to a certain position point relevant to its
     * current position.
     * 
     * @param addX
     *            the amount of spaces to walk to the {@code X}.
     * @param addY
     *            the amount of spaces to walk to the {@code Y}.
     */
    public void walk(int addX, int addY) {
        walk(new Position(character.getPosition().getX() + addX, character.getPosition().getY() + addY));
    }

    /**
     * Forces the character to walk to a certain position point not relevant to
     * its current position.
     * 
     * @param position
     *            the position the character is moving too.
     */
    public void walk(Position position) {
        reset();
        addToPath(position);
        finish();
    }

    /**
     * Resets the walking queue for this character.
     */
    public void reset() {
        runPath = false;
        waypoints.clear();
        Position p = character.getPosition();
        waypoints.add(new Point(p.getX(), p.getY(), -1));
    }

    /**
     * Finishes the current path for this character.
     */
    public void finish() {
        waypoints.removeFirst();
    }

    /**
     * Determines if this walking queue is finished or not.
     * 
     * @return {@code true} if this walking queue is finished, {@code false}
     *         otherwise.
     */
    public boolean isMovementDone() {
        return waypoints.size() == 0;
    }

    /**
     * Adds a new position to the walking queue.
     * 
     * @param position
     *            the position to add.
     */
    public void addToPath(Position position) {
        if (waypoints.size() == 0) {
            reset();
        }
        Point last = waypoints.peekLast();
        int deltaX = position.getX() - last.getX();
        int deltaY = position.getY() - last.getY();
        int max = Math.max(Math.abs(deltaX), Math.abs(deltaY));
        for (int i = 0; i < max; i++) {
            if (deltaX < 0) {
                deltaX++;
            } else if (deltaX > 0) {
                deltaX--;
            }
            if (deltaY < 0) {
                deltaY++;
            } else if (deltaY > 0) {
                deltaY--;
            }
            addStep(position.getX() - deltaX, position.getY() - deltaY);
        }
    }

    /**
     * Adds a step to the walking queue.
     * 
     * @param x
     *            the {@code X} coordinate of the step.
     * @param y
     *            the {@code Y} coordinate of the step.
     */
    private void addStep(int x, int y) {
        if (waypoints.size() >= 100) {
            return;
        }
        Point last = waypoints.peekLast();
        int deltaX = x - last.getX();
        int deltaY = y - last.getY();
        int direction = direction(deltaX, deltaY);
        if (direction > -1) {
            waypoints.add(new Point(x, y, direction));
        }
    }

    /**
     * Calculates the direction between the two coordinates.
     * 
     * @param dx
     *            the first coordinate.
     * @param dy
     *            the second coordinate.
     * @return the direction.
     */
    private int direction(int dx, int dy) {
        if (dx < 0) {
            if (dy < 0) {
                return 5;
            } else if (dy > 0) {
                return 0;
            } else {
                return 3;
            }
        } else if (dx > 0) {
            if (dy < 0) {
                return 7;
            } else if (dy > 0) {
                return 2;
            } else {
                return 4;
            }
        } else {
            if (dy < 0) {
                return 6;
            } else if (dy > 0) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    /**
     * Prompts the controller of this movement queue to follow {@code leader}.
     * 
     * @param leader
     *            the character being followed.
     */
    public void follow(CharacterNode leader) {
        if (character.getFollowCharacter() != null && character.getFollowCharacter().equals(leader)) {
            return;
        }
        if (character.isFollowing() && !character.getFollowCharacter().equals(leader)) {
            character.faceCharacter(null);
            character.setFollowing(false);
            character.setFollowCharacter(null);
        }
        if (!character.isFollowing()) {
            followTask.ifPresent(t -> t.cancel());
        }
        if (!followTask.isPresent()) {
            character.setFollowing(true);
            character.setFollowCharacter(leader);
            followTask = Optional.of(new CharacterFollowTask(character, leader));
            TaskHandler.submit(followTask.get());
        }
    }

    /**
     * Determines if the run button is toggled.
     * 
     * @return {@code true} if the run button is toggled, {@code false}
     *         otherwise.
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Sets the value for {@link MovementQueue#running}.
     * 
     * @param runToggled
     *            the new value to set.
     */
    public void setRunning(boolean runToggled) {
        this.running = runToggled;
    }

    /**
     * Determines if the current path is a run path.
     * 
     * @return {@code true} if the current path is a run path, {@code false}
     *         otherwise.
     */
    public boolean isRunPath() {
        return runPath;
    }

    /**
     * Sets the value for {@link MovementQueue#runPath}.
     * 
     * @param runPath
     *            the new value to set.
     */
    public void setRunPath(boolean runPath) {
        this.runPath = runPath;
    }

    /**
     * Determines if the movement queue is locked.
     * 
     * @return {@code true} if the movement queue is locked, {@code false}
     *         otherwise.
     */
    public boolean isLockMovement() {
        return lockMovement;
    }

    /**
     * Sets the value for {@link MovementQueue#lockMovement}.
     * 
     * @param lockMovement
     *            the new value to set.
     */
    public void setLockMovement(boolean lockMovement) {
        this.lockMovement = lockMovement;
    }

    /**
     * The internal position type class with support for direction.
     * 
     * @author blakeman8192
     */
    private final class Point extends Position {

        /**
         * The direction to this point.
         */
        private final int direction;

        /**
         * Creates a new {@link Point}.
         * 
         * @param x
         *            the {@code X} coordinate.
         * @param y
         *            the {@code Y} coordinate.
         * @param direction
         *            the direction to this point.
         */
        public Point(int x, int y, int direction) {
            super(x, y);
            this.direction = direction;
        }

        /**
         * Gets the direction to this point.
         * 
         * @return the direction.
         */
        public int getDirection() {
            return direction;
        }
    }

    /**
     * The {@link Task} implementation that handles the entire following
     * process.
     * 
     * @author lare96 <http://github.com/lare96>
     */
    private static final class CharacterFollowTask extends Task {

        /**
         * The character this process is being executed for.
         */
        private final CharacterNode character;

        /**
         * The character being followed in this process.
         */
        private final CharacterNode leader;

        /**
         * The random generator to generate pseudo-random numbers.
         */
        private final RandomGen random = new RandomGen();

        /**
         * Creates a new {@link CharacterFollowTask}.
         * 
         * @param character
         *            the character this process is being executed for.
         * @param leader
         *            the character being followed in this process.
         */
        public CharacterFollowTask(CharacterNode character, CharacterNode leader) {
            super(1, true);
            this.character = character;
            this.leader = leader;
        }

        @Override
        public void execute() {
            if (!character.isFollowing() || !character.getPosition().withinDistance(leader.getPosition(), 20) || character
                .isDead() || leader.isDead()) {
                character.faceCharacter(null);
                character.setFollowing(false);
                character.setFollowCharacter(null);
                this.cancel();
                return;
            }

            character.faceCharacter(leader);
            if (character.getMovementQueue().isLockMovement() || character.isFrozen()) {
                return;
            }
            if (character.getPosition().equals(leader.getPosition().copy())) {
                character.getMovementQueue().reset();
                int[] dir = { 1, -1 };

                if (random.nextBoolean()) {
                    character.getMovementQueue().walk(random.random(dir), 0);
                } else {
                    character.getMovementQueue().walk(0, random.random(dir));
                }
                return;
            }
            if (character.getCombatBuilder().isAttacking() && character.getType() == NodeType.PLAYER) {
                character.getCombatBuilder().determineStrategy();

                if (Combat.checkAttackDistance(character.getCombatBuilder())) {
                    return;
                }
            }
            if (character.getPosition().withinDistance(leader.getPosition(), 1)) {
                return;
            }
            character.getMovementQueue().walk(leader.getPosition().copy());
        }

        @Override
        public void onCancel() {
            character.getMovementQueue().followTask = Optional.empty();
        }
    }
}
