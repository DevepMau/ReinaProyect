package unidades;

import java.util.ArrayList;

import principal.Habilidades;
import principal.PanelDeJuego;
import principal.Zona;

public class PollueloEntusiasta extends Unidad {
	
	private String[] listaDeHabilidades = new String[1];

	public PollueloEntusiasta(Zona zona, boolean aliado, PanelDeJuego pdj) {
		super(zona, aliado, pdj);
		this.setNombre("");
		this.setTipo("Recluta");
		this.setClase("Polluelo Entusiasta");
		this.setIdFaccion(0);
		this.setHPMax(obtenerValorEntre(90,120));
		this.setHP(this.getHPMax());
		this.setSP(0);
		this.setSPMax(0);
		this.setAtq(obtenerValorEntre(8,12));
		this.setDef(obtenerValorEntre(25,35));
		this.setPCRT(0);
		this.setDCRT(1.5);
		this.setEva(0);
		this.setBloq(30);
		this.setVel(obtenerValorEntre(10,20));
		this.listaDeHabilidades[0] = "...";
		this.definirIdTez();
		if(this.getGenero() == 1) {
			this.asignarImagen( 4, "/imagenes/accesorios/banda", 3);
			this.asignarImagen( 3, "/imagenes/unisex/latas-"+this.getIdTez(), 3);
			this.asignarImagen( 2, "/imagenes/hombre/pantalon-1",3);
			this.asignarImagen( 1, "/imagenes/unisex/cuerpo"+this.getIdTez(), 3);
			this.asignarImagen( 0, "/imagenes/hombre/cabeza-boy-"+this.getIdTez(), 3);
		}
		else {
			this.asignarImagen( 4, "/imagenes/accesorios/banda", 3);
			this.asignarImagen( 3, "/imagenes/unisex/latas-"+this.getIdTez(), 3);
			this.asignarImagen( 2, "/imagenes/mujer/falda-"+this.getIdTez(),3);
			this.asignarImagen( 1, "/imagenes/unisex/cuerpo"+this.getIdTez(), 3);
			this.asignarImagen( 0, "/imagenes/mujer/cabeza-girl-"+this.getIdTez(), 3);
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
		Habilidades.darBonificacionAleatorio(aliados);
	}
	public String[] getListaDeHabilidades() {return listaDeHabilidades;}
	public void setListaDeHabilidades(String[] habilidades) {this.listaDeHabilidades = habilidades;}
}

