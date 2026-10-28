package fr.abes.thesesapirecherche.theses.dto;

import fr.abes.thesesapirecherche.theses.dto.client.ClientTheseLiteResponseDto;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
public class GenericSearchDto<T> {
    long totalHits;
    long took;
    List<T> contenus  = new ArrayList<>();
}
