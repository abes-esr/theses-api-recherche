package fr.abes.thesesapirecherche.theses.repository;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class SortEnumTest {

    @ParameterizedTest
    @CsvSource({
            "dateAsc,       dateFiltre,        Asc",
            "dateDesc,      dateFiltre,        Desc",
            "auteursAsc,    auteursNP.exact,   Asc",
            "auteursDesc,   auteursNP.exact,   Desc",
            "disciplineAsc, discipline.exact,  Asc",
            "disciplineDesc,discipline.exact,  Desc"
    })
    void singleCode_mapsToExpectedFieldAndOrder(String code, String field, SortOrder order) {
        List<SortOptions> result = SortEnum.toSortOptions(code);

        assertEquals(1, result.size());
        assertEquals(field, result.get(0).field().field());
        assertEquals(order, result.get(0).field().order());
    }

    @Test
    void multipleCodes_keepsGivenOrder() {
        List<SortOptions> result = SortEnum.toSortOptions("dateDesc, auteursAsc");

        assertEquals(List.of("dateFiltre", "auteursNP.exact"),
                result.stream().map(o -> o.field().field()).toList());
        assertEquals(List.of(SortOrder.Desc.ordinal(), SortOrder.Asc.ordinal()),
                result.stream().map(o -> Objects.requireNonNull(o.field().order()).ordinal()).toList());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "pertinence"})
    void blankOrPertinence_returnsNoSortOptions(String input) {
        assertTrue(SortEnum.toSortOptions(input).isEmpty());
    }

    @Test
    void duplicateCodes_areDeduplicated() {
        assertEquals(1, SortEnum.toSortOptions("dateAsc,dateAsc").size());
    }

    @Test
    void conflictingSortsOnSameField_areRejected() {
        assertThrows(IllegalArgumentException.class, () -> SortEnum.toSortOptions("dateAsc,dateDesc"));
    }

    @Test
    void unknownCode_isRejected() {
        var ex = assertThrows(IllegalArgumentException.class, () -> SortEnum.toSortOptions("dateDesc,foo"));
        assertTrue(ex.getMessage().contains("foo"));
    }
}