package fr.abes.thesesapirecherche.theses.converters;

import co.elastic.clients.elasticsearch.core.search.Hit;
import fr.abes.thesesapirecherche.theses.dto.TheseLiteResponseDto;
import fr.abes.thesesapirecherche.theses.model.These;

public class TheseLiteMapper {

    ThesePersonneMapper personneMapper = new ThesePersonneMapper();
    OrganismeMapper organismeMapper = new OrganismeMapper();
    PersonneTheseMapper personneTheseMapper = new PersonneTheseMapper();
    SujetMapper sujetMapper = new SujetMapper();
    SujetsRameauMapper sujetsRameauMapper = new SujetsRameauMapper();

    public TheseLiteResponseDto theseLiteToDto(Hit<These> theseHit) {
        return TheseLiteResponseDto.builder()
                .id(theseHit.id())
                .titrePrincipal(theseHit.source().getTitrePrincipal())
                .titreEN(theseHit.source().getTitres().get("en") != null ? theseHit.source().getTitres().get("en") : "")
                .etabSoutenanceN(theseHit.source().getEtabSoutenanceN())
                .etabSoutenancePpn(theseHit.source().getEtabSoutenancePpn())
                .dateSoutenance(theseHit.source().getDateSoutenance())
                .datePremiereInscriptionDoctorat(theseHit.source().getDatePremiereInscriptionDoctorat())
                .auteurs(personneTheseMapper.personnesTheseToDtoList(theseHit.source().getAuteurs()))
                .directeurs(personneTheseMapper.personnesTheseToDtoList(theseHit.source().getDirecteurs()))
                .rapporteurs(personneTheseMapper.personnesTheseToDtoList(theseHit.source().getRapporteurs()))
                .examinateurs(personneTheseMapper.personnesTheseToDtoList(theseHit.source().getMembresJury()))
                .president(personneTheseMapper.personneTheseToDto(theseHit.source().getPresidentJury()))
                .nnt(theseHit.source().getNnt())
                .doi(theseHit.source().getDoi())
                .discipline(theseHit.source().getDiscipline())
                .status(theseHit.source().getStatus())
                .ecolesDoctorale(organismeMapper.organismesToDto(theseHit.source().getEcolesDoctorales()))
                .partenairesDeRecherche(organismeMapper.organismesToDto(theseHit.source().getPartenairesRecherche()))
                .sujets(sujetMapper.sujetsToDtoList(theseHit.source().getSujets()))
                .sujetsRameau(sujetsRameauMapper.sujetsRameauListToDtoList(theseHit.source().getSujetsRameau()))
                .build();
    }
}
