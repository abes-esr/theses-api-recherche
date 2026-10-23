package fr.abes.thesesapirecherche.theses.converters;

import fr.abes.thesesapirecherche.theses.dto.ThesePersonneResponseDto;
import fr.abes.thesesapirecherche.theses.model.PersonneThese;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component 
public class ThesePersonneMapper {
    public ThesePersonneResponseDto personneToDto(PersonneThese these) {
        return ThesePersonneResponseDto.builder()
                .ppn(these.getPpn())
                .prenom(these.getPrenom())
                .nom(these.getNom())
                .build();
    }

    public List<ThesePersonneResponseDto> personnesToDto(List<PersonneThese> theses) {
        List<ThesePersonneResponseDto> results = new ArrayList<>();
        if (theses != null) {
            for (PersonneThese item : theses) {
                results.add(personneToDto(item));
            }
        }
        return results;
    }
}