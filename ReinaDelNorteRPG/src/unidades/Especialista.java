package unidades;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import principal.PanelDeJuego;
import principal.Zona;

public class Especialista extends Unidad {
	
	int spHabilidad1;

	public Especialista(Zona zona, boolean aliado, PanelDeJuego pdj) {
		super(zona, aliado, pdj);
		this.nombre = "Especialista";
		this.hp = 45;
		this.hpMax = 45;
		this.sp = 80;
		this.spMax = 80;
		this.atq = 8;
		this.def = 4;
		this.pcrt = 0;
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
				this.sp -= this.spHabilidad1;
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
	        if (unidad.getHp() < menorHp) {
	            menorHp = unidad.getHp();
	            unidadSeleccionada = unidad;
	        }
	    }
	    return unidadSeleccionada;
	}
	
	public void usarHabilidad(ArrayList<Unidad> unidades) {
		if(unidades.size() > 1) {
			Unidad objetivo = elegirObjetivo(unidades);
			objetivo.restaurarHP(objetivo.hpMax/3);
		}
	}
	
	public boolean puedeUsarHabilidad() {
		if(this.sp > 0) {
			if(this.sp >= this.spHabilidad1) {
				return true;
			}
		}
		return false;
	}
	
	public boolean aliadoBajoDeHP(ArrayList<Unidad> unidades) {
		for (Unidad unidad : unidades) {
			if(unidad.hp <= unidad.hpMax/2) {
				return true;
			}
		}
		return false;
	}
	
	public boolean aliadosHeridos(ArrayList<Unidad> unidades) {
		for (Unidad unidad : unidades) {
			if(unidad.hp < unidad.hpMax) {
				return true;
			}
		}
		return false;
	}

}