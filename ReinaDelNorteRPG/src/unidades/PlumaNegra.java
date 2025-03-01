package unidades;

import java.util.ArrayList;

import principal.Habilidades;
import principal.PanelDeJuego;
import principal.Zona;

public class PlumaNegra extends Unidad {
	
	private String[] habilidades = new String[1];
	private int spHabilidad1;

	public PlumaNegra(Zona zona, boolean aliado, PanelDeJuego pdj) {
		super(zona, aliado, pdj);
		this.setNombre("");
		this.setTipo("Especialista");
		this.setClase("Pluma Negra");
		this.setIdFaccion(0);
		this.setHPMax(obtenerValorEntre(110,130));
		this.setHP(this.getHPMax());
		this.setSPMax(obtenerValorEntre(100,150));
		this.setSP(this.getSPMax());
		this.setAtq(obtenerValorEntre(12,15));
		this.setDef(obtenerValorEntre(10,20));
		this.setVel(obtenerValorEntre(20,30));
		this.setPCRT(0);
		this.setDCRT(1.5);
		this.setEva(0);
		this.spHabilidad1 = 15;
		this.habilidades[0] = "GOLPE SOMBRIO";
		this.definirIdTez();
		if(this.getGenero() == 1) {
			this.asignarImagen( 4, "/imagenes/accesorios/banda-negra-barbijo-hombre",3);
			this.asignarImagen( 3, "/imagenes/unisex/mano-sola-"+this.getIdTez(),3);
			this.asignarImagen( 2, "/imagenes/hombre/pantalon-1",3);
			this.asignarImagen( 1, "/imagenes/unisex/cuerpo-tapado-negro-"+this.getIdTez(), 3);
			this.asignarImagen( 0, "/imagenes/hombre/cabeza-boy-"+this.getIdTez(), 3);
		}
		else {
			this.asignarImagen( 4, "/imagenes/accesorios/banda-negra-barbijo-mujer",3);
			this.asignarImagen( 3, "/imagenes/unisex/manos-"+this.getIdTez(), 3);
			this.asignarImagen( 2, "/imagenes/mujer/falda-"+this.getIdTez(),3);
			this.asignarImagen( 1, "/imagenes/unisex/cuerpo-tapado-"+this.getIdTez(), 3);
			this.asignarImagen( 0, "/imagenes/mujer/cabeza-girl-"+this.getIdTez(), 3);
		}
		this.setAlturaDeAccesorio(3);
	}
	//METODO PRINCIPAL//////////////////////////////////////////////////////////////////
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
	//METODOS ENEMIGO////////////////////////////////////////////////////////////////////
		public void usarHabilidadEnemigo(ArrayList<Unidad> aliados, ArrayList<Unidad> enemigos) {
			if(this.getHabilidadElegida() == 0) {
				if(!enemigos.isEmpty()) {
					Unidad unidad = elegirObjetivo(enemigos);
					super.usarHabilidadOfensiva(unidad,() -> Habilidades.oxidarArmadura(unidad));
					this.setCdHabilidad1(1);
					this.setHabilidad1(false);
				}
			}
		}
		//METODOS JUGADOR////////////////////////////////////////////////////////////////////
		public void usarHabilidad(Unidad unidad, ArrayList<Unidad> unidades) {	
			if(this.getHabilidadElegida() == 0) {
				super.usarHabilidadOfensiva(unidad,() -> Habilidades.oxidarArmadura(unidad));
				this.setCdHabilidad1(1);
				this.setHabilidad1(false);
			}
		}
	//METODOS AUXILIARES////////////////////////////////////////////////////////////
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
		if(this.getTimerCastigo() > 0) {
			this.setTimerCastigo(this.getTimerCastigo() - 1);
		}
		if(this.getCdHabilidad1() == 0) {
			this.setHabilidad1(true);
		}
		else {
			this.setCdHabilidad1(this.getCdHabilidad1() - 1);
		}
		super.pasivaDeClase(aliados, enemigos);
	}
	public String[] getListaDeHabilidades() {return habilidades;}
	public void setListaDeHabilidades(String[] habilidades) {this.habilidades = habilidades;}
}