package unidades;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;

import principal.Habilidades;
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
		this.setHPMax(obtenerValorEntre(90,120));
		this.setHP(this.getHPMax());
		this.setSPMax(obtenerValorEntre(120,170));
		this.setSP(this.getSPMax());
		this.setAtq(obtenerValorEntre(8,12));
		this.setDef(obtenerValorEntre(10,20));
		this.setVel(obtenerValorEntre(10,20));
		this.setPCRT(0);
		this.setNeocreditos(0);
		this.spHabilidad1 = 20;
		this.habilidades[0] = "CURAR";
		this.habilidades[1] = "TONICO IMPERIA";
		this.definirIdTez();
		if(this.getGenero() == 1) {
			this.asignarImagen(3, "/imagenes/unisex/quimicos-"+this.getIdTez(), 3); 
			this.asignarImagen(2, "/imagenes/hombre/pantalon-1", 3); 
			this.asignarImagen(1, "/imagenes/unisex/cuerpo"+this.getIdTez(), 3); 
			this.asignarImagen(0, "/imagenes/hombre/cabeza-boy-"+this.getIdTez(), 3); 
		}
		else {
			this.asignarImagen(3, "/imagenes/unisex/quimicos-"+this.getIdTez(), 3); 
			this.asignarImagen(2, "/imagenes/mujer/falda-"+this.getIdTez(), 3);
			this.asignarImagen(1, "/imagenes/unisex/cuerpo"+this.getIdTez(), 3); 
			this.asignarImagen(0, "/imagenes/mujer/cabeza-girl-"+this.getIdTez(), 3);
		}
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
				Unidad unidad = elegirObjetivo(aliados);
				curar(unidad);
			}
		}
		else {
			if(!aliados.isEmpty()) {
				this.setNeocreditos(0);
				for(Unidad unidad : aliados) {
					tonicoImperial(unidad);
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
					tonicoImperial(unidadObjetivo);
				}
			}	
		}
	}
	//HABILIDADES////////////////////////////////////////////////////////////////////////
	public void curar(Unidad unidad) {
		this.setSP(this.getSP() - this.spHabilidad1);
		if(unidad != null) {
			pdj.ReproducirSE(4);
			Habilidades.restaurarHP(unidad, 20);
			Habilidades.ganarNeoCreditos(this, 10);
			if(unidad.getTimerLisiado() > 0) {
				unidad.setTimerLisiado(-1);
				Habilidades.cancelarDestruirMovilidad(unidad);
			}
			if(unidad.getRdcDefAcc() > 0) {
				unidad.setTimerRdcDefAcc(-1);
				Habilidades.cancelarOxidarArmadura(unidad);
			}
			unidad.setTimerSangrando(0);	
			unidad.setTimerIncendiado(0);	
		}
	}
	public void tonicoImperial(Unidad unidad) {
		if(unidad != null) {
			pdj.ReproducirSE(7);
			int i = elegirAleatorio(2);
			if(i == 0) {
				Habilidades.aumentarAgresividad(unidad);
			}
			else {
				Habilidades.aumentarProteccion(unidad);
			}
		}
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
	}
	public String[] getListaDeHabilidades() {return habilidades;}
	public void setListaDeHabilidades(String[] habilidades) {this.habilidades = habilidades;}
}