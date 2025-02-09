package unidades;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;

import principal.Habilidades;
import principal.PanelDeJuego;
import principal.Zona;

public class ShaolinEscolar extends Unidad {
	
	private String[] listaDeHabilidades = new String[1];
	private int acumuladorDeVidaPrdida = 0;

	public ShaolinEscolar(Zona zona, boolean aliado, PanelDeJuego pdj) {
		super(zona, aliado, pdj);
		this.setNombre("");
		this.setTipo("Combatiente");
		this.setClase("Shaolin Escolar");
		this.setIdFaccion(2);
		this.setGenero(1);
		this.setHPMax(obtenerValorEntre(110,130));
		this.setHP(this.getHPMax());
		this.setSP(0);
		this.setSPMax(0);
		this.setAtq(obtenerValorEntre(12,15));
		this.setDef(obtenerValorEntre(25,35));
		this.setPCRT(25);
		this.setDCRT(1.5);
		this.setEva(25);
		this.setBloq(25);
		this.setVel(obtenerValorEntre(10,20));
		this.setNeocreditos(0);
		this.listaDeHabilidades[0] = "...";
		this.generarCuerpo();
	}
	//METODO PRINCIPAL//////////////////////////////////////////////////////////////////
	public void realizarAccion(ArrayList<Unidad> enemigos, ArrayList<Unidad> aliados) {
		realizarAtaqueEnemigo(enemigos);
		this.pasivaDeClase(aliados, enemigos);
	}
	public void recibirDaño(int daño, boolean isCritical, Unidad unidad) {
		int valor = 5;
	    int hpAnterior = this.getHP();
	    super.recibirDaño(daño, isCritical, unidad);
	    int hpPerdido = hpAnterior - this.getHP();
	    this.acumuladorDeVidaPrdida += hpPerdido;
	    if(acumuladorDeVidaPrdida >= valor) {
	    	int acumuladorDeDef = (acumuladorDeVidaPrdida / valor);
	    	this.setDefMod(this.getDefMod() + acumuladorDeDef);
	        this.acumuladorDeVidaPrdida -= (valor*acumuladorDeDef); 
	    }
	}
	//METODOS ENEMIGO////////////////////////////////////////////////////////////////////
	public void realizarAtaqueEnemigo(ArrayList<Unidad> unidades) {
		if(this.getNeocreditos() == 100) {
			potenciar();
			this.setNeocreditos(0);
		}
		super.realizarAtaqueEnemigo(unidades);
	}
	//METODOS JUGADOR////////////////////////////////////////////////////////////////////
	public void realizarAtaque(Unidad unidad, ArrayList<Unidad> unidades) {
		if(this.getNeocreditos() == 100) {
			potenciar();
			this.setNeocreditos(0);
		}
		super.realizarAtaque(unidad, unidades);
	}
	//HABILIDADES////////////////////////////////////////////////////////////////////////
	public void potenciar() {
		pdj.ReproducirSE(7);
		Habilidades.potenciarUnidad(this);
		Habilidades.setearEstado(this, "POWERED!");
		this.setPotenciado(true);
	}
	//METODOS AUXILIARES/////////////////////////////////////////////////////////////////
	public boolean cumpleReqDeHab1() {
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
