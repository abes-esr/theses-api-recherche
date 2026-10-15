package fr.abes.thesesapirecherche.theses.converters;


import fr.abes.thesesapirecherche.theses.dto.RameauRefDto;
import fr.abes.thesesapirecherche.theses.model.SujetsRameau;

import java.util.List;

// TODO refactor, utiliser MapStruct
public class SujetsRameauMapper {
    public RameauRefDto sujetsRameauToDto(SujetsRameau sujetsRameau) {
        return RameauRefDto.builder()
                .libelle(sujetsRameau.getLibelle())
                .ppn(sujetsRameau.getPpn())
                .build();
    }

    public List<RameauRefDto> sujetsRameauListToDtoList(List<SujetsRameau> sujetsRameauList) {
        return sujetsRameauList.stream()
                .map(this::sujetsRameauToDto)
                .toList();
    }
}
