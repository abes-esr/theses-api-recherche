package fr.abes.thesesapirecherche.theses.converters;

import fr.abes.thesesapirecherche.theses.service.DateFormatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import co.elastic.clients.elasticsearch.core.search.Hit;
import fr.abes.thesesapirecherche.theses.dto.TheseLiteDto;
import fr.abes.thesesapirecherche.theses.model.These;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class TheseLiteMapper {
    private final OrganismeMapper organismeMapper;
    private final PersonneRefMapper personneRefMapper;
    private final DateFormatService dateFormatService;

    public TheseLiteDto theseLiteToDto(Hit<These> theseHit) {
        return TheseLiteDto.builder()
                .id(theseHit.id())
                .titrePrincipal(Objects.requireNonNull(theseHit.source()).getTitrePrincipal())
                .titreEN(theseHit.source().getTitres().get("en") != null ? theseHit.source().getTitres().get("en") : "")
                .etabSoutenanceN(theseHit.source().getEtabSoutenanceN())
                .etabSoutenancePpn(theseHit.source().getEtabSoutenancePpn())
                .dateSoutenance(dateFormatService.formatDate(theseHit.source().getDateSoutenance()))
                .datePremiereInscriptionDoctorat(dateFormatService.formatDate(theseHit.source().getDatePremiereInscriptionDoctorat()))
                .auteurs(theseHit.source().getAuteurs() != null ? theseHit.source().getAuteurs().stream().map(personneRefMapper::personneToDto).toList() : null)
                .directeurs(theseHit.source().getDirecteurs() != null ? theseHit.source().getDirecteurs().stream().map(personneRefMapper::personneToDto).toList() : null)
                .rapporteurs(theseHit.source().getRapporteurs() != null ? theseHit.source().getRapporteurs().stream().map(personneRefMapper::personneToDto).toList() : null)
                .examinateurs(theseHit.source().getRapporteurs() != null ? theseHit.source().getMembresJury().stream().map(personneRefMapper::personneToDto).toList() : null)
                .president(theseHit.source().getPresidentJury() != null ? personneRefMapper.personneToDto(theseHit.source().getPresidentJury()) : null)
                .nnt(theseHit.source().getNnt())
                .doi(theseHit.source().getDoi())
                .discipline(theseHit.source().getDiscipline())
                .status(theseHit.source().getStatus())
                .ecolesDoctorale(theseHit.source().getEcolesDoctorales() != null ? organismeMapper.organismesToDto(theseHit.source().getEcolesDoctorales()) : null)
                .partenairesDeRecherche(theseHit.source().getPartenairesRecherche() != null ? organismeMapper.organismesToDto(theseHit.source().getPartenairesRecherche()) : null)
                .sujets(theseHit.source().getSujets())
                .sujetsRameau(theseHit.source().getSujetsRameau())
                .build();
    }
}