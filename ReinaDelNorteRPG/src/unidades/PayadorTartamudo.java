package unidades;

import java.util.ArrayList;
import principal.PanelDeJuego;
import principal.Zona;

public class PayadorTartamudo extends Unidad {
	
	private String[] habilidades = new String[2];
	private int spHabilidad1;
	private int spHabilidad2;

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
		this.setVel(obtenerValorEntre(8,15));
		this.setPCRT(0);
		this.spHabilidad1 = 10;
		this.spHabilidad2 = 30;
		this.habilidades[0] = "CHICANEAR";
		this.habilidades[1] = "MOTIVAR";
	}
	//METODO PRINCIPAL//////////////////////////////////////////////////////////////////
	public void realizarAccion(ArrayList<Unidad> enemigos, ArrayList<Unidad> aliados) {
		int accion = elegirAleatorio(5);
		if(accion <= 3 && cumpleReqDeHab1()) {
			int nroHabilidad = elegirAleatorio(4);
			if(nroHabilidad == 0 &&  cumpleReqDeHab2()) {
				this.setHabilidadElegida(1);
			}
			else {
				this.setHabilidadElegida(0);
			}
			usarHabilidadEnemigo(aliados, enemigos);
			
		}
		else {
			realizarAtaqueEnemigo(enemigos);
		}
	}
	//METODOS ENEMIGO////////////////////////////////////////////////////////////////////
	public void realizarAtaqueEnemigo(ArrayList<Unidad> unidades) {
		Unidad objetivo = elegirObjetivo(unidades);
		boolean isCritical = Math.random() <= (this.getPCRT() + this.getPcrtMod());  
		if(objetivo != null) {
			boolean isMiss = Math.random() <= (objetivo.getEva() + objetivo.getEvaMod());
			int daño = Math.max(1, (this.getAtq() + this.getAtqMod()) - (objetivo.getDef() + objetivo.getDefMod()));
	    	 
			if (isCritical) {
				daño *= (this.getDCRT() + this.getDcrtMod());
			}
			if(!isMiss) {
				objetivo.recibirDaño(daño, isCritical);
			}
			else {
				objetivo.evadirAtaque();
			}
		}
	}
	public void usarHabilidadEnemigo(ArrayList<Unidad> aliados, ArrayList<Unidad> enemigos) {
		if(this.getHabilidadElegida() == 0) {
			if(!enemigos.isEmpty()) {
				this.setSP(this.getSP() - this.spHabilidad1);
				Unidad unidad = elegirObjetivo(enemigos);
				chicanear(unidad);
			}
		}
		else {
			if(!aliados.isEmpty()) {
				this.setSP(this.getSP() - this.spHabilidad2);
				for(Unidad unidad : aliados) {
					motivar(unidad);
				}
			}
		}
	}
	//METODOS JUGADOR////////////////////////////////////////////////////////////////////
	public void realizarAtaque(Unidad unidad) {
		boolean isCritical = Math.random() <= (this.getPCRT() + this.getPcrtMod());
		if(unidad != null) {
			boolean isMiss = Math.random() <= (unidad.getEva() + unidad.getEvaMod());	 
			int daño = Math.max(1, (this.getAtq() + this.getAtqMod()) - (unidad.getDef() + unidad.getDefMod())); 	 
			if (isCritical) {
				daño *= (this.getDCRT() + this.getDcrtMod());
			}
			if(!isMiss) {
				unidad.recibirDaño(daño, isCritical);
				this.setSP(this.getSP() + daño*3);
				this.setEstaGanandoSP(true);
			}
			else {
				unidad.evadirAtaque();
			}
		}
	}
	public void usarHabilidad(Unidad unidad, ArrayList<Unidad> unidades) {	
		if(this.getHabilidadElegida() == 0) {
			chicanear(unidad);
		}
		else {
			this.setSP(this.getSP() - this.spHabilidad2);
			for(Unidad unidadObjetivo : unidades) {
				motivar(unidadObjetivo);
			}	
		}
		
	}
	//HABILIDADES////////////////////////////////////////////////////////////////////////
	public void chicanear(Unidad unidad) {
		this.setSP(this.getSP() - this.spHabilidad1);
		int daño = this.getAtq()+this.getAtqMod()+(this.getSPMax() / 20);
		if(unidad != null) {
			pdj.ReproducirSE(8);
			unidad.recibirDaño(daño, false);
			unidad.setearSacudida(true);
			unidad.setDuracionSacudida(20);
			unidad.setEsUnaHabilidad(true);
			unidad.setVelMod(unidad.getVelMod() - 3);
			unidad.setDefMod(unidad.getDefMod() - obtenerValorEntre(1,3));
		}
	}
	public void motivar(Unidad unidad) {
		if(unidad != null) {
			pdj.ReproducirSE(7);
			unidad.setearSacudida(true);
			unidad.setDuracionSacudida(20);
			unidad.setEstaMotivado(true);
			unidad.setVelMod(unidad.getVelMod() + obtenerValorEntre(1,5));
			unidad.setAtqMod(unidad.getAtqMod() + obtenerValorEntre(1,5));
		}
	}
	//METODOS AUXILIARES/////////////////////////////////////////////////////////////////
	public void configurarTipoDeaccion() {
		if(this.getHabilidadElegida() == 0) {
			this.setAccion("ATACAR");
			this.setObjetivoUnico(true);
		}
		else {
			this.setAccion("APOYAR");
			this.setObjetivoUnico(false);
		}
	}
	public boolean cumpleReqDeHab1() {
		if(this.getSP() > 0) {
			if(this.getSP() >= this.spHabilidad1) {
				return true;
			}
		}
		return false;
	}
	public boolean cumpleReqDeHab2() {
		if(this.getSP() > 0) {
			if(this.getSP() >= this.spHabilidad2) {
				return true;
			}
		}
		return false;
	}
	public String[] getListaDeHabilidades() {return habilidades;}
	public void setListaDeHabilidades(String[] habilidades) {this.habilidades = habilidades;}
}