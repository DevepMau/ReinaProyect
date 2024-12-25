package unidades;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

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
	public boolean esCritico = false;
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
	
	public Unidad(Zona zona,boolean aliado, PanelDeJuego pdj) {
		this.zona = zona;
		posX = zona.x;
		posY = zona.y;
		this.pdj = pdj;
		this.aliado = aliado;
		dañoRecibido = "";
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
                desplazamientoSacudidaX = 0;
                desplazamientoSacudidaY = 0;
                desplazarDañoRecibido = posY;
                dañoRecibido = "";
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

        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16f));
        g2.drawString(dañoRecibido, posX+84, desplazarDañoRecibido-48);
    }
	
	public void posicionar(Zona zona) {
		posX = zona.x;
		posY = zona.y;
	}
	
	public int calcularBarraHP() {
		return (hp*barraHP)/hpMax;
	}
	
	public void dibujarVida() {
		
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 12f));
		g2.setColor(Color.white);
		int hp = calcularBarraHP();
		int altura = -pdj.tamañoDeBaldosa-(pdj.tamañoDeBaldosa/8);
		
		g2.drawString(nombre, posX+10, posY-5+altura);
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
	
	public void recibirDaño(int daño) {
		this.hp -= daño;
		dañoRecibido = "" + daño;

	    enSacudida = true;
	    duracionSacudida = 20;
	}

	public void atacar(Unidad unidad) {
		esCritico = Math.random() <= this.pcrt;
	    	 
		int daño = Math.max(1, this.atq - unidad.def);
	    	 
		if (esCritico) {
			daño *= 2;
		}
		unidad.recibirDaño(daño);
	}
}
