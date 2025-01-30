package unidades;

import java.util.ArrayList;
import principal.PanelDeJuego;
import principal.Zona;

public class PuñoFurioso extends Unidad {
	
	private int spHabilidad1;
	private String[] listaDeHabilidades = new String[1];

	public PuñoFurioso(Zona zona, boolean aliado, PanelDeJuego pdj) {
		super(zona, aliado, pdj);
		this.setNombre("");
		this.setTipo("Combatiente");
		this.setClase("Puño Furioso");
		this.setIdFaccion(3);
		this.setHPMax(obtenerValorEntre(70,100));
		this.setHP(this.getHPMax());
		this.setSPMax(obtenerValorEntre(20,30));
		this.setSP(this.getSPMax());
		this.setAtq(obtenerValorEntre(11,16));
		this.setDef(obtenerValorEntre(5,8));
		this.setPCRT(0.25);
		this.setDCRT(1.5);
		this.setEva(0.25);
		this.setVel(obtenerValorEntre(12,17));
		this.spHabilidad1 = 10;
		this.listaDeHabilidades[0] = "DOBLE IMPACTO";
		this.generarCuerpo();
	}
	//METODO PRINCIPAL//////////////////////////////////////////////////////////////////
	public void realizarAccion(ArrayList<Unidad> enemigos, ArrayList<Unidad> aliados) {
		if(cumpleReqDeHab1()) {
			this.setHabilidadElegida(0);
			usarHabilidadEnemigo(enemigos);	
		}
		else {
			realizarAtaqueEnemigo(enemigos);
		}
		this.pasivaDeClase(aliados, enemigos);
	}
	public void recibirDaño(int daño, boolean isCritical, int duracionSacudida) {
		super.recibirDaño(daño, isCritical, duracionSacudida);
		this.restaurarSP(daño/2);
	}
	//METODOS ENEMIGO////////////////////////////////////////////////////////////////////
	public void usarHabilidadEnemigo(ArrayList<Unidad> unidades) {
		Unidad objetivo = elegirObjetivoConMayorHP(unidades);
		dobleGolpe(objetivo);
	}
	//METODOS JUGADOR////////////////////////////////////////////////////////////////////
	public void usarHabilidad(Unidad unidad, ArrayList<Unidad> unidades) {
		dobleGolpe(unidad);
		this.setSP(this.getSP() - this.spHabilidad1);
	}
	//HABILIDADES////////////////////////////////////////////////////////////////////////
	public void dobleGolpe(Unidad unidad) {
		boolean isCritical = Math.random() <= (this.getPCRT() + this.getPcrtMod());   
		if(unidad != null) {
			int daño = Math.max(1, (this.getAtq() + this.getAtqMod()) - (unidad.getDef() + unidad.getDefMod()));
	    	if(this.getHPMax() < unidad.getHPMax()) {
	    		daño += (unidad.getHPMax()/10);
	    	}
			if (isCritical) {
				daño *= (this.getDCRT() + this.getDcrtMod());
			}
			unidad.recibirGolpesMúltiples(daño, 2, isCritical);
			this.reflejarDaño(unidad, daño);
			this.robarVida(daño, unidad);
		}
	}
	//METODOS AUXILIARES/////////////////////////////////////////////////////////////////
	public Unidad elegirObjetivoConMayorHP(ArrayList<Unidad> unidades) {
		Unidad unidadSeleccionada = null;
	    int mayorPorcentajeHP = Integer.MIN_VALUE;
	    for (Unidad unidad : unidades) {
	    	int HPUnidad = unidad.getHPMax();
	        if (HPUnidad > mayorPorcentajeHP) {
	        	mayorPorcentajeHP = HPUnidad;
	            unidadSeleccionada = unidad;
	        }
	    }
	    return unidadSeleccionada;
	}
	public boolean cumpleReqDeHab1() {
		if(this.getSP() > 0) {
			if(this.getSP() >= this.spHabilidad1) {
				return true;
			}
		}
		return false;
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
