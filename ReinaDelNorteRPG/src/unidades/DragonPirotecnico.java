package unidades;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
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
		this.setHPMax(obtenerValorEntre(130,160));
		this.setHP(this.getHPMax());
		this.setSPMax(0);
		this.setSP(this.getSPMax());
		this.setAtq(obtenerValorEntre(18,27));
		this.setDef(obtenerValorEntre(2,5));
		this.setPCRT(0);
		this.setDCRT(1);
		this.setEva(0);
		this.setVel(obtenerValorEntre(5,7));
		this.cargaExplosiva = 0;
		this.listaDeHabilidades[0] = "AÑO NUEVO";
		this.generarCuerpo();
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
		Unidad objetivo = elegirObjetivo(unidades); 
		if(objetivo != null) {
			int daño = Math.max(1, (this.getAtq() + this.getAtqMod()) - (objetivo.getDef() + objetivo.getDefMod()));
			objetivo.recibirDaño(daño, true, 20);
			contarFaltas(objetivo);
			this.cargaExplosiva++;
			for (Unidad unidadAledaña : unidades) {
	            if (!unidadAledaña.equals(objetivo)) { // Evitar aplicar daño a la unidad objetivo
	                unidadAledaña.recibirDaño(daño / 3, false, 20);
	            }
	        }
			this.robarVida(daño, objetivo);
		}
	}
	//METODOS DE JUGADOR////////////////////////////////////////////////////////////
	public void usarHabilidad(Unidad unidad, ArrayList<Unidad> unidades) {
		FestejoDeAñoNuevo(unidades);
		this.cargaExplosiva = 0;
	}
	public void realizarAtaque(Unidad unidad, ArrayList<Unidad> unidades) {
		if(unidad != null) {
			int daño = Math.max(1, (this.getAtq() + this.getAtqMod()) - (unidad.getDef() + unidad.getDefMod())); 	 
			unidad.recibirDaño(daño, true, 20);
			contarFaltas(unidad);
			this.cargaExplosiva++;
			for (Unidad unidadAledaña : unidades) {
	            if (!unidadAledaña.equals(unidad)) { // Evitar aplicar daño a la unidad objetivo
	                unidadAledaña.recibirDaño(daño / 3, false, 20);
	            }
	        }
			this.robarVida(daño, unidad);
		}
	}
	//HABILIDADES///////////////////////////////////////////////////////////////////
	public void FestejoDeAñoNuevo(ArrayList<Unidad> unidades) {
	    int dañoExplosion = ((this.getAtq() + this.getAtqMod()))*2;
	    if (!unidades.isEmpty()) {
	        Unidad unidad = elegirObjetivoMasFuerte(unidades);
	        unidad.recibirDaño(dañoExplosion, true, 20);
	        for (Unidad unidadAledaña : unidades) {
	            if (!unidadAledaña.equals(unidad)) { // Evitar aplicar daño a la unidad objetivo
	                unidadAledaña.recibirDaño(dañoExplosion / 2, false, 20);
	            }
	        }
	    }
	}
	//METODOS AUXILIARES////////////////////////////////////////////////////////////
	public Unidad elegirObjetivo(ArrayList<Unidad> unidades) {
	    Unidad unidadSeleccionada = null;
	    int menorPorcentajeHP = Integer.MAX_VALUE;
	    for (Unidad unidad : unidades) {
	    	int porcentajeHP = (unidad.getHP() * 100) / unidad.getHPMax();;
	        if (porcentajeHP < menorPorcentajeHP) {
	            menorPorcentajeHP = porcentajeHP;
	            unidadSeleccionada = unidad;
	        }
	    }
	    return unidadSeleccionada;
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