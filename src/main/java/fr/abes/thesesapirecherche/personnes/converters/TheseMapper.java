package fr.abes.thesesapirecherche.personnes.converters;

import fr.abes.thesesapirecherche.personnes.dto.TheseLiteResponseDto;
import fr.abes.thesesapirecherche.personnes.dto.TheseResponseDto;
import fr.abes.thesesapirecherche.personnes.model.ThesePersonne;

import java.util.ArrayList;
import java.util.List;

/**
 * Convertisseur de format pour les objets Thèses
 */
public class TheseMapper {

    EtablissementMapper etablissementMapper = new EtablissementMapper();
    ThesePersonneLiteMapper personneMapper = new ThesePersonneLiteMapper();

    /**
     * Transforme une thèse en web dto
     * @param these
     * @return
     */
    public TheseResponseDto theseToDto(ThesePersonne these) {
        return TheseResponseDto.builder()
                .nnt(these.getNnt())
                .titre(these.getTitre())
                .role(these.getRole())
                .discipline(these.getDiscipline())
                .status(these.getStatus())
                .etablissement_soutenance(etablissementMapper.etablissementToDto(these.getEtablissement_soutenance()))
                .date_soutenance(these.getDate_soutenance())
                .auteurs(personneMapper.personnesLiteToDto(these.getAuteurs()))
                .directeurs(personneMapper.personnesLiteToDto(these.getDirecteurs()))
                .build();
    }

    /**
     * Transforme une liste de thèse en web dto
     * @param theses
     * @return
     */
    public List<TheseResponseDto> thesesToDto(List<ThesePersonne> theses) {
        List<TheseResponseDto> results = new ArrayList<>();
        if (theses != null) {
            for (ThesePersonne item : theses) {
                results.add(theseToDto(item));
            }
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
                .nnt(these.getNnt())
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
