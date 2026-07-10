package fr.abes.thesesapirecherche.theses.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;

@Getter
@Setter
@JsonView({JsonViews.Lite.class, JsonViews.Full.class})
public class ResponseTheseEnhancedDto {

    long totalHits;
    long took;
    List<TheseEnhancedResponseDto> theses = new ArrayList<>();
}
