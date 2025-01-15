package unidades;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import principal.PanelDeJuego;
import principal.Zona;

public class CebadorDeMate extends Unidad{
	
	private int spHabilidad1;
	private Rectangle cubo = new Rectangle(0, 0, 50, 50);

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
		this.setDCRT(2);
		this.setEva(0.5);
		this.spHabilidad1 = 5;
	}
	
	public void realizarAccion(ArrayList<Unidad> enemigos, ArrayList<Unidad> aliados) {
		int accion = elegirAleatorio(4);
		if(accion <= 2 && puedeUsarHabilidad()) {
			usarHabilidadEnemigo(aliados);
			setSP(getSP() - spHabilidad1);
			
		}
		else {
			realizarAtaqueEnemigo(enemigos);
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
	
	public void usarHabilidadEnemigo(ArrayList<Unidad> unidades) {
		setHabilidadOn(true);
		Unidad unidad = elegirObjetivo(unidades);
		moverCubo(cubo, unidad);
		cebarMate(unidad);	
	}
	
	public void usarHabilidad(Unidad unidad) {
		setHabilidadOn(true);
		moverCubo(cubo, unidad);
		cebarMate(unidad);
	}
	
	public void cebarMate(Unidad unidad) {
		System.out.println("cebando un mate");
		int opcion = elegirAleatorio(5);
		System.out.println("es un mate "+opcion);
		if(opcion == 0) {
			//HERVIDO Y DULCE
			sumarHP(obtenerValorEntre(7,15));
			unidad.setAtqMod(unidad.getAtqMod() + obtenerValorEntre(3,7));
			unidad.setPcrtMod(unidad.getPcrtMod() + (0.05));
		}
		else if(opcion == 1) {
			//HERVIDO y AMARGO
			sumarHP(obtenerValorEntre(7,15));
			unidad.setDefMod(unidad.getDefMod() + obtenerValorEntre(1,3));
			unidad.setEvaMod(unidad.getEvaMod() + (0.10));
		}
		else if(opcion == 2) {
			//HELADO Y DULCE
			sumarSP(obtenerValorEntre(7,15));
			unidad.setAtqMod(unidad.getAtqMod() + obtenerValorEntre(3,7));
			unidad.setPcrtMod(unidad.getPcrtMod() + (0.05));
			
		}
		else if(opcion == 3) {
			//HELADO Y AMARGO
			sumarSP(obtenerValorEntre(7,15));
			unidad.setDefMod(unidad.getDefMod() + obtenerValorEntre(1,3));
			unidad.setEvaMod(unidad.getEvaMod() + (0.10));
		}
		else {
			//PERFECTO
			unidad.setHPMax(unidad.getHPMax()+(unidad.getHPMax()/10));
			unidad.setSPMax(unidad.getSPMax()+(unidad.getSPMax()/10));
			unidad.setHP(unidad.getHPMax());
			unidad.setSP(unidad.getSPMax());
			unidad.setAtqMod(unidad.getAtqMod() + obtenerValorEntre(3,7));
			unidad.setDefMod(unidad.getDefMod() + obtenerValorEntre(1,3));
			unidad.setPcrtMod(unidad.getPcrtMod() + (0.05));
			unidad.setEvaMod(unidad.getEvaMod() + 0.10);
			
		}
	}
	
	public void sumarHP(int hp) {
		int i = hp + this.getHP();
		if(i > this.getHPMax()) {
			this.setHP(this.getHPMax());
		}
		else {
			this.setHP(i);
		}
	}
	
	public void sumarSP(int sp) {
		int i = sp + this.getSP();
		if(i > this.getSPMax()) {
			this.setSP(this.getSPMax());
		}
		else {
			this.setSP(i);
		}
	}
	
	public void dibujarEfecto(Graphics2D g2) {
		g2.setColor(Color.pink);
		g2.fillRect(cubo.x, cubo.y, cubo.width, cubo.height);
		
	}
	
	public void moverCubo(Rectangle cubo, Unidad unidad) {
		pdj.ReproducirSE(5);
		cubo.setLocation(unidad.getPosX()+25, unidad.getPosY()+25);
	}

}
