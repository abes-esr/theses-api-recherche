package fr.abes.thesesapirecherche.theses.dto;

import fr.abes.thesesapirecherche.theses.model.PersonneThese;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class TheseLiteResponseDto {
    String id;
    String titrePrincipal;
    String etabSoutenanceN;
    String etabSoutenancePpn;
    String dateSoutenance;
    String datePremiereInscriptionDoctorat;
    List<PersonneThese> auteurs;
    List<PersonneThese> directeurs;
    String nnt;
    String discipline;
    String status;
}
