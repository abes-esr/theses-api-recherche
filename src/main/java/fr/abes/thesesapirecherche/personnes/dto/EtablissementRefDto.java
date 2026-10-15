package fr.abes.thesesapirecherche.personnes.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EtablissementRefDto {
    private String ppn;
    private String nom;
    private String type;
}
