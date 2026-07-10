package fr.abes.thesesapirecherche.theses.model;

import com.fasterxml.jackson.annotation.JsonView;

import fr.abes.thesesapirecherche.theses.dto.JsonViews;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonView({JsonViews.Lite.class, JsonViews.Full.class})
public class Sujet {

    private String langue;
    private String libelle;
}
