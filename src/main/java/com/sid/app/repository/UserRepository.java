package com.sid.app.repository;

import com.sid.app.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for {@link User} entities.
 * <p>
 * Extends {@link MongoRepository} to provide built-in CRUD methods and
 * custom query derivations based on method names.
 * </p>
 */
public interface UserRepository extends MongoRepository<User, String> {

    /**
     * Finds a user by email.
     *
     * @param email user email
     * @return optional of User
     */
    Optional<User> findByEmail(String email);

    /**
     * Checks whether a user with the given email exists.
     *
     * @param email user email
     * @return true if a user exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Finds all users with a given role.
     *
     * @param role role of the users
     * @return list of users
     */
    List<User> findByRole(String role);

    /**
     * Finds all users by status.
     *
     * @param status ACTIVE, INACTIVE, SUSPENDED, etc.
     * @return list of users
     */
    List<User> findByStatus(String status);

    /**
     * Finds all users whose name contains the given keyword (case-insensitive).
     *
     * @param namePart partial name
     * @return list of matching users
     */
    List<User> findByNameContainingIgnoreCase(String namePart);

    /**
     * Custom MongoDB query: Find users created after a specific timestamp.
     *
     * @param timestamp epoch time in milliseconds
     * @return list of users
     */
    @Query("{ 'createdAt': { $gt: ?0 } }")
    List<User> findUsersCreatedAfter(Long timestamp);

    /**
     * Custom MongoDB query: Find users by email domain.
     *
     * @param domain email domain (e.g., "gmail.com")
     * @return list of users
     */
    @Query("{ 'email': { $regex: ?0, $options: 'i' } }")
    List<User> findByEmailDomain(String domain);
}
