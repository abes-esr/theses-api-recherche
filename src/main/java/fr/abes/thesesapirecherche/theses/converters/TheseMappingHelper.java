package fr.abes.thesesapirecherche.theses.converters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.abes.thesesapirecherche.theses.dto.OrganismeResponseDto;
import fr.abes.thesesapirecherche.theses.dto.ThesePersoneResponseDto;
import fr.abes.thesesapirecherche.theses.model.Organisme;
import fr.abes.thesesapirecherche.theses.model.PersonneThese;
import fr.abes.thesesapirecherche.theses.model.Sujet;
import fr.abes.thesesapirecherche.theses.model.SujetsRameau;
import fr.abes.thesesapirecherche.theses.model.SujetsToMap;
import fr.abes.thesesapirecherche.theses.model.These;

public class TheseMappingHelper {

    //mapping mots clés
    public static Map<String, List<SujetsToMap>> formatKeywords(These these) {
        Map<String, List<SujetsToMap>> mapSujets = new HashMap<>();
        for (Sujet s : these.getSujets()) {
            List<SujetsToMap> l = new ArrayList<>();
            if (mapSujets.get(s.getLangue()) != null) l = mapSujets.get(s.getLangue());
            l.add(new SujetsToMap(s.getLibelle(), SujetsToMap.Type.sujet, s.getLibelle()));
            mapSujets.put(s.getLangue(), l);
        }
        for (SujetsRameau s : these.getSujetsRameau()) {
            List<SujetsToMap> l = new ArrayList<>();
            if (mapSujets.get("fr") != null) l = mapSujets.get("fr");
            l.add(0, new SujetsToMap(s.getLibelle(), SujetsToMap.Type.sujetsRameau, "\"" + s.getLibelle() + "\" ET " + s.getPpn()));
            mapSujets.put("fr", l);
        }

        return mapSujets;
    }






    //mapping organisme
    public static OrganismeResponseDto organismeToDto(Organisme organisme) {
        if(organisme == null){
            return null;
        }
        return OrganismeResponseDto.builder()
                .ppn(organisme.getPpn())
                .nom(organisme.getNom())
                .type(organisme.getType())
                .build();
    }

    public static List<OrganismeResponseDto> organismesToDto(List<Organisme> organismes) {
        List<OrganismeResponseDto> results = new ArrayList<>();
        if (organismes != null) {
            for (Organisme item : organismes) {
                results.add(organismeToDto(item));
            }
        }
        return results;
    }





    //mapping personnes
    public static ThesePersoneResponseDto personneToDto(PersonneThese personne) {
        if(personne == null){
            return null;
        }

        return ThesePersoneResponseDto.builder()
                .ppn(personne.getPpn())
                .prenom(personne.getPrenom())
                .nom(personne.getNom())
                .build();
    }

    public static List<ThesePersoneResponseDto> personnesToDto(List<PersonneThese> personnes) {
        List<ThesePersoneResponseDto> results = new ArrayList<>();
        if (personnes != null) {
            for (PersonneThese item : personnes) {
                results.add(personneToDto(item));
            }
        }
        return results;
    }
}
