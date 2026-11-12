package fr.abes.thesesapirecherche.theses.converters;

import fr.abes.thesesapirecherche.theses.dto.SujetDto;
import fr.abes.thesesapirecherche.theses.model.Sujet;
import fr.abes.thesesapirecherche.theses.model.SujetsRameau;
import org.springframework.stereotype.Component;

@Component
public class SujetMapper {
    public SujetDto toDto(Sujet sujet) {
        return  new SujetDto(sujet.getLibelle(), SujetDto.Type.sujet, sujet.getLibelle());
    }

    public SujetDto toDto(SujetsRameau sujet) {
        return new SujetDto(
                sujet.getLibelle(),
                SujetDto.Type.sujetsRameau,
                String.format("\"%s\" ET %s", sujet.getLibelle(), sujet.getPpn()));
    }
}
