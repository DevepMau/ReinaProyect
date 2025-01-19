package unidades;

import java.util.ArrayList;
import principal.PanelDeJuego;
import principal.Zona;

public class GauchoModerno extends Unidad {
	
	private String[] listaDeHabilidades = new String[1];
	private int acumuladorDeVidaPrdida = 0;

	public GauchoModerno(Zona zona, boolean aliado, PanelDeJuego pdj) {
		super(zona, aliado, pdj);
		this.setNombre("");
		this.setClase("Gaucho Moderno");
		this.setHPMax(obtenerValorEntre(60,90));
		this.setHP(this.getHPMax());
		this.setSP(0);
		this.setSPMax(0);
		this.setAtq(obtenerValorEntre(13,17));
		this.setDef(obtenerValorEntre(5,9));
		this.setPCRT(0.15);
		this.setDCRT(1.5);
		this.setEva(0);
		this.setVel(obtenerValorEntre(5,10));
		this.listaDeHabilidades[0] = "SALDAR DEUDA";
	}
	//METODO PRINCIPAL//////////////////////////////////////////////////////////////////
	public void recibirDaño(int daño, boolean isCritical) {
	    int hpAnterior = this.getHP();
	    super.recibirDaño(daño, isCritical);
	    int hpPerdido = hpAnterior - this.getHP();
	    this.acumuladorDeVidaPrdida += hpPerdido;
	    if(acumuladorDeVidaPrdida >= 10) {
	    	int acumuladorDeAtq = (acumuladorDeVidaPrdida / 10);
	    	this.setAtqMod(this.getAtqMod() + acumuladorDeAtq);
	        this.acumuladorDeVidaPrdida -= (10*acumuladorDeAtq); 
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
				this.setPcrtMod(this.getPcrtMod()+0.05);
			}
			else {
				objetivo.evadirAtaque();
			}
		}
	}
	public void usarHabilidadEnemigo(ArrayList<Unidad> unidades) {
		if(this.getHabilidadElegida() == 0) {
			if(!unidades.isEmpty()) {
				Unidad unidad = elegirObjetivo(unidades);
				saldarDeuda(unidad);
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
				this.setPcrtMod(this.getPcrtMod()+0.05);
			}
			else {
				unidad.evadirAtaque();
			}
		}
	}
	public void usarHabilidad(Unidad unidad, ArrayList<Unidad> unidades) {	
		saldarDeuda(unidad);
	}
	//HABILIDADES////////////////////////////////////////////////////////////////////////
	public void saldarDeuda(Unidad unidad) {
		int daño = (this.getAtqMod()*4);
		if(unidad != null) {
			unidad.recibirDaño(daño, false);
			unidad.setearSacudida(true);
			unidad.setDuracionSacudida(20);
			unidad.setEsUnaHabilidad(true);
		}
		this.setAtqMod(0);
	}
	//METODOS AUXILIARES/////////////////////////////////////////////////////////////////
	public boolean cumpleReqDeHab1() {
		if(this.getAtqMod() >= 5) {
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
