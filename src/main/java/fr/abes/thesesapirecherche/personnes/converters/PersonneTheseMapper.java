package fr.abes.thesesapirecherche.personnes.converters;

import fr.abes.thesesapirecherche.personnes.dto.PersonnesTheseLiteResponseDto;
import fr.abes.thesesapirecherche.personnes.dto.PersonnesTheseResponseDto;
import fr.abes.thesesapirecherche.personnes.model.ThesePersonne;

import java.util.ArrayList;
import java.util.List;

/**
 * Convertisseur de format pour les objets Thèses
 */
public class PersonneTheseMapper {

    EtablissementMapper etablissementMapper = new EtablissementMapper();

    /**
     * Transforme une thèse en web dto
     * @param these
     * @return
     */
    public PersonnesTheseResponseDto theseToDto(ThesePersonne these) {
        return PersonnesTheseResponseDto.builder()
                .nnt(these.getNnt())
                .titre(these.getTitre())
                .role(these.getRole())
                .discipline(these.getDiscipline())
                .status(these.getStatus())
                .etablissement_soutenance(etablissementMapper.etablissementToDto(these.getEtablissement_soutenance()))
                .date_soutenance(these.getDate_soutenance())
                .build();
    }

    /**
     * Transforme une liste de thèse en web dto
     * @param theses
     * @return
     */
    public List<PersonnesTheseResponseDto> thesesToDto(List<ThesePersonne> theses) {
        List<PersonnesTheseResponseDto> results = new ArrayList<>();
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
    public PersonnesTheseLiteResponseDto theseLiteToDto(ThesePersonne these) {
        return PersonnesTheseLiteResponseDto.builder()
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
    public List<PersonnesTheseLiteResponseDto> thesesLiteToDto(List<ThesePersonne> theses) {
        List<PersonnesTheseLiteResponseDto> results = new ArrayList<>();
        if (theses != null) {
            for (ThesePersonne item : theses) {
                results.add(theseLiteToDto(item));
            }
        }
        return results;
    }
}
