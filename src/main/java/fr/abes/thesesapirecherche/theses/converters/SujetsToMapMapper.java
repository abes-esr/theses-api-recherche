package fr.abes.thesesapirecherche.theses.converters;

import fr.abes.thesesapirecherche.theses.model.SujetsToMap;
import org.springframework.stereotype.Component;

@Component
public class SujetsToMapMapper {

    public fr.abes.thesesapirecherche.theses.dto.SujetsToMapDto sujetsToMapToDto(SujetsToMap sujetsToMap) {
        return fr.abes.thesesapirecherche.theses.dto.SujetsToMapDto.builder()
                .keyword(sujetsToMap.getKeyword())
                .type(sujetsToMap.getType())
                .query(sujetsToMap.getQuery())
                .build();
    }
}
