package unidades;

import java.util.ArrayList;
import principal.Habilidades;
import principal.PanelDeJuego;
import principal.Zona;

public class PayadorPicante extends Unidad {
	
	private String[] habilidades = new String[2];
	private int spHabilidad1;
	private int spHabilidad2;

	public PayadorPicante(Zona zona, boolean aliado, PanelDeJuego pdj) {
		super(zona, aliado, pdj);
		this.setNombre("");
		this.setTipo("Especialista");
		this.setClase("Payador Picante");
		this.setIdFaccion(1);
		this.setHPMax(obtenerValorEntre(110,130));
		this.setHP(this.getHPMax());
		this.setSPMax(obtenerValorEntre(90,150));
		this.setSP(this.getSPMax());
		this.setAtq(obtenerValorEntre(5,8));
		this.setDef(obtenerValorEntre(5,10));
		this.setVel(obtenerValorEntre(10,20));
		this.setPCRT(0);
		this.spHabilidad1 = 15;
		this.spHabilidad2 = 50;
		this.habilidades[0] = "CHICANEAR";
		this.habilidades[1] = "MOTIVAR";
		this.definirIdTez();
		if(this.getGenero() == 1) {
			this.asignarImagen(4, "/imagenes/accesorios/boina1", 3); 
			this.asignarImagen(3, "/imagenes/unisex/guitarra-"+this.getIdTez(), 3); 
			this.asignarImagen(2, "/imagenes/hombre/pantalon-1", 3); 
			this.asignarImagen(1, "/imagenes/unisex/cuerpo"+this.getIdTez(), 3); 
			this.asignarImagen(0, "/imagenes/hombre/cabeza-boy-"+this.getIdTez(), 3); 	
		}
		else {
			this.asignarImagen(4, "/imagenes/accesorios/boina1",3);
			this.asignarImagen(3, "/imagenes/unisex/guitarra-"+this.getIdTez(), 3); 
			this.asignarImagen(2, "/imagenes/mujer/falda-"+this.getIdTez(), 3);
			this.asignarImagen(1, "/imagenes/unisex/cuerpo"+this.getIdTez(), 3); 
			this.asignarImagen(0, "/imagenes/mujer/cabeza-girl-"+this.getIdTez(), 3);
		}
	}
	//METODO PRINCIPAL//////////////////////////////////////////////////////////////////
	public void realizarAccion(ArrayList<Unidad> enemigos, ArrayList<Unidad> aliados) {
		int accion = elegirAleatorio(5);
		if(accion <= 3 && cumpleReqDeHab1()) {
			int nroHabilidad = elegirAleatorio(4);
			if(nroHabilidad == 0 &&  cumpleReqDeHab2()) {
				this.setHabilidadElegida(1);
			}
			else {
				this.setHabilidadElegida(0);
			}
			usarHabilidadEnemigo(aliados, enemigos);
			
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
				super.usarHabilidadOfensiva(unidad, () -> Habilidades.oxidarArmadura(unidad));
				this.setCdHabilidad1(1);
				this.setHabilidad1(false);
			}
		}
		else {
			if(!aliados.isEmpty()) {
				this.setSP(this.getSP() - this.spHabilidad2);
				for(Unidad unidad : aliados) {
					motivar(unidad);
				}
				this.setCdHabilidad2(8);
				this.setHabilidad2(false);
			}	
		}
	}
	//METODOS JUGADOR////////////////////////////////////////////////////////////////////
	public void usarHabilidad(Unidad unidad, ArrayList<Unidad> unidades) {	
		if(this.getHabilidadElegida() == 0) {
			super.usarHabilidadOfensiva(unidad, () -> Habilidades.oxidarArmadura(unidad));
			this.setCdHabilidad1(1);
			this.setHabilidad1(false);
		}
		else {
			this.setSP(this.getSP() - this.spHabilidad2);
			for(Unidad unidadObjetivo : unidades) {
				motivar(unidadObjetivo);
			}	
			this.setCdHabilidad2(8);
			this.setHabilidad2(false);
		}
		
	}
	//HABILIDADES////////////////////////////////////////////////////////////////////////
	public void motivar(Unidad unidad) {
		if(unidad != null) {
			pdj.ReproducirSE(7);
			Habilidades.motivarUnidad(unidad);
		}
	}
	//METODOS AUXILIARES/////////////////////////////////////////////////////////////////
	public void configurarTipoDeaccion() {
		if(this.getHabilidadElegida() == 0) {
			this.setAccion("ATACAR");
			this.setObjetivoUnico(true);
		}
		else {
			this.setAccion("APOYAR");
			this.setObjetivoUnico(false);
		}
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
	}
	public String[] getListaDeHabilidades() {return habilidades;}
	public void setListaDeHabilidades(String[] habilidades) {this.habilidades = habilidades;}
}