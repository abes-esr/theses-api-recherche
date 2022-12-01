package fr.abes.thesesapirecherche.personnes.converters;

import fr.abes.thesesapirecherche.personnes.dto.PersonnesTheseResponseDto;
import fr.abes.thesesapirecherche.personnes.model.ThesePersonne;

import java.util.ArrayList;
import java.util.List;

/**
 * Convertisseur de format pour les objets Thèses par les personnes
 */
public class PersonneTheseMapper {

    public PersonnesTheseResponseDto personneToDto(ThesePersonne these) {
        return PersonnesTheseResponseDto.builder()
                .nnt(these.getNnt())
                .role(these.getRole())
                .build();
    }

    public List<PersonnesTheseResponseDto> personnesToDto(List<ThesePersonne> theses) {
        List<PersonnesTheseResponseDto> results = new ArrayList<>();
        if (theses != null) {
            for (ThesePersonne item : theses) {
                results.add(personneToDto(item));
            }
        }
        return results;
    }
}
