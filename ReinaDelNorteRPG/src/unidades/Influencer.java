package unidades;

import java.util.ArrayList;
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
		this.setHPMax(obtenerValorEntre(150,200));
		this.setHP(this.getHPMax());
		this.setSPMax(obtenerValorEntre(70,90));
		this.setSP(this.getSPMax());
		this.setAtq(obtenerValorEntre(4,6));
		this.setDef(obtenerValorEntre(1,2));
		this.setVel(obtenerValorEntre(5,12));
		this.setPCRT(0);
		this.setDCRT(1.5);
		this.setEva(0);
		this.setCapacidadBloqueo(0.6);
		this.spHabilidad1 = 10;
		this.spHabilidad2 = 30;
		this.habilidades[0] = "DIFAMAR";
		this.habilidades[1] = "SELFIE GRUPAL";
		this.generarCuerpo();
	}
	//METODO PRINCIPAL//////////////////////////////////////////////////////////////////
	public void realizarAccion(ArrayList<Unidad> enemigos, ArrayList<Unidad> aliados) {
		if(this.getDefMod() >= 4) {
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
			if(!enemigos.isEmpty()) {
				selfieGrupal(aliados);
			}
		}
	}
	//METODOS JUGADOR////////////////////////////////////////////////////////////////////
	public void usarHabilidad(Unidad unidad, ArrayList<Unidad> unidades) {	
		if(this.getHabilidadElegida() == 0) {
			difamar(unidad);
		}
		else {
			selfieGrupal(unidades);
		}
	}
	//HABILIDADES////////////////////////////////////////////////////////////////////////
	public void difamar(Unidad unidad) {
		if(unidad != null) {
			this.setSP(this.getSP() - this.spHabilidad1);
			unidad.recibirDaño(this.getDefMod()*2, false, 20);
			if((unidad.getDef() + unidad.getDefMod()) > 0) {
				unidad.setDefMod(unidad.getDefMod() -2);
				this.setDefMod(this.getDefMod() + 1);
			}
			unidad.setearSacudida(true);
			unidad.setDuracionSacudida(20);
			unidad.setEstaDesmotivado(true);
		}
	}
	
	public void selfieGrupal(ArrayList<Unidad> unidades) {
		if(!unidades.isEmpty()) {
			if(this.cumpleReqDeHab2()) {
				this.setSP(this.getSP() - this.spHabilidad2);
				for(Unidad unidad : unidades) {
					pdj.ReproducirSE(9);
					unidad.setEscudos(unidad.getEscudos() + 1);
					unidad.setEstaBloqueando(true);
					unidad.setDuracionSacudida(20);
					unidad.setearSacudida(true);
				}
			}
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
