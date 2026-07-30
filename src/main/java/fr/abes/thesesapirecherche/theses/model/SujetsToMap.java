package fr.abes.thesesapirecherche.theses.model;

import com.fasterxml.jackson.annotation.JsonView;

import fr.abes.thesesapirecherche.theses.dto.JsonViews;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@JsonView({JsonViews.Lite.class, JsonViews.Full.class})
public class SujetsToMap {
    String keyword;
    Type type;
    String query;
    public enum Type {
        sujet,
        sujetsRameau
    }
}
