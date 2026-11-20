package fr.abes.thesesapirecherche.theses.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SujetDto {
    String keyword;
    Type type;
    String query;
    public enum Type {
        sujet,
        sujetsRameau
    }
}
