package fr.abes.thesesapirecherche.exception;

import fr.abes.thesesapirecherche.dto.ApiExceptionDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * Gestionnaire d'exception pour Spring Boot MVC
 */
@Slf4j
@ControllerAdvice
public class ExceptionControllerHandler {

    private ResponseEntity<Object> buildResponseEntity(ApiExceptionDto apiReturnError) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(apiReturnError, headers, apiReturnError.getStatus());
    }

    /**
     * Retourne l'erreur au format de l'API lorsqu'une route n'existe pas
     *
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, WebRequest request) {
        return buildResponseEntity(new ApiExceptionDto(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    /**
     * Retourne l'erreur au format de l'API lorsqu'une méthode n'est pas supporté
     *
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, WebRequest request) {
        return buildResponseEntity(new ApiExceptionDto(HttpStatus.METHOD_NOT_ALLOWED, ex.getMessage()));
    }

    /**
     * Retourne l'erreur au format de l'API lorsqu'il la méthode n'existe pas
     *
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        return buildResponseEntity(new ApiExceptionDto(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    /**
     * Retourne l'erreur au format de l'API lorsqu'il manque une paramètre dans l'URL
     *
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(MissingPathVariableException.class)
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, WebRequest request) {
        return buildResponseEntity(new ApiExceptionDto(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler(RecaptchaInvalidException.class)
    protected ResponseEntity<Object> handleCaptchaException(RecaptchaInvalidException ex) {
        return buildResponseEntity(new ApiExceptionDto(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    /**
     * Retourne l'erreur au format de l'API lorsqu'il y a une erreur interne
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleMappingException(Exception ex) {
        log.error(ex.toString());
        ex.printStackTrace();
        return buildResponseEntity(new ApiExceptionDto(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }
}

