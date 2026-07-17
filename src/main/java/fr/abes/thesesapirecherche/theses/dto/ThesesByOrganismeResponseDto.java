package fr.abes.thesesapirecherche.theses.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;

@Getter
@Setter
@JsonView({JsonViews.Full.class, JsonViews.Lite.class})
public class ThesesByOrganismeResponseDto {
    private long totalHitsetabSoutenance;
    private List<TheseEnhancedResponseDto> etabSoutenance;
    private long totalHitsetabSoutenanceEnCours;
    private List<TheseEnhancedResponseDto> etabSoutenanceEnCours;
    private long totalHitspartenaireRecherche;
    private List<TheseEnhancedResponseDto> partenaireRecherche;
    private long totalHitspartenaireRechercheEnCours;
    private List<TheseEnhancedResponseDto> partenaireRechercheEnCours;
    private long totalHitsetabCotutelle;
    private List<TheseEnhancedResponseDto> etabCotutelle;
    private long totalHitsetabCotutelleEnCours;
    private List<TheseEnhancedResponseDto> etabCotutelleEnCours;
    private long totalHitsecoleDoctorale;
    private List<TheseEnhancedResponseDto> ecoleDoctorale;
    private long totalHitsecoleDoctoraleEnCours;
    private List<TheseEnhancedResponseDto> ecoleDoctoraleEnCours;
}
