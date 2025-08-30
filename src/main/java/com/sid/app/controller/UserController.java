package com.sid.app.controller;

import com.sid.app.constant.AppConstants;
import com.sid.app.model.ApiResponse;
import com.sid.app.entity.User;
import com.sid.app.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * REST Controller for managing {@link User} resources.
 * Provides CRUD operations with MongoDB as the data source.
 */
@RestController
@RequestMapping(AppConstants.USERS_API)
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<List<User>>> all() {
        log.info("Fetching all users");
        List<User> users = userService.findAll();
        return ResponseEntity.ok(
                ApiResponse.<List<User>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .status(AppConstants.STATUS_SUCCESS)
                        .message(AppConstants.MSG_USERS_FETCHED)
                        .data(users)
                        .build()
        );
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<User>> byId(@PathVariable String id) {
        log.info("Fetching user with id={}", id);
        User user = userService.findById(id);
        return ResponseEntity.ok(
                ApiResponse.<User>builder()
                        .statusCode(HttpStatus.OK.value())
                        .status(AppConstants.STATUS_SUCCESS)
                        .message(AppConstants.MSG_USER_FETCHED)
                        .data(user)
                        .build()
        );
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<User>> create(@Valid @RequestBody User user) {
        log.info("Creating new user with email={} and role={}", user.getEmail(), user.getRole());
        User created = userService.create(user);
        return ResponseEntity.created(URI.create(AppConstants.USERS_API + "/" + created.getId()))
                .body(ApiResponse.<User>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .status(AppConstants.STATUS_SUCCESS)
                        .message(AppConstants.MSG_USER_CREATED)
                        .data(created)
                        .build());
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<User>> update(@PathVariable String id, @Valid @RequestBody User user) {
        log.info("Updating user with id={}", id);
        User updated = userService.update(id, user);
        return ResponseEntity.ok(
                ApiResponse.<User>builder()
                        .statusCode(HttpStatus.OK.value())
                        .status(AppConstants.STATUS_SUCCESS)
                        .message(AppConstants.MSG_USER_UPDATED)
                        .data(updated)
                        .build()
        );
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable String id) {
        log.info("Deleting user with id={}", id);
        userService.delete(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .statusCode(HttpStatus.NO_CONTENT.value())
                        .status(AppConstants.STATUS_SUCCESS)
                        .message(AppConstants.MSG_USER_DELETED)
                        .data(null)
                        .build()
        );
    }
}
