package fr.abes.thesesapirecherche.theses.service;

import fr.abes.thesesapirecherche.theses.dto.OrganismeRefDto;
import fr.abes.thesesapirecherche.theses.dto.PersonneRefDto;
import fr.abes.thesesapirecherche.theses.dto.client.ClientTheseResponseDto;
import fr.abes.thesesapirecherche.theses.model.PersonneRef;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvWriterTest {
    List<ClientTheseResponseDto> clientTheseResponseDtoList;

    @BeforeEach
    void setUp() {
        clientTheseResponseDtoList = new ArrayList<>();
        clientTheseResponseDtoList.add(ClientTheseResponseDto.builder()
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
                .build());
    }

    @Test
    void writeToCsvFrom() {
        String actual = CsvWriter.ClientTheseResponseDtoCsvWriter.writeToCsvFrom(clientTheseResponseDtoList);
        String expected = """
                "NNT";"DOI";"NumSujet";"NumSujetSansS";"Auteurs";"Titre FR";"Titre EN";"Directeurs";"Discipline";"Date de soutenance";"Date de première inscription";"Date d'archivage CINES";"Etablissement de soutenance";"Code etablissement";"Etablissement de cotutelle";"Ecoles doctorales";"Partenaires de recherche";"Président du jury";"Rapporteurs";"Examinateurs";"Mots clés FR";"Mots clés EN";"Langue";"Noms des sets OAI";"Source";"Statut";"Cas";"Accessible"
                "";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"en cours";"";"";
                """;

        assertEquals(expected, actual);
    }


    @Test
    void writeToCsvFrom_1_auteur_best_case_scenario() {
        ArrayList<PersonneRefDto> personneList = new ArrayList<>();
        personneList.add(PersonneRefDto.builder().ppn("azerty").nom("nono").prenom("toto").build());
        clientTheseResponseDtoList = new ArrayList<>();
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
                .build());

        String actual = CsvWriter.ClientTheseResponseDtoCsvWriter.writeToCsvFrom(clientTheseResponseDtoList);
        String expected = """
                "NNT";"DOI";"NumSujet";"NumSujetSansS";"Auteurs";"Titre FR";"Titre EN";"Directeurs";"Discipline";"Date de soutenance";"Date de première inscription";"Date d'archivage CINES";"Etablissement de soutenance";"Code etablissement";"Etablissement de cotutelle";"Ecoles doctorales";"Partenaires de recherche";"Président du jury";"Rapporteurs";"Examinateurs";"Mots clés FR";"Mots clés EN";"Langue";"Noms des sets OAI";"Source";"Statut";"Cas";"Accessible"
                "";"";"";"";"nono, toto (azerty)";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"en cours";"";"";
                """;

        assertEquals(expected, actual);
    }

    @Test
    void writeToCsvFrom_1_auteur_sans_prenom() {
        ArrayList<PersonneRefDto> personneList = new ArrayList<>();
        personneList.add(PersonneRefDto.builder().ppn("azerty").nom("nono").build());
        clientTheseResponseDtoList = new ArrayList<>();
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
                .build());

        String actual = CsvWriter.ClientTheseResponseDtoCsvWriter.writeToCsvFrom(clientTheseResponseDtoList);
        String expected = """
                "NNT";"DOI";"NumSujet";"NumSujetSansS";"Auteurs";"Titre FR";"Titre EN";"Directeurs";"Discipline";"Date de soutenance";"Date de première inscription";"Date d'archivage CINES";"Etablissement de soutenance";"Code etablissement";"Etablissement de cotutelle";"Ecoles doctorales";"Partenaires de recherche";"Président du jury";"Rapporteurs";"Examinateurs";"Mots clés FR";"Mots clés EN";"Langue";"Noms des sets OAI";"Source";"Statut";"Cas";"Accessible"
                "";"";"";"";"nono (azerty)";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"en cours";"";"";
                """;

        assertEquals(expected, actual);
    }

    @Test
    void writeToCsvFrom_1_auteur_sans_nom() {
        ArrayList<PersonneRefDto> personneList = new ArrayList<>();
        personneList.add(PersonneRefDto.builder().ppn("azerty").prenom("toto").build());
        clientTheseResponseDtoList = new ArrayList<>();
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
                .build());

        String actual = CsvWriter.ClientTheseResponseDtoCsvWriter.writeToCsvFrom(clientTheseResponseDtoList);
        String expected = """
                "NNT";"DOI";"NumSujet";"NumSujetSansS";"Auteurs";"Titre FR";"Titre EN";"Directeurs";"Discipline";"Date de soutenance";"Date de première inscription";"Date d'archivage CINES";"Etablissement de soutenance";"Code etablissement";"Etablissement de cotutelle";"Ecoles doctorales";"Partenaires de recherche";"Président du jury";"Rapporteurs";"Examinateurs";"Mots clés FR";"Mots clés EN";"Langue";"Noms des sets OAI";"Source";"Statut";"Cas";"Accessible"
                "";"";"";"";"toto (azerty)";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"en cours";"";"";
                """;

        assertEquals(expected, actual);
    }

    @Test
    void writeToCsvFrom_1_auteur_sans_ppn() {
        ArrayList<PersonneRefDto> personneList = new ArrayList<>();
        personneList.add(PersonneRefDto.builder().nom("nono").prenom("toto").build());
        clientTheseResponseDtoList = new ArrayList<>();
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
                .build());

        String actual = CsvWriter.ClientTheseResponseDtoCsvWriter.writeToCsvFrom(clientTheseResponseDtoList);
        String expected = """
                "NNT";"DOI";"NumSujet";"NumSujetSansS";"Auteurs";"Titre FR";"Titre EN";"Directeurs";"Discipline";"Date de soutenance";"Date de première inscription";"Date d'archivage CINES";"Etablissement de soutenance";"Code etablissement";"Etablissement de cotutelle";"Ecoles doctorales";"Partenaires de recherche";"Président du jury";"Rapporteurs";"Examinateurs";"Mots clés FR";"Mots clés EN";"Langue";"Noms des sets OAI";"Source";"Statut";"Cas";"Accessible"
                "";"";"";"";"nono, toto";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"en cours";"";"";
                """;

        assertEquals(expected, actual);
    }

    @Test
    void writeToCsvFrom_1_auteur_sans_ppn_sans_prenom() {
        ArrayList<PersonneRefDto> personneList = new ArrayList<>();
        personneList.add(PersonneRefDto.builder().nom("nono").build());
        clientTheseResponseDtoList = new ArrayList<>();
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
                .build());

        String actual = CsvWriter.ClientTheseResponseDtoCsvWriter.writeToCsvFrom(clientTheseResponseDtoList);
        String expected = """
                "NNT";"DOI";"NumSujet";"NumSujetSansS";"Auteurs";"Titre FR";"Titre EN";"Directeurs";"Discipline";"Date de soutenance";"Date de première inscription";"Date d'archivage CINES";"Etablissement de soutenance";"Code etablissement";"Etablissement de cotutelle";"Ecoles doctorales";"Partenaires de recherche";"Président du jury";"Rapporteurs";"Examinateurs";"Mots clés FR";"Mots clés EN";"Langue";"Noms des sets OAI";"Source";"Statut";"Cas";"Accessible"
                "";"";"";"";"nono";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"en cours";"";"";
                """;

        assertEquals(expected, actual);
    }

    @Test
    void writeToCsvFrom_1_auteur_sans_ppn_sans_nom() {
        ArrayList<PersonneRefDto> personneList = new ArrayList<>();
        personneList.add(PersonneRefDto.builder().prenom("toto").build());
        clientTheseResponseDtoList = new ArrayList<>();
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
                .build());

        String actual = CsvWriter.ClientTheseResponseDtoCsvWriter.writeToCsvFrom(clientTheseResponseDtoList);
        String expected = """
                "NNT";"DOI";"NumSujet";"NumSujetSansS";"Auteurs";"Titre FR";"Titre EN";"Directeurs";"Discipline";"Date de soutenance";"Date de première inscription";"Date d'archivage CINES";"Etablissement de soutenance";"Code etablissement";"Etablissement de cotutelle";"Ecoles doctorales";"Partenaires de recherche";"Président du jury";"Rapporteurs";"Examinateurs";"Mots clés FR";"Mots clés EN";"Langue";"Noms des sets OAI";"Source";"Statut";"Cas";"Accessible"
                "";"";"";"";"toto";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"";"en cours";"";"";
                """;

        assertEquals(expected, actual);
    }

}