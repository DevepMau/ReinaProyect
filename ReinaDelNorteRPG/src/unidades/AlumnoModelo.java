package unidades;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;

import principal.Habilidades;
import principal.PanelDeJuego;
import principal.Zona;

public class AlumnoModelo extends Unidad{
	
	private int spHabilidad1;
	private String[] listaDeHabilidades = new String[1];
	
	public AlumnoModelo(Zona zona, boolean aliado, PanelDeJuego pdj) {
		super(zona, aliado, pdj);
		this.setNombre("");
		this.setTipo("Recluta");
		this.setClase("Alumno Modelo");
		this.setIdFaccion(2);
		this.setHPMax(obtenerValorEntre(50,70));
		this.setHP(this.getHPMax());
		this.setSPMax(0);
		this.setSP(this.getSPMax());
		this.setAtq(obtenerValorEntre(8,12));
		this.setDef(obtenerValorEntre(10,20));
		this.setPCRT(0);
		this.setDCRT(1.5);
		this.setEva(0);
		this.setVel(obtenerValorEntre(20,30));
		this.setNeocreditos(0);
		this.spHabilidad1 = 100;
		this.listaDeHabilidades[0] = "FAVOR ESTATAL";
		this.generarCuerpo();
	}
	//METODO PRINCIPAL//////////////////////////////////////////////////////////////////
	public void realizarAccion(ArrayList<Unidad> enemigos, ArrayList<Unidad> aliados) {
		if(cumpleReqDeHab2()) {
			this.setHabilidadElegida(this.elegirAleatorio(2));
			ArrayList<Unidad> unidades = enemigos;
			if(this.getHabilidadElegida() == 0) {
				unidades = aliados;
			}
			usarHabilidadEnemigo(unidades);
			this.setNeocreditos(0);
			
		}
		else {
			realizarAtaqueEnemigo(enemigos);
		}
		this.pasivaDeClase(aliados, enemigos);
	}
	//METODOS ENEMIGO////////////////////////////////////////////////////////////////////
	public void usarHabilidadEnemigo(ArrayList<Unidad> unidades) {
		if(this.getHabilidadElegida() == 0) {
			pdj.ReproducirSE(4);
			reconocimientoNacional(unidades);
		}
		else if(this.getHabilidadElegida() == 1) {
			pdj.ReproducirSE(3);
			denunciaColectiva(unidades);
		}
		this.setNeocreditos(0);
	}
	//METODOS JUGADOR////////////////////////////////////////////////////////////////////
	public void usarHabilidad(Unidad unidad, ArrayList<Unidad> unidades) {
		if(this.getHabilidadElegida() == 0) {
			pdj.ReproducirSE(4);
			reconocimientoNacional(unidades);
		}
		else if(this.getHabilidadElegida() == 1) {
			pdj.ReproducirSE(3);
			denunciaColectiva(unidades);
		}
		this.setNeocreditos(0);
	}
	//HABILIDADES////////////////////////////////////////////////////////////////////////
	public void reconocimientoNacional(ArrayList<Unidad> unidades) {
		if(!unidades.isEmpty()) {
			for(Unidad unidad : unidades) {
				if(unidad.getHPMax() > unidad.getHP()) {
					Habilidades.restaurarHP(unidad, 20);
				}
				else if(unidad.getTimerPrecavido() == -1) {
					Habilidades.aumentarProteccion(unidad);
				}
				else if(unidad.getTimerAgresivo() == -1) {
					Habilidades.aumentarAgresividad(unidad);
				}
				else if(unidad.getTimerAcelerado() == -1) {
					Habilidades.aumentarAgilidad(unidad);
				}
				else {
					Habilidades.potenciarUnidad(unidad);
				}
			}
		}
	}
	public void denunciaColectiva(ArrayList<Unidad> unidades) {
		if(!unidades.isEmpty()) {
			for(Unidad unidad : unidades) {
				unidad.setHP(unidad.getHP() - unidad.getHPMax()/10);
		        Habilidades.stunearUnidad(unidad);
			}
		}
	}
	
	//METODOS AUXILIARES/////////////////////////////////////////////////////////////////
	public boolean cumpleReqDeHab2() {
		if(this.getNeocreditos() >= this.spHabilidad1) {
			return true;
		}
		return false;
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
		if(this.getNeocreditos() < 10) {
			g2.drawString("00"+ this.getNeocreditos(), this.getPosX()+9, this.getPosY()-9);
		}
		else if(this.getNeocreditos() >= 10 && this.getNeocreditos() < 100) {
			g2.drawString("0"+ this.getNeocreditos(), this.getPosX()+9, this.getPosY()-9);
		}
		else {
			g2.setColor(Color.orange);
			g2.drawString(""+ this.getNeocreditos(), this.getPosX()+9, this.getPosY()-9);
		}
		
	}
	public void pasivaDeClase(ArrayList<Unidad> aliados, ArrayList<Unidad> enemigos) {
		super.pasivaDeClase(aliados, enemigos);
	}
	public String[] getListaDeHabilidades() {return listaDeHabilidades;}
	public void setListaDeHabilidades(String[] habilidades) {this.listaDeHabilidades = habilidades;}
}
