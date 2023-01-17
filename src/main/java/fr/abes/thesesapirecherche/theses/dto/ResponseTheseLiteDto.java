package fr.abes.thesesapirecherche.theses.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ResponseTheseLiteDto {

    long totalHits;
    List<TheseLiteResponseDto> theses = new ArrayList<>();
}
