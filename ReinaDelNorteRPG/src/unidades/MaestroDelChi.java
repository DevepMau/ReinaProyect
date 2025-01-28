package unidades;

import java.util.ArrayList;
import principal.PanelDeJuego;
import principal.Zona;

public class MaestroDelChi extends Unidad {
	
	private String[] habilidades = new String[2];
	private int spHabilidad1;
	private int cargas;

	public MaestroDelChi(Zona zona, boolean aliado, PanelDeJuego pdj) {
		super(zona, aliado, pdj);
		this.setNombre("Especialista");
		this.setClase("Maestro Del Chi");
		this.setIdFaccion(3);
		this.setHPMax(obtenerValorEntre(40,70));
		this.setHP(this.getHPMax());
		this.setSPMax(obtenerValorEntre(60,100));
		this.setSP(this.getSPMax());
		this.setAtq(obtenerValorEntre(12,18));
		this.setDef(obtenerValorEntre(1,7));
		this.setVel(obtenerValorEntre(14,19));
		this.setPCRT(0.25);
		this.setDCRT(1.5);
		this.setEva(0.25);
		this.spHabilidad1 = 20;
		this.cargas = 0;
		this.habilidades[0] = "MARCAR";
		this.habilidades[1] = "EXPLOTAR";
		this.generarCuerpo();
	}
	//METODO PRINCIPAL//////////////////////////////////////////////////////////////////
	public void realizarAccion(ArrayList<Unidad> enemigos, ArrayList<Unidad> aliados) {
		int accion = elegirAleatorio(4);
		if(!this.haySinMarcar(enemigos)) {
			this.setHabilidadElegida(1);
			usarHabilidadEnemigo(aliados, enemigos);
		}
		else if(accion <= 2 && cumpleReqDeHab1() && this.haySinMarcar(enemigos)) {
			this.setHabilidadElegida(0);
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
			this.robarVida(daño, objetivo);
		}
	}
	public void usarHabilidadEnemigo(ArrayList<Unidad> aliados, ArrayList<Unidad> enemigos) {
		if(this.getHabilidadElegida() == 0) {
			if(!enemigos.isEmpty()) {
				this.setSP(this.getSP() - this.spHabilidad1);
				Unidad unidad = elegirObjetivoSinMarcar(enemigos);
				marcar(unidad);
			}
		}
		else {
			if(!enemigos.isEmpty()) {
				for(Unidad unidad : enemigos) {
					explotar(unidad);
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
			this.robarVida(daño, unidad);
		}
	}
	public void usarHabilidad(Unidad unidad, ArrayList<Unidad> unidades) {	
		if(this.getHabilidadElegida() == 0) {
			marcar(unidad);
		}
		else {
			for(Unidad unidadObjetivo : unidades) {
				explotar(unidadObjetivo);
			}
		}
	}
	//HABILIDADES////////////////////////////////////////////////////////////////////////
	public void marcar(Unidad unidad) {
		this.setSP(this.getSP() - this.spHabilidad1);
		if(unidad != null) {
			pdj.ReproducirSE(8);
			unidad.setEstaMArcado(true);
			unidad.setearSacudida(true);
			unidad.setDuracionSacudida(20);
			this.cargas++;
		}
	}
	
	public void explotar(Unidad unidad) {
		int daño = (this.getAtq()+this.getAtqMod()+(unidad.getVel()/2));
		if(unidad != null) {
			pdj.ReproducirSE(8);
			if(unidad.getEstaMarcado()) {
				unidad.setearSacudida(true);
				unidad.setDuracionSacudida(20);
				unidad.setEstaDesmotivado(true);
				unidad.setearSacudida(true);
				unidad.setDuracionSacudida(20);
				unidad.setEstaMArcado(false);
				unidad.setVelMod(unidad.getVelMod() - obtenerValorEntre(1,5));
				unidad.setDefMod(unidad.getDefMod() - obtenerValorEntre(1,3));
				unidad.recibirDaño(daño, false);
				this.cargas = 0;
			}
			
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
	public boolean haySinMarcar(ArrayList<Unidad> unidades) {
		if(!unidades.isEmpty()) {
			for(Unidad unidad : unidades) {
				if(!unidad.getEstaMarcado()) {
					return true;
				}
			}
		}
		return false;
	}
	public Unidad elegirObjetivoSinMarcar(ArrayList<Unidad> unidades) {
	    ArrayList<Unidad> unidadesSinMarcar = new ArrayList<>();
	    for (Unidad unidad : unidades) {
	        if (!unidad.getEstaMarcado()) {
	            unidadesSinMarcar.add(unidad);
	        }
	    }
	    if (!unidadesSinMarcar.isEmpty()) {
	        return unidadesSinMarcar.get(this.elegirAleatorio(unidadesSinMarcar.size()));
	    }
	    return null;
	}
	public void configurarTipoDeaccion() {
		if(this.getHabilidadElegida() == 0) {
			this.setAccion("ATACAR");
			this.setObjetivoUnico(true);
		}
		else {
			this.setAccion("ATACAR");
			this.setObjetivoUnico(false);
		}
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
		if(this.cargas > 0) {
			return true;
		}
		return false;
	}
	public String[] getListaDeHabilidades() {return habilidades;}
	public void setListaDeHabilidades(String[] habilidades) {this.habilidades = habilidades;}
}
