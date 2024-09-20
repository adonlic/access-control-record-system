package me.adonlic.app.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ControllerNotFoundException extends RuntimeException {
    public ControllerNotFoundException(Long id) {
        super("Could not find controller with id " + id);
    }
}
