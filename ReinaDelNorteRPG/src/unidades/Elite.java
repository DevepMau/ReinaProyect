package unidades;

import java.util.ArrayList;
import principal.PanelDeJuego;
import principal.Zona;

public class Elite extends Unidad{

	public Elite(Zona zona, boolean aliado, PanelDeJuego pdj) {
		super(zona, aliado, pdj);
		this.setNombre("Elite");
		this.setHP(70);
		this.setHPMax(70);
		this.setSP(20);
		this.setSPMax(20);
		this.setAtq(15);
		this.setDef(10);
		this.setPCRT(0.5);
	}
	
	public Unidad elegirObjetivo(ArrayList<Unidad> unidades) {
	    Unidad unidadSeleccionada = null;
	    int menorHp = Integer.MAX_VALUE;

	    for (Unidad unidad : unidades) {
	        if (unidad.getHP() < menorHp) {
	            menorHp = unidad.getHP();
	            unidadSeleccionada = unidad;
	        }
	    }
	    return unidadSeleccionada;
	}

}