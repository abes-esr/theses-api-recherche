package fr.abes.thesesapirecherche.theses.dto;


import fr.abes.thesesapirecherche.theses.model.PersonneThese;
import fr.abes.thesesapirecherche.theses.model.Sujet;
import fr.abes.thesesapirecherche.theses.model.SujetsRameau;
import fr.abes.thesesapirecherche.theses.model.SujetsToMap;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonView;



@SuperBuilder
@Getter
@Setter
public class TheseEnhancedResponseDto extends TheseLiteResponseDto{
    // classe qui sera exposée pour les endpoints de recherche (theses/recherche/, theses/organismes ...)

    // hérite de TheseLiteDTO (pour conserver le même comportement) et expose en + tous les autres champs de TheseDTO. 
    // Ces champs supplémentaires seront remplis / exposés seulement lorsque le paramètre "viewFull" sera true


    // *****************
    // champs TheseLiteDTO hérités
    // *****************

    
    
    //champs TheseDTO

    @JsonView({JsonViews.Full.class})
    String numSujet;
        
    @JsonView({JsonViews.Full.class})
    Map<String, String> titres;
        
    @JsonView({JsonViews.Full.class})
    Map<String, String> resumes;
        
    @JsonView({JsonViews.Full.class})
    OrganismeResponseDto etabSoutenance;
        
    @JsonView({JsonViews.Full.class})
    String codeEtab;
        
    @JsonView({JsonViews.Full.class})
    List<OrganismeResponseDto> etabCotutelle;
        
    @JsonView({JsonViews.Full.class})
    List<OrganismeResponseDto> partenairesRecherche;
        
    @JsonView({JsonViews.Full.class})
    Map<String, List<SujetsToMap>> mapSujets;
        
    @JsonView({JsonViews.Full.class})
    List<ThesePersoneResponseDto> membresJury;
        
    @JsonView({JsonViews.Full.class})
    List<String> langues;
        
    @JsonView({JsonViews.Full.class})
    Boolean isSoutenue;
        
    @JsonView({JsonViews.Full.class})
    String accessible;
        
    @JsonView({JsonViews.Full.class})
    List<OrganismeResponseDto> ecolesDoctorales;
        
    @JsonView({JsonViews.Full.class})
    ThesePersoneResponseDto presidentJury;
        
    @JsonView({JsonViews.Full.class})
    String source;





    
   

}
