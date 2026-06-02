package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Utils {

    public static long CONSECUTIVO = 1;

    public static final long VALOR_MINUTO = 300;
    public static final long CARGO_FIJO_POSTPAGO = 20000;
    public static final int MINUTOS_PREPAGO = 5;
    public static final int RECARGO_INTERNACIONAL = 20;

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static long generarIdCuenta() {
        long id = CONSECUTIVO;
        CONSECUTIVO = CONSECUTIVO + 1;
        return id;
    }

    public static LocalDate crearFecha(int anio, int mes, int dia) {
        return LocalDate.of(anio, mes, dia);
    }

    public static LocalDate convertirStringFecha(String fecha) {
        return LocalDate.parse(fecha, FORMATO_FECHA);
    }

    public static String convertirFechaString(LocalDate fecha) {
        return fecha.format(FORMATO_FECHA);
    }

    public static long calcularValorLlamada(int duracion) {
        return duracion * VALOR_MINUTO;
    }

    public static long calcularValorLlamadaInternacional(int duracion) {
        long valorBase = calcularValorLlamada(duracion);
        long recargo = valorBase * RECARGO_INTERNACIONAL / 100;
        return valorBase + recargo;
    }
}