package unidades;

import java.util.ArrayList;
import principal.PanelDeJuego;
import principal.Zona;

public class GauchoModerno extends Unidad {
	
	private String[] listaDeHabilidades = new String[1];
	private int acumuladorDeVidaPrdida = 0;

	public GauchoModerno(Zona zona, boolean aliado, PanelDeJuego pdj) {
		super(zona, aliado, pdj);
		this.setNombre("");
		this.setTipo("Combatiente");
		this.setClase("Gaucho Moderno");
		this.setIdFaccion(1);
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
		this.listaDeHabilidades[0] = "SALDAR DEUDA";
		this.generarCuerpo();
	}
	//METODO PRINCIPAL//////////////////////////////////////////////////////////////////
	public void recibirDaño(int daño, boolean isCritical, int duracionSacudida) {
	    int hpAnterior = this.getHP();
	    super.recibirDaño(daño, isCritical, duracionSacudida);
	    int hpPerdido = hpAnterior - this.getHP();
	    this.acumuladorDeVidaPrdida += hpPerdido;
	    if(acumuladorDeVidaPrdida >= 10) {
	    	int acumuladorDeAtq = (acumuladorDeVidaPrdida / 10);
	    	this.setAtqMod(this.getAtqMod() + acumuladorDeAtq);
	        this.acumuladorDeVidaPrdida -= (10*acumuladorDeAtq); 
	    }
	}
	public void realizarAccion(ArrayList<Unidad> enemigos, ArrayList<Unidad> aliados) {
		super.realizarAccion(enemigos, aliados);
		this.pasivaDeClase(aliados, enemigos);
	}
	//METODOS ENEMIGO////////////////////////////////////////////////////////////////////
	public void usarHabilidadEnemigo(ArrayList<Unidad> unidades) {
		if(this.getHabilidadElegida() == 0) {
			if(!unidades.isEmpty()) {
				Unidad unidad = elegirObjetivo(unidades);
				saldarDeuda(unidad);
			}
		}
	}
	//METODOS JUGADOR////////////////////////////////////////////////////////////////////
	public void usarHabilidad(Unidad unidad, ArrayList<Unidad> unidades) {	
		saldarDeuda(unidad);
	}
	//HABILIDADES////////////////////////////////////////////////////////////////////////
	public void saldarDeuda(Unidad unidad) {
		boolean isMiss = Math.random() <= (unidad.getEva() + unidad.getEvaMod());	
		if(unidad != null) {
			if(!isMiss) {
				int daño = (this.getAtqMod()*4);
				unidad.recibirDaño(daño, false, 20);
				unidad.setearSacudida(true);
				unidad.setDuracionSacudida(20);
				unidad.setEsUnaHabilidad(true);
				contarFaltas(unidad, 3);
			}
			else {
				unidad.evadirAtaque();
			}	
		}
		this.setAtqMod(0);
	}
	//METODOS AUXILIARES/////////////////////////////////////////////////////////////////
	public boolean cumpleReqDeHab1() {
		if(this.getAtqMod() >= 5) {
			return true;
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
