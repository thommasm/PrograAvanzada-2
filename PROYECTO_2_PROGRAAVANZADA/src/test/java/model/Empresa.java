package model;

import utils.Utils;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Empresa implements IEmpresa, Serializable {

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
            throw new Exception("ya existe un cliente con esa cedula");
        }

        clientes.add(cliente);
    }

    public long agregarCuentaPrepago(String cedulaCliente, String numeroTelefono) throws Exception {
        Cliente cliente = buscarCliente(cedulaCliente);

        if (cliente == null) {
            throw new Exception("el cliente no existe");
        }

        long id = Utils.generarIdCuenta();

        Prepago cuenta = new Prepago(id, numeroTelefono);

        cuentas.add(cuenta);
        cliente.agregarCuenta(cuenta);

        return id;
    }

    public long agregarCuentaPostpago(String cedulaCliente, String numeroTelefono) throws Exception {
        Cliente cliente = buscarCliente(cedulaCliente);

        if (cliente == null) {
            throw new Exception("el cliente no existe");
        }

        long id = Utils.generarIdCuenta();

        Postpago cuenta = new Postpago(id, numeroTelefono);

        cuentas.add(cuenta);
        cliente.agregarCuenta(cuenta);

        return id;
    }

    public void agregarRecarga(long idCuenta, LocalDate fecha, long valor) throws Exception {
        Cuenta cuenta = buscarCuenta(idCuenta);

        if (cuenta == null) {
            throw new Exception("la cuenta no existe");
        }

        if (!(cuenta instanceof Prepago)) {
            throw new Exception("la cuenta no es prepago");
        }

        if (valor <= 0) {
            throw new Exception("el valor debe ser mayor a cero");
        }

        Prepago prepago = (Prepago) cuenta;
        Recarga recarga = new Recarga(fecha, valor);

        prepago.agregarRecarga(recarga);
    }

    public void agregarLlamadaNacional(long idCuenta, LocalDate fecha, String telefonoDestinatario, int duracion) throws Exception {
        Cuenta cuenta = buscarCuenta(idCuenta);

        if (cuenta == null) {
            throw new Exception("la cuenta no existe");
        }

        if (duracion <= 0) {
            throw new Exception("la duracion debe ser mayor a cero");
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
                throw new Exception("no tiene saldo suficiente");
            }

            if (tieneMinutos == false) {
                throw new Exception("no tiene minutos suficientes");
            }

            cuenta.agregarLlamada(llamada);
        }
    }

    public void agregarLlamadaInternacional(long idCuenta, LocalDate fecha, String telefonoDestinatario, int duracion, Indicativo indicativo) throws Exception {
        Cuenta cuenta = buscarCuenta(idCuenta);

        if (cuenta == null) {
            throw new Exception("la cuenta no existe");
        }

        if (duracion <= 0) {
            throw new Exception("la duracion debe ser mayor a cero");
        }

        LlamadaInternacional llamada = new LlamadaInternacional(fecha, duracion, telefonoDestinatario, indicativo);

        if (cuenta instanceof Postpago) {
            cuenta.agregarLlamada(llamada);

        } else if (cuenta instanceof Prepago) {
            Prepago prepago = (Prepago) cuenta;

            boolean tieneSaldo = prepago.tieneSaldoDisponible(fecha.getYear(), fecha.getMonthValue(), llamada.getValor());
            boolean tieneMinutos = prepago.tieneMinutosDisponibles(fecha.getYear(), fecha.getMonthValue(), duracion);

            if (tieneSaldo == false) {
                throw new Exception("no tiene saldo suficiente");
            }

            if (tieneMinutos == false) {
                throw new Exception("no tiene minutos suficientes");
            }

            cuenta.agregarLlamada(llamada);
        }
    }

    public ArrayList<Cliente> ordenarClientesPorCedula(ArrayList<Cliente> listaClientes) {
        ArrayList<Cliente> ordenados = new ArrayList<>();

        for (Cliente cliente : listaClientes) {
            ordenados.add(cliente);
        }

        for (int i = 0; i < ordenados.size() - 1; i++) {
            for (int j = 0; j < ordenados.size() - 1 - i; j++) {

                Cliente clienteActual = ordenados.get(j);
                Cliente clienteSiguiente = ordenados.get(j + 1);

                if (clienteActual.getCedula().compareTo(clienteSiguiente.getCedula()) > 0) {
                    ordenados.set(j, clienteSiguiente);
                    ordenados.set(j + 1, clienteActual);
                }
            }
        }

        return ordenados;
    }

    public ArrayList<Llamada> ordenarLlamadasPorFecha(ArrayList<Llamada> listaLlamadas) {
        ArrayList<Llamada> ordenadas = new ArrayList<>();

        for (Llamada llamada : listaLlamadas) {
            ordenadas.add(llamada);
        }

        for (int i = 0; i < ordenadas.size() - 1; i++) {
            for (int j = 0; j < ordenadas.size() - 1 - i; j++) {

                Llamada llamadaActual = ordenadas.get(j);
                Llamada llamadaSiguiente = ordenadas.get(j + 1);

                if (llamadaActual.getFecha().isAfter(llamadaSiguiente.getFecha())) {
                    ordenadas.set(j, llamadaSiguiente);
                    ordenadas.set(j + 1, llamadaActual);
                }
            }
        }

        return ordenadas;
    }

    public ArrayList<Recarga> ordenarRecargasPorFecha(ArrayList<Recarga> listaRecargas) {
        ArrayList<Recarga> ordenadas = new ArrayList<>();

        for (Recarga recarga : listaRecargas) {
            ordenadas.add(recarga);
        }

        for (int i = 0; i < ordenadas.size() - 1; i++) {
            for (int j = 0; j < ordenadas.size() - 1 - i; j++) {

                Recarga recargaActual = ordenadas.get(j);
                Recarga recargaSiguiente = ordenadas.get(j + 1);

                if (recargaActual.getFecha().isAfter(recargaSiguiente.getFecha())) {
                    ordenadas.set(j, recargaSiguiente);
                    ordenadas.set(j + 1, recargaActual);
                }
            }
        }

        return ordenadas;
    }

    public String reportePostpagoCliente(String cedulaCliente, int anio, int mes) throws Exception {
        Cliente cliente = buscarCliente(cedulaCliente);

        if (cliente == null) {
            throw new Exception("el cliente no existe");
        }

        String reporte = "";
        boolean tienePostpago = false;

        reporte = reporte + "reporte postpago\n";
        reporte = reporte + "cliente: " + cliente.getNombre() + "\n";
        reporte = reporte + "cedula: " + cliente.getCedula() + "\n\n";

        for (Cuenta cuenta : cliente.getCuentas()) {
            if (cuenta instanceof Postpago) {
                tienePostpago = true;

                Postpago postpago = (Postpago) cuenta;

                reporte = reporte + "cuenta postpago id: " + postpago.getId() + "\n";
                reporte = reporte + "telefono: " + postpago.getNumeroTelefono() + "\n";
                reporte = reporte + "cargo fijo: " + postpago.getCargoFijo() + "\n";
                reporte = reporte + "llamadas del mes:\n";

                int totalDuracion = 0;
                long totalValorLlamadas = 0;

                ArrayList<Llamada> llamadasMes = postpago.obtenerLlamadasMes(anio, mes);
                ArrayList<Llamada> llamadasOrdenadas = ordenarLlamadasPorFecha(llamadasMes);

                for (Llamada llamada : llamadasOrdenadas) {
                    reporte = reporte + llamada.toString() + "\n";

                    totalDuracion = totalDuracion + llamada.getDuracion();
                    totalValorLlamadas = totalValorLlamadas + llamada.getValor();
                }

                reporte = reporte + "total duracion: " + totalDuracion + " minutos\n";
                reporte = reporte + "total valor llamadas: " + totalValorLlamadas + "\n";
                reporte = reporte + "total a pagar: " + postpago.obtenerPagoCuenta(anio, mes) + "\n\n";
            }
        }

        if (tienePostpago == false) {
            reporte = reporte + "el cliente no tiene cuentas postpago\n";
        }

        return reporte;
    }

    public String reportePrepago(int anio, int mes) {
        String reporte = "";
        long totalGeneralRecargas = 0;
        int totalGeneralDuracion = 0;
        boolean existenCuentasPrepago = false;

        reporte = reporte + "reporte prepago\n\n";

        ArrayList<Cliente> clientesOrdenados = ordenarClientesPorCedula(clientes);

        for (Cliente cliente : clientesOrdenados) {
            for (Cuenta cuenta : cliente.getCuentas()) {
                if (cuenta instanceof Prepago) {
                    existenCuentasPrepago = true;

                    Prepago prepago = (Prepago) cuenta;

                    reporte = reporte + "cliente: " + cliente.getNombre() + "\n";
                    reporte = reporte + "cedula: " + cliente.getCedula() + "\n";
                    reporte = reporte + "cuenta prepago id: " + prepago.getId() + "\n";
                    reporte = reporte + "telefono: " + prepago.getNumeroTelefono() + "\n";

                    long totalRecargas = prepago.calcularTotalRecargasMes(anio, mes);
                    int totalDuracion = prepago.calcularDuracionLlamadasMes(anio, mes);

                    reporte = reporte + "total recargas del mes: " + totalRecargas + "\n";
                    reporte = reporte + "total duracion llamadas: " + totalDuracion + " minutos\n";

                    reporte = reporte + "recargas del mes:\n";

                    ArrayList<Recarga> recargasMes = prepago.obtenerRecargasMes(anio, mes);
                    ArrayList<Recarga> recargasOrdenadas = ordenarRecargasPorFecha(recargasMes);

                    for (Recarga recarga : recargasOrdenadas) {
                        reporte = reporte + recarga.toString() + "\n";
                    }

                    reporte = reporte + "llamadas del mes:\n";

                    ArrayList<Llamada> llamadasMes = prepago.obtenerLlamadasMes(anio, mes);
                    ArrayList<Llamada> llamadasOrdenadas = ordenarLlamadasPorFecha(llamadasMes);

                    for (Llamada llamada : llamadasOrdenadas) {
                        reporte = reporte + llamada.toString() + "\n";
                    }

                    reporte = reporte + "\n";

                    totalGeneralRecargas = totalGeneralRecargas + totalRecargas;
                    totalGeneralDuracion = totalGeneralDuracion + totalDuracion;
                }
            }
        }

        if (existenCuentasPrepago == false) {
            reporte = reporte + "no hay cuentas prepago registradas\n";
        }

        reporte = reporte + "total general recargas: " + totalGeneralRecargas + "\n";
        reporte = reporte + "total general duracion: " + totalGeneralDuracion + " minutos\n";

        return reporte;
    }

    public String toString() {
        return "Empresa{" +
                "nombre='" + nombre + '\'' +
                ", clientes=" + clientes +
                ", cuentas=" + cuentas +
                '}';
    }
}