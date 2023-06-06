package fr.abes.thesesapirecherche.personnes.model;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SujetsRameau {

    @Getter
    @Setter
    private String ppn;

    @Getter
    @Setter
    private String libelle;
}
