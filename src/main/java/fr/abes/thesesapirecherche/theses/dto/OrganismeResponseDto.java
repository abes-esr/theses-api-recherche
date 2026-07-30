package fr.abes.thesesapirecherche.theses.dto;

import com.fasterxml.jackson.annotation.JsonView;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@JsonView({JsonViews.Lite.class, JsonViews.Full.class})
public class OrganismeResponseDto {
    //@JsonView({JsonViews.Lite.class, JsonViews.Full.class})
    private String ppn;

    //@JsonView({JsonViews.Lite.class, JsonViews.Full.class})
    private String nom;

    //@JsonView({JsonViews.Lite.class, JsonViews.Full.class})
    private String type;
}
