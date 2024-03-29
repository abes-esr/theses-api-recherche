package fr.abes.thesesapirecherche.theses.converters;

import fr.abes.thesesapirecherche.theses.dto.OrganismeResponseDto;
import fr.abes.thesesapirecherche.theses.model.Organisme;

import java.util.ArrayList;
import java.util.List;

public class OrganismeMapper {
    public OrganismeResponseDto organismeToDto(Organisme these) {
        return OrganismeResponseDto.builder()
                .ppn(these.getPpn())
                .nom(these.getNom())
                .type(these.getType())
                .build();
    }

    public List<OrganismeResponseDto> organismesToDto(List<Organisme> theses) {
        List<OrganismeResponseDto> results = new ArrayList<>();
        if (theses != null) {
            for (Organisme item : theses) {
                results.add(organismeToDto(item));
            }
        }
        return results;
    }
}
