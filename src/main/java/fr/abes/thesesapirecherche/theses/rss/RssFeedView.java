package fr.abes.thesesapirecherche.theses.rss;

import com.rometools.rome.feed.rss.Channel;
import com.rometools.rome.feed.rss.Description;
import com.rometools.rome.feed.rss.Item;
import fr.abes.thesesapirecherche.theses.builder.SearchQueryBuilder;
import fr.abes.thesesapirecherche.theses.dto.ResponseTheseLiteDto;
import fr.abes.thesesapirecherche.theses.dto.TheseLiteResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.feed.AbstractRssFeedView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class RssFeedView extends AbstractRssFeedView {

    @Autowired
    SearchQueryBuilder searchQueryBuilder;

    @SneakyThrows
    @Override
    protected void buildFeedMetadata(Map<String, Object> model,
                                     Channel feed, HttpServletRequest request) {
        // Récupération des paramètres
        Optional<String> q = Optional.ofNullable(request.getParameter("q"));

        feed.setTitle("Flux RSS Theses.fr");
        feed.setDescription("Dernières thèses pour : " + q.orElse("toutes les thèses"));
        feed.setLink("https://theses.fr/");
    }

    @Override
    protected List<Item> buildFeedItems(Map<String, Object> model,
                                        HttpServletRequest request, HttpServletResponse response) throws Exception {
        // Récupération des paramètres
        Optional<String> q = Optional.ofNullable(request.getParameter("q"));
        Optional<String> filtres = Optional.ofNullable(request.getParameter("filtres"));

        ResponseTheseLiteDto ESresponse = searchQueryBuilder.simple(q.orElse("*"), 0, 30, "dateDesc", filtres.orElse(""));

        List<Item> rssItems = new ArrayList<>();

        for(TheseLiteResponseDto these : ESresponse.getTheses()) {
            Item i = new Item();
            i.setAuthor(these.getAuteurs().get(0).getPrenom() + " " + these.getAuteurs().get(0).getNom());
            i.setTitle(these.getTitrePrincipal());
            i.setLink("https://theses.fr/" + these.getId());

            Description desc = new Description();
            desc.setValue((these.getStatus().equals("soutenue") ? "Thèse soutenue" : "Thèse en cours") + " - " + these.getDiscipline());
            i.setDescription(desc);


            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            try {
                // Conversion de la chaîne en objet Date
                Date date = dateFormat.parse(these.getDateSoutenance() != null ? these.getDateSoutenance() : these.getDatePremiereInscriptionDoctorat());

                i.setPubDate(date);
            } catch (ParseException e) {
                System.out.println("Erreur de date flux rss");
            }

            rssItems.add(i);
        }

        return rssItems;
    }
}
