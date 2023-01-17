package fr.abes.thesesapirecherche.theses.converters;

import co.elastic.clients.elasticsearch.core.search.Hit;
import fr.abes.thesesapirecherche.theses.dto.TheseLiteResponseDto;
import fr.abes.thesesapirecherche.theses.model.These;

public class TheseLiteMapper {

    ThesePersonneMapper personneMapper = new ThesePersonneMapper();
    OrganismeMapper organismeMapper = new OrganismeMapper();

    public TheseLiteResponseDto theseLiteToDto(Hit<These> theseHit) {
        return TheseLiteResponseDto.builder()
                .titres(theseHit.source().getTitres())
                .titrePrincipal(theseHit.source().getTitrePrincipal())
                .etabSoutenanceN(theseHit.source().getEtabSoutenanceN())
                .dateSoutenance(theseHit.source().getDateSoutenance())
                .auteurs(theseHit.source().getAuteurs())
                .directeurs(theseHit.source().getDirecteurs())
                .nnt(theseHit.source().getNnt())
                .codeEtab(theseHit.source().getCodeEtab())
                .discipline(theseHit.source().getDiscipline())
                .build();
    }
}
