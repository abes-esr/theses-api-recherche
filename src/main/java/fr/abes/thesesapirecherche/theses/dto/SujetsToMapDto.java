package fr.abes.thesesapirecherche.theses.dto;

import fr.abes.thesesapirecherche.theses.model.SujetsToMap;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class SujetsToMapDto {
    String keyword;
    SujetsToMap.Type type;
    String query;
}
