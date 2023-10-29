package array;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Represents a field of a class using Java reflection.
 */
public final class Field {

    private final java.lang.reflect.Field field;

    Field(java.lang.reflect.Field field) {
        this.field = field;
    }

    /**
     * Returns the name of the field.
     *
     * @return The name of the field.
     */
    public String getName() {
        return field.getName();
    }

    /**
     * Returns a Class object that identifies the declared type for the field.
     *
     * @return The Class object representing the declared type of the field.
     */
    public Class getType() {
        return field.getType();
    }

    /**
     * Returns the Class object representing the class or interface that declares the field.
     *
     * @return The declaring class of the field.
     */
    public Class getDeclaringClass() {
        return field.getDeclaringClass();
    }

    /**
     * Checks if the field is accessible.
     *
     * @return true if the field is accessible; otherwise, false.
     */
    public boolean isAccessible() {
        return field.isAccessible();
    }

    /**
     * Sets the accessibility of the field.
     *
     * @param accessible true to make the field accessible; otherwise, false.
     */
    public void setAccessible(boolean accessible) {
        field.setAccessible(accessible);
    }

    /**
     * Return true if the field does not include any of the {@code private}, {@code protected}, or {@code public} modifiers.
     *
     * @return true if the field has default (package-private) access; otherwise, false.
     */
    public boolean isDefaultAccess() {
        return !isPrivate() && !isProtected() && !isPublic();
    }

    /**
     * Return true if the field includes the {@code final} modifier.
     *
     * @return true if the field is marked as final; otherwise, false.
     */
    public boolean isFinal() {
        return Modifier.isFinal(field.getModifiers());
    }

    /**
     * Return true if the field includes the {@code private} modifier.
     *
     * @return true if the field is marked as private; otherwise, false.
     */
    public boolean isPrivate() {
        return Modifier.isPrivate(field.getModifiers());
    }

    /**
     * Return true if the field includes the {@code protected} modifier.
     *
     * @return true if the field is marked as protected; otherwise, false.
     */
    public boolean isProtected() {
        return Modifier.isProtected(field.getModifiers());
    }

    /**
     * Return true if the field includes the {@code public} modifier.
     *
     * @return true if the field is marked as public; otherwise, false.
     */
    public boolean isPublic() {
        return Modifier.isPublic(field.getModifiers());
    }

    /**
     * Return true if the field includes the {@code static} modifier.
     *
     * @return true if the field is marked as static; otherwise, false.
     */
    public boolean isStatic() {
        return Modifier.isStatic(field.getModifiers());
    }

    /**
     * Return true if the field includes the {@code transient} modifier.
     *
     * @return true if the field is marked as transient; otherwise, false.
     */
    public boolean isTransient() {
        return Modifier.isTransient(field.getModifiers());
    }

    /**
     * Return true if the field includes the {@code volatile} modifier.
     *
     * @return true if the field is marked as volatile; otherwise, false.
     */
    public boolean isVolatile() {
        return Modifier.isVolatile(field.getModifiers());
    }

    /**
     * Return true if the field is a synthetic field.
     *
     * @return true if the field is synthetic; otherwise, false.
     */
    public boolean isSynthetic() {
        return field.isSynthetic();
    }

    /**
     * If the type of the field is parameterized, returns the Class object representing the parameter type at the specified index,
     * null otherwise.
     *
     * @param index The index of the parameter type to retrieve.
     * @return The Class object representing the parameter type at the specified index, or null if not parameterized.
     */
    public Class getElementType(int index) {
        Type genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType) {
            Type[] actualTypes = ((ParameterizedType) genericType).getActualTypeArguments();
            if (actualTypes.length - 1 >= index) {
                Type actualType = actualTypes[index];
                if (actualType instanceof Class) return (Class) actualType;
                else if (actualType instanceof ParameterizedType)
                    return (Class) ((ParameterizedType) actualType).getRawType();
                else if (actualType instanceof GenericArrayType) {
                    Type componentType = ((GenericArrayType) actualType).getGenericComponentType();
                    if (componentType instanceof Class)
                        return ArrayReflection.newInstance((Class) componentType, 0).getClass();
                }
            }
        }
        return null;
    }

    /**
     * Returns true if the field includes an annotation of the provided class type.
     *
     * @param annotationType The class type of the annotation to check.
     * @return true if the field has the specified annotation; otherwise, false.
     */
    public boolean isAnnotationPresent(Class<? extends java.lang.annotation.Annotation> annotationType) {
        return field.isAnnotationPresent(annotationType);
    }

    /**
     * Returns an array of {@link Annotation} objects reflecting all annotations declared by this field, or an empty array if
     * there are none. Does not include inherited annotations.
     *
     * @return An array of Annotation objects representing the field's annotations.
     */
    public Annotation[] getDeclaredAnnotations() {
        java.lang.annotation.Annotation[] annotations = field.getDeclaredAnnotations();
        Annotation[] result = new Annotation[annotations.length];
        for (int i = 0; i < annotations.length; i++) {
            result[i] = new Annotation(annotations[i]);
        }
        return result;
    }

    /**
     * Returns an {@link Annotation} object reflecting the annotation provided, or null if this field doesn't have such an
     * annotation. This is a convenience function if the caller knows already which annotation type they're looking for.
     *
     * @param annotationType The class type of the annotation to retrieve.
     * @return An Annotation object representing the specified annotation, or null if not found.
     */
    public Annotation getDeclaredAnnotation(Class<? extends java.lang.annotation.Annotation> annotationType) {
        java.lang.annotation.Annotation[] annotations = field.getDeclaredAnnotations();
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

    /**
     * Returns the value of the field on the supplied object.
     *
     * @param obj The object from which to get the field's value.
     * @return The value of the field on the supplied object.
     * @throws ReflectionException If there is an issue getting the field's value.
     */
    public Object get(Object obj) throws ReflectionException {
        try {
            return field.get(obj);
        } catch (IllegalArgumentException e) {
            throw new ReflectionException("Object is not an instance of " + getDeclaringClass(), e);
        } catch (IllegalAccessException e) {
            throw new ReflectionException("Illegal access to field: " + getName(), e);
        }
    }


    /**
     * Sets the value of the field on the supplied object.
     *
     * @param obj   The object on which to set the field.
     * @param value The value to set in the field.
     * @throws ReflectionException If there is an issue setting the field's value.
     */
    public void set(Object obj, Object value) throws ReflectionException {
        try {
            field.set(obj, value);
        } catch (IllegalArgumentException e) {
            throw new ReflectionException("Argument not valid for field: " + getName(), e);
        } catch (IllegalAccessException e) {
            throw new ReflectionException("Illegal access to field: " + getName(), e);
        }
    }

}
