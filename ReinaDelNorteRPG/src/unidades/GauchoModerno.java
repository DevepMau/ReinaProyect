package unidades;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import principal.PanelDeJuego;
import principal.Zona;

public class GauchoModerno extends Unidad {
	
	private String[] habilidades = new String[1];

	public GauchoModerno(Zona zona, boolean aliado, PanelDeJuego pdj) {
		super(zona, aliado, pdj);
		this.setNombre("");
		this.setClase("Gaucho Moderno");
		this.setHPMax(obtenerValorEntre(60,90));
		this.setHP(this.getHPMax());
		this.setSP(0);
		this.setSPMax(0);
		this.setAtq(obtenerValorEntre(13,17));
		this.setDef(obtenerValorEntre(5,9));
		this.setPCRT(0.15);
		this.setDCRT(1.5);
		this.setEva(0);
		this.setVel(obtenerValorEntre(5,10));
		this.habilidades[0] = "SALDAR DEUDA";
	}
	
	public void realizarAtaque(Unidad unidad) {
		boolean isCritical = Math.random() <= (this.getPCRT() + this.getPcrtMod());
		if(unidad != null) {
			boolean isMiss = Math.random() <= (unidad.getEva() + unidad.getEvaMod());	
			this.setAtqMod(this.getAtqMod() + (this.getVidaPerdida()/8));
			int daño = Math.max(1, (this.getAtq() + this.getAtqMod()) - (unidad.getDef() + unidad.getDefMod())); 	 
			if (isCritical) {
				daño *= (this.getDCRT() + this.getDcrtMod());
			}
			if(!isMiss) {
				unidad.recibirDaño(daño, isCritical);
				this.setPcrtMod(this.getPcrtMod()+0.05);
			}
			else {
				unidad.evadirAtaque();
			}
		}
	}
	
	public void realizarAtaqueEnemigo(ArrayList<Unidad> unidades) {
		Unidad objetivo = elegirObjetivo(unidades);
		boolean isCritical = Math.random() <= (this.getPCRT() + this.getPcrtMod());   
		if(objetivo != null) {
			boolean isMiss = Math.random() <= (objetivo.getEva() + objetivo.getEvaMod());
			this.setAtqMod(this.getAtqMod() + (this.getVidaPerdida()/10));
			int daño = Math.max(1, (this.getAtq() + this.getAtqMod()) - (objetivo.getDef() + objetivo.getDefMod()));
	    	 
			if (isCritical) {
				daño *= (this.getDCRT() + this.getDcrtMod());
			}
			if(!isMiss) {
				objetivo.recibirDaño(daño, isCritical);
				this.setPcrtMod(this.getPcrtMod()+0.05);
			}
			else {
				objetivo.evadirAtaque();
			}
		}
	}
	
	public String[] getHabilidades() {
		return habilidades;
	}

	public void setHabilidades(String[] habilidades) {
		this.habilidades = habilidades;
	}
}
