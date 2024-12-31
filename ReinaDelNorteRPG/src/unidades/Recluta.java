package unidades;

import java.awt.Color;
import java.awt.Graphics2D;

import principal.PanelDeJuego;
import principal.Zona;

public class Recluta extends Unidad{

	public Recluta(Zona zona, boolean aliado, PanelDeJuego pdj) {
		super(zona, aliado, pdj);
		this.setNombre("Recluta");
		this.setHP(30);
		this.setHPMax(30);
		this.setSP(0);
		this.setSPMax(0);
		this.setAtq(7);
		this.setDef(2);
		this.setPCRT(0);
	}

}
