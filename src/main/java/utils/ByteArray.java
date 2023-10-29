package utils;

import math.Maths;

import java.util.Arrays;

/**
 * A dynamic array for storing bytes. Supports ordered and unordered storage.
 */
public class ByteArray {
    public byte[] items;
    public int size;
    public boolean ordered;

    /**
     * Creates an ordered array with a default capacity of 16.
     */
    public ByteArray() {
        this(true, 16);
    }

    /**
     * Creates an ordered array with the specified capacity.
     *
     * @param capacity Initial capacity of the array.
     */
    public ByteArray(int capacity) {
        this(true, capacity);
    }

    /**
     * Creates an array with the specified ordering and capacity.
     *
     * @param ordered  If false, removing elements may change the order of other elements.
     * @param capacity Initial capacity of the array.
     */
    public ByteArray(boolean ordered, int capacity) {
        this.ordered = ordered;
        items = new byte[capacity];
    }

    /**
     * Creates a new array by copying elements from the given array.
     *
     * @param array The array to copy elements from.
     */
    public ByteArray(ByteArray array) {
        this.ordered = array.ordered;
        size = array.size;
        items = new byte[size];
        System.arraycopy(array.items, 0, items, 0, size);
    }

    /**
     * Creates a new ordered array by copying elements from the given native array.
     *
     * @param array The native array to copy elements from.
     */
    public ByteArray(byte[] array) {
        this(true, array, 0, array.length);
    }

    /**
     * Creates a new array by copying a subset of elements from the given native array.
     *
     * @param ordered    If false, removing elements may change the order of other elements.
     * @param array      The native array to copy elements from.
     * @param startIndex The starting index to copy from.
     * @param count      The number of elements to copy.
     */
    public ByteArray(boolean ordered, byte[] array, int startIndex, int count) {
        this(ordered, count);
        size = count;
        System.arraycopy(array, startIndex, items, 0, count);
    }

    /**
     * Factory method to create a new ByteArray with the given elements.
     *
     * @param array Elements to initialize the array with.
     * @return A new ByteArray containing the given elements.
     */
    static public ByteArray with(byte... array) {
        return new ByteArray(array);
    }

    /**
     * Adds a single byte value to the array.
     *
     * @param value The byte value to add.
     */
    public void add(byte value) {
        byte[] items = this.items;
        if (size == items.length) items = resize(Math.max(8, (int) (size * 1.75f)));
        items[size++] = value;
    }

    /**
     * Adds two byte values to the array.
     *
     * @param value1 The first byte value to add.
     * @param value2 The second byte value to add.
     */
    public void add(byte value1, byte value2) {
        byte[] items = this.items;
        if (size + 1 >= items.length) items = resize(Math.max(8, (int) (size * 1.75f)));
        items[size] = value1;
        items[size + 1] = value2;
        size += 2;
    }

    /**
     * Adds three byte values to the array.
     *
     * @param value1 The first byte value to add.
     * @param value2 The second byte value to add.
     * @param value3 The third byte value to add.
     */
    public void add(byte value1, byte value2, byte value3) {
        byte[] items = this.items;
        if (size + 2 >= items.length) items = resize(Math.max(8, (int) (size * 1.75f)));
        items[size] = value1;
        items[size + 1] = value2;
        items[size + 2] = value3;
        size += 3;
    }

    /**
     * Adds four byte values to the array.
     *
     * @param value1 The first byte value to add.
     * @param value2 The second byte value to add.
     * @param value3 The third byte value to add.
     * @param value4 The fourth byte value to add.
     */
    public void add(byte value1, byte value2, byte value3, byte value4) {
        byte[] items = this.items;
        if (size + 3 >= items.length)
            items = resize(Math.max(8, (int) (size * 1.8f))); // 1.75 isn't enough when size=5.
        items[size] = value1;
        items[size + 1] = value2;
        items[size + 2] = value3;
        items[size + 3] = value4;
        size += 4;
    }

    /**
     * Adds all elements from another ByteArray to this array.
     *
     * @param array The ByteArray to copy elements from.
     */
    public void addAll(ByteArray array) {
        addAll(array.items, 0, array.size);
    }

    /**
     * Adds a subset of elements from another ByteArray to this array.
     *
     * @param array  The ByteArray to copy elements from.
     * @param offset The starting index to copy from.
     * @param length The number of elements to copy.
     */
    public void addAll(ByteArray array, int offset, int length) {
        if (offset + length > array.size)
            throw new IllegalArgumentException("offset + length must be <= size: " + offset + " + " + length + " <= " + array.size);
        addAll(array.items, offset, length);
    }

    /**
     * Adds all elements from the given native array to this array.
     *
     * @param array The native array to copy elements from.
     */
    public void addAll(byte... array) {
        addAll(array, 0, array.length);
    }

    /**
     * Adds a subset of elements from a native array to this array.
     *
     * @param array  The native array to copy elements from.
     * @param offset The starting index to copy from.
     * @param length The number of elements to copy.
     */
    public void addAll(byte[] array, int offset, int length) {
        byte[] items = this.items;
        int sizeNeeded = size + length;
        if (sizeNeeded > items.length) items = resize(Math.max(Math.max(8, sizeNeeded), (int) (size * 1.75f)));
        System.arraycopy(array, offset, items, size, length);
        size += length;
    }

    /**
     * Retrieves the byte value at the specified index.
     *
     * @param index The index to retrieve the byte from.
     * @return The byte value at the specified index.
     */
    public byte get(int index) {
        if (index >= size) throw new IndexOutOfBoundsException("index can't be >= size: " + index + " >= " + size);
        return items[index];
    }

    /**
     * Sets the byte value at the specified index.
     *
     * @param index The index to set the byte at.
     * @param value The byte value to set.
     */
    public void set(int index, byte value) {
        if (index >= size) throw new IndexOutOfBoundsException("index can't be >= size: " + index + " >= " + size);
        items[index] = value;
    }

    /**
     * Increments the byte value at the specified index by the given value.
     *
     * @param index The index to increment the byte at.
     * @param value The value to increment by.
     */
    public void incr(int index, byte value) {
        if (index >= size) throw new IndexOutOfBoundsException("index can't be >= size: " + index + " >= " + size);
        items[index] += value;
    }

    /**
     * Increments all byte values in the array by the given value.
     *
     * @param value The value to increment by.
     */
    public void incr(byte value) {
        byte[] items = this.items;
        for (int i = 0, n = size; i < n; i++)
            items[i] += value;
    }

    /**
     * Multiplies the byte value at the specified index by the given value.
     *
     * @param index The index to multiply the byte at.
     * @param value The value to multiply by.
     */
    public void mul(int index, byte value) {
        if (index >= size) throw new IndexOutOfBoundsException("index can't be >= size: " + index + " >= " + size);
        items[index] *= value;
    }

    /**
     * Multiplies all byte values in the array by the given value.
     *
     * @param value The value to multiply by.
     */
    public void mul(byte value) {
        byte[] items = this.items;
        for (int i = 0, n = size; i < n; i++)
            items[i] *= value;
    }

    /**
     * Inserts a byte value at the specified index.
     *
     * @param index The index to insert the byte at.
     * @param value The byte value to insert.
     */
    public void insert(int index, byte value) {
        if (index > size) throw new IndexOutOfBoundsException("index can't be > size: " + index + " > " + size);
        byte[] items = this.items;
        if (size == items.length) items = resize(Math.max(8, (int) (size * 1.75f)));
        if (ordered)
            System.arraycopy(items, index, items, index + 1, size - index);
        else
            items[size] = items[index];
        size++;
        items[index] = value;
    }

    /**
     * Inserts a range of bytes at the specified index.
     *
     * @param index The index to insert bytes at.
     * @param count The number of bytes to insert.
     */
    public void insertRange(int index, int count) {
        if (index > size) throw new IndexOutOfBoundsException("index can't be > size: " + index + " > " + size);
        int sizeNeeded = size + count;
        if (sizeNeeded > items.length) items = resize(Math.max(Math.max(8, sizeNeeded), (int) (size * 1.75f)));
        System.arraycopy(items, index, items, index + count, size - index);
        size = sizeNeeded;
    }

    /**
     * Swaps the byte values at the specified indices.
     *
     * @param first  The index of the first byte to swap.
     * @param second The index of the second byte to swap.
     */
    public void swap(int first, int second) {
        if (first >= size) throw new IndexOutOfBoundsException("first can't be >= size: " + first + " >= " + size);
        if (second >= size) throw new IndexOutOfBoundsException("second can't be >= size: " + second + " >= " + size);
        byte[] items = this.items;
        byte firstValue = items[first];
        items[first] = items[second];
        items[second] = firstValue;
    }

    /**
     * Checks if the array contains the specified byte value.
     *
     * @param value The byte value to check for.
     * @return True if the array contains the value, false otherwise.
     */
    public boolean contains(byte value) {
        int i = size - 1;
        byte[] items = this.items;
        while (i >= 0)
            if (items[i--] == value) return true;
        return false;
    }

    /**
     * Finds the index of the first occurrence of the specified byte value.
     *
     * @param value The byte value to find.
     * @return The index of the first occurrence, or -1 if not found.
     */
    public int indexOf(byte value) {
        byte[] items = this.items;
        for (int i = 0, n = size; i < n; i++)
            if (items[i] == value) return i;
        return -1;
    }

    /**
     * Finds the index of the last occurrence of the specified byte value.
     *
     * @param value The byte value to find.
     * @return The index of the last occurrence, or -1 if not found.
     */
    public int lastIndexOf(byte value) {
        byte[] items = this.items;
        for (int i = size - 1; i >= 0; i--)
            if (items[i] == value) return i;
        return -1;
    }

    /**
     * Removes the first occurrence of the specified byte value.
     *
     * @param value The byte value to remove.
     * @return True if the value was found and removed, false otherwise.
     */
    public boolean removeValue(byte value) {
        byte[] items = this.items;
        for (int i = 0, n = size; i < n; i++) {
            if (items[i] == value) {
                removeIndex(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Removes and returns the item at the specified index.
     *
     * @param index The index of the item to remove.
     * @return The removed item.
     * @throws IndexOutOfBoundsException If the index is out of bounds.
     */
    public int removeIndex(int index) {
        if (index >= size) throw new IndexOutOfBoundsException("index can't be >= size: " + index + " >= " + size);
        byte[] items = this.items;
        int value = items[index];
        size--;
        if (ordered)
            System.arraycopy(items, index + 1, items, index, size - index);
        else
            items[index] = items[size];
        return value;
    }

    /**
     * Removes the items between the specified indices, inclusive.
     *
     * @param start The start index (inclusive).
     * @param end   The end index (inclusive).
     * @throws IndexOutOfBoundsException If start or end are out of bounds.
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
     * Removes from this array all of elements contained in the specified array.
     *
     * @param array The array of elements to remove.
     * @return true if this array was modified.
     */
    public boolean removeAll(ByteArray array) {
        int size = this.size;
        int startSize = size;
        byte[] items = this.items;
        for (int i = 0, n = array.size; i < n; i++) {
            int item = array.get(i);
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
     * Removes and returns the last item.
     *
     * @return The last item.
     * @throws IllegalStateException If the array is empty.
     */
    public byte pop() {
        return items[--size];
    }

    /**
     * Returns the last item.
     *
     * @return The last item.
     */
    public byte peek() {
        return items[size - 1];
    }

    /**
     * Returns the first item.
     *
     * @return The first item.
     * @throws IllegalStateException If the array is empty.
     */
    public byte first() {
        if (size == 0) throw new IllegalStateException("Array is empty.");
        return items[0];
    }

    /**
     * Returns true if the array has one or more items.
     *
     * @return true if the array is not empty.
     */
    public boolean notEmpty() {
        return size > 0;
    }

    /**
     * Returns true if the array is empty.
     *
     * @return true if the array is empty.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Clears the array.
     */
    public void clear() {
        size = 0;
    }

    /**
     * Reduces the size of the backing array to the size of the actual items.
     * This is useful to release memory when many items have been removed,
     * or if it is known that more items will not be added.
     *
     * @return The backing array.
     */
    public byte[] shrink() {
        if (items.length != size) resize(size);
        return items;
    }

    /**
     * Increases the size of the backing array to accommodate the specified number of additional items.
     * Useful before adding many items to avoid multiple backing array resizes.
     *
     * @param additionalCapacity The additional capacity to ensure.
     * @return The backing array.
     * @throws IllegalArgumentException If additionalCapacity is negative.
     */
    public byte[] ensureCapacity(int additionalCapacity) {
        if (additionalCapacity < 0)
            throw new IllegalArgumentException("additionalCapacity must be >= 0: " + additionalCapacity);
        int sizeNeeded = size + additionalCapacity;
        if (sizeNeeded > items.length) resize(Math.max(Math.max(8, sizeNeeded), (int) (size * 1.75f)));
        return items;
    }


    /**
     * Sets the array size, leaving any values beyond the current size undefined.
     *
     * @param newSize The new size of the array.
     * @return The backing array.
     * @throws IllegalArgumentException If newSize is negative.
     */
    public byte[] setSize(int newSize) {
        if (newSize < 0) throw new IllegalArgumentException("newSize must be >= 0: " + newSize);
        if (newSize > items.length) resize(Math.max(8, newSize));
        size = newSize;
        return items;
    }

    /**
     * Resizes the internal array to the specified new size.
     *
     * @param newSize The new size for the internal array.
     * @return The resized internal array.
     */
    protected byte[] resize(int newSize) {
        byte[] newItems = new byte[newSize];
        byte[] items = this.items;
        System.arraycopy(items, 0, newItems, 0, Math.min(size, newItems.length));
        this.items = newItems;
        return newItems;
    }

    /**
     * Sorts the array in ascending order.
     */
    public void sort() {
        Arrays.sort(items, 0, size);
    }

    /**
     * Reverses the order of elements in the array.
     */
    public void reverse() {
        byte[] items = this.items;
        for (int i = 0, lastIndex = size - 1, n = size / 2; i < n; i++) {
            int ii = lastIndex - i;
            byte temp = items[i];
            items[i] = items[ii];
            items[ii] = temp;
        }
    }

    /**
     * Shuffles the elements in the array randomly.
     */
    public void shuffle() {
        byte[] items = this.items;
        for (int i = size - 1; i >= 0; i--) {
            int ii = Maths.random(i);
            byte temp = items[i];
            items[i] = items[ii];
            items[ii] = temp;
        }
    }

    /**
     * Reduces the size of the array to the specified size. If the array is already smaller than the specified size, no action is taken.
     *
     * @param newSize The new size for the array.
     */
    public void truncate(int newSize) {
        if (size > newSize) size = newSize;
    }

    /**
     * Returns a random item from the array, or zero if the array is empty.
     *
     * @return A random item from the array, or zero if the array is empty.
     */
    public byte random() {
        if (size == 0) return 0;
        return items[Maths.random(0, size - 1)];
    }

    /**
     * Converts the array to a new byte array.
     *
     * @return A new byte array containing the same elements as the original array.
     */
    public byte[] toArray() {
        byte[] array = new byte[size];
        System.arraycopy(items, 0, array, 0, size);
        return array;
    }

    @Override
    public int hashCode() {
        if (!ordered) return super.hashCode();
        byte[] items = this.items;
        int h = 1;
        for (int i = 0, n = size; i < n; i++)
            h = h * 31 + items[i];
        return h;
    }

    /**
     * Compares this ByteArray to another object for equality.
     *
     * @param object The object to compare to.
     * @return true if the objects are equal, false otherwise.
     */
    public boolean equals(Object object) {
        if (object == this) return true;
        if (!ordered) return false;
        if (!(object instanceof ByteArray)) return false;
        ByteArray array = (ByteArray) object;
        if (!array.ordered) return false;
        int n = size;
        if (n != array.size) return false;
        byte[] items1 = this.items, items2 = array.items;
        for (int i = 0; i < n; i++)
            if (items1[i] != items2[i]) return false;
        return true;
    }

    @Override
    public String toString() {
        if (size == 0) return "[]";
        byte[] items = this.items;
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
     * Converts the array to a string with elements separated by the specified separator.
     *
     * @param separator The separator to use between elements.
     * @return A string representation of the array with elements separated by the separator.
     */
    public String toString(String separator) {
        if (size == 0) return "";
        byte[] items = this.items;
        StringBuilder buffer = new StringBuilder(32);
        buffer.append(items[0]);
        for (int i = 1; i < size; i++) {
            buffer.append(separator);
            buffer.append(items[i]);
        }
        return buffer.toString();
    }
}
