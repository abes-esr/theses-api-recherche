package fr.abes.thesesapirecherche.theses.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Setter
public class ThesesByOrganismeDto<T> {
    // TODO : breaking change, mettre le e de établissement en majuscule
    private long totalHitsetabSoutenance;
    private List<T> etabSoutenance;
    private long totalHitsetabSoutenanceEnCours;
    private List<T> etabSoutenanceEnCours;
    private long totalHitspartenaireRecherche;
    private List<T> partenaireRecherche;
    private long totalHitspartenaireRechercheEnCours;
    private List<T> partenaireRechercheEnCours;
    private long totalHitsetabCotutelle;
    private List<T> etabCotutelle;
    private long totalHitsetabCotutelleEnCours;
    private List<T> etabCotutelleEnCours;
    private long totalHitsecoleDoctorale;
    private List<T> ecoleDoctorale;
    private long totalHitsecoleDoctoraleEnCours;
    private List<T> ecoleDoctoraleEnCours;
}
