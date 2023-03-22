package fr.abes.thesesapirecherche.personnes.controller;

import fr.abes.thesesapirecherche.EnableOnIntegrationTest;
import fr.abes.thesesapirecherche.ThesesApiRechercheApplicationTests;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PersonneControllerTest extends ThesesApiRechercheApplicationTests {

    /* --------------------- */
    /*  Rechercher test API */
    /* -------------------- */

    @Test
    @DisplayName("Rechercher personne avec un mot - Mauvaise méthode")
    public void rechercherMauvaiseMethode() throws Exception {
        mockMvc.perform(post("/api/v1/tests/personnes/recherche"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value("METHOD_NOT_ALLOWED"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("Rechercher personne avec un mot - sans argument")
    public void rechercherSansArguments() throws Exception {
        mockMvc.perform(get("/api/v1/tests/personnes/recherche"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").exists());
    }

    /* ---------------------------- */
    /*  Rechercher avec nom, prénom */
    /* ---------------------------- */

    @Test
    @DisplayName("Rechercher personne avec le mot Rousseau")
    @EnableOnIntegrationTest
    public void rechercherRousseau() throws Exception {
        mockMvc.perform(get("/api/v1/tests/personnes/recherche/?q=Rousseau&index=per_recherche_simple_rousseau"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalHits").value(8))
                .andExpect(jsonPath("$.personnes[?(@.id)]").exists())
                .andExpect(jsonPath("$.personnes[?(@.nom)]").exists())
                .andExpect(jsonPath("$.personnes[?(@.prenom)]").exists())
                .andExpect(jsonPath("$.personnes[?(@.has_idref)]").exists());
    }

    /* ------------------------------- */
    /*  Rechercher avec une thématique */
    /* ------------------------------- */

    @Test
    @DisplayName("Rechercher personne avec la thématique 'hyperbolicité'")
    @EnableOnIntegrationTest
    public void rechercherThematiqueHyperbolicité() throws Exception {
        mockMvc.perform(get("/api/v1/tests/personnes/recherche/?q=hyperbolicité&index=per_recherche_sujet&debut=0&nombre=100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalHits").value(32))
                .andExpect(jsonPath("$.personnes[?(@.id=='221478388')]").exists())
                .andExpect(jsonPath("$.personnes[?(@.id=='07069348X')]").exists())
                .andExpect(jsonPath("$.personnes[?(@.id=='156182831')]").exists())
                .andExpect(jsonPath("$.personnes[?(@.id=='082989117')]").exists())
                .andExpect(jsonPath("$.personnes[?(@.id=='234956372')]").exists())
                .andExpect(jsonPath("$.personnes[?(@.id=='031753973')]").exists())
                .andExpect(jsonPath("$.personnes[?(@.id=='120567822')]").exists())
                .andExpect(jsonPath("$.personnes[?(@.id=='184714346')]").exists())
                .andExpect(jsonPath("$.personnes[?(@.id=='028750691')]").exists())
                .andExpect(jsonPath("$.personnes[?(@.id=='223395471')]").exists())
                .andExpect(jsonPath("$.personnes[?(@.id=='098307991')]").exists())
                .andExpect(jsonPath("$.personnes[?(@.id=='133999793')]").exists())
                .andExpect(jsonPath("$.personnes[?(@.id=='262100606')]").exists())
                .andExpect(jsonPath("$.personnes[?(@.id=='096319097')]").exists())
                .andExpect(jsonPath("$.personnes[?(@.id=='112080553')]").exists())
                .andExpect(jsonPath("$.personnes[?(@.id=='098313940')]").exists())
                .andExpect(jsonPath("$.personnes[?(@.id=='07099563X')]").exists())
                .andExpect(jsonPath("$.personnes[?(@.id=='152938095')]").exists())
                .andExpect(jsonPath("$.personnes[?(@.id=='118017853')]").exists())
                .andExpect(jsonPath("$.personnes[?(@.id=='204758394')]").exists())
                .andExpect(jsonPath("$.personnes[?(@.id=='137728395')]").exists())
                .andExpect(jsonPath("$.personnes[?(@.id=='098248782')]").exists())
                .andExpect(jsonPath("$.personnes[?(@.id=='031832113')]").exists())
                .andExpect(jsonPath("$.personnes[?(@.id=='050264753')]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2021AIXM0253')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2017GREAM026')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2018AIXM0198')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2021AIXM0253')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2017SACLX027')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2018AIXM0198')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2017SACLX027')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2018AIXM0198')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2021AIXM0253')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2017GREAM026')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2017GREAM026')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2004BRES2040')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2017GREAM026')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2021AIXM0253')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2018AIXM0198')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2021AIXM0253')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2017GREAM026')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2018AIXM0198')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2017SACLX027')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2017SACLX027')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2017GREAM026')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2017GREAM026')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2017SACLX027')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2017SACLX027')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2021AIXM0253')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2004BRES2040')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2017SACLX027')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2017GREAM026')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2021AIXM0253')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2018AIXM0198')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2018AIXM0198')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2021AIXM0253')])]").exists());
    }

    /* ---------------------- */
    /*  Rechercher avec un ID */
    /* ---------------------- */

    @Test
    @DisplayName("Rechercher personne avec un id - Mauvaise méthode")
    public void rechercherAvecIdMauvaiseMethode() throws Exception {
        mockMvc.perform(post("/api/v1/tests/personnes/personne/098248782?index=per_recherche_simple_rousseau"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value("METHOD_NOT_ALLOWED"))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("Rechercher personne avec son identifiant")
    @EnableOnIntegrationTest
    public void rechercherAvecId() throws Exception {
        mockMvc.perform(get("/api/v1/tests/personnes/personne/050646176/?index=per_recherche_simple_rousseau"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("050646176"))
                .andExpect(jsonPath("$.nom").value("Rousseau"))
                .andExpect(jsonPath("$.prenom").value("Pascal"))
                .andExpect(jsonPath("$.has_idref").value(true))
                .andExpect(jsonPath("$.theses[?(@.nnt=='1997PA040286')]").exists())
                .andExpect(jsonPath("$.theses[?(@.nnt=='1997PA040286')].role").value("auteur"))
                .andExpect(jsonPath("$.theses[?(@.nnt=='1997PA040286')].status").value("soutenue"))
                .andExpect(jsonPath("$.theses[?(@.nnt=='1997PA040286')].titre").value("\"Réalité, peinture pure\" : l'oeuvre de Robert Delaunay jusqu'en 1914"))
                .andExpect(jsonPath("$.theses[?(@.nnt=='1997PA040286')].etablissement_soutenance").value("Paris 4"))
                .andExpect(jsonPath("$.theses[?(@.nnt=='1997PA040286')].discipline").value("Art et archéologie"))
                .andExpect(jsonPath("$.theses[?(@.nnt=='1997PA040286')].date_soutenance").value("1998"));
    }
}
