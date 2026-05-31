package model;

import java.time.LocalDate;

public class Postpago extends Cuenta {

    private long cargoFijo;

    public Postpago(long id, String numeroTelefono) {
        super(id, numeroTelefono);
        this.cargoFijo = 20000;
    }

    public long getCargoFijo() {
        return cargoFijo;
    }

    public void setCargoFijo(long cargoFijo) {
        this.cargoFijo = cargoFijo;
    }

    public long calcularValorLlamadasInternacionalesMes(int anio, int mes) {
        long total = 0;

        for (Llamada llamada : getLlamadas()) {
            LocalDate fecha = llamada.getFecha();

            if (fecha.getYear() == anio && fecha.getMonthValue() == mes) {
                if (llamada instanceof LlamadaInternacional) {
                    total = total + llamada.getValor();
                }
            }
        }

        return total;
    }

    @Override
    public long obtenerPagoCuenta(int anio, int mes) {
        long totalInternacionales = calcularValorLlamadasInternacionalesMes(anio, mes);
        long totalPagar = cargoFijo + totalInternacionales;

        return totalPagar;
    }

    @Override
    public String toString() {
        return "Postpago{" +
                "id=" + getId() +
                ", numeroTelefono='" + getNumeroTelefono() + '\'' +
                ", cargoFijo=" + cargoFijo +
                '}';
    }
}