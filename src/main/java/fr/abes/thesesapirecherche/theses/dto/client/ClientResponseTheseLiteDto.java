package fr.abes.thesesapirecherche.theses.dto.client;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ClientResponseTheseLiteDto {

    long totalHits;

    long took;
    List<ClientTheseLiteResponseDto> theses = new ArrayList<>();
}
