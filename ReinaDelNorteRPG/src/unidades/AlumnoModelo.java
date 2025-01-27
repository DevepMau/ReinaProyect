package unidades;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import principal.PanelDeJuego;
import principal.Zona;

public class AlumnoModelo extends Unidad{
	
	private int neocreditos;
	private int dañoCausado = 0;
	private int spHabilidad1;
	private String[] listaDeHabilidades = new String[1];
	
	public AlumnoModelo(Zona zona, boolean aliado, PanelDeJuego pdj) {
		super(zona, aliado, pdj);
		this.setNombre("");
		this.setClase("Alumno Modelo");
		this.setIdFaccion(2);
		this.setHPMax(obtenerValorEntre(50,70));
		this.setHP(this.getHPMax());
		this.setSPMax(0);
		this.setSP(this.getSPMax());
		this.setAtq(obtenerValorEntre(8,12));
		this.setDef(obtenerValorEntre(1,5));
		this.setPCRT(0);
		this.setDCRT(1.5);
		this.setEva(0);
		this.setVel(obtenerValorEntre(10,20));
		this.neocreditos = 0;
		this.spHabilidad1 = 100;
		this.listaDeHabilidades[0] = "FAVOR ESTATAL";
		this.generarCuerpo();
	}
	//METODO PRINCIPAL//////////////////////////////////////////////////////////////////
	public void realizarAccion(ArrayList<Unidad> enemigos, ArrayList<Unidad> aliados) {
		//int accion = elegirAleatorio(4);
		if(cumpleReqDeHab2()) {
			this.setHabilidadElegida(this.elegirAleatorio(2));
			ArrayList<Unidad> unidades = enemigos;
			if(this.getHabilidadElegida() == 0) {
				unidades = aliados;
			}
			usarHabilidadEnemigo(unidades);
			this.neocreditos = 0;
			
		}
		else {
			realizarAtaqueEnemigo(enemigos);
			this.pasivaDeClase(aliados, enemigos);
		}
	}
	//METODOS ENEMIGO////////////////////////////////////////////////////////////////////
	public void usarHabilidadEnemigo(ArrayList<Unidad> unidades) {
		if(this.getHabilidadElegida() == 0) {
			reconocimientoNacional(unidades);
		}
		else if(this.getHabilidadElegida() == 1) {
			denunciaColectiva(unidades);
		}
	}
	public void realizarAtaqueEnemigo(ArrayList<Unidad> unidades) {
		Unidad objetivo = elegirObjetivo(unidades);
		boolean isCritical = Math.random() <= (this.getPCRT() + this.getPcrtMod());  
		if(objetivo != null) {
			boolean isMiss = Math.random() <= (objetivo.getEva() + objetivo.getEvaMod());
			int daño = Math.max(1, (this.getAtq() + this.getAtqMod()) - (objetivo.getDef() + objetivo.getDefMod()));
	    	 
			if (isCritical) {
				daño *= (this.getDCRT() + this.getDcrtMod());
			}
			if(!isMiss) {
				objetivo.recibirDaño(daño, isCritical);
				this.dañoCausado = daño*3;
				this.sumarNeocreditos(this.dañoCausado);
			}
			else {
				objetivo.evadirAtaque();
			}
			this.reflejarDaño(objetivo, daño);
		}
	}	
	//METODOS JUGADOR////////////////////////////////////////////////////////////////////
	public void usarHabilidad(Unidad unidad, ArrayList<Unidad> unidades) {
		if(this.getHabilidadElegida() == 0) {
			reconocimientoNacional(unidades);
		}
		else if(this.getHabilidadElegida() == 1) {
			denunciaColectiva(unidades);
		}
		this.neocreditos = 0;
	}
	public void realizarAtaque(Unidad unidad) {
		boolean isCritical = Math.random() <= (this.getPCRT() + this.getPcrtMod());
		if(unidad != null) {
			boolean isMiss = Math.random() <= (unidad.getEva() + unidad.getEvaMod());	 
			int daño = Math.max(1, (this.getAtq() + this.getAtqMod()) - (unidad.getDef() + unidad.getDefMod())); 	 
			if (isCritical) {
				daño *= (this.getDCRT() + this.getDcrtMod());
			}
			if(!isMiss) {
				unidad.recibirDaño(daño, isCritical);
				this.dañoCausado = daño*3;
				this.sumarNeocreditos(this.dañoCausado);
				
			}
			else {
				unidad.evadirAtaque();
			}
			this.reflejarDaño(unidad, daño);
		}
	}
	//HABILIDADES////////////////////////////////////////////////////////////////////////
	public void reconocimientoNacional(ArrayList<Unidad> unidades) {
		if(!unidades.isEmpty()) {
			for(Unidad unidad : unidades) {
				unidad.restaurarHP(unidad.getHPMax()/4);
			}
		}
	}
	public void denunciaColectiva(ArrayList<Unidad> unidades) {
		if(!unidades.isEmpty()) {
			for(Unidad unidad : unidades) {
				pdj.ReproducirSE(3);
				unidad.recibirDaño(unidad.getHPMax()/10, false);
				unidad.setEstaActivo(false);
				unidad.setEstaKO(true);
				unidad.setearSacudida(true);
				unidad.setDuracionSacudida(20);
			}
		}
	}
	
	//METODOS AUXILIARES/////////////////////////////////////////////////////////////////
	public boolean cumpleReqDeHab2() {
		if(this.neocreditos >= this.spHabilidad1) {
			return true;
		}
		return false;
	}
	public void sumarHP(Unidad unidad, int hp) {
		int i = hp + unidad.getHP();
		if(i > unidad.getHPMax()) {
			unidad.setHP(unidad.getHPMax());
		}
		else {
			unidad.setHP(i);
		}
	}	
	public void sumarSP(Unidad unidad, int sp) {
		int i = sp + unidad.getSP();
		if(i > unidad.getSPMax()) {
			unidad.setSP(unidad.getSPMax());
		}
		else {
			unidad.setSP(i);
		}
	}
	public void sumarNeocreditos(int neocreditos) {
		int i = neocreditos + this.neocreditos;
		if(i > 100) {
			this.neocreditos = 100;
		}
		else {
			this.neocreditos += neocreditos;
		}
	}
	public void configurarTipoDeaccion() {
		if(this.getHabilidadElegida() != -1){
			this.setHabilidadElegida(this.elegirAleatorio(2));
		}
		if(this.getHabilidadElegida() == 0) {
			this.setAccion("APOYAR");
			this.setObjetivoUnico(false);
		}
		else if(this.getHabilidadElegida() == 1) {
			this.setAccion("ATACAR");
			this.setObjetivoUnico(false);
		}
	}
	public void efectosVisualesPersonalizados(Graphics2D g2) {
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 18f));
		g2.setColor(Color.white);
		g2.drawRect(this.getPosX()+7, this.getPosY()-24, 24, 18);
		if(this.neocreditos < 10) {
			g2.drawString("00"+this.neocreditos, this.getPosX()+9, this.getPosY()-9);
		}
		else if(this.neocreditos >= 10 && this.neocreditos < 100) {
			g2.drawString("0"+this.neocreditos, this.getPosX()+9, this.getPosY()-9);
		}
		else {
			g2.setColor(Color.orange);
			g2.drawString(""+this.neocreditos, this.getPosX()+9, this.getPosY()-9);
		}
		
	}
	public void pasivaDeClase(ArrayList<Unidad> aliados, ArrayList<Unidad> enemigos) {
		if(!aliados.isEmpty()) {
			for(Unidad unidad : aliados) {
				if(unidad.getIdFaccion() == 2) {
					if(!this.equals(unidad)) {
						unidad.sumarNeocreditos(this.getDañoCaudado());
					}
				}
			}
		}
	}
	public int getDañoCaudado() {
		return this.dañoCausado;
	}
	public void setDañoCausado(int daño) {
		this.dañoCausado = daño;
	}
	public String[] getListaDeHabilidades() {return listaDeHabilidades;}
	public void setListaDeHabilidades(String[] habilidades) {this.listaDeHabilidades = habilidades;}
}
