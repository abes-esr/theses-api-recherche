package fr.abes.thesesapirecherche.converters;

import fr.abes.thesesapirecherche.dto.TheseResponseDto;
import fr.abes.thesesapirecherche.model.These;

import java.util.ArrayList;
import java.util.List;

public class TheseMapper {
    ThesePersonneMapper personneMapper = new ThesePersonneMapper();

    public TheseResponseDto theseToDto(These these) {
        return TheseResponseDto.builder()
                .nnt(these.getNnt())
                .titre(these.getTitre())
                .dateSoutenance(these.getDateSoutenance())
                .abstractFR(these.getAbstractFR())
                .abstractEN(these.getAbstractEN())
                .sujetsRameau(these.getSujetsRameau())
                .membresJury(personneMapper.personnesToDto(these.getMembresJury()))
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
