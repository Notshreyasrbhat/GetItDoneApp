package com.project.getitdone.util

object InputValidator {

    /**
     * Validates user input for tasks/lists.
     *
     * Rules:
     * - Input must not be null or empty (after trimming spaces).
     * - Input must be at least 2 characters long.
     *
     * Returns true if input is considered valid.
     */
    fun isInputValid(input: String?): Boolean {
        return !input?.trim().isNullOrEmpty() && (input?.length ?: 0) > 1
    }
}
