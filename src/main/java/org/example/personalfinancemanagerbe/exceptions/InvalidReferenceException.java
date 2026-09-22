package org.example.personalfinancemanagerbe.exceptions;

import lombok.Getter;

@Getter
public class InvalidReferenceException extends RuntimeException {
    private final Long id;
    public InvalidReferenceException(Long id) {
        super("Invalid reference for id: " + id);
        this.id = id;
    }
}
