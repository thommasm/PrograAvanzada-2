package model;

import java.io.Serializable;
import java.time.LocalDate;

public abstract class Llamada implements Serializable {

    private LocalDate fecha;
    private int duracion;
    private String telefonoDestinatario;
    private long valor;

    public Llamada(LocalDate fecha, int duracion, String telefonoDestinatario) {
        this.fecha = fecha;
        this.duracion = duracion;
        this.telefonoDestinatario = telefonoDestinatario;
        this.valor = 0;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public int getDuracion() {
        return duracion;
    }

    public String getTelefonoDestinatario() {
        return telefonoDestinatario;
    }

    public long getValor() {
        return valor;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public void setTelefonoDestinatario(String telefonoDestinatario) {
        this.telefonoDestinatario = telefonoDestinatario;
    }

    public void setValor(long valor) {
        this.valor = valor;
    }

    public abstract long calcularValor();

    @Override
    public String toString() {
        return "Llamada{" +
                "fecha=" + fecha +
                ", duracion=" + duracion +
                ", telefonoDestinatario='" + telefonoDestinatario + '\'' +
                ", valor=" + valor +
                '}';
    }
}