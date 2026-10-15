package fr.abes.thesesapirecherche.theses.converters;


import fr.abes.thesesapirecherche.theses.dto.SujetRefDto;
import fr.abes.thesesapirecherche.theses.model.Sujet;

import java.util.List;

// TODO refactor, utiliser MapStruct
public class SujetMapper {
    public SujetRefDto sujetToDto(Sujet sujet) {
        return SujetRefDto.builder()
                .langue(sujet.getLangue())
                .libelle(sujet.getLibelle())
                .build();
    }

    public List<SujetRefDto> sujetsToDtoList(List<Sujet> sujets) {
        return sujets.stream()
                .map(this::sujetToDto)
                .toList();
    }
}
