package unidades;

import java.awt.Color;
import java.awt.Graphics2D;

import principal.PanelDeJuego;
import principal.Zona;

public class Recluta extends Unidad{

	public Recluta(Zona zona, boolean aliado,int idFaccion, PanelDeJuego pdj) {
		super(zona, aliado,idFaccion, pdj);
		this.setNombre("Recluta");
		this.setClase(nombrarClase(idFaccion));
		this.setHP(30);
		this.setHPMax(30);
		this.setSP(0);
		this.setSPMax(0);
		this.setAtq(7);
		this.setDef(2);
		this.setPCRT(0);
	}
	
	public String nombrarClase(int id) {
		String clase;
		if(id == 0) {
			clase = "Polluelo Blanco";
		}
		else if(id == 1) {
			clase = "Cebador De Mate";
		}
		else if(id == 2) {
			clase = "Cheongsam Lover";
		}
		else if(id == 3) {
			clase = "Puño inexperto";
		}
		else if(id == 4) {
			clase = "Novata Introvertida";
		}
		else if(id == 5) {
			clase = "Malcriado Opulento";
		}
		else {
			clase = "Delincuente Juvenil";
		}
		return clase;
	}

}
