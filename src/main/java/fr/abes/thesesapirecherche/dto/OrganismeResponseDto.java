package fr.abes.thesesapirecherche.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class OrganismeResponseDto {
    private String ppn;
    private String nom;
}
