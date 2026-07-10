package fr.abes.thesesapirecherche.theses.converters;

import fr.abes.thesesapirecherche.theses.dto.TheseEnhancedResponseDto;
import fr.abes.thesesapirecherche.theses.model.Sujet;
import fr.abes.thesesapirecherche.theses.model.SujetsRameau;
import fr.abes.thesesapirecherche.theses.model.SujetsToMap;
import fr.abes.thesesapirecherche.theses.model.These;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import co.elastic.clients.elasticsearch.core.search.Hit;


@Component
public class TheseEnhancedMapper extends TheseLiteMapper {

    //pour transformer un objet model "These" en objet DTO "TheseFullView" 
    //ce DTO servira pour fournir soit une réponse "Lite" / "Detailed" / "Full" selon le paramètre 

   
    public TheseEnhancedResponseDto theseToEnhancedDto(Hit<These> theseHit) {
        These these = theseHit.source();

        // on créé le dto "enhanced". On le remplit d'abord avec les champs qui ne sont pas présents dans le dto Lite
        TheseEnhancedResponseDto dto = TheseEnhancedResponseDto.builder()
        .numSujet(these.getNumSujet())
        .titres(these.getTitres())
        .resumes(these.getResumes())
        .etabSoutenanceN(these.getEtabSoutenanceN())
        .codeEtab(these.getCodeEtab())
        .etabCotutelle(TheseMappingHelper.organismesToDto(these.getEtabsCotutelle()))
        .partenairesRecherche(TheseMappingHelper.organismesToDto(these.getPartenairesRecherche()))
        .membresJury(TheseMappingHelper.personnesToDto(these.getMembresJury()))
        .langues(these.getLangues())
        .isSoutenue(these.getIsSoutenue())
        .accessible(these.getAccessible())
        .ecolesDoctorales(TheseMappingHelper.organismesToDto(these.getEcolesDoctorales()))
        .presidentJury(TheseMappingHelper.personneToDto(these.getPresidentJury()))
        .source(these.getSource())
        .build();


        // On remplit ensuite les champs qui viennent du Lite
        fillLiteFields(theseHit, dto);

        return dto;
    }

    public TheseEnhancedResponseDto theseToLitedDto(Hit<These> theseHit) {
        
        TheseEnhancedResponseDto dto = TheseEnhancedResponseDto.builder().build();
       
        // On remplit seulement les champs qui viennent du Lite
        fillLiteFields(theseHit, dto);

        return dto;
    }
}