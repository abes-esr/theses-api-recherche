package fr.abes.thesesapirecherche.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * DTO web pour une exception retournée par l'API
 */
@AllArgsConstructor
public class ApiExceptionDto {

    @Getter
    private HttpStatus status;

    @Getter
    private String message;
}
