package fr.abes.thesesapirecherche.personnes.converters;

import co.elastic.clients.elasticsearch.core.search.CompletionSuggestOption;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.Suggestion;
import fr.abes.thesesapirecherche.personnes.dto.PersonneComputedFields;
import fr.abes.thesesapirecherche.personnes.dto.PersonneLiteResponseDto;
import fr.abes.thesesapirecherche.personnes.dto.PersonneResponseDto;
import fr.abes.thesesapirecherche.personnes.dto.SuggestionPersonneResponseDto;
import fr.abes.thesesapirecherche.personnes.model.Personne;
import fr.abes.thesesapirecherche.personnes.model.RecherchePersonne;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Convertisseur de format pour les objets Personne
 */
public class PersonneMapper {

    TheseMapper theseMapper = new TheseMapper();

    /**
     * Conversion d'une personne du format ES au format DTO pour une liste de personnes.
     * Les champs d'une personne sont minimisés afin de renvoyer uniquement les champs importants
     * lors d'une recherche contenant un ensemble de personnes.
     *
     * @param personne Hit<Personne>
     * @return
     */
    public PersonneLiteResponseDto personnesToDto(Hit<RecherchePersonne> personne) {
        PersonneLiteResponseDto item = PersonneLiteResponseDto.builder()
                .id(personne.id())
                .nom(personne.source().getNom())
                .prenom(personne.source().getPrenom())
                .hasIdref(personne.source().getHasIdref())
                .these(personne.source().getTheses_id().get(0))
                .theses(personne.source().getTheses_id())
                .build();

        // On remplit les champs calculés
        item.setRoles(PersonneComputedFields.calculerStatistiquesRoles(personne.source().getRoles()));
        item.setDisciplines(PersonneComputedFields.calculerDisciplines(personne.source().getDisciplines()));
        item.setEtablissements(PersonneComputedFields.calculerEtablissements(personne.source().getEtablissements()));

        return item;
    }

    /**
     * Conversion d'une liste de personne du format ES au format DTO.
     *
     * @param personnes
     * @return
     */
    public List<PersonneLiteResponseDto> personnesListToDto(List<Hit<RecherchePersonne>> personnes) {
        List<PersonneLiteResponseDto> results = new ArrayList<>();
        for (Hit<RecherchePersonne> item : personnes) {
            results.add(personnesToDto(item));
        }
        return results;
    }

    /**
     * Conversion d'une personne du format ES au format DTO.
     * Les champs d'une personne sont maximisé afin de renvoyer tous les champs.
     * @param personne Hit<Personne>
     * @return
     */
    public PersonneResponseDto personneToDto(Hit<Personne> personne) {
        PersonneResponseDto item = PersonneResponseDto.builder()
                .id(personne.id())
                .nom(personne.source().getNom())
                .prenom(personne.source().getPrenom())
                .hasIdref(personne.source().getHasIdref())
                .theses(theseMapper.thesesToDto(personne.source().getTheses()))
                .build();

        // On remplit les champs calculés
        item.setRoles(PersonneComputedFields.calculerStatistiquesRoles(personne.source().getRoles()));
        item.setMotsCles(PersonneComputedFields.calculerMotsCles(personne.source().getTheses()));

        return item;
    }

    /**
     * Conversion d'une proposition de personne du format ES au format DTO.
     * @param suggestion CompletionSuggestOption<Void>
     * @return Une suggestion de personnes au format DTO
     */
    public SuggestionPersonneResponseDto suggestionPersonneToDto(
            CompletionSuggestOption<Void> suggestion) {
        return SuggestionPersonneResponseDto.builder().text(suggestion.text()).id(suggestion.id())
                .build();
    }

    /**
     * Conversion d'une liste de proposition de personnes du format ES au format DTO.
     * @param personnes Map<String,List<Suggestion<Void>>>
     * @return Une liste de suggestion de personnes au format DTO
     */
    public List<SuggestionPersonneResponseDto> suggestionListPersonneToDto(
            Map<String,List<Suggestion<Void>>> personnes) {
        List<SuggestionPersonneResponseDto> results = new ArrayList<>();
        personnes.entrySet().forEach(a->a.getValue().forEach(s->s.completion().options().forEach(b->results.add(suggestionPersonneToDto(b)))));
        return results;
    }

}
