package fr.abes.thesesapirecherche.theses.model;

import org.springframework.data.elasticsearch.core.mapping.PropertyValueConverter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.TemporalAccessor;

public class CustomZonedDateTimeConverter implements PropertyValueConverter {

    // Parsing: offset is optional, accepts "Z", "+02", "+0200" and "+02:00"
    private static final DateTimeFormatter PARSER = new DateTimeFormatterBuilder()
            .appendPattern("uuuu-MM-dd'T'HH:mm:ss")
            .optionalStart().appendOffset("+HH:MM:ss", "Z").optionalEnd()
            .toFormatter();

    // Writing: always "+02:00" or "Z"
    private static final DateTimeFormatter WRITER =
            DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ssXXX");

    @Override
    public Object write(Object value) {
        if (value instanceof ZonedDateTime zdt) {
            return WRITER.format(zdt);
        }
        return value;
    }

    @Override
    public Object read(Object value) {
        if (value instanceof String s) {
            TemporalAccessor parsed = PARSER.parseBest(s, ZonedDateTime::from, LocalDateTime::from);
            if (parsed instanceof ZonedDateTime zdt) {
                return zdt;
            }
            return ((LocalDateTime) parsed).atZone(ZoneOffset.UTC); // no offset: assume UTC
        }
        return value;
    }
}
