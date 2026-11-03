package com.codingkit.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleMethodArgumentNotValid(MethodArgumentNotValidException ex, Model model) {
        StringBuilder message = new StringBuilder();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            if (!message.isEmpty()) {
                message.append("; ");
            }
            message.append(fieldError.getDefaultMessage());
        }
        model.addAttribute("errorMessage", message.toString());
        return "error";
    }

    @ExceptionHandler({UserAlreadyExistsException.class, InvalidVideoUrlException.class, ResourceNotFoundException.class, UnauthorizedAccessException.class})
    public String handleBusinessExceptions(RuntimeException ex, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        log.error("Handled business exception: {}", ex.getMessage());
        redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/dashboard");
    }

    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception ex, Model model) {
        log.error("Unhandled exception", ex);
        model.addAttribute("errorMessage", "Something went wrong. Please try again later.");
        return "error";
    }
}
