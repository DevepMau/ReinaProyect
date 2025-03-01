package unidades;

import java.util.ArrayList;
import principal.Habilidades;
import principal.PanelDeJuego;
import principal.Zona;

public class HeroeFederal extends Unidad{
	
	private int spHabilidad1;
	private String[] listaDeHabilidades = new String[1];

	public HeroeFederal(Zona zona, boolean aliado, PanelDeJuego pdj) {
		super(zona, aliado, pdj);
		this.setNombre("");
		this.setTipo("Elite");
		this.setClase("Heroe Federal");
		this.setIdFaccion(1);
		this.setHPMax(obtenerValorEntre(150,220));
		this.setHP(this.getHPMax());
		this.setSPMax(obtenerValorEntre(70,90));
		this.setSP(this.getSPMax());
		this.setAtq(obtenerValorEntre(18,25));
		this.setDef(obtenerValorEntre(30,40));
		this.setPCRT(5);
		this.setDCRT(2);
		this.setEva(15);
		this.setBloq(15);
		this.setVel(obtenerValorEntre(30,50));
		this.spHabilidad1 = 15;
		this.listaDeHabilidades[0] = "EXPULSAR";
		this.definirIdTez();
		if(this.getGenero() == 1) {
			this.asignarImagen( 4, "/imagenes/accesorios/boina1",3);
			this.asignarImagen( 3, "/imagenes/unisex/caballo-"+this.getIdTez(), 3);
			this.asignarImagen( 2, "/imagenes/hombre/bici-boy", 3);
			this.asignarImagen( 1, "/imagenes/unisex/cuerpo-heroe", 3);
			this.asignarImagen( 0, "/imagenes/hombre/cabeza-boy-"+this.getIdTez(), 3);		
		}
		else {
			this.asignarImagen( 4, "/imagenes/accesorios/boina1",3);
			this.asignarImagen( 3, "/imagenes/unisex/caballo-"+this.getIdTez(), 3);
			this.asignarImagen( 2, "/imagenes/mujer/bici-girl-"+this.getIdTez(), 3);
			this.asignarImagen( 1, "/imagenes/unisex/cuerpo-heroe", 3);
			this.asignarImagen( 0, "/imagenes/mujer/cabeza-girl-"+this.getIdTez(), 3);
		}
		this.setAlturaPorClase(-10);
	}
	//METODOS PRINCIPALES///////////////////////////////////////////////////////////
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
	//METODOS DE ENEMIGO////////////////////////////////////////////////////////////
	public void usarHabilidadEnemigo(ArrayList<Unidad> unidades) {
		if(this.getHabilidadElegida() == 0) {
			Unidad unidad = elegirObjetivoMasFuerte(unidades);
			super.usarHabilidadOfensiva(unidad, false, true, this.getVelMod() ,() -> Habilidades.destruirMovilidad(unidad));
			this.setCdHabilidad1(1);
			this.setHabilidad1(false);	
		}
	}
	//METODOS DE JUGADOR////////////////////////////////////////////////////////////
	public void usarHabilidad(Unidad unidad, ArrayList<Unidad> unidades) {
		super.usarHabilidadOfensiva(unidad, false, true, this.getVelMod() ,() -> Habilidades.destruirMovilidad(unidad));
		this.setCdHabilidad1(1);
		this.setHabilidad1(false);
	}
	//METODOS AUXILIARES////////////////////////////////////////////////////////////
	public Unidad elegirObjetivoMasFuerte(ArrayList<Unidad> unidades) {
	    Unidad unidadSeleccionada = null;
	    int mayorPorcentajeATQ = Integer.MIN_VALUE;
	    for (Unidad unidad : unidades) {
	    	int ATQUnidad = unidad.getAtq() + unidad.getAtqMod();
	        if (ATQUnidad > mayorPorcentajeATQ) {
	        	mayorPorcentajeATQ = ATQUnidad;
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
	public void configurarTipoDeaccion() {
		if(this.getHabilidadElegida() == 0) {
			this.setAccion("ATACAR");
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