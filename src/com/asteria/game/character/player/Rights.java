package com.asteria.game.character.player;

/**
 * The enumerated type whose elements represent the types of authority a player
 * can have.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 */
public enum Rights {
    PLAYER(0, 0),
    DONATOR(0, 0),
    VETERAN(0, 0),
    MODERATOR(1, 1),
    ADMINISTRATOR(2, 2),
    DEVELOPER(2, 3);

    /**
     * The value of this rank as seen by the protocol. The only ranks the
     * protocol sees are:<br>
     * <br>
     * <table BORDER CELLPADDING=3 CELLSPACING=1>
     * <tr>
     * <td></td>
     * <td ALIGN=CENTER><em>Protocol Value</em></td>
     * </tr>
     * <tr>
     * <td>Player</td>
     * <td>0</td>
     * </tr>
     * <tr>
     * <td>Moderator</td>
     * <td>1</td>
     * </tr>
     * <tr>
     * <td>Administrator</td>
     * <td>2</td>
     * </tr>
     * </table>
     */
    private final int protocolValue;

    /**
     * The value of this rank as seen by the server. This value will be used to
     * determine which of the elements are greater than each other.
     */
    private final int value;

    /**
     * Create a new {@link Rights}.
     * 
     * @param protocolValue
     *            the value of this rank as seen by the protocol.
     * @param value
     *            the value of this rank as seen by the server.
     */
    private Rights(int protocolValue, int value) {
        this.protocolValue = protocolValue;
        this.value = value;
    }

    /**
     * Determines if this right is greater than the argued right. Please note
     * that this method <b>does not</b> compare the Objects themselves, but
     * instead compares the value behind them as specified by {@code value} in
     * the enumerated type.
     * 
     * @param other
     *            the argued right to compare.
     * @return {@code true} if this right is greater, {@code false} otherwise.
     */
    public final boolean greater(Rights other) {
        return value > other.value;
    }

    /**
     * Determines if this right is lesser than the argued right. Please note
     * that this method <b>does not</b> compare the Objects themselves, but
     * instead compares the value behind them as specified by {@code value} in
     * the enumerated type.
     * 
     * @param other
     *            the argued right to compare.
     * @return {@code true} if this right is lesser, {@code false} otherwise.
     */
    public final boolean less(Rights other) {
        return value < other.value;
    }

    /**
     * Determines if this right is equal in power to the argued right. Please
     * note that this method <b>does not</b> compare the Objects themselves, but
     * instead compares the value behind them as specified by {@code value} in
     * the enumerated type.
     * 
     * @param other
     *            the argued right to compare.
     * @return {@code true} if this right is equal, {@code false} otherwise.
     */
    public final boolean equal(Rights other) {
        return value == other.value;
    }

    /**
     * Gets the value of this rank as seen by the protocol.
     * 
     * @return the protocol value of this rank.
     */
    public final int getProtocolValue() {
        return protocolValue;
    }

    /**
     * Gets the value of this rank as seen by the server.
     * 
     * @return the server value of this rank.
     */
    public final int getValue() {
        return value;
    }
}
