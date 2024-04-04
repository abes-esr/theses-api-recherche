package fr.abes.thesesapirecherche.theses.converters;

import co.elastic.clients.elasticsearch.core.search.Hit;
import fr.abes.thesesapirecherche.theses.dto.TheseLiteResponseDto;
import fr.abes.thesesapirecherche.theses.model.These;

public class TheseLiteMapper {

    ThesePersonneMapper personneMapper = new ThesePersonneMapper();
    OrganismeMapper organismeMapper = new OrganismeMapper();

    public TheseLiteResponseDto theseLiteToDto(Hit<These> theseHit) {
        return TheseLiteResponseDto.builder()
                .id(theseHit.id())
                .titrePrincipal(theseHit.source().getTitrePrincipal())
                .titreEN(theseHit.source().getTitres().get("en") != null ? theseHit.source().getTitres().get("en") : "")
                .etabSoutenanceN(theseHit.source().getEtabSoutenanceN())
                .etabSoutenancePpn(theseHit.source().getEtabSoutenancePpn())
                .dateSoutenance(theseHit.source().getDateSoutenance())
                .datePremiereInscriptionDoctorat(theseHit.source().getDatePremiereInscriptionDoctorat())
                .auteurs(theseHit.source().getAuteurs())
                .directeurs(theseHit.source().getDirecteurs())
                .rapporteurs(theseHit.source().getRapporteurs())
                .examinateurs(theseHit.source().getMembresJury())
                .president(theseHit.source().getPresidentJury())
                .nnt(theseHit.source().getNnt())
                .discipline(theseHit.source().getDiscipline())
                .status(theseHit.source().getStatus())
                .ecolesDoctorale(organismeMapper.organismesToDto(theseHit.source().getEcolesDoctorales()))
                .partenairesDeRecherche(organismeMapper.organismesToDto(theseHit.source().getPartenairesRecherche()))
                .sujets(theseHit.source().getSujets())
                .sujetsRameau(theseHit.source().getSujetsRameau())
                .build();
    }
}
