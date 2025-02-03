package unidades;

import java.util.ArrayList;

import principal.PanelDeJuego;
import principal.Zona;

public class NovataTimida extends Unidad{
	
	private int spHabilidad1;
	private int cargas;
	private String[] listaDeHabilidades = new String[1];

	public NovataTimida(Zona zona, boolean aliado, PanelDeJuego pdj) {
		super(zona, aliado, pdj);
		this.setNombre("");
		this.setTipo("Recluta");
		this.setClase("Novata Timida");
		this.setIdFaccion(4);
		this.setGenero(0);
		this.setHPMax(obtenerValorEntre(40,50));
		this.setHP(this.getHPMax());
		this.setSPMax(0);
		this.setSP(this.getSPMax());
		this.setAtq(obtenerValorEntre(6,9));
		this.setDef(obtenerValorEntre(2,5));
		this.setPCRT(0);
		this.setDCRT(1.5);
		this.setEva(0);
		this.setVel(obtenerValorEntre(4,12));
		this.setEscudos(0);
		this.cargas = 0;
		this.spHabilidad1 = 12;
		this.listaDeHabilidades[0] = "...";
		this.generarCuerpo();
	}
	//METODO PRINCIPAL//////////////////////////////////////////////////////////////////
	public void realizarAccion(ArrayList<Unidad> enemigos, ArrayList<Unidad> aliados) {
		if(this.getClase() != "Novata Timida") {
			if(cumpleReqDeHab1()) {
				this.setHabilidadElegida(0);
				usarHabilidadEnemigo(enemigos);
				
			}
			else {
				realizarAtaqueEnemigo(enemigos);
			}
		}
		else {
			realizarAtaqueEnemigo(enemigos);
		}
		this.pasivaDeClase(aliados, enemigos);
	}
	//METODOS ENEMIGO////////////////////////////////////////////////////////////////////
	public void usarHabilidadEnemigo(ArrayList<Unidad> unidades) {
		if(this.getHabilidadElegida() == 0) {
			Unidad unidad = elegirObjetivo(unidades);
			iniciativa(unidad);	
			if(this.cargas == 2) {
				if(this.getClase() != "Novata Confiable") {
					evolucionar();
				}
			}
			else {
				this.cargas++;
			}
		}
	}
	public void realizarAtaqueEnemigo(ArrayList<Unidad> unidades) {
		if(!unidades.isEmpty()) {
			super.realizarAtaqueEnemigo(unidades);
			if(this.cargas == 2) {
				if(this.getClase() != "Novata Confiable") {
					evolucionar();
				}
			}
			else {
				this.cargas++;
			}
		}
	}
	//METODOS JUGADOR////////////////////////////////////////////////////////////////////
	public void realizarAtaque(Unidad unidad, ArrayList<Unidad> unidades) {
		super.realizarAtaque(unidad, unidades);
		if(this.cargas == 2) {
			if(this.getClase() != "Novata Confiable") {
				evolucionar();
			}
		}
		else {
			this.cargas++;
		}
	}
	public void usarHabilidad(Unidad unidad, ArrayList<Unidad> unidades) {
		iniciativa(unidad);
		if(this.cargas == 2) {
			if(this.getClase() != "Novata Confiable") {
				evolucionar();
			}
		}
		else {
			this.cargas++;
		}
	}
	//HABILIDADES////////////////////////////////////////////////////////////////////////
	public void evolucionar() {
		if(this.getClase() == "Novata Timida") {
			this.setClase("Novata Cauta");
			this.listaDeHabilidades[0] = "INICIATIVA";
		}
		else if(this.getClase() == "Novata Cauta") {
			this.setClase("Novata Confiable");
		}
		this.setHPMax(this.getHPMax() + this.getHPMax()/4);
		this.restaurarHP(this.getHPMax()/4);
		this.setSPMax(this.getSPMax() + 30);
		this.restaurarSP(this.getSPMax());
		this.setAtq(this.getAtq() + 3);
		this.setDef(this.getDef() + 1);
		this.setVel(this.getVel() + 3);
		this.setEva(this.getEva() + 0.05);
		this.setPCRT(this.getPCRT() + 0.05);
		this.generarCuerpo();
		this.cargas = 0;
	}
	public void iniciativa(Unidad unidad) {
		if(unidad != null) {
			this.setSP(this.getSP() - this.spHabilidad1);
			int daño = Math.max(1, ((this.getAtq()) + this.getAtqMod()) - (unidad.getDef() + unidad.getDefMod()));
			unidad.recibirDaño(daño, true, 20);
			this.setEscudos(this.getEscudos() + 1);
			contarFaltas(unidad, 1);
		}	
	}
	//METODOS AUXILIARES/////////////////////////////////////////////////////////////////
	public boolean cumpleReqDeHab1() {
		if(this.getClase() != "Novata Timida") {
			if(this.getSP() > 0) {
				if(this.getSP() >= this.spHabilidad1) {
					return true;
				}
			}
		}
		return false;
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
