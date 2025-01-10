package unidades;

import java.util.ArrayList;
import principal.PanelDeJuego;
import principal.Zona;

public class Elite extends Unidad{

	public Elite(Zona zona, boolean aliado,int idFaccion, PanelDeJuego pdj) {
		super(zona, aliado, idFaccion, pdj);
		this.setNombre("Elite");
		this.setClase(nombrarClase(idFaccion));
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
	
	public String nombrarClase(int id) {
		String clase;
		if(id == 0) {
			clase = "Guardian Del Halcon";
		}
		else if(id == 1) {
			clase = "Gran Caudillo";
		}
		else if(id == 2) {
			clase = "Dragon Supremo";
		}
		else if(id == 3) {
			clase = "Puño Divino";
		}
		else if(id == 4) {
			clase = "Reina Del Loto";
		}
		else if(id == 5) {
			clase = "Heredero De Jade";
		}
		else {
			clase = "Lider De Pandilla";
		}
		return clase;
	}

}