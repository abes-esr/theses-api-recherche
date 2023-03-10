package fr.abes.thesesapirecherche.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Facet {
    String name;
    boolean searchBar;
    List<Checkbox> checkboxes;
}

