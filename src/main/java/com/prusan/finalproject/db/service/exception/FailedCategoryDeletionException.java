package com.prusan.finalproject.db.service.exception;

/**
 * Exception for cases when a category can not be deleted (this happens very likely because a category can not be deleted while it has at least one activity associated with it).
 */
public class FailedCategoryDeletionException extends ServiceException {
    public FailedCategoryDeletionException() {
        super();
    }

    public FailedCategoryDeletionException(String message) {
        super(message);
    }

    public FailedCategoryDeletionException(String message, Throwable cause) {
        super(message, cause);
    }

    public FailedCategoryDeletionException(Throwable cause) {
        super(cause);
    }
}
