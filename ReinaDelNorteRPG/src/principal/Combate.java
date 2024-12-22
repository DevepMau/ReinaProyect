package principal;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;

public class Combate {
	PanelDeJuego pdj;
	Graphics2D g2;
	HashMap<Integer, Zona> zonas = new HashMap<>();
	ArrayList<Unidad> unidades = new ArrayList<>();
	
	public Combate(PanelDeJuego pdj) {
		this.pdj = pdj;
		crearTablero();
		unidades.add(new Unidad(zonas.get(0), true, pdj));
		unidades.add(new Unidad(zonas.get(0), false, pdj));
		unidades.add(new Unidad(zonas.get(0), true, pdj));
		unidades.add(new Unidad(zonas.get(0), true, pdj));
		unidades.add(new Unidad(zonas.get(0), false, pdj));
		unidades.add(new Unidad(zonas.get(0), false, pdj));
		
	}
	/////////////////////////////////////////////////////////
	public void actualizar() {
		int idAli = 5;
		int idEne = 1;
		for (Unidad unidad : unidades) {
		    if(unidad.aliado) {
		    	unidad.posicionar(zonas.get(idAli));
		    	idAli++;
		    }
		    else {
		    	unidad.posicionar(zonas.get(idEne));
		    	idEne++;
		    }
		}
	}
	
	public void dibujar(Graphics2D g2) {
		this.g2 = g2;
		for (Zona zona : zonas.values()) {
			zona.dibujar(g2);
		}
		for (Unidad unidad : unidades) {
		    unidad.dibujar(g2);;
		}
		//dibujarRetrato(zonas.get(32).x, zonas.get(32).y, pdj.tamañoDeBaldosa*4, pdj.tamañoDeBaldosa*4);
	}
	//////////////////////////////////////////////////////////
	public void crearTablero() {
		int x = 0;
		int y = 0;
		int n = 0;
		int id = 0;
		for (int i = 0; i < 6; i++) {
			for (int k = 0; k < 8; k++) {
				if(verificar(n)) {
					if(n == 32) {
						zonas.put(9, new Zona(9, x, y, pdj));
					}
					else {
						zonas.put(id, new Zona(id, x, y, pdj));
						id++;
					}
				}
				n++;
				x += pdj.tamañoDeBaldosa*2;
			}
			x = 0;
			y += pdj.tamañoDeBaldosa*2;
		}
	}
	
	public void dibujarVentana(int x, int y, int width, int height) {
		Color c = new Color(0,0,0, 200);
		g2.setColor(c);
		g2.fillRoundRect(x+5, y+5, width, height, 35, 35);
		
		c = new Color(255, 255, 255);
		g2.setColor(c);
		g2.setStroke(new BasicStroke(5));
		g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
	}
	
	public void dibujarRetrato(int x, int y, int width, int height) {
		Color c = new Color(135, 206, 235);
		g2.setColor(c);
		g2.setStroke(new BasicStroke(5));
		g2.fillRoundRect(x+width/4, y+5, width/2, height/2, 100, 100);
		g2.fillRect(x+width/4, y+height/2, width/2, height/2);
		g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
	}
	
	public boolean verificar(int n) {
		if(n == 0 || (10 <= n && n <= 13) || (34 <= n && n <= 37) || n == 32) {
			return true;
		}
		return false;
	}

}
