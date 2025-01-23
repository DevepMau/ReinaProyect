package principal;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class UI {

	PanelDeJuego pdj;
	Graphics2D g2;
	Font maruMonica;
	int anchoLinea;
	int altoFila;
	public boolean mensajeActivo = false;
	public String mensaje = "";
	int contadorMensaje = 0;
	public boolean juegoTerminado = false;
	public String dialogoActual = "";
	int numeroDeInstruccion = 0;

	public UI(PanelDeJuego pdj) {

		this.pdj = pdj;
		anchoLinea = pdj.tamañoDeBaldosa/16;
		altoFila = (pdj.tamañoDeBaldosa/2)+(pdj.tamañoDeBaldosa/4);
	
		try {
			InputStream is = getClass().getResourceAsStream("/fuentes/x12y16pxMaruMonica.ttf");
			maruMonica = Font.createFont(Font.TRUETYPE_FONT, is);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void mostrarMensaje(String texto) {

		mensaje = texto;
		mensajeActivo = true;

	}

	public void dibujar(Graphics2D g2) {

		this.g2 = g2;

		g2.setFont(maruMonica);
		g2.setColor(Color.white);

	}
	
	public void dibujarPantallaDeTitulo() {
		
		int anchoDeLinea = 5;
		int anchoDeTexto;
		FontMetrics fm;
		
		g2.setColor(new Color(0, 0, 0));
		g2.fillRect(0, 0, pdj.anchoDePantalla, pdj.altoDePantalla);
		
		//TITULO
		g2.setFont(g2.getFont().deriveFont(Font.BOLD,96f));
		String texto = "EXAMPLE TITLE";
		int x = obtenerXParaTextoCentrado(texto);
		int y = pdj.tamañoDeBaldosa*3;
		
		//SOMBRA
		g2.setColor(Color.gray);
		g2.drawString(texto, x+5, y+5);
		
		//COLOR PRINCIPAL
		g2.setColor(Color.white);
		g2.drawString(texto, x,y);
		
		//MENU
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48f));
		
		texto = "JUEGO NUEVO";
		x = obtenerXParaTextoCentrado(texto);
		y += pdj.tamañoDeBaldosa*5;
		g2.drawString(texto, x, y);
		if(numeroDeInstruccion == 0) {
			fm = g2.getFontMetrics();
			anchoDeTexto = fm.stringWidth(texto);
			g2.setColor(Color.white);
			g2.setStroke(new BasicStroke(anchoDeLinea));
			g2.drawRoundRect(x-anchoDeLinea, y-pdj.tamañoDeBaldosa+anchoDeLinea, anchoDeTexto+anchoDeLinea*2 , pdj.tamañoDeBaldosa+anchoDeLinea, 10, 10);
		}
		
		texto = "CARGAR JUEGO";
		x = obtenerXParaTextoCentrado(texto);
		y += pdj.tamañoDeBaldosa+(pdj.tamañoDeBaldosa/8);
		g2.drawString(texto, x, y);
		if(numeroDeInstruccion == 1) {
			fm = g2.getFontMetrics();
			anchoDeTexto = fm.stringWidth(texto);
			g2.setColor(Color.white);
			g2.setStroke(new BasicStroke(anchoDeLinea));
			g2.drawRoundRect(x-anchoDeLinea, y-pdj.tamañoDeBaldosa+anchoDeLinea, anchoDeTexto+anchoDeLinea*2 , pdj.tamañoDeBaldosa+anchoDeLinea, 10, 10);
		}
		
		texto = "SALIR";
		x = obtenerXParaTextoCentrado(texto);
		y += pdj.tamañoDeBaldosa+(pdj.tamañoDeBaldosa/8);
		g2.drawString(texto, x, y);
		if(numeroDeInstruccion == 2) {
			fm = g2.getFontMetrics();
			anchoDeTexto = fm.stringWidth(texto);
			g2.setColor(Color.white);
			g2.setStroke(new BasicStroke(anchoDeLinea));
			g2.drawRoundRect(x-anchoDeLinea, y-pdj.tamañoDeBaldosa+anchoDeLinea, anchoDeTexto+anchoDeLinea*2 , pdj.tamañoDeBaldosa+anchoDeLinea, 10, 10);
		}
		
	}

	public void dibujarPantallaPausa() {

		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80f));
		String texto = "PAUSADO";
		int x = obtenerXParaTextoCentrado(texto);
		int y = pdj.altoDePantalla / 2;

		g2.drawString(texto, x, y);

	}
	
	public void dibujarPantallaDeDialogo() {

		int x = pdj.tamañoDeBaldosa*2;
		int y = pdj.tamañoDeBaldosa*8;
		int width = pdj.anchoDePantalla - (pdj.tamañoDeBaldosa*4);
		int height = pdj.tamañoDeBaldosa*4;

		dibujarSubVentana(x, y, width, height);
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 32F));
		x += pdj.tamañoDeBaldosa;
		y += pdj.tamañoDeBaldosa;

		for(String line : dialogoActual.split("\n")) {
			g2.drawString(line, x, y);
			y += 40;
		}

	}
	
	public void dibujarPantallaDeCombate() {
		pdj.combate.dibujar(g2);
	}

	public void dibujarSubVentana(int x, int y, int width, int height) {

		Color c = new Color(0,0,0, 200);
		g2.setColor(c);
		g2.fillRoundRect(x, y, width, height, 35, 35);
		
		c = new Color(255, 255, 255);
		g2.setColor(c);
		g2.setStroke(new BasicStroke(5));
		g2.drawRoundRect(x, y, width-10, height-10, 25, 25);

	}

	public int obtenerXParaTextoCentrado(String texto) {

		int longitud = (int) g2.getFontMetrics().getStringBounds(texto, g2).getWidth();
		int x = pdj.anchoDePantalla / 2 - longitud / 2;
		return x;

	}
	
	public BufferedImage configurarImagen(String rutaImagen, int escala) {
	    Utilidades uTool = new Utilidades();
	    BufferedImage imagen = null;
	    try {
	        imagen = ImageIO.read(getClass().getResourceAsStream(rutaImagen + ".png"));
	        imagen = uTool.escalarImagen(imagen, imagen.getWidth()/2*escala, imagen.getHeight()/2*escala);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return imagen;

	}
	
	public Font getMaruMonica() {
		return this.maruMonica;
	}
	
}