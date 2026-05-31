package utils;

public class Utils {
    private static long CONSECUTIVO = 1;
    public static final double VALOR_MINUTO = 300;

    public static long getCONSECUTIVO() {
        return CONSECUTIVO;
    }

    public static void setCONSECUTIVO(long CONSECUTIVO) {
        Utils.CONSECUTIVO = CONSECUTIVO;
    }
}
