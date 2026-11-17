package fr.abes.thesesapirecherche.theses.converters;

import fr.abes.thesesapirecherche.theses.dto.PersonneRefDto;
import fr.abes.thesesapirecherche.theses.model.PersonneRef;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component 
public class PersonneRefMapper {
    public PersonneRefDto personneToDto(PersonneRef personneRef) {
        return personneRef != null ? PersonneRefDto.builder()
                .ppn(personneRef.getPpn())
                .prenom(personneRef.getPrenom())
                .nom(personneRef.getNom())
                .build() : null;
    }

    public List<PersonneRefDto> personnesToDto(List<PersonneRef> personneRefList) {
        if (personneRefList == null) return null;

        List<PersonneRefDto> results = new ArrayList<>();
        for (PersonneRef item : personneRefList) {
            results.add(personneToDto(item));
        }
        return results;
    }
}