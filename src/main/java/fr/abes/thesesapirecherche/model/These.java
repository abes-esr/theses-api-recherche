package fr.abes.thesesapirecherche.model;

import lombok.Getter;
import lombok.Setter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class These {

    String titre;
    String nnt;
    Date dateSoutenance;
    String abstractFR;
    String abstractEN;
    List<String> sujetsRameau;
    List<PersonneThese> membresJury;

    public String getDateSoutenance() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(dateSoutenance);
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

    public List<String> getSujetsRameau() {
        return sujetsRameau;
    }

    public void setSujetsRameau(List<String> sujetsRameau) {
        this.sujetsRameau = sujetsRameau;
    }
}
