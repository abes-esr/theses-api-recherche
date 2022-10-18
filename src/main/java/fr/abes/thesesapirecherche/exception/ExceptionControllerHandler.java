package fr.abes.thesesapirecherche.exception;

import fr.abes.thesesapirecherche.dto.ApiExceptionDto;
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
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Gestionnaire d'exception pour Spring Boot MVC
 */
@ControllerAdvice
public class ExceptionControllerHandler extends ResponseEntityExceptionHandler {

    private ResponseEntity<Object> buildResponseEntity(ApiExceptionDto apiReturnError) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(apiReturnError, headers, apiReturnError.getStatus());
    }

    /**
     * Retourne l'erreur au format de l'API lorsqu'une route n'existe pas
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return buildResponseEntity(new ApiExceptionDto(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    /**
     * Retourne l'erreur au format de l'API lorsqu'une méthode n'est pas supporté
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return buildResponseEntity(new ApiExceptionDto(HttpStatus.METHOD_NOT_ALLOWED, ex.getMessage()));
    }

    /**
     * Retourne l'erreur au format de l'API lorsqu'il la méthode n'existe pas
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return buildResponseEntity(new ApiExceptionDto(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    /**
     * Retourne l'erreur au format de l'API lorsqu'il manque une paramètre dans l'URL
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return buildResponseEntity(new ApiExceptionDto(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    /**
     * Retourne l'erreur au format de l'API lorsqu'il y a une erreur interne
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return buildResponseEntity(new ApiExceptionDto(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    /**
     * Retourne l'erreur au format de l'API lorsqu'il y a une erreur interne
     * @param ex
     * @return
     */
    @ExceptionHandler(ApiException.class)
    protected ResponseEntity<Object> handleMappingException(ApiException ex) {
        return buildResponseEntity(new ApiExceptionDto(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }
}
