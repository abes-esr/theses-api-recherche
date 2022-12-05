package fr.abes.thesesapirecherche.theses.converters;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.TotalHitsRelation;
import fr.abes.thesesapirecherche.theses.dto.TheseLiteResponseDto;
import fr.abes.thesesapirecherche.theses.model.PersonneThese;
import fr.abes.thesesapirecherche.theses.model.These;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TheseLiteMapperTest {

    TheseLiteMapper theseLiteMapper = new TheseLiteMapper();
    These these1 = new These();
    These these2 = new These();
    PersonneThese pers1 = new PersonneThese();
    PersonneThese pers2 = new PersonneThese();
    SearchResponse<These> searchResponse;

    @BeforeEach
    public void init() {

        // these 1
        these1.setTitres(new HashMap<>() {{
            put("fr", "titre en fr");
            put("en", "title in en");
        }});
        pers1.setNom("bukowski");
        pers1.setPrenom("charles");
        pers1.setPpn("111");
        pers2.setNom("dumas");
        pers2.setPrenom("alex");
        pers2.setPpn("222");
        List<PersonneThese> auteurs = new ArrayList<>();
        auteurs.add(pers1);
        auteurs.add(pers2);
        these1.setAuteurs(auteurs);

        // these 2
        these2.setTitres(new HashMap<>() {{
            put("fr", "le titre 2");
            put("en", "title 2");
        }});

        searchResponse = SearchResponse.of(b -> b
                .aggregations(new HashMap<>())
                .took(0)
                .timedOut(false)
                .hits(h -> h
                        .total(t -> t.value(0).relation(TotalHitsRelation.Eq))
                        .hits(
                                Hit.of(z -> z.index("theses-sample-2").id("127566635")
                                                .source(these1)),
                                        Hit.of(z -> z.index("theses-sample-2").id("127566636")
                                                .source(these2))
                        ))
                        .shards(s -> s
                                .total(1)
                                .failed(0)
                                .successful(1)
                        )
                );
    }


    @Test
    @DisplayName("Conversion d'une thèse lite du format ES vers DTO")
    void testConversionTheseLiteESversDto() {

        TheseLiteResponseDto thesesLite = theseLiteMapper.theseLiteToDto(searchResponse.hits().hits().get(0));

        Assertions.assertEquals("titre en fr", thesesLite.getTitres().get("fr"));
        Assertions.assertEquals("title in en", thesesLite.getTitres().get("en"));

    }
}
