package unidades;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import principal.PanelDeJuego;
import principal.Zona;

public class PayadorTartamudo extends Unidad {
	
	private int spHabilidad1;

	public PayadorTartamudo(Zona zona, boolean aliado, PanelDeJuego pdj) {
		super(zona, aliado, pdj);
		this.setNombre("Especialista");
		this.setClase("Payador Tartamudo");
		this.setHPMax(obtenerValorEntre(40,70));
		this.setHP(this.getHPMax());
		this.setSPMax(obtenerValorEntre(60,100));
		this.setSP(this.getSPMax());
		this.setAtq(obtenerValorEntre(8,12));
		this.setDef(obtenerValorEntre(3,7));
		this.setPCRT(0);
		this.spHabilidad1 = 10;
	}
	
	public void realizarAccion(ArrayList<Unidad> enemigos, ArrayList<Unidad> aliados) {
		int accion;
		if(aliadosHeridos(aliados)) {
			if(aliadoBajoDeHP(aliados)) {
				accion = 0;
			}
			else{
				accion = elegirAleatorio(4);
			}
			if(accion == 0 && puedeUsarHabilidad()) {
				usarHabilidad(aliados);
				this.setSP(this.getSP() - this.spHabilidad1);
			}
			else {
				realizarAtaqueEnemigo(enemigos);
			}
		}
		else {
			realizarAtaqueEnemigo(enemigos);
		}
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
	
	public void usarHabilidad(ArrayList<Unidad> unidades) {
		if(unidades.size() > 1) {
			Unidad objetivo = elegirObjetivo(unidades);
			objetivo.restaurarHP(objetivo.getHPMax()/3);
		}
	}
	
	public boolean puedeUsarHabilidad() {
		if(this.getSP() > 0) {
			if(this.getSP() >= this.spHabilidad1) {
				return true;
			}
		}
		return false;
	}
	
	public boolean aliadoBajoDeHP(ArrayList<Unidad> unidades) {
		for (Unidad unidad : unidades) {
			if(unidad.getHP() <= unidad.getHPMax()/2) {
				return true;
			}
		}
		return false;
	}
	
	public boolean aliadosHeridos(ArrayList<Unidad> unidades) {
		for (Unidad unidad : unidades) {
			if(unidad.getHP() < unidad.getHPMax()) {
				return true;
			}
		}
		return false;
	}
	
	public String nombrarClase(int id) {
		String clase;
		if(id == 0) {
			clase = "Ave De Presa";
		}
		else if(id == 1) {
			clase = "Payador Tartamudo";
		}
		else if(id == 2) {
			clase = "Medichino Tradicional";
		}
		else if(id == 3) {
			clase = "Puño Astuto";
		}
		else if(id == 4) {
			clase = "Influencer Digital";
		}
		else if(id == 5) {
			clase = "Ballena De Los Gacha";
		}
		else {
			clase = "Estafador novato";
		}
		return clase;
	}

}