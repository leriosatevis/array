package utils;

import array.ClassReflection;
import array.Constructor;
import array.ReflectionException;

/**
 * An object pool specialized for creating new instances of a specified type using reflection.
 * <p>
 * This pool requires the type to have a zero-argument constructor. If the class or constructor is not publicly accessible,
 * {@link Constructor#setAccessible(boolean)} will be invoked to bypass Java's access control.
 * </p>
 *
 * @param <T> The type of objects that this pool will hold.
 */
public class ReflectionPool<T> extends Pool<T> {

    /**
     * The constructor of the class type, used for creating new instances.
     */
    private final Constructor constructor;

    /**
     * Constructs a new ReflectionPool for the specified type with an initial capacity of 16 and no maximum size limit.
     *
     * @param type The class type for which the pool is to be created.
     */
    public ReflectionPool(Class<T> type) {
        this(type, 16, Integer.MAX_VALUE);
    }

    /**
     * Constructs a new ReflectionPool for the specified type with the given initial capacity and no maximum size limit.
     *
     * @param type            The class type for which the pool is to be created.
     * @param initialCapacity The initial capacity of the pool.
     */
    public ReflectionPool(Class<T> type, int initialCapacity) {
        this(type, initialCapacity, Integer.MAX_VALUE);
    }

    /**
     * Constructs a new ReflectionPool for the specified type with the given initial capacity and maximum size limit.
     *
     * @param type            The class type for which the pool is to be created.
     * @param initialCapacity The initial capacity of the pool.
     * @param max             The maximum size of the pool.
     */
    public ReflectionPool(Class<T> type, int initialCapacity, int max) {
        super(initialCapacity, max);
        constructor = findConstructor(type);
        if (constructor == null) {
            throw new RuntimeException("Class cannot be created (missing no-arg constructor): " + type.getName());
        }
    }

    /**
     * Finds and returns the zero-argument constructor for the specified class type.
     *
     * @param type The class type for which the constructor is to be found.
     * @return The zero-argument constructor, or null if not found.
     */
    private Constructor findConstructor(Class<T> type) {
        try {
            return ClassReflection.getConstructor(type, (Class[]) null);
        } catch (Exception ex1) {
            try {
                Constructor constructor = ClassReflection.getDeclaredConstructor(type, (Class[]) null);
                constructor.setAccessible(true);
                return constructor;
            } catch (ReflectionException ex2) {
                return null;
            }
        }
    }

    /**
     * Creates a new instance of the object using the zero-argument constructor.
     *
     * @return The newly created object.
     */
    @Override
    protected T newObject() {
        try {
            return (T) constructor.newInstance((Object[]) null);
        } catch (Exception ex) {
            throw new RuntimeException("Unable to create new instance: " + constructor.getDeclaringClass().getName(), ex);
        }
    }
}
