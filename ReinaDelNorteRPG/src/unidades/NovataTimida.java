package unidades;

import java.util.ArrayList;

import principal.Habilidades;
import principal.PanelDeJuego;
import principal.Zona;

public class NovataTimida extends Unidad{
	
	private String[] listaDeHabilidades = new String[1];
	private boolean habilitar = true;
	private int escudosIniciales = 0;

	public NovataTimida(Zona zona, boolean aliado, PanelDeJuego pdj) {
		super(zona, aliado, pdj);
		this.setNombre("");
		this.setTipo("Recluta");
		this.setClase("Novata Timida");
		this.setIdFaccion(4);
		this.setGenero(0);
		this.setHPMax(obtenerValorEntre(60,75));
		this.setHP(this.getHPMax());
		this.setSPMax(0);
		this.setSP(this.getSPMax());
		this.setAtq(obtenerValorEntre(6,8));
		this.setDef(obtenerValorEntre(10,20));
		this.setPCRT(0);
		this.setDCRT(1.5);
		this.setEva(15);
		this.setVel(obtenerValorEntre(10,20));
		this.setEscudos(1);
		this.listaDeHabilidades[0] = "...";
		this.generarCuerpo();
	}
	//METODO PRINCIPAL//////////////////////////////////////////////////////////////////
	//METODOS ENEMIGO////////////////////////////////////////////////////////////////////
	public void realizarAtaqueEnemigo(ArrayList<Unidad> unidades) {
		if(!unidades.isEmpty()) {
			super.realizarAtaqueEnemigo(unidades);
		}
	}
	//METODOS JUGADOR////////////////////////////////////////////////////////////////////
	public void realizarAtaque(Unidad unidad, ArrayList<Unidad> unidades) {
		super.realizarAtaque(unidad, unidades);
	}
	//HABILIDADES////////////////////////////////////////////////////////////////////////
	//METODOS AUXILIARES/////////////////////////////////////////////////////////////////
	public void configurarTipoDeaccion() {
		if(this.getHabilidadElegida() == 0) {
			this.setObjetivoUnico(true);
			this.setAccion("ATACAR");
		}
		else {
			this.setAccion("");
		}
	}
	public void pasivaDeClase(ArrayList<Unidad> aliados, ArrayList<Unidad> enemigos) {
		super.pasivaDeClase(aliados, enemigos);
		if(habilitar) {
			if(!aliados.isEmpty()) {
				for(Unidad unidad : aliados) {
					if(unidad.getIdFaccion() == 4 && !this.equals(unidad)) {
						this.escudosIniciales++;
					}
				}
				Habilidades.protegerUnidad(this, this.escudosIniciales, pdj);
				habilitar = false;
			}
		}
	}
	public String[] getListaDeHabilidades() {return listaDeHabilidades;}
	public void setListaDeHabilidades(String[] habilidades) {this.listaDeHabilidades = habilidades;}
}
