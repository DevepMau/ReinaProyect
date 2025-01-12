package unidades;

import java.util.ArrayList;
import principal.PanelDeJuego;
import principal.Zona;

public class Elite extends Unidad{

	public Elite(Zona zona, boolean aliado,int idFaccion, PanelDeJuego pdj) {
		super(zona, aliado, idFaccion, pdj);
		this.setNombre("Elite");
		this.setClase(nombrarClase(idFaccion));
		this.setHPMax(obtenerValorEntre(100,150));
		this.setHP(this.getHPMax());
		this.setSPMax(obtenerValorEntre(30,70));
		this.setSP(this.getSPMax());
		this.setAtq(obtenerValorEntre(18,25));
		this.setDef(obtenerValorEntre(10,13));
		this.setPCRT(0.5);
		this.setDCRT(2);
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
			clase = "Heroe Federal";
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