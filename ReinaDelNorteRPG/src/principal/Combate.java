package principal;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.HashMap;

public class Combate {
	
	PanelDeJuego pdj;
	Graphics2D g2;
	HashMap<Integer, Zona> zona = new HashMap<>();
	Personaje[] pj = new Personaje[16];
	public int numeroOpcion = 0;
	public int opcionElegida = -1;
	public boolean FinTurno = false;
	
	public Combate(PanelDeJuego pdj) {
		this.pdj = pdj;
		cargarZonas();
		cargarGrupo();
	}
	
	public void actualizar() {
		if(opcionElegida != -1) {
			pj[opcionElegida].reset();
			pj[opcionElegida].accionAtaque(pj[3]);
			if(!pj[opcionElegida].enTurno) {
				opcionElegida = -1;
			}
		}
	}
	
	public void dinujar(Graphics2D g2) {
		this.g2 = g2;
		//UNIDADES
		for(int i = 0; i < pj.length; i++) {
			if(pj[i] != null) {
				pj[i].dibujar(g2);
			}
		}
		//RETRATOS
		retratoProv(zona.get(32).getPos(), true);
		retratoProv(zona.get(6).getPos(), false);
		
		if(opcionElegida == -1) {
			cuadroDeSeleccion(pj[numeroOpcion]);
		}
		//System.out.println(zona.get(36).getPos());
		
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
	
	public void cuadroDeSeleccion(Personaje e) {
		g2.setColor(Color.yellow);
		g2.drawRect(e.zonaInicio.getX(), e.zonaInicio.getY(), pdj.tamañoDeBaldosa*2, pdj.tamañoDeBaldosa*2);
	}
	
	public void cargarGrupos() {
		int id = 34;
		for(int i = 0; i < 8; i++) {
			pj[i] = new Personaje(zona.get(id), true, pdj);
			if(i == 3) {
				id += 5;
			}
			else {
				id++;
			}
		}
		id = 2;
		for(int i = 0; i < 8; i++) {
			//en[i] = new Personaje(zona.get(id), false, pdj);
			if(i == 3) {
				id += 5;
			}
			else {
				id++;
			}
		}
	}
	
	public void cargarGrupo() {
		int cantPJ = 3;
		int cantEn = 5;
		
		int id = 34;
		for(int i = 0; i < cantPJ; i++) {
			pj[i] = new Personaje(zona.get(id), true, pdj);
			if(i == 3) {
				id += 5;
			}
			else {
				id++;
			}
		}
		id = 2;
		for(int i = 0; i < cantEn; i++) {
			pj[i+cantPJ] = new Personaje(zona.get(id), false, pdj);
			if(i == 3) {
				id += 5;
			}
			else {
				id++;
			}
		}
	}
	
}
