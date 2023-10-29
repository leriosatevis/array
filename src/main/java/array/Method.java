package array;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

/**
 * Provides utility methods for working with methods via reflection.
 */
public final class Method {

    private final java.lang.reflect.Method method;

    Method(java.lang.reflect.Method method) {
        this.method = method;
    }

    /**
     * Returns the name of the method.
     *
     * @return The name of the method.
     */
    public String getName() {
        return method.getName();
    }

    /**
     * Returns a Class object that represents the formal return type of the method.
     *
     * @return The Class object representing the return type of the method.
     */
    public Class getReturnType() {
        return method.getReturnType();
    }

    /**
     * Returns an array of Class objects that represent the formal parameter types, in declaration order, of the method.
     *
     * @return An array of Class objects representing the parameter types of the method.
     */
    public Class[] getParameterTypes() {
        return method.getParameterTypes();
    }

    /**
     * Returns the Class object representing the class or interface that declares the method.
     *
     * @return The Class object representing the declaring class of the method.
     */
    public Class getDeclaringClass() {
        return method.getDeclaringClass();
    }

    /**
     * Checks if the method is accessible.
     *
     * @return true if the method is accessible; otherwise, false.
     */
    public boolean isAccessible() {
        return method.isAccessible();
    }

    /**
     * Sets the accessibility of the method.
     *
     * @param accessible true to make the method accessible; false otherwise.
     */
    public void setAccessible(boolean accessible) {
        method.setAccessible(accessible);
    }

    /**
     * Returns true if the method includes the {@code abstract} modifier.
     *
     * @return true if the method is abstract; otherwise, false.
     */
    public boolean isAbstract() {
        return Modifier.isAbstract(method.getModifiers());
    }

    /**
     * Returns true if the method does not include any of the {@code private}, {@code protected}, or {@code public} modifiers.
     *
     * @return true if the method has default access; otherwise, false.
     */
    public boolean isDefaultAccess() {
        return !isPrivate() && !isProtected() && !isPublic();
    }

    /**
     * Returns true if the method includes the {@code final} modifier.
     *
     * @return true if the method is final; otherwise, false.
     */
    public boolean isFinal() {
        return Modifier.isFinal(method.getModifiers());
    }

    /**
     * Returns true if the method includes the {@code private} modifier.
     *
     * @return true if the method is private; otherwise, false.
     */
    public boolean isPrivate() {
        return Modifier.isPrivate(method.getModifiers());
    }

    /**
     * Returns true if the method includes the {@code protected} modifier.
     *
     * @return true if the method is protected; otherwise, false.
     */
    public boolean isProtected() {
        return Modifier.isProtected(method.getModifiers());
    }

    /**
     * Returns true if the method includes the {@code public} modifier.
     *
     * @return true if the method is public; otherwise, false.
     */
    public boolean isPublic() {
        return Modifier.isPublic(method.getModifiers());
    }

    /**
     * Returns true if the method includes the {@code native} modifier.
     *
     * @return true if the method is native; otherwise, false.
     */
    public boolean isNative() {
        return Modifier.isNative(method.getModifiers());
    }

    /**
     * Returns true if the method includes the {@code static} modifier.
     *
     * @return true if the method is static; otherwise, false.
     */
    public boolean isStatic() {
        return Modifier.isStatic(method.getModifiers());
    }

    /**
     * Returns true if the method takes a variable number of arguments.
     *
     * @return true if the method is a varargs method; otherwise, false.
     */
    public boolean isVarArgs() {
        return method.isVarArgs();
    }

    /**
     * Invokes the underlying method on the supplied object with the supplied parameters.
     *
     * @param obj  The object on which to invoke the method.
     * @param args The arguments to pass to the method.
     * @return The result of invoking the method.
     * @throws ReflectionException If an error occurs during method invocation.
     */
    public Object invoke(Object obj, Object... args) throws ReflectionException {
        try {
            return method.invoke(obj, args);
        } catch (IllegalArgumentException e) {
            throw new ReflectionException("Illegal argument(s) supplied to method: " + getName(), e);
        } catch (IllegalAccessException e) {
            throw new ReflectionException("Illegal access to method: " + getName(), e);
        } catch (InvocationTargetException e) {
            throw new ReflectionException("Exception occurred in method: " + getName(), e);
        }
    }

    /**
     * Checks if the method includes a specific annotation.
     *
     * @param annotationType The class type of the annotation to check.
     * @return true if the method includes the provided annotation; otherwise, false.
     */
    public boolean isAnnotationPresent(Class<? extends java.lang.annotation.Annotation> annotationType) {
        return method.isAnnotationPresent(annotationType);
    }

    /**
     * Returns an array of annotations declared directly on this method.
     *
     * @return An array of Annotation objects representing annotations declared on this method.
     * Returns an empty array if there are none.
     * Does not include inherited annotations or parameter annotations.
     */
    public Annotation[] getDeclaredAnnotations() {
        java.lang.annotation.Annotation[] annotations = method.getDeclaredAnnotations();
        Annotation[] result = new Annotation[annotations.length];
        for (int i = 0; i < annotations.length; i++) {
            result[i] = new Annotation(annotations[i]);
        }
        return result;
    }

    /**
     * Returns a specific annotation declared on this method if it exists.
     *
     * @param annotationType The class type of the annotation to retrieve.
     * @return An Annotation object reflecting the provided annotation, or null if this method does not have such an annotation.
     * This is a convenience function for cases where the caller knows the desired annotation type.
     */
    public Annotation getDeclaredAnnotation(Class<? extends java.lang.annotation.Annotation> annotationType) {
        java.lang.annotation.Annotation[] annotations = method.getDeclaredAnnotations();
        if (annotations == null) {
            return null;
        }
        for (java.lang.annotation.Annotation annotation : annotations) {
            if (annotation.annotationType().equals(annotationType)) {
                return new Annotation(annotation);
            }
        }
        return null;
    }

}
