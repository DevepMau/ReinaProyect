package unidades;

import java.util.ArrayList;
import principal.PanelDeJuego;
import principal.Zona;

public class Delegada extends Unidad {
	
	private String[] listaDeHabilidades = new String[1];

	public Delegada(Zona zona, boolean aliado, PanelDeJuego pdj) {
		super(zona, aliado, pdj);
		this.setNombre("");
		this.setTipo("Combatiente");
		this.setClase("Delegada");
		this.setIdFaccion(4);
		this.setGenero(0);
		this.setHPMax(obtenerValorEntre(40,70));
		this.setHP(this.getHPMax());
		this.setSP(0);
		this.setSPMax(0);
		this.setAtq(obtenerValorEntre(11,16));
		this.setDef(obtenerValorEntre(3,6));
		this.setPCRT(0);
		this.setDCRT(1.5);
		this.setEva(0);
		this.setVel(obtenerValorEntre(7,12));
		this.listaDeHabilidades[0] = "REGAÑAR";
		this.generarCuerpo();
	}
	//METODO PRINCIPAL//////////////////////////////////////////////////////////////////
	public void realizarAccion(ArrayList<Unidad> enemigos, ArrayList<Unidad> aliados) {
		if(this.hayQueReportar(enemigos)) {
			usarHabilidadEnemigo(enemigos);
		}
		else {
			realizarAtaqueEnemigo(enemigos);
		}
		this.pasivaDeClase(aliados, enemigos);
	}
	//METODOS ENEMIGO////////////////////////////////////////////////////////////////////
	public void usarHabilidadEnemigo(ArrayList<Unidad> unidades) {
		Unidad objetivo = this.elegirObjetivoCon3Marcas(unidades);
		if(objetivo != null) {
			reportar(objetivo);
		}
	}
	//METODOS JUGADOR////////////////////////////////////////////////////////////////////
	public void usarHabilidad(Unidad unidad, ArrayList<Unidad> unidades) {
		if(unidad != null) {
			reportar(unidad);
		}
	}
	//HABILIDADES////////////////////////////////////////////////////////////////////////
	public void reportar(Unidad unidad) {
		if(unidad != null) {
			if(unidad.getFaltasCometidas() >= 3) {
				unidad.recibirDaño((unidad.getAtq() + unidad.getAtqMod()), false, 20);
				unidad.setEstaDebilitado(true);
				unidad.setAtqMod(unidad.getAtqMod() - obtenerValorEntre(2,4));
				unidad.setPcrtMod(unidad.getPcrtMod() - 0.05);
				unidad.setEvaMod(unidad.getEvaMod() - 0.05);
				unidad.setFaltasCometidas(0);
				pdj.ReproducirSE(3);
			}
			else if(unidad.getFaltasCometidas() == 2) {
				unidad.recibirDaño((unidad.getAtq() + unidad.getAtqMod())/2, false, 20);
				unidad.setFaltasCometidas(0);
				pdj.ReproducirSE(3);
			}
			else if(unidad.getFaltasCometidas() == 1){
				unidad.recibirDaño((unidad.getAtq() + unidad.getAtqMod())/3, false, 20);
				unidad.setEstaDebilitado(true);
				unidad.setFaltasCometidas(0);
				pdj.ReproducirSE(3);
			}
			else {
				unidad.setFaltasCometidas(unidad.getFaltasCometidas() + 1);
			}
		}
	}
	//METODOS AUXILIARES/////////////////////////////////////////////////////////////////
	public Unidad elegirObjetivoCon3Marcas(ArrayList<Unidad> unidades) {
		if(!unidades.isEmpty()) {
			for(Unidad unidad : unidades) {
				if(unidad.getFaltasCometidas() >= 3) {
					return unidad;
				}
			}
		}
		return unidades.get(0);
		
	}
	public boolean hayQueReportar(ArrayList<Unidad> unidades) {
		if(!unidades.isEmpty()) {
			for(Unidad unidad : unidades) {
				if(unidad.getFaltasCometidas() >= 3) {
					return true;
				}
			}
		}
		return false;
	}
	public boolean cumpleReqDeHab1() {
		return true;
	}
	public void configurarTipoDeaccion() {
		if(this.getHabilidadElegida() == 0) {
			this.setObjetivoUnico(true);
			this.setAccion("ATACAR");
		}
		else {
			this.setAccion("");
		}
	}	
	public String[] getListaDeHabilidades() {return listaDeHabilidades;}
	public void setListaDeHabilidades(String[] habilidades) {this.listaDeHabilidades = habilidades;}
}
