package com.mavil.reports.util;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FechasUtil {

    private static final String FORMATO_DB = "yyyy-MM-dd";
    private static final String FORMATO_PRESENTACION ="dd/MM/yyyy";

    public static String formatCadenaDb(Date fecha) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMATO_DB);
        return simpleDateFormat.format(fecha);
    }

    public static String formatCadenaDb(java.util.Date fecha) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMATO_DB);
        return simpleDateFormat.format(fecha);
    }

    public static java.util.Date parseCadena(String fechaString) throws ParseException {
        String formato = "dd/MM/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formato);
        java.util.Date fecha = simpleDateFormat.parse(fechaString);
        return fecha;
    }

    public static String formatDate(java.util.Date fecha) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMATO_PRESENTACION);
        return simpleDateFormat.format(fecha);
    }

    /**
     * Retorna un tipo LocalDate dada la fecha en el formato dd/mm/yyyy
     *
     * @param date
     * @return
     */
    public static LocalDate parse(String date) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}
