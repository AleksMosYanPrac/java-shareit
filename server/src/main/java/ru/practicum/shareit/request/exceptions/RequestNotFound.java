package ru.practicum.shareit.request.exceptions;

public class RequestNotFound extends Exception {
    public RequestNotFound(long requestId) {
        super("Item request not found with ID: " + requestId);
    }
}
