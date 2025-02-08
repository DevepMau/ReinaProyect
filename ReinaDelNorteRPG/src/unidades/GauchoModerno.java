package unidades;

import java.util.ArrayList;

import principal.Habilidades;
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
		this.setAtq(obtenerValorEntre(12,17));
		this.setDef(obtenerValorEntre(5,9));
		this.setPCRT(15);
		this.setDCRT(1.5);
		this.setEva(0);
		this.setBloq(0);
		this.setVel(obtenerValorEntre(5,10));
		this.listaDeHabilidades[0] = "APUÑALAR";
		this.generarCuerpo();
	}
	//METODO PRINCIPAL//////////////////////////////////////////////////////////////////
	public void recibirDaño(int daño, boolean isCritical, int duracionSacudida) {
	    int hpAnterior = this.getHP();
	    super.recibirDaño(daño, isCritical, duracionSacudida);
	    int hpPerdido = hpAnterior - this.getHP();
	    this.acumuladorDeVidaPrdida += hpPerdido;
	    if(acumuladorDeVidaPrdida >= 5) {
	    	int acumuladorDeAtq = (acumuladorDeVidaPrdida / 5);
	    	this.setAtqMod(this.getAtqMod() + acumuladorDeAtq);
	        this.acumuladorDeVidaPrdida -= (5*acumuladorDeAtq); 
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
				apuñalar(unidad);
			}
		}
	}
	//METODOS JUGADOR////////////////////////////////////////////////////////////////////
	public void usarHabilidad(Unidad unidad, ArrayList<Unidad> unidades) {	
		apuñalar(unidad);
	}
	//HABILIDADES////////////////////////////////////////////////////////////////////////
	public void apuñalar(Unidad unidad) {
		int daño = this.getAtq() + this.getAtqMod();
	    int reduccion = (int) (daño * ((unidad.getDef() + unidad.getDefMod()) / 100.0));
        int dañoFinal = Math.max(1, daño - reduccion);
        String textoMostrado = "";
	    if (unidad.elegirAleatorio(100) < (unidad.getEva() + unidad.getEvaMod())) {
	    	pdj.ReproducirSE(6);
	    	unidad.setEvadiendo(true);
	        textoMostrado = "MISS!";
	        Habilidades.setearEstado(unidad, textoMostrado);
	    }
	    else {
	    	if(this.elegirAleatorio(100) < (unidad.getBloq() + unidad.getBloqMod())) {
	    		pdj.ReproducirSE(9);
	    		unidad.setBloqueando(true);
	    		unidad.setHP(unidad.getHP() - (dañoFinal/4));
		        textoMostrado = "BLOCK!";
		        Habilidades.setearEstado(unidad, textoMostrado);
	    	}
	    	else if (unidad.getEscudos() > 0) {
		        unidad.setEscudos(unidad.getEscudos() - 1);
		        pdj.ReproducirSE(9);
		        unidad.setRompiendo(true);
		        textoMostrado = "BREAK!";
		        Habilidades.setearEstado(unidad, textoMostrado);
		    } else {
		    	textoMostrado = "" + dañoFinal;
		        unidad.setHP(unidad.getHP() - dañoFinal);
		        pdj.ReproducirSE(3);
		        Habilidades.setearDaño(unidad, textoMostrado);
		        unidad.setValorSangrado(unidad.getHPMax()/20 + this.getAtqMod());
				unidad.setSangrando(true);
				unidad.setTimerSangrando(5);
		    }
	    }  
	}
	//METODOS AUXILIARES/////////////////////////////////////////////////////////////////
	public boolean cumpleReqDeHab1() {
		if(this.getAtqMod() >= 0) {
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
