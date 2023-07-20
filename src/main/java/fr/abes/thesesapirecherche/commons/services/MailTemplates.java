package fr.abes.thesesapirecherche.commons.services;

public class MailTemplates {

    public static String mailSignalerErreur(String domaine, String url, String nom, String prenom, String mail, String objet, String question, String appSource) {
        return "Bonjour, <br /><br />Une erreur du domaine <i>"
                +
                domaine
                + "</i> a été signalée sur la page <a target='_blank' href='" + url + "'>" + url + "</a>"
                +
                """
                        <br /><br />
                        Personne à l'origine du signalement de l'erreur : <br />
                        Nom :""" + " " + nom + "<br /> Prenom : " + prenom + "<br /> Courriel : " + mail + """
                <br /><br />
                Détail de l'erreur : <br />
                Objet :""" + " " + objet + "<br /> Question : <br />"
                + question.replaceAll("\n", "<br />") +
                "<br /><br />Nous vous remercions d'intervenir dans un délai raisonnable dans " + appSource.toUpperCase() + " " +
                """
                        pour remédier au problème et de le faire savoir à la personne à l'origine du signalement de l'erreur, en copie de ce message.
                        Si ce problème n'est pas de votre ressort, utilisez le guichet d'assistance <a href='https://stp.abes.fr/node/3?origine=thesesFr'>ABESstp</a> pour le signaler à l'ABES.
                        <br /><br />
                        --<br />
                        Cordialement,<br />
                        l'équipe de theses.fr
                        <br /><br />
                        <i>Ce message vous a été envoyé automatiquement. Veuillez ne pas y répondre.</i>
                         """;
    }

}
