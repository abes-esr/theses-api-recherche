package fr.abes.thesesapirecherche.theses.repository;

import org.springframework.stereotype.Component;


@Component
final class QueryStringNormalizer {

    static String normalize(String input) {
        String s = input.trim();
        s = replaceAndOutsideQuotes(s);    // your existing method, unchanged
        s = replaceSpacesOutsideQuotes(s);
        return cleanDoi(s);
    }

    /**
     * Supprime "https://doi.org/" ou "doi.org/" de la query pour pouvoir matcher avec
     * le champ doi qui ne contient que le doi simple
     * @param input
     * @return
     */
    public static String cleanDoi(String input) {
        if (input == null) {
            return null;
        }
        return input.replace("https\\:\\/\\/doi.org\\/", "")
                .replace("http\\:\\/\\/doi.org\\/", "")
                .replace("doi.org\\/", "");
    }

    /**
     * Requis: input n'as pas d'espace au début et à la fin
     * @param input
     * @return
     */
    @Deprecated
    // TODO extraire ça
    // TODO test unitaire
    public static String replaceAndOutsideQuotes(String input) {
        StringBuilder result = new StringBuilder();
        boolean insideQuotes = false;
        int i = 0;

        while (i < input.length()) {
            char currentChar = input.charAt(i);

            if (currentChar == '\"') {
                insideQuotes = !insideQuotes;
                result.append(currentChar);
                i++;
            } else if (!insideQuotes && i + 2 < input.length() && input.startsWith(" AND ", i)) {
                result.append(" ");
                i += 5;  // Skip past the "AND"
            } else {
                result.append(currentChar);
                i++;
            }
        }

        return result.toString();
    }

    @Deprecated
    /* Corriger cette fonction pcq y a beaucoup de bug dedans */
    private static String replaceSpacesOutsideQuotes(String input) {
        StringBuilder result = new StringBuilder();
        boolean insideQuotes = false;
        boolean insideBrackets = false;


        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if (c == '"') {
                insideQuotes = !insideQuotes;
                result.append(c);
            } else if (c == '[') {
                insideBrackets = true;
                result.append(c);
            } else if (c == ']') {
                insideBrackets = false;
                result.append(c);
            }
            else if (c == ' ') {
                if (isKeywordAt(input, i + 1, "OR")) {
                    result.append(" OR ");
                    i += 3;
                } else if (isKeywordAt(input, i + 1, "NOT")) {
                    result.append(" NOT ");
                    i += 4;
                } else if (!insideQuotes && !insideBrackets) {
                    result.append(" AND ");
                } else {
                    result.append(c);
                }
            } else {
                result.append(c);

            }

        }

        return result.toString();
    }

    private static boolean isKeywordAt(String s, int from, String keyword) {
        int end = from + keyword.length();
        return s.startsWith(keyword, from) && (end == s.length() || s.charAt(end) == ' ');
    }
}