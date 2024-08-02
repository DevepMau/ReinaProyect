package principal;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Random;

public class Personaje {
	
	PanelDeJuego pdj;
	Graphics2D g2;
	public int ancho, alto;
	boolean boo;
	Zona zonaInicio;
	public int x, y;
	private boolean enMovimiento = true;
	public boolean enTurno = true;
	public boolean enSacudida = false;
	public int contador = 15;
	public int velocidad;
	
	public Personaje(Zona zona, boolean jugador, PanelDeJuego pdj) {
		this.pdj = pdj;
		this.x = (int)zona.getPos().getX()+pdj.tamañoDeBaldosa/2;
		this.y = (int)zona.getPos().getY()+pdj.tamañoDeBaldosa/2;
		zonaInicio = zona;
		ancho = alto = pdj.tamañoDeBaldosa;
		boo = jugador;
		Random rand = new Random();
		velocidad = rand.nextInt(20);
	}
	
	public void dibujar(Graphics2D g2) {
		this.g2 = g2;
		if(boo) {
			g2.setColor(Color.cyan);
		}
		else {
			g2.setColor(Color.red);
		}
		g2.fillRect(x, y, ancho, alto);
		
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void moverHaciaDestino(Personaje e) {
		int destinoX = e.zonaInicio.getX() + pdj.tamañoDeBaldosa / 2;
		int destinoY = e.zonaInicio.getY() + pdj.tamañoDeBaldosa / 2;
		// DIFERENCIAS ENTRE LAS COORDENADAS ACTUALES Y LAS DEL DESTINO
        int deltaX = destinoX - this.x;
        int deltaY = destinoY - this.y;
        // CALCULAR LA DISTANCIA A RECORRER
        double distancia = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        // CALCULAR LA VELOCIDAD SEGÚN LA DISTANCIA (CUANTO MÁS CERCA, MÁS LENTO)
        double velocidad = Math.max(1, distancia / 10); // AJUSTAR EL DIVISOR PARA CAMBIAR LA DESACELERACIÓN
        // SI LA DISTANCIA ES MAYOR QUE CERO, CALCULAR LOS INCREMENTOS
        if (distancia > 0) {
            double incrementoX = (deltaX / distancia) * velocidad;
            double incrementoY = (deltaY / distancia) * velocidad;
            // ACTUALIZAR LAS COORDENADAS
            this.x += incrementoX;
            this.y += incrementoY;
            // REDONDEAR A ENTEROS PARA EVITAR PROBLEMAS DE PRECISIÓN ACUMULATIVA
            this.x = Math.round(this.x);
            this.y = Math.round(this.y);
            // COMPROBAR SI HEMOS ALCANZADO EL DESTINO
            if (distancia < 20 || Math.abs(deltaX) <= Math.abs(incrementoX) && Math.abs(deltaY) <= Math.abs(incrementoY)) {
                this.x = destinoX;
                this.y = destinoY;   
                enSacudida = true;
                enMovimiento = false;
            }
        }  		
    }
	
	public void moverHaciaPosInicial() {
		int destinoX = zonaInicio.getX() + pdj.tamañoDeBaldosa / 2;
		int destinoY = zonaInicio.getY() + pdj.tamañoDeBaldosa / 2;
		// DIFERENCIAS ENTRE LAS COORDENADAS ACTUALES Y LAS DEL DESTINO
        int deltaX = destinoX - this.x;
        int deltaY = destinoY - this.y;   
        // CALCULAR LA DISTANCIA A RECORRER
        double distancia = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        // CALCULAR LA VELOCIDAD SEGÚN LA DISTANCIA (CUANTO MÁS CERCA, MÁS LENTO)
        double velocidad = Math.max(1, distancia / 10); // AJUSTAR EL DIVISOR PARA CAMBIAR LA DESACELERACIÓN
        // SI LA DISTANCIA ES MAYOR QUE CERO, CALCULAR LOS INCREMENTOS
        if (distancia > 0) {
            double incrementoX = (deltaX / distancia) * velocidad;
            double incrementoY = (deltaY / distancia) * velocidad;
            // ACTUALIZAR LAS COORDENADAS
            this.x += incrementoX;
            this.y += incrementoY;
            // REDONDEAR A ENTEROS PARA EVITAR PROBLEMAS DE PRECISIÓN ACUMULATIVA
            this.x = Math.round(this.x);
            this.y = Math.round(this.y);
            // COMPROBAR SI HEMOS ALCANZADO EL DESTINO
            if (distancia < 20 || (Math.abs(deltaX) <= Math.abs(incrementoX) && Math.abs(deltaY) <= Math.abs(incrementoY))) {
                this.x = destinoX;
                this.y = destinoY;  
                enTurno = false;
                enMovimiento = true;
            }
        }		
    }

	public void sacudir(Personaje e) {
		int shakeAmount = 5; // LA CANTIDAD DE PÍXELES QUE SE MUEVE PARA SACUDIR
		setX(zonaInicio.getX() + (int)(Math.random() * shakeAmount * 2 - shakeAmount) + pdj.tamañoDeBaldosa/2);
	    setY(zonaInicio.getY() + (int)(Math.random() * shakeAmount * 2 - shakeAmount) + pdj.tamañoDeBaldosa/2); 
	    if(contador == 0) {
	    	e.enSacudida = false;
	    	contador = 20; 	
	    }
	    contador--;   
	}

	public void accionAtaque(Personaje e) {
		if(enTurno) {
			if(enMovimiento) {
				moverHaciaDestino(e);
			}
			if(enSacudida) {
				e.sacudir(this);
			}
			if(!enMovimiento && !enSacudida) {
				moverHaciaPosInicial();
			}
		}
	}
	
	public void reset() {
		this.enTurno = true;
	}

}
