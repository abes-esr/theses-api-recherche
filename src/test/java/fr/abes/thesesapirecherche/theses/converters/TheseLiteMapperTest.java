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
    PersonneThese pers3 = new PersonneThese();
    SearchResponse<These> searchResponse;

    @BeforeEach
    public void init() {

        // these 1
        these1.setTitres(new HashMap<>() {{
            put("fr", "Avancées en chimie de coordination des phosphines nitroaromatiques : synthèse et chimie supramoléculaire de dérivés alkyl - aryl-carbo-benzènes");
            put("en", "Advances in the coordination chemistry of nitroaromatic phosphines : synthesis and supramolecular chemistry of alkyl/aryl carbo-benzene derivatives");
        }});
        pers1.setNom("Zhu");
        pers1.setPrenom("Chongwei");
        pers1.setPpn("232771227");
        pers2.setPpn("196425964");
        pers2.setPrenom("Valérie");
        pers2.setNom("Maraval");
        pers3.setPpn("07227817X");
        pers3.setNom("Chauvin");
        pers3.setPrenom("Rémi");
        List<PersonneThese> auteurs = new ArrayList<>();
        List<PersonneThese> directeurs = new ArrayList<>();
        auteurs.add(pers1);
        directeurs.add(pers2);
        directeurs.add(pers3);

        these1.setAuteurs(auteurs);
        these1.setDirecteurs(directeurs);
        these1.setNnt("2017TOU30281");
        these1.setCodeEtab("TOU3");
        these1.setDiscipline("Chimie organométallique de coordination");

        // these 2
        these2.setTitres(new HashMap<>() {{
            put("fr", "Propriétés structurales et de transport de pérovskites doubles Sr2FeMoO6 élaborées par chimie douce");
            put("en", "Structural and transport properties of Sr2FeMoO6 double perovskites prepared by chimie douce");
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

        Assertions.assertEquals("127566635", thesesLite.getId());
        Assertions.assertEquals("Zhu",
                thesesLite.getAuteurs().get(0).getNom());
        Assertions.assertEquals("Maraval",
                thesesLite.getDirecteurs().get(0).getNom());
        Assertions.assertEquals("Chauvin",
                thesesLite.getDirecteurs().get(1).getNom());
        Assertions.assertEquals("2017TOU30281",
                thesesLite.getNnt());
        Assertions.assertEquals("Chimie organométallique de coordination",
                thesesLite.getDiscipline());
    }
}
