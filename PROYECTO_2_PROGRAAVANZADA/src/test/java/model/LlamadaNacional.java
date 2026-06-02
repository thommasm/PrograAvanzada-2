package model;

import utils.Utils;

import java.io.Serializable;
import java.time.LocalDate;

public class LlamadaNacional extends Llamada implements Serializable {

    public LlamadaNacional(LocalDate fecha, int duracion, String telefonoDestinatario) {
        super(fecha, duracion, telefonoDestinatario);
        setValor(calcularValor());
    }

    @Override
    public long calcularValor() {
        long valorTotal = getDuracion() * Utils.VALOR_MINUTO;
        return valorTotal;
    }

    @Override
    public String toString() {
        return "LlamadaNacional{" +
                "fecha=" + getFecha() +
                ", duracion=" + getDuracion() +
                ", telefonoDestinatario='" + getTelefonoDestinatario() + '\'' +
                ", valor=" + getValor() +
                '}';
    }
}