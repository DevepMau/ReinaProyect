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
	private int neocreditos;
	private int dañoCausado = 0;

	public MedicoTradicionalista(Zona zona, boolean aliado, PanelDeJuego pdj) {
		super(zona, aliado, pdj);
		this.setNombre("Especialista");
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
		this.neocreditos = 100;
		this.spHabilidad1 = 10;
		this.habilidades[0] = "CURAR";
		this.habilidades[1] = "TONICO IMPERIA";
		this.generarCuerpo();
	}
	//METODO PRINCIPAL//////////////////////////////////////////////////////////////////
	public void realizarAccion(ArrayList<Unidad> enemigos, ArrayList<Unidad> aliados) {
		int accion = elegirAleatorio(6);
		if(accion <= 4 && cumpleReqDeHab1()) {
			if(cumpleReqDeHab2()) {
				this.setHabilidadElegida(1);
			}
			else {
				this.setHabilidadElegida(0);
			}
			usarHabilidadEnemigo(aliados, enemigos);
			
		}
		else {
			realizarAtaqueEnemigo(enemigos);
		}
	}
	//METODOS ENEMIGO////////////////////////////////////////////////////////////////////
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
				this.restaurarSP(daño*2);
			}
			else {
				objetivo.evadirAtaque();
			}
			this.reflejarDaño(objetivo, daño);
		}
	}
	public void usarHabilidadEnemigo(ArrayList<Unidad> aliados, ArrayList<Unidad> enemigos) {
		if(this.getHabilidadElegida() == 0) {
			if(!enemigos.isEmpty()) {
				this.setSP(this.getSP() - this.spHabilidad1);
				Unidad unidad = elegirObjetivo(enemigos);
				curar(unidad);
			}
		}
		else {
			if(!aliados.isEmpty()) {
				this.neocreditos = 0;
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
				this.restaurarSP(daño*2);
			}
			else {
				unidad.evadirAtaque();
			}
			this.reflejarDaño(unidad, daño);
		}
	}
	public void usarHabilidad(Unidad unidad, ArrayList<Unidad> unidades) {	
		if(this.getHabilidadElegida() == 0) {
			curar(unidad);
		}
		else {
			if(!unidades.isEmpty()) {
				this.neocreditos = 0;
				for(Unidad unidadObjetivo : unidades) {
					if(unidadObjetivo.getGenero() == this.generoMayoritario(unidades)){
						System.out.println(this.generoMayoritario(unidades));
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
			unidad.restaurarHP(unidad.getHPMax()/4);
			this.sumarNeocreditos(unidad.getHPMax()/8);
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
	public boolean cumpleReqDeHab1() {
		if(this.getSP() > 0) {
			if(this.getSP() >= this.spHabilidad1) {
				return true;
			}
		}
		return false;
	}
	public boolean cumpleReqDeHab2() {
		if(this.neocreditos == 100) {
			return true;
		}
		return false;
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
		g2.drawString("NC: "+this.neocreditos, this.getPosX()+8, this.getPosY()-11);
	}
	public void pasivaDeClase(ArrayList<Unidad> aliados, ArrayList<Unidad> enemigos) {
		if(!aliados.isEmpty()) {
			for(Unidad unidad : aliados) {
				if(unidad.getIdFaccion() == 2) {
					unidad.sumarNeocreditos(this.getDañoCaudado()/2);
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
	public String[] getListaDeHabilidades() {return habilidades;}
	public void setListaDeHabilidades(String[] habilidades) {this.habilidades = habilidades;}
}