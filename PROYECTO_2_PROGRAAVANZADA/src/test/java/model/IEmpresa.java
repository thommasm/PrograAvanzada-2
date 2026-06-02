package model;

import java.time.LocalDate;
import java.util.ArrayList;

public interface IEmpresa {

    Cliente buscarCliente(String cedula);

    Cuenta buscarCuenta(long idCuenta);

    void agregarCliente(Cliente cliente) throws Exception;

    long agregarCuentaPrepago(String cedulaCliente, String numeroTelefono) throws Exception;

    long agregarCuentaPostpago(String cedulaCliente, String numeroTelefono) throws Exception;

    void agregarRecarga(long idCuenta, LocalDate fecha, long valor) throws Exception;

    void agregarLlamadaNacional(long idCuenta, LocalDate fecha, String telefonoDestinatario, int duracion) throws Exception;

    void agregarLlamadaInternacional(long idCuenta, LocalDate fecha, String telefonoDestinatario, int duracion, Indicativo indicativo) throws Exception;

    String reportePostpagoCliente(String cedulaCliente, int anio, int mes) throws Exception;

    String reportePrepago(int anio, int mes);

    ArrayList<Cliente> getClientes();

    ArrayList<Cuenta> getCuentas();
}