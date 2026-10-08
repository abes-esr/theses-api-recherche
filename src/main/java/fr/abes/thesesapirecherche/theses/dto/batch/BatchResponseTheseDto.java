package fr.abes.thesesapirecherche.theses.dto.batch;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BatchResponseTheseDto {

    long totalHits;

    long took;
    List<BatchTheseResponseDto> theses = new ArrayList<>();
}
