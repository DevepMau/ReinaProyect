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
	/////////////////////////////////////////////
	PanelDeJuego pdj;
	Graphics2D g2;
	public int posX = 0;
	public int posY = 0;
	public boolean aliado = false;
	public boolean activo = true;
	public boolean esCritico;
	public boolean esCurar;
	public Zona zona;
	int barraHP = 72; 
	public boolean vivo = true;
	// Variables adicionales para la sacudida
    boolean enSacudida = false;
    int duracionSacudida = 0; // Duración en frames
    int desplazamientoSacudidaX = 0;
    int desplazamientoSacudidaY = 0;
    int desplazarDañoRecibido;
    /////////////////////////////////////////////
	public int hp;
	int hpMax;
	int sp;
	int spMax;
	int atq;
	int def;
	double pcrt;
	public String nombre;
	public String dañoRecibido;
	public String curaRecibida;
	
	public Unidad(Zona zona,boolean aliado, PanelDeJuego pdj) {
		this.zona = zona;
		posX = zona.x;
		posY = zona.y;
		this.pdj = pdj;
		this.aliado = aliado;
		dañoRecibido = "";
		curaRecibida = "";
		if(aliado) {
			desplazarDañoRecibido = 384;
		}
		else {
			desplazarDañoRecibido = 96;
		}
	}
	
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
                desplazarDañoRecibido = posY;
                dañoRecibido = "";
                esCritico = false;
            }
        }
        if(hp <= 0) {
        	vivo = false;
        }
    }
	
	public void dibujar(Graphics2D g2) {
        this.g2 = g2;
        if (aliado) {
            g2.setColor(Color.BLUE);
        } else {
            g2.setColor(Color.RED);
        }
        if(!vivo) {
        	g2.setColor(Color.YELLOW);
        }
        int dibujarX = posX + desplazamientoSacudidaX;
        int dibujarY = posY + desplazamientoSacudidaY;
        g2.fillRect(dibujarX + 24, dibujarY - 24, pdj.tamañoDeBaldosa, pdj.tamañoDeBaldosa * 2);
        
        //MOSTRAR DAÑO RECIBIDO////////////////////////////
        if(this.esCurar) {
            g2.setColor(Color.green);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16f));
            g2.drawString(curaRecibida , posX+84, desplazarDañoRecibido-48);
        }
        else if(this.esCritico) {
        	g2.setColor(Color.YELLOW);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20f));
            g2.drawString(dañoRecibido, posX+84, desplazarDañoRecibido-48);
        }
        else {
        	g2.setColor(Color.WHITE);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16f));
            g2.drawString(dañoRecibido, posX+84, desplazarDañoRecibido-48);
        }

        
    }
	
	public void posicionar(Zona zona) {
		posX = zona.x;
		posY = zona.y;
	}
	
	public int calcularBarraHP() {
		return (hp*barraHP)/hpMax;
	}
	
	public void dibujarVida() {
		
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16f));
		g2.setColor(Color.white);
		int hp = calcularBarraHP();
		int altura = -pdj.tamañoDeBaldosa-(pdj.tamañoDeBaldosa/8);
		
		g2.drawString(nombre, posX+10, posY-10+altura);
		Color c = new Color(0,0,0, 200);
		g2.setColor(c);
		g2.fillRoundRect(posX+10, posY+altura, barraHP, pdj.tamañoDeBaldosa/5, 5, 5);
		g2.setColor(Color.red);
		g2.fillRoundRect(posX+10, posY+altura, hp, pdj.tamañoDeBaldosa/5, 5, 5);
		
		c = new Color(255, 255, 255);
		g2.setColor(c);
		g2.setStroke(new BasicStroke(2));
		g2.drawRoundRect(posX+10, posY+altura, barraHP, pdj.tamañoDeBaldosa/5, 5, 5);
	}
	
	public void recibirDaño(int daño, boolean isCritical) {
		this.hp -= daño;
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
	
	public void atacar(Unidad unidad) {
		boolean isCritical = Math.random() <= this.pcrt;
	    	 
		int daño = Math.max(1, this.atq - unidad.def);
	    	 
		if (isCritical) {
			daño *= 2;
		}
		unidad.recibirDaño(daño, isCritical);
	}
	
	public void realizarAccion(ArrayList<Unidad> enemigos, ArrayList<Unidad> aliados) {
		int accion = elegirAleatorio();
		if(accion == 0 && puedeUsarHabilidad()) {
			usarHabilidad(aliados);
		}
		else {
			ataqueEnemigo(enemigos);
		}
	}

	public void ataqueEnemigo(ArrayList<Unidad> unidades) {
		Unidad objetivo = elegirObjetivo(unidades);
		boolean isCritical = Math.random() <= this.pcrt;
	    
		if(objetivo != null) {
			int daño = Math.max(1, this.atq - objetivo.def);
	    	 
			if (isCritical) {
				daño *= 2;
			}
			objetivo.recibirDaño(daño, isCritical);
		}
	}
	
	public void usarHabilidad(ArrayList<Unidad> unidades) {}
	
	public Unidad elegirObjetivo(ArrayList<Unidad> unidades) {
	    if (!unidades.isEmpty()) {
	        return unidades.get((int) (Math.random() * unidades.size()));
	    }
	    return null;
	}
	
	public void restaurarHP(int curacion) {
		if((this.hp + curacion) > this.hpMax) {
			this.hp = this.hpMax;
		}
		else {
			this.hp += curacion;
		}
		curaRecibida = "+" + curacion;
		esCurar = true;
		enSacudida = true;
	    duracionSacudida = 20;
	}
	
	public boolean puedeUsarHabilidad() {
		return false;
	}
	
	public int elegirAleatorio() {
	    Random random = new Random();
	    return random.nextInt(4);
	}
	
	//GETTERS & SETTERS//////////////////////////////////////
	public int getHp() {
        return this.hp;
    }
}
