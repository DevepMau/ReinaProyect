package principal;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.HashMap;

public class Combate {
	
	PanelDeJuego pdj;
	Graphics2D g2;
	HashMap<Integer, Zona> zona = new HashMap<>();
	Personaje[] pj = new Personaje[8];
	Personaje[] en = new Personaje[8];
	
	public Combate(PanelDeJuego pdj) {
		this.pdj = pdj;
		cargarZonas();
		cargarGrupos();
	}
	
	public void actualizar() {
		
		int id = 34;
		for(int i = 0; i < pj.length; i++) {
			pj[i].setPos(zona.get(id).getPos());
			if(i == 3) {
				id += 5;
			}
			else {
				id++;
			}
		}
		id = 2;
		for(int i = 0; i < pj.length; i++) {
			en[i].setPos(zona.get(id).getPos());
			if(i == 3) {
				id += 5;
			}
			else {
				id++;
			}
		}
	}
	
	public void dinujar(Graphics2D g2) {
		this.g2 = g2;
		//ZONAS
		for(Zona num : zona.values()) {
			num.dibujar(g2);
		}
		//UNIDADES
		for(int i = 0; i < pj.length; i++) {
			pj[i].dibujar(g2);
		}
		for(int i = 0; i < en.length; i++) {
			en[i].dibujar(g2);
		}
		//RETRATOS
		retratoProv(zona.get(32).getPos(), true);
		retratoProv(zona.get(6).getPos(), false);
		
	}
	
	public void cuadricula () {
		int x = 0;
		int y = 0;
		for(int i = 0; i < 6; i++) {
			
			for(int j = 0; j < 8; j++) {
				g2.setColor(Color.white);
				g2.drawRect(x, y, 96, 96);
				x += 96;
			}
			y += 96;
			x = 0;
		}		
	}
	
	public void cargarZonas() {
		int id = 0;
		int x = 0;
		int y = 0;
		for(int i = 0; i < 6; i++) {
			
			for(int j = 0; j < 8; j++) {
				zona.put(id, new Zona(new Point(x, y), pdj));
				x += 96;
				id++;
			}
			y += 96;
			x = 0;
		}
	}

	public void retratoProv(Point pos, boolean jugador) {
		if(jugador) {
			g2.setColor(Color.cyan);
		}
		else {
			g2.setColor(Color.red);
		}
		g2.drawRect((int)pos.getX(), (int)pos.getY(), pdj.tamañoDeBaldosa*4, pdj.tamañoDeBaldosa*4);
		g2.fillRect((int)pos.getX()+pdj.tamañoDeBaldosa, (int)pos.getY()+pdj.tamañoDeBaldosa, pdj.tamañoDeBaldosa*2, pdj.tamañoDeBaldosa*2);
	}
	
	public void cargarGrupos() {
		for(int i = 0; i < 8; i++) {
			pj[i] = new Personaje(true, pdj);
		}
		for(int i = 0; i < 8; i++) {
			en[i] = new Personaje(false, pdj);
		}
	}
	
}
