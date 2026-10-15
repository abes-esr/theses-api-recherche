package fr.abes.thesesapirecherche.personnes.model;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SujetsRameau {
    private String ppn;
    private String libelle;
}
