package fr.abes.thesesapirecherche.theses.converters;

import fr.abes.thesesapirecherche.theses.dto.PersonneRefDto;
import fr.abes.thesesapirecherche.theses.model.PersonneRef;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component 
public class PersonneRefMapper {
    public PersonneRefDto personneToDto(PersonneRef these) {
        return PersonneRefDto.builder()
                .ppn(these.getPpn())
                .prenom(these.getPrenom())
                .nom(these.getNom())
                .build();
    }

    public List<PersonneRefDto> personnesToDto(List<PersonneRef> theses) {
        List<PersonneRefDto> results = new ArrayList<>();
        if (theses != null) {
            for (PersonneRef item : theses) {
                results.add(personneToDto(item));
            }
        }
        return results;
    }
}