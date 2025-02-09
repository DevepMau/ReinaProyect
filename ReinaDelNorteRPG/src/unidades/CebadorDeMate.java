package unidades;

import java.util.ArrayList;

import principal.Habilidades;
import principal.PanelDeJuego;
import principal.Zona;

public class CebadorDeMate extends Unidad{
	
	private int costeHabilidad1;
	private String[] listaDeHabilidades = new String[1];

	public CebadorDeMate(Zona zona, boolean aliado, PanelDeJuego pdj) {
		super(zona, aliado, pdj);
		this.setNombre("");
		this.setTipo("Recluta");
		this.setClase("Cebador De Mate");
		this.setIdFaccion(1);
		this.setHPMax(obtenerValorEntre(50,75));
		this.setHP(this.getHPMax());
		this.setSPMax(obtenerValorEntre(35,50));
		this.setSP(this.getSPMax());
		this.setAtq(obtenerValorEntre(5,8));
		this.setDef(obtenerValorEntre(10,20));
		this.setPCRT(0);
		this.setDCRT(1.5);
		this.setEva(0);
		this.setVel(obtenerValorEntre(20,30));
		this.costeHabilidad1 = 7;
		this.listaDeHabilidades[0] = "CEBAR UN MATE";
		this.generarCuerpo();
	}
	//METODO PRINCIPAL//////////////////////////////////////////////////////////////////
	public void realizarAccion(ArrayList<Unidad> enemigos, ArrayList<Unidad> aliados) {
		int accion = elegirAleatorio(4);
		if(accion <= 2 && cumpleReqDeHab1()) {
			this.setHabilidadElegida(0);
			usarHabilidadEnemigo(aliados);
			this.setSP(this.getSP() - this.costeHabilidad1);
			
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
			cebarMate(unidad);	
		}
	}
	//METODOS JUGADOR////////////////////////////////////////////////////////////////////
	public void usarHabilidad(Unidad unidad, ArrayList<Unidad> unidades) {
		cebarMate(unidad);
	}
	//HABILIDADES////////////////////////////////////////////////////////////////////////
	public void cebarMate(Unidad unidad) {
		this.setSP(this.getSP() - this.costeHabilidad1);;
		int opcion = elegirAleatorio(4);
		if(opcion == 0) {
			//HERVIDO Y DULCE
			Habilidades.restaurarHP(unidad, 20);
			Habilidades.setearEstado(unidad, "+" + this.porcentajeHP(20));
			unidad.setCurando(true);
			
		}
		else if(opcion == 1) {
			//HERVIDO y AMARGO
			if(unidad.getTimerPrecavido() == -1) {
				Habilidades.aumentarProteccion(unidad);
			}
			Habilidades.setearEstado(unidad, "PRECAVIDO");
			unidad.setPrecavido(true);
			unidad.setTimerPrecavido(5);
		}
		else if(opcion == 2) {
			//HELADO Y DULCE
			if(unidad.getTimerAgresivo() == -1) {
				Habilidades.aumentarAgresividad(unidad);
			}
			Habilidades.setearEstado(unidad, "AGRESIVO");
			unidad.setAgresivo(true);
			unidad.setTimerAgresivo(5);
			
		}
		else {
			//PERFECTO
			if(unidad.getTimerPotenciado() == -1) {
				Habilidades.potenciarUnidad(unidad);
			}
			Habilidades.setearEstado(unidad, "POTENCIADO");
			Habilidades.aumentarHPMax(unidad, 20);
			Habilidades.restaurarHP(unidad, 20);
			Habilidades.aumentarSPMax(unidad, 20);
			Habilidades.restaurarSP(unidad, 20);
			unidad.setPotenciado(true);
			unidad.setTimerPotenciado(5);
			
		}
		this.setCdHabilidad1(0);
		this.setHabilidad1(false);
		pdj.ReproducirSE(5);
	}
	//METODOS AUXILIARES/////////////////////////////////////////////////////////////////
	public boolean cumpleReqDeHab1() {
		if(this.getSP() > 0 && this.isHabilidad1()) {
			if(this.getSP() >= this.costeHabilidad1) {
				return true;
			}
		}
		return false;
	}	
	public void configurarTipoDeaccion() {
		if(this.getHabilidadElegida() == 0) {
			this.setAccion("APOYAR");
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
