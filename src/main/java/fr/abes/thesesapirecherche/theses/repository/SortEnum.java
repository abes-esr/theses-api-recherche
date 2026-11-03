package fr.abes.thesesapirecherche.theses.repository;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

public enum SortEnum {
    PERTINENCE("pertinence", List.of()),
    DATE_ASC("dateAsc", List.of(byField("dateFiltre", SortOrder.Asc))),
    DATE_DESC("dateDesc", List.of(byField("dateFiltre", SortOrder.Desc))),
    AUTEUR_ASC("auteursAsc", List.of(byField("auteursNP.exact", SortOrder.Asc))),
    AUTEUR_DESC("auteursDesc", List.of(byField("auteursNP.exact", SortOrder.Desc))),
    DISCIPLINE_ASC("disciplineAsc", List.of(byField("discipline.exact", SortOrder.Asc))),
    DISCIPLINE_DESC("disciplineDesc", List.of(byField("discipline.exact", SortOrder.Desc)));

    private static final Map<String, SortEnum> BY_CODE =
            Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(s -> s.code, s -> s));

    private final String code;
    private final List<SortOptions> sortOption;

    SortEnum(String code, List<SortOptions> sortOption) {
        this.code = code;
        this.sortOption = sortOption;
    }

    private static SortEnum fromCode(String code) {
        SortEnum result = code == null ? null : BY_CODE.get(code.trim());
        if (result == null) {
            throw new IllegalArgumentException("Unknown sort: \"" + code + "\"");
        }
        return result;
    }

    /** "dateDesc,auteursAsc" -> [SortEnum.DATE_DESC, SortEnum.AUTEUR_ASC] */
    private static List<SortEnum> fromCodes(String codes) {
        if (codes == null || codes.isBlank()) {
            return List.of(PERTINENCE);
        }
        List<SortEnum> sorts = Arrays.stream(codes.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(SortEnum::fromCode)
                .distinct()
                .toList();

        // Reject contradictory sorts on the same field (e.g. dateAsc,dateDesc)
        Set<String> seenFields = new HashSet<>();
        for (SortEnum s : sorts) {
            for (SortOptions o : s.sortOption) {
                if (!seenFields.add(o.field().field())) {
                    throw new IllegalArgumentException("Sort on the same field twice: " + s.code);
                }
            }
        }
        return sorts;
    }

    /** Flattened options to pass to the Elasticsearch query. */
    public static List<SortOptions> toSortOptions(String codes) {
        return fromCodes(codes).stream()
                .flatMap(s -> s.sortOption.stream())
                .toList();
    }

    private static SortOptions byField(String field, SortOrder order) {
        return SortOptions.of(s -> s.field(f -> f.field(field).order(order)));
    }
}