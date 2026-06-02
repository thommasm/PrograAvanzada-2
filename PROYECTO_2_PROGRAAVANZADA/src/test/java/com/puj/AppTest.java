package com.puj;

import model.Cuenta;
import model.Empresa;
import model.IEmpresa;
import model.Indicativo;
import persistencia.ManejoArchivos;

import java.time.LocalDate;
import java.util.Scanner;

public class AppTest {

    private static Scanner sc = new Scanner(System.in);
    private static IEmpresa empresa = new Empresa("javemovil");

    public static void main(String[] args) {

        int opcion = 0;

        do {
            mostrarMenu();

            try {
                opcion = leerEntero("escoja una opcion: ");

                if (opcion == 1) {
                    cargarClientes();
                } else if (opcion == 2) {
                    agregarCuenta();
                } else if (opcion == 3) {
                    agregarLlamada();
                } else if (opcion == 4) {
                    agregarRecarga();
                } else if (opcion == 5) {
                    reportePostpago();
                } else if (opcion == 6) {
                    reportePrepago();
                } else if (opcion == 7) {
                    guardarSistema();
                } else if (opcion == 8) {
                    cargarSistema();
                } else if (opcion == 9) {
                    System.out.println("saliendo del sistema...");
                } else {
                    System.out.println("opcion no valida");
                }

            } catch (Exception e) {
                System.out.println("error: " + e.getMessage());
            }

        } while (opcion != 9);
    }

    public static void mostrarMenu() {
        System.out.println("\n===== menu javemovil =====");
        System.out.println("1. cargar clientes desde archivo");
        System.out.println("2. agregar cuenta prepago o postpago");
        System.out.println("3. registrar llamada");
        System.out.println("4. agregar recarga");
        System.out.println("5. reporte postpago");
        System.out.println("6. reporte prepago");
        System.out.println("7. guardar sistema");
        System.out.println("8. cargar sistema");
        System.out.println("9. salir");
    }

    public static void cargarClientes() {
        boolean cargado = false;

        do {
            try {
                String ruta = leerTexto("ingrese la ruta del archivo de clientes o 0 para volver: ");

                if (ruta.equals("0")) {
                    return;
                }

                ManejoArchivos.cargarClientesDesdeTexto(ruta, (Empresa) empresa);

                System.out.println("clientes cargados bien");
                cargado = true;

            } catch (Exception e) {
                System.out.println("error: " + e.getMessage());
                System.out.println("intente otra vez");
            }

        } while (cargado == false);
    }

    public static void agregarCuenta() {
        boolean creada = false;

        do {
            try {
                System.out.println("\n1. cuenta prepago");
                System.out.println("2. cuenta postpago");
                System.out.println("0. volver");

                int tipo = leerEntero("escoja el tipo de cuenta: ");

                if (tipo == 0) {
                    return;
                }

                String cedula = leerTexto("ingrese la cedula del cliente: ");
                String telefono = leerTexto("ingrese el numero de telefono: ");

                long idCuenta;

                if (tipo == 1) {
                    idCuenta = empresa.agregarCuentaPrepago(cedula, telefono);
                    System.out.println("cuenta prepago creada con id: " + idCuenta);
                    creada = true;

                } else if (tipo == 2) {
                    idCuenta = empresa.agregarCuentaPostpago(cedula, telefono);
                    System.out.println("cuenta postpago creada con id: " + idCuenta);
                    creada = true;

                } else {
                    throw new Exception("tipo de cuenta no valido");
                }

            } catch (Exception e) {
                System.out.println("error: " + e.getMessage());
                System.out.println("vuelva a intentar");
            }

        } while (creada == false);
    }

    public static void agregarLlamada() {
        boolean agregada = false;

        do {
            try {
                System.out.println("\n1. llamada nacional");
                System.out.println("2. llamada internacional");
                System.out.println("0. volver");

                int tipo = leerEntero("escoja el tipo de llamada: ");

                if (tipo == 0) {
                    return;
                }

                long idCuenta = leerLong("ingrese el id de la cuenta: ");

                int anio = leerEntero("año de la llamada: ");
                int mes = leerEntero("mes de la llamada: ");
                int dia = leerEntero("dia de la llamada: ");

                LocalDate fecha = LocalDate.of(anio, mes, dia);

                String telefonoDestino = leerTexto("telefono destinatario: ");
                int duracion = leerEntero("duracion en minutos: ");

                if (tipo == 1) {
                    empresa.agregarLlamadaNacional(idCuenta, fecha, telefonoDestino, duracion);
                    System.out.println("llamada nacional agregada");
                    agregada = true;

                } else if (tipo == 2) {
                    Indicativo indicativo = seleccionarIndicativo();

                    empresa.agregarLlamadaInternacional(idCuenta, fecha, telefonoDestino, duracion, indicativo);
                    System.out.println("llamada internacional agregada");
                    agregada = true;

                } else {
                    throw new Exception("tipo de llamada no valido");
                }

            } catch (Exception e) {
                System.out.println("error: " + e.getMessage());
                System.out.println("intente otra vez");
            }

        } while (agregada == false);
    }

    public static void agregarRecarga() {
        boolean recargada = false;

        do {
            try {
                long idCuenta = leerLong("ingrese el id de la cuenta prepago o 0 para volver: ");

                if (idCuenta == 0) {
                    return;
                }

                int anio = leerEntero("año de la recarga: ");
                int mes = leerEntero("mes de la recarga: ");
                int dia = leerEntero("dia de la recarga: ");

                LocalDate fecha = LocalDate.of(anio, mes, dia);

                long valor = leerLong("valor de la recarga: ");

                empresa.agregarRecarga(idCuenta, fecha, valor);

                System.out.println("recarga agregada");
                recargada = true;

            } catch (Exception e) {
                System.out.println("error: " + e.getMessage());
                System.out.println("intente de nuevo");
            }

        } while (recargada == false);
    }

    public static void reportePostpago() {
        boolean generado = false;

        do {
            try {
                String cedula = leerTexto("ingrese la cedula del cliente o 0 para volver: ");

                if (cedula.equals("0")) {
                    return;
                }

                int anio = leerEntero("año del reporte: ");
                int mes = leerEntero("mes del reporte: ");

                String reporte = empresa.reportePostpagoCliente(cedula, anio, mes);

                System.out.println(reporte);
                generado = true;

            } catch (Exception e) {
                System.out.println("error: " + e.getMessage());
                System.out.println("intente otra vez");
            }

        } while (generado == false);
    }

    public static void reportePrepago() {
        try {
            int anio = leerEntero("año del reporte: ");
            int mes = leerEntero("mes del reporte: ");

            String reporte = empresa.reportePrepago(anio, mes);

            System.out.println(reporte);

        } catch (Exception e) {
            System.out.println("error: " + e.getMessage());
        }
    }

    public static void guardarSistema() {
        boolean guardado = false;

        do {
            try {
                String ruta = leerTexto("nombre del archivo para guardar o 0 para volver: ");

                if (ruta.equals("0")) {
                    return;
                }

                ManejoArchivos.salvarSistema((Empresa) empresa, ruta);

                System.out.println("sistema guardado");
                guardado = true;

            } catch (Exception e) {
                System.out.println("error: " + e.getMessage());
                System.out.println("intente otra vez");
            }

        } while (guardado == false);
    }

    public static void cargarSistema() {
        boolean cargado = false;

        do {
            try {
                String ruta = leerTexto("nombre del archivo para cargar o 0 para volver: ");

                if (ruta.equals("0")) {
                    return;
                }

                empresa = ManejoArchivos.cargarSistema(ruta);

                System.out.println("sistema cargado");
                cargado = true;

            } catch (Exception e) {
                System.out.println("error: " + e.getMessage());
                System.out.println("intente otra vez");
            }

        } while (cargado == false);
    }

    public static Indicativo seleccionarIndicativo() throws Exception {
        Indicativo[] paises = Indicativo.values();

        for (int i = 0; i < paises.length; i++) {
            System.out.println((i + 1) + ". " + paises[i]);
        }

        int opcion = leerEntero("escoja el pais: ");

        if (opcion < 1 || opcion > paises.length) {
            throw new Exception("pais no valido");
        }

        return paises[opcion - 1];
    }

    public static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return sc.nextLine();
    }

    public static int leerEntero(String mensaje) throws Exception {
        try {
            System.out.print(mensaje);
            String texto = sc.nextLine();
            return Integer.parseInt(texto);

        } catch (NumberFormatException e) {
            throw new Exception("debe ingresar un numero entero");
        }
    }

    public static long leerLong(String mensaje) throws Exception {
        try {
            System.out.print(mensaje);
            String texto = sc.nextLine();
            return Long.parseLong(texto);

        } catch (NumberFormatException e) {
            throw new Exception("debe ingresar un numero valido");
        }
    }
}