package fr.abes.thesesapirecherche.theses.dto;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        "success",
        "score",
        "action",
        "challenge_ts",
        "hostname",
        "error-codes"
})
@Getter
@Setter
public class CaptchaResponseDto {

    @JsonProperty("success")
    private boolean success;

    @JsonProperty("score")
    private float score;

    @JsonProperty("action")
    private String action;

    @JsonProperty("challenge_ts")
    private String challengeTs;

    @JsonProperty("hostname")
    private String hostname;

    @JsonProperty("error-codes")
    private ErrorCode[] errorCodes;

    @JsonIgnore
    public boolean hasClientError() {
        ErrorCode[] errors = getErrorCodes();
        if (errors == null) {
            return false;
        }
        for (ErrorCode error : errors) {
            switch (error) {
                case InvalidResponse:
                case MissingResponse:
                    return true;
            }
        }
        return false;
    }

    @Getter
    enum ErrorCode {
        MissingSecret("missing-input-secret"),
        InvalidSecret("invalid-input-secret"),
        MissingResponse("missing-input-response"),
        InvalidResponse("invalid-input-response");

        private final String valeur;

        ErrorCode(String val) {
            this.valeur = val;
        }

        private static final Map<String, ErrorCode> errorsMap =
                Arrays.stream(values())
                        .collect(Collectors.toUnmodifiableMap(
                                ErrorCode::getValeur, e -> e));

        @JsonCreator
        public static ErrorCode forValue(String value) {
            return value == null ? null : errorsMap.get(value.toLowerCase(Locale.ROOT));
        }
    }
}
