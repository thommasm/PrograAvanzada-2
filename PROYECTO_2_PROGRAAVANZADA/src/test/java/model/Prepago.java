package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Prepago extends Cuenta implements Serializable {

    private int numeroMinutos;
    private ArrayList<Recarga> recargas;

    public Prepago(long id, String numeroTelefono) {
        super(id, numeroTelefono);
        this.numeroMinutos = 5;
        this.recargas = new ArrayList<>();
    }

    public int getNumeroMinutos() {
        return numeroMinutos;
    }

    public void setNumeroMinutos(int numeroMinutos) {
        this.numeroMinutos = numeroMinutos;
    }

    public ArrayList<Recarga> getRecargas() {
        return recargas;
    }

    public void setRecargas(ArrayList<Recarga> recargas) {
        this.recargas = recargas;
    }

    public void agregarRecarga(Recarga recarga) {
        this.recargas.add(recarga);
    }

    public ArrayList<Recarga> obtenerRecargasMes(int anio, int mes) {
        ArrayList<Recarga> recargasMes = new ArrayList<>();

        for (Recarga recarga : recargas) {
            LocalDate fecha = recarga.getFecha();

            if (fecha.getYear() == anio && fecha.getMonthValue() == mes) {
                recargasMes.add(recarga);
            }
        }

        return recargasMes;
    }

    public long calcularTotalRecargasMes(int anio, int mes) {
        long total = 0;

        for (Recarga recarga : recargas) {
            LocalDate fecha = recarga.getFecha();

            if (fecha.getYear() == anio && fecha.getMonthValue() == mes) {
                total = total + recarga.getValor();
            }
        }

        return total;
    }

    public boolean tieneSaldoDisponible(int anio, int mes, long valorNuevaLlamada) {
        long totalRecargas = calcularTotalRecargasMes(anio, mes);
        long totalLlamadas = calcularValorLlamadasMes(anio, mes);
        long saldoDisponible = totalRecargas - totalLlamadas;

        if (saldoDisponible >= valorNuevaLlamada) {
            return true;
        } else {
            return false;
        }
    }

    public boolean tieneMinutosDisponibles(int anio, int mes, int duracionNuevaLlamada) {
        int minutosUsados = calcularDuracionLlamadasMes(anio, mes);
        int minutosDisponibles = numeroMinutos - minutosUsados;

        if (minutosDisponibles >= duracionNuevaLlamada) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public long obtenerPagoCuenta(int anio, int mes) {
        return calcularTotalRecargasMes(anio, mes);
    }

    @Override
    public String toString() {
        return "Prepago{" +
                "id=" + getId() +
                ", numeroTelefono='" + getNumeroTelefono() + '\'' +
                ", numeroMinutos=" + numeroMinutos +
                ", recargas=" + recargas +
                '}';
    }
}