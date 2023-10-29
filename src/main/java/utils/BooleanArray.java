package utils;

import math.Maths;

/**
 * Dynamic array of boolean values. Similar to Java's ArrayList but optimized for boolean data.
 * <p>
 * The {@code BooleanArray} class provides a resizable array to store boolean values efficiently. It is designed to be memory-efficient
 * and can grow dynamically as needed.
 * </p>
 * <p>
 * You can choose whether the array should be ordered or unordered. Ordered arrays maintain the order of elements when removing items,
 * while unordered arrays do not guarantee order preservation when removing elements.
 * </p>
 * <p>
 * The capacity of the array can be specified when creating it, and it automatically grows when additional elements are added beyond
 * the current capacity.
 * </p>
 *
 * @see BooleanArray#BooleanArray()
 * @see BooleanArray#BooleanArray(int)
 * @see BooleanArray#BooleanArray(boolean, int)
 * @see BooleanArray#BooleanArray(BooleanArray)
 * @see BooleanArray#BooleanArray(boolean[])
 * @see BooleanArray#BooleanArray(boolean, boolean[], int, int)
 */
public class BooleanArray {
    /**
     * The array of boolean values.
     */
    public boolean[] items;
    /**
     * The number of elements currently in the array.
     */
    public int size;
    /**
     * Indicates whether the array is ordered or not.
     * <p>
     * If set to false, methods that remove elements may change the order of other elements in the array to avoid memory copying.
     * </p>
     */
    public boolean ordered;


    /**
     * Creates an ordered array with a default capacity of 16.
     *
     * @see BooleanArray#BooleanArray(boolean, int)
     */
    public BooleanArray() {
        this(true, 16);
    }

    /**
     * Creates an ordered array with the specified initial capacity.
     *
     * @param capacity The initial capacity of the array.
     * @see BooleanArray#BooleanArray(boolean, int)
     */
    public BooleanArray(int capacity) {
        this(true, capacity);
    }

    /**
     * Creates a new BooleanArray with the specified ordered state and capacity.
     *
     * @param ordered  If false, methods that remove elements may change the order of other elements in the array, which avoids a
     *                 memory copy.
     * @param capacity Any elements added beyond this will cause the backing array to be grown.
     */
    public BooleanArray(boolean ordered, int capacity) {
        this.ordered = ordered;
        items = new boolean[capacity];
    }

    /**
     * Creates a new array containing the elements from a specific BooleanArray.
     *
     * @param array The BooleanArray to copy elements from.
     */
    public BooleanArray(BooleanArray array) {
        this.ordered = array.ordered;
        size = array.size;
        items = new boolean[size];
        System.arraycopy(array.items, 0, items, 0, size);
    }

    /**
     * Creates a new ordered array containing the elements from a specified boolean array.
     *
     * @param array The boolean array to copy elements from.
     */
    public BooleanArray(boolean[] array) {
        this(true, array, 0, array.length);
    }

    /**
     * Creates a new array containing the elements from a specified boolean array.
     *
     * @param ordered    If false, methods that remove elements may change the order of other elements in the array, which avoids a
     *                   memory copy.
     * @param array      The boolean array to copy elements from.
     * @param startIndex The index to start copying from in the source array.
     * @param count      The number of elements to copy from the source array.
     */
    public BooleanArray(boolean ordered, boolean[] array, int startIndex, int count) {
        this(ordered, count);
        size = count;
        System.arraycopy(array, startIndex, items, 0, count);
    }

    /**
     * Creates a new `BooleanArray` from a given boolean array.
     *
     * @param array The boolean array to create a `BooleanArray` from.
     * @return A new `BooleanArray` containing the elements from the given boolean array.
     * @see #BooleanArray(boolean[])
     */
    public static BooleanArray with(boolean... array) {
        return new BooleanArray(array);
    }

    /**
     * Adds a boolean value to the end of the array.
     *
     * @param value The boolean value to add.
     */
    public void add(boolean value) {
        boolean[] items = this.items;
        if (size == items.length) items = resize(Math.max(8, (int) (size * 1.75f)));
        items[size++] = value;
    }

    /**
     * Adds two boolean values to the end of the array.
     *
     * @param value1 The first boolean value to add.
     * @param value2 The second boolean value to add.
     */
    public void add(boolean value1, boolean value2) {
        boolean[] items = this.items;
        if (size + 1 >= items.length) items = resize(Math.max(8, (int) (size * 1.75f)));
        items[size] = value1;
        items[size + 1] = value2;
        size += 2;
    }

    /**
     * Adds three boolean values to the end of the array.
     *
     * @param value1 The first boolean value to add.
     * @param value2 The second boolean value to add.
     * @param value3 The third boolean value to add.
     */
    public void add(boolean value1, boolean value2, boolean value3) {
        boolean[] items = this.items;
        if (size + 2 >= items.length) items = resize(Math.max(8, (int) (size * 1.75f)));
        items[size] = value1;
        items[size + 1] = value2;
        items[size + 2] = value3;
        size += 3;
    }

    /**
     * Adds four boolean values to the end of the array.
     *
     * @param value1 The first boolean value to add.
     * @param value2 The second boolean value to add.
     * @param value3 The third boolean value to add.
     * @param value4 The fourth boolean value to add.
     */
    public void add(boolean value1, boolean value2, boolean value3, boolean value4) {
        boolean[] items = this.items;
        if (size + 3 >= items.length)
            items = resize(Math.max(8, (int) (size * 1.8f))); // 1.75 isn't enough when size=5.
        items[size] = value1;
        items[size + 1] = value2;
        items[size + 2] = value3;
        items[size + 3] = value4;
        size += 4;
    }

    /**
     * Adds all the elements from the provided {@code BooleanArray} to the end of this array.
     *
     * @param array The source BooleanArray whose elements to add.
     * @see BooleanArray#addAll(boolean[], int, int)
     */
    public void addAll(BooleanArray array) {
        addAll(array.items, 0, array.size);
    }

    /**
     * Adds a specified range of elements from the provided {@code BooleanArray} to the end of this array.
     *
     * @param array  The source BooleanArray whose elements to add.
     * @param offset The starting index in the source array.
     * @param length The number of elements to add from the source array.
     * @throws IllegalArgumentException If the sum of offset and length is greater than the size of the source array.
     * @see BooleanArray#addAll(boolean[], int, int)
     */
    public void addAll(BooleanArray array, int offset, int length) {
        if (offset + length > array.size)
            throw new IllegalArgumentException("offset + length must be <= size: " + offset + " + " + length + " <= " + array.size);
        addAll(array.items, offset, length);
    }

    /**
     * Adds all the elements from the provided boolean array to the end of this array.
     *
     * @param array The source boolean array whose elements to add.
     * @see BooleanArray#addAll(boolean[], int, int)
     */
    public void addAll(boolean... array) {
        addAll(array, 0, array.length);
    }

    /**
     * Adds a specified range of elements from the provided boolean array to the end of this array.
     *
     * @param array  The source boolean array whose elements to add.
     * @param offset The starting index in the source array.
     * @param length The number of elements to add from the source array.
     * @throws IllegalArgumentException If the sum of offset and length is greater than the size of the source array.
     */
    public void addAll(boolean[] array, int offset, int length) {
        boolean[] items = this.items;
        int sizeNeeded = size + length;
        if (sizeNeeded > items.length) items = resize(Math.max(Math.max(8, sizeNeeded), (int) (size * 1.75f)));
        System.arraycopy(array, offset, items, size, length);
        size += length;
    }

    /**
     * Retrieves the boolean value at the specified index in the array.
     *
     * @param index The index of the boolean value to retrieve.
     * @return The boolean value at the specified index.
     * @throws IndexOutOfBoundsException If the index is greater than or equal to the size of the array.
     */
    public boolean get(int index) {
        if (index >= size) throw new IndexOutOfBoundsException("index can't be >= size: " + index + " >= " + size);
        return items[index];
    }

    /**
     * Sets the boolean value at the specified index in the array.
     *
     * @param index The index at which to set the boolean value.
     * @param value The boolean value to set.
     * @throws IndexOutOfBoundsException If the index is greater than or equal to the size of the array.
     */
    public void set(int index, boolean value) {
        if (index >= size) throw new IndexOutOfBoundsException("index can't be >= size: " + index + " >= " + size);
        items[index] = value;
    }

    /**
     * Inserts a boolean value at the specified index in the array. Existing elements at and after the index will be shifted to accommodate the new value.
     *
     * @param index The index at which to insert the boolean value.
     * @param value The boolean value to insert.
     * @throws IndexOutOfBoundsException If the index is greater than the current size of the array.
     */
    public void insert(int index, boolean value) {
        if (index > size) throw new IndexOutOfBoundsException("index can't be > size: " + index + " > " + size);
        boolean[] items = this.items;
        if (size == items.length) items = resize(Math.max(8, (int) (size * 1.75f)));
        if (ordered)
            System.arraycopy(items, index, items, index + 1, size - index);
        else
            items[size] = items[index];
        size++;
        items[index] = value;
    }

    /**
     * Inserts a specified number of boolean values at the specified index in the array. The new values will have values equal to the values at those indices before the insertion.
     *
     * @param index The index at which to insert the boolean values.
     * @param count The number of boolean values to insert.
     * @throws IndexOutOfBoundsException If the index is greater than the current size of the array.
     */
    public void insertRange(int index, int count) {
        if (index > size) throw new IndexOutOfBoundsException("index can't be > size: " + index + " > " + size);
        int sizeNeeded = size + count;
        if (sizeNeeded > items.length) items = resize(Math.max(Math.max(8, sizeNeeded), (int) (size * 1.75f)));
        System.arraycopy(items, index, items, index + count, size - index);
        size = sizeNeeded;
    }

    /**
     * Swaps the positions of two boolean values in the array.
     *
     * @param first  The index of the first boolean value to swap.
     * @param second The index of the second boolean value to swap.
     * @throws IndexOutOfBoundsException If either index is greater than or equal to the size of the array.
     */
    public void swap(int first, int second) {
        if (first >= size) throw new IndexOutOfBoundsException("first can't be >= size: " + first + " >= " + size);
        if (second >= size) throw new IndexOutOfBoundsException("second can't be >= size: " + second + " >= " + size);
        boolean[] items = this.items;
        boolean firstValue = items[first];
        items[first] = items[second];
        items[second] = firstValue;
    }

    /**
     * Removes and returns the boolean value at the specified index in the array.
     *
     * @param index The index of the boolean value to remove.
     * @return The removed boolean value.
     * @throws IndexOutOfBoundsException If the index is greater than or equal to the size of the array.
     */
    public boolean removeIndex(int index) {
        if (index >= size) throw new IndexOutOfBoundsException("index can't be >= size: " + index + " >= " + size);
        boolean[] items = this.items;
        boolean value = items[index];
        size--;
        if (ordered)
            System.arraycopy(items, index + 1, items, index, size - index);
        else
            items[index] = items[size];
        return value;
    }

    /**
     * Removes all boolean values between the specified indices, inclusive.
     *
     * @param start The index from which to start removing boolean values.
     * @param end   The index at which to stop removing boolean values.
     * @throws IndexOutOfBoundsException If the end index is greater than or equal to the size of the array, or if the start index is greater than the end index.
     */
    public void removeRange(int start, int end) {
        int n = size;
        if (end >= n) throw new IndexOutOfBoundsException("end can't be >= size: " + end + " >= " + size);
        if (start > end) throw new IndexOutOfBoundsException("start can't be > end: " + start + " > " + end);
        int count = end - start + 1, lastIndex = n - count;
        if (ordered)
            System.arraycopy(items, start + count, items, start, n - (start + count));
        else {
            int i = Math.max(lastIndex, end + 1);
            System.arraycopy(items, i, items, start, n - i);
        }
        size = n - count;
    }

    /**
     * Removes all elements from this array that are also present in the specified {@code BooleanArray}.
     *
     * @param array The {@code BooleanArray} containing the elements to be removed.
     * @return {@code true} if this array was modified.
     */
    public boolean removeAll(BooleanArray array) {
        int size = this.size;
        int startSize = size;
        boolean[] items = this.items;
        for (int i = 0, n = array.size; i < n; i++) {
            boolean item = array.get(i);
            for (int ii = 0; ii < size; ii++) {
                if (item == items[ii]) {
                    removeIndex(ii);
                    size--;
                    break;
                }
            }
        }
        return size != startSize;
    }

    /**
     * Removes and returns the last boolean value in the array.
     *
     * @return The removed last boolean value.
     */
    public boolean pop() {
        return items[--size];
    }

    /**
     * Returns the last boolean value in the array.
     *
     * @return The last boolean value.
     */
    public boolean peek() {
        return items[size - 1];
    }

    /**
     * Returns the first boolean value in the array.
     *
     * @return The first boolean value.
     * @throws IllegalStateException If the array is empty.
     */
    public boolean first() {
        if (size == 0) throw new IllegalStateException("Array is empty.");
        return items[0];
    }

    /**
     * Checks if the array has one or more boolean values.
     *
     * @return {@code true} if the array has one or more items, {@code false} otherwise.
     */
    public boolean notEmpty() {
        return size > 0;
    }

    /**
     * Checks if the array is empty.
     *
     * @return {@code true} if the array is empty, {@code false} otherwise.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Clears the array, setting its size to 0.
     */
    public void clear() {
        size = 0;
    }

    /**
     * Reduces the size of the backing array to match the actual number of items. Useful for memory optimization when many items
     * have been removed.
     *
     * @return The internal boolean array after resizing.
     */
    public boolean[] shrink() {
        if (items.length != size) resize(size);
        return items;
    }

    /**
     * Increases the size of the backing array to accommodate the specified additional capacity.
     *
     * @param additionalCapacity The additional capacity to ensure.
     * @return The internal boolean array after resizing.
     * @throws IllegalArgumentException If additionalCapacity is less than 0.
     */
    public boolean[] ensureCapacity(int additionalCapacity) {
        if (additionalCapacity < 0)
            throw new IllegalArgumentException("additionalCapacity must be >= 0: " + additionalCapacity);
        int sizeNeeded = size + additionalCapacity;
        if (sizeNeeded > items.length) resize(Math.max(Math.max(8, sizeNeeded), (int) (size * 1.75f)));
        return items;
    }

    /**
     * Sets the size of the array, leaving any values beyond the new size undefined.
     *
     * @param newSize The new size to set.
     * @return The internal boolean array after resizing.
     * @throws IllegalArgumentException If newSize is less than 0.
     */
    public boolean[] setSize(int newSize) {
        if (newSize < 0) throw new IllegalArgumentException("newSize must be >= 0: " + newSize);
        if (newSize > items.length) resize(Math.max(8, newSize));
        size = newSize;
        return items;
    }

    /**
     * Resizes the internal array to the specified new size.
     *
     * @param newSize The new size for the internal array.
     * @return The internal boolean array after resizing.
     */
    protected boolean[] resize(int newSize) {
        boolean[] newItems = new boolean[newSize];
        boolean[] items = this.items;
        System.arraycopy(items, 0, newItems, 0, Math.min(size, newItems.length));
        this.items = newItems;
        return newItems;
    }

    /**
     * Reverses the order of elements in the array.
     */
    public void reverse() {
        boolean[] items = this.items;
        for (int i = 0, lastIndex = size - 1, n = size / 2; i < n; i++) {
            int ii = lastIndex - i;
            boolean temp = items[i];
            items[i] = items[ii];
            items[ii] = temp;
        }
    }

    /**
     * Shuffles the elements in the array into a random order.
     */
    public void shuffle() {
        boolean[] items = this.items;
        for (int i = size - 1; i >= 0; i--) {
            int ii = Maths.random(i);
            boolean temp = items[i];
            items[i] = items[ii];
            items[ii] = temp;
        }
    }

    /**
     * Reduces the size of the array to the specified size. If the array is already smaller than the specified size, no action is
     * taken.
     *
     * @param newSize The new size to truncate the array to.
     */
    public void truncate(int newSize) {
        if (size > newSize) size = newSize;
    }

    /**
     * Returns a random boolean item from the array, or false if the array is empty.
     *
     * @return A random boolean item or false if the array is empty.
     */
    public boolean random() {
        if (size == 0) return false;
        return items[Maths.random(0, size - 1)];
    }

    /**
     * Converts the `BooleanArray` into a new boolean array.
     *
     * @return A new boolean array containing the elements from the `BooleanArray`.
     */
    public boolean[] toArray() {
        boolean[] array = new boolean[size];
        System.arraycopy(items, 0, array, 0, size);
        return array;
    }

    /**
     * Computes the hash code of the `BooleanArray`. The hash code is computed based on the order of elements in the array.
     *
     * @return The computed hash code.
     */
    public int hashCode() {
        if (!ordered) return super.hashCode();
        boolean[] items = this.items;
        int h = 1;
        for (int i = 0, n = size; i < n; i++)
            h = h * 31 + (items[i] ? 1231 : 1237);
        return h;
    }

    /**
     * Checks if another object is equal to this `BooleanArray`. Two `BooleanArray` objects are equal if they have the same
     * elements in the same order.
     *
     * @param object The object to compare to this `BooleanArray`.
     * @return `true` if the objects are equal, `false` otherwise.
     */
    public boolean equals(Object object) {
        if (object == this) return true;
        if (!ordered) return false;
        if (!(object instanceof BooleanArray)) return false;
        BooleanArray array = (BooleanArray) object;
        if (!array.ordered) return false;
        int n = size;
        if (n != array.size) return false;
        boolean[] items1 = this.items, items2 = array.items;
        for (int i = 0; i < n; i++)
            if (items1[i] != items2[i]) return false;
        return true;
    }

    /**
     * Returns a string representation of the `BooleanArray`, with elements enclosed in square brackets and separated by commas.
     *
     * @return A string representation of the `BooleanArray`.
     */
    public String toString() {
        if (size == 0) return "[]";
        boolean[] items = this.items;
        StringBuilder buffer = new StringBuilder(32);
        buffer.append('[');
        buffer.append(items[0]);
        for (int i = 1; i < size; i++) {
            buffer.append(", ");
            buffer.append(items[i]);
        }
        buffer.append(']');
        return buffer.toString();
    }

    /**
     * Returns a string representation of the `BooleanArray`, with elements separated by a specified separator.
     *
     * @param separator The separator to use between elements.
     * @return A string representation of the `BooleanArray` with the specified separator.
     */
    public String toString(String separator) {
        if (size == 0) return "";
        boolean[] items = this.items;
        StringBuilder buffer = new StringBuilder(32);
        buffer.append(items[0]);
        for (int i = 1; i < size; i++) {
            buffer.append(separator);
            buffer.append(items[i]);
        }
        return buffer.toString();
    }
}
