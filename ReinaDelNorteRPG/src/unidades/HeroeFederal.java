package unidades;

import java.util.ArrayList;
import principal.PanelDeJuego;
import principal.Zona;

public class HeroeFederal extends Unidad{
	
	private int spHabilidad1;
	private String[] listaDeHabilidades = new String[1];

	public HeroeFederal(Zona zona, boolean aliado, PanelDeJuego pdj) {
		super(zona, aliado, pdj);
		this.setNombre("");
		this.setTipo("Elite");
		this.setClase("Heroe Federal");
		this.setIdFaccion(1);
		this.setHPMax(obtenerValorEntre(100,150));
		this.setHP(this.getHPMax());
		this.setSPMax(obtenerValorEntre(50,70));
		this.setSP(this.getSPMax());
		this.setAtq(obtenerValorEntre(13,17));
		this.setDef(obtenerValorEntre(10,13));
		this.setPCRT(0.5);
		this.setDCRT(2);
		this.setEva(0.15);
		this.setVel(obtenerValorEntre(20,25));
		this.spHabilidad1 = 10;
		this.listaDeHabilidades[0] = "EXPULSAR";
		this.generarCuerpo();
	}
	//METODOS PRINCIPALES///////////////////////////////////////////////////////////
	public void realizarAccion(ArrayList<Unidad> enemigos, ArrayList<Unidad> aliados) {
		int accion = elegirAleatorio(3);
		if(accion <= 1 && cumpleReqDeHab1()) {
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
			Unidad unidad = elegirObjetivoMasFuerte(unidades);
			expulsar(unidad);	
		}
	}
	//METODOS DE JUGADOR////////////////////////////////////////////////////////////
	public void usarHabilidad(Unidad unidad, ArrayList<Unidad> unidades) {
		expulsar(unidad);
	}
	//HABILIDADES///////////////////////////////////////////////////////////////////
	public void expulsar(Unidad unidad) {
		if(unidad != null) {
			this.setSP(this.getSP() - this.spHabilidad1);
			boolean isCritical = Math.random() <= (this.getPCRT() + this.getPcrtMod());   
			int daño = Math.max(1, (this.getAtq() + this.getAtqMod()) - (unidad.getDef() + unidad.getDefMod()));
	    	 
			if (isCritical) {
				daño *= (this.getDCRT() + this.getDcrtMod());
				unidad.setEstaActivo(false);
				unidad.setEstaKO(true);
			}
			else {
				unidad.setEstaLisiado(true);
			}
			unidad.recibirDaño(daño, false);
			unidad.setVelMod(unidad.getVelMod() - (unidad.getVel()+unidad.getVelMod()));
			unidad.setearSacudida(true);
			unidad.setDuracionSacudida(20);
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
	public boolean cumpleReqDeHab1() {
		if(this.getSP() > 0) {
			if(this.getSP() >= this.spHabilidad1) {
				return true;
			}
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