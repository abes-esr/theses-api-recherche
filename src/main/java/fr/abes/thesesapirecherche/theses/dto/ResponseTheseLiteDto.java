package fr.abes.thesesapirecherche.theses.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ResponseTheseLiteDto {

    long totalHits;

    long took;
    List<TheseLiteResponseDto> theses = new ArrayList<>();
}
