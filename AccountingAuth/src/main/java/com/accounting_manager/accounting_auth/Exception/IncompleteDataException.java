package com.accounting_manager.accounting_auth.Exception;

public class IncompleteDataException extends RuntimeException {
    public IncompleteDataException() {
        super("Data is incomplete.");
    }

    public IncompleteDataException(String message) {
        super(message);
    }
}
