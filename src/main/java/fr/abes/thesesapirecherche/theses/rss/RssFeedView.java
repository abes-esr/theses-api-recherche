package fr.abes.thesesapirecherche.theses.rss;

import com.rometools.rome.feed.module.DCModuleImpl;
import com.rometools.rome.feed.module.Module;
import com.rometools.rome.feed.rss.Channel;
import com.rometools.rome.feed.rss.Description;
import com.rometools.rome.feed.rss.Guid;
import com.rometools.rome.feed.rss.Item;
import fr.abes.thesesapirecherche.theses.builder.SearchQueryBuilder;
import fr.abes.thesesapirecherche.theses.dto.ResponseTheseLiteDto;
import fr.abes.thesesapirecherche.theses.dto.TheseLiteResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.jdom2.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.feed.AbstractRssFeedView;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
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


        // Ajout du self link, recommandé par W3C
        org.jdom2.Element atomLink = new Element("link", org.jdom2.Namespace.getNamespace("atom", "http://www.w3.org/2005/Atom"));
        atomLink.setAttribute("href", (String.format("%s%s", request.getRequestURL(), (request.getQueryString() != null ? "?" + request.getQueryString() : ""))));
        atomLink.setAttribute("rel", "self");
        atomLink.setAttribute("type", "application/rss+xml");
        feed.getForeignMarkup().add(atomLink);

    }

    @Override
    protected List<Item> buildFeedItems(Map<String, Object> model,
                                        HttpServletRequest request, HttpServletResponse response) throws Exception {
        // Récupération des paramètres
        Optional<String> q = Optional.ofNullable(URLDecoder.decode(request.getParameter("q") != null ? request.getParameter("q") : "", StandardCharsets.UTF_8));
        Optional<String> filtres = Optional.ofNullable(URLDecoder.decode(request.getParameter("filtres") != null ? request.getParameter("filtres") : "", StandardCharsets.UTF_8));

        ResponseTheseLiteDto ESresponse = searchQueryBuilder.simple(q.orElse("*"), 0, 30, "dateDesc", filtres.orElse(""));

        List<Item> rssItems = new ArrayList<>();

        for(TheseLiteResponseDto these : ESresponse.getTheses()) {
            Item i = new Item();

            // Balise DublinCore pour auteur (car balise author = uniquement des adresses mails)
            List<Module> modules = new ArrayList<>();
            DCModuleImpl dcModule = new DCModuleImpl();
            dcModule.setCreator(these.getAuteurs().get(0).getPrenom() + " " + these.getAuteurs().get(0).getNom());
            modules.add(dcModule);
            i.setModules(modules);

            //Link / GUID (contiennent le même lien)
            i.setTitle(these.getTitrePrincipal());
            i.setLink("https://theses.fr/" + these.getId());
            Guid g = new Guid();
            g.setPermaLink(true);
            g.setValue("https://theses.fr/" + these.getId());
            i.setGuid(g);

            Description desc = new Description();
            desc.setValue((these.getStatus().equals("soutenue") ? "Thèse soutenue" : "Thèse en cours") + " - " + these.getDiscipline());
            i.setDescription(desc);


            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            try {
                Date date = null;
                // Conversion de la chaîne en objet Date
                if(these.getStatus().equals("soutenue")) date = dateFormat.parse(these.getDateSoutenance() != null ? these.getDateSoutenance() : null);
                else date = dateFormat.parse(these.getDatePremiereInscriptionDoctorat() != null ? these.getDatePremiereInscriptionDoctorat() : null);
                i.setPubDate(date);
            } catch (ParseException e) {
                System.out.println("Erreur de date flux rss");
            }

            rssItems.add(i);
        }

        return rssItems;
    }
}
