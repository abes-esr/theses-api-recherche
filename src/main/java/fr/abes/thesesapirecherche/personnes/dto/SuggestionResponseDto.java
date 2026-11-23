package fr.abes.thesesapirecherche.personnes.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * DTO web retournée par l'API pour une suggestion du module personne
 */
@Builder
@Getter
@Setter
public class SuggestionResponseDto {
    List<SuggestionPersonneResponseDto> personnes;
    List<SuggestionPersonneResponseDto> thematiques;


}
