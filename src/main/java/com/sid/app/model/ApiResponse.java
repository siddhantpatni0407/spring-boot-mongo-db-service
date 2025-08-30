package com.sid.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Standard API response wrapper.
 *
 * @param <T> the type of response data
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    /**
     * HTTP status code of the response.
     */
    private int statusCode;

    /**
     * Status message (e.g., SUCCESS, ERROR).
     */
    private String status;

    /**
     * Additional descriptive message.
     */
    private String message;

    /**
     * Response data payload.
     */
    private T data;
}
