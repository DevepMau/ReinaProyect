package unidades;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import principal.PanelDeJuego;
import principal.Zona;

public class PuñoFurioso extends Unidad {
	
	private String[] listaDeHabilidades = new String[1];

	public PuñoFurioso(Zona zona, boolean aliado, PanelDeJuego pdj) {
		super(zona, aliado, pdj);
		this.setNombre("");
		this.setTipo("Combatiente");
		this.setClase("Puño Furioso");
		this.setIdFaccion(3);
		this.setHPMax(obtenerValorEntre(110,130));
		this.setHP(this.getHPMax());
		this.setSPMax(0);
		this.setSP(this.getSPMax());
		this.setAtq(obtenerValorEntre(13,18));
		this.setDef(obtenerValorEntre(10,20));
		this.setPCRT(25);
		this.setDCRT(1.5);
		this.setEva(25);
		this.setVel(obtenerValorEntre(10,20));
		this.listaDeHabilidades[0] = "DOBLE IMPACTO";
		this.definirIdTez();
		if(this.getGenero() == 1) {
			this.asignarImagen(3, "/imagenes/unisex/nunchaku-"+this.elegirDiseñoNunchaku()+"-"+this.getIdTez(), 3); 
			this.asignarImagen(2, "/imagenes/unisex/cortos-pose-"+this.getIdTez(), 3); 
			this.asignarImagen(1, "/imagenes/unisex/cuerpo-chaqueta", 3); 
			this.asignarImagen(0, "/imagenes/hombre/cabeza-boy-"+this.getIdTez(), 3); 
		}
		else {
			this.asignarImagen(3, "/imagenes/unisex/nunchaku-"+this.elegirDiseñoNunchaku()+"-"+this.getIdTez(), 3);
			this.asignarImagen(2, "/imagenes/unisex/cortos-pose-"+this.getIdTez(), 3); 
			this.asignarImagen(1, "/imagenes/unisex/cuerpo-chaqueta", 3); 
			this.asignarImagen(0, "/imagenes/mujer/cabeza-coleta-"+this.getIdTez(), 3);
		}
	}
	//METODO PRINCIPAL//////////////////////////////////////////////////////////////////
	public void realizarAccion(ArrayList<Unidad> enemigos, ArrayList<Unidad> aliados) {
		if(cumpleReqDeHab1()) {
			this.setHabilidadElegida(0);
			usarHabilidadEnemigo(enemigos);	
		}
		else {
			realizarAtaqueEnemigo(enemigos);
		}
		this.pasivaDeClase(aliados, enemigos);
	}
	public void recibirDaño(int daño, boolean isCritical, Unidad unidad) {
		super.recibirDaño(daño, isCritical, unidad);
		if(this.getPuñosAcumulados() < 2) {
			this.setPuñosAcumulados(this.getPuñosAcumulados() + 1);
		}
	}
	//METODOS ENEMIGO////////////////////////////////////////////////////////////////////
	public void usarHabilidadEnemigo(ArrayList<Unidad> unidades) {
		Unidad objetivo = elegirObjetivoConMayorHP(unidades);
		dobleGolpe(objetivo);
		this.setPuñosAcumulados(0);
	}
	//METODOS JUGADOR////////////////////////////////////////////////////////////////////
	public void usarHabilidad(Unidad unidad, ArrayList<Unidad> unidades) {
		dobleGolpe(unidad);
		this.setPuñosAcumulados(0);
	}
	//HABILIDADES////////////////////////////////////////////////////////////////////////
	public void dobleGolpe(Unidad unidad) {
		int daño = this.getAtq() + this.getAtqMod();
		unidad.recibirGolpesMúltiples(daño, 2 , true, this);
	}
	//METODOS AUXILIARES/////////////////////////////////////////////////////////////////
	public Unidad elegirObjetivoConMayorHP(ArrayList<Unidad> unidades) {
		Unidad unidadSeleccionada = null;
	    int mayorPorcentajeHP = Integer.MIN_VALUE;
	    for (Unidad unidad : unidades) {
	    	int HPUnidad = unidad.getHPMax();
	        if (HPUnidad > mayorPorcentajeHP) {
	        	mayorPorcentajeHP = HPUnidad;
	            unidadSeleccionada = unidad;
	        }
	    }
	    return unidadSeleccionada;
	}
	public boolean cumpleReqDeHab1() {
		if(this.getPuñosAcumulados() >= 2) {
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
	public void efectosVisualesPersonalizados(Graphics2D g2) {
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 18f));
		g2.setColor(Color.white);
		if(this.getPuñosAcumulados() == 1) {
			g2.drawString("+", this.getPosX()+8, this.getPosY()-12+this.getAlturaBarraHP());
			g2.fillRoundRect(this.getPosX()+16, this.getPosY()-24+this.getAlturaBarraHP(), 10, 10, 50, 50);
		}
		else if(this.getPuñosAcumulados() >= 2) {
			g2.setColor(Color.white);
			g2.drawString("+", this.getPosX()+8, this.getPosY()-12+this.getAlturaBarraHP());
			g2.setColor(Color.yellow);
			g2.fillRoundRect(this.getPosX()+16, this.getPosY()-24+this.getAlturaBarraHP(), 10, 10, 50, 50);
			g2.fillRoundRect(this.getPosX()+26, this.getPosY()-24+this.getAlturaBarraHP(), 10, 10, 50, 50);
		}
	}
	
	public void pasivaDeClase(ArrayList<Unidad> aliados, ArrayList<Unidad> enemigos) {
		super.pasivaDeClase(aliados, enemigos);
	}
	public String[] getListaDeHabilidades() {return listaDeHabilidades;}
	public void setListaDeHabilidades(String[] habilidades) {this.listaDeHabilidades = habilidades;}
}
