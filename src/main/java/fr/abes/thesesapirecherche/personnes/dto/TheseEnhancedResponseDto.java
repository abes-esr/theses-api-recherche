package fr.abes.thesesapirecherche.personnes.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@SuperBuilder
@Setter
@Getter
// @JsonView({JsonViews.Normal.class}) hérité
public class TheseEnhancedResponseDto extends TheseResponseDto {

    // *****************
    // champs TheseResponseDto hérités
    // *****************

    @JsonView({JsonViews.Full.class})
    String numSujetSansS;


    @JsonView({JsonViews.Full.class})
    String doi;


    @JsonView({JsonViews.Full.class})
    String nnt;


    @JsonView({JsonViews.Full.class})
    String codeEtab;


    @JsonView({JsonViews.Full.class})
    String dateCines;


    @JsonView({JsonViews.Full.class})
    List<String> langues;


    @JsonView({JsonViews.Full.class})
    String accessible;


    @JsonView({JsonViews.Full.class})
    String cas;


}
