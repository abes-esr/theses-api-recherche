package fr.abes.thesesapirecherche.theses.converters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import fr.abes.thesesapirecherche.theses.dto.OrganismeRefDto;
import fr.abes.thesesapirecherche.theses.dto.PersonneRefDto;
import fr.abes.thesesapirecherche.theses.model.Organisme;
import fr.abes.thesesapirecherche.theses.model.PersonneRef;
import fr.abes.thesesapirecherche.theses.model.Sujet;
import fr.abes.thesesapirecherche.theses.model.SujetsRameau;
import fr.abes.thesesapirecherche.theses.dto.SujetDto;
import fr.abes.thesesapirecherche.theses.model.These;

@Component
public class TheseMappingHelper {
    private static final SujetMapper sujetMapper = new SujetMapper();

    //mapping mots clés
    public static Map<String, List<SujetDto>> formatKeywords(These these) {
        Map<String, List<SujetDto>> mapSujets = new HashMap<>();
        for (Sujet s : these.getSujets()) {
            mapSujets.computeIfAbsent(s.getLangue(), langue -> new ArrayList<>())
                    .add(sujetMapper.toDto(s));
        }
        for (SujetsRameau s : these.getSujetsRameau()) {
            mapSujets.computeIfAbsent("fr", key -> new ArrayList<>())
                    .add(sujetMapper.toDto(s));
        }

        return mapSujets;
    }

    //mapping organisme
    public static OrganismeRefDto organismeToDto(Organisme organisme) {
        if(organisme == null){
            return null;
        }
        return OrganismeRefDto.builder()
                .ppn(organisme.getPpn())
                .nom(organisme.getNom())
                .type(organisme.getType())
                .build();
    }

    public static List<OrganismeRefDto> organismesToDto(List<Organisme> organismes) {
        List<OrganismeRefDto> results = new ArrayList<>();
        if (organismes != null) {
            for (Organisme item : organismes) {
                results.add(organismeToDto(item));
            }
        }
        return results;
    }





    //mapping personnes
    public static PersonneRefDto personneToDto(PersonneRef personne) {
        if(personne == null){
            return null;
        }

        return PersonneRefDto.builder()
                .ppn(personne.getPpn())
                .prenom(personne.getPrenom())
                .nom(personne.getNom())
                .build();
    }

    public static List<PersonneRefDto> personnesToDto(List<PersonneRef> personnes) {
        List<PersonneRefDto> results = new ArrayList<>();
        if (personnes != null) {
            for (PersonneRef item : personnes) {
                results.add(personneToDto(item));
            }
        }
        return results;
    }
}
