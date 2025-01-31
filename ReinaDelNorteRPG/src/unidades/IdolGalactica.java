package unidades;

import java.util.ArrayList;
import principal.PanelDeJuego;
import principal.Zona;

public class IdolGalactica extends Unidad{
	
	private int spHabilidad1;
	private String[] listaDeHabilidades = new String[2];

	public IdolGalactica(Zona zona, boolean aliado, PanelDeJuego pdj) {
		super(zona, aliado, pdj);
		this.setNombre("");
		this.setTipo("Elite");
		this.setClase("Idol Galactica");
		this.setGenero(0);
		this.setIdFaccion(4);
		this.setHPMax(obtenerValorEntre(100,150));
		this.setHP(this.getHPMax());
		this.setSPMax(obtenerValorEntre(90,120));
		this.setSP(this.getSPMax());
		this.setAtq(obtenerValorEntre(9,13));
		this.setDef(obtenerValorEntre(3,6));
		this.setPCRT(0.5);
		this.setDCRT(2);
		this.setEva(0.33);
		this.setVel(obtenerValorEntre(20,25));
		this.spHabilidad1 = 20;
		this.listaDeHabilidades[0] = "ANIMAR";
		this.listaDeHabilidades[1] = "ROMPE-CORAZON";
		this.generarCuerpo();
	}
	//METODOS PRINCIPALES///////////////////////////////////////////////////////////
	public void realizarAccion(ArrayList<Unidad> enemigos, ArrayList<Unidad> aliados) {
		int accion = elegirAleatorio(5);
		if(!this.haySinMarcar(enemigos)) {
			this.setHabilidadElegida(1);
			usarHabilidadEnemigo(aliados, enemigos);
		}
		else if(accion <= 1 && this.cumpleReqDeHab1()) {
			this.setHabilidadElegida(0);
			usarHabilidadEnemigo(aliados, enemigos);
		}
		else if(accion == 2 && this.getCargarEnamoramiento() > 0) {
			this.setHabilidadElegida(1);
			usarHabilidadEnemigo(aliados, enemigos);
		}
		else {
			realizarAtaqueEnemigo(enemigos);
		}
		this.pasivaDeClase(aliados, enemigos);
	}
	//METODOS DE ENEMIGO////////////////////////////////////////////////////////////
	public void realizarAtaqueEnemigo(ArrayList<Unidad> unidades) {
		super.realizarAtaqueEnemigo(unidades);
	}
	public void usarHabilidadEnemigo(ArrayList<Unidad> aliados, ArrayList<Unidad> enemigos) {
		if(this.getHabilidadElegida() == 0) {
			if(!aliados.isEmpty()) {
				Unidad unidad = elegirObjetivo(aliados);
				curar(unidad);
			}
		}
		else {
			if(!enemigos.isEmpty()) {
				for(Unidad unidad : enemigos) {
					romperCorazones(unidad);
				}
				this.setCargarEnamoramiento(0);
			}
		}
	}
	//METODOS DE JUGADOR////////////////////////////////////////////////////////////
	public void realizarAtaque(Unidad unidad, ArrayList<Unidad> enemigos) {
		super.realizarAtaque(unidad, enemigos);
	}
	public void usarHabilidad(Unidad unidad, ArrayList<Unidad> unidades) {	
		if(this.getHabilidadElegida() == 0) {
			curar(unidad);
		}
		else {
			for(Unidad unidadObjetivo : unidades) {
				romperCorazones(unidadObjetivo);
			}
			this.setCargarEnamoramiento(0);
		}
		
	}
	//HABILIDADES///////////////////////////////////////////////////////////////////
	public void curar(Unidad unidad) {
		this.setSP(this.getSP() - this.spHabilidad1);
		if(unidad != null) {
			pdj.ReproducirSE(4);
			unidad.setVelMod(unidad.getVelMod() + 5);
			unidad.restaurarHP(unidad.getHPMax()/4);
			unidad.setRealizandoCuracion(true);
			unidad.setearSacudida(true);
			unidad.setDuracionSacudida(20);
		}
	}
	public void romperCorazones(Unidad unidad) {
		int daño = (this.getAtq()+((this.getAtq()/2)*(this.getCargarEnamoramiento()+1)));
		System.out.println(daño);
		if(unidad != null) {
			pdj.ReproducirSE(8);
			if(unidad.isEstaEnamorado()) {
				unidad.setearSacudida(true);
				unidad.setDuracionSacudida(20);
				unidad.setEstaEnamorado(false);
				unidad.recibirDaño(daño, false, 20);
				this.restaurarSP(daño/2);
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
	public boolean haySinMarcar(ArrayList<Unidad> unidades) {
		if(!unidades.isEmpty()) {
			for(Unidad unidad : unidades) {
				if(!unidad.isEstaEnamorado()) {
					return true;
				}
			}
		}
		return false;
	}
	public Unidad elegirObjetivoSinMarcar(ArrayList<Unidad> unidades) {
	    ArrayList<Unidad> unidadesSinMarcar = new ArrayList<>();
	    for (Unidad unidad : unidades) {
	        if (!unidad.isEstaEnamorado()) {
	            unidadesSinMarcar.add(unidad);
	        }
	    }
	    if (!unidadesSinMarcar.isEmpty()) {
	        return unidadesSinMarcar.get(this.elegirAleatorio(unidadesSinMarcar.size()));
	    }
	    return null;
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
		if(this.getCargarEnamoramiento() > 0) {
			return true;
		}
		return false;
	}
	public void configurarTipoDeaccion() {
		if(this.getHabilidadElegida() == 0) {
			this.setObjetivoUnico(true);
			this.setAccion("APOYAR");
		}
		else {
			this.setObjetivoUnico(false);
			this.setAccion("ATACAR");
		}
	}
	public void pasivaDeClase(ArrayList<Unidad> aliados, ArrayList<Unidad> enemigos) {
		super.pasivaDeClase(aliados, enemigos);
		if(!aliados.isEmpty() && this.getContador() > 0) {
			for(Unidad unidad : aliados) {
				if(!this.equals(unidad)) {
					unidad.setAtqMod(unidad.getAtqMod() + 1);
					unidad.setDefMod(unidad.getDefMod() + 1);
					unidad.setVelMod(unidad.getVelMod() + 1);
				}
			}
			this.setContador(this.getContador() - 1);
		}
	}
	public String[] getListaDeHabilidades() {return listaDeHabilidades;}
	public void setListaDeHabilidades(String[] habilidades) {this.listaDeHabilidades = habilidades;}
}