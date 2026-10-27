package fr.abes.thesesapirecherche.theses.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SujetsToMap {
    String keyword;
    Type type;
    String query;
    public enum Type {
        sujet,
        sujetsRameau
    }
}
