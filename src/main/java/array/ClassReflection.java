package array;

import java.lang.reflect.Modifier;

/**
 * Provides reflection-related utility methods for working with classes.
 */
public final class ClassReflection {

    /**
     * Returns the Class object associated with the class or interface with the supplied string name.
     *
     * @param name The fully qualified name of the class or interface.
     * @return The Class object associated with the specified name.
     * @throws ReflectionException If the class is not found.
     */
    public static Class forName(String name) throws ReflectionException {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            throw new ReflectionException("Class not found: " + name, e);
        }
    }

    /**
     * Returns the simple name of the underlying class as supplied in the source code.
     *
     * @param c The Class object for which to get the simple name.
     * @return The simple name of the class.
     */
    public static String getSimpleName(Class c) {
        return c.getSimpleName();
    }

    /**
     * Determines if the supplied Object is assignment-compatible with the object represented by supplied Class.
     *
     * @param c   The Class object.
     * @param obj The object to check compatibility with.
     * @return true if the object is assignment-compatible with the class represented by c; otherwise, false.
     */
    public static boolean isInstance(Class c, Object obj) {
        return c.isInstance(obj);
    }

    /**
     * Determines if one class is assignable from another class.
     *
     * @param c1 The first Class object.
     * @param c2 The second Class object.
     * @return true if c2 is either the same as, or a subclass or superinterface of c1; otherwise, false.
     */
    public static boolean isAssignableFrom(Class c1, Class c2) {
        return c1.isAssignableFrom(c2);
    }

    /**
     * Returns true if the class or interface represented by the supplied Class is a member class.
     *
     * @param c The Class object.
     * @return true if the class is a member class; otherwise, false.
     */
    public static boolean isMemberClass(Class c) {
        return c.isMemberClass();
    }

    /**
     * Returns true if the class or interface represented by the supplied Class is a static class.
     *
     * @param c The Class object.
     * @return true if the class is a static class; otherwise, false.
     */
    public static boolean isStaticClass(Class c) {
        return Modifier.isStatic(c.getModifiers());
    }

    /**
     * Determines if the supplied Class object represents an array class.
     *
     * @param c The Class object.
     * @return true if the class represents an array; otherwise, false.
     */
    public static boolean isArray(Class c) {
        return c.isArray();
    }

    /**
     * Determines if the supplied Class object represents a primitive type.
     *
     * @param c The Class object.
     * @return true if the class represents a primitive type; otherwise, false.
     */
    public static boolean isPrimitive(Class c) {
        return c.isPrimitive();
    }

    /**
     * Determines if the supplied Class object represents an enum type.
     *
     * @param c The Class object.
     * @return true if the class represents an enum type; otherwise, false.
     */
    public static boolean isEnum(Class c) {
        return c.isEnum();
    }

    /**
     * Determines if the supplied Class object represents an annotation type.
     *
     * @param c The Class object.
     * @return true if the class represents an annotation type; otherwise, false.
     */
    public static boolean isAnnotation(Class c) {
        return c.isAnnotation();
    }

    /**
     * Determines if the supplied Class object represents an interface type.
     *
     * @param c The Class object.
     * @return true if the class represents an interface type; otherwise, false.
     */
    public static boolean isInterface(Class c) {
        return c.isInterface();
    }

    /**
     * Determines if the supplied Class object represents an abstract type.
     *
     * @param c The Class object.
     * @return true if the class represents an abstract type; otherwise, false.
     */
    public static boolean isAbstract(Class c) {
        return Modifier.isAbstract(c.getModifiers());
    }

    /**
     * Creates a new instance of the class represented by the supplied Class.
     *
     * @param <T> The type of the object to be instantiated.
     * @param c   The Class object.
     * @return A new instance of the specified class.
     * @throws ReflectionException If instantiation fails.
     */
    public static <T> T newInstance(Class<T> c) throws ReflectionException {
        try {
            return c.newInstance();
        } catch (InstantiationException e) {
            throw new ReflectionException("Could not instantiate instance of class: " + c.getName(), e);
        } catch (IllegalAccessException e) {
            throw new ReflectionException("Could not instantiate instance of class: " + c.getName(), e);
        }
    }

    /**
     * Returns the Class representing the component type of an array. If this class does not represent an array class, this method returns null.
     *
     * @param c The Class object.
     * @return The Class representing the component type of an array, or null if the class is not an array.
     */
    public static Class getComponentType(Class c) {
        return c.getComponentType();
    }

    /**
     * Returns an array of Constructor containing the public constructors of the class represented by the supplied Class.
     *
     * @param c The Class object.
     * @return An array of Constructor objects representing the public constructors.
     */
    public static Constructor[] getConstructors(Class c) {
        java.lang.reflect.Constructor[] constructors = c.getConstructors();
        Constructor[] result = new Constructor[constructors.length];
        for (int i = 0, j = constructors.length; i < j; i++) {
            result[i] = new Constructor(constructors[i]);
        }
        return result;
    }

    /**
     * Returns a Constructor that represents the public constructor for the supplied class which takes the supplied parameter types.
     *
     * @param c              The Class object.
     * @param parameterTypes The parameter types of the constructor.
     * @return A Constructor object representing the public constructor.
     * @throws ReflectionException If the constructor cannot be found.
     */
    public static Constructor getConstructor(Class c, Class... parameterTypes) throws ReflectionException {
        try {
            return new Constructor(c.getConstructor(parameterTypes));
        } catch (SecurityException e) {
            throw new ReflectionException("Security violation occurred while getting constructor for class: '" + c.getName() + "'.",
                    e);
        } catch (NoSuchMethodException e) {
            throw new ReflectionException("Constructor not found for class: " + c.getName(), e);
        }
    }

    /**
     * Returns a Constructor that represents the constructor for the supplied class which takes the supplied parameter types.
     *
     * @param c              The Class object.
     * @param parameterTypes The parameter types of the constructor.
     * @return A Constructor object representing the constructor.
     * @throws ReflectionException If the constructor cannot be found.
     */
    public static Constructor getDeclaredConstructor(Class c, Class... parameterTypes) throws ReflectionException {
        try {
            return new Constructor(c.getDeclaredConstructor(parameterTypes));
        } catch (SecurityException e) {
            throw new ReflectionException("Security violation while getting constructor for class: " + c.getName(), e);
        } catch (NoSuchMethodException e) {
            throw new ReflectionException("Constructor not found for class: " + c.getName(), e);
        }
    }

    /**
     * Returns the elements of this enum class or null if this Class object does not represent an enum type.
     *
     * @param c The Class object.
     * @return An array of enum constants, or null if the class is not an enum.
     */
    public static Object[] getEnumConstants(Class c) {
        return c.getEnumConstants();
    }

    /**
     * Returns an array of Method containing the public member methods of the class represented by the supplied Class.
     *
     * @param c The Class object.
     * @return An array of Method objects representing the public methods.
     */
    public static Method[] getMethods(Class c) {
        java.lang.reflect.Method[] methods = c.getMethods();
        Method[] result = new Method[methods.length];
        for (int i = 0, j = methods.length; i < j; i++) {
            result[i] = new Method(methods[i]);
        }
        return result;
    }

    /**
     * Returns a Method that represents the public member method for the supplied class with the specified name and parameter types.
     *
     * @param c              The Class object.
     * @param name           The name of the method.
     * @param parameterTypes The parameter types of the method.
     * @return A Method object representing the public method.
     * @throws ReflectionException If the method cannot be found.
     */
    public static Method getMethod(Class c, String name, Class... parameterTypes) throws ReflectionException {
        try {
            return new Method(c.getMethod(name, parameterTypes));
        } catch (SecurityException e) {
            throw new ReflectionException("Security violation while getting method: " + name + ", for class: " + c.getName(), e);
        } catch (NoSuchMethodException e) {
            throw new ReflectionException("Method not found: " + name + ", for class: " + c.getName(), e);
        }
    }

    /**
     * Returns an array of Method containing the methods declared by the class represented by the supplied Class.
     *
     * @param c The Class object.
     * @return An array of Method objects representing the declared methods.
     */
    public static Method[] getDeclaredMethods(Class c) {
        java.lang.reflect.Method[] methods = c.getDeclaredMethods();
        Method[] result = new Method[methods.length];
        for (int i = 0, j = methods.length; i < j; i++) {
            result[i] = new Method(methods[i]);
        }
        return result;
    }

    /**
     * Returns a Method that represents the method declared by the supplied class which takes the supplied parameter types.
     *
     * @param c              The Class object.
     * @param name           The name of the method.
     * @param parameterTypes The parameter types of the method.
     * @return A Method object representing the declared method.
     * @throws ReflectionException If the method cannot be found.
     */
    public static Method getDeclaredMethod(Class c, String name, Class... parameterTypes) throws ReflectionException {
        try {
            return new Method(c.getDeclaredMethod(name, parameterTypes));
        } catch (SecurityException e) {
            throw new ReflectionException("Security violation while getting method: " + name + ", for class: " + c.getName(), e);
        } catch (NoSuchMethodException e) {
            throw new ReflectionException("Method not found: " + name + ", for class: " + c.getName(), e);
        }
    }

    /**
     * Returns an array of Field containing the public fields of the class represented by the supplied Class.
     *
     * @param c The Class object.
     * @return An array of Field objects representing the public fields.
     */
    public static Field[] getFields(Class c) {
        java.lang.reflect.Field[] fields = c.getFields();
        Field[] result = new Field[fields.length];
        for (int i = 0, j = fields.length; i < j; i++) {
            result[i] = new Field(fields[i]);
        }
        return result;
    }

    /**
     * Returns a Field that represents the specified public member field for the supplied class.
     *
     * @param c    The Class object.
     * @param name The name of the field.
     * @return A Field object representing the specified public field.
     * @throws ReflectionException If the field cannot be found.
     */
    public static Field getField(Class c, String name) throws ReflectionException {
        try {
            return new Field(c.getField(name));
        } catch (SecurityException e) {
            throw new ReflectionException("Security violation while getting field: " + name + ", for class: " + c.getName(), e);
        } catch (NoSuchFieldException e) {
            throw new ReflectionException("Field not found: " + name + ", for class: " + c.getName(), e);
        }
    }

    /**
     * Returns an array of Field objects reflecting all the fields declared by the supplied class.
     *
     * @param c The Class object.
     * @return An array of Field objects representing the declared fields.
     */
    public static Field[] getDeclaredFields(Class c) {
        java.lang.reflect.Field[] fields = c.getDeclaredFields();
        Field[] result = new Field[fields.length];
        for (int i = 0, j = fields.length; i < j; i++) {
            result[i] = new Field(fields[i]);
        }
        return result;
    }

    /**
     * Returns a Field that represents the specified declared field for the supplied class.
     *
     * @param c    The Class object.
     * @param name The name of the field.
     * @return A Field object representing the specified declared field.
     * @throws ReflectionException If the field cannot be found.
     */
    public static Field getDeclaredField(Class c, String name) throws ReflectionException {
        try {
            return new Field(c.getDeclaredField(name));
        } catch (SecurityException e) {
            throw new ReflectionException("Security violation while getting field: " + name + ", for class: " + c.getName(), e);
        } catch (NoSuchFieldException e) {
            throw new ReflectionException("Field not found: " + name + ", for class: " + c.getName(), e);
        }
    }

    /**
     * Returns true if the supplied class includes an annotation of the given type.
     *
     * @param c              The Class object.
     * @param annotationType The type of the annotation.
     * @return true if the class includes the specified annotation; otherwise, false.
     */
    public static boolean isAnnotationPresent(Class c, Class<? extends java.lang.annotation.Annotation> annotationType) {
        return c.isAnnotationPresent(annotationType);
    }

    /**
     * Returns an array of Annotation objects reflecting all annotations declared by the supplied class, and inherited from its superclass.
     * Returns an empty array if there are none.
     *
     * @param c The Class object.
     * @return An array of Annotation objects representing the annotations.
     */
    public static Annotation[] getAnnotations(Class c) {
        java.lang.annotation.Annotation[] annotations = c.getAnnotations();
        Annotation[] result = new Annotation[annotations.length];
        for (int i = 0; i < annotations.length; i++) {
            result[i] = new Annotation(annotations[i]);
        }
        return result;
    }

    /**
     * Returns an Annotation object reflecting the annotation provided, or null if this class doesn't have such an annotation.
     * This is a convenience function if the caller knows already which annotation type he's looking for.
     *
     * @param c              The Class object.
     * @param annotationType The type of the annotation.
     * @return An Annotation object representing the specified annotation, or null if not found.
     */
    public static Annotation getAnnotation(Class c, Class<? extends java.lang.annotation.Annotation> annotationType) {
        java.lang.annotation.Annotation annotation = c.getAnnotation(annotationType);
        if (annotation != null) return new Annotation(annotation);
        return null;
    }

    /**
     * Returns an array of Annotation objects reflecting all annotations declared by the supplied class, or an empty array if there are none.
     * Does not include inherited annotations.
     *
     * @param c The Class object.
     * @return An array of Annotation objects representing the declared annotations.
     */
    public static Annotation[] getDeclaredAnnotations(Class c) {
        java.lang.annotation.Annotation[] annotations = c.getDeclaredAnnotations();
        Annotation[] result = new Annotation[annotations.length];
        for (int i = 0; i < annotations.length; i++) {
            result[i] = new Annotation(annotations[i]);
        }
        return result;
    }

    /**
     * Returns an Annotation object reflecting the annotation provided, or null if this class doesn't have such an annotation.
     * This is a convenience function if the caller knows already which annotation type he's looking for.
     *
     * @param c              The Class object.
     * @param annotationType The type of the annotation.
     * @return An Annotation object representing the specified annotation, or null if not found.
     */
    public static Annotation getDeclaredAnnotation(Class c, Class<? extends java.lang.annotation.Annotation> annotationType) {
        java.lang.annotation.Annotation[] annotations = c.getDeclaredAnnotations();
        for (java.lang.annotation.Annotation annotation : annotations) {
            if (annotation.annotationType().equals(annotationType)) return new Annotation(annotation);
        }
        return null;
    }

    /**
     * Returns an array of Class objects representing the interfaces implemented by the supplied class.
     *
     * @param c The Class object.
     * @return An array of Class objects representing the implemented interfaces.
     */
    public static Class[] getInterfaces(Class c) {
        return c.getInterfaces();
    }

}
