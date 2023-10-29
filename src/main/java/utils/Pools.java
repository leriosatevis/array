package utils;

import array.Array;

/**
 * A utility class that manages object pools, typically {@link ReflectionPool}, mapped by their class types.
 * <p>
 * This class provides a centralized way to obtain and manage object pools. It's particularly useful
 * for reducing object creation overhead.
 * </p>
 */
public class Pools {

    /**
     * A map that holds the association between a class type and its corresponding object pool.
     */
    private static final ObjectMap<Class, Pool> typePools = new ObjectMap<>();

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private Pools() {
    }

    /**
     * Retrieves or creates a new object pool for the specified class type.
     * <p>
     * If the pool for the given type does not exist, a new pool is created and stored.
     * The maximum size of the pool is set only if the pool is newly created.
     * </p>
     *
     * @param type The class type for which the pool is to be obtained.
     * @param max  The maximum size of the pool.
     * @param <T>  The type of objects that the pool will hold.
     * @return The existing or newly created object pool.
     */
    public static <T> Pool<T> get(Class<T> type, int max) {
        Pool pool = typePools.get(type);
        if (pool == null) {
            pool = new ReflectionPool<>(type, 4, max);
            typePools.put(type, pool);
        }
        return pool;
    }

    /**
     * Retrieves or creates a new object pool for the specified class type with a default maximum size of 100.
     *
     * @param type The class type for which the pool is to be obtained.
     * @param <T>  The type of objects that the pool will hold.
     * @return The existing or newly created object pool.
     */
    public static <T> Pool<T> get(Class<T> type) {
        return get(type, 100);
    }

    /**
     * Registers an existing object pool for a specified class type.
     *
     * @param type The class type for which the pool is to be registered.
     * @param pool The object pool to register.
     * @param <T>  The type of objects that the pool holds.
     */
    public static <T> void set(Class<T> type, Pool<T> pool) {
        typePools.put(type, pool);
    }

    /**
     * Obtains an object from the pool corresponding to the specified class type.
     *
     * @param type The class type of the object to be obtained.
     * @param <T>  The type of the object.
     * @return The object obtained from the pool.
     */
    public static <T> T obtain(Class<T> type) {
        return get(type).obtain();
    }

    /**
     * Releases an object back to its corresponding pool.
     *
     * @param object The object to be released.
     */
    public static void free(Object object) {
        if (object == null) throw new IllegalArgumentException("object cannot be null.");
        Pool pool = typePools.get(object.getClass());
        if (pool == null) return;
        pool.free(object);
    }

    /**
     * Releases all objects in the given array back to their corresponding pools.
     *
     * @param objects The array of objects to be released.
     */
    public static void freeAll(Array objects) {
        freeAll(objects, false);
    }

    /**
     * Releases all objects in the given array back to their corresponding pools.
     *
     * @param objects  The array of objects to be released.
     * @param samePool If true, all objects are assumed to belong to the same pool.
     */
    public static void freeAll(Array objects, boolean samePool) {
        if (objects == null) throw new IllegalArgumentException("objects cannot be null.");
        Pool pool = null;
        for (int i = 0, n = objects.size; i < n; i++) {
            Object object = objects.get(i);
            if (object == null) continue;
            if (pool == null) {
                pool = typePools.get(object.getClass());
                if (pool == null) continue;
            }
            pool.free(object);
            if (!samePool) pool = null;
        }
    }
}
