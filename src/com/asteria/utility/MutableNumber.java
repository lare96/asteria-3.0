package com.asteria.utility;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * The container class that contains functions to simplify the modification of a
 * number.
 * <p>
 * <p>
 * This class is similar in functionality to {@link AtomicInteger} but does not
 * support atomic operations, and therefore should not be used across multiple
 * threads.
 *
 * @author lare96 <http://github.com/lare96>
 */
public final class MutableNumber extends Number implements Comparable<MutableNumber> {

    /**
     * The constant serial version UID for serialization.
     */
    private static final long serialVersionUID = -7475363158492415879L;

    /**
     * The value present within this counter.
     */
    private int value;

    /**
     * Creates a new {@link MutableNumber} with {@code value}.
     *
     * @param value
     *            the value present within this counter.
     */
    public MutableNumber(int value) {
        this.value = value;
    }

    /**
     * Creates a new {@link MutableNumber} with a value of {@code 0}.
     */
    public MutableNumber() {
        this(0);
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof MutableNumber))
            return false;
        MutableNumber other = (MutableNumber) obj;
        if (value != other.value)
            return false;
        return true;
    }

    @Override
    public int compareTo(MutableNumber o) {
        return Integer.compare(value, o.value);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This function equates to the {@link MutableNumber#get()} function.
     */
    @Override
    public int intValue() {
        return value;
    }

    @Override
    public long longValue() {
        return (long) value;
    }

    @Override
    public float floatValue() {
        return (float) value;
    }

    @Override
    public double doubleValue() {
        return (double) value;
    }

    /**
     * Returns the value within this counter and then increments it by
     * {@code amount} to a maximum of {@code maximum}.
     *
     * @param amount
     *            the amount to increment it by.
     * @param maximum
     *            the maximum amount it will be incremented to.
     * @return the value before it is incremented.
     */
    public int getAndIncrement(int amount, int maximum) {
        int val = value;
        value += amount;
        if (value > maximum)
            value = maximum;
        return val;
    }

    /**
     * Returns the value within this counter and then increments it by
     * {@code amount}.
     *
     * @param amount
     *            the amount to increment it by.
     * @return the value before it is incremented.
     */
    public int getAndIncrement(int amount) {
        return getAndIncrement(amount, Integer.MAX_VALUE);
    }

    /**
     * Returns the value within this counter and then increments it by an amount
     * of {@code 1}.
     *
     * @return the value before it is incremented.
     */
    public int getAndIncrement() {
        return getAndIncrement(1);
    }

    /**
     * Increments the value within this counter by {@code amount} to a maximum
     * of {@code maximum} and then returns it.
     *
     * @param amount
     *            the amount to increment it by.
     * @param maximum
     *            the maximum amount it will be incremented to.
     * @return the value after it is incremented.
     */
    public int incrementAndGet(int amount, int maximum) {
        value += amount;
        if (value > maximum)
            value = maximum;
        return value;
    }

    /**
     * Increments the value within this counter by {@code amount} and then
     * returns it.
     *
     * @param amount
     *            the amount to increment it by.
     * @return the value after it is incremented.
     */
    public int incrementAndGet(int amount) {
        return incrementAndGet(amount, Integer.MAX_VALUE);
    }

    /**
     * Increments the value within this counter by {@code 1} and then returns
     * it.
     *
     * @return the value after it is incremented.
     */
    public int incrementAndGet() {
        return incrementAndGet(1);
    }

    /**
     * Returns the value within this counter and then decrements it by
     * {@code amount} to a minimum of {@code minimum}.
     *
     * @param amount
     *            the amount to decrement it by.
     * @param minimum
     *            the minimum amount it will be decremented to.
     * @return the value before it is decremented.
     */
    public int getAndDecrement(int amount, int minimum) {
        int val = value;
        value -= amount;
        if (value < minimum)
            value = minimum;
        return val;
    }

    /**
     * Returns the value within this counter and then decrements it by
     * {@code amount}.
     *
     * @param amount
     *            the amount to decrement it by.
     * @return the value before it is decremented.
     */
    public int getAndDecrement(int amount) {
        return getAndDecrement(amount, Integer.MIN_VALUE);
    }

    /**
     * Returns the value within this counter and then decrements it by an amount
     * of {@code 1}.
     *
     * @return the value before it is decremented.
     */
    public int getAndDecrement() {
        return getAndDecrement(1);
    }

    /**
     * Decrements the value within this counter by {@code amount} to a minimum
     * of {@code minimum} and then returns it.
     *
     * @param amount
     *            the amount to decrement it by.
     * @param minimum
     *            the minimum amount it will be decremented to.
     * @return the value after it is decremented.
     */
    public int decrementAndGet(int amount, int minimum) {
        value -= amount;
        if (value < minimum)
            value = minimum;
        return value;
    }

    /**
     * Decrements the value within this counter by {@code amount} and then
     * returns it.
     *
     * @param amount
     *            the amount to decrement it by.
     * @return the value after it is decremented.
     */
    public int decrementAndGet(int amount) {
        return decrementAndGet(amount, Integer.MIN_VALUE);
    }

    /**
     * Decrements the value within this counter by {@code 1} and then returns
     * it.
     *
     * @return the value after it is decremented.
     */
    public int decrementAndGet() {
        return decrementAndGet(1);
    }

    /**
     * Gets the value present within this counter. This function equates to the
     * inherited {@link MutableNumber#intValue()} function.
     *
     * @return the value present.
     */
    public int get() {
        return value;
    }

    /**
     * Sets the value within this container to {@code value}.
     *
     * @param value
     *            the new value to set.
     */
    public void set(int value) {
        this.value = value;
    }
}
