package array;

/**
 * Utility class for working with arrays using Java reflection.
 */
public final class ArrayReflection {

    /**
     * Creates a new array with the specified component type and length.
     *
     * @param c    The class type of the array's components.
     * @param size The length or size of the new array.
     * @return A new array with the specified component type and length.
     */
    public static Object newInstance(Class c, int size) {
        return java.lang.reflect.Array.newInstance(c, size);
    }


    /**
     * Returns the length of the supplied array.
     *
     * @param array The array for which to get the length.
     * @return The length of the supplied array.
     */
    public static int getLength(Object array) {
        return java.lang.reflect.Array.getLength(array);
    }


    /**
     * Returns the value of the indexed component in the supplied array.
     *
     * @param array The array from which to retrieve the value.
     * @param index The index of the component to get.
     * @return The value of the indexed component in the supplied array.
     */
    public static Object get(Object array, int index) {
        return java.lang.reflect.Array.get(array, index);
    }

    /**
     * Sets the value of the indexed component in the supplied array to the supplied value.
     *
     * @param array The array in which to set the value.
     * @param index The index of the component to set.
     * @param value The value to set in the indexed component.
     */
    public static void set(Object array, int index, Object value) {
        java.lang.reflect.Array.set(array, index, value);
    }

}
