package fr.abes.thesesapirecherche.theses.converters;

import fr.abes.thesesapirecherche.theses.dto.ThesePersoneResponseDto;
import fr.abes.thesesapirecherche.theses.model.PersonneThese;

import java.util.ArrayList;
import java.util.List;

public class ThesePersonneMapper {
    public ThesePersoneResponseDto personneToDto(PersonneThese these) {
        return ThesePersoneResponseDto.builder()
                .ppn(these.getPpn())
                .prenom(these.getPrenom())
                .nom(these.getNom())
                .build();
    }

    public List<ThesePersoneResponseDto> personnesToDto(List<PersonneThese> theses) {
        List<ThesePersoneResponseDto> results = new ArrayList<>();
        if (theses != null) {
            for (PersonneThese item : theses) {
                results.add(personneToDto(item));
            }
        }
        return results;
    }
}
