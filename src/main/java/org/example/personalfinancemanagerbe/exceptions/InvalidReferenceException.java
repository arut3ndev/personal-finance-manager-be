package org.example.personalfinancemanagerbe.exceptions;

import lombok.Getter;

@Getter
public class InvalidReferenceException extends RuntimeException {
    private final String resourceType;
    private final Long id;

    public InvalidReferenceException(String resourceType, Long id) {
        super("Invalid reference for " + resourceType + " with id: " + id);
        this.id = id;
        this.resourceType = resourceType;
    }
}
