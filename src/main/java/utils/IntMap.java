package utils;

import array.Array;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static utils.ObjectSet.tableSize;

/**
 * A hash map implementation that uses primitive int keys rather than objects.
 * This class provides methods for basic operations such as put, get, remove, containsKey, and clear.
 * It also implements the Iterable interface to allow iteration over its entries.
 *
 * @param <V> the type of mapped values
 */
public class IntMap<V> implements Iterable<IntMap.Entry<V>> {
    private final float loadFactor;
    public int size;

    /**
     * Used for bit-shifting the hash codes into a usable range.
     * For more details, see the class-level comment block.
     */
    protected int shift;

    /**
     * A bitmask used to confine hash codes to the size of the table.
     * For more details, see the class-level comment block.
     */
    protected int mask;

    // Internal arrays for storing keys and values
    int[] keyTable;
    V[] valueTable;

    // Special case storage for zero key
    V zeroValue;
    boolean hasZeroValue;

    private int threshold;
    private transient Entries entries1, entries2;
    private transient Values values1, values2;
    private transient Keys keys1, keys2;

    /**
     * Default constructor. Initializes the map with an initial capacity of 51 and a load factor of 0.8.
     */
    public IntMap() {
        this(51, 0.8f);
    }

    /**
     * Constructor with initial capacity. Initializes the map with the given initial capacity and a default load factor of 0.8.
     *
     * @param initialCapacity The initial capacity of the map.
     */
    public IntMap(int initialCapacity) {
        this(initialCapacity, 0.8f);
    }

    /**
     * Constructor with initial capacity and load factor.
     * Initializes the map with the given initial capacity and load factor.
     *
     * @param initialCapacity The initial capacity of the map.
     * @param loadFactor      The load factor for internal resizing operations.
     */
    public IntMap(int initialCapacity, float loadFactor) {
        if (loadFactor <= 0f || loadFactor >= 1f)
            throw new IllegalArgumentException("loadFactor must be > 0 and < 1: " + loadFactor);
        this.loadFactor = loadFactor;

        int tableSize = tableSize(initialCapacity, loadFactor);
        threshold = (int) (tableSize * loadFactor);
        mask = tableSize - 1;
        shift = Long.numberOfLeadingZeros(mask);

        keyTable = new int[tableSize];
        valueTable = (V[]) new Object[tableSize];
    }

    /**
     * Copy constructor. Creates a new map that is identical to the specified map.
     *
     * @param map The map to copy.
     */
    public IntMap(IntMap<? extends V> map) {
        this((int) (map.keyTable.length * map.loadFactor), map.loadFactor);
        System.arraycopy(map.keyTable, 0, keyTable, 0, map.keyTable.length);
        System.arraycopy(map.valueTable, 0, valueTable, 0, map.valueTable.length);
        size = map.size;
        zeroValue = map.zeroValue;
        hasZeroValue = map.hasZeroValue;
    }

    /**
     * Calculates the index in the key table where the given item should be placed.
     * Uses Fibonacci hashing to distribute hash codes across the table.
     *
     * @param item The item whose placement index is to be calculated.
     * @return The calculated index within the bounds of the key table.
     */
    protected int place(int item) {
        return (int) (item * 0x9E3779B97F4A7C15L >>> shift);
    }

    /**
     * Locates the index of a given key in the key table.
     *
     * @param key The key to locate.
     * @return The index of the key if found, otherwise returns -(index + 1) where index is the next empty slot.
     */
    private int locateKey(int key) {
        int[] keyTable = this.keyTable;
        for (int i = place(key); ; i = i + 1 & mask) {
            int other = keyTable[i];
            if (other == 0) return -(i + 1); // Empty space is available.
            if (other == key) return i; // Same key was found.
        }
    }

    /**
     * Inserts or updates a key-value pair in the map.
     *
     * @param key   The key to insert or update.
     * @param value The value to associate with the key.
     * @return The old value associated with the key, or null if the key is new.
     */
    public @Null V put(int key, @Null V value) {
        if (key == 0) {
            V oldValue = zeroValue;
            zeroValue = value;
            if (!hasZeroValue) {
                hasZeroValue = true;
                size++;
            }
            return oldValue;
        }
        int i = locateKey(key);
        if (i >= 0) { // Existing key was found.
            V oldValue = valueTable[i];
            valueTable[i] = value;
            return oldValue;
        }
        i = -(i + 1); // Empty space was found.
        keyTable[i] = key;
        valueTable[i] = value;
        if (++size >= threshold) resize(keyTable.length << 1);
        return null;
    }

    /**
     * Inserts all key-value pairs from another IntMap into this map.
     *
     * @param map The source map.
     */
    public void putAll(IntMap<? extends V> map) {
        ensureCapacity(map.size);
        if (map.hasZeroValue) put(0, map.zeroValue);
        int[] keyTable = map.keyTable;
        V[] valueTable = map.valueTable;
        for (int i = 0, n = keyTable.length; i < n; i++) {
            int key = keyTable[i];
            if (key != 0) put(key, valueTable[i]);
        }
    }


    /**
     * Inserts a key-value pair into the map, assuming the key is not already present.
     * This method is used internally for resizing.
     *
     * @param key   The key to insert.
     * @param value The value to associate with the key.
     */
    private void putResize(int key, @Null V value) {
        int[] keyTable = this.keyTable;
        for (int i = place(key); ; i = (i + 1) & mask) {
            if (keyTable[i] == 0) {
                keyTable[i] = key;
                valueTable[i] = value;
                return;
            }
        }
    }

    /**
     * Retrieves the value associated with a given key.
     *
     * @param key The key whose associated value is to be returned.
     * @return The value associated with the specified key, or null if the key is not found.
     */
    public V get(int key) {
        if (key == 0) return hasZeroValue ? zeroValue : null;
        int i = locateKey(key);
        return i >= 0 ? valueTable[i] : null;
    }

    /**
     * Retrieves the value associated with a given key, or returns a default value if the key is not found.
     *
     * @param key          The key whose associated value is to be returned.
     * @param defaultValue The default value to return if the key is not found.
     * @return The value associated with the specified key, or the default value.
     */
    public V get(int key, @Null V defaultValue) {
        if (key == 0) return hasZeroValue ? zeroValue : defaultValue;
        int i = locateKey(key);
        return i >= 0 ? valueTable[i] : defaultValue;
    }

    /**
     * Removes a key-value pair from the map.
     *
     * @param key The key to remove.
     * @return The value that was associated with the removed key, or null if the key was not found.
     */
    public @Null V remove(int key) {
        if (key == 0) {
            if (!hasZeroValue) return null;
            hasZeroValue = false;
            V oldValue = zeroValue;
            zeroValue = null;
            size--;
            return oldValue;
        }

        int i = locateKey(key);
        if (i < 0) return null;
        int[] keyTable = this.keyTable;
        V[] valueTable = this.valueTable;
        V oldValue = valueTable[i];
        int mask = this.mask, next = i + 1 & mask;
        while ((key = keyTable[next]) != 0) {
            int placement = place(key);
            if ((next - placement & mask) > (i - placement & mask)) {
                keyTable[i] = key;
                valueTable[i] = valueTable[next];
                i = next;
            }
            next = next + 1 & mask;
        }
        keyTable[i] = 0;
        valueTable[i] = null;
        size--;
        return oldValue;
    }

    /**
     * Returns true if the map has one or more items.
     */
    public boolean notEmpty() {
        return size > 0;
    }

    /**
     * Returns true if the map is empty.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Reduces the size of the backing arrays to be the specified capacity / loadFactor, or less. If the capacity is already less,
     * nothing is done. If the map contains more items than the specified capacity, the next highest power of two capacity is used
     * instead.
     */
    public void shrink(int maximumCapacity) {
        if (maximumCapacity < 0) throw new IllegalArgumentException("maximumCapacity must be >= 0: " + maximumCapacity);
        int tableSize = tableSize(maximumCapacity, loadFactor);
        if (keyTable.length > tableSize) resize(tableSize);
    }

    /**
     * Clears the map and reduces the size of the backing arrays to be the specified capacity / loadFactor, if they are larger.
     */
    public void clear(int maximumCapacity) {
        int tableSize = tableSize(maximumCapacity, loadFactor);
        if (keyTable.length <= tableSize) {
            clear();
            return;
        }
        size = 0;
        hasZeroValue = false;
        zeroValue = null;
        resize(tableSize);
    }

    public void clear() {
        if (size == 0) return;
        size = 0;
        Arrays.fill(keyTable, 0);
        Arrays.fill(valueTable, null);
        zeroValue = null;
        hasZeroValue = false;
    }

    /**
     * Returns true if the specified value is in the map. Note this traverses the entire map and compares every value, which may
     * be an expensive operation.
     *
     * @param identity If true, uses == to compare the specified value with values in the map. If false, uses
     *                 {@link #equals(Object)}.
     */
    public boolean containsValue(@Null Object value, boolean identity) {
        V[] valueTable = this.valueTable;
        if (value == null) {
            if (hasZeroValue && zeroValue == null) return true;
            int[] keyTable = this.keyTable;
            for (int i = valueTable.length - 1; i >= 0; i--)
                if (keyTable[i] != 0 && valueTable[i] == null) return true;
        } else if (identity) {
            if (value == zeroValue) return true;
            for (int i = valueTable.length - 1; i >= 0; i--)
                if (valueTable[i] == value) return true;
        } else {
            if (hasZeroValue && value.equals(zeroValue)) return true;
            for (int i = valueTable.length - 1; i >= 0; i--)
                if (value.equals(valueTable[i])) return true;
        }
        return false;

    }

    public boolean containsKey(int key) {
        if (key == 0) return hasZeroValue;
        return locateKey(key) >= 0;
    }

    /**
     * Returns the key for the specified value, or <code>notFound</code> if it is not in the map. Note this traverses the entire map
     * and compares every value, which may be an expensive operation.
     *
     * @param identity If true, uses == to compare the specified value with values in the map. If false, uses
     *                 {@link #equals(Object)}.
     */
    public int findKey(@Null Object value, boolean identity, int notFound) {
        V[] valueTable = this.valueTable;
        if (value == null) {
            if (hasZeroValue && zeroValue == null) return 0;
            int[] keyTable = this.keyTable;
            for (int i = valueTable.length - 1; i >= 0; i--)
                if (keyTable[i] != 0 && valueTable[i] == null) return keyTable[i];
        } else if (identity) {
            if (value == zeroValue) return 0;
            for (int i = valueTable.length - 1; i >= 0; i--)
                if (valueTable[i] == value) return keyTable[i];
        } else {
            if (hasZeroValue && value.equals(zeroValue)) return 0;
            for (int i = valueTable.length - 1; i >= 0; i--)
                if (value.equals(valueTable[i])) return keyTable[i];
        }
        return notFound;
    }

    /**
     * Increases the size of the backing array to accommodate the specified number of additional items / loadFactor. Useful before
     * adding many items to avoid multiple backing array resizes.
     */
    public void ensureCapacity(int additionalCapacity) {
        int tableSize = tableSize(size + additionalCapacity, loadFactor);
        if (keyTable.length < tableSize) resize(tableSize);
    }

    private void resize(int newSize) {
        int oldCapacity = keyTable.length;
        threshold = (int) (newSize * loadFactor);
        mask = newSize - 1;
        shift = Long.numberOfLeadingZeros(mask);

        int[] oldKeyTable = keyTable;
        V[] oldValueTable = valueTable;

        keyTable = new int[newSize];
        valueTable = (V[]) new Object[newSize];

        if (size > 0) {
            for (int i = 0; i < oldCapacity; i++) {
                int key = oldKeyTable[i];
                if (key != 0) putResize(key, oldValueTable[i]);
            }
        }
    }

    public int hashCode() {
        int h = size;
        if (hasZeroValue && zeroValue != null) h += zeroValue.hashCode();
        int[] keyTable = this.keyTable;
        V[] valueTable = this.valueTable;
        for (int i = 0, n = keyTable.length; i < n; i++) {
            int key = keyTable[i];
            if (key != 0) {
                h += key * 31;
                V value = valueTable[i];
                if (value != null) h += value.hashCode();
            }
        }
        return h;
    }

    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof IntMap)) return false;
        IntMap other = (IntMap) obj;
        if (other.size != size) return false;
        if (other.hasZeroValue != hasZeroValue) return false;
        if (hasZeroValue) {
            if (other.zeroValue == null) {
                if (zeroValue != null) return false;
            } else {
                if (!other.zeroValue.equals(zeroValue)) return false;
            }
        }
        int[] keyTable = this.keyTable;
        V[] valueTable = this.valueTable;
        for (int i = 0, n = keyTable.length; i < n; i++) {
            int key = keyTable[i];
            if (key != 0) {
                V value = valueTable[i];
                if (value == null) {
                    if (other.get(key, ObjectMap.dummy) != null) return false;
                } else {
                    if (!value.equals(other.get(key))) return false;
                }
            }
        }
        return true;
    }

    /**
     * Uses == for comparison of each value.
     */
    public boolean equalsIdentity(@Null Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof IntMap)) return false;
        IntMap other = (IntMap) obj;
        if (other.size != size) return false;
        if (other.hasZeroValue != hasZeroValue) return false;
        if (hasZeroValue && zeroValue != other.zeroValue) return false;
        int[] keyTable = this.keyTable;
        V[] valueTable = this.valueTable;
        for (int i = 0, n = keyTable.length; i < n; i++) {
            int key = keyTable[i];
            if (key != 0 && valueTable[i] != other.get(key, ObjectMap.dummy)) return false;
        }
        return true;
    }

    public String toString() {
        if (size == 0) return "[]";
        StringBuilder buffer = new StringBuilder(32);
        buffer.append('[');
        int[] keyTable = this.keyTable;
        V[] valueTable = this.valueTable;
        int i = keyTable.length;
        if (hasZeroValue) {
            buffer.append("0=");
            buffer.append(zeroValue);
        } else {
            while (i-- > 0) {
                int key = keyTable[i];
                if (key == 0) continue;
                buffer.append(key);
                buffer.append('=');
                buffer.append(valueTable[i]);
                break;
            }
        }
        while (i-- > 0) {
            int key = keyTable[i];
            if (key == 0) continue;
            buffer.append(", ");
            buffer.append(key);
            buffer.append('=');
            buffer.append(valueTable[i]);
        }
        buffer.append(']');
        return buffer.toString();
    }

    public Iterator<Entry<V>> iterator() {
        return entries();
    }

    /**
     * Returns an iterator for the entries in the map. Remove is supported.
     * <p>
     * If {@link Collections#allocateIterators} is false, the same iterator instance is returned each time this method is called.
     * Use the {@link Entries} constructor for nested or multithreaded iteration.
     */
    public Entries<V> entries() {
        if (Collections.allocateIterators) return new Entries(this);
        if (entries1 == null) {
            entries1 = new Entries(this);
            entries2 = new Entries(this);
        }
        if (!entries1.valid) {
            entries1.reset();
            entries1.valid = true;
            entries2.valid = false;
            return entries1;
        }
        entries2.reset();
        entries2.valid = true;
        entries1.valid = false;
        return entries2;
    }

    /**
     * Returns an iterator for the values in the map. Remove is supported.
     * <p>
     * If {@link Collections#allocateIterators} is false, the same iterator instance is returned each time this method is called.
     * Use the {@link Entries} constructor for nested or multithreaded iteration.
     */
    public Values<V> values() {
        if (Collections.allocateIterators) return new Values(this);
        if (values1 == null) {
            values1 = new Values(this);
            values2 = new Values(this);
        }
        if (!values1.valid) {
            values1.reset();
            values1.valid = true;
            values2.valid = false;
            return values1;
        }
        values2.reset();
        values2.valid = true;
        values1.valid = false;
        return values2;
    }

    /**
     * Returns an iterator for the keys in the map. Remove is supported.
     * <p>
     * If {@link Collections#allocateIterators} is false, the same iterator instance is returned each time this method is called.
     * Use the {@link Entries} constructor for nested or multithreaded iteration.
     */
    public Keys keys() {
        if (Collections.allocateIterators) return new Keys(this);
        if (keys1 == null) {
            keys1 = new Keys(this);
            keys2 = new Keys(this);
        }
        if (!keys1.valid) {
            keys1.reset();
            keys1.valid = true;
            keys2.valid = false;
            return keys1;
        }
        keys2.reset();
        keys2.valid = true;
        keys1.valid = false;
        return keys2;
    }

    static public class Entry<V> {
        public int key;
        public @Null V value;

        public String toString() {
            return key + "=" + value;
        }
    }

    static private class MapIterator<V> {
        static final int INDEX_ZERO = -1;
        static private final int INDEX_ILLEGAL = -2;
        final IntMap<V> map;
        public boolean hasNext;
        int nextIndex, currentIndex;
        boolean valid = true;

        public MapIterator(IntMap<V> map) {
            this.map = map;
            reset();
        }

        public void reset() {
            currentIndex = INDEX_ILLEGAL;
            nextIndex = INDEX_ZERO;
            if (map.hasZeroValue)
                hasNext = true;
            else
                findNextIndex();
        }

        void findNextIndex() {
            int[] keyTable = map.keyTable;
            for (int n = keyTable.length; ++nextIndex < n; ) {
                if (keyTable[nextIndex] != 0) {
                    hasNext = true;
                    return;
                }
            }
            hasNext = false;
        }

        public void remove() {
            int i = currentIndex;
            if (i == INDEX_ZERO && map.hasZeroValue) {
                map.hasZeroValue = false;
                map.zeroValue = null;
            } else if (i < 0) {
                throw new IllegalStateException("next must be called before remove.");
            } else {
                int[] keyTable = map.keyTable;
                V[] valueTable = map.valueTable;
                int mask = map.mask, next = i + 1 & mask, key;
                while ((key = keyTable[next]) != 0) {
                    int placement = map.place(key);
                    if ((next - placement & mask) > (i - placement & mask)) {
                        keyTable[i] = key;
                        valueTable[i] = valueTable[next];
                        i = next;
                    }
                    next = next + 1 & mask;
                }
                keyTable[i] = 0;
                valueTable[i] = null;
                if (i != currentIndex) --nextIndex;
            }
            currentIndex = INDEX_ILLEGAL;
            map.size--;
        }
    }

    static public class Entries<V> extends MapIterator<V> implements Iterable<Entry<V>>, Iterator<Entry<V>> {
        private final Entry<V> entry = new Entry();

        public Entries(IntMap map) {
            super(map);
        }

        /**
         * Note the same entry instance is returned each time this method is called.
         */
        public Entry<V> next() {
            if (!hasNext) throw new NoSuchElementException();
            if (!valid) throw new RuntimeException("#iterator() cannot be used nested.");
            int[] keyTable = map.keyTable;
            if (nextIndex == INDEX_ZERO) {
                entry.key = 0;
                entry.value = map.zeroValue;
            } else {
                entry.key = keyTable[nextIndex];
                entry.value = map.valueTable[nextIndex];
            }
            currentIndex = nextIndex;
            findNextIndex();
            return entry;
        }

        public boolean hasNext() {
            if (!valid) throw new RuntimeException("#iterator() cannot be used nested.");
            return hasNext;
        }

        public Iterator<Entry<V>> iterator() {
            return this;
        }
    }

    static public class Values<V> extends MapIterator<V> implements Iterable<V>, Iterator<V> {
        public Values(IntMap<V> map) {
            super(map);
        }

        public boolean hasNext() {
            if (!valid) throw new RuntimeException("#iterator() cannot be used nested.");
            return hasNext;
        }

        public @Null V next() {
            if (!hasNext) throw new NoSuchElementException();
            if (!valid) throw new RuntimeException("#iterator() cannot be used nested.");
            V value;
            if (nextIndex == INDEX_ZERO)
                value = map.zeroValue;
            else
                value = map.valueTable[nextIndex];
            currentIndex = nextIndex;
            findNextIndex();
            return value;
        }

        public Iterator<V> iterator() {
            return this;
        }

        /**
         * Returns a new array containing the remaining values.
         */
        public Array<V> toArray() {
            Array array = new Array(true, map.size);
            while (hasNext)
                array.add(next());
            return array;
        }
    }

    static public class Keys extends MapIterator {
        public Keys(IntMap map) {
            super(map);
        }

        public int next() {
            if (!hasNext) throw new NoSuchElementException();
            if (!valid) throw new RuntimeException("#iterator() cannot be used nested.");
            int key = nextIndex == INDEX_ZERO ? 0 : map.keyTable[nextIndex];
            currentIndex = nextIndex;
            findNextIndex();
            return key;
        }

        /**
         * Returns a new array containing the remaining keys.
         */
        public IntArray toArray() {
            IntArray array = new IntArray(true, map.size);
            while (hasNext)
                array.add(next());
            return array;
        }

        /**
         * Adds the remaining values to the specified array.
         */
        public IntArray toArray(IntArray array) {
            while (hasNext)
                array.add(next());
            return array;
        }
    }
}
