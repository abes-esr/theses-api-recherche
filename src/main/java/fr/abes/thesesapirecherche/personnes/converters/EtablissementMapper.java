package fr.abes.thesesapirecherche.personnes.converters;

import fr.abes.thesesapirecherche.personnes.dto.EtablissementResponseDto;
import fr.abes.thesesapirecherche.personnes.dto.PersonnesTheseLiteResponseDto;
import fr.abes.thesesapirecherche.personnes.dto.PersonnesTheseResponseDto;
import fr.abes.thesesapirecherche.personnes.model.Etablissement;
import fr.abes.thesesapirecherche.personnes.model.ThesePersonne;

import java.util.ArrayList;
import java.util.List;

/**
 * Convertisseur de format pour les objets Etablissement
 */
public class EtablissementMapper {

    /**
     * Transforme un établissement en web dto
     * @param item Etablissement à convertir
     * @return
     */
    public EtablissementResponseDto etablissementToDto(Etablissement item) {

        if(item == null) {
            return null;
        } else {
            return EtablissementResponseDto.builder()
                    .ppn(item.getPpn())
                    .nom(item.getNom())
                    .type(item.getType())
                    .build();
        }
    }

    /**
     * Transforme une liste de établissements en web dto
     * @param items Liste des établissements à convertir
     * @return
     */
    public List<EtablissementResponseDto> etablissementsToDto(List<Etablissement> items) {
        List<EtablissementResponseDto> results = new ArrayList<>();
        if (items != null) {
            for (Etablissement item : items) {
                results.add(etablissementToDto(item));
            }
        }
        return results;
    }
}
