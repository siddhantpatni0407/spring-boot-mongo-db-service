package com.sid.app.constant;

/**
 * Application-wide constants.
 */
public final class AppConstants {

    private AppConstants() {
        // prevent instantiation
    }

    // API base paths
    public static final String BASE_API = "/api/v1/spring-boot-mongo-db-service";
    public static final String USERS_API = BASE_API + "/users";

    // Response statuses
    public static final String STATUS_SUCCESS = "SUCCESS";
    public static final String STATUS_ERROR = "ERROR";

    // Response messages
    public static final String MSG_USERS_FETCHED = "Users fetched successfully";
    public static final String MSG_USER_FETCHED = "User fetched successfully";
    public static final String MSG_USER_CREATED = "User created successfully";
    public static final String MSG_USER_UPDATED = "User updated successfully";
    public static final String MSG_USER_DELETED = "User deleted successfully";
}
