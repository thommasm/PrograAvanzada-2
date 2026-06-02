package model;

import utils.Utils;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Empresa implements Serializable {

    private String nombre;
    private ArrayList<Cliente> clientes;
    private ArrayList<Cuenta> cuentas;

    public Empresa(String nombre) {
        this.nombre = nombre;
        this.clientes = new ArrayList<>();
        this.cuentas = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public ArrayList<Cliente> getClientes() {
        return clientes;
    }

    public ArrayList<Cuenta> getCuentas() {
        return cuentas;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setClientes(ArrayList<Cliente> clientes) {
        this.clientes = clientes;
    }

    public void setCuentas(ArrayList<Cuenta> cuentas) {
        this.cuentas = cuentas;
    }

    public Cliente buscarCliente(String cedula) {
        for (Cliente cliente : clientes) {
            if (cliente.getCedula().equals(cedula)) {
                return cliente;
            }
        }

        return null;
    }

    public Cuenta buscarCuenta(long idCuenta) {
        for (Cuenta cuenta : cuentas) {
            if (cuenta.getId() == idCuenta) {
                return cuenta;
            }
        }

        return null;
    }

    public void agregarCliente(Cliente cliente) throws Exception {
        Cliente clienteEncontrado = buscarCliente(cliente.getCedula());

        if (clienteEncontrado != null) {
            throw new Exception("No se pudo agregar el cliente. Ya existe un cliente con esa cédula.");
        }

        clientes.add(cliente);
    }

    public long agregarCuentaPrepago(String cedulaCliente, String numeroTelefono) throws Exception {
        Cliente cliente = buscarCliente(cedulaCliente);

        if (cliente == null) {
            throw new Exception("No se pudo crear la cuenta prepago. El cliente no existe.");
        }

        long id = Utils.CONSECUTIVO;
        Utils.CONSECUTIVO = Utils.CONSECUTIVO + 1;

        Prepago cuenta = new Prepago(id, numeroTelefono);

        cuentas.add(cuenta);
        cliente.agregarCuenta(cuenta);

        return id;
    }

    public long agregarCuentaPostpago(String cedulaCliente, String numeroTelefono) throws Exception {
        Cliente cliente = buscarCliente(cedulaCliente);

        if (cliente == null) {
            throw new Exception("No se pudo crear la cuenta postpago. El cliente no existe.");
        }

        long id = Utils.CONSECUTIVO;
        Utils.CONSECUTIVO = Utils.CONSECUTIVO + 1;

        Postpago cuenta = new Postpago(id, numeroTelefono);

        cuentas.add(cuenta);
        cliente.agregarCuenta(cuenta);

        return id;
    }

    public void agregarRecarga(long idCuenta, LocalDate fecha, long valor) throws Exception {
        Cuenta cuenta = buscarCuenta(idCuenta);

        if (cuenta == null) {
            throw new Exception("No se pudo hacer la recarga. La cuenta no existe.");
        }

        if (!(cuenta instanceof Prepago)) {
            throw new Exception("No se pudo hacer la recarga. La cuenta no es prepago.");
        }

        if (valor <= 0) {
            throw new Exception("No se pudo hacer la recarga. El valor debe ser mayor a cero.");
        }

        Prepago prepago = (Prepago) cuenta;
        Recarga recarga = new Recarga(fecha, valor);

        prepago.agregarRecarga(recarga);
    }

    public void agregarLlamadaNacional(long idCuenta, LocalDate fecha, String telefonoDestinatario, int duracion) throws Exception {
        Cuenta cuenta = buscarCuenta(idCuenta);

        if (cuenta == null) {
            throw new Exception("No se pudo registrar la llamada. La cuenta no existe.");
        }

        if (duracion <= 0) {
            throw new Exception("No se pudo registrar la llamada. La duración debe ser mayor a cero.");
        }

        LlamadaNacional llamada = new LlamadaNacional(fecha, duracion, telefonoDestinatario);

        if (cuenta instanceof Postpago) {
            llamada.setValor(0);
            cuenta.agregarLlamada(llamada);
        } else if (cuenta instanceof Prepago) {
            Prepago prepago = (Prepago) cuenta;

            boolean tieneSaldo = prepago.tieneSaldoDisponible(fecha.getYear(), fecha.getMonthValue(), llamada.getValor());
            boolean tieneMinutos = prepago.tieneMinutosDisponibles(fecha.getYear(), fecha.getMonthValue(), duracion);

            if (tieneSaldo == false) {
                throw new Exception("No se pudo registrar la llamada. La cuenta prepago no tiene saldo suficiente.");
            }

            if (tieneMinutos == false) {
                throw new Exception("No se pudo registrar la llamada. La cuenta prepago no tiene minutos suficientes.");
            }

            cuenta.agregarLlamada(llamada);
        }
    }

    public void agregarLlamadaInternacional(long idCuenta, LocalDate fecha, String telefonoDestinatario, int duracion, Indicativo indicativo) throws Exception {
        Cuenta cuenta = buscarCuenta(idCuenta);

        if (cuenta == null) {
            throw new Exception("No se pudo registrar la llamada. La cuenta no existe.");
        }

        if (duracion <= 0) {
            throw new Exception("No se pudo registrar la llamada. La duración debe ser mayor a cero.");
        }

        LlamadaInternacional llamada = new LlamadaInternacional(fecha, duracion, telefonoDestinatario, indicativo);

        if (cuenta instanceof Postpago) {
            cuenta.agregarLlamada(llamada);
        } else if (cuenta instanceof Prepago) {
            Prepago prepago = (Prepago) cuenta;

            boolean tieneSaldo = prepago.tieneSaldoDisponible(fecha.getYear(), fecha.getMonthValue(), llamada.getValor());
            boolean tieneMinutos = prepago.tieneMinutosDisponibles(fecha.getYear(), fecha.getMonthValue(), duracion);

            if (tieneSaldo == false) {
                throw new Exception("No se pudo registrar la llamada. La cuenta prepago no tiene saldo suficiente.");
            }

            if (tieneMinutos == false) {
                throw new Exception("No se pudo registrar la llamada. La cuenta prepago no tiene minutos suficientes.");
            }

            cuenta.agregarLlamada(llamada);
        }
    }

    public String reportePostpagoCliente(String cedulaCliente, int anio, int mes) throws Exception {
        Cliente cliente = buscarCliente(cedulaCliente);

        if (cliente == null) {
            throw new Exception("No se pudo generar el reporte. El cliente no existe.");
        }

        String reporte = "";
        boolean tienePostpago = false;

        reporte = reporte + "REPORTE POSTPAGO\n";
        reporte = reporte + "Cliente: " + cliente.getNombre() + "\n";
        reporte = reporte + "Cédula: " + cliente.getCedula() + "\n\n";

        for (Cuenta cuenta : cliente.getCuentas()) {
            if (cuenta instanceof Postpago) {
                tienePostpago = true;

                Postpago postpago = (Postpago) cuenta;

                reporte = reporte + "Cuenta postpago ID: " + postpago.getId() + "\n";
                reporte = reporte + "Teléfono: " + postpago.getNumeroTelefono() + "\n";
                reporte = reporte + "Cargo fijo: " + postpago.getCargoFijo() + "\n";
                reporte = reporte + "Llamadas del mes:\n";

                int totalDuracion = 0;
                long totalValorLlamadas = 0;

                for (Llamada llamada : postpago.getLlamadas()) {
                    LocalDate fecha = llamada.getFecha();

                    if (fecha.getYear() == anio && fecha.getMonthValue() == mes) {
                        reporte = reporte + llamada.toString() + "\n";

                        totalDuracion = totalDuracion + llamada.getDuracion();
                        totalValorLlamadas = totalValorLlamadas + llamada.getValor();
                    }
                }

                reporte = reporte + "Total duración: " + totalDuracion + " minutos\n";
                reporte = reporte + "Total valor llamadas: " + totalValorLlamadas + "\n";
                reporte = reporte + "Total a pagar: " + postpago.obtenerPagoCuenta(anio, mes) + "\n\n";
            }
        }

        if (tienePostpago == false) {
            reporte = reporte + "El cliente no tiene cuentas postpago.\n";
        }

        return reporte;
    }

    public String reportePrepago(int anio, int mes) {
        String reporte = "";
        long totalGeneralRecargas = 0;
        int totalGeneralDuracion = 0;
        boolean existenCuentasPrepago = false;

        reporte = reporte + "REPORTE PREPAGO\n\n";

        for (Cliente cliente : clientes) {
            for (Cuenta cuenta : cliente.getCuentas()) {
                if (cuenta instanceof Prepago) {
                    existenCuentasPrepago = true;

                    Prepago prepago = (Prepago) cuenta;

                    reporte = reporte + "Cliente: " + cliente.getNombre() + "\n";
                    reporte = reporte + "Cédula: " + cliente.getCedula() + "\n";
                    reporte = reporte + "Cuenta prepago ID: " + prepago.getId() + "\n";
                    reporte = reporte + "Teléfono: " + prepago.getNumeroTelefono() + "\n";

                    long totalRecargas = prepago.calcularTotalRecargasMes(anio, mes);
                    int totalDuracion = prepago.calcularDuracionLlamadasMes(anio, mes);

                    reporte = reporte + "Total recargas del mes: " + totalRecargas + "\n";
                    reporte = reporte + "Total duración llamadas: " + totalDuracion + " minutos\n";

                    reporte = reporte + "Recargas del mes:\n";

                    for (Recarga recarga : prepago.getRecargas()) {
                        LocalDate fecha = recarga.getFecha();

                        if (fecha.getYear() == anio && fecha.getMonthValue() == mes) {
                            reporte = reporte + recarga.toString() + "\n";
                        }
                    }

                    reporte = reporte + "Llamadas del mes:\n";

                    for (Llamada llamada : prepago.getLlamadas()) {
                        LocalDate fecha = llamada.getFecha();

                        if (fecha.getYear() == anio && fecha.getMonthValue() == mes) {
                            reporte = reporte + llamada.toString() + "\n";
                        }
                    }

                    reporte = reporte + "\n";

                    totalGeneralRecargas = totalGeneralRecargas + totalRecargas;
                    totalGeneralDuracion = totalGeneralDuracion + totalDuracion;
                }
            }
        }

        if (existenCuentasPrepago == false) {
            reporte = reporte + "No hay cuentas prepago registradas.\n";
        }

        reporte = reporte + "TOTAL GENERAL RECARGAS: " + totalGeneralRecargas + "\n";
        reporte = reporte + "TOTAL GENERAL DURACIÓN: " + totalGeneralDuracion + " minutos\n";

        return reporte;
    }

    @Override
    public String toString() {
        return "Empresa{" +
                "nombre='" + nombre + '\'' +
                ", clientes=" + clientes +
                ", cuentas=" + cuentas +
                '}';
    }
}