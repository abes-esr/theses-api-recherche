package fr.abes.thesesapirecherche.personnes.converters;

import fr.abes.thesesapirecherche.personnes.dto.TheseLiteResponseDto;
import fr.abes.thesesapirecherche.personnes.dto.TheseResponseDto;
import fr.abes.thesesapirecherche.personnes.model.ThesePersonne;

import java.util.*;

/**
 * Convertisseur de format pour les objets Thèses
 */
public class TheseMapper {

    EtablissementMapper etablissementMapper = new EtablissementMapper();
    ThesePersonneLiteMapper personneMapper = new ThesePersonneLiteMapper();

    SujetRameauMapper sujetRameauMapper = new SujetRameauMapper();

    /**
     * Transforme une thèse en web dto
     * @param these
     * @return
     */
    public TheseResponseDto theseToDto(ThesePersonne these) {
        return TheseResponseDto.builder()
                .id(these.getId())
                .titre(these.getTitre())
                .role(these.getRole())
                .discipline(these.getDiscipline())
                .status(these.getStatus())
                .etablissement_soutenance(etablissementMapper.etablissementToDto(these.getEtablissement_soutenance()))
                .date_soutenance(these.getDate_soutenance())
                .date_inscription(these.getDate_inscription())
                .auteurs(personneMapper.personnesLiteToDto(these.getAuteurs()))
                .directeurs(personneMapper.personnesLiteToDto(these.getDirecteurs()))
                .sujets_rameau(sujetRameauMapper.sujetsRameauToDto(these.getSujets_rameau()))
                .sujets(these.getSujets())
                .build();
    }

    /**
     * Transforme une liste de thèse en web dto
     * @param theses
     * @return
     */
    public Map<String,List<TheseResponseDto>> thesesToDto(List<ThesePersonne> theses) {
        Map<String,List<TheseResponseDto>> results = new HashMap<>();
        if (theses != null) {
            for (ThesePersonne item : theses) {
                if (results.containsKey(item.getRole())) {
                    results.get(item.getRole()).add(theseToDto(item));
                } else {
                    List<TheseResponseDto> list = new ArrayList<>();
                    list.add(theseToDto(item));
                    results.put(item.getRole(),list);
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
