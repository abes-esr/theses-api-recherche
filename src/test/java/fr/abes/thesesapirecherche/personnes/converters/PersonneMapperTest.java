package fr.abes.thesesapirecherche.personnes.converters;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.*;
import fr.abes.thesesapirecherche.ThesesApiRechercheApplicationTests;
import fr.abes.thesesapirecherche.personnes.dto.PersonneResponseDto;
import fr.abes.thesesapirecherche.personnes.dto.PersonneLiteResponseDto;
import fr.abes.thesesapirecherche.personnes.dto.SuggestionPersonneResponseDto;
import fr.abes.thesesapirecherche.personnes.model.Personne;
import fr.abes.thesesapirecherche.personnes.model.ThesePersonne;
import fr.abes.thesesapirecherche.personnes.converters.PersonneMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.List;

/**
 * Tests du convertisseur de format pour les personnes
 */
@ExtendWith(SpringExtension.class)
public class PersonneMapperTest extends ThesesApiRechercheApplicationTests {

    PersonneMapper personneMapper = new PersonneMapper();
    /**
     * Génération d'un retour de recherche ElasticSearch contenant 2 personnes
     */
    private static final SearchResponse<Personne> searchResponse = SearchResponse.of(b -> b
            .aggregations(new HashMap<>())
            .took(0)
            .timedOut(false)
            .hits(h -> h
                    .total(t -> t.value(0).relation(TotalHitsRelation.Eq))
                    .hits(Hit.of(z -> z.index("personnes").id("127566635").source(Personne.builder().nom("Rousseau").prenom("Erwann").hasIdref(true).theses(List.of(ThesePersonne.builder().role("auteur").nnt("2007PA066375").build())).build())),
                            Hit.of(z -> z.index("personnes").id("D_Cs1oMBVl5--j0CzKFo").source(Personne.builder().nom("Rousseau").prenom("Erwan").hasIdref(false).theses(List.of(ThesePersonne.builder().role("directeur de thèse").nnt("s347820").build())).build())))
            )
            .shards(s -> s
                    .total(1)
                    .failed(0)
                    .successful(1)
            )
    );

    /**
     * Génération d'un retour de suggestion ElasticSearch contenant 2 personnes
     */
    private static final SearchResponse<Void> suggestionResponse = SearchResponse.of(b -> b
            .aggregations(new HashMap<>())
            .took(0)
            .timedOut(false)
            .hits(h -> h
                    .total(t -> t.value(0).relation(TotalHitsRelation.Eq))
                    .hits(List.of())
            )
            .shards(s -> s
                    .total(1)
                    .failed(0)
                    .successful(1)
            )
            .suggest("personne-suggestion", List.of(SuggestionBuilders.completion().text("Rouss").offset(0).length(1)
                    .options(List.of(CompletionSuggestOption.of(z -> z.index("personnes").id("098248782").text("Rousseau Erwan")),
                            CompletionSuggestOption.of(z -> z.index("personnes").id("127566635").text("Rousseau Erwann")))).build()._toSuggestion()
            ))
    );


    @Test
    @DisplayName("Conversion d'une liste de personnes au format ES vers DTO")
    void testConversionESversDto() {

        List<PersonneLiteResponseDto> results = personneMapper.personnesListToDto(searchResponse.hits().hits());

        Assertions.assertEquals(2, results.size());
        // 1ère personne
        Assertions.assertEquals("127566635", results.get(0).getId());
        Assertions.assertEquals("Rousseau", results.get(0).getNom());
        Assertions.assertEquals("Erwann", results.get(0).getPrenom());
        Assertions.assertEquals(true, results.get(0).getHasIdref());
        Assertions.assertEquals("auteur", results.get(0).getThese().getRole());
        Assertions.assertEquals("2007PA066375", results.get(0).getThese().getNnt());

        // 2ème personne
        Assertions.assertEquals("D_Cs1oMBVl5--j0CzKFo", results.get(1).getId());
        Assertions.assertEquals("Rousseau", results.get(1).getNom());
        Assertions.assertEquals("Erwan", results.get(1).getPrenom());
        Assertions.assertEquals(false, results.get(1).getHasIdref());
        Assertions.assertEquals("directeur de thèse", results.get(1).getThese().getRole());
        Assertions.assertEquals("s347820", results.get(1).getThese().getNnt());

    }

    @Test
    @DisplayName("Conversion d'une personne du format ES vers DTO")
    void testConversionPersonneESversDto() {

        PersonneResponseDto results = personneMapper.personneToDto(searchResponse.hits().hits().get(0));

        // 1ère personne
        Assertions.assertEquals("127566635", results.getId());
        Assertions.assertEquals("Rousseau", results.getNom());
        Assertions.assertEquals("Erwann", results.getPrenom());
        Assertions.assertEquals(true, results.getHasIdref());
        Assertions.assertEquals(1, results.getTheses().size());
        Assertions.assertEquals("auteur", results.getTheses().get(0).getRole());
        Assertions.assertEquals("2007PA066375", results.getTheses().get(0).getNnt());

    }

    @Test
    @DisplayName("Conversion d'une suggestion de personnes du format ES vers DTO")
    void testConversionSuggestionESversDto() {
        List<SuggestionPersonneResponseDto> results = personneMapper.suggestionListPersonneToDto(suggestionResponse.suggest());

        Assertions.assertEquals(2, results.size());
        // 1ère suggestion
        Assertions.assertEquals("098248782", results.get(0).getId());
        Assertions.assertEquals("Rousseau Erwan", results.get(0).getText());

        // 2ème suggestion
        Assertions.assertEquals("127566635", results.get(1).getId());
        Assertions.assertEquals("Rousseau Erwann", results.get(1).getText());

    }

}
