package fr.abes.thesesapirecherche.theses.dto.client;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientThesesByOrganismeResponseDto {
    // TODO : breaking change, mettre le e de établissement en majuscule
    private long totalHitsetabSoutenance;
    private List<ClientTheseLiteResponseDto> etabSoutenance;
    private long totalHitsetabSoutenanceEnCours;
    private List<ClientTheseLiteResponseDto> etabSoutenanceEnCours;
    private long totalHitspartenaireRecherche;
    private List<ClientTheseLiteResponseDto> partenaireRecherche;
    private long totalHitspartenaireRechercheEnCours;
    private List<ClientTheseLiteResponseDto> partenaireRechercheEnCours;
    private long totalHitsetabCotutelle;
    private List<ClientTheseLiteResponseDto> etabCotutelle;
    private long totalHitsetabCotutelleEnCours;
    private List<ClientTheseLiteResponseDto> etabCotutelleEnCours;
    private long totalHitsecoleDoctorale;
    private List<ClientTheseLiteResponseDto> ecoleDoctorale;
    private long totalHitsecoleDoctoraleEnCours;
    private List<ClientTheseLiteResponseDto> ecoleDoctoraleEnCours;
}
