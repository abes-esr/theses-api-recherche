package fr.abes.thesesapirecherche.personnes.converters;

import org.springframework.stereotype.Component;

import fr.abes.thesesapirecherche.personnes.dto.TheseEnhancedResponseDto;
import fr.abes.thesesapirecherche.personnes.model.ThesePersonne;


import fr.abes.thesesapirecherche.personnes.dto.TheseLiteResponseDto;
import fr.abes.thesesapirecherche.personnes.dto.TheseResponseDto;

import java.util.*;

@Component

public class ThesePersonneEnhancedMapper extends TheseMapper{
    

    public TheseEnhancedResponseDto theseToEnhancedDto(ThesePersonne these){
        
            TheseEnhancedResponseDto dto = TheseEnhancedResponseDto.builder()
            .numSujetSansS(these.getNumSujetSansS())
            .doi(these.getDoi())
            .nnt(these.getNnt())
            .codeEtab(these.getCodeEtab())
            .dateCines(these.getDateCines())
            .langues(these.getLangues())
            .accessible(these.getAccessible())
            .cas(these.getCas())
            .build();

            fillNormalFields(these, dto);

            return dto;
    }






    public TheseEnhancedResponseDto theseToLitedDto(ThesePersonne these) {
        
        TheseEnhancedResponseDto dto = TheseEnhancedResponseDto.builder().build();
       
        // On remplit seulement les champs qui viennent du Lite
        fillNormalFields(these, dto);

        return dto;
    } 



    /**
     * Transforme une liste de thèse en web dto (version "normal")
     *
     * @param theses
     * @return
     */
    public Map<String, List<TheseEnhancedResponseDto>> thesesToDto(List<ThesePersonne> theses) {
        Map<String, List<TheseEnhancedResponseDto>> results = new HashMap<>();
        if (theses != null) {
            for (ThesePersonne item : theses) {
                if (results.containsKey(item.getRole())) {
                    results.get(item.getRole()).add(theseToLitedDto(item));
                } else {
                    List<TheseEnhancedResponseDto> list = new ArrayList<>();
                    list.add(theseToLitedDto(item));
                    results.put(item.getRole(), list);
                }
            }
        }

        //On tri les thèses par date
        for (String role : results.keySet()) {
            Collections.sort(results.get(role), Comparator.comparing(TheseResponseDto::getDate_soutenanceTri).reversed());
        }
        return results;
    }


    /**
     * Transforme une liste de thèse en web dto (version "augmentée", inclut + de champ)
     *
     * @param theses
     * @return
     */
    public Map<String, List<TheseEnhancedResponseDto>> thesesToEnhancedDto(List<ThesePersonne> theses) {
        Map<String, List<TheseEnhancedResponseDto>> results = new HashMap<>();
        if (theses != null) {
            for (ThesePersonne item : theses) {
                if (results.containsKey(item.getRole())) {
                    results.get(item.getRole()).add(theseToEnhancedDto(item));
                } else {
                    List<TheseEnhancedResponseDto> list = new ArrayList<>();
                    list.add(theseToEnhancedDto(item));
                    results.put(item.getRole(), list);
                }
            }
        }

        //On tri les thèses par date
        for (String role : results.keySet()) {
            Collections.sort(results.get(role), Comparator.comparing(TheseResponseDto::getDate_soutenanceTri).reversed());
        }
        return results;
    }

    /**
     * Transforme une thèse simplifiée en web dto
     *
     * @param these
     * @return
     */
    public TheseLiteResponseDto theseLiteToDto(ThesePersonne these) {
        return TheseLiteResponseDto.builder()
                .id(these.getId())
                .role(these.getRole())
                .discipline(these.getDiscipline())
                .build();
    }

    /**
     * Trasnforme une liste de thèse simplifié en web dto
     *
     * @param theses
     * @return
     */
    public List<TheseLiteResponseDto> thesesLiteToDto(List<ThesePersonne> theses) {
        List<TheseLiteResponseDto> results = new ArrayList<>();
        if (theses != null) {
            for (ThesePersonne item : theses) {
                results.add(theseLiteToDto(item));
            }
        }
        return results;
    }
}
