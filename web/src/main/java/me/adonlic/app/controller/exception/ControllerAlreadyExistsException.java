package me.adonlic.app.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class ControllerAlreadyExistsException extends RuntimeException {
    public ControllerAlreadyExistsException(String controllerSN) {
        super("Controller " + controllerSN + " already exists");
    }
}
