package fr.abes.thesesapirecherche.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class RecaptchaInvalidException extends Exception {
    private String message;
}
