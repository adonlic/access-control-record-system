package me.adonlic.app.access_control.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class AccessPermissionNotFoundException extends RuntimeException {
    public AccessPermissionNotFoundException(Long id) {
        super("Could not find controller with id " + id);
    }
}

