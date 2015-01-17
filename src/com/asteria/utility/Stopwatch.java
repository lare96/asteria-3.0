package com.asteria.utility;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * A utility class that provides functionality for measuring the elapsed time
 * between two different time periods.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public final class Stopwatch {

    /**
     * The internal cached time that acts as a time stamp.
     */
    private long cachedTime = Utility.currentTime();

    /**
     * The current state of this stopwatch.
     */
    private State state = State.STOPPED;

    @Override
    public String toString() {
        boolean stopped = (state == State.STOPPED);
        return "STOPWATCH[elasped= " + (stopped ? 0 : elapsedTime()) + "]";
    }

    /**
     * Sets the internal cached time to {@link Utility#currentTime()},
     * effectively making {@link Stopwatch#elapsedTime()} and
     * {@link Stopwatch#elapsed(long, TimeUnit)} return {@code 0}. If this
     * stopwatch is in a {@link State#STOPPED} state, invocation of this method
     * will change it to a {@link State#RUNNING} state.
     * 
     * @return an instance of this stopwatch.
     */
    public Stopwatch reset() {
        cachedTime = Utility.currentTime();
        state = State.RUNNING;
        return this;
    }

    /**
     * Sets the internal cached time to {@code 0} effectively putting this
     * stopwatch in a {@link State#STOPPED} state.
     * 
     * @return an instance of this stopwatch.
     */
    public Stopwatch stop() {
        state = State.STOPPED;
        return this;
    }

    /**
     * Retrieves the elapsed time in {@code unit}. If this stopwatch is stopped
     * invocation of this method will throw an exception.
     * 
     * @param unit
     *            the time unit to retrieve the elapsed time in.
     * @return the elapsed time.
     * @throws IllegalStateException
     *             if this stopwatch has been stopped.
     */
    public long elapsedTime(TimeUnit unit) {
        if (state == State.STOPPED)
            throw new IllegalStateException("The timer has been stopped!");
        return unit.convert((Utility.currentTime() - cachedTime), TimeUnit.MILLISECONDS);
    }

    /**
     * Retrieves the elapsed time in {@link TimeUnit#MILLISECONDS}. If this
     * stopwatch is stopped invocation of this method will throw an exception.
     * 
     * @return the elapsed time.
     * @throws IllegalStateException
     *             if this stopwatch has been stopped.
     */
    public long elapsedTime() {
        return elapsedTime(TimeUnit.MILLISECONDS);
    }

    /**
     * Determines if the elapsed time is greater than {@code time} in
     * {@code unit}. If this stopwatch is stopped invocation of this method will
     * automatically return {@code true}.
     * 
     * @param time
     *            the time to check if greater than the elapsed time.
     * @param unit
     *            the time unit to check in.
     * @return {@code true} if the elapsed time has passed or this stopwatch has
     *         been stopped, {@code false} otherwise.
     */
    public boolean elapsed(long time, TimeUnit unit) {
        if (state == State.STOPPED)
            return true;
        return elapsedTime(unit) >= time;
    }

    /**
     * Determines if the elapsed time is greater than {@code time} in
     * {@link TimeUnit#MILLISECONDS}. If this stopwatch is stopped invocation of
     * this method will automatically return {@code true}.
     * 
     * @param time
     *            the time to check if greater than the elapsed time.
     * @return {@code true} if the elapsed time has passed or this stopwatch has
     *         been stopped, {@code false} otherwise.
     */
    public boolean elapsed(long time) {
        return elapsed(time, TimeUnit.MILLISECONDS);
    }

    /**
     * Executes {@code action} if the elapsed time is greater than {@code time}
     * in {@code unit}. If this stopwatch is stopped invocation of this method
     * will automatically execute {@code action}.
     * 
     * @param time
     *            the time to check if greater than the elapsed time.
     * @param action
     *            the action to execute if satisfied.
     * @param unit
     *            the time unit to check in.
     */
    public void ifElapsed(long time, Consumer<? super Long> action, TimeUnit unit) {
        if (state == State.STOPPED) {
            action.accept((long) 0);
            return;
        }

        long elapsed = elapsedTime(unit);
        if (elapsed >= time) {
            action.accept(elapsed);
        }
    }

    /**
     * Executes {@code action} if the elapsed time is greater than {@code time}
     * in {@link TimeUnit#MILLISECONDS}. If this stopwatch is stopped invocation
     * of this method will automatically execute {@code action}.
     * 
     * @param time
     *            the time to check if greater than the elapsed time.
     * @param action
     *            the action to execute if satisfied.
     * @param unit
     *            the time unit to check in.
     */
    public void ifElapsed(long timePassed, Consumer<? super Long> action) {
        ifElapsed(timePassed, action, TimeUnit.MILLISECONDS);
    }

    /**
     * The enumerated type representing all possible states of this stopwatch.
     * 
     * @author lare96 <http://www.rune-server.org/members/lare96/>
     */
    private enum State {
        RUNNING,
        STOPPED
    }
}