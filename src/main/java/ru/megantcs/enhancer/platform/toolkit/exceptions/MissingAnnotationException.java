package ru.megantcs.enhancer.api.core.exceptions;

public class MissingAnnotationException extends RuntimeException {
    public MissingAnnotationException(String message) {
        super(message);
    }

    public MissingAnnotationException(Class<?> clazz) {
        super(clazz.getName());
    }
}
