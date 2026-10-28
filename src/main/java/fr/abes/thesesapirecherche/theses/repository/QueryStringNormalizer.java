package fr.abes.thesesapirecherche.theses.repository;

import static fr.abes.thesesapirecherche.theses.builder.SearchQueryBuilder.cleanDoi;
import static fr.abes.thesesapirecherche.theses.builder.SearchQueryBuilder.replaceAndOutsideQuotes;

final class QueryStringNormalizer {
//    private QueryStringNormalizer() {}

    static String normalize(String input) {
        String s = input.trim();
        s = replaceAndOutsideQuotes(s);    // your existing method, unchanged
        s = replaceSpacesOutsideQuotes(s);
        return cleanDoi(s);
    }

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