package fr.abes.thesesapirecherche.theses.model;

import org.springframework.data.elasticsearch.core.mapping.PropertyValueConverter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class CustomZonedDateTimeConverter implements PropertyValueConverter {

    private final DateTimeFormatter formatterWithZone =
            DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ssX");
    private final DateTimeFormatter formatterWithoutZone =
            DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss");

    @Override
    public Object write(Object value) {
        // When writing to Elasticsearch, always use the format with the zone (Z)
        if (value instanceof ZonedDateTime zonedDateTime) {
            return formatterWithZone.format(zonedDateTime);
        }
        return value;
    }

    @Override
    public Object read(Object value) {
        if (value instanceof String s) {
            try {
                // First, try to parse assuming a timezone is present (e.g., ...Z)
                return formatterWithZone.parse(s, ZonedDateTime::from);
            } catch (Exception e) {
                // If that fails, parse without a timezone and assume UTC
                return formatterWithoutZone.parse(s, LocalDateTime::from)
                        .atZone(ZoneId.of("UTC"));
            }
        }
        return value;
    }
}
