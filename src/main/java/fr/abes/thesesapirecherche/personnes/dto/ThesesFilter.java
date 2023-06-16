package fr.abes.thesesapirecherche.personnes.dto;

/**
 * Cette classe permet d'afficher ou pas toutes les thèses en fonction du profil d'exécution
 * En mode 'test', on affiche toutes les thèses car les tests d'intégrations vérifient la présence des thèses
 * En mode 'normal', on n'affiche pas les toutes les thèses pour alléger le JSON renvoyé par l'API
 */
public class ThesesFilter {

    public static boolean isJUnitTest() {
        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
            if (element.getClassName().startsWith("org.junit.")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object other) {
        return !isJUnitTest();
    }
}
