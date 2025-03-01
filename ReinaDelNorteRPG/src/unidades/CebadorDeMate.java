package unidades;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;

import principal.Habilidades;
import principal.PanelDeJuego;
import principal.Zona;

public class CebadorDeMate extends Unidad{
	
	private Image imagen = null;
	private int costeHabilidad1;
	private int opcion = this.elegirAleatorio(4);
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
		this.definirIdTez();
		if(this.getGenero() == 1) {
			this.asignarImagen( 4, "/imagenes/accesorios/boina1",3);
			this.asignarImagen( 3, "/imagenes/unisex/mates-"+this.elegirDiseñoMate()+"-"+this.getIdTez(), 3);
			this.asignarImagen( 2, "/imagenes/hombre/pantalon-1",3);
			this.asignarImagen( 1, "/imagenes/unisex/cuerpo"+this.getIdTez(), 3);
			this.asignarImagen( 0, "/imagenes/hombre/cabeza-boy-"+this.getIdTez(), 3);		
		}
		else {
			this.asignarImagen( 4, "/imagenes/accesorios/boina1",3);
			this.asignarImagen( 3, "/imagenes/unisex/mates-"+this.elegirDiseñoMate()+"-"+this.getIdTez(), 3);
			this.asignarImagen( 2, "/imagenes/mujer/falda-"+this.getIdTez(), 3);
			this.asignarImagen( 1, "/imagenes/unisex/cuerpo"+this.getIdTez(), 3);
			this.asignarImagen( 0, "/imagenes/mujer/cabeza-girl-"+this.getIdTez(), 3);
		}
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
		this.setSP(this.getSP() - this.costeHabilidad1);
		if(unidad.getIdFaccion() == 1) {
			Habilidades.restaurarHPPlano(unidad, unidad.getHPMax()/4);
		}
		//int opcion = 1;
		if(opcion == 0) {
			Habilidades.aumentarAgilidad(unidad);	
		}
		else if(opcion == 1) {
			Habilidades.aumentarProteccion(unidad);
		}
		else if(opcion == 2) {
			Habilidades.aumentarAgresividad(unidad);
		}
		else {
			Habilidades.potenciarUnidad(unidad);	
		}
		this.setCdHabilidad1(0);
		this.setHabilidad1(false);
		opcion = this.elegirAleatorio(4);
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
	public void efectosVisualesPersonalizados(Graphics2D g2) {
		if(opcion == 0) {
			imagen = pdj.img.mateVerde;
		}
		else if(opcion == 1) {
			imagen = pdj.img.mateAzul;
		}
		else if(opcion == 2) {
			imagen = pdj.img.mateRojo;
		}
		else {
			imagen = pdj.img.mateAmarillo;
		}
		g2.drawImage(imagen, this.getPosX() - 8, this.getPosY() - 16, null);
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
