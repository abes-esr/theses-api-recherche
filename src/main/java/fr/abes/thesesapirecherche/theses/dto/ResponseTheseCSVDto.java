package fr.abes.thesesapirecherche.theses.dto;

import fr.abes.thesesapirecherche.theses.model.SujetsToMap;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ResponseTheseCSVDto {

    List<TheseResponseDto> theses = new ArrayList<>();

    public String toCSV() {
        StringBuilder s = new StringBuilder();
        //en tête
        s.append("\"NNT\";\"NumSujet\";\"Auteurs\";\"Titre FR\";\"Titre EN\";\"Directeurs\";\"Discipline\";\"Date de soutenance\";\"Date de première inscription\";\"Etablissement de soutenance\";\"Code etablissement\";\"Etablissement de cotutelle\";\"Ecoles doctorales\";\"Partenaires de recherche\";\"Président du jury\";\"Rapporteurs\";\"Examinateurs\";\"Mots clés FR\";\"Mots clés EN\";\"Langue\";\"Source\";\"Statut\";\"Accessible\"");
        s.append("\n");

        //permet de déterminer quand on est dans le 1er tour de boucle (pour ajouter les sépérateurs ;)
        Boolean firstRound = true;

        //contenu
        for(TheseResponseDto t : theses) {

            String dateSoutenance = t.getDateSoutenance() == null ? "" : t.getDateSoutenance();
            String datePremiereInscriptionDoctorat = t.getDatePremiereInscriptionDoctorat() == null ? "" : t.getDatePremiereInscriptionDoctorat();

            //NNT
            s.append("\"").append(t.getNnt() != null ? t.getNnt() : "").append("\";");

            //NumSujet
            s.append("\"").append(t.getNumSujet() != null ? t.getNumSujet() : "").append("\";");

            // Auteurs
            firstRound = true;
            s.append("\"");
            for(ThesePersoneResponseDto auteur : t.getAuteurs()) {
                if(!firstRound) s.append(" || ");
                s.append(auteur.getNom() != null ? auteur.getNom().replace("\"", "") : "").append(", ").append(auteur.getPrenom() != null ? auteur.getPrenom().replace("\"", "") : "");
                if(auteur.getPpn() != null) {
                    s.append(" (").append(auteur.getPpn()).append(")");
                }
                firstRound = false;
            }
            s.append("\";");

            //Titre FR
            s.append("\"").append(t.getTitrePrincipal().replace("\"", "")).append("\";");

            //Titre EN
            s.append("\"");
            s.append(t.getTitres().containsKey("en") ? t.getTitres().get("en").replace("\"", "") : "");
            s.append("\";");

            //Directeurs
            firstRound = true;
            s.append("\"");
            for(ThesePersoneResponseDto directeur : t.getDirecteurs()) {
                if(!firstRound) s.append(" || ");
                s.append(directeur.getNom() != null ? directeur.getNom().replace("\"", "") : "").append(", ").append(directeur.getPrenom() != null ? directeur.getPrenom().replace("\"", "") : "");
                if(directeur.getPpn() != null) {
                    s.append(" (").append(directeur.getPpn()).append(")");
                }
                firstRound = false;
            }
            s.append("\";");

            //Discipline
            s.append("\"").append(t.getDiscipline().replace("\"", "")).append("\";");

            //Date de soutenance
            s.append("\"").append(dateSoutenance).append("\";");

            //Date d'inscription
            s.append("\"").append(datePremiereInscriptionDoctorat).append("\";");


            //Etablissement soutenance
            s.append("\"").append(t.getEtabSoutenance().getNom().replace("\"", "")).append(t.getEtabSoutenance().getPpn() != null ? " (" + t.getEtabSoutenance().getPpn() + ")" : "").append("\";");

            //Code Etab
            s.append("\"").append(t.getCodeEtab() != null ? t.getCodeEtab().replace("\"", "") : "").append("\";");

            //Etablissements de cotutelle
            firstRound = true;
            s.append("\"");
            for(OrganismeResponseDto cotutelle : t.getEtabCotutelle()) {
                if(!firstRound) s.append(" || ");
                s.append(cotutelle.getNom().replace("\"", "").replace("||", ""));
                if(cotutelle.getPpn() != null) {
                    s.append(" (").append(cotutelle.getPpn()).append(")");
                }
                firstRound = false;
            }
            s.append("\";");

            //Ecoles doctorale
            firstRound = true;
            s.append("\"");
            for(OrganismeResponseDto doctorale : t.getEcolesDoctorales()) {
                if(!firstRound) s.append(" || ");
                s.append(doctorale.getNom().replace("\"", "").replace("||", " "));
                if(doctorale.getPpn() != null) {
                    s.append(" (").append(doctorale.getPpn()).append(")");
                }
                firstRound = false;
            }
            s.append("\";");


            //Partenaires de recherche
            firstRound = true;
            s.append("\"");
            for(OrganismeResponseDto partenaire : t.getPartenairesRecherche()) {
                if(!firstRound) s.append(", ");
                s.append(partenaire.getNom().replace("\"", ""));
                if(partenaire.getPpn() != null) {
                    s.append(" (").append(partenaire.getPpn()).append(")");
                }
                firstRound = false;
            }
            s.append("\";");


            //Président du jury
            s.append("\"").append(t.getPresidentJury().getPrenom()  == null ? "" : t.getPresidentJury().getNom().replace("\"", "")).append(", ").append(t.getPresidentJury().getPrenom()   == null ? "" : t.getPresidentJury().getNom().replace("\"", "")).append("\";");

            //Rapporteurs
            firstRound = true;
            s.append("\"");
            for(ThesePersoneResponseDto rapp : t.getRapporteurs()) {
                if(!firstRound) s.append(" || ");
                s.append(rapp.getNom() != null ? rapp.getNom().replace("\"", "") : "").append(", ").append(rapp.getPrenom() != null ? rapp.getPrenom().replace("\"", "") : "");
                if(rapp.getPpn() != null) {
                    s.append(" (").append(rapp.getPpn()).append(")");
                }
                firstRound = false;
            }
            s.append("\";");

            //Membres du jury
            firstRound = true;
            s.append("\"");
            for(ThesePersoneResponseDto membre : t.getMembresJury()) {
                if(!firstRound) s.append(" || ");
                s.append(membre.getNom() != null ? membre.getNom().replace("\"", "") : "").append(", ").append(membre.getPrenom() != null ? membre.getPrenom().replace("\"", "") : "");
                if(membre.getPpn() != null) {
                    s.append(" (").append(membre.getPpn()).append(")");
                }
                firstRound = false;
            }
            s.append("\";");

            //Mots clés FR
            firstRound = true;
            s.append("\"");
            if(t.getMapSujets().containsKey("fr")){
            for(SujetsToMap map : t.getMapSujets().get("fr")) {
                if(!firstRound) s.append(" || ");
                s.append(map.getKeyword().replace("\"", "").replace("||", " "));
                firstRound = false;
            }}
            s.append("\";");

            //Mots clés EN
            firstRound = true;
            s.append("\"");
            if(t.getMapSujets().containsKey("en")){
                for(SujetsToMap map : t.getMapSujets().get("en")) {
                    if(!firstRound) s.append(" || ");
                    s.append(map.getKeyword().replace("\"", "").replace("||", " "));
                    firstRound = false;
                }}
            s.append("\";");

            //Langue
            firstRound = true;
            s.append("\"");
            for(String l : t.getLangues()) {
                if(!firstRound) s.append(" || ");
                s.append(l);
                firstRound = false;
            }
            s.append("\";");

            //Source
            s.append("\"").append(t.getSource()).append("\";");

            //Statut
            s.append("\"").append(t.getStatus().equals("soutenue") ? "soutenue" : "en cours").append("\";");

            //Accessible
            s.append("\"").append(t.getAccessible()).append("\"");

            s.append("\n");
        }

        return s.toString();
    }
}
