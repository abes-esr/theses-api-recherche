package fr.abes.thesesapirecherche.personnes.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import fr.abes.thesesapirecherche.personnes.builder.SearchPersonneQueryBuilder;
import fr.abes.thesesapirecherche.personnes.converters.BatchTheseMapper;
import fr.abes.thesesapirecherche.personnes.converters.ClientTheseMapper;
import fr.abes.thesesapirecherche.personnes.converters.TheseMapper;
import fr.abes.thesesapirecherche.personnes.dto.PersonneComputedFields;
import fr.abes.thesesapirecherche.personnes.dto.PersonneResponseDto;
import fr.abes.thesesapirecherche.personnes.dto.batch.BatchPersonneResponseDto;
import fr.abes.thesesapirecherche.personnes.dto.batch.BatchTheseResponseDto;
import fr.abes.thesesapirecherche.personnes.dto.client.ClientPersonneResponseDto;
import fr.abes.thesesapirecherche.personnes.model.Personne;
import fr.abes.thesesapirecherche.personnes.model.ThesePersonne;
import fr.abes.thesesapirecherche.theses.model.These;

@Service
public class PersonnesService {
    
    @Autowired
    private SearchPersonneQueryBuilder searchPersonneQueryBuilder;

    @Autowired
    private TheseMapper theseMapper;

    @Autowired
    private ClientTheseMapper clientTheseMapper;

    @Autowired
    private BatchTheseMapper batchClientTheseMapper;
    
    


    // récupère une personne d'après son id
    public PersonneResponseDto getPersonne(String id) throws Exception{
        PersonneResponseDto res = PersonneResponseDto.builder().build();


        // on récupère la personne via ElasticSearch
        SearchResponse<Personne> response = searchPersonneQueryBuilder.getPersonne(id);

        // on fait le mapping ici TODO : refactorer propre en utilisant le mapper personne
        Personne p = response.hits().hits().get(0).source();
        
        res.setId(response.hits().hits().get(0).id());
        res.setNom(p.getNom());
        res.setPrenom(p.getPrenom());
        res.setHasIdref(p.getHasIdref());

        res.setRoles(PersonneComputedFields.calculerStatistiquesRoles(p.getRoles()));
        res.setMotsCles(PersonneComputedFields.calculerMotsCles(p.getTheses()));


        //mapping de ThesePersonne (model) vers TheseDto
        res.setTheses(theseMapper.thesesToDto(p.getTheses()));

        return res;
    }




    // récupère une personne d'après son id
    public ClientPersonneResponseDto getPersonneForClient(String id) throws Exception{
        ClientPersonneResponseDto res = ClientPersonneResponseDto.builder().build();


        // on récupère la personne via ElasticSearch
        SearchResponse<Personne> response = searchPersonneQueryBuilder.getPersonne(id);

        // on fait le mapping ici
        Personne p = response.hits().hits().get(0).source();
        res.setId(response.hits().hits().get(0).id());
        res.setNom(p.getNom());
        res.setPrenom(p.getPrenom());
        res.setHasIdref(p.getHasIdref());

        res.setRoles(PersonneComputedFields.calculerStatistiquesRoles(p.getRoles()));
        res.setMotsCles(PersonneComputedFields.calculerMotsCles(p.getTheses()));



        // on récupère d'abord les ids des thèses (pour plus tard aller requêter sur l'index theses)
        List<String> theseIds = p.getTheses().stream()
                                                .map(ThesePersonne::getId)
                                                .toList();

        // récupération des thèses complètes
        SearchResponse<ThesePersonne> thesesHit = searchPersonneQueryBuilder.getThesesByIds(theseIds);


        // récupération des objets ThesePersonne
        List<ThesePersonne> theses = thesesHit.hits().hits().stream()
            .map(Hit::source)
            .toList();


        res.setTheses(clientTheseMapper.thesesToClientDto(theses));

        return res;
    }



    
    // récupère une personne d'après son id
    public BatchPersonneResponseDto getPersonneForBatch(String id) throws Exception{
        BatchPersonneResponseDto res = BatchPersonneResponseDto.builder().build();


        // on récupère la personne via ElasticSearch
        SearchResponse<Personne> response = searchPersonneQueryBuilder.getPersonne(id);

        // on fait le mapping ici
        Personne p = response.hits().hits().get(0).source();
        res.setId(response.hits().hits().get(0).id());
        res.setNom(p.getNom());
        res.setPrenom(p.getPrenom());
        res.setHasIdref(p.getHasIdref());

        res.setRoles(PersonneComputedFields.calculerStatistiquesRoles(p.getRoles()));
        res.setMotsCles(PersonneComputedFields.calculerMotsCles(p.getTheses()));



        // on récupère d'abord les ids des thèses (pour plus tard aller requêter sur l'index theses)
        List<String> theseIds = p.getTheses().stream()
                                                .map(ThesePersonne::getId)
                                                .toList();

        // récupération des thèses complètes
        SearchResponse<ThesePersonne> thesesHit = searchPersonneQueryBuilder.getThesesByIds(theseIds);


        // récupération des objets ThesePersonne
        List<ThesePersonne> theses = thesesHit.hits().hits().stream()
            .map(Hit::source)
            .toList();


        res.setTheses(batchClientTheseMapper.thesesToBatchDto(theses));

        return res;
    }




}
