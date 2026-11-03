package fr.abes.thesesapirecherche.theses.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class QueryStringNormalizerTest {
    @CsvFileSource(resources = "/normalize-cases.csv", numLinesToSkip = 1, quoteCharacter = '"')
    @ParameterizedTest(name = "[{index}] {0}")
    void normalize(String input, String expected) {
        assertEquals(expected, QueryStringNormalizer.normalize(input));
    }

    @Test
    void cleanDoiNull() {
        assertNull(QueryStringNormalizer.cleanDoi(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"doi.org/10.00001/azerty", "http://doi.org/10.00001/azerty", "https://doi.org/10.00001/azerty",})
    void cleanDoiAllPrefixes(String input) {
        assertEquals("10.00001/azerty", QueryStringNormalizer.cleanDoi(input));
    }
}