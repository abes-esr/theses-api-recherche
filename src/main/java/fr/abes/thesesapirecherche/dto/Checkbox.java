package fr.abes.thesesapirecherche.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Checkbox {
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
        this.label = capitalize(name);
    }

    /**
     * Transforme le caractère suivant un caractère qui n'est pas en alphanumérique (alinéa, espace etc) en majuscule
     * Tri sur la valeur de Character.getType() - seuil = 9
     *
     * @param name
     * @return
     */
    private static String capitalize(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }

        StringBuilder converted = new StringBuilder();

        boolean convertNext = true;
        for (char ch : name.toCharArray()) {
            int type = Character.getType(ch);
            if (Character.getType(ch) > 9) {
                convertNext = true;
            } else if (convertNext) {
                ch = Character.toTitleCase(ch);
                convertNext = false;
            } else {
                ch = Character.toLowerCase(ch);
            }
            converted.append(ch);
        }
        return converted.toString();
    }
}
