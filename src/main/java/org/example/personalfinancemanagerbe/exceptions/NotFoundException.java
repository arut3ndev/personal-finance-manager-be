package org.example.personalfinancemanagerbe.exceptions;

import lombok.Getter;
@Getter
public class NotFoundException extends RuntimeException
{
    private final String resourceType;
    private final Long id;
    public NotFoundException(String resourceType, Long id)
    {
        super("Not found " + resourceType + " with id: " + id);
        this.resourceType = resourceType;
        this.id = id;
    }
}
