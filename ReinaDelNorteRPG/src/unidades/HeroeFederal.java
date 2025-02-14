package unidades;

import java.awt.Color;
import java.util.ArrayList;

import principal.Habilidades;
import principal.PanelDeJuego;
import principal.Zona;

public class HeroeFederal extends Unidad{
	
	private int spHabilidad1;
	private String[] listaDeHabilidades = new String[1];

	public HeroeFederal(Zona zona, boolean aliado, PanelDeJuego pdj) {
		super(zona, aliado, pdj);
		this.setNombre("");
		this.setTipo("Elite");
		this.setClase("Heroe Federal");
		this.setIdFaccion(1);
		this.setHPMax(obtenerValorEntre(150,220));
		this.setHP(this.getHPMax());
		this.setSPMax(obtenerValorEntre(70,90));
		this.setSP(this.getSPMax());
		this.setAtq(obtenerValorEntre(18,25));
		this.setDef(obtenerValorEntre(30,40));
		this.setPCRT(5);
		this.setDCRT(2);
		this.setEva(15);
		this.setBloq(15);
		this.setVel(obtenerValorEntre(30,50));
		this.spHabilidad1 = 15;
		this.listaDeHabilidades[0] = "EXPULSAR";
		this.generarCuerpo();
	}
	//METODOS PRINCIPALES///////////////////////////////////////////////////////////
	public void realizarAccion(ArrayList<Unidad> enemigos, ArrayList<Unidad> aliados) {
		int accion = elegirAleatorio(3);
		if(accion <= 1 && cumpleReqDeHab1()) {
			setHabilidadElegida(0);
			usarHabilidadEnemigo(enemigos);
		}
		else {
			realizarAtaqueEnemigo(enemigos);
		}
		this.pasivaDeClase(aliados, enemigos);
	}
	//METODOS DE ENEMIGO////////////////////////////////////////////////////////////
	public void usarHabilidadEnemigo(ArrayList<Unidad> unidades) {
		if(this.getHabilidadElegida() == 0) {
			Unidad unidad = elegirObjetivoMasFuerte(unidades);
			expulsar(unidad);	
		}
	}
	//METODOS DE JUGADOR////////////////////////////////////////////////////////////
	public void usarHabilidad(Unidad unidad, ArrayList<Unidad> unidades) {
		expulsar(unidad);
	}
	//HABILIDADES///////////////////////////////////////////////////////////////////
	public void expulsar(Unidad unidad) {
		this.setSP(this.getSP() - this.spHabilidad1);
		int daño = this.getAtq() + this.getAtqMod() + this.getVel()/2;
	    int reduccion = (int) (daño * ((unidad.getDef() + unidad.getDefMod()) / 100.0));
        int dañoFinal = Math.max(1, daño - reduccion);
	    if (unidad.elegirAleatorio(100) < (unidad.getEva() + unidad.getEvaMod())) {
	    	pdj.ReproducirSE(6);
	    	unidad.setEvadiendo(true);
	        Habilidades.setearEfectoDeEstado(unidad, "MISS!", Color.white);
	    }
	    else {
	    	if (unidad.getEscudos() > 0) {
		        unidad.setEscudos(0);
		        pdj.ReproducirSE(9);
		        unidad.setRompiendo(true);
		        Habilidades.setearEfectoDeEstado(unidad, "BREAK!", Color.white);
		    } else {
		    	 pdj.ReproducirSE(3);
		        unidad.setHP(unidad.getHP() - dañoFinal);
		        Habilidades.destruirMovilidad(unidad);
		    }
	    } 
	    this.setCdHabilidad1(1);
		this.setHabilidad1(false);
	}
	//METODOS AUXILIARES////////////////////////////////////////////////////////////
	public Unidad elegirObjetivoMasFuerte(ArrayList<Unidad> unidades) {
	    Unidad unidadSeleccionada = null;
	    int mayorPorcentajeATQ = Integer.MIN_VALUE;
	    for (Unidad unidad : unidades) {
	    	int ATQUnidad = unidad.getAtq() + unidad.getAtqMod();
	        if (ATQUnidad > mayorPorcentajeATQ) {
	        	mayorPorcentajeATQ = ATQUnidad;
	            unidadSeleccionada = unidad;
	        }
	    }
	    return unidadSeleccionada;
	}
	public boolean cumpleReqDeHab1() {
		if(this.getSP() >= this.spHabilidad1 && this.isHabilidad1()) {
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
	public void pasivaDeClase(ArrayList<Unidad> aliados, ArrayList<Unidad> enemigos) {
		if(this.getCdHabilidad1() == 0) {
			this.setHabilidad1(true);
		}
		else {
			this.setCdHabilidad1(this.getCdHabilidad1() - 1);
		}
		super.pasivaDeClase(aliados, enemigos);
	}
	public String[] getListaDeHabilidades() {return listaDeHabilidades;}
	public void setListaDeHabilidades(String[] habilidades) {this.listaDeHabilidades = habilidades;}
}