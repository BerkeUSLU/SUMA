package com.berke.subscriptionmanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.support.HttpRequestHandlerServlet;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomResponseExceptionHandler extends ResponseEntityExceptionHandler {
    private final String BASE_ERR_URL = "error/base-error";

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleServiceProviderNotFoundException(final Throwable throwable, final Model model) {

        String errorMessage = (throwable != null ? throwable.getMessage() : "Unknown error");
        model.addAttribute("errorMessage", errorMessage);

        return BASE_ERR_URL;
    }

    /*@ExceptionHandler(UserException.class)
    public ResponseEntity<?> handleUserNotFound(UserException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotModifiedException.class)
    public ResponseEntity<?> handleUserNotModifed(NotModifiedException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_MODIFIED);
    }

    @ExceptionHandler(ServiceProviderAlreadyExistExp.class)
    public ResponseEntity<?> handleUserNotModifed(ServiceProviderAlreadyExistExp ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_MODIFIED);
    }
    errormessage
     */
}

