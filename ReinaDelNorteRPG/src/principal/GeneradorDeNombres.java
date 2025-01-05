package principal;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneradorDeNombres {
	PanelDeJuego pdj;
    public List<String> nombres;
    public List<String> apellidos;
    private Random random;

    public GeneradorDeNombres(PanelDeJuego pdj) {
    	this.pdj = pdj;
        this.nombres = new ArrayList<>();
        this.apellidos = new ArrayList<>();
        this.random = new Random();
        
        try {
			cargarNombres();
			cargarApellidos();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public void cargarNombres() throws IOException {
    	InputStream is = getClass().getResourceAsStream("/archivos/maleNames.txt");
        if (is == null) {
            throw new FileNotFoundException("Archivo no encontrado: /archivos/maleNames.txt");
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                nombres.add(linea.trim());
            }
        }
    }

    public void cargarApellidos() throws IOException {
    	InputStream is = getClass().getResourceAsStream("/archivos/surnames.txt");
        if (is == null) {
            throw new FileNotFoundException("Archivo no encontrado: /archivos/surnames.txt");
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                apellidos.add(linea.trim());
            }
        }
    }

    public String generarNombreCompleto() {
        if (nombres.isEmpty() || apellidos.isEmpty()) {
            throw new IllegalStateException("Debe cargar nombres y apellidos antes de generar un nombre completo.");
        }
        String nombre = nombres.get(random.nextInt(nombres.size()));
        String apellido = apellidos.get(random.nextInt(apellidos.size()));
        return apellido+" "+nombre;
    }
}
