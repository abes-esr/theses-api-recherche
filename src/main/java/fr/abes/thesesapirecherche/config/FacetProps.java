package fr.abes.thesesapirecherche.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@ConfigurationProperties(prefix = "facets")
@Getter
@Setter
public class FacetProps {
    private List<MainFacet> mainTheses;
    private List<SubFacet> subsTheses;

    private List<MainFacet> mainPersonnes;
    private List<SubFacet> subsPersonnes;

    @Setter
    @Getter
    public static class MainFacet {
        private String libelle;
        private String champ;
        private boolean searchBar;
    }

    @Getter
    @Setter
    public static class SubFacet {

        private String libelle;
        private String champ;
        private String parentName;

    }
}