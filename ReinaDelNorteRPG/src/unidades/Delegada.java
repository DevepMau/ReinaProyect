package unidades;

import java.util.ArrayList;

import principal.Habilidades;
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
		this.setHPMax(obtenerValorEntre(110,130));
		this.setHP(this.getHPMax());
		this.setSP(0);
		this.setSPMax(0);
		this.setAtq(obtenerValorEntre(12,15));
		this.setDef(obtenerValorEntre(10,20));
		this.setPCRT(0);
		this.setDCRT(1.5);
		this.setEva(0);
		this.setVel(obtenerValorEntre(10,20));
		this.listaDeHabilidades[0] = "REGAÑAR";
		this.definirIdTez();
		this.asignarImagen(4, "/imagenes/accesorios/mano-de-lente-"+this.getIdTez(), 3); 
		this.asignarImagen(3, "/imagenes/unisex/manos-libreta-"+this.getIdTez(), 3); 
		this.asignarImagen(2, "/imagenes/mujer/falda-"+this.getIdTez(), 3); 
		this.asignarImagen(1, "/imagenes/unisex/cuerpo"+this.getIdTez(), 3); 
		this.asignarImagen(0, "/imagenes/mujer/cabeza-delegada-"+this.getIdTez(), 3); 
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
		Unidad unidad = this.elegirObjetivoCon3Marcas(unidades);
		if(unidad != null) {
			super.usarHabilidadOfensiva(unidad,() -> Habilidades.reportarUnidad(this, unidad, pdj));
		}
	}
	//METODOS JUGADOR////////////////////////////////////////////////////////////////////
	public void usarHabilidad(Unidad unidad, ArrayList<Unidad> unidades) {
		if(unidad != null) {
			super.usarHabilidadOfensiva(unidad,() -> Habilidades.reportarUnidad(this, unidad, pdj));
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
