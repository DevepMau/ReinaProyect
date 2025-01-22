package principal;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneradorDeNombres {
	PanelDeJuego pdj;
    public List<String> nombresMasculinos;
    public List<String> nombresFemeninos;
    public List<String> apellidos;
    private Random random;

    public GeneradorDeNombres(PanelDeJuego pdj) {
    	this.pdj = pdj;
        this.nombresMasculinos = new ArrayList<>();
        this.nombresFemeninos = new ArrayList<>();
        this.apellidos = new ArrayList<>();
        this.random = new Random();
        
        try {
			cargarNombresMasculinos();
			cargarNombresFemeninos();
			cargarApellidos();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public void cargarNombresMasculinos() throws IOException {
    	InputStream is = getClass().getResourceAsStream("/archivos/maleNames.txt");
        if (is == null) {
            throw new FileNotFoundException("Archivo no encontrado: /archivos/maleNames.txt");
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                nombresMasculinos.add(linea.trim());
            }
        }
    }
    
    public void cargarNombresFemeninos() throws IOException {
    	InputStream is = getClass().getResourceAsStream("/archivos/femaleNames.txt");
        if (is == null) {
            throw new FileNotFoundException("Archivo no encontrado: /archivos/femaleNames.txt");
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                nombresFemeninos.add(linea.trim());
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

    public String generarNombreCompleto(int generoBinario) {
        if (nombresMasculinos.isEmpty() || nombresFemeninos.isEmpty() || apellidos.isEmpty()) {
            throw new IllegalStateException("Debe cargar nombres y apellidos antes de generar un nombre completo.");
        }
        String nombre;
        if(generoBinario == 1) {
        	nombre = nombresMasculinos.get(random.nextInt(nombresMasculinos.size()));
        }
        else {
        	nombre = nombresFemeninos.get(random.nextInt(nombresFemeninos.size()));
        }
        String apellido = apellidos.get(random.nextInt(apellidos.size()));
        return nombre+" "+apellido;
    }
}
