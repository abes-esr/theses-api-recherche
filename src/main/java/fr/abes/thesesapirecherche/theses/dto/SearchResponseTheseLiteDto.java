package fr.abes.thesesapirecherche.theses.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SearchResponseTheseLiteDto {

    long totalHits;

    long took;
    List<TheseLiteDto> theses = new ArrayList<>();
}
