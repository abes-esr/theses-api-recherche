package fr.abes.thesesapirecherche.theses.converters;

import org.springframework.stereotype.Component;

import co.elastic.clients.elasticsearch.core.search.Hit;
import fr.abes.thesesapirecherche.theses.dto.TheseLiteResponseDto;
import fr.abes.thesesapirecherche.theses.model.These;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component 
public class TheseLiteMapper {

    OrganismeMapper organismeMapper = new OrganismeMapper();

    private static final DateTimeFormatter DD_MM_YYYY =
            DateTimeFormatter.ofPattern("dd/MM/yyyy").withZone(ZoneId.of("Europe/Paris"));

    public TheseLiteResponseDto theseLiteToDto(These these) {
        return TheseLiteResponseDto.builder()
                .id(these.getIdDoc())
                .titrePrincipal(these.getTitrePrincipal())
                .titreEN(these.getTitres().get("en") != null ? these.getTitres().get("en") : "")
                .etabSoutenanceN(these.getEtabSoutenanceN())
                .etabSoutenancePpn(these.getEtabSoutenancePpn())
                .dateSoutenance(these.getDateSoutenance() != null ? these.getDateSoutenance().format(DD_MM_YYYY) : null)
                .datePremiereInscriptionDoctorat(these.getDatePremiereInscriptionDoctorat() != null ? these.getDatePremiereInscriptionDoctorat().format(DD_MM_YYYY) : null)
                .auteurs(these.getAuteurs())
                .directeurs(these.getDirecteurs())
                .rapporteurs(these.getRapporteurs())
                .examinateurs(these.getMembresJury())
                .president(these.getPresidentJury())
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

    public List<TheseLiteResponseDto> theseLiteToDtoNoHitList(List<These> these) {
        return these.stream().map(this::theseLiteToDto).toList();
    }
}