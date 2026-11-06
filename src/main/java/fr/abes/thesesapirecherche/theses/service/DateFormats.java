package fr.abes.thesesapirecherche.theses.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.function.Supplier;

public class DateFormats {
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    public static String formatDate(Supplier<Date> func) {
        Date d = func.get();
        return d == null ? null : DATE_FORMAT.format(d);
    }
}
