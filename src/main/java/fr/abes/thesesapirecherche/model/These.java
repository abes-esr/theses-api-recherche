package fr.abes.thesesapirecherche.model;

import java.util.Date;
import java.util.List;

public class These {

    String titre;
    String nnt;
    Date dateSoutenance;
    String abstractFR;
    String abstractEN;
    List<String> rameau;

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getNnt() {
        return nnt;
    }

    public void setNnt(String nnt) {
        this.nnt = nnt;
    }

    public Date getDateSoutenance() {
        return dateSoutenance;
    }

    public void setDateSoutenance(Date dateSoutenance) {
        this.dateSoutenance = dateSoutenance;
    }

    public String getAbstractFR() {
        return abstractFR;
    }

    public void setAbstractFR(String abstractFR) {
        this.abstractFR = abstractFR;
    }

    public String getAbstractEN() {
        return abstractEN;
    }

    public void setAbstractEN(String abstractEN) {
        this.abstractEN = abstractEN;
    }

    public List<String> getRameau() {
        return rameau;
    }

    public void setRameau(List<String> rameau) {
        this.rameau = rameau;
    }
}
