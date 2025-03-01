package unidades;

import java.util.ArrayList;
import principal.PanelDeJuego;
import principal.Zona;

public class NiñoCheung extends Unidad {
	
	private String[] listaDeHabilidades = new String[1];

	public NiñoCheung(Zona zona, boolean aliado, PanelDeJuego pdj) {
		super(zona, aliado, pdj);
		this.setNombre("");
		this.setTipo("Recluta");
		this.setClase("Niño Cheung");
		this.setIdFaccion(3);
		this.setHPMax(obtenerValorEntre(90,120));
		this.setHP(this.getHPMax());
		this.setSP(0);
		this.setSPMax(0);
		this.setAtq(obtenerValorEntre(12,15));
		this.setDef(obtenerValorEntre(10,20));
		this.setPCRT(5);
		this.setDCRT(10);
		this.setEva(25);
		this.setVel(obtenerValorEntre(10,20));
		this.listaDeHabilidades[0] = "...";
		this.definirIdTez();
		if(this.getGenero() == 1) {
			this.asignarImagen(3, "/imagenes/unisex/manos-pose-"+this.getIdTez(), 3); 
			this.asignarImagen(2, "/imagenes/hombre/pantalon-pose", 3); 
			this.asignarImagen(1, "/imagenes/hombre/cuerpo-chino-boy", 3); 
			this.asignarImagen(0, "/imagenes/hombre/cabeza-boy-"+this.getIdTez(), 3); 
		}
		else {
			this.asignarImagen(3, "/imagenes/unisex/manos-pose-"+this.getIdTez(), 3);
			this.asignarImagen(2, "/imagenes/mujer/falda-pose-"+this.getIdTez(), 3);
			this.asignarImagen(1, "/imagenes/mujer/cuerpo-chino-girl", 3); 
			this.asignarImagen(0, "/imagenes/mujer/cabeza-girl-"+this.getIdTez(), 3);
		}
	}
	//METODO PRINCIPAL//////////////////////////////////////////////////////////////////
	public void realizarAccion(ArrayList<Unidad> enemigos, ArrayList<Unidad> aliados) {
		realizarAtaqueEnemigo(enemigos);
		this.pasivaDeClase(aliados, enemigos);
	}
	//METODOS ENEMIGO////////////////////////////////////////////////////////////////////
	//METODOS JUGADOR////////////////////////////////////////////////////////////////////
	//HABILIDADES////////////////////////////////////////////////////////////////////////
	//METODOS AUXILIARES/////////////////////////////////////////////////////////////////
	public void configurarTipoDeaccion() {
		if(this.getHabilidadElegida() == 0) {
			this.setAccion("ATACAR");
		}
		else {
			this.setAccion("");
		}
	}
	public void pasivaDeClase(ArrayList<Unidad> aliados, ArrayList<Unidad> enemigos) {
		super.pasivaDeClase(aliados, enemigos);
	}
	public String[] getListaDeHabilidades() {return listaDeHabilidades;}
	public void setListaDeHabilidades(String[] habilidades) {this.listaDeHabilidades = habilidades;}
}
