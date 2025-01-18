package unidades;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import principal.PanelDeJuego;
import principal.Zona;

public class CebadorDeMate extends Unidad{
	
	private int spHabilidad1;
	private Rectangle cubo = new Rectangle(0, 0, 50, 50);
	private String[] habilidades = new String[1];

	public CebadorDeMate(Zona zona, boolean aliado, PanelDeJuego pdj) {
		super(zona, aliado, pdj);
		this.setNombre("");
		this.setClase("Cebador De Mate");
		this.setIdFaccion(1);
		this.setHPMax(obtenerValorEntre(40,60));
		this.setHP(this.getHPMax());
		this.setSPMax(obtenerValorEntre(25,50));
		this.setSP(this.getSPMax());
		this.setAtq(obtenerValorEntre(8,12));
		this.setDef(obtenerValorEntre(1,5));
		this.setPCRT(0);
		this.setDCRT(1.5);
		this.setEva(0);
		this.setVel(obtenerValorEntre(10,20));
		this.spHabilidad1 = 7;
		this.habilidades[0] = "CEBAR UN MATE";
		
	}
	
	public void realizarAccion(ArrayList<Unidad> enemigos, ArrayList<Unidad> aliados) {
		int accion = elegirAleatorio(4);
		if(accion <= 2 && cumpleLosRequisitos()) {
			this.setHabilidadElegida(0);
			usarHabilidadEnemigo(aliados);
			this.setSP(this.getSP() - this.spHabilidad1);
			
		}
		else {
			realizarAtaqueEnemigo(enemigos);
		}
	}
	
	public boolean cumpleLosRequisitos() {
		if(this.getSP() > 0) {
			if(this.getSP() >= this.spHabilidad1) {
				return true;
			}
		}
		return false;
	}
	
	public void usarHabilidadEnemigo(ArrayList<Unidad> unidades) {
		if(this.getHabilidadElegida() == 0) {
			setHabilidadOn(true);
			Unidad unidad = elegirObjetivo(unidades);
			moverCubo(cubo, unidad);
			cebarMate(unidad);	
		}
	}
	
	public void usarHabilidad(Unidad unidad) {
		setHabilidadOn(true);
		moverCubo(cubo, unidad);
		cebarMate(unidad);
		this.setSP(this.getSP() - this.spHabilidad1);
	}
	
	public void cebarMate(Unidad unidad) {
		int opcion = elegirAleatorio(5);
		unidad.setEnSacudida(true);
		unidad.setDuracionSacudida(20);
		unidad.setIdMate(opcion);
		unidad.setEsMate(true);
		if(opcion == 0) {
			//HERVIDO Y DULCE
			sumarHP(unidad, obtenerValorEntre(7,15));
			unidad.setAtqMod(unidad.getAtqMod() + obtenerValorEntre(3,7));
			unidad.setPcrtMod(unidad.getPcrtMod() + (0.05));
		}
		else if(opcion == 1) {
			//HERVIDO y AMARGO
			sumarHP(unidad, obtenerValorEntre(7,15));
			unidad.setDefMod(unidad.getDefMod() + obtenerValorEntre(1,3));
			unidad.setEvaMod(0.25);
		}
		else if(opcion == 2) {
			//HELADO Y DULCE
			if(unidad.getSPMax() > 0) {
				sumarSP(unidad, obtenerValorEntre(7,15));
			}
			unidad.setAtqMod(unidad.getAtqMod() + obtenerValorEntre(3,7));
			unidad.setPcrtMod(unidad.getPcrtMod() + (0.05));
			
		}
		else if(opcion == 3) {
			//HELADO Y AMARGO
			if(unidad.getSPMax() > 0) {
				sumarSP(unidad, obtenerValorEntre(7,15));
			}
			unidad.setDefMod(unidad.getDefMod() + obtenerValorEntre(1,3));
			unidad.setEvaMod(0.25);
		}
		else {
			//PERFECTO
			unidad.setHPMax(unidad.getHPMax()+(unidad.getHPMax()/10));
			sumarHP(unidad, obtenerValorEntre(15,23));
			if(unidad.getSPMax() > 0) {
				unidad.setSPMax(unidad.getSPMax()+(unidad.getSPMax()/10));
				sumarSP(unidad, obtenerValorEntre(15,23));
			}
			unidad.setAtqMod(unidad.getAtqMod() + obtenerValorEntre(3,7));
			unidad.setDefMod(unidad.getDefMod() + obtenerValorEntre(1,3));
			unidad.setPcrtMod(unidad.getPcrtMod() + (0.05));
			unidad.setEvaMod(0.25);
			
		}
	}
	
	public void sumarHP(Unidad unidad, int hp) {
		int i = hp + unidad.getHP();
		if(i > unidad.getHPMax()) {
			unidad.setHP(unidad.getHPMax());
		}
		else {
			unidad.setHP(i);
		}
	}
	
	public void sumarSP(Unidad unidad, int sp) {
		int i = sp + unidad.getSP();
		if(i > unidad.getSPMax()) {
			unidad.setSP(unidad.getSPMax());
		}
		else {
			unidad.setSP(i);
		}
	}
	
	public void establecerTipoDeaccion() {
		if(this.getHabilidadElegida() == 0) {
			this.setAccion("APOYAR");
		}
		else {
			this.setAccion("");
		}
	}
	
	public void moverCubo(Rectangle cubo, Unidad unidad) {
		pdj.ReproducirSE(5);
		cubo.setLocation(unidad.getPosX()+25, unidad.getPosY()+25);
	}

	public String[] getHabilidades() {
		return habilidades;
	}

	public void setHabilidades(String[] habilidades) {
		this.habilidades = habilidades;
	}

}
