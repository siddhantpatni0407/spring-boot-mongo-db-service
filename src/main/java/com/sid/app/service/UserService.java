package com.sid.app.service;

import com.sid.app.entity.User;
import com.sid.app.repository.UserRepository;
import com.sid.app.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Service layer for managing {@link User} CRUD operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    /**
     * Get all users from database.
     */
    public List<User> findAll() {
        log.info("Retrieving all users from MongoDB");
        return userRepository.findAll();
    }

    /**
     * Find user by ID.
     */
    public User findById(String id) {
        log.info("Searching user by id={}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("User not found with id={}", id);
                    return new ResourceNotFoundException("User not found: " + id);
                });
    }

    /**
     * Create a new user.
     */
    public User create(User u) {
        if (!StringUtils.hasText(u.getId())) {
            u.setId(null); // Let Mongo generate the ID
        }
        log.info("Saving new user with email={} and role={}", u.getEmail(), u.getRole());
        return userRepository.save(u);
    }

    /**
     * Update an existing user.
     */
    public User update(String id, User updated) {
        User existing = findById(id);
        log.info("Updating user id={} with new values", id);

        existing.setName(updated.getName());
        existing.setEmail(updated.getEmail());
        existing.setPhone(updated.getPhone());
        existing.setRole(updated.getRole());
        existing.setStatus(updated.getStatus());
        existing.setAddress(updated.getAddress());

        return userRepository.save(existing);
    }

    /**
     * Delete a user by ID.
     */
    public void delete(String id) {
        User existing = findById(id);
        log.info("Deleting user with id={} and email={}", id, existing.getEmail());
        userRepository.delete(existing);
    }
}
