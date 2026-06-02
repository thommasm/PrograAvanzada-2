package persistencia;

import model.Cliente;
import model.Empresa;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ManejoArchivos {

    public static void cargarClientesDesdeTexto(String archivo, Empresa empresa) throws Exception {

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {

            String linea;

            while ((linea = reader.readLine()) != null) {

                linea = linea.trim();

                if (linea.equals("#FIN")) {
                    break;
                }

                if (!linea.startsWith("#") && !linea.equals("")) {

                    String[] partes = linea.split("\\*");

                    if (partes.length == 3) {

                        String nombre = partes[0].trim();
                        String cedula = partes[1].trim();
                        String direccion = partes[2].trim();

                        Cliente cliente = new Cliente(nombre, cedula, direccion);

                        empresa.agregarCliente(cliente);

                    } else {
                        throw new Exception("Error en el formato de la linea: " + linea);
                    }
                }
            }

        } catch (IOException e) {
            throw new Exception("Error al leer el archivo de clientes: " + e.getMessage());
        }
    }

    public static void salvarSistema(Empresa empresa, String archivo) throws Exception {

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {

            oos.writeObject(empresa);

        } catch (IOException e) {
            throw new Exception("Error al guardar el sistema: " + e.getMessage());
        }
    }

    public static Empresa cargarSistema(String archivo) throws Exception {

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {

            return (Empresa) ois.readObject();

        } catch (IOException e) {
            throw new Exception("Error al cargar el sistema: " + e.getMessage());

        } catch (ClassNotFoundException e) {
            throw new Exception("Error al leer el objeto guardado: " + e.getMessage());
        }
    }
}