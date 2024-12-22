package unidades;

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
	/////////////////////////////////////////////
	int hp;
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
	
	public void actualizar() {}
	
	public void dibujar(Graphics2D g2) {
		this.g2 = g2;
		if(aliado) {
			g2.setColor(Color.BLUE);
		}
		else {
			g2.setColor(Color.RED);
		}
		g2.fillRect(posX+24, posY-24, pdj.tamañoDeBaldosa, pdj.tamañoDeBaldosa*2);
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
		int altura = -pdj.tamañoDeBaldosa-(pdj.tamañoDeBaldosa/4);
		if(aliado) {
			altura = pdj.tamañoDeBaldosa*2+(pdj.tamañoDeBaldosa/4);
		}
		
		g2.drawString(nombre, posX+10, posY-5+altura);
		Color c = new Color(0,0,0, 200);
		g2.setColor(c);
		g2.fillRoundRect(posX+10, posY+altura, barraHP, pdj.tamañoDeBaldosa/4, 10, 10);
		g2.setColor(Color.red);
		g2.fillRoundRect(posX+10, posY+altura, hp, pdj.tamañoDeBaldosa/4, 10, 10);
		
		c = new Color(255, 255, 255);
		g2.setColor(c);
		//g2.setStroke(new BasicStroke(4));
		g2.drawRoundRect(posX+10, posY+altura, barraHP, pdj.tamañoDeBaldosa/4, 10, 10);
	}
	
	public void recibirDaño() {
		this.hp--;
	}
}
