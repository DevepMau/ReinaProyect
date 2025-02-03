package unidades;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import principal.PanelDeJuego;
import principal.Zona;

public class AspiranteADragon extends Unidad{
	
	private String[] listaDeHabilidades = new String[1];

	public AspiranteADragon(Zona zona, boolean aliado, PanelDeJuego pdj) {
		super(zona, aliado, pdj);
		this.setNombre("");
		this.setTipo("Elite");
		this.setClase("Aspirante A Dragon");
		this.setIdFaccion(3);
		this.setHPMax(obtenerValorEntre(100,130));
		this.setHP(this.getHPMax());
		this.setSPMax(0);
		this.setSP(this.getSPMax());
		this.setAtq(obtenerValorEntre(12,15));
		this.setDef(obtenerValorEntre(4,7));
		this.setPCRT(0.5);
		this.setDCRT(2);
		this.setEva(0.5);
		this.setVel(obtenerValorEntre(15,20));
		this.setPuñosAcumulados(1);
		this.listaDeHabilidades[0] = "COMBO LETAL";
		this.generarCuerpo();
	}
	//METODOS PRINCIPALES///////////////////////////////////////////////////////////
	public void realizarAccion(ArrayList<Unidad> enemigos, ArrayList<Unidad> aliados) {
		if(cumpleReqDeHab1()) {
			setHabilidadElegida(0);
			usarHabilidadEnemigo(enemigos);
		}
		else {
			realizarAtaqueEnemigo(enemigos);
		}
		this.pasivaDeClase(aliados, enemigos);
	}
	
	public void recibirDaño(int daño, boolean isCritical, int duracionSacudida) {
		super.recibirDaño(daño, isCritical, duracionSacudida);
		if(this.getPuñosAcumulados() < 5) {
			this.setPuñosAcumulados(this.getPuñosAcumulados() + 1);
		}
	}
	public void evadirAtaque() {
		super.evadirAtaque();
		if(this.getPuñosAcumulados() < 5) {
			this.setPuñosAcumulados(this.getPuñosAcumulados() + 1);
		}
	}
	//METODOS DE ENEMIGO////////////////////////////////////////////////////////////
	public void usarHabilidadEnemigo(ArrayList<Unidad> unidades) {
		if(this.getHabilidadElegida() == 0) {
			if(this.getPuñosAcumulados() >= 5) {
				this.setAtqMod(this.getAtqMod() + this.obtenerValorEntre(1, this.getPuñosAcumulados()));
				this.setVelMod(this.getVelMod() + this.obtenerValorEntre(1, this.getPuñosAcumulados()));
				this.setPcrtMod(this.getPcrtMod() + 0.05);
				this.setEvaMod(this.getEvaMod() + 0.05);
			}
			Unidad unidad = elegirObjetivoMasFuerte(unidades);
			comboLetal(unidad);	
		}
	}
	//METODOS DE JUGADOR////////////////////////////////////////////////////////////
	public void usarHabilidad(Unidad unidad, ArrayList<Unidad> unidades) {
		if(this.getPuñosAcumulados() >= 5) {
			this.setAtqMod(this.getAtqMod() + this.obtenerValorEntre(1, this.getPuñosAcumulados()));
			this.setVelMod(this.getVelMod() + this.obtenerValorEntre(1, this.getPuñosAcumulados()));
			this.setPcrtMod(this.getPcrtMod() + 0.05);
			this.setEvaMod(this.getEvaMod() + 0.05);
		}
		comboLetal(unidad);
	}
	//HABILIDADES///////////////////////////////////////////////////////////////////
	public void comboLetal(Unidad unidad) {
		boolean isCritical = Math.random() <= (this.getPCRT() + this.getPcrtMod());   
		if(unidad != null) {
			int daño = Math.max(1, (this.getAtq() + this.getAtqMod()) - (unidad.getDef() + unidad.getDefMod()));
	    	if(this.getHPMax() < unidad.getHPMax()) {
	    		daño += (unidad.getHPMax()/10);
	    	}
			if (isCritical) {
				daño *= (this.getDCRT() + this.getDcrtMod());
			}
			unidad.recibirGolpesMúltiples(daño, this.getPuñosAcumulados() , isCritical);
			contarFaltas(unidad, this.getPuñosAcumulados());
			this.reflejarDaño(unidad, daño);
			this.robarVida(daño, unidad);
			this.setPuñosAcumulados(1);
		}
	}
	//METODOS AUXILIARES////////////////////////////////////////////////////////////
	public void efectosVisualesPersonalizados(Graphics2D g2) {
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 18f));
		g2.setColor(Color.white);
		if(this.getPuñosAcumulados() == 2) {
			g2.drawString("+", this.getPosX()+8, this.getPosY()-12+this.getAlturaBarraHP());
			g2.fillRoundRect(this.getPosX()+16, this.getPosY()-24+this.getAlturaBarraHP(), 10, 10, 50, 50);
		}
		else if(this.getPuñosAcumulados() == 3) {
			g2.setColor(Color.white);
			g2.drawString("+", this.getPosX()+8, this.getPosY()-12+this.getAlturaBarraHP());
			g2.setColor(Color.yellow);
			g2.fillRoundRect(this.getPosX()+16, this.getPosY()-24+this.getAlturaBarraHP(), 10, 10, 50, 50);
			g2.fillRoundRect(this.getPosX()+26, this.getPosY()-24+this.getAlturaBarraHP(), 10, 10, 50, 50);
		}
		else if(this.getPuñosAcumulados() == 4) {
			g2.setColor(Color.white);
			g2.drawString("+", this.getPosX()+8, this.getPosY()-12+this.getAlturaBarraHP());
			g2.setColor(Color.ORANGE);
			g2.fillRoundRect(this.getPosX()+16, this.getPosY()-24+this.getAlturaBarraHP(), 10, 10, 50, 50);
			g2.fillRoundRect(this.getPosX()+26, this.getPosY()-24+this.getAlturaBarraHP(), 10, 10, 50, 50);
			g2.fillRoundRect(this.getPosX()+36, this.getPosY()-24+this.getAlturaBarraHP(), 10, 10, 50, 50);
		}
		else if(this.getPuñosAcumulados() >= 5) {
			g2.setColor(Color.white);
			g2.drawString("+", this.getPosX()+8, this.getPosY()-12+this.getAlturaBarraHP());
			g2.setColor(Color.RED);
			g2.fillRoundRect(this.getPosX()+16, this.getPosY()-24+this.getAlturaBarraHP(), 10, 10, 50, 50);
			g2.fillRoundRect(this.getPosX()+26, this.getPosY()-24+this.getAlturaBarraHP(), 10, 10, 50, 50);
			g2.fillRoundRect(this.getPosX()+36, this.getPosY()-24+this.getAlturaBarraHP(), 10, 10, 50, 50);
			g2.fillRoundRect(this.getPosX()+46, this.getPosY()-24+this.getAlturaBarraHP(), 10, 10, 50, 50);
		}
	}
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
		if(this.getPuñosAcumulados() > 1) {
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
	public String[] getListaDeHabilidades() {return listaDeHabilidades;}
	public void setListaDeHabilidades(String[] habilidades) {this.listaDeHabilidades = habilidades;}
}