package fr.abes.thesesapirecherche.personnes.converters;

import fr.abes.thesesapirecherche.personnes.dto.SujetRameauResponseDto;
import fr.abes.thesesapirecherche.personnes.model.SujetsRameau;

import java.util.ArrayList;
import java.util.List;

/**
 * Convertisseur de format pour les objets Sujet Rameau
 */
public class SujetRameauMapper {

    /**
     * Transforme un sujet rameau en web dto
     *
     * @param item Sujet Rameau à convertir
     * @return
     */
    public SujetRameauResponseDto sujetRameauToDto(SujetsRameau item) {

        if (item == null) {
            return null;
        } else {
            return SujetRameauResponseDto.builder()
                    .ppn(item.getPpn())
                    .libelle(item.getLibelle())
                    .build();
        }
    }

    /**
     * Transforme une liste de sujet rameau en web dto
     *
     * @param items Liste de sujet rameau à convertir
     * @return
     */
    public List<SujetRameauResponseDto> sujetsRameauToDto(List<SujetsRameau> items) {
        List<SujetRameauResponseDto> results = new ArrayList<>();
        if (items != null) {
            for (SujetsRameau item : items) {
                results.add(sujetRameauToDto(item));
            }
        }
        return results;
    }
}
