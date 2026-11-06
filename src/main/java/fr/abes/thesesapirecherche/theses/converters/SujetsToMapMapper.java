package fr.abes.thesesapirecherche.theses.converters;

import fr.abes.thesesapirecherche.theses.dto.SujetsToMapDto;
import fr.abes.thesesapirecherche.theses.dto.TheseResponseDto;
import fr.abes.thesesapirecherche.theses.model.SujetsToMap;
import fr.abes.thesesapirecherche.theses.model.These;

public class SujetsToMapMapper {

    public SujetsToMapDto sujetsToMapToDto(SujetsToMap sujetsToMap) {
        return SujetsToMapDto.builder()
                .keyword(sujetsToMap.getKeyword())
                .type(sujetsToMap.getType())
                .query(sujetsToMap.getQuery())
                .build();
    }
}
