package com.sid.app.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

/**
 * Represents a User entity stored in MongoDB.
 * <p>
 * Includes common profile fields, auditing, and validation constraints.
 * </p>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
public class User {

    /**
     * Unique identifier of the user (MongoDB ObjectId as String).
     */
    @Id
    private String id;

    /**
     * Name of the user. Cannot be blank, must have at least 2 characters.
     */
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    /**
     * Email of the user. Must be unique and a valid email address.
     */
    @Email(message = "Email must be valid")
    @NotBlank(message = "Email is required")
    @Indexed(unique = true)
    private String email;

    /**
     * Phone number (international format).
     */
    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Phone number is invalid")
    private String phone;

    /**
     * Role of the user (e.g., ADMIN, USER, GUEST).
     */
    @NotBlank(message = "Role is required")
    private String role;

    /**
     * Account status (e.g., ACTIVE, INACTIVE, SUSPENDED).
     */
    @Builder.Default
    private String status = "ACTIVE";

    /**
     * Address of the user.
     */
    private String address;

    /**
     * Timestamp when the user was created.
     */
    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")
    @Builder.Default
    private Instant createdAt = Instant.now();

    /**
     * Timestamp when the user was last updated.
     */
    @LastModifiedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")
    private Instant updatedAt;
}
