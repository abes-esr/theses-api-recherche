package fr.abes.thesesapirecherche.theses.controller;

import fr.abes.thesesapirecherche.ThesesApiRechercheApplicationTests;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class SearchThesesControllerTest extends ThesesApiRechercheApplicationTests {

    @Test
    public void remplacementMotsCle() {
        String q = "mais \"ble\" ET ET orge OU \" SAUF \" SAUF test";
        q = new SearchThesesController().remplaceEtOuSauf(q);
        assertThat(q).isEqualTo("mais \"ble\" AND AND orge OR \" SAUF \" NOT test");
    }

    @Test
    public void remplacementMotsCleSimple() {
        String q = "\" ET \" ET test";
        q = new SearchThesesController().remplaceEtOuSauf(q);
        assertThat(q).isEqualTo("\" ET \" AND test");
    }

    @Test
    public void remplacementMotsCle2() {
        String q = "mais \"ble\" ET ET orge OU \" SAUF \" SAUF a ET b OU c ET d SAUF test\"";
        q = new SearchThesesController().remplaceEtOuSauf(q);
        assertThat(q).isEqualTo("mais \"ble\" AND AND orge OR \" SAUF \" NOT a AND b OR c AND d NOT test\"");
    }
    @Test
    public void remplacementMotsCle3() {
        String q = "mais ET ble\" ET ET orge SAUF a ET b OU c ET d SAUF test";
        q = new SearchThesesController().remplaceEtOuSauf(q);
        assertThat(q).isEqualTo("mais AND ble\" ET ET orge SAUF a ET b OU c ET d SAUF test");
    }




}
