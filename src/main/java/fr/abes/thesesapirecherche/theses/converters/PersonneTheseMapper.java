package fr.abes.thesesapirecherche.theses.converters;

import fr.abes.thesesapirecherche.theses.dto.PersonneRefDto;
import fr.abes.thesesapirecherche.theses.model.PersonneThese;

import java.util.ArrayList;
import java.util.List;

// TODO refactor, utiliser MapStruct
public class PersonneTheseMapper {
    public PersonneRefDto personneTheseToDto(PersonneThese model) {
        return PersonneRefDto
                .builder()
                .ppn(model.getPpn())
                .nom(model.getNom())
                .prenom(model.getPrenom())
                .build();
    }

    public List<PersonneRefDto> personnesTheseToDtoList(List<PersonneThese> modelList) {
        List<PersonneRefDto> results = new ArrayList<>();
        if (modelList != null) {
            for (PersonneThese item : modelList) {
                results.add(personneTheseToDto(item));
            }
        }
        return results;
    }
}
