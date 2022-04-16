package com.berke.subscriptionmanager.entity.user;

public enum UserRole {
    ADMIN("Admin"),
    USER("User");

    private final String displayValue;

    private UserRole(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }


}