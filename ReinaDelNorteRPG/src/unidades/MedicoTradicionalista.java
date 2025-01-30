package unidades;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import principal.PanelDeJuego;
import principal.Zona;

public class MedicoTradicionalista extends Unidad {
	
	private String[] habilidades = new String[2];
	private int spHabilidad1;

	public MedicoTradicionalista(Zona zona, boolean aliado, PanelDeJuego pdj) {
		super(zona, aliado, pdj);
		this.setNombre("");
		this.setTipo("Especialista");
		this.setClase("Medico Tradicionalista");
		this.setIdFaccion(2);
		this.setHPMax(obtenerValorEntre(40,70));
		this.setHP(this.getHPMax());
		this.setSPMax(obtenerValorEntre(120,150));
		this.setSP(this.getSPMax());
		this.setAtq(obtenerValorEntre(3,8));
		this.setDef(obtenerValorEntre(5,9));
		this.setVel(obtenerValorEntre(2,8));
		this.setPCRT(0);
		this.setNeocreditos(0);
		this.spHabilidad1 = 10;
		this.habilidades[0] = "CURAR";
		this.habilidades[1] = "TONICO IMPERIA";
		this.generarCuerpo();
	}
	//METODO PRINCIPAL//////////////////////////////////////////////////////////////////
	public void realizarAccion(ArrayList<Unidad> enemigos, ArrayList<Unidad> aliados) {
		int accion = elegirAleatorio(6);
		if(this.getNeocreditos() == 100) {
			this.setHabilidadElegida(1);
			usarHabilidadEnemigo(aliados, enemigos);
		}
		else if(accion <= 4 && cumpleReqDeHab1()) {
			this.setHabilidadElegida(0);
			usarHabilidadEnemigo(aliados, enemigos);
			
		}
		else {
			realizarAtaqueEnemigo(enemigos);
		}
		this.pasivaDeClase(aliados, enemigos);
	}
	//METODOS ENEMIGO////////////////////////////////////////////////////////////////////
	public void usarHabilidadEnemigo(ArrayList<Unidad> aliados, ArrayList<Unidad> enemigos) {
		if(this.getHabilidadElegida() == 0) {
			if(!aliados.isEmpty()) {
				this.setSP(this.getSP() - this.spHabilidad1);
				Unidad unidad = elegirObjetivo(aliados);
				curar(unidad);
				this.pasivaDeClase(aliados, enemigos);
			}
		}
		else {
			if(!aliados.isEmpty()) {
				this.setNeocreditos(0);
				for(Unidad unidad : aliados) {
					if(unidad.getGenero() == this.generoMayoritario(aliados)){
						System.out.println(this.generoMayoritario(aliados));
						tonicoImperial(unidad);
					}
				}
			}
		}
	}
	//METODOS JUGADOR////////////////////////////////////////////////////////////////////
	public void usarHabilidad(Unidad unidad, ArrayList<Unidad> unidades) {	
		if(this.getHabilidadElegida() == 0) {
			curar(unidad);
		}
		else {
			if(!unidades.isEmpty()) {
				this.setNeocreditos(0);
				for(Unidad unidadObjetivo : unidades) {
					if(unidadObjetivo.getGenero() == this.generoMayoritario(unidades)){
						tonicoImperial(unidadObjetivo);
					}
				}
			}	
		}
		
	}
	//HABILIDADES////////////////////////////////////////////////////////////////////////
	public void curar(Unidad unidad) {
		this.setSP(this.getSP() - this.spHabilidad1);
		if(unidad != null) {
			pdj.ReproducirSE(4);
			unidad.restaurarHP(unidad.getHPMax()/4);
			unidad.setRealizandoCuracion(true);
			unidad.setearSacudida(true);
			unidad.setDuracionSacudida(20);
			this.sumarNeocreditos(unidad.getHPMax()/4);
			this.setDañoCausado(unidad.getHPMax()/10);
		}
	}
	public void tonicoImperial(Unidad unidad) {
		if(unidad != null) {
			pdj.ReproducirSE(7);
			unidad.setearSacudida(true);
			unidad.setDuracionSacudida(20);
			unidad.setEstaMotivado(true);
			unidad.setAtqMod(unidad.getAtqMod() + obtenerValorEntre(3,7));
			unidad.setHPMax(unidad.getHPMax()+(unidad.getHPMax()/5));
			unidad.restaurarHP(unidad.getHPMax()/5);
		}
	}
	public void ganarSP(int daño) {
		if((this.getSP()+(daño*2)) > this.getSPMax()){
			this.setSP(this.getSPMax());
		}
		else {
			this.setSP(this.getSP() + daño*2);
		}
		this.setearSacudida(true);
		this.setDuracionSacudida(20);
		this.setEstaGanandoSP(true);
	}
	//METODOS AUXILIARES/////////////////////////////////////////////////////////////////
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
	public boolean cumpleReqDeHab1() {
		if(this.getSP() > 0) {
			if(this.getSP() >= this.spHabilidad1) {
				return true;
			}
		}
		return false;
	}
	public boolean cumpleReqDeHab2() {
		if(this.getNeocreditos() == 100) {
			return true;
		}
		return false;
	}
	public void configurarTipoDeaccion() {
		if(this.getHabilidadElegida() == 0) {
			this.setAccion("APOYAR");
			this.setObjetivoUnico(true);
		}
		else {
			this.setAccion("APOYAR");
			this.setObjetivoUnico(false);
		}
	}
	public int generoMayoritario(ArrayList<Unidad> unidades) {
	    int conteoMasculino = 0;
	    int genero = 0;
	    for (Unidad unidad : unidades) {
	        if (unidad.getGenero() == 1) {
	            conteoMasculino++;
	        }
	    }
	   if(conteoMasculino >= unidades.size() / 2) {
		   genero = 1;
	   } 
	   return genero;
	}
	public void efectosVisualesPersonalizados(Graphics2D g2) {
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 18f));
		g2.setColor(Color.white);
		g2.drawRect(this.getPosX()+7, this.getPosY()-24, 24, 18);
		if(this.getNeocreditos() < 10) {
			g2.drawString("00"+this.getNeocreditos(), this.getPosX()+9, this.getPosY()-9);
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
		if(!aliados.isEmpty()) {
			for(Unidad unidad : aliados) {
				if(unidad.getIdFaccion() == 2) {
					if(!this.equals(unidad)) {
						unidad.sumarNeocreditos(this.getDañoCausado());
					}
				}
			}
		}
	}
	public String[] getListaDeHabilidades() {return habilidades;}
	public void setListaDeHabilidades(String[] habilidades) {this.habilidades = habilidades;}
}