package fr.abes.thesesapirecherche.theses.converters;

import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Component;

import co.elastic.clients.elasticsearch.core.search.Hit;
import fr.abes.thesesapirecherche.theses.dto.TheseLiteResponseDto;
import fr.abes.thesesapirecherche.theses.model.These;

@Component 
public class TheseLiteMapper {

    OrganismeMapper organismeMapper = new OrganismeMapper();

    public TheseLiteResponseDto theseLiteToDto(Hit<These> theseHit) {
        return TheseLiteResponseDto.builder()
                .id(theseHit.id())
                .titrePrincipal(theseHit.source().getTitrePrincipal())
                .titreEN(theseHit.source().getTitres().get("en") != null ? theseHit.source().getTitres().get("en") : "")
                .etabSoutenanceN(theseHit.source().getEtabSoutenanceN())
                .etabSoutenancePpn(theseHit.source().getEtabSoutenancePpn())
//                .dateSoutenance(theseHit.source().getDateSoutenance()) //FIXME
//                .datePremiereInscriptionDoctorat(theseHit.source().getDatePremiereInscriptionDoctorat())
                .auteurs(theseHit.source().getAuteurs())
                .directeurs(theseHit.source().getDirecteurs())
                .rapporteurs(theseHit.source().getRapporteurs())
                .examinateurs(theseHit.source().getMembresJury())
                .president(theseHit.source().getPresidentJury())
                .nnt(theseHit.source().getNnt())
                .doi(theseHit.source().getDoi())
                .discipline(theseHit.source().getDiscipline())
                .status(theseHit.source().getStatus())
                .ecolesDoctorale(organismeMapper.organismesToDto(theseHit.source().getEcolesDoctorales()))
                .partenairesDeRecherche(organismeMapper.organismesToDto(theseHit.source().getPartenairesRecherche()))
                .sujets(theseHit.source().getSujets())
                .sujetsRameau(theseHit.source().getSujetsRameau())
                .build();
    }

    public TheseLiteResponseDto searchHitsTheseToTheseLiteResponseDto(SearchHit<These> theseHit) {
        return TheseLiteResponseDto.builder()
                .id(theseHit.getContent().getIdDoc())
                .titrePrincipal(theseHit.getContent().getTitrePrincipal())
                .titreEN(theseHit.getContent().getTitres().get("en") != null ? theseHit.getContent().getTitres().get("en") : "")
                .etabSoutenanceN(theseHit.getContent().getEtabSoutenanceN())
                .etabSoutenancePpn(theseHit.getContent().getEtabSoutenancePpn())
//                .dateSoutenance(theseHit.source().getDateSoutenance()) //FIXME
//                .datePremiereInscriptionDoctorat(theseHit.source().getDatePremiereInscriptionDoctorat())
                .auteurs(theseHit.getContent().getAuteurs())
                .directeurs(theseHit.getContent().getDirecteurs())
                .rapporteurs(theseHit.getContent().getRapporteurs())
                .examinateurs(theseHit.getContent().getMembresJury())
                .president(theseHit.getContent().getPresidentJury())
                .nnt(theseHit.getContent().getNnt())
                .doi(theseHit.getContent().getDoi())
                .discipline(theseHit.getContent().getDiscipline())
                .status(theseHit.getContent().getStatus())
                .ecolesDoctorale(organismeMapper.organismesToDto(theseHit.getContent().getEcolesDoctorales()))
                .partenairesDeRecherche(organismeMapper.organismesToDto(theseHit.getContent().getPartenairesRecherche()))
                .sujets(theseHit.getContent().getSujets())
                .sujetsRameau(theseHit.getContent().getSujetsRameau())
                .build();
    }
}