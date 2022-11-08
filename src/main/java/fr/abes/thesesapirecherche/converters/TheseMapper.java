package fr.abes.thesesapirecherche.converters;

import fr.abes.thesesapirecherche.dto.TheseResponseDto;
import fr.abes.thesesapirecherche.model.These;

import java.util.ArrayList;
import java.util.List;

public class TheseMapper {
    ThesePersonneMapper personneMapper = new ThesePersonneMapper();
    OrganismeMapper organismeMapper = new OrganismeMapper();

    public TheseResponseDto theseToDto(These these) {
        return TheseResponseDto.builder()
                .titrePrincipal(these.getTitrePrincipal())
                .nnt(these.getNnt())
                .dateSoutenance(these.getDateSoutenance())
                .discipline(these.getDiscipline())
                .titres(these.getTitres())
                .resumes(these.getResumes())
                .etabSoutenance(organismeMapper.organismeToDto(these.getEtabSoutenance()))
                .etabCotutelle(organismeMapper.organismesToDto(these.getEtabCotutelle()))
                .partenairesRecherche(organismeMapper.organismesToDto(these.getPartenairesRecherche()))
                .sujetsRameau(these.getSujetsRameau())
                .membresJury(personneMapper.personnesToDto(these.getMembresJury()))
                .rapporteurs(personneMapper.personnesToDto(these.getRapporteurs()))
                .auteurs(personneMapper.personnesToDto(these.getAuteurs()))
                .directeurs(personneMapper.personnesToDto(these.getDirecteurs()))
                .cas(these.getCas())
                .ecolesDoctorales(organismeMapper.organismesToDto(these.getEcolesDoctorales()))
                .presidentJury(personneMapper.personneToDto(these.getPresidentJury()))
                .build();
    }

    public List<TheseResponseDto> thesesToDto(List<These> personnes) {
        List<TheseResponseDto> results = new ArrayList<>();
        for (These item : personnes) {
            results.add(theseToDto(item));
        }
        return results;
    }
}
