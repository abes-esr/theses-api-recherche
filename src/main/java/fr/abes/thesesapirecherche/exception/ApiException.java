package fr.abes.thesesapirecherche.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Exception générique de l'API
 */
@AllArgsConstructor
public class ApiException extends Exception {

    @Getter
    private String message;
}
