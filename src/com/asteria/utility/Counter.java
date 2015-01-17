package com.asteria.utility;

/**
 * The container class that contains functions to modify a number.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public final class Counter {

    /**
     * The value present within this counter.
     */
    private int value;

    /**
     * Creates a new {@link Counter} with {@code value}.
     * 
     * @param value
     *            the value present within this counter.
     */
    public Counter(int value) {
        this.value = value;
    }

    /**
     * Creates a new {@link Counter} with a value of {@code 0}.
     */
    public Counter() {
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
        if (!(obj instanceof Counter))
            return false;
        Counter other = (Counter) obj;
        if (value != other.value)
            return false;
        return true;
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
     * Gets the value present within this counter.
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
