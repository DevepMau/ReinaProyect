package unidades;

import java.awt.Color;
import java.awt.Graphics2D;

import principal.PanelDeJuego;
import principal.Zona;

public class Soldado extends Unidad {

	public Soldado(Zona zona, boolean aliado, PanelDeJuego pdj) {
		super(zona, aliado, pdj);
		this.setNombre("Soldado");
		this.setHP(45);
		this.setHPMax(45);
		this.setSP(0);
		this.setSPMax(0);
		this.setAtq(10);
		this.setDef(5);
		this.setPCRT(0.3);
	}

}
