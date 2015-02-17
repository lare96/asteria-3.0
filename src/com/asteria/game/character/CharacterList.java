package com.asteria.game.character;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * A collection that provides functionality for storing and managing characters.
 * This list does not support the storage of elements with a value of
 * {@code null}, and maintains an extremely strict ordering of the elements.
 * 
 * @author lare96 <http://www.rune-server.org/members/lare96/>
 * @param <E>
 *            the type of character being managed with this collection.
 */
public final class CharacterList<E extends CharacterNode> implements Iterable<E> {

    /**
     * The backing array of {@link CharacterNode}s within this collection.
     */
    private E[] characters;

    /**
     * The finite capacity of this collection.
     */
    private final int capacity;

    /**
     * The size of this collection.
     */
    private int size;

    /**
     * The last slot that a {@link CharacterNode} was removed from.
     */
    private int lastSlot = -1;

    /**
     * Creates a new {@link CharacterList}.
     * 
     * @param cap
     *            the finite capacity of this collection.
     */
    @SuppressWarnings("unchecked")
    public CharacterList(int capacity) {
        this.capacity = ++capacity;
        this.characters = (E[]) new CharacterNode[capacity];
        this.size = 0;
    }

    /**
     * Adds an element to this collection.
     * 
     * @param e
     *            the element to add to this collection.
     * @return {@code true} if the element was successfully added, {@code false}
     *         otherwise.
     */
    public boolean add(E e) {
        Objects.requireNonNull(e);

        if (!e.isRegistered()) {
            int slot = slotSearch();
            if (slot < 0)
                return false;
            e.setRegistered(true);
            e.setSlot(slot);
            characters[slot] = e;
            e.create();
            size++;
            return true;
        }
        return false;
    }

    /**
     * Removes an element from this collection.
     * 
     * @param e
     *            the element to remove from this collection.
     * @return {@code true} if the element was successfully removed,
     *         {@code false} otherwise.
     */
    public boolean remove(E e) {
        Objects.requireNonNull(e);

        if (e.isRegistered() && characters[e.getSlot()] != null) {
            e.setRegistered(false);
            e.dispose();
            characters[e.getSlot()] = null;
            lastSlot = e.getSlot();
            size--;
            return true;
        }
        return false;
    }

    /**
     * Determines if this collection contains the specified element.
     * 
     * @param e
     *            the element to determine if this collection contains.
     * @return {@code true} if this collection contains the element,
     *         {@code false} otherwise.
     */
    public boolean contains(E e) {
        Objects.requireNonNull(e);
        return characters[e.getSlot()] != null;
    }

    /**
     * {@inheritDoc}
     * 
     * This implementation will exclude all elements with a value of
     * {@code null} to avoid {@link NullPointerException}s.
     */
    @Override
    public void forEach(Consumer<? super E> action) {
        for (E e : characters) {
            if (e == null)
                continue;
            action.accept(e);
        }
    }

    @Override
    public Spliterator<E> spliterator() {
        return Spliterators.spliterator(characters, Spliterator.ORDERED);
    }

    /**
     * Searches the backing array for the first element encountered that matches
     * {@code filter}. This does not include elements with a value of
     * {@code null}.
     * 
     * @param filter
     *            the predicate that the search will be based on.
     * @return an optional holding the found element, or an empty optional if no
     *         element was found.
     */
    public Optional<E> search(Predicate<? super E> filter) {
        for (E e : characters) {
            if (e == null)
                continue;
            if (filter.test(e))
                return Optional.of(e);
        }
        return Optional.empty();
    }

    @Override
    public Iterator<E> iterator() {
        return new CharacterListIterator<>(this);
    }

    /**
     * Retrieves the element on the given slot.
     * 
     * @param slot
     *            the slot to retrieve the element on.
     * @return the element on the given slot or {@code null} if no element is on
     *         the spot.
     */
    public E get(int slot) {
        return characters[slot];
    }

    /**
     * Searches through the backing array for the next available slot. This
     * method will perform in constant time if a {@link CharacterNode} was
     * removed from this collection after the last search.
     * 
     * @return the found slot, or -1 if no slot is available.
     */
    private int slotSearch() {
        if (lastSlot < 1) {
            for (int slot = 1; slot < capacity; slot++) {
                if (characters[slot] == null) {
                    return slot;
                }
            }
            return -1;
        }
        int found = lastSlot;
        lastSlot = -1;
        return found;
    }

    /**
     * Determines the amount of elements stored in this collection.
     * 
     * @return the amount of elements stored in this collection.
     */
    public int size() {
        return size;
    }

    /**
     * Gets the finite capacity of this collection.
     * 
     * @return the finite capacity of this collection.
     */
    public int capacity() {
        return capacity;
    }

    /**
     * Gets the remaining amount of space in this collection.
     * 
     * @return the remaining amount of space in this collection.
     */
    public int spaceLeft() {
        return capacity - size;
    }

    /**
     * Returns a sequential stream with this collection as its source.
     * 
     * @return a sequential stream over the elements in this collection.
     */
    public Stream<E> stream() {
        return Arrays.stream(characters);
    }

    /**
     * Removes all of the elements in this collection and resets the
     * {@link CharacterList#characters} and {@link CharacterList#size}.
     */
    @SuppressWarnings("unchecked")
    public void clear() {
        forEach(this::remove);
        characters = (E[]) new CharacterNode[capacity];
        size = 0;
    }

    /**
     * An {@link Iterator} implementation that will iterate over the elements in
     * a character list.
     * 
     * @author lare96 <http://www.rune-server.org/members/lare96/>
     * @param <E>
     *            the type of character being iterated over.
     */
    private static final class CharacterListIterator<E extends CharacterNode> implements Iterator<E> {

        /**
         * The {@link CharacterList} that is storing the elements.
         */
        private final CharacterList<E> list;

        /**
         * The current index that the iterator is iterating over.
         */
        private int index;

        /**
         * The last index that the iterator iterated over.
         */
        private int lastIndex = -1;

        /**
         * Creates a new {@link CharacterListIterator}.
         * 
         * @param list
         *            the list that is storing the elements.
         */
        public CharacterListIterator(CharacterList<E> list) {
            this.list = list;
        }

        @Override
        public boolean hasNext() {
            return !(index + 1 > list.capacity());
        }

        @Override
        public E next() {
            if (index >= list.capacity()) {
                throw new ArrayIndexOutOfBoundsException("There are no elements left to iterate over!");
            }

            lastIndex = index;
            index++;
            return list.characters[lastIndex];
        }

        @Override
        public void remove() {
            if (lastIndex == -1) {
                throw new IllegalStateException("This method can only be called once after \"next\".");
            }
            list.remove(list.characters[lastIndex]);
            index = lastIndex;
            lastIndex = -1;
        }
    }
}