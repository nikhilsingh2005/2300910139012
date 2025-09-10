package com.nikhil.project.exception;

public class ShortcodeNotFoundException extends RuntimeException {
    public ShortcodeNotFoundException(String message) {
        super(message);
    }
}
