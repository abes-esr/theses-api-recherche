package fr.abes.thesesapirecherche.converters;

import co.elastic.clients.elasticsearch.core.search.Hit;
import fr.abes.thesesapirecherche.dto.PersonnesResponseDto;
import fr.abes.thesesapirecherche.model.Personne;

import java.util.ArrayList;
import java.util.List;

/**
 * Convertisseur de format pour les objets Personne
 */
public class PersonneMapper {

    PersonneTheseMapper theseMapper = new PersonneTheseMapper();

    public PersonnesResponseDto personneToDto(Hit<Personne> personne) {
        return PersonnesResponseDto.builder()
                .id(personne.id())
                .nom(personne.source().getNom())
                .prenom(personne.source().getPrenom())
                .hasIdref(personne.source().getHasIdref())
                .theses(theseMapper.personnesToDto(personne.source().getTheses()))
                .build();
    }

    public List<PersonnesResponseDto> personnesToDto(List<Hit<Personne>> personnes) {
        List<PersonnesResponseDto> results = new ArrayList<>();
        for (Hit<Personne> item : personnes) {
            results.add(personneToDto(item));
        }
        return results;
    }

}
