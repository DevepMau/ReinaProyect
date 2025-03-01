package unidades;

import java.util.ArrayList;

import principal.Habilidades;
import principal.PanelDeJuego;
import principal.Zona;

public class PlumaBlanca extends Unidad {
	
	private String[] listaDeHabilidades = new String[1];

	public PlumaBlanca(Zona zona, boolean aliado, PanelDeJuego pdj) {
		super(zona, aliado, pdj);
		this.setNombre("");
		this.setTipo("Combatiente");
		this.setClase("Pluma Blanca");
		this.setIdFaccion(0);
		this.setHPMax(obtenerValorEntre(110,130));
		this.setHP(this.getHPMax());
		this.setSP(0);
		this.setSPMax(0);
		this.setAtq(obtenerValorEntre(11,13));
		this.setDef(obtenerValorEntre(20,30));
		this.setPCRT(15);
		this.setDCRT(1.5);
		this.setEva(0);
		this.setBloq(0);
		this.setVel(obtenerValorEntre(10,20));
		this.listaDeHabilidades[0] = "...";
		this.definirIdTez();
		if(this.getGenero() == 1) {
			this.asignarImagen( 4, "/imagenes/accesorios/banda", 3);
			this.asignarImagen( 3, "/imagenes/unisex/manos-"+this.getIdTez(), 3);
			this.asignarImagen( 2, "/imagenes/hombre/pantalon-1",3);
			this.asignarImagen( 1, "/imagenes/unisex/cuerpo-tapado-"+this.getIdTez(), 3);
			this.asignarImagen( 0, "/imagenes/hombre/cabeza-boy-"+this.getIdTez(), 3);
		}
		else {
			this.asignarImagen( 4, "/imagenes/accesorios/banda", 3);
			this.asignarImagen( 3, "/imagenes/unisex/manos-"+this.getIdTez(), 3);
			this.asignarImagen( 2, "/imagenes/mujer/falda-"+this.getIdTez(),3);
			this.asignarImagen( 1, "/imagenes/unisex/cuerpo-tapado-"+this.getIdTez(), 3);
			this.asignarImagen( 0, "/imagenes/mujer/cabeza-girl-"+this.getIdTez(), 3);
		}
	}
	//METODO PRINCIPAL//////////////////////////////////////////////////////////////////
	public void realizarAccion(ArrayList<Unidad> enemigos, ArrayList<Unidad> aliados) {
		super.realizarAccion(enemigos, aliados);
		this.pasivaDeClase(aliados, enemigos);
	}
	public void recibirDaño(int daño, boolean isCritical, Unidad unidad) {
		super.recibirDaño(daño, isCritical, unidad);
		Habilidades.activarDeterminacion(this);
	}
	//METODOS ENEMIGO////////////////////////////////////////////////////////////////////
	public void usarHabilidadEnemigo(ArrayList<Unidad> unidades) {
		if(this.getHabilidadElegida() == 0) {
			if(!unidades.isEmpty()) {
				Unidad unidad = elegirObjetivo(unidades);
				super.usarHabilidadOfensiva(unidad, true, true, 0,() -> Habilidades.ataqueActivadorHemorragia(this, unidad));
				this.setCdHabilidad1(3);
				this.setHabilidad1(false);
			}
		}
	}
	//METODOS JUGADOR////////////////////////////////////////////////////////////////////
	//METODOS AUXILIARES/////////////////////////////////////////////////////////////////
	public boolean cumpleReqDeHab1() {
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
		if(this.isDeterminado()) {
			this.setHP(this.getHP() - (this.getHPMax()/20));
		}
		super.pasivaDeClase(aliados, enemigos);
	}
	public String[] getListaDeHabilidades() {return listaDeHabilidades;}
	public void setListaDeHabilidades(String[] habilidades) {this.listaDeHabilidades = habilidades;}
}
