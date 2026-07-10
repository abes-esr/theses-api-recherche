package fr.abes.thesesapirecherche.theses.dto;

import com.fasterxml.jackson.annotation.JsonView;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@JsonView({JsonViews.Lite.class, JsonViews.Full.class})
public class ThesePersoneResponseDto {
    private String ppn;
    private String nom;
    private String prenom;
}
