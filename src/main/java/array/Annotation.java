package array;

/**
 * Provides information about, and access to, an annotation of a field, class, or interface.
 * Wraps a native Java annotation object for additional utility.
 */
public final class Annotation {

    // The native Java annotation this class wraps
    private java.lang.annotation.Annotation annotation;

    /**
     * Constructs an Annotation object that wraps a native Java annotation.
     *
     * @param annotation the native Java annotation to wrap
     */
    Annotation(java.lang.annotation.Annotation annotation) {
        this.annotation = annotation;
    }

    /**
     * Retrieves the wrapped annotation, cast to the specified annotation type.
     *
     * @param <T>            the type of annotation to return
     * @param annotationType the Class object corresponding to the annotation type
     * @return the wrapped annotation if it matches the specified type; null otherwise
     */
    @SuppressWarnings("unchecked")
    public <T extends java.lang.annotation.Annotation> T getAnnotation(Class<T> annotationType) {
        if (annotation.annotationType().equals(annotationType)) {
            return (T) annotation;
        }
        return null;
    }

    /**
     * Retrieves the type of the wrapped annotation.
     *
     * @return the Class object that represents the type of the wrapped annotation
     */
    public Class<? extends java.lang.annotation.Annotation> getAnnotationType() {
        return annotation.annotationType();
    }
}
