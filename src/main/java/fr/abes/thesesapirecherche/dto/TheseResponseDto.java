package fr.abes.thesesapirecherche.dto;

import fr.abes.thesesapirecherche.model.PersonneThese;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class TheseResponseDto {

    String titre;
    String nnt;
    String dateSoutenance;
    String abstractFR;
    String abstractEN;
    List<String> sujetsRameau;
    List<ThesePersoneResponseDto> membresJury;

}
