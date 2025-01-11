package unidades;

import java.awt.Color;
import java.awt.Graphics2D;

import principal.PanelDeJuego;
import principal.Zona;

public class Combatiente extends Unidad {

	public Combatiente(Zona zona, boolean aliado,int idFaccion, PanelDeJuego pdj) {
		super(zona, aliado,idFaccion, pdj);
		this.setNombre("Soldado");
		this.setClase(nombrarClase(idFaccion));
		this.setHP(45);
		this.setHPMax(45);
		this.setSP(0);
		this.setSPMax(0);
		this.setAtq(10);
		this.setDef(5);
		this.setPCRT(0.3);
		this.setDCRT(2);
	}
	
	public String nombrarClase(int id) {
		String clase;
		if(id == 0) {
			clase = "Pichon Rebelde";
		}
		else if(id == 1) {
			clase = "Gaucho Moderno";
		}
		else if(id == 2) {
			clase = "Wushu Warrior";
		}
		else if(id == 3) {
			clase = "Puño Furioso";
		}
		else if(id == 4) {
			clase = "Hermana Confiable";
		}
		else if(id == 5) {
			clase = "Estudiante VIP";
		}
		else {
			clase = "Buscapleitos";
		}
		return clase;
	}

}
