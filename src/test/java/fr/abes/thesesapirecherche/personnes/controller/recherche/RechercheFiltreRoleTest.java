package fr.abes.thesesapirecherche.personnes.controller.recherche;

import fr.abes.thesesapirecherche.EnableOnIntegrationTest;
import fr.abes.thesesapirecherche.personnes.controller.PersonneControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        mockMvc.perform(get("/api/v1/tests/personnes/recherche/?q=rousseau&index=per_affichage_theses_roles_etabs_disc&filtres=[role=\"auteur\"]&debut=0&nombre=1000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..theses[?(@=='2004BRES2040')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2007PA066375')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2011ANGE0040')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2003PA066582')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='1997PA040286')]").exists());
    }

    /* ------------ */
    /*  Directeur   */
    /* ------------ */

    @Test
    @DisplayName("Rechercher des personnes avec le filtre role : Directeur de thèse")
    @EnableOnIntegrationTest
    public void rechercherRoleDirecteur() throws Exception {
        mockMvc.perform(get("/api/v1/tests/personnes/recherche/?q=rousseau&index=per_affichage_theses_roles_etabs_disc&filtres=[role=\"directeur de thèse\"]&debut=0&nombre=1000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..theses[?(@=='2021AIXM0253')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2018AIXM0659')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2018AIXM0198')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2022PA01H030')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2020PA01H073')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2019PA01H036')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2019PA01H067')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2019PA01H090')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2018PA01H046')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2015PA010584')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2012PA010707')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2012PA010644')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2022PA01H081')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2019LYSES011')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2019LYSES010')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2017LYSES022')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2017LYSES012')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2017LYSES011')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2016LYSES056')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2015STET4004')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2014STET4015')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2014STET4010')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2014STET4006')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2014STET4002')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2013STET4002')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2012STET4009')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2010STET4019')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2008STET4009')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2008STET4015')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2008ISAL0003')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2004STET4012')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2004STET4010')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2004INPG0060')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2003ISAL0049')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2000STET4011')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='1996ISAL0026')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='1995ISAL0076')]").exists());
    }

    /* ------------ */
    /*  Président   */
    /* ------------ */

    @Test
    @DisplayName("Rechercher des personnes avec le filtre role : Président du jury")
    @EnableOnIntegrationTest
    public void rechercherRolePresident() throws Exception {
        mockMvc.perform(get("/api/v1/tests/personnes/recherche/?q=rousseau&index=per_affichage_theses_roles_etabs_disc&filtres=[role=\"président du jury\"]&debut=0&nombre=1000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..theses[?(@=='2021SORUL154')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2020PA01H040')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2018PA01H049')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2016PA040017')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2014PA040127')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2013PA100201')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2013PA010652')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2013PA010597')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2012AIXM3115')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2012AIXM3115')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2018LYSE1193')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2016LYSES015')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2015CAEN2008')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2012STET4019')]").exists());
    }

    /* ------------ */
    /*  Rapporteurs */
    /* ------------ */

    @Test
    @DisplayName("Rechercher des personnes avec le filtre role : Rapporteur")
    @EnableOnIntegrationTest
    public void rechercherRoleRapporteur() throws Exception {
        mockMvc.perform(get("/api/v1/tests/personnes/recherche/?q=rousseau&index=per_affichage_theses_roles_etabs_disc&filtres=[role=\"rapporteur\"]&debut=0&nombre=1000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..theses[?(@=='2017GREAM026')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2016SACLS213')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2021PA080021')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2021SORUL154')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2019LYSE2095')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2013PA100201')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2017ECLI0025')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2017SACLS479')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2015GREAT020')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2014PA112271')]").exists());
    }

    /* --------------- */
    /*  Membre du jury */
    /* --------------- */

    @Test
    @DisplayName("Rechercher des personnes avec le filtre role : Membre du jury")
    @EnableOnIntegrationTest
    public void rechercherRoleMembre() throws Exception {
        mockMvc.perform(get("/api/v1/tests/personnes/recherche/?q=rousseau&index=per_affichage_theses_roles_etabs_disc&filtres=[role=\"membre du jury\"]&debut=0&nombre=1000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..theses[?(@=='2020AIXM0184')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2018AIXM0224')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2018AIXM0140')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2017SACLX027')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2014AIXM4745')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2017PA01H038')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2016PA040191')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2016PA01H032')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2016PA040052')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2012PA040212')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2011PA040151')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2016SACLN057')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2016LYSES039')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2013STET4011')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2012GRENY105')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2011STET4017')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2011STET4012')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2007INPG0139')]").exists());
    }


    /* --------------------- */
    /*  Auteur ou Directeur  */
    /* --------------------- */

    @Test
    @DisplayName("Rechercher des personnes avec le filtre role : Auteur ou Directeur")
    @EnableOnIntegrationTest
    public void rechercherRoleAuteurOuDirecteur() throws Exception {
        mockMvc.perform(get("/api/v1/tests/personnes/recherche/?q=rousseau&index=per_affichage_theses_roles_etabs_disc&filtres=[role=\"auteur\"%26role=\"directeur de thèse\"]&debut=0&nombre=1000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..theses[?(@=='2007PA066375')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2011ANGE0040')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2003PA066582')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2004BRES2040')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2021AIXM0253')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2018AIXM0659')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2018AIXM0198')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='1997PA040286')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2022PA01H030')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2020PA01H073')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2019PA01H036')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2019PA01H067')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2019PA01H090')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2018PA01H046')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2015PA010584')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2012PA010707')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2012PA010644')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2022PA01H081')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2019LYSES011')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2019LYSES010')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2017LYSES022')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2017LYSES012')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2017LYSES011')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2016LYSES056')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2015STET4004')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2014STET4015')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2014STET4010')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2014STET4006')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2014STET4002')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2013STET4002')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2012STET4009')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2010STET4019')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2008STET4009')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2008STET4015')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2008ISAL0003')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2004STET4012')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2004STET4010')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2004INPG0060')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2003ISAL0049')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2000STET4011')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='1996ISAL0026')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='1995ISAL0076')]").exists());
    }

    /* ------------------------- */
    /*  Directeur ou Rapporteur  */
    /* ------------------------- */

    @Test
    @DisplayName("Rechercher des personnes avec le filtre role : Directeur ou Rapporteur")
    @EnableOnIntegrationTest
    public void rechercherRoleDirecteurouRapporteur() throws Exception {
        mockMvc.perform(get("/api/v1/tests/personnes/recherche/?q=rousseau&index=per_affichage_theses_roles_etabs_disc&filtres=[role=\"directeur de thèse\"%26role=\"rapporteur\"]&debut=0&nombre=1000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..theses[?(@=='2021AIXM0253')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2018AIXM0659')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2018AIXM0198')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2017GREAM026')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2016SACLS213')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2022PA01H030')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2020PA01H073')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2019PA01H036')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2019PA01H067')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2019PA01H090')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2018PA01H046')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2015PA010584')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2012PA010707')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2012PA010644')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2022PA01H081')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2021PA080021')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2021SORUL154')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2019LYSE2095')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2013PA100201')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2019LYSES011')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2019LYSES010')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2017LYSES022')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2017LYSES012')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2017LYSES011')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2016LYSES056')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2015STET4004')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2014STET4015')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2014STET4010')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2014STET4006')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2014STET4002')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2013STET4002')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2012STET4009')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2010STET4019')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2008STET4009')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2008STET4015')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2008ISAL0003')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2004STET4012')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2004STET4010')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2004INPG0060')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2003ISAL0049')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2000STET4011')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='1996ISAL0026')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='1995ISAL0076')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2017ECLI0025')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2017SACLS479')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2015GREAT020')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2014PA112271')]").exists());
    }

    /* --------------------------------- */
    /*  Directeur ou Président du jury   */
    /* --------------------------------- */

    @Test
    @DisplayName("Rechercher des personnes avec le filtre role : Directeur ou Président du jury")
    @EnableOnIntegrationTest
    public void rechercherRoleDirecteurouPresident() throws Exception {
        mockMvc.perform(get("/api/v1/tests/personnes/recherche/?q=rousseau&index=per_affichage_theses_roles_etabs_disc&filtres=[role=\"directeur de thèse\"%26role=\"président du jury\"]&debut=0&nombre=1000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..theses[?(@=='2021AIXM0253')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2018AIXM0659')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2018AIXM0198')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2022PA01H030')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2020PA01H073')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2019PA01H036')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2019PA01H067')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2019PA01H090')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2018PA01H046')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2015PA010584')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2012PA010707')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2012PA010644')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2022PA01H081')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2021SORUL154')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2020PA01H040')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2018PA01H049')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2016PA040017')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2014PA040127')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2013PA100201')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2013PA010652')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2013PA010597')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2012AIXM3115')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2012AIXM3115')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2019LYSES011')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2019LYSES010')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2017LYSES022')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2017LYSES012')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2017LYSES011')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2016LYSES056')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2015STET4004')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2014STET4015')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2014STET4010')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2014STET4006')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2014STET4002')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2013STET4002')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2012STET4009')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2010STET4019')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2008STET4009')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2008STET4015')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2008ISAL0003')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2004STET4012')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2004STET4010')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2004INPG0060')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2003ISAL0049')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2000STET4011')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='1996ISAL0026')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='1995ISAL0076')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2018LYSE1193')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2016LYSES015')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2015CAEN2008')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2012STET4019')]").exists());
    }

    /* ------------------------------ */
    /*  Directeur ou Membre du jury   */
    /* ------------------------------ */

    @Test
    @DisplayName("Rechercher des personnes avec le filtre role : Directeur ou Membre du jury")
    @EnableOnIntegrationTest
    public void rechercherRoleDirecteurouMembre() throws Exception {
        mockMvc.perform(get("/api/v1/tests/personnes/recherche/?q=rousseau&index=per_affichage_theses_roles_etabs_disc&filtres=[role=\"directeur de thèse\"%26role=\"membre du jury\"]&debut=0&nombre=1000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..theses[?(@=='2021AIXM0253')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2018AIXM0659')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2018AIXM0198')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2020AIXM0184')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2018AIXM0224')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2018AIXM0140')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2017SACLX027')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2014AIXM4745')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2022PA01H030')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2020PA01H073')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2019PA01H036')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2019PA01H067')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2019PA01H090')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2018PA01H046')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2015PA010584')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2012PA010707')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2012PA010644')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2022PA01H081')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2017PA01H038')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2016PA040191')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2016PA01H032')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2016PA040052')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2012PA040212')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2011PA040151')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2019LYSES011')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2019LYSES010')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2017LYSES022')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2017LYSES012')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2017LYSES011')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2016LYSES056')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2015STET4004')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2014STET4015')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2014STET4010')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2014STET4006')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2014STET4002')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2013STET4002')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2012STET4009')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2010STET4019')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2008STET4009')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2008STET4015')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2008ISAL0003')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2004STET4012')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2004STET4010')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2004INPG0060')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2003ISAL0049')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2000STET4011')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='1996ISAL0026')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='1995ISAL0076')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2016SACLN057')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2016LYSES039')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2013STET4011')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2012GRENY105')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2011STET4017')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2011STET4012')]").exists())
                .andExpect(jsonPath("$..theses[?(@=='2007INPG0139')]").exists());
    }

}
