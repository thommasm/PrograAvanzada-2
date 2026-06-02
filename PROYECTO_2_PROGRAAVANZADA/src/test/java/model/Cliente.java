package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Cliente implements Serializable {

    private String nombre;
    private String cedula;
    private String direccion;
    private ArrayList<Cuenta> cuentas;

    public Cliente(String nombre, String cedula, String direccion) {
        this.nombre = nombre;
        this.cedula = cedula;
        this.direccion = direccion;
        this.cuentas = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public String getCedula() {
        return cedula;
    }

    public String getDireccion() {
        return direccion;
    }

    public ArrayList<Cuenta> getCuentas() {
        return cuentas;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setCuentas(ArrayList<Cuenta> cuentas) {
        this.cuentas = cuentas;
    }

    public void agregarCuenta(Cuenta cuenta) {
        this.cuentas.add(cuenta);
    }

    public Cuenta buscarCuenta(long idCuenta) {
        for (Cuenta cuenta : cuentas) {
            if (cuenta.getId() == idCuenta) {
                return cuenta;
            }
        }

        return null;
    }

    public boolean tieneCuentasPrepago() {
        for (Cuenta cuenta : cuentas) {
            if (cuenta instanceof Prepago) {
                return true;
            }
        }

        return false;
    }

    public boolean tieneCuentasPostpago() {
        for (Cuenta cuenta : cuentas) {
            if (cuenta instanceof Postpago) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "nombre='" + nombre + '\'' +
                ", cedula='" + cedula + '\'' +
                ", direccion='" + direccion + '\'' +
                ", cuentas=" + cuentas +
                '}';
    }
}