package fr.abes.thesesapirecherche.theses.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class Facet {
    String parentName;
    String name;
    Map<String, Long> data;
}
