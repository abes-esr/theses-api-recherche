package fr.abes.thesesapirecherche.theses.repository;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum SortEnum {
    PERTINENCE("pertinence", List.of()),
    DATE_ASC("dateAsc", List.of(byField("dateFiltre", SortOrder.Asc))),
    DATE_DESC("dateDesc", List.of(byField("dateFiltre", SortOrder.Desc))),

    AUTEUR_ASC("auteursAsc", List.of(byField("auteursNP.exact", SortOrder.Asc))),
    AUTEUR_DESC("auteursDesc", List.of(byField("auteursNP.exact", SortOrder.Desc))),

    DISCIPLINE_ASC("disciplineAsc", List.of(byField("discipline.exact", SortOrder.Asc))),
    DISCIPLINE_DESC("disciplineDesc", List.of(byField("discipline.exact", SortOrder.Desc)));

    private final String code;
    private final List<SortOptions> sortOption;

    SortEnum(String code, List<SortOptions> sortOption) {
        this.code = code;
        this.sortOption = sortOption;
    }

    public static SortEnum fromCode(String code) {
        String c = code == null ? "" : code;
        return Arrays.stream(values())
                .filter(s -> s.code.equals(c))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown sort: " + code));
    }

    private static SortOptions byField(String field, SortOrder order) {
        return SortOptions.of(s -> s.field(f -> f.field(field).order(order)));
    }
}