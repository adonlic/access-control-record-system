package me.adonlic.app.access_control.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class CardNotFoundException extends RuntimeException {
    public CardNotFoundException() {
        super("Could not find card");
    }

    public CardNotFoundException(Long id) {
        super("Could not find card with id " + id);
    }

    public CardNotFoundException(String cardNO) {
        super("Could not find card with number " + cardNO);
    }
}

