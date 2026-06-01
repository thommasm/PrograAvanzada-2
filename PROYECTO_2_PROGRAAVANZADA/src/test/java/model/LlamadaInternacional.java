package model;

import utils.Utils;

import java.time.LocalDate;

public class LlamadaInternacional extends Llamada {

    private Indicativo indicativo;

    public LlamadaInternacional(LocalDate fecha, int duracion, String telefonoDestinatario, Indicativo indicativo) {
        super(fecha, duracion, indicativo.formarTelefonoInternacional(telefonoDestinatario));
        this.indicativo = indicativo;
        setValor(calcularValor());
    }

    public Indicativo getIndicativo() {
        return indicativo;
    }

    public void setIndicativo(Indicativo indicativo) {
        this.indicativo = indicativo;
    }

    @Override
    public long calcularValor() {
        long valorBase = getDuracion() * Utils.VALOR_MINUTO;
        long recargo = valorBase * 20 / 100;
        long valorTotal = valorBase + recargo;

        return valorTotal;
    }

    @Override
    public String toString() {
        return "LlamadaInternacional{" +
                "fecha=" + getFecha() +
                ", duracion=" + getDuracion() +
                ", telefonoDestinatario='" + getTelefonoDestinatario() + '\'' +
                ", indicativo=" + indicativo +
                ", valor=" + getValor() +
                '}';
    }
}