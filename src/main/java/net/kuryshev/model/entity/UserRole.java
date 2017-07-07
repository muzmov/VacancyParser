package net.kuryshev.model.entity;

public enum UserRole {
    ADMIN, USER, NONE;

    @Override
    public String toString() {
        switch (this) {
            case ADMIN: return "ADMIN";
            case USER: return "USER";
            default: return "NONE";
        }
    }

    public static UserRole fromString(String value) {
        if (value == null) return UserRole.NONE;
        switch (value) {
            case "ADMIN": return UserRole.ADMIN;
            case "USER": return UserRole.USER;
            default: return UserRole.NONE;
        }
    }
}
