package unidades;

import java.util.ArrayList;

import principal.Habilidades;
import principal.PanelDeJuego;
import principal.Zona;

public class Influencer extends Unidad {
	
	private String[] habilidades = new String[2];
	private int spHabilidad1;
	private int spHabilidad2;

	public Influencer(Zona zona, boolean aliado, PanelDeJuego pdj) {
		super(zona, aliado, pdj);
		this.setNombre("");
		this.setTipo("Especialista");
		this.setClase("Influencer");
		this.setGenero(0);
		this.setIdFaccion(4);
		this.setHPMax(obtenerValorEntre(180,250));
		this.setHP(this.getHPMax());
		this.setSPMax(obtenerValorEntre(90,150));
		this.setSP(this.getSPMax());
		this.setAtq(obtenerValorEntre(8,12));
		this.setDef(obtenerValorEntre(10,20));
		this.setVel(obtenerValorEntre(10,20));
		this.setPCRT(0);
		this.setDCRT(1.5);
		this.setEva(0);
		this.setBloq(30);
		this.spHabilidad1 = 15;
		this.spHabilidad2 = 40;
		this.habilidades[0] = "DIFAMAR";
		this.habilidades[1] = "TENDENCIA";
		this.generarCuerpo();
	}
	//METODO PRINCIPAL//////////////////////////////////////////////////////////////////
	public void realizarAccion(ArrayList<Unidad> enemigos, ArrayList<Unidad> aliados) {
		if(cumpleReqDeHab2()) {
			setHabilidadElegida(1);
			usarHabilidadEnemigo(aliados, enemigos);
		}
		else if(cumpleReqDeHab1()) {
			setHabilidadElegida(0);
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
			Unidad unidad = elegirObjetivo(enemigos);
			difamar(unidad);
		}
		else {
			tendencia();
		}
	}
	//METODOS JUGADOR////////////////////////////////////////////////////////////////////
	public void usarHabilidad(Unidad unidad, ArrayList<Unidad> unidades) {	
		if(this.getHabilidadElegida() == 0) {
			difamar(unidad);
		}
		else {
			tendencia();
		}
	}
	//HABILIDADES////////////////////////////////////////////////////////////////////////
	public void difamar(Unidad unidad) {
		int daño = this.getAtq() + this.getAtqMod() + this.getDefMod();
		int reduccion = (int) (daño * ((unidad.getDef() + unidad.getDefMod()) / 100.0));
        int dañoFinal = Math.max(1, daño - reduccion);
		if(unidad != null) {
			pdj.ReproducirSE(2);
			unidad.setHP(unidad.getHP() - dañoFinal);
			Habilidades.difamarUnidad(this, unidad);
			this.setSP(this.getSP() - this.spHabilidad1);
			this.setCdHabilidad1(1);
			this.setHabilidad1(false);
		}
	}
	
	public void tendencia() {
		pdj.ReproducirSE(3);
		this.setSP(this.getSP() - this.spHabilidad2);
		Habilidades.tendencia(this);
		this.setCdHabilidad2(10);
		this.setHabilidad2(false);
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
		if(this.getSP() >= this.spHabilidad1 && this.isHabilidad1()) {
			return true;
		}
		return false;
	}
	public boolean cumpleReqDeHab2() {
		if(this.getSP() >= this.spHabilidad2 && this.isHabilidad2()) {
			return true;
		}
		return false;
	}
	public void pasivaDeClase(ArrayList<Unidad> aliados, ArrayList<Unidad> enemigos) {
		if(this.getCdHabilidad1() == 0) {
			this.setHabilidad1(true);
		}
		else {
			this.setCdHabilidad1(this.getCdHabilidad1() - 1);
		}
		if(this.getCdHabilidad2() == 0) {
			this.setHabilidad2(true);
		}
		else {
			this.setCdHabilidad2(this.getCdHabilidad2() - 1);
		}
		super.pasivaDeClase(aliados, enemigos);
	}
	public String[] getListaDeHabilidades() {return habilidades;}
	public void setListaDeHabilidades(String[] habilidades) {this.habilidades = habilidades;}
}
