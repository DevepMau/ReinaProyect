package principal;

import java.awt.Graphics2D;
import java.util.HashMap;

public class Combate {
	PanelDeJuego pdj;
	Graphics2D g2;
	HashMap<Integer, Zona> zona = new HashMap<>();
	
	public Combate(PanelDeJuego pdj) {
		this.pdj = pdj;
		crearTablero();
		
	}
	public void actualizar() {}
	
	public void dibujar(Graphics2D g2) {
		this.g2 = g2;
		for (Zona zona : zona.values()) {
			zona.dibujar(g2);
		}
	}
	
	public void crearTablero() {
		int x = 0;
		int y = 0;
		int id = 0;
		for (int i = 0; i < 6; i++) {
			for (int k = 0; k < 8; k++) {
				zona.put(id, new Zona(x, y, pdj));
				id++;
				x += pdj.tamañoDeBaldosa*2;
			}
			x = 0;
			y += pdj.tamañoDeBaldosa*2;
		}
	}

}
