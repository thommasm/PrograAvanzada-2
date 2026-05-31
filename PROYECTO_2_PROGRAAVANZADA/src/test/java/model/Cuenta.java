package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public abstract class Cuenta implements Serializable {

    private long id;
    private String numeroTelefono;
    private ArrayList<Llamada> llamadas;

    public Cuenta(long id, String numeroTelefono) {
        this.id = id;
        this.numeroTelefono = numeroTelefono;
        this.llamadas = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public ArrayList<Llamada> getLlamadas() {
        return llamadas;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

    public void setLlamadas(ArrayList<Llamada> llamadas) {
        this.llamadas = llamadas;
    }

    public void agregarLlamada(Llamada llamada) {
        this.llamadas.add(llamada);
    }

    public ArrayList<Llamada> obtenerLlamadasMes(int anio, int mes) {
        ArrayList<Llamada> llamadasMes = new ArrayList<>();

        for (Llamada llamada : llamadas) {
            LocalDate fecha = llamada.getFecha();

            if (fecha.getYear() == anio && fecha.getMonthValue() == mes) {
                llamadasMes.add(llamada);
            }
        }

        return llamadasMes;
    }

    public int calcularDuracionLlamadasMes(int anio, int mes) {
        int total = 0;

        for (Llamada llamada : llamadas) {
            LocalDate fecha = llamada.getFecha();

            if (fecha.getYear() == anio && fecha.getMonthValue() == mes) {
                total = total + llamada.getDuracion();
            }
        }

        return total;
    }

    public long calcularValorLlamadasMes(int anio, int mes) {
        long total = 0;

        for (Llamada llamada : llamadas) {
            LocalDate fecha = llamada.getFecha();

            if (fecha.getYear() == anio && fecha.getMonthValue() == mes) {
                total = total + llamada.getValor();
            }
        }

        return total;
    }

    public abstract long obtenerPagoCuenta(int anio, int mes);

    @Override
    public String toString() {
        return "Cuenta{" +
                "id=" + id +
                ", numeroTelefono='" + numeroTelefono + '\'' +
                ", llamadas=" + llamadas +
                '}';
    }
}