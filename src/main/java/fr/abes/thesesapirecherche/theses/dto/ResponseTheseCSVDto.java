package fr.abes.thesesapirecherche.theses.dto;

import fr.abes.thesesapirecherche.theses.dto.client.ClientTheseResponseDto;
import fr.abes.thesesapirecherche.theses.model.SujetsToMap;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ResponseTheseCSVDto {
    List<ClientTheseResponseDto> theses = new ArrayList<>();
}
