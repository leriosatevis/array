package array;

import math.Maths;
import utils.*;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A dynamic array implementation that can be either ordered or unordered.
 * <p>
 * This class provides a flexible and efficient way to store objects. If the array is unordered,
 * removal operations avoid memory copying by swapping the last element to the removed element's position.
 * </p>
 *
 * @param <T> The type of elements this array will hold.
 */
public class Array<T> implements Iterable<T> {
    /**
     * Direct access to the underlying storage array of elements.
     * <p>
     * This field is accessible only if the {@link Array#Array(boolean, int, Class)} constructor is used.
     * </p>
     */
    public T[] items;
    /**
     * The current number of elements stored in the array.
     */
    public int size;

    /**
     * Flag indicating whether the array maintains the order of elements.
     */
    public boolean ordered;

    /**
     * Internal iterable for traversing the elements in this array.
     */
    private ArrayIterable iterable;

    /**
     * Internal iterable for applying predicates to elements in this array.
     */
    private Predicate.PredicateIterable<T> predicateIterable;

    /**
     * Constructs an ordered array with an initial capacity of 16.
     */
    public Array() {
        this(true, 16);
    }

    /**
     * Constructs an ordered array with the specified initial capacity.
     *
     * @param capacity The initial capacity of the array.
     */
    public Array(int capacity) {
        this(true, capacity);
    }

    /**
     * Constructs an array with the specified ordering and initial capacity.
     *
     * @param ordered  Whether the array should be ordered.
     * @param capacity The initial capacity of the array.
     */
    public Array(boolean ordered, int capacity) {
        this.ordered = ordered;
        items = (T[]) new Object[capacity];
    }

    /**
     * Constructs an array with specified ordering, initial capacity, and array type.
     *
     * @param ordered   Whether the array should be ordered.
     * @param capacity  The initial capacity of the array.
     * @param arrayType The class type of the elements in this array.
     */
    public Array(boolean ordered, int capacity, Class arrayType) {
        this.ordered = ordered;
        items = (T[]) ArrayReflection.newInstance(arrayType, capacity);
    }

    /**
     * Constructs an ordered array with a specified array type and an initial capacity of 16.
     *
     * @param arrayType The class type of the elements in this array.
     */
    public Array(Class arrayType) {
        this(true, 16, arrayType);
    }

    /**
     * Constructs an array by copying elements from another array.
     *
     * @param array The source array to copy from.
     */
    public Array(Array<? extends T> array) {
        this(array.ordered, array.size, array.items.getClass().getComponentType());
        size = array.size;
        System.arraycopy(array.items, 0, items, 0, size);
    }

    /**
     * Constructs an ordered array by copying elements from a native array.
     *
     * @param array The source native array to copy from.
     */
    public Array(T[] array) {
        this(true, array, 0, array.length);
    }

    /**
     * Constructs an array by copying a range of elements from a native array.
     *
     * @param ordered Whether the array should be ordered.
     * @param array   The source native array to copy from.
     * @param start   The starting index in the source array.
     * @param count   The number of elements to copy.
     */
    public Array(boolean ordered, T[] array, int start, int count) {
        this(ordered, count, array.getClass().getComponentType());
        size = count;
        System.arraycopy(array, start, items, 0, size);
    }

    /**
     * Creates a new empty array of the specified type.
     *
     * @param <T>       The type parameter for the resulting array.
     * @param arrayType The class type of the array.
     * @return A new empty array of the specified type.
     */
    public static <T> Array<T> of(Class<T> arrayType) {
        return new Array(arrayType);
    }

    /**
     * Creates a new empty array of the specified type with the specified initial capacity and ordering.
     *
     * @param <T>       The type parameter for the resulting array.
     * @param ordered   If true, the array will be ordered; if false, it will be unordered.
     * @param capacity  The initial capacity of the array.
     * @param arrayType The class type of the array.
     * @return A new empty array of the specified type with the specified initial capacity and ordering.
     */
    public static <T> Array<T> of(boolean ordered, int capacity, Class<T> arrayType) {
        return new Array(ordered, capacity, arrayType);
    }

    /**
     * Creates a new array with the specified items.
     *
     * @param <T>   The type parameter for the resulting array.
     * @param array The initial items of the array.
     * @return A new array containing the specified items.
     */
    public static <T> Array<T> with(T... array) {
        return new Array(array);
    }

    /**
     * Adds the specified value to the array.
     *
     * @param value The value to add.
     */
    public void add(T value) {
        T[] items = this.items;
        if (size == items.length) items = resize(Math.max(8, (int) (size * 1.75f)));
        items[size++] = value;
    }

    /**
     * Adds two specified values to the array.
     *
     * @param value1 The first value to add.
     * @param value2 The second value to add.
     */
    public void add(T value1, T value2) {
        T[] items = this.items;
        if (size + 1 >= items.length) items = resize(Math.max(8, (int) (size * 1.75f)));
        items[size] = value1;
        items[size + 1] = value2;
        size += 2;
    }

    /**
     * Adds three specified values to the array.
     *
     * @param value1 The first value to add.
     * @param value2 The second value to add.
     * @param value3 The third value to add.
     */
    public void add(T value1, T value2, T value3) {
        T[] items = this.items;
        if (size + 2 >= items.length) items = resize(Math.max(8, (int) (size * 1.75f)));
        items[size] = value1;
        items[size + 1] = value2;
        items[size + 2] = value3;
        size += 3;
    }

    /**
     * Adds four specified values to the array.
     *
     * @param value1 The first value to add.
     * @param value2 The second value to add.
     * @param value3 The third value to add.
     * @param value4 The fourth value to add.
     */
    public void add(T value1, T value2, T value3, T value4) {
        T[] items = this.items;
        if (size + 3 >= items.length)
            items = resize(Math.max(8, (int) (size * 1.8f))); // 1.75 isn't enough when size=5.
        items[size] = value1;
        items[size + 1] = value2;
        items[size + 2] = value3;
        items[size + 3] = value4;
        size += 4;
    }

    /**
     * Adds all elements from another array to this array.
     *
     * @param array The array containing elements to add.
     */
    public void addAll(Array<? extends T> array) {
        addAll(array.items, 0, array.size);
    }

    /**
     * Adds a specified range of elements from another array to this array.
     *
     * @param array The source array containing elements to add.
     * @param start The starting index in the source array.
     * @param count The number of elements to add from the source array.
     */
    public void addAll(Array<? extends T> array, int start, int count) {
        if (start + count > array.size)
            throw new IllegalArgumentException("start + count must be <= size: " + start + " + " + count + " <= " + array.size);
        addAll(array.items, start, count);
    }

    /**
     * Adds all elements from the specified array to this array.
     *
     * @param array The array containing elements to add.
     */
    public void addAll(T... array) {
        addAll(array, 0, array.length);
    }

    /**
     * Adds a specified range of elements from the specified array to this array.
     *
     * @param array The source array containing elements to add.
     * @param start The starting index in the source array.
     * @param count The number of elements to add from the source array.
     */
    public void addAll(T[] array, int start, int count) {
        T[] items = this.items;
        int sizeNeeded = size + count;
        if (sizeNeeded > items.length) items = resize(Math.max(Math.max(8, sizeNeeded), (int) (size * 1.75f)));
        System.arraycopy(array, start, items, size, count);
        size = sizeNeeded;
    }

    /**
     * Gets the element at the specified index.
     *
     * @param index The index of the element to retrieve.
     * @return The element at the specified index.
     * @throws IndexOutOfBoundsException If the index is out of bounds.
     */
    public T get(int index) {
        if (index >= size) throw new IndexOutOfBoundsException("index can't be >= size: " + index + " >= " + size);
        return items[index];
    }

    /**
     * Sets the element at the specified index to the given value.
     *
     * @param index The index of the element to set.
     * @param value The value to set at the specified index.
     * @throws IndexOutOfBoundsException If the index is out of bounds.
     */
    public void set(int index, T value) {
        if (index >= size) throw new IndexOutOfBoundsException("index can't be >= size: " + index + " >= " + size);
        items[index] = value;
    }

    /**
     * Inserts the specified value at the specified index.
     *
     * @param index The index at which to insert the value.
     * @param value The value to insert.
     * @throws IndexOutOfBoundsException If the index is greater than the size of the array.
     */
    public void insert(int index, T value) {
        if (index > size) throw new IndexOutOfBoundsException("index can't be > size: " + index + " > " + size);
        T[] items = this.items;
        if (size == items.length) items = resize(Math.max(8, (int) (size * 1.75f)));
        if (ordered)
            System.arraycopy(items, index, items, index + 1, size - index);
        else
            items[size] = items[index];
        size++;
        items[index] = value;
    }

    /**
     * Inserts the specified number of items at the specified index.
     * The new items will have values equal to the values at those indices before the insertion.
     *
     * @param index The index at which to insert the items.
     * @param count The number of items to insert.
     * @throws IndexOutOfBoundsException If the index is greater than the size of the array.
     */
    public void insertRange(int index, int count) {
        if (index > size) throw new IndexOutOfBoundsException("index can't be > size: " + index + " > " + size);
        int sizeNeeded = size + count;
        if (sizeNeeded > items.length) items = resize(Math.max(Math.max(8, sizeNeeded), (int) (size * 1.75f)));
        System.arraycopy(items, index, items, index + count, size - index);
        size = sizeNeeded;
    }

    /**
     * Swaps the elements at the specified indices.
     *
     * @param first  The index of the first element to swap.
     * @param second The index of the second element to swap.
     * @throws IndexOutOfBoundsException If either index is out of bounds.
     */
    public void swap(int first, int second) {
        if (first >= size) throw new IndexOutOfBoundsException("first can't be >= size: " + first + " >= " + size);
        if (second >= size) throw new IndexOutOfBoundsException("second can't be >= size: " + second + " >= " + size);
        T[] items = this.items;
        T firstValue = items[first];
        items[first] = items[second];
        items[second] = firstValue;
    }

    /**
     * Returns true if this array contains the specified value.
     *
     * @param value    May be null.
     * @param identity If true, == comparison will be used. If false, .equals() comparison will be used.
     * @return True if the array contains the specified value, otherwise false.
     */
    public boolean contains(@Null T value, boolean identity) {
        T[] items = this.items;
        int i = size - 1;
        if (identity || value == null) {
            while (i >= 0)
                if (items[i--] == value) return true;
        } else {
            while (i >= 0)
                if (value.equals(items[i--])) return true;
        }
        return false;
    }

    /**
     * Returns true if this array contains all the specified values.
     *
     * @param values   May contain nulls.
     * @param identity If true, == comparison will be used. If false, .equals() comparison will be used.
     * @return True if the array contains all the specified values, otherwise false.
     */
    public boolean containsAll(Array<? extends T> values, boolean identity) {
        T[] items = values.items;
        for (int i = 0, n = values.size; i < n; i++)
            if (!contains(items[i], identity)) return false;
        return true;
    }

    /**
     * Returns true if this array contains any of the specified values.
     *
     * @param values   May contain nulls.
     * @param identity If true, == comparison will be used. If false, .equals() comparison will be used.
     * @return True if the array contains any of the specified values, otherwise false.
     */
    public boolean containsAny(Array<? extends T> values, boolean identity) {
        T[] items = values.items;
        for (int i = 0, n = values.size; i < n; i++)
            if (contains(items[i], identity)) return true;
        return false;
    }

    /**
     * Returns the index of the first occurrence of the value in the array, or -1 if no such value exists.
     *
     * @param value    May be null.
     * @param identity If true, == comparison will be used. If false, .equals() comparison will be used.
     * @return The index of the first occurrence of the value in the array, or -1 if no such value exists.
     */
    public int indexOf(@Null T value, boolean identity) {
        T[] items = this.items;
        if (identity || value == null) {
            for (int i = 0, n = size; i < n; i++)
                if (items[i] == value) return i;
        } else {
            for (int i = 0, n = size; i < n; i++)
                if (value.equals(items[i])) return i;
        }
        return -1;
    }

    /**
     * Returns the index of the last occurrence of the value in the array, or -1 if no such value exists.
     * The search is started from the end of the array.
     *
     * @param value    May be null.
     * @param identity If true, == comparison will be used. If false, .equals() comparison will be used.
     * @return The index of the last occurrence of the value in the array, or -1 if no such value exists.
     */
    public int lastIndexOf(@Null T value, boolean identity) {
        T[] items = this.items;
        if (identity || value == null) {
            for (int i = size - 1; i >= 0; i--)
                if (items[i] == value) return i;
        } else {
            for (int i = size - 1; i >= 0; i--)
                if (value.equals(items[i])) return i;
        }
        return -1;
    }

    /**
     * Removes the first instance of the specified value in the array.
     *
     * @param value    May be null.
     * @param identity If true, == comparison will be used. If false, .equals() comparison will be used.
     * @return True if the value was found and removed, false otherwise.
     */
    public boolean removeValue(@Null T value, boolean identity) {
        T[] items = this.items;
        if (identity || value == null) {
            for (int i = 0, n = size; i < n; i++) {
                if (items[i] == value) {
                    removeIndex(i);
                    return true;
                }
            }
        } else {
            for (int i = 0, n = size; i < n; i++) {
                if (value.equals(items[i])) {
                    removeIndex(i);
                    return true;
                }
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
    public T removeIndex(int index) {
        if (index >= size) throw new IndexOutOfBoundsException("index can't be >= size: " + index + " >= " + size);
        T[] items = this.items;
        T value = items[index];
        size--;
        if (ordered)
            System.arraycopy(items, index + 1, items, index, size - index);
        else
            items[index] = items[size];
        items[size] = null;
        return value;
    }

    /**
     * Removes the items between the specified indices, inclusive.
     *
     * @param start The starting index of the range to remove.
     * @param end   The ending index of the range to remove.
     * @throws IndexOutOfBoundsException If the start or end indices are out of bounds.
     */
    public void removeRange(int start, int end) {
        int n = size;
        if (end >= n) throw new IndexOutOfBoundsException("end can't be >= size: " + end + " >= " + size);
        if (start > end) throw new IndexOutOfBoundsException("start can't be > end: " + start + " > " + end);
        T[] items = this.items;
        int count = end - start + 1, lastIndex = n - count;
        if (ordered)
            System.arraycopy(items, start + count, items, start, n - (start + count));
        else {
            int i = Math.max(lastIndex, end + 1);
            System.arraycopy(items, i, items, start, n - i);
        }
        for (int i = lastIndex; i < n; i++)
            items[i] = null;
        size = n - count;
    }

    /**
     * Removes from this array all elements contained in the specified array.
     *
     * @param array    The array containing elements to remove.
     * @param identity True to use == for comparison, false to use .equals().
     * @return True if this array was modified, false otherwise.
     */
    public boolean removeAll(Array<? extends T> array, boolean identity) {
        int size = this.size;
        int startSize = size;
        T[] items = this.items;
        if (identity) {
            for (int i = 0, n = array.size; i < n; i++) {
                T item = array.get(i);
                for (int ii = 0; ii < size; ii++) {
                    if (item == items[ii]) {
                        removeIndex(ii);
                        size--;
                        break;
                    }
                }
            }
        } else {
            for (int i = 0, n = array.size; i < n; i++) {
                T item = array.get(i);
                for (int ii = 0; ii < size; ii++) {
                    if (item.equals(items[ii])) {
                        removeIndex(ii);
                        size--;
                        break;
                    }
                }
            }
        }
        return size != startSize;
    }

    /**
     * Removes and returns the last item.
     *
     * @return The removed last item.
     * @throws IllegalStateException If the array is empty.
     */
    public T pop() {
        if (size == 0) throw new IllegalStateException("Array is empty.");
        --size;
        T item = items[size];
        items[size] = null;
        return item;
    }

    /**
     * Returns the last item.
     *
     * @return The last item in the array.
     * @throws IllegalStateException If the array is empty.
     */
    public T peek() {
        if (size == 0) throw new IllegalStateException("Array is empty.");
        return items[size - 1];
    }

    /**
     * Returns the first item.
     *
     * @return The first item in the array.
     * @throws IllegalStateException If the array is empty.
     */
    public T first() {
        if (size == 0) throw new IllegalStateException("Array is empty.");
        return items[0];
    }

    /**
     * Returns true if the array has one or more items.
     *
     * @return True if the array is not empty, otherwise false.
     */
    public boolean notEmpty() {
        return size > 0;
    }

    /**
     * Returns true if the array is empty.
     *
     * @return True if the array is empty, otherwise false.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Clears the array, removing all elements.
     */
    public void clear() {
        Arrays.fill(items, 0, size, null);
        size = 0;
    }

    /**
     * Reduces the size of the backing array to the size of the actual items. This is useful to release memory when many items
     * have been removed, or if it is known that more items will not be added.
     *
     * @return {@link #items}
     */
    public T[] shrink() {
        if (items.length != size) resize(size);
        return items;
    }

    /**
     * Increases the size of the backing array to accommodate the specified number of additional items. Useful before adding many
     * items to avoid multiple backing array resizes.
     *
     * @param additionalCapacity The number of additional items to accommodate.
     * @return The backing array after ensuring capacity.
     * @throws IllegalArgumentException If additionalCapacity is negative.
     */
    public T[] ensureCapacity(int additionalCapacity) {
        if (additionalCapacity < 0)
            throw new IllegalArgumentException("additionalCapacity must be >= 0: " + additionalCapacity);
        int sizeNeeded = size + additionalCapacity;
        if (sizeNeeded > items.length) resize(Math.max(Math.max(8, sizeNeeded), (int) (size * 1.75f)));
        return items;
    }

    /**
     * Sets the array size, leaving any values beyond the current size null.
     *
     * @param newSize The new size of the array.
     * @return The backing array after setting the size.
     */
    public T[] setSize(int newSize) {
        truncate(newSize);
        if (newSize > items.length) resize(Math.max(8, newSize));
        size = newSize;
        return items;
    }

    /**
     * Creates a new backing array with the specified size containing the current items.
     *
     * @param newSize The new size of the backing array.
     * @return The new backing array after resizing.
     */
    protected T[] resize(int newSize) {
        T[] items = this.items;
        T[] newItems = (T[]) ArrayReflection.newInstance(items.getClass().getComponentType(), newSize);
        System.arraycopy(items, 0, newItems, 0, Math.min(size, newItems.length));
        this.items = newItems;
        return newItems;
    }

    /**
     * Sorts this array. The array elements must implement {@link Comparable}. This method is not thread safe (uses
     * {@link Sort#instance()}).
     */
    public void sort() {
        Sort.instance().sort(items, 0, size);
    }

    /**
     * Sorts the array using the specified comparator. This method is not thread safe (uses {@link Sort#instance()}).
     *
     * @param comparator The comparator to be used for sorting.
     */
    public void sort(Comparator<? super T> comparator) {
        Sort.instance().sort(items, comparator, 0, size);
    }

    /**
     * Selects the nth-lowest element from the Array according to Comparator ranking. This might partially sort the Array. The
     * array must have a size greater than 0, or a {@link RuntimeException} will be thrown.
     *
     * @param comparator used for comparison
     * @param kthLowest  rank of desired object according to comparison, n is based on ordinal numbers, not array indices. for min
     *                   value use 1, for max value use size of array, using 0 results in runtime exception.
     * @return the value of the Nth lowest ranked object.
     * @see Select
     */
    public T selectRanked(Comparator<T> comparator, int kthLowest) {
        if (kthLowest < 1) {
            throw new RuntimeException("nth_lowest must be greater than 0, 1 = first, 2 = second...");
        }
        return Select.instance().select(items, comparator, kthLowest, size);
    }

    /**
     * @param comparator used for comparison
     * @param kthLowest  rank of desired object according to comparison, n is based on ordinal numbers, not array indices. for min
     *                   value use 1, for max value use size of array, using 0 results in runtime exception.
     * @return the index of the Nth lowest ranked object.
     * @see Array#selectRanked(Comparator, int)
     */
    public int selectRankedIndex(Comparator<T> comparator, int kthLowest) {
        if (kthLowest < 1) {
            throw new RuntimeException("nth_lowest must be greater than 0, 1 = first, 2 = second...");
        }
        return Select.instance().selectIndex(items, comparator, kthLowest, size);
    }

    /**
     * Reverses the order of elements in the array.
     */
    public void reverse() {
        T[] items = this.items;
        for (int i = 0, lastIndex = size - 1, n = size / 2; i < n; i++) {
            int ii = lastIndex - i;
            T temp = items[i];
            items[i] = items[ii];
            items[ii] = temp;
        }
    }

    /**
     * Shuffles the elements in the array randomly.
     */
    public void shuffle() {
        T[] items = this.items;
        for (int i = size - 1; i >= 0; i--) {
            int ii = Maths.random(i);
            T temp = items[i];
            items[i] = items[ii];
            items[ii] = temp;
        }
    }

    /**
     * Returns an iterator for the items in the array. Remove is supported.
     * <p>
     * If {@link Collections#allocateIterators} is false, the same iterator instance is returned each time this method is called.
     * Use the {@link ArrayIterator} constructor for nested or multithreaded iteration.
     */
    public ArrayIterator<T> iterator() {
        if (Collections.allocateIterators) return new ArrayIterator(this, true);
        if (iterable == null) iterable = new ArrayIterable(this);
        return iterable.iterator();
    }

    /**
     * Returns an iterable for the selected items in the array based on the provided predicate.
     * Remove is supported, but not between hasNext() and next().
     * <p>
     * If {@link Collections#allocateIterators} is false, the same iterable instance is returned each time this method is called.
     * Use the {@link Predicate.PredicateIterable} constructor for nested or multithreaded iteration.
     *
     * @param predicate The predicate used to select items for iteration.
     * @return An iterable for the selected items in the array.
     */
    public Iterable<T> select(Predicate<T> predicate) {
        if (Collections.allocateIterators) return new Predicate.PredicateIterable<T>(this, predicate);
        if (predicateIterable == null)
            predicateIterable = new Predicate.PredicateIterable<T>(this, predicate);
        else
            predicateIterable.set(this, predicate);
        return predicateIterable;
    }

    /**
     * Reduces the size of the array to the specified size. If the array is already smaller than the specified size, no action is
     * taken.
     *
     * @param newSize The new size of the array.
     */
    public void truncate(int newSize) {
        if (newSize < 0) throw new IllegalArgumentException("newSize must be >= 0: " + newSize);
        if (size <= newSize) return;
        for (int i = newSize; i < size; i++)
            items[i] = null;
        size = newSize;
    }

    /**
     * Returns a random item from the array, or null if the array is empty.
     *
     * @return A random item from the array, or null if the array is empty.
     */
    public @Null T random() {
        if (size == 0) return null;
        return items[Maths.random(0, size - 1)];
    }

    /**
     * Returns the items as an array. Note the array is typed, so the {@link #Array(Class)} constructor must have been used.
     * Otherwise, use {@link #toArray(Class)} to specify the array type.
     *
     * @return An array containing the items in this array.
     */
    public T[] toArray() {
        return (T[]) toArray(items.getClass().getComponentType());
    }

    /**
     * Returns the items as an array of the specified type.
     *
     * @param type The class type of the desired array.
     * @param <V>  The type parameter for the resulting array.
     * @return An array containing the items in this array with the specified type.
     */
    public <V> V[] toArray(Class<V> type) {
        V[] result = (V[]) ArrayReflection.newInstance(type, size);
        System.arraycopy(items, 0, result, 0, size);
        return result;
    }

    /**
     * Computes a hash code for this array.
     *
     * @return The computed hash code.
     */
    public int hashCode() {
        if (!ordered) return super.hashCode();
        Object[] items = this.items;
        int h = 1;
        for (int i = 0, n = size; i < n; i++) {
            h *= 31;
            Object item = items[i];
            if (item != null) h += item.hashCode();
        }
        return h;
    }

    /**
     * Compares this array to another object for equality.
     *
     * @param object The object to compare to.
     * @return True if the arrays are equal, false otherwise.
     */
    public boolean equals(Object object) {
        if (object == this) return true;
        if (!ordered) return false;
        if (!(object instanceof Array)) return false;
        Array array = (Array) object;
        if (!array.ordered) return false;
        int n = size;
        if (n != array.size) return false;
        Object[] items1 = this.items, items2 = array.items;
        for (int i = 0; i < n; i++) {
            Object o1 = items1[i], o2 = items2[i];
            if (!(o1 == null ? o2 == null : o1.equals(o2))) return false;
        }
        return true;
    }

    /**
     * Compares this array to another object for identity equality (using ==).
     *
     * @param object The object to compare to.
     * @return True if the arrays are identically equal, false otherwise.
     */
    public boolean equalsIdentity(Object object) {
        if (object == this) return true;
        if (!ordered) return false;
        if (!(object instanceof Array)) return false;
        Array array = (Array) object;
        if (!array.ordered) return false;
        int n = size;
        if (n != array.size) return false;
        Object[] items1 = this.items, items2 = array.items;
        for (int i = 0; i < n; i++)
            if (items1[i] != items2[i]) return false;
        return true;
    }

    /**
     * Returns a string representation of the array.
     *
     * @return A string representation of the array.
     */
    public String toString() {
        if (size == 0) return "[]";
        T[] items = this.items;
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
     * Returns a string representation of the array with the specified separator.
     *
     * @param separator The separator to use between elements.
     * @return A string representation of the array with the specified separator.
     */
    public String toString(String separator) {
        if (size == 0) return "";
        T[] items = this.items;
        StringBuilder buffer = new StringBuilder(32);
        buffer.append(items[0]);
        for (int i = 1; i < size; i++) {
            buffer.append(separator);
            buffer.append(items[i]);
        }
        return buffer.toString();
    }

    /**
     * An iterator for iterating over the elements of an Array.
     *
     * @param <T> The type of elements in the Array.
     */
    public static class ArrayIterator<T> implements Iterator<T>, Iterable<T> {
        private final Array<T> array;
        private final boolean allowRemove;
        int index;
        boolean valid = true;

        // ArrayIterable<T> iterable;

        /**
         * Constructs an ArrayIterator for the given Array with the ability to remove elements.
         *
         * @param array The Array to iterate over.
         */
        public ArrayIterator(Array<T> array) {
            this(array, true);
        }

        /**
         * Constructs an ArrayIterator for the given Array with the ability to remove elements.
         *
         * @param array       The Array to iterate over.
         * @param allowRemove If true, elements can be removed using the remove() method.
         */
        public ArrayIterator(Array<T> array, boolean allowRemove) {
            this.array = array;
            this.allowRemove = allowRemove;
        }

        /**
         * Checks if there is another element in the Array to iterate over.
         *
         * @return True if there is another element to iterate over, false otherwise.
         * @throws RuntimeException If used nested, which is not allowed.
         */
        public boolean hasNext() {
            if (!valid) {
// System.out.println(iterable.lastAcquire);
                throw new RuntimeException("#iterator() cannot be used nested.");
            }
            return index < array.size;
        }

        /**
         * Retrieves the next element in the Array and advances the iterator.
         *
         * @return The next element in the Array.
         * @throws NoSuchElementException If there are no more elements to iterate over.
         * @throws RuntimeException       If used nested, which is not allowed.
         */
        public T next() {
            if (index >= array.size) throw new NoSuchElementException(String.valueOf(index));
            if (!valid) {
                // System.out.println(iterable.lastAcquire);
                throw new RuntimeException("#iterator() cannot be used nested.");
            }
            return array.items[index++];
        }

        /**
         * Removes the last retrieved element from the Array if allowed.
         *
         * @throws RuntimeException If removal is not allowed.
         */
        public void remove() {
            if (!allowRemove) throw new RuntimeException("Remove not allowed.");
            index--;
            array.removeIndex(index);
        }

        /**
         * Resets the iterator to the beginning of the Array.
         */
        public void reset() {
            index = 0;
        }

        /**
         * Returns an iterator over the elements of the Array.
         *
         * @return An iterator over the elements of the Array.
         */
        public ArrayIterator<T> iterator() {
            return this;
        }
    }

    /**
     * An iterable class for iterating over the elements of an Array.
     *
     * @param <T> The type of elements in the Array.
     */
    public static class ArrayIterable<T> implements Iterable<T> {
        private final Array<T> array;
        private final boolean allowRemove;
        private ArrayIterator iterator1, iterator2;

        // java.io.StringWriter lastAcquire = new java.io.StringWriter();

        /**
         * Constructs an ArrayIterable for the given Array with the ability to remove elements.
         *
         * @param array The Array to iterate over.
         */
        public ArrayIterable(Array<T> array) {
            this(array, true);
        }

        /**
         * Constructs an ArrayIterable for the given Array with the ability to remove elements.
         *
         * @param array       The Array to iterate over.
         * @param allowRemove If true, elements can be removed during iteration.
         */
        public ArrayIterable(Array<T> array, boolean allowRemove) {
            this.array = array;
            this.allowRemove = allowRemove;
        }

        /**
         * Returns an iterator over the elements of the Array.
         *
         * @return An iterator over the elements of the Array.
         * @see Collections#allocateIterators
         */
        public ArrayIterator<T> iterator() {
            if (Collections.allocateIterators) return new ArrayIterator(array, allowRemove);
            // lastAcquire.getBuffer().setLength(0);
            // new Throwable().printStackTrace(new java.io.PrintWriter(lastAcquire));
            if (iterator1 == null) {
                iterator1 = new ArrayIterator(array, allowRemove);
                iterator2 = new ArrayIterator(array, allowRemove);
                // iterator1.iterable = this;
                // iterator2.iterable = this;
            }
            if (!iterator1.valid) {
                iterator1.index = 0;
                iterator1.valid = true;
                iterator2.valid = false;
                return iterator1;
            }
            iterator2.index = 0;
            iterator2.valid = true;
            iterator1.valid = false;
            return iterator2;
        }
    }
}
