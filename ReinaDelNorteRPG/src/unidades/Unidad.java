package unidades;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

import principal.PanelDeJuego;
import principal.Zona;

public class Unidad {
	//SISTEMA////////////////////////////////////////////////////////
	PanelDeJuego pdj;
	Graphics2D g2;
	//POSICIONAMIENTO////////////////////////////////////////////////
	private Zona zona;
	private int posX = 0;
	private int posY = 0;
	//INDICADORES DE SALUD///////////////////////////////////////////
	int barraHP = 72; 
	private String dañoRecibido;
	private String curaRecibida;
	//ESTADOS Y COMPORTAMIENTOS//////////////////////////////////////
	private boolean aliado = false;
	private boolean activo = true;
	private boolean vivo = true;
	private boolean esCritico;
	private boolean esCurar;
	//VARIABLES PARA LA SACUDIDA/////////////////////////////////////
	private boolean enSacudida = false;
	private int duracionSacudida = 0; // Duración en frames
	private int desplazamientoSacudidaX = 0;
	private int desplazamientoSacudidaY = 0;
	private int desplazarDañoRecibido;
    //ESTADISTICAS DE LA UNIDAD//////////////////////////////////////
	private String nombre;
	private int hp;
	private int hpMax;
	private int sp;
	private int spMax;
	private int atq;
	private int def;
	private double pcrt;
	////////////////////////////////////////////////////////////////
	public Unidad(Zona zona,boolean aliado, PanelDeJuego pdj) {
		this.zona = zona;
		setPosX(zona.x);
		setPosY(zona.y);
		this.pdj = pdj;
		this.setAliado(aliado);
		dañoRecibido = "";
		curaRecibida = "";
		if(aliado) {
			desplazarDañoRecibido = 384;
		}
		else {
			desplazarDañoRecibido = 96;
		}
	}
	//METODOS PRINCIPALES///////////////////////////////////////////
	public void actualizar() {
        if (enSacudida) {
            if (duracionSacudida > 0) {
                desplazamientoSacudidaX = (int) (Math.random() * 10 - 5);
                desplazamientoSacudidaY = (int) (Math.random() * 10 - 5);
                duracionSacudida--;
                desplazarDañoRecibido--;
                
            } else {
                enSacudida = false;
                esCurar = false;
                desplazamientoSacudidaX = 0;
                desplazamientoSacudidaY = 0;
                desplazarDañoRecibido = getPosY();
                dañoRecibido = "";
                esCritico = false;
            }
        }
        if(getHP() <= 0) {
        	setAlive(false);
        }
    }
	
	public void dibujar(Graphics2D g2) {
        this.g2 = g2;
        if (isAliado()) {
            g2.setColor(Color.BLUE);
        } else {
            g2.setColor(Color.RED);
        }
        if(!isAlive()) {
        	g2.setColor(Color.YELLOW);
        }
        int dibujarX = getPosX() + desplazamientoSacudidaX;
        int dibujarY = getPosY() + desplazamientoSacudidaY;
        g2.fillRect(dibujarX + 24, dibujarY - 24, pdj.tamañoDeBaldosa, pdj.tamañoDeBaldosa * 2);
        
        //MOSTRAR DAÑO RECIBIDO////////////////////////////
        if(this.esCurar) {
            g2.setColor(Color.green);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16f));
            g2.drawString(curaRecibida , getPosX()+84, desplazarDañoRecibido-48);
        }
        else if(this.esCritico) {
        	g2.setColor(Color.YELLOW);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20f));
            g2.drawString(dañoRecibido, getPosX()+84, desplazarDañoRecibido-48);
        }
        else {
        	g2.setColor(Color.WHITE);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16f));
            g2.drawString(dañoRecibido, getPosX()+84, desplazarDañoRecibido-48);
        }

        
    }
	//METODOS DE ACCION///////////////////////////////////////////////////////
	public void realizarAccion(ArrayList<Unidad> enemigos, ArrayList<Unidad> aliados) {
		int accion = elegirAleatorio();
		if(accion == 0 && puedeUsarHabilidad()) {
			usarHabilidad(aliados);
		}
		else {
			ataqueEnemigo(enemigos);
		}
	}
	
	public void atacar(Unidad unidad) {
		boolean isCritical = Math.random() <= this.getPCRT();
	    	 
		int daño = Math.max(1, this.getAtq() - unidad.getDef());
	    	 
		if (isCritical) {
			daño *= 2;
		}
		unidad.recibirDaño(daño, isCritical);
	}
	
	public void recibirDaño(int daño, boolean isCritical) {
		this.setHP(this.getHP() - daño);
		if(isCritical) {
			dañoRecibido = "CRITICAL " + daño +"!";
			this.esCritico = true;
		}
		else {
			dañoRecibido = "" + daño;
		}

	    enSacudida = true;
	    duracionSacudida = 20;
	    
	}
	
	public void ataqueEnemigo(ArrayList<Unidad> unidades) {
		Unidad objetivo = elegirObjetivo(unidades);
		boolean isCritical = Math.random() <= this.getPCRT();
	    
		if(objetivo != null) {
			int daño = Math.max(1, this.getAtq() - objetivo.getDef());
	    	 
			if (isCritical) {
				daño *= 2;
			}
			objetivo.recibirDaño(daño, isCritical);
		}
	}
	
	public void restaurarHP(int curacion) {
		if((this.getHP() + curacion) > this.getHPMax()) {
			this.setHP(this.getHPMax());
		}
		else {
			this.setHP(this.getHP() + curacion);
		}
		curaRecibida = "+" + curacion;
		esCurar = true;
		enSacudida = true;
	    duracionSacudida = 20;
	}
	
	public void usarHabilidad(ArrayList<Unidad> unidades) {}
	
	//METODOS VISUALES/////////////////////////////////////////////////////////
	public void dibujarVida() {
		
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16f));
		g2.setColor(Color.white);
		int hp = calcularBarraHP();
		int altura = -pdj.tamañoDeBaldosa-(pdj.tamañoDeBaldosa/8);
		
		g2.drawString(getNombre(), getPosX()+10, getPosY()-10+altura);
		Color c = new Color(0,0,0, 200);
		g2.setColor(c);
		g2.fillRoundRect(getPosX()+10, getPosY()+altura, barraHP, pdj.tamañoDeBaldosa/5, 5, 5);
		g2.setColor(Color.red);
		g2.fillRoundRect(getPosX()+10, getPosY()+altura, hp, pdj.tamañoDeBaldosa/5, 5, 5);
		
		c = new Color(255, 255, 255);
		g2.setColor(c);
		g2.setStroke(new BasicStroke(2));
		g2.drawRoundRect(getPosX()+10, getPosY()+altura, barraHP, pdj.tamañoDeBaldosa/5, 5, 5);
	}
	
	//METODOS DE ELECCION/////////////////////////////////////////////////////
	public Unidad elegirObjetivo(ArrayList<Unidad> unidades) {
	    if (!unidades.isEmpty()) {
	        return unidades.get((int) (Math.random() * unidades.size()));
	    }
	    return null;
	}
	
	public int elegirAleatorio() {
	    Random random = new Random();
	    return random.nextInt(4);
	}
	
	//METODOS AUXILIARES//////////////////////////////////////////////////////
	public void posicionar(Zona zona) {
		setPosX(zona.x);
		setPosY(zona.y);
	}
	
	public int calcularBarraHP() {
		return (getHP()*barraHP)/getHPMax();
	}
	
	public boolean puedeUsarHabilidad() {
		return false;
	}
	
	//GETTERS & SETTERS//////////////////////////////////////
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getHP() {
		return hp;
	}

	public void setHP(int hp) {
		this.hp = hp;
	}

	public int getHPMax() {
		return hpMax;
	}

	public void setHPMax(int hpMax) {
		this.hpMax = hpMax;
	}

	public int getSP() {
		return sp;
	}

	public void setSP(int sp) {
		this.sp = sp;
	}

	public int getSPMax() {
		return spMax;
	}

	public void setSPMax(int spMax) {
		this.spMax = spMax;
	}

	public int getAtq() {
		return atq;
	}

	public void setAtq(int atq) {
		this.atq = atq;
	}

	public int getDef() {
		return def;
	}

	public void setDef(int def) {
		this.def = def;
	}

	public double getPCRT() {
		return pcrt;
	}

	public void setPCRT(double pcrt) {
		this.pcrt = pcrt;
	}

	public boolean isAliado() {
		return aliado;
	}

	public void setAliado(boolean aliado) {
		this.aliado = aliado;
	}

	public boolean isAlive() {
		return vivo;
	}

	public void setAlive(boolean vivo) {
		this.vivo = vivo;
	}

	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}
}
