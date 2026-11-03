package fr.abes.thesesapirecherche.exception;

import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import fr.abes.thesesapirecherche.dto.ApiExceptionDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.*;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.IOException;
import java.net.SocketTimeoutException;

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

    @ExceptionHandler(DocumentNotFoundException.class)
    public ProblemDetail handleNotFound(DocumentNotFoundException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        pd.setTitle("Document not found");
        return pd;
    }

    @ExceptionHandler(DataAccessResourceFailureException.class)
    ProblemDetail handleDataAccess(DataAccessResourceFailureException ex) {
        log.error("Search backend unavailable", ex);   // log once, here
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.SERVICE_UNAVAILABLE, "Search is temporarily unavailable");
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ApiError> handleIo(IOException e) {
        log.error("Elasticsearch unreachable", e);
        HttpStatus status = (e instanceof SocketTimeoutException)
                ? HttpStatus.GATEWAY_TIMEOUT : HttpStatus.SERVICE_UNAVAILABLE;
        return ResponseEntity.status(status)
                .body(new ApiError("Search service temporarily unavailable. Please try again later."));
    }

    @ExceptionHandler(ElasticsearchException.class)
    public ResponseEntity<ApiError> handleEs(ElasticsearchException e) {
        log.error("Elasticsearch error, status={}", e.status(), e);
        return switch (e.status()) {
            case 400 -> ResponseEntity.badRequest().body(new ApiError("Invalid search query."));
            case 429 -> ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .header("Retry-After", "5")
                    .body(new ApiError("Service is busy. Please try again shortly."));
            case 502, 503, 504 -> ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(new ApiError("Search service temporarily unavailable. Please try again later."));
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiError("Internal error."));
        };
    }
    public record ApiError(String message) {}
}

