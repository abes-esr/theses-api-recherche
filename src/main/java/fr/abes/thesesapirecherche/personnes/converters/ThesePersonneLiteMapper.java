package fr.abes.thesesapirecherche.personnes.converters;

import fr.abes.thesesapirecherche.personnes.dto.ThesePersonneLiteResponseDto;
import fr.abes.thesesapirecherche.personnes.model.PersonneLite;

import java.util.ArrayList;
import java.util.List;

/**
 * Convertisseur de format pour les objets Personne dans les informations d'une thèse
 */
public class ThesePersonneLiteMapper {

    /**
     * Transforme une personne simplifiée en web dto
     * @param item Personne à convertir
     * @return
     */
    public ThesePersonneLiteResponseDto personneLiteToDto(PersonneLite item) {

        if(item == null) {
            return null;
        } else {
            return ThesePersonneLiteResponseDto.builder()
                    .id(item.getPpn())
                    .nom(item.getNom())
                    .prenom(item.getPrenom())
                    .hasIdref(item.getHasIdref())
                    .build();
        }
    }

    /**
     * Transforme une liste de personne simplifiée en web dto
     * @param items Liste des personnes à convertir
     * @return
     */
    public List<ThesePersonneLiteResponseDto> personnesLiteToDto(List<PersonneLite> items) {
        List<ThesePersonneLiteResponseDto> results = new ArrayList<>();
        if (items != null) {
            for (PersonneLite item : items) {
                results.add(personneLiteToDto(item));
            }
        }
        return results;
    }
}
