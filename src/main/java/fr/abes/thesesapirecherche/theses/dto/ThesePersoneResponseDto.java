package fr.abes.thesesapirecherche.theses.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ThesePersoneResponseDto {
    private String ppn;
    private String nom;
    private String prenom;
}
