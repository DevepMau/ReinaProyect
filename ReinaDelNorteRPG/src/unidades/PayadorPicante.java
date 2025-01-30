package unidades;

import java.util.ArrayList;
import principal.PanelDeJuego;
import principal.Zona;

public class PayadorPicante extends Unidad {
	
	private String[] habilidades = new String[2];
	private int spHabilidad1;
	private int spHabilidad2;

	public PayadorPicante(Zona zona, boolean aliado, PanelDeJuego pdj) {
		super(zona, aliado, pdj);
		this.setNombre("");
		this.setTipo("Especialista");
		this.setClase("Payador Picante");
		this.setIdFaccion(1);
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
		this.generarCuerpo();
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
		this.pasivaDeClase(aliados, enemigos);
	}
	//METODOS ENEMIGO////////////////////////////////////////////////////////////////////
	public void usarHabilidadEnemigo(ArrayList<Unidad> aliados, ArrayList<Unidad> enemigos) {
		if(this.getHabilidadElegida() == 0) {
			if(!enemigos.isEmpty()) {
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
		int daño = (this.getAtq()+this.getAtqMod()+(this.getSPMax() / 20));
		if(unidad != null) {
			this.setSP(this.getSP() - this.spHabilidad1);
			pdj.ReproducirSE(3);
			unidad.recibirDaño(daño, false, 20);
			unidad.setearSacudida(true);
			unidad.setDuracionSacudida(20);
			unidad.setEstaDesmotivado(true);
			unidad.setVelMod(unidad.getVelMod() - obtenerValorEntre(1,5));
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