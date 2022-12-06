package fr.abes.thesesapirecherche.theses.dto;

import fr.abes.thesesapirecherche.theses.model.PersonneThese;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Builder
@Getter
@Setter
public class TheseLiteResponseDto {

    Map<String, String> titres;
    String dateSoutenance;
    List<PersonneThese> auteurs;
    List<PersonneThese> directeurs;
    String nnt;
    String codeEtab;
    String discipline;
}
