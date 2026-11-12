package fr.abes.thesesapirecherche.theses.service;

import fr.abes.thesesapirecherche.theses.dto.OrganismeRefDto;
import fr.abes.thesesapirecherche.theses.dto.PersonneRefDto;
import fr.abes.thesesapirecherche.theses.dto.client.ClientTheseResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;


class CsvWriterTest {

    private static Stream<Arguments> provideArguments() {
        return Stream.of(
                Arguments.of(null, null, null, "\"\""),
                Arguments.of("nono", "toto", "azerty", "\"nono, toto (azerty)\""),
                Arguments.of("nono", null, "azerty", "\"nono (azerty)\""),
                Arguments.of(null, "toto", "azerty", "\"toto (azerty)\""),
                Arguments.of("nono", "toto", null, "\"nono, toto\""),
                Arguments.of("nono", null, null, "\"nono\""),
                Arguments.of(null, "toto", null, "\"toto\"")
        );
    }

    @ParameterizedTest(name = "When nom is {0} and prenom is {1} and ppn is {2} then name field in csv should be {3}")
    @MethodSource("provideArguments")
    void writeToCsvShouldWriteNamesProperly(String nom, String prenom, String ppn, String expected) {
        ArrayList<PersonneRefDto> personneList = new ArrayList<>();
        personneList.add(PersonneRefDto.builder().prenom(prenom).nom(nom).ppn(ppn).build());
        List<ClientTheseResponseDto> clientTheseResponseDtoList = new ArrayList<>();
        clientTheseResponseDtoList.add(ClientTheseResponseDto.builder()
                .auteurs(personneList)
                .titres(new HashMap<>())
                .directeurs(new ArrayList<>())
                .etabSoutenance(OrganismeRefDto.builder().build())
                .etabCotutelle(new ArrayList<>())
                .ecolesDoctorales(new ArrayList<>())
                .partenairesRecherche(new ArrayList<>())
                .presidentJury(PersonneRefDto.builder().build())
                .rapporteurs(new ArrayList<>())
                .membresJury(new ArrayList<>())
                .mapSujets(new HashMap<>())
                .langues(new ArrayList<>())
                .oaiSetNames(new ArrayList<>())
                .source("")
                .cas("")
                .accessible("")
                .build()
        );

        String resultCSV = CsvWriter.ClientTheseResponseDtoCsvWriter.writeToCsvFrom(clientTheseResponseDtoList);
        String expectedCSV = """
                "NNT";"DOI";"NumSujet";"NumSujetSansS";"Auteurs";"Titre FR";"Titre EN";"Directeurs";"Discipline";"Date de soutenance";"Date de première inscription";"Date d'archivage CINES";"Etablissement de soutenance";"Code etablissement";"Etablissement de cotutelle";"Ecoles doctorales";"Partenaires de recherche";"Président du jury";"Rapporteurs";"Examinateurs";"Mots clés FR";"Mots clés EN";"Langue";"Noms des sets OAI";"Source";"Statut";"Cas";"Accessible"
                "";"";"";"";""" + expected +
                """
                ;"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"en cours";"";"";
                """;
        Assertions.assertThat(resultCSV).isEqualTo(expectedCSV);
    }

}