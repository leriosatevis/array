package array;

import java.lang.reflect.InvocationTargetException;

/**
 * Provides utility methods for working with constructors via reflection.
 */
public final class Constructor {

    private final java.lang.reflect.Constructor constructor;

    Constructor(java.lang.reflect.Constructor constructor) {
        this.constructor = constructor;
    }

    /**
     * Returns an array of Class objects that represent the formal parameter types, in declaration order, of the constructor.
     *
     * @return An array of Class objects representing the parameter types of the constructor.
     */
    public Class[] getParameterTypes() {
        return constructor.getParameterTypes();
    }

    /**
     * Returns the Class object representing the class or interface that declares the constructor.
     *
     * @return The Class object representing the declaring class of the constructor.
     */
    public Class getDeclaringClass() {
        return constructor.getDeclaringClass();
    }

    /**
     * Checks if the constructor is accessible.
     *
     * @return true if the constructor is accessible; otherwise, false.
     */
    public boolean isAccessible() {
        return constructor.isAccessible();
    }

    /**
     * Sets the accessibility of the constructor.
     *
     * @param accessible true to make the constructor accessible; false otherwise.
     */
    public void setAccessible(boolean accessible) {
        constructor.setAccessible(accessible);
    }

    /**
     * Uses the constructor to create and initialize a new instance of the constructor's declaring class with the supplied
     * initialization parameters.
     *
     * @param args The initialization parameters.
     * @return A new instance of the declaring class.
     * @throws ReflectionException If an error occurs during instantiation.
     */
    public Object newInstance(Object... args) throws ReflectionException {
        try {
            return constructor.newInstance(args);
        } catch (IllegalArgumentException e) {
            throw new ReflectionException("Illegal argument(s) supplied to constructor for class: " + getDeclaringClass().getName(), e);
        } catch (InstantiationException e) {
            throw new ReflectionException("Could not instantiate instance of class: " + getDeclaringClass().getName(), e);
        } catch (IllegalAccessException e) {
            throw new ReflectionException("Could not instantiate instance of class: " + getDeclaringClass().getName(), e);
        } catch (InvocationTargetException e) {
            throw new ReflectionException("Exception occurred in constructor for class: " + getDeclaringClass().getName(), e);
        }
    }

}
