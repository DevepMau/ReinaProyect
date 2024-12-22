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
	int posX = 0;
	int posY = 0;
	public boolean aliado = false;
	public Zona zona;
	int barraHP = 72; 
	// Variables adicionales para la sacudida
    boolean enSacudida = false;
    int duracionSacudida = 0; // Duración en frames
    int desplazamientoSacudidaX = 0;
    int desplazamientoSacudidaY = 0;
    /////////////////////////////////////////////
	public int hp;
	int hpMax;
	int sp;
	int spMax;
	int atq;
	int def;
	double pcrt;
	public String nombre;
	
	public Unidad(Zona zona,boolean aliado, PanelDeJuego pdj) {
		this.zona = zona;
		posX = zona.x;
		posY = zona.y;
		this.pdj = pdj;
		this.aliado = aliado;
	}
	
	public void actualizar() {
        if (enSacudida) {
            if (duracionSacudida > 0) {
                // Generar un pequeño desplazamiento aleatorio
                desplazamientoSacudidaX = (int) (Math.random() * 10 - 5);
                desplazamientoSacudidaY = (int) (Math.random() * 10 - 5);
                duracionSacudida--;
            } else {
                // Termina la sacudida
                enSacudida = false;
                desplazamientoSacudidaX = 0;
                desplazamientoSacudidaY = 0;
            }
        }
    }
	
	public void dibujar(Graphics2D g2) {
        this.g2 = g2;
        if (aliado) {
            g2.setColor(Color.BLUE);
        } else {
            g2.setColor(Color.RED);
        }

        // Aplicar el desplazamiento de la sacudida
        int dibujarX = posX + desplazamientoSacudidaX;
        int dibujarY = posY + desplazamientoSacudidaY;

        g2.fillRect(dibujarX + 24, dibujarY - 24, pdj.tamañoDeBaldosa, pdj.tamañoDeBaldosa * 2);
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
		//if(aliado) {
			//altura = pdj.tamañoDeBaldosa*2+(pdj.tamañoDeBaldosa/4);
		//}
		
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

	        // Activar la sacudida
	        enSacudida = true;
	        duracionSacudida = 10; // Duración de 10 frames (ajustable)
	    }

	    public void atacar(Unidad unidad) {
	        unidad.recibirDaño(this.atq);
	    }
}
