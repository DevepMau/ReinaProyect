package unidades;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import principal.PanelDeJuego;
import principal.Zona;

public class Especialista extends Unidad {
	
	private int spHabilidad1;

	public Especialista(Zona zona, boolean aliado, PanelDeJuego pdj) {
		super(zona, aliado, pdj);
		this.setNombre("Especialista");
		this.setHP(45);
		this.setHPMax(45);
		this.setSP(80);
		this.setSPMax(80);
		this.setAtq(8);
		this.setDef(4);
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
				accion = elegirAleatorio();
			}
			if(accion == 0 && puedeUsarHabilidad()) {
				usarHabilidad(aliados);
				this.setSP(this.getSP() - this.spHabilidad1);
			}
			else {
				ataqueEnemigo(enemigos);
			}
		}
		else {
			ataqueEnemigo(enemigos);
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

}