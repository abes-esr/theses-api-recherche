package fr.abes.thesesapirecherche.theses.service;

import fr.abes.thesesapirecherche.theses.dto.OrganismeRefDto;
import fr.abes.thesesapirecherche.theses.dto.PersonneRefDto;
import fr.abes.thesesapirecherche.theses.dto.client.ClientTheseResponseDto;
import fr.abes.thesesapirecherche.theses.model.PersonneRef;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CsvWriterTest {

    private static Stream<Arguments> providePeopleNameVariation() {
        return Stream.of(
                Arguments.of(null, null, null, "\"\""),
                Arguments.of("nono", "toto", "azerty", "\"nono, toto (azerty)\""),
                Arguments.of("nono", null, "azerty", "\"nono (azerty)\""),
                Arguments.of(null, "toto", "azerty", "\"toto (azerty)\""),
                Arguments.of("nono", "toto", null, "\"nono, toto\""),
                Arguments.of("nono", null, null, "\"nono\""),
                Arguments.of(null, "toto", null, "\"toto\""),
                Arguments.of(null, null, "azerty", "\"(azerty)\""),
                Arguments.of("Johansson", "Scarlett \"Jo\"", "azerty", "\"Johansson, Scarlett Jo (azerty)\""),
                Arguments.of("Johansson \"Jo\"", "Scarlett", "azerty", "\"Johansson Jo, Scarlett (azerty)\"")
        );
    }
    @ParameterizedTest(name = "When nom is {0}, prenom is {1} and ppn is {2} then name field in csv should be {3}")
    @MethodSource("providePeopleNameVariation")
    void writeToCsvShouldWriteAuthorNamesProperly(String nom, String prenom, String ppn, String expected) {
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

    private static Stream<Arguments> provideOrgIdentityVariation() {
        return Stream.of(
                Arguments.of(null, null, "\"\""),
                Arguments.of("CNRS", "123456789", "\"CNRS (123456789)\""),
                Arguments.of("CNRS", null, "\"CNRS\""),
                Arguments.of(null, "123456789", "\"(123456789)\"")
        );
    }
    @ParameterizedTest(name = "When research partner is {0} and ppn is {1} then name field in csv should be {2}")
    @MethodSource("provideOrgIdentityVariation")
    void writeToCsvShouldWriteResearchPartnerNameProperly(String orgName, String ppn, String expected) {

        ArrayList<OrganismeRefDto> researchPartnerList = new ArrayList<>();
        researchPartnerList.add(OrganismeRefDto.builder().ppn(ppn).nom(orgName).build());


        List<ClientTheseResponseDto> clientTheseResponseDtoList = new ArrayList<>();
        clientTheseResponseDtoList.add(ClientTheseResponseDto.builder()
                .auteurs(new ArrayList<>())
                .titres(new HashMap<>())
                .directeurs(new ArrayList<>())
                .etabSoutenance(OrganismeRefDto.builder().build())
                .etabCotutelle(new ArrayList<>())
                .ecolesDoctorales(new ArrayList<>())
                .partenairesRecherche(researchPartnerList)
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
                "";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";""" + expected +
                """
                ;"";"";"";"";"";"";"";"";"en cours";"";"";
                """;
        Assertions.assertThat(resultCSV).isEqualTo(expectedCSV);
    }

    private static Stream<Arguments> provideOrgIdentitiesVariation() {
        return Stream.of(
                Arguments.of("LIRMM", "987654321", "CNRS", "123456789", "\"LIRMM (987654321), CNRS (123456789)\""),

                Arguments.of(null, "987654321", "CNRS", "123456789", "\"(987654321), CNRS (123456789)\""),
                Arguments.of("LIRMM", null, "CNRS", "123456789", "\"LIRMM, CNRS (123456789)\""),
                Arguments.of("LIRMM", "987654321", null, "123456789", "\"LIRMM (987654321), (123456789)\""),
                Arguments.of("LIRMM", "987654321", "CNRS", null, "\"LIRMM (987654321), CNRS\""),

                Arguments.of(null, null, "CNRS", "123456789", "\"CNRS (123456789)\""),
                Arguments.of("LIRMM", "987654321", null, null, "\"LIRMM (987654321)\"")
        );
    }
    @ParameterizedTest(name = "When research partners are {0} with ppn {1} and {2} with ppn {3} then written field in csv format should be {4}")
    @MethodSource("provideOrgIdentitiesVariation")
    void writeToCsvShouldWriteResearchPartnersNamesProperly(String orgName1, String ppn1, String orgName2, String ppn2, String expected) {

        ArrayList<OrganismeRefDto> researchPartnerList = new ArrayList<>();
        researchPartnerList.add(OrganismeRefDto.builder().ppn(ppn1).nom(orgName1).build());
        researchPartnerList.add(OrganismeRefDto.builder().ppn(ppn2).nom(orgName2).build());


        List<ClientTheseResponseDto> clientTheseResponseDtoList = new ArrayList<>();
        clientTheseResponseDtoList.add(ClientTheseResponseDto.builder()
                .auteurs(new ArrayList<>())
                .titres(new HashMap<>())
                .directeurs(new ArrayList<>())
                .etabSoutenance(OrganismeRefDto.builder().build())
                .etabCotutelle(new ArrayList<>())
                .ecolesDoctorales(new ArrayList<>())
                .partenairesRecherche(researchPartnerList)
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
                "";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";""" + expected +
                """
                ;"";"";"";"";"";"";"";"";"en cours";"";"";
                """;
        Assertions.assertThat(resultCSV).isEqualTo(expectedCSV);
    }

    private static Stream<Arguments> provideSchoolsVariation() {
        return Stream.of(
            Arguments.of("Université de Montpellier", "414243444", "Université de Toulouse", "414243444", "\"Université de Montpellier (414243444) || Université de Toulouse (414243444)\""),
                Arguments.of("Université de Montpellier || UM3", "414243444", "Université de Toulouse", "414243444", "\"Université de Montpellier   UM3 (414243444) || Université de Toulouse (414243444)\"")
        );
    }
    @ParameterizedTest(name = "When doctorate shcools are {0} with ppn {1} and {2} with ppn {3} then written field in csv format should be {4}")
    @MethodSource("provideSchoolsVariation")
    void writeToCsvShouldWriteSchoolsNamesProperly(String orgName1, String ppn1, String orgName2, String ppn2, String expected) {

        ArrayList<OrganismeRefDto> ecoleList = new ArrayList<>();
        ecoleList.add(OrganismeRefDto.builder().ppn(ppn1).nom(orgName1).build());
        ecoleList.add(OrganismeRefDto.builder().ppn(ppn2).nom(orgName2).build());


        List<ClientTheseResponseDto> clientTheseResponseDtoList = new ArrayList<>();
        clientTheseResponseDtoList.add(ClientTheseResponseDto.builder()
                .auteurs(new ArrayList<>())
                .titres(new HashMap<>())
                .directeurs(new ArrayList<>())
                .etabSoutenance(OrganismeRefDto.builder().build())
                .etabCotutelle(new ArrayList<>())
                .ecolesDoctorales(ecoleList)
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
                "";"";"";"";"";"";"";"";"";"";"";"";"";"";"";""" + expected +
                """
                ;"";"";"";"";"";"";"";"";"";"en cours";"";"";
                """;
        Assertions.assertThat(resultCSV).isEqualTo(expectedCSV);
    }

    private static Stream<Arguments> provideNntVariation() {
        return Stream.of(
                Arguments.of("2026MONT0001", "\"2026MONT0001\""),
                Arguments.of(null, "\"\""),
                Arguments.of("", "\"\"")
        );
    }
    @ParameterizedTest(name = "When a thesis dto has {0} nnt then written field in csv format should be {1}")
    @MethodSource("provideNntVariation")
    void writeToCsvShouldWriteNntProperly(String nnt, String expected) {
        List<ClientTheseResponseDto> clientTheseResponseDtoList = new ArrayList<>();
        clientTheseResponseDtoList.add(ClientTheseResponseDto.builder()
                .nnt(nnt)
                .auteurs(new ArrayList<>())
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
                """ +
                expected + """
                ;"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"en cours";"";"";
                """;
        Assertions.assertThat(resultCSV).isEqualTo(expectedCSV);
    }
}