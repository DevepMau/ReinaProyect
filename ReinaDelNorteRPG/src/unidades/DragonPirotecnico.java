package unidades;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;

import principal.Habilidades;
import principal.PanelDeJuego;
import principal.Zona;

public class DragonPirotecnico extends Unidad{
	
	private int cargaExplosiva;
	private String[] listaDeHabilidades = new String[1];

	public DragonPirotecnico(Zona zona, boolean aliado, PanelDeJuego pdj) {
		super(zona, aliado, pdj);
		this.setNombre("");
		this.setTipo("Elite");
		this.setClase("Dragon Pirotecnico");
		this.setIdFaccion(2);
		this.setHPMax(obtenerValorEntre(180,250));
		this.setHP(this.getHPMax());
		this.setSPMax(0);
		this.setSP(this.getSPMax());
		this.setAtq(obtenerValorEntre(20,30));
		this.setDef(obtenerValorEntre(10,20));
		this.setPCRT(100);
		this.setDCRT(1.5);
		this.setEva(0);
		this.setVel(obtenerValorEntre(5,10));
		this.cargaExplosiva = elegirAleatorio(6);
		this.listaDeHabilidades[0] = "AÑO NUEVO";
		this.definirIdTez();
		if(this.getGenero() == 1) {
			this.asignarImagen(4, "/imagenes/accesorios/dragon", 3); 
			this.asignarImagen(3, "/imagenes/unisex/cañon-"+this.getIdTez(), 3); 
			this.asignarImagen(2, "/imagenes/hombre/pantalon-dragon", 3); 
			this.asignarImagen(1, "/imagenes/unisex/cuerpo"+this.getIdTez(), 3); 
			this.asignarImagen(0, "/imagenes/hombre/cabeza-boy-"+this.getIdTez(), 3); 
		}
		else {
			this.asignarImagen(4, "/imagenes/accesorios/dragon", 3); 
			this.asignarImagen(3, "/imagenes/unisex/cañon-"+this.getIdTez(), 3); 
			this.asignarImagen(2, "/imagenes/mujer/falda-dragon-"+this.getIdTez(), 3);
			this.asignarImagen(1, "/imagenes/unisex/cuerpo"+this.getIdTez(), 3); 
			this.asignarImagen(0, "/imagenes/mujer/cabeza-girl-"+this.getIdTez(), 3);
		}
		this.setAlturaDeAccesorio(5);
		this.setAlturaDeBarraHP(-20);
	}
	//METODOS PRINCIPALES///////////////////////////////////////////////////////////
	public void realizarAccion(ArrayList<Unidad> enemigos, ArrayList<Unidad> aliados) {
		if(cumpleReqDeHab2()) {
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
			FestejoDeAñoNuevo(unidades);	
			this.cargaExplosiva = 0;
		}
	}
	public void realizarAtaqueEnemigo(ArrayList<Unidad> unidades) {
		super.realizarAtaqueEnemigo(unidades);
		this.cargaExplosiva++;
		int daño = (this.getAtq() + this.getAtqMod())/3;
		for(Unidad unidad : unidades) {
			if(unidad != null) {
				if(!this.getUnidadObjetivoEnemigo().equals(unidad)) {
					int reduccion = (int) (daño * ((unidad.getDef() + unidad.getDefMod()) / 100.0));
			        int dañoFinal = Math.max(1, daño - reduccion);
			        unidad.setHP(unidad.getHP() - dañoFinal);
			        Habilidades.setearDaño(unidad, ""+dañoFinal, new Color(255, 155, 0));	
				}	
			}
		}
	}
	//METODOS DE JUGADOR////////////////////////////////////////////////////////////
	public void usarHabilidad(Unidad unidad, ArrayList<Unidad> unidades) {
		FestejoDeAñoNuevo(unidades);
		this.cargaExplosiva = 0;
	}
	public void realizarAtaque(Unidad unidad, ArrayList<Unidad> unidades) {
		super.realizarAtaque(unidad, unidades);
		this.cargaExplosiva++;
		int daño = (this.getAtq() + this.getAtqMod())/3;
		for(Unidad objetivo : unidades) {
			if(objetivo != null) {
				if(!unidad.equals(objetivo)) {
					int reduccion = (int) (daño * ((objetivo.getDef() + objetivo.getDefMod()) / 100.0));
			        int dañoFinal = Math.max(1, daño - reduccion);
			        objetivo.setHP(objetivo.getHP() - dañoFinal);
			        Habilidades.setearDaño(objetivo, ""+dañoFinal, new Color(255, 155, 0));
				}
			}
		}
	}
	//HABILIDADES///////////////////////////////////////////////////////////////////
	public void FestejoDeAñoNuevo(ArrayList<Unidad> unidades) {
		this.setDCRT(2.0);
		Unidad unidad = elegirObjetivoMasFuerte(unidades);
		this.realizarAtaque(unidad, unidades);
		this.setDCRT(1.5);
		int daño = (this.getAtq() + this.getAtqMod())/2;
		for(Unidad objetivo : unidades) {
			if(objetivo != null) {
				int reduccion = (int) (daño * ((objetivo.getDef() + objetivo.getDefMod()) / 100.0));
		        int dañoFinal = Math.max(1, daño - reduccion);
		        objetivo.setHP(objetivo.getHP() - dañoFinal);  
		        Habilidades.setearEfectoDeEstado(objetivo, "BURN!", new Color(255, 155, 0));
				objetivo.setTimerIncendiado(10);
			}
		}
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
	public boolean cumpleReqDeHab2() {
		if(this.cargaExplosiva >= 5) {
			return true;
		}
		return false;
	}
	public void efectosVisualesPersonalizados(Graphics2D g2) {
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 18f));
		g2.setColor(Color.white);
		if(this.cargaExplosiva == 1) {
			g2.fillRoundRect(this.getPosX()+8, this.getPosY()-24+this.getAlturaBarraHP(), 10, 10, 50, 50);
		}
		else if(this.cargaExplosiva == 2) {
			g2.fillRoundRect(this.getPosX()+8, this.getPosY()-24+this.getAlturaBarraHP(), 10, 10, 50, 50);
			g2.fillRoundRect(this.getPosX()+18, this.getPosY()-24+this.getAlturaBarraHP(), 10, 10, 50, 50);
		}
		else if(this.cargaExplosiva == 3) {
			g2.fillRoundRect(this.getPosX()+8, this.getPosY()-24+this.getAlturaBarraHP(), 10, 10, 50, 50);
			g2.fillRoundRect(this.getPosX()+18, this.getPosY()-24+this.getAlturaBarraHP(), 10, 10, 50, 50);
			g2.fillRoundRect(this.getPosX()+28, this.getPosY()-24+this.getAlturaBarraHP(), 10, 10, 50, 50);
		}
		else if(this.cargaExplosiva == 4) {
			g2.fillRoundRect(this.getPosX()+8, this.getPosY()-24+this.getAlturaBarraHP(), 10, 10, 50, 50);
			g2.fillRoundRect(this.getPosX()+18, this.getPosY()-24+this.getAlturaBarraHP(), 10, 10, 50, 50);
			g2.fillRoundRect(this.getPosX()+28, this.getPosY()-24+this.getAlturaBarraHP(), 10, 10, 50, 50);
			g2.fillRoundRect(this.getPosX()+38, this.getPosY()-24+this.getAlturaBarraHP(), 10, 10, 50, 50);
		}
		else if(this.cargaExplosiva >= 5) {
			g2.setColor(Color.RED);
			g2.fillRoundRect(this.getPosX()+8, this.getPosY()-24+this.getAlturaBarraHP(), 10, 10, 50, 50);
			g2.fillRoundRect(this.getPosX()+18, this.getPosY()-24+this.getAlturaBarraHP(), 10, 10, 50, 50);
			g2.fillRoundRect(this.getPosX()+28, this.getPosY()-24+this.getAlturaBarraHP(), 10, 10, 50, 50);
			g2.fillRoundRect(this.getPosX()+38, this.getPosY()-24+this.getAlturaBarraHP(), 10, 10, 50, 50);
			g2.fillRoundRect(this.getPosX()+48, this.getPosY()-24+this.getAlturaBarraHP(), 10, 10, 50, 50);
		}
	}
	public void pasivaDeClase(ArrayList<Unidad> aliados, ArrayList<Unidad> enemigos) {
		super.pasivaDeClase(aliados, enemigos);
	}
	public void configurarTipoDeaccion() {
		if(this.getHabilidadElegida() == 0) {
			this.setAccion("ATACAR");
			this.setObjetivoUnico(false);
		}
		else {
			this.setAccion("");
		}
	}
	public String[] getListaDeHabilidades() {return listaDeHabilidades;}
	public void setListaDeHabilidades(String[] habilidades) {this.listaDeHabilidades = habilidades;}
}