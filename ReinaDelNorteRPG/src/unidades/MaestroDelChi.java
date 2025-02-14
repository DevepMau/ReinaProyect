package unidades;

import java.util.ArrayList;

import principal.Estadisticas;
import principal.Habilidades;
import principal.PanelDeJuego;
import principal.Zona;

public class MaestroDelChi extends Unidad {
	
	private String[] habilidades = new String[2];
	private int spHabilidad1;
	private int cargas;

	public MaestroDelChi(Zona zona, boolean aliado, PanelDeJuego pdj) {
		super(zona, aliado, pdj);
		this.setNombre("");
		this.setTipo("Especialista");
		this.setClase("Maestro Del Chi");
		this.setIdFaccion(3);
		this.setHPMax(obtenerValorEntre(110,130));
		this.setHP(this.getHPMax());
		this.setSPMax(obtenerValorEntre(80,120));
		this.setSP(this.getSPMax());
		this.setAtq(obtenerValorEntre(12,15));
		this.setDef(obtenerValorEntre(10,20));
		this.setVel(obtenerValorEntre(20,30));
		this.setPCRT(25);
		this.setDCRT(1.5);
		this.setEva(25);
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
		this.pasivaDeClase(aliados, enemigos);
	}
	//METODOS ENEMIGO////////////////////////////////////////////////////////////////////
	public void usarHabilidadEnemigo(ArrayList<Unidad> aliados, ArrayList<Unidad> enemigos) {
		if(this.getHabilidadElegida() == 0) {
			if(!enemigos.isEmpty()) {
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
			Habilidades.marcarUnidad(unidad);
			this.cargas++;
		}
	}
	
	public void explotar(Unidad unidad) {
		if(unidad != null) {
			pdj.ReproducirSE(8);
			if(unidad.getTimerMarcado() > 0) {
				Habilidades.desmarcarUnidad(unidad);
				Habilidades.explotarMarca(this, unidad);
				this.cargas = 0;
			}
			
		}
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
	public void pasivaDeClase(ArrayList<Unidad> aliados, ArrayList<Unidad> enemigos) {
		super.pasivaDeClase(aliados, enemigos);
	}
	public String[] getListaDeHabilidades() {return habilidades;}
	public void setListaDeHabilidades(String[] habilidades) {this.habilidades = habilidades;}
}
