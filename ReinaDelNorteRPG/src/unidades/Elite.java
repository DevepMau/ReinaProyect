package unidades;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Comparator;

import principal.PanelDeJuego;
import principal.Zona;

public class Elite extends Unidad{

	public Elite(Zona zona, boolean aliado, PanelDeJuego pdj) {
		super(zona, aliado, pdj);
		this.nombre = "Elite";
		this.hp = 70;
		this.hpMax = 70;
		this.sp = 20;
		this.spMax = 20;
		this.atq = 15;
		this.def = 10;
		this.pcrt = 0.5;
	}
	
	public Unidad elegirObjetivo(ArrayList<Unidad> unidades) {
	    Unidad unidadSeleccionada = null;
	    int menorHp = Integer.MAX_VALUE;

	    for (Unidad unidad : unidades) {
	        if (unidad.getHp() < menorHp) {
	            menorHp = unidad.getHp();
	            unidadSeleccionada = unidad;
	        }
	    }
	    return unidadSeleccionada;
	}

}