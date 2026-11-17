package fr.abes.thesesapirecherche.theses.converters;

import fr.abes.thesesapirecherche.theses.service.DateFormatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import co.elastic.clients.elasticsearch.core.search.Hit;
import fr.abes.thesesapirecherche.theses.dto.TheseLiteDto;
import fr.abes.thesesapirecherche.theses.model.These;

@Component
@RequiredArgsConstructor
public class TheseLiteMapper {
    private final OrganismeMapper organismeMapper;
    private final PersonneRefMapper personneRefMapper;
    private final DateFormatService dateFormatService;

    public TheseLiteDto theseLiteToDto(Hit<These> theseHit) {
        These these = theseHit.source();
        return TheseLiteDto.builder()
                .id(theseHit.id())
                .titrePrincipal(these.getTitrePrincipal())
                .titreEN(these.getTitres().get("en") != null ? these.getTitres().get("en") : "")
                .etabSoutenanceN(these.getEtabSoutenanceN())
                .etabSoutenancePpn(these.getEtabSoutenancePpn())
                .dateSoutenance(dateFormatService.formatDate(these.getDateSoutenance()))
                .datePremiereInscriptionDoctorat(dateFormatService.formatDate(these.getDatePremiereInscriptionDoctorat()))
                .auteurs(personneRefMapper.personnesToDto(these.getAuteurs()))
                .directeurs(personneRefMapper.personnesToDto(these.getDirecteurs()))
                .rapporteurs(personneRefMapper.personnesToDto(these.getRapporteurs()))
                .examinateurs(personneRefMapper.personnesToDto(these.getRapporteurs()))
                .president(personneRefMapper.personneToDto(these.getPresidentJury()))
                .nnt(these.getNnt())
                .doi(these.getDoi())
                .discipline(these.getDiscipline())
                .status(these.getStatus())
                .ecolesDoctorale(organismeMapper.organismesToDto(these.getEcolesDoctorales()))
                .partenairesDeRecherche(organismeMapper.organismesToDto(these.getPartenairesRecherche()))
                .sujets(these.getSujets())
                .sujetsRameau(these.getSujetsRameau())
                .build();
    }
}