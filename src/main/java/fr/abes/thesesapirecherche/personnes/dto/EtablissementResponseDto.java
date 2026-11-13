package fr.abes.thesesapirecherche.personnes.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO web retournée par l'API pour un établissement lié à une thèse
 */
@Builder
@Getter
@Setter
public class EtablissementResponseDto {
    String ppn;
    String nom;
    String type;

}
