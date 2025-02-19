package unidades;

import java.util.ArrayList;

import principal.Estadisticas;
import principal.Habilidades;
import principal.PanelDeJuego;
import principal.Zona;

public class DoncellaDelCiruelo extends Unidad{
	
	private int spHabilidad1;
	private int spHabilidad2;
	private int contador = 5;
	private String[] listaDeHabilidades = new String[2];

	public DoncellaDelCiruelo(Zona zona, boolean aliado, PanelDeJuego pdj) {
		super(zona, aliado, pdj);
		this.setNombre("");
		this.setTipo("Elite");
		this.setClase("Doncella del Cerezo");
		this.setGenero(0);
		this.setIdFaccion(4);
		this.setHPMax(obtenerValorEntre(150,220));
		this.setHP(this.getHPMax());
		this.setSPMax(obtenerValorEntre(70,90));
		this.setSP(this.getSPMax());
		this.setAtq(obtenerValorEntre(15,22));
		this.setDef(obtenerValorEntre(20,30));
		this.setPCRT(50);
		this.setDCRT(2);
		this.setEva(15);
		this.setVel(obtenerValorEntre(20,30));
		this.spHabilidad1 = 10;
		this.spHabilidad2 = 40;
		this.listaDeHabilidades[0] = "ABOFETEAR";
		this.listaDeHabilidades[1] = "DAR DISCURSO";
		this.generarCuerpo();
	}
	//METODOS PRINCIPALES///////////////////////////////////////////////////////////
	public void realizarAccion(ArrayList<Unidad> enemigos, ArrayList<Unidad> aliados) {
		if(!this.cumpleReqDeHab2()) {
			this.setHabilidadElegida(1);
			usarHabilidadEnemigo(aliados, enemigos);
		}
		else if(this.cumpleReqDeHab1()) {
			this.setHabilidadElegida(0);
			usarHabilidadEnemigo(aliados, enemigos);
		}
		else {
			realizarAtaqueEnemigo(enemigos);
		}
		this.pasivaDeClase(aliados, enemigos);
	}
	//METODOS DE ENEMIGO////////////////////////////////////////////////////////////
	public void realizarAtaqueEnemigo(ArrayList<Unidad> unidades) {
		super.realizarAtaqueEnemigo(unidades);
	}
	public void usarHabilidadEnemigo(ArrayList<Unidad> aliados, ArrayList<Unidad> enemigos) {
		if(this.getHabilidadElegida() == 0) {
			if(!aliados.isEmpty()) {
				Unidad unidad = elegirObjetivo(enemigos);
				abofetear(unidad);
			}
		}
		else {
			if(!enemigos.isEmpty()) {
				for(Unidad unidad : aliados) {
					discursoInspirador(unidad);
				}
			}
		}
	}
	//METODOS DE JUGADOR////////////////////////////////////////////////////////////
	public void realizarAtaque(Unidad unidad, ArrayList<Unidad> enemigos) {
		super.realizarAtaque(unidad, enemigos);
	}
	public void usarHabilidad(Unidad unidad, ArrayList<Unidad> unidades) {	
		if(this.getHabilidadElegida() == 0) {
			abofetear(unidad);
		}
		else {
			for(Unidad unidadObjetivo : unidades) {
				discursoInspirador(unidadObjetivo);
			}
			this.setCargarEnamoramiento(0);
		}
		
	}
	//HABILIDADES///////////////////////////////////////////////////////////////////
	public void abofetear(Unidad unidad) {
		this.setSP(this.getSP() - this.spHabilidad1);
		if(unidad != null) {
			pdj.ReproducirSE(2);
			Habilidades.desmoralizarUnidad(unidad);
			this.setCdHabilidad1(1);
			this.setHabilidad1(false);
		}
	}
	public void discursoInspirador(Unidad unidad) {
		if(unidad != null) {
			Habilidades.protegerUnidad(unidad, 1, pdj);
			this.setCdHabilidad2(5);
			this.setHabilidad2(false);
		}
	}
	//METODOS AUXILIARES////////////////////////////////////////////////////////////
	public Unidad elegirObjetivo(ArrayList<Unidad> unidades) {
	    Unidad unidadSeleccionada = null;
	    int menorPorcentajeHP = Integer.MAX_VALUE;
	    for (Unidad unidad : unidades) {
	    	int porcentajeHP = (unidad.getHP() * 100) / unidad.getHPMax();;
	        if (porcentajeHP < menorPorcentajeHP && !unidad.isEstaEnamorado()) {
	            menorPorcentajeHP = porcentajeHP;
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
	
	public boolean cumpleReqDeHab2() {
		if(this.getSP() >= this.spHabilidad2 && this.isHabilidad2()) {
			return true;
		}
		return false;
	}
	
	public void configurarTipoDeaccion() {
		if(this.getHabilidadElegida() == 0) {
			this.setObjetivoUnico(true);
			this.setAccion("ATACAR");
		}
		else {
			this.setObjetivoUnico(false);
			this.setAccion("APOYAR");
		}
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
		if(!aliados.isEmpty() && this.contador > 0) {
			for(Unidad unidad : aliados) {
				if(!this.equals(unidad)) {
					Estadisticas.aumentarAtaque(unidad, 1);
					Estadisticas.aumentarDefensa(unidad, 1);
				}
			}
			contador--;
		}
	}
	public String[] getListaDeHabilidades() {return listaDeHabilidades;}
	public void setListaDeHabilidades(String[] habilidades) {this.listaDeHabilidades = habilidades;}
}