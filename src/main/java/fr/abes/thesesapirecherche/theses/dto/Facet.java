package fr.abes.thesesapirecherche.theses.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Facet {
    String name;
    boolean searchBar;
    List<Checkbox> checkboxes;
}

