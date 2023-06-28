package fr.abes.thesesapirecherche.personnes.controller.recherche;

import fr.abes.thesesapirecherche.EnableOnIntegrationTest;
import fr.abes.thesesapirecherche.personnes.controller.PersonneControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RechercheBooleenTest extends PersonneControllerTest {

    /* ---- */
    /*  Ou  */
    /* ---- */

    @Test
    @DisplayName("Rechercher des personnes avec le booléen OU : OR")
    @EnableOnIntegrationTest
    public void rechercherBooleenOuOr() throws Exception {
        mockMvc.perform(get("/api/v1/tests/personnes/recherche/?q=Erwan%20OR%20Rousseau&index=per_recherche_simple_rousseau&debut=0&nombre=1000"))
                .andExpect(status().isOk())              
                .andExpect(jsonPath("$..theses[?(@=='2004BRES2040')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2021AIXM0253')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2017GREAM026')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2020AIXM0184')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2007PA066375')]").exists())
                .andExpect(jsonPath("$.personnes[?(@.id=='178429708')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2020REN1B015')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='1999ROUES082')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2011ANGE0040')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2003PA066582')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='1997PA040286')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2020PA01H073')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2021SORUL154')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2019LYSE2095')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2017PA01H038')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2019LYSES011')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2018LYSE1193')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2017ECLI0025')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2016SACLN057')]").exists());
    }


    /* ------ */
    /*  Sauf  */
    /* ------ */

    @Test
    @DisplayName("Rechercher des personnes avec le booléen SAUF : NOT")
    @EnableOnIntegrationTest
    public void rechercherBooleenSaufNot() throws Exception {
        mockMvc.perform(get("/api/v1/tests/personnes/recherche/?q=Erwan%20NOT%20Rousseau&index=per_recherche_simple_rousseau"))
                .andExpect(status().isOk())              
                .andExpect(jsonPath("$..theses[?(@=='2020REN1B015')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='1999ROUES082')]").exists());
    }

    @Test
    @DisplayName("Rechercher des personnes avec le booléen SAUF : -")
    @EnableOnIntegrationTest
    public void rechercherBooleenSaufMoins() throws Exception {
        mockMvc.perform(get("/api/v1/tests/personnes/recherche/?q=Erwan%20-Rousseau&index=per_recherche_simple_rousseau"))
                .andExpect(status().isOk())            
                .andExpect(jsonPath("$..theses[?(@=='2020REN1B015')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='1999ROUES082')]").exists());
    }

    @Test
    @DisplayName("Rechercher des personnes 2 - avec le booléen SAUF : NOT")
    @EnableOnIntegrationTest
    public void rechercher2BooleenSaufNot() throws Exception {
        mockMvc.perform(get("/api/v1/tests/personnes/recherche/?q=Rousseau%20NOT%20Erwan&index=per_recherche_simple_rousseau"))
                .andExpect(status().isOk())             
                .andExpect(jsonPath("$..theses[?(@=='2011ANGE0040')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2003PA066582')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='1997PA040286')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2020PA01H073')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2021SORUL154')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2019LYSE2095')]").exists());
    }

    /* ------ */
    /*  Et    */
    /* ------ */

    @Test
    @DisplayName("Rechercher des personnes avec le booléen ET : AND")
    @EnableOnIntegrationTest
    public void rechercherBooleenEtAnd() throws Exception {
        mockMvc.perform(get("/api/v1/tests/personnes/recherche/?q=Erwan%20AND%20Rousseau&index=per_recherche_simple_rousseau"))
                .andExpect(status().isOk())                
                .andExpect(jsonPath("$..theses[?(@=='2004BRES2040')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2021AIXM0253')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2017GREAM026')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2020AIXM0184')]").exists());
    }

    @Test
    @DisplayName("Rechercher des personnes avec le booléen ET : +")
    @EnableOnIntegrationTest
    public void rechercherBooleenEtPlus() throws Exception {
        mockMvc.perform(get("/api/v1/tests/personnes/recherche/?q=Erwan%20+%20Rousseau&index=per_recherche_simple_rousseau"))
                .andExpect(status().isOk())               
                .andExpect(jsonPath("$..theses[?(@=='2004BRES2040')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2021AIXM0253')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2017GREAM026')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2020AIXM0184')]").exists());
    }

}
