package com.asteria.game.character.player;

import com.google.common.base.Preconditions;

/**
 * The container class that contains functions to handle the appearance of a
 * {@link Player}.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public final class Appearance {

    /**
     * The player appearance update male gender identifier.
     */
    public static final int GENDER_MALE = 0;

    /**
     * The player appearance update female gender identifier.
     */
    public static final int GENDER_FEMALE = 1;

    /**
     * The gender value for the appearance of a player.
     */
    private int gender;

    /**
     * The chest value for the appearance of a player.
     */
    private int chest;

    /**
     * The arms value for the appearance of a player.
     */
    private int arms;

    /**
     * The legs value for the appearance of a player.
     */
    private int legs;

    /**
     * The head value for the appearance of a player.
     */
    private int head;

    /**
     * The hands value for the appearance of a player.
     */
    private int hands;

    /**
     * The feet value for the appearance of a player.
     */
    private int feet;

    /**
     * The beard value for the appearance of a player.
     */
    private int beard;

    /**
     * The hair color value for the appearance of a player.
     */
    private int hairColor;

    /**
     * The torso color value for the appearance of a player.
     */
    private int torsoColor;

    /**
     * The leg color value for the appearance of a player.
     */
    private int legColor;

    /**
     * The feet color value for the appearance of a player.
     */
    private int feetColor;

    /**
     * The skin color value for the appearance of a player.
     */
    private int skinColor;

    /**
     * Creates a new {@link Appearance} with the default appearance values.
     */
    public Appearance() {
        this.gender = Appearance.GENDER_MALE;
        this.chest = 18;
        this.arms = 26;
        this.legs = 36;
        this.head = 0;
        this.hands = 33;
        this.feet = 42;
        this.beard = 10;
        this.hairColor = 7;
        this.torsoColor = 8;
        this.legColor = 9;
        this.feetColor = 5;
        this.skinColor = 0;
    }

    /**
     * Determines if the gender value equates to a male.
     * 
     * @return {@code true} if the gender value is a male, {@code false}
     *         otherwise.
     */
    public boolean isMale() {
        return gender == Appearance.GENDER_MALE;
    }

    /**
     * Determines if the gender value equates to a female.
     * 
     * @return {@code true} if the gender value is a female, {@code false}
     *         otherwise.
     */
    public boolean isFemale() {
        return gender == Appearance.GENDER_FEMALE;
    }

    /**
     * Sets the appearance values in this container to the ones in
     * {@code values}.
     * 
     * @param values
     *            the array of appearance values that will be set.
     */
    public void setValues(int[] values) {
        Preconditions.checkArgument(values.length == 13);
        gender = values[0];
        head = values[1];
        beard = values[2];
        chest = values[3];
        arms = values[4];
        hands = values[5];
        legs = values[6];
        feet = values[7];
        hairColor = values[8];
        torsoColor = values[9];
        legColor = values[10];
        feetColor = values[11];
        skinColor = values[12];
    }

    /**
     * Constructs an array that holds all of the appearance values in this
     * container.
     * 
     * @return the array of appearance values.
     */
    public int[] getValues() {
        return new int[] { gender, head, beard, chest, arms, hands, legs, feet, hairColor, torsoColor, legColor, feetColor,
                skinColor };
    }

    /**
     * Gets the gender value for the appearance of a player.
     * 
     * @return the gender value.
     */
    public int getGender() {
        return gender;
    }

    /**
     * Sets the value for {@link Appearance#gender}.
     * 
     * @param gender
     *            the new value to set.
     */
    public void setGender(int gender) {
        this.gender = gender;
    }

    /**
     * Gets the chest value for the appearance of a player.
     * 
     * @return the chest value.
     */
    public int getChest() {
        return chest;
    }

    /**
     * Sets the value for {@link Appearance#chest}.
     * 
     * @param chest
     *            the new value to set.
     */
    public void setChest(int chest) {
        this.chest = chest;
    }

    /**
     * Gets the arms value for the appearance of a player.
     * 
     * @return the arms value.
     */
    public int getArms() {
        return arms;
    }

    /**
     * Sets the value for {@link Appearance#arms}.
     * 
     * @param arms
     *            the new value to set.
     */
    public void setArms(int arms) {
        this.arms = arms;
    }

    /**
     * Gets the legs value for the appearance of a player.
     * 
     * @return the legs value.
     */
    public int getLegs() {
        return legs;
    }

    /**
     * Sets the value for {@link Appearance#legs}.
     * 
     * @param legs
     *            the new value to set.
     */
    public void setLegs(int legs) {
        this.legs = legs;
    }

    /**
     * Gets the head value for the appearance of a player.
     * 
     * @return the head value.
     */
    public int getHead() {
        return head;
    }

    /**
     * Sets the value for {@link Appearance#head}.
     * 
     * @param head
     *            the new value to set.
     */
    public void setHead(int head) {
        this.head = head;
    }

    /**
     * Gets the hands value for the appearance of a player.
     * 
     * @return the hands value.
     */
    public int getHands() {
        return hands;
    }

    /**
     * Sets the value for {@link Appearance#hands}.
     * 
     * @param hands
     *            the new value to set.
     */
    public void setHands(int hands) {
        this.hands = hands;
    }

    /**
     * Gets the feet value for the appearance of a player.
     * 
     * @return the feet value.
     */
    public int getFeet() {
        return feet;
    }

    /**
     * Sets the value for {@link Appearance#feet}.
     * 
     * @param feet
     *            the new value to set.
     */
    public void setFeet(int feet) {
        this.feet = feet;
    }

    /**
     * Gets the beard value for the appearance of a player.
     * 
     * @return the beard value.
     */
    public int getBeard() {
        return beard;
    }

    /**
     * Sets the value for {@link Appearance#beard}.
     * 
     * @param beard
     *            the new value to set.
     */
    public void setBeard(int beard) {
        this.beard = beard;
    }

    /**
     * Gets the hair color value for the appearance of a player.
     * 
     * @return the hair color value.
     */
    public int getHairColor() {
        return hairColor;
    }

    /**
     * Sets the value for {@link Appearance#hairColor}.
     * 
     * @param hairColor
     *            the new value to set.
     */
    public void setHairColor(int hairColor) {
        this.hairColor = hairColor;
    }

    /**
     * Gets the torso color value for the appearance of a player.
     * 
     * @return the torso color value.
     */
    public int getTorsoColor() {
        return torsoColor;
    }

    /**
     * Sets the value for {@link Appearance#torsoColor}.
     * 
     * @param torsoColor
     *            the new value to set.
     */
    public void setTorsoColor(int torsoColor) {
        this.torsoColor = torsoColor;
    }

    /**
     * Gets the leg color value for the appearance of a player.
     * 
     * @return the leg color value.
     */
    public int getLegColor() {
        return legColor;
    }

    /**
     * Sets the value for {@link Appearance#legColor}.
     * 
     * @param legColor
     *            the new value to set.
     */
    public void setLegColor(int legColor) {
        this.legColor = legColor;
    }

    /**
     * Gets the feet color value for the appearance of a player.
     * 
     * @return the feet color value.
     */
    public int getFeetColor() {
        return feetColor;
    }

    /**
     * Sets the value for {@link Appearance#feetColor}.
     * 
     * @param feetColor
     *            the new value to set.
     */
    public void setFeetColor(int feetColor) {
        this.feetColor = feetColor;
    }

    /**
     * Gets  the skin color value for the appearance of a player.
     * 
     * @return the skin color value.
     */
    public int getSkinColor() {
        return skinColor;
    }

    /**
     * Sets the value for {@link Appearance#skinColor}.
     * 
     * @param skinColor
     *            the new value to set.
     */
    public void setSkinColor(int skinColor) {
        this.skinColor = skinColor;
    }
}