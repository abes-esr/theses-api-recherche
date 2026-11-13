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
                .auteurs(these.getAuteurs() != null ? these.getAuteurs().stream().map(personneRefMapper::personneToDto).toList() : null)
                .directeurs(these.getDirecteurs() != null ? these.getDirecteurs().stream().map(personneRefMapper::personneToDto).toList() : null)
                .rapporteurs(these.getRapporteurs() != null ? these.getRapporteurs().stream().map(personneRefMapper::personneToDto).toList() : null)
                .examinateurs(these.getRapporteurs() != null ? these.getMembresJury().stream().map(personneRefMapper::personneToDto).toList() : null)
                .president(these.getPresidentJury() != null ? personneRefMapper.personneToDto(these.getPresidentJury()) : null)
                .nnt(these.getNnt())
                .doi(these.getDoi())
                .discipline(these.getDiscipline())
                .status(these.getStatus())
                .ecolesDoctorale(these.getEcolesDoctorales() != null ? organismeMapper.organismesToDto(these.getEcolesDoctorales()) : null)
                .partenairesDeRecherche(these.getPartenairesRecherche() != null ? organismeMapper.organismesToDto(these.getPartenairesRecherche()) : null)
                .sujets(these.getSujets())
                .sujetsRameau(these.getSujetsRameau())
                .build();
    }
}