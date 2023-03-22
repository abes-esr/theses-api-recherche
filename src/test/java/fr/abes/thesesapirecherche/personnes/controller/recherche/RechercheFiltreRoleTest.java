package fr.abes.thesesapirecherche.personnes.controller.recherche;

import fr.abes.thesesapirecherche.EnableOnIntegrationTest;
import fr.abes.thesesapirecherche.personnes.controller.PersonneControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RechercheFiltreRoleTest extends PersonneControllerTest {

    /* -------- */
    /*  Auteur  */
    /* -------- */

    @Test
    @DisplayName("Rechercher des personnes avec le filtre role : Auteur")
    @EnableOnIntegrationTest
    public void rechercherRoleAuteur() throws Exception {
        mockMvc.perform(get("/api/v1/tests/personnes/recherche/?q=rousseau&index=per_recherche_simple_rousseau&filtres=[role=\"auteur\"]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalHits").value(5))
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2004BRES2040')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2007PA066375')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2011ANGE0040')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2003PA066582')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='1997PA040286')])]").exists());
    }

    /* ------------ */
    /*  Directeur   */
    /* ------------ */

    @Test
    @DisplayName("Rechercher des personnes avec le filtre role : Directeur de thèse")
    @EnableOnIntegrationTest
    public void rechercherRoleDirecteur() throws Exception {
        mockMvc.perform(get("/api/v1/tests/personnes/recherche/?q=rousseau&index=per_recherche_simple_rousseau&filtres=[role=\"directeur de thèse\"]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalHits").value(5))
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2021AIXM0253')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2018AIXM0659')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2018AIXM0198')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s347820')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2022PA01H030')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2020PA01H073')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2019PA01H036')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2019PA01H067')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2019PA01H090')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2018PA01H046')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2015PA010584')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2012PA010707')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2012PA010644')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2022PA01H081')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s353038')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s303558')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s303565')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s261078')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s236755')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s236749')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s236757')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s236736')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s216908')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s216921')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s216950')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s192553')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s192600')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s192592')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s170202')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s170210')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s127632')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s233939')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2019LYSES011')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2019LYSES010')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2017LYSES022')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2017LYSES012')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2017LYSES011')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2016LYSES056')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2015STET4004')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2014STET4015')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2014STET4010')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2014STET4006')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2014STET4002')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2013STET4002')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2012STET4009')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2010STET4019')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2008STET4009')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2008STET4015')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2008ISAL0003')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2004STET4012')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2004STET4010')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2004INPG0060')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2003ISAL0049')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2000STET4011')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='1996ISAL0026')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='1995ISAL0076')])]").exists());
    }

    /* ------------ */
    /*  Président   */
    /* ------------ */

    @Test
    @DisplayName("Rechercher des personnes avec le filtre role : Président du jury")
    @EnableOnIntegrationTest
    public void rechercherRolePresident() throws Exception {
        mockMvc.perform(get("/api/v1/tests/personnes/recherche/?q=rousseau&index=per_recherche_simple_rousseau&filtres=[role=\"président du jury\"]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalHits").value(2))
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2021SORUL154')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2020PA01H040')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2018PA01H049')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2016PA040017')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2014PA040127')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2013PA100201')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2013PA010652')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2013PA010597')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2012AIXM3115')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2012AIXM3115')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2018LYSE1193')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2016LYSES015')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2015CAEN2008')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2012STET4019')])]").exists());
    }

    /* ------------ */
    /*  Rapporteurs */
    /* ------------ */

    @Test
    @DisplayName("Rechercher des personnes avec le filtre role : Rapporteur")
    @EnableOnIntegrationTest
    public void rechercherRoleRapporteur() throws Exception {
        mockMvc.perform(get("/api/v1/tests/personnes/recherche/?q=rousseau&index=per_recherche_simple_rousseau&filtres=[role=\"rapporteur\"]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalHits").value(3))
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2017GREAM026')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2016SACLS213')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2021PA080021')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2021SORUL154')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2019LYSE2095')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2013PA100201')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2017ECLI0025')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2017SACLS479')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2015GREAT020')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2014PA112271')])]").exists());
    }

    /* --------------- */
    /*  Membre du jury */
    /* --------------- */

    @Test
    @DisplayName("Rechercher des personnes avec le filtre role : Membre du jury")
    @EnableOnIntegrationTest
    public void rechercherRoleMembre() throws Exception {
        mockMvc.perform(get("/api/v1/tests/personnes/recherche/?q=rousseau&index=per_recherche_simple_rousseau&filtres=[role=\"membre du jury\"]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalHits").value(3))
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2020AIXM0184')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2018AIXM0224')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2018AIXM0140')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2017SACLX027')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2014AIXM4745')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2017PA01H038')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2016PA040191')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2016PA01H032')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2016PA040052')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2012PA040212')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2011PA040151')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2016SACLN057')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2016LYSES039')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2013STET4011')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2012GRENY105')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2011STET4017')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2011STET4012')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2007INPG0139')])]").exists());
    }


    /* --------------------- */
    /*  Auteur ou Directeur  */
    /* --------------------- */

    @Test
    @DisplayName("Rechercher des personnes avec le filtre role : Auteur ou Directeur")
    @EnableOnIntegrationTest
    public void rechercherRoleAuteurOuDirecteur() throws Exception {
        mockMvc.perform(get("/api/v1/tests/personnes/recherche/?q=rousseau&index=per_recherche_simple_rousseau&filtres=[role=\"auteur\"%26role=\"directeur de thèse\"]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalHits").value(8))
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2007PA066375')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2011ANGE0040')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2003PA066582')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2004BRES2040')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2021AIXM0253')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2018AIXM0659')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2018AIXM0198')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s347820')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='1997PA040286')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2022PA01H030')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2020PA01H073')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2019PA01H036')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2019PA01H067')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2019PA01H090')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2018PA01H046')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2015PA010584')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2012PA010707')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2012PA010644')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2022PA01H081')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s353038')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s303558')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s303565')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s261078')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s236755')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s236749')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s236757')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s236736')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s216908')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s216921')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s216950')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s192553')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s192600')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s192592')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s170202')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s170210')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s127632')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s233939')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2019LYSES011')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2019LYSES010')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2017LYSES022')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2017LYSES012')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2017LYSES011')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2016LYSES056')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2015STET4004')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2014STET4015')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2014STET4010')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2014STET4006')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2014STET4002')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2013STET4002')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2012STET4009')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2010STET4019')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2008STET4009')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2008STET4015')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2008ISAL0003')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2004STET4012')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2004STET4010')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2004INPG0060')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2003ISAL0049')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2000STET4011')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='1996ISAL0026')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='1995ISAL0076')])]").exists());
    }

    /* ------------------------- */
    /*  Directeur ou Rapporteur  */
    /* ------------------------- */

    @Test
    @DisplayName("Rechercher des personnes avec le filtre role : Directeur ou Rapporteur")
    @EnableOnIntegrationTest
    public void rechercherRoleDirecteurouRapporteur() throws Exception {
        mockMvc.perform(get("/api/v1/tests/personnes/recherche/?q=rousseau&index=per_recherche_simple_rousseau&filtres=[role=\"directeur de thèse\"%26role=\"rapporteur\"]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalHits").value(5))
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s347820')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s233939')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2021AIXM0253')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2018AIXM0659')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2018AIXM0198')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2017GREAM026')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2016SACLS213')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2022PA01H030')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2020PA01H073')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2019PA01H036')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2019PA01H067')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2019PA01H090')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2018PA01H046')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2015PA010584')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2012PA010707')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2012PA010644')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2022PA01H081')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s353038')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s303558')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s303565')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s261078')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s236755')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s236749')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s236757')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s236736')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s216908')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s216921')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s216950')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s192553')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s192600')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s192592')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s170202')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s170210')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s127632')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2021PA080021')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2021SORUL154')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2019LYSE2095')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2013PA100201')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2019LYSES011')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2019LYSES010')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2017LYSES022')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2017LYSES012')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2017LYSES011')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2016LYSES056')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2015STET4004')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2014STET4015')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2014STET4010')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2014STET4006')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2014STET4002')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2013STET4002')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2012STET4009')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2010STET4019')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2008STET4009')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2008STET4015')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2008ISAL0003')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2004STET4012')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2004STET4010')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2004INPG0060')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2003ISAL0049')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2000STET4011')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='1996ISAL0026')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='1995ISAL0076')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2017ECLI0025')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2017SACLS479')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2015GREAT020')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2014PA112271')])]").exists());
    }

    /* --------------------------------- */
    /*  Directeur ou Président du jury   */
    /* --------------------------------- */

    @Test
    @DisplayName("Rechercher des personnes avec le filtre role : Directeur ou Président du jury")
    @EnableOnIntegrationTest
    public void rechercherRoleDirecteurouPresident() throws Exception {
        mockMvc.perform(get("/api/v1/tests/personnes/recherche/?q=rousseau&index=per_recherche_simple_rousseau&filtres=[role=\"directeur de thèse\"%26role=\"président du jury\"]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalHits").value(5))
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2021AIXM0253')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2018AIXM0659')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2018AIXM0198')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s347820')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s233939')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2022PA01H030')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2020PA01H073')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2019PA01H036')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2019PA01H067')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2019PA01H090')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2018PA01H046')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2015PA010584')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2012PA010707')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2012PA010644')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2022PA01H081')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s353038')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s303558')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s303565')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s261078')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s236755')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s236749')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s236757')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s236736')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s216908')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s216921')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s216950')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s192553')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s192600')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s192592')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s170202')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s170210')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s127632')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2021SORUL154')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2020PA01H040')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2018PA01H049')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2016PA040017')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2014PA040127')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2013PA100201')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2013PA010652')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2013PA010597')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2012AIXM3115')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2012AIXM3115')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2019LYSES011')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2019LYSES010')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2017LYSES022')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2017LYSES012')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2017LYSES011')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2016LYSES056')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2015STET4004')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2014STET4015')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2014STET4010')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2014STET4006')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2014STET4002')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2013STET4002')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2012STET4009')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2010STET4019')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2008STET4009')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2008STET4015')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2008ISAL0003')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2004STET4012')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2004STET4010')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2004INPG0060')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2003ISAL0049')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2000STET4011')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='1996ISAL0026')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='1995ISAL0076')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2018LYSE1193')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2016LYSES015')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2015CAEN2008')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2012STET4019')])]").exists());
    }

    /* ------------------------------ */
    /*  Directeur ou Membre du jury   */
    /* ------------------------------ */

    @Test
    @DisplayName("Rechercher des personnes avec le filtre role : Directeur ou Membre du jury")
    @EnableOnIntegrationTest
    public void rechercherRoleDirecteurouMembre() throws Exception {
        mockMvc.perform(get("/api/v1/tests/personnes/recherche/?q=rousseau&index=per_recherche_simple_rousseau&filtres=[role=\"directeur de thèse\"%26role=\"membre du jury\"]"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalHits").value(5))
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2021AIXM0253')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2018AIXM0659')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2018AIXM0198')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2020AIXM0184')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2018AIXM0224')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2018AIXM0140')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2017SACLX027')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2014AIXM4745')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2022PA01H030')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2020PA01H073')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2019PA01H036')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2019PA01H067')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2019PA01H090')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2018PA01H046')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2015PA010584')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2012PA010707')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2012PA010644')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2022PA01H081')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s353038')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s303558')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s303565')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s261078')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s236755')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s236749')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s236757')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s236736')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s216908')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s216921')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s216950')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s192553')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s192600')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s192592')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s170202')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s170210')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='s127632')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2017PA01H038')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2016PA040191')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2016PA01H032')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2016PA040052')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2012PA040212')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2011PA040151')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2019LYSES011')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2019LYSES010')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2017LYSES022')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2017LYSES012')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2017LYSES011')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2016LYSES056')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2015STET4004')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2014STET4015')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2014STET4010')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2014STET4006')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2014STET4002')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2013STET4002')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2012STET4009')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2010STET4019')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2008STET4009')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2008STET4015')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2008ISAL0003')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2004STET4012')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2004STET4010')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2004INPG0060')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2003ISAL0049')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2000STET4011')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='1996ISAL0026')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='1995ISAL0076')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2016SACLN057')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2016LYSES039')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2013STET4011')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2012GRENY105')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2011STET4017')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2011STET4012')])]").exists())
                .andExpect(jsonPath("$.personnes[?(@.theses[?(@.nnt=='2007INPG0139')])]").exists());
    }

}
