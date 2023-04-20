package fr.abes.thesesapirecherche.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.Collator;
import java.util.List;
import java.util.Locale;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Checkbox implements Comparable<Checkbox> {
    String name;
    String label;
    Long value;
    String parentName;
    List<Checkbox> checkboxes;

    public Checkbox(String name, Long value, String parentName, List<Checkbox> checkboxes) {
        this.name = name;
        this.value = value;
        this.parentName = parentName;
        this.checkboxes = checkboxes;
        // @TODO Supprimer le champ label et son utilisation dans le client
        this.label = name;
    }

    @Override
    public int compareTo(Checkbox other) {
        // Utiliser la comparaison basée sur le champ name
        Collator collator = Collator.getInstance(Locale.FRENCH);
        return collator.compare(this.name, other.name);
    }
}
