package fr.abes.thesesapirecherche.theses.converters;

import fr.abes.thesesapirecherche.theses.dto.OrganismeRefDto;
import fr.abes.thesesapirecherche.theses.model.Organisme;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

@Component 
public class OrganismeMapper {
    public OrganismeRefDto organismeToDto(Organisme org) {
        return OrganismeRefDto.builder()
                .ppn(org.getPpn())
                .nom(org.getNom())
                .type(org.getType())
                .build();
    }

    public List<OrganismeRefDto> organismesToDto(List<Organisme> orgs) {
        List<OrganismeRefDto> results = new ArrayList<>();
        if (orgs != null) {
            for (Organisme item : orgs) {
                results.add(organismeToDto(item));
            }
        }
        return results;
    }
}