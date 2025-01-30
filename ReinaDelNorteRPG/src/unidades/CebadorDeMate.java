package unidades;

import java.util.ArrayList;
import principal.PanelDeJuego;
import principal.Zona;

public class CebadorDeMate extends Unidad{
	
	private int spHabilidad1;
	private String[] listaDeHabilidades = new String[1];

	public CebadorDeMate(Zona zona, boolean aliado, PanelDeJuego pdj) {
		super(zona, aliado, pdj);
		this.setNombre("");
		this.setTipo("Recluta");
		this.setClase("Cebador De Mate");
		this.setIdFaccion(1);
		this.setHPMax(obtenerValorEntre(40,60));
		this.setHP(this.getHPMax());
		this.setSPMax(obtenerValorEntre(25,50));
		this.setSP(this.getSPMax());
		this.setAtq(obtenerValorEntre(8,12));
		this.setDef(obtenerValorEntre(1,5));
		this.setPCRT(0);
		this.setDCRT(1.5);
		this.setEva(0);
		this.setVel(obtenerValorEntre(10,20));
		this.spHabilidad1 = 7;
		this.listaDeHabilidades[0] = "CEBAR UN MATE";
		this.generarCuerpo();
	}
	//METODO PRINCIPAL//////////////////////////////////////////////////////////////////
	public void realizarAccion(ArrayList<Unidad> enemigos, ArrayList<Unidad> aliados) {
		int accion = elegirAleatorio(4);
		if(accion <= 2 && cumpleReqDeHab1()) {
			this.setHabilidadElegida(0);
			usarHabilidadEnemigo(aliados);
			this.setSP(this.getSP() - this.spHabilidad1);
			
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
		this.setSP(this.getSP() - this.spHabilidad1);
		int opcion = elegirAleatorio(5);
		if(opcion == 0) {
			//HERVIDO Y DULCE
			unidad.restaurarHP(obtenerValorEntre(7,15));
			unidad.setAtqMod(unidad.getAtqMod() + obtenerValorEntre(3,7));
			unidad.setPcrtMod(unidad.getPcrtMod() + (0.05));
		}
		else if(opcion == 1) {
			//HERVIDO y AMARGO
			unidad.restaurarHP(obtenerValorEntre(7,15));
			unidad.setDefMod(unidad.getDefMod() + obtenerValorEntre(1,3));
			unidad.setEvaMod(0.25);
		}
		else if(opcion == 2) {
			//HELADO Y DULCE
			if(unidad.getSPMax() > 0) {
				unidad.restaurarSP(obtenerValorEntre(7,15));
			}
			unidad.setAtqMod(unidad.getAtqMod() + obtenerValorEntre(3,7));
			unidad.setPcrtMod(unidad.getPcrtMod() + (0.05));
			
		}
		else if(opcion == 3) {
			//HELADO Y AMARGO
			if(unidad.getSPMax() > 0) {
				unidad.restaurarSP(obtenerValorEntre(7,15));
			}
			unidad.setDefMod(unidad.getDefMod() + obtenerValorEntre(1,3));
			unidad.setEvaMod(0.25);
		}
		else {
			//PERFECTO
			unidad.setHPMax(unidad.getHPMax()+(unidad.getHPMax()/10));
			unidad.restaurarHP(obtenerValorEntre(15,23));
			if(unidad.getSPMax() > 0) {
				unidad.setSPMax(unidad.getSPMax()+(unidad.getSPMax()/10));
				unidad.restaurarSP(obtenerValorEntre(15,23));
			}
			unidad.setAtqMod(unidad.getAtqMod() + obtenerValorEntre(3,7));
			unidad.setDefMod(unidad.getDefMod() + obtenerValorEntre(1,3));
			unidad.setPcrtMod(unidad.getPcrtMod() + (0.05));
			unidad.setEvaMod(0.25);
			
		}
		pdj.ReproducirSE(5);
		unidad.setearSacudida(true);
		unidad.setDuracionSacudida(20);
		unidad.setIdMate(opcion);
		unidad.setTomandoUnMate(true);
	}
	//METODOS AUXILIARES/////////////////////////////////////////////////////////////////
	public boolean cumpleReqDeHab1() {
		if(this.getSP() > 0) {
			if(this.getSP() >= this.spHabilidad1) {
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
	public String[] getListaDeHabilidades() {return listaDeHabilidades;}
	public void setListaDeHabilidades(String[] habilidades) {this.listaDeHabilidades = habilidades;}
}
