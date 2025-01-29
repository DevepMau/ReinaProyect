package unidades;

import java.util.ArrayList;
import principal.PanelDeJuego;
import principal.Zona;

public class NiñoCheung extends Unidad {
	
	private String[] listaDeHabilidades = new String[1];

	public NiñoCheung(Zona zona, boolean aliado, PanelDeJuego pdj) {
		super(zona, aliado, pdj);
		this.setNombre("");
		this.setClase("Niño Cheung");
		this.setIdFaccion(3);
		this.setHPMax(obtenerValorEntre(60,90));
		this.setHP(this.getHPMax());
		this.setSP(0);
		this.setSPMax(0);
		this.setAtq(obtenerValorEntre(13,16));
		this.setDef(obtenerValorEntre(2,6));
		this.setPCRT(0.05);
		this.setDCRT(10);
		this.setEva(0.25);
		this.setVel(obtenerValorEntre(10,15));
		this.listaDeHabilidades[0] = "...";
		this.generarCuerpo();
	}
	//METODO PRINCIPAL//////////////////////////////////////////////////////////////////
	public void realizarAccion(ArrayList<Unidad> enemigos, ArrayList<Unidad> aliados) {
		realizarAtaqueEnemigo(enemigos);
	}
	//METODOS ENEMIGO////////////////////////////////////////////////////////////////////
	public void realizarAtaqueEnemigo(ArrayList<Unidad> unidades) {
		Unidad objetivo = elegirObjetivo(unidades);
		this.ataqueNormal(objetivo);
	}
	//METODOS JUGADOR////////////////////////////////////////////////////////////////////
	public void realizarAtaque(Unidad unidad) {
		this.ataqueNormal(unidad);
	}
	//HABILIDADES////////////////////////////////////////////////////////////////////////
	//METODOS AUXILIARES/////////////////////////////////////////////////////////////////
	public void ataqueNormal(Unidad objetivo) {
		boolean isCritical = Math.random() <= (this.getPCRT() + this.getPcrtMod());   
		if(objetivo != null) {
			boolean isMiss = Math.random() <= (objetivo.getEva() + objetivo.getEvaMod());
			int daño = Math.max(1, (this.getAtq() + this.getAtqMod()) - (objetivo.getDef() + objetivo.getDefMod()));
	    	 
			if (isCritical) {
				daño *= (this.getDCRT() + this.getDcrtMod());
				this.reflejarDaño(objetivo, daño/10);
			}
			else {
				this.reflejarDaño(objetivo, daño);
			}
			if(!isMiss) {
				objetivo.recibirDaño(daño, isCritical);
			}
			else {
				objetivo.evadirAtaque();
			}
			this.robarVida(daño, objetivo);
		}
	}
	public void configurarTipoDeaccion() {
		if(this.getHabilidadElegida() == 0) {
			this.setAccion("ATACAR");
		}
		else {
			this.setAccion("");
		}
	}
	public String[] getListaDeHabilidades() {return listaDeHabilidades;}
	public void setListaDeHabilidades(String[] habilidades) {this.listaDeHabilidades = habilidades;}
}
