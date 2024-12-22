package principal;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import unidades.Elite;
import unidades.Especialista;
import unidades.Recluta;
import unidades.Soldado;
import unidades.Unidad;

public class Combate {
	PanelDeJuego pdj;
	Graphics2D g2;
	HashMap<Integer, Zona> zonas = new HashMap<>();
	ArrayList<Unidad> unidades = new ArrayList<>();
	int id = 0;
	boolean habilitar = true;
	boolean turnoJugador = true;
	
	public Combate(PanelDeJuego pdj) {
		this.pdj = pdj;
		crearTablero();
		unidades.add(new Recluta(zonas.get(0), true, pdj));
		unidades.add(new Recluta(zonas.get(0), false, pdj));
		unidades.add(new Soldado(zonas.get(0), true, pdj));
		unidades.add(new Elite(zonas.get(0), true, pdj));
		unidades.add(new Soldado(zonas.get(0), false, pdj));
		unidades.add(new Elite(zonas.get(0), false, pdj));
		unidades.add(new Especialista(zonas.get(0), false, pdj));
		
	}
	/////////////////////////////////////////////////////////
	public void actualizar() {
		int idAli = 5;
		int idEne = 1;
		for (Unidad unidad : unidades) {
		    if(unidad.aliado && idAli <= 8) {
		    	unidad.posicionar(zonas.get(idAli));
		    	idAli++;
		    }
		    else if(!unidad.aliado && idEne <= 4) {
		    	unidad.posicionar(zonas.get(idEne));
		    	idEne++;
		    }
		}
		for (Unidad unidad : unidades) {
			unidad.actualizar();
		}
		realizarTurno();
		
	}
	
	public void dibujar(Graphics2D g2) {
		this.g2 = g2;
		for (Zona zona : zonas.values()) {
			zona.dibujar(g2);
		}
		for (Unidad unidad : unidades) {
		    unidad.dibujar(g2);
		    unidad.dibujarVida();
		}
		//dibujarRetrato(zonas.get(32).x, zonas.get(32).y, pdj.tamañoDeBaldosa*4, pdj.tamañoDeBaldosa*4);
	}
	//////////////////////////////////////////////////////////
	public void crearTablero() {
		int x = 0;
		int y = 0;
		int n = 0;
		int id = 0;
		for (int i = 0; i < 6; i++) {
			for (int k = 0; k < 8; k++) {
				if(verificar(n)) {
					if(n == 32) {
						zonas.put(9, new Zona(9, x, y, pdj));
					}
					else {
						zonas.put(id, new Zona(id, x, y, pdj));
						id++;
					}
				}
				n++;
				x += pdj.tamañoDeBaldosa*2;
			}
			x = 0;
			y += pdj.tamañoDeBaldosa*2;
		}
	}
	
	public void dibujarVentana(int x, int y, int width, int height) {
		Color c = new Color(0,0,0, 200);
		g2.setColor(c);
		g2.fillRoundRect(x+5, y+5, width, height, 35, 35);
		
		c = new Color(255, 255, 255);
		g2.setColor(c);
		g2.setStroke(new BasicStroke(5));
		g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
	}
	
	public void dibujarRetrato(int x, int y, int width, int height) {
		Color c = new Color(135, 206, 235);
		g2.setColor(c);
		g2.setStroke(new BasicStroke(5));
		g2.fillRoundRect(x+width/4, y+5, width/2, height/2, 100, 100);
		g2.fillRect(x+width/4, y+height/2, width/2, height/2);
		g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
	}
	
	public boolean verificar(int n) {
		if(n == 0 || (10 <= n && n <= 13) || (34 <= n && n <= 37) || n == 32) {
			return true;
		}
		return false;
	}
	
	////////////////////////////////////////
	public void realizarTurno() {
	    for (Unidad unidad : unidades) {
	        if (unidad.aliado && turnoJugador) {
	            // Unidad aliada: Permitir elegir un objetivo enemigo
	            Unidad objetivo = elegirObjetivoEnemigo();
	            if (objetivo != null) {
	                unidad.atacar(objetivo);
	                turnoJugador = false;
	            }
	        } else if(!turnoJugador) {
	            // Unidad enemiga: Seleccionar automáticamente un objetivo aliado
	            Unidad objetivo = elegirObjetivoAliado();
	            if (objetivo != null) {
	                unidad.atacar(objetivo);
	                turnoJugador = true;
	            }
	        }
	    }
	}

	private Unidad elegirObjetivoEnemigo() {
	    // Lista de enemigos en zonas 1 a 4
	    ArrayList<Unidad> enemigos = new ArrayList<>();
	    for (Unidad unidad : unidades) {
	    	if(unidad.hp > 0) {
	    		if (!unidad.aliado) {
		            enemigos.add(unidad);
		        }
	    	}
	    }
	    // Aquí puedes implementar la lógica para elegir al enemigo (manual o automática)
	    if (!enemigos.isEmpty()) {
	    	if(pdj.teclado.RIGHT == true && habilitar) {
	    		if(id >= enemigos.size()-1) {
	    			id = 0;
	    		}
	    		else {
	    			id++;
	    		}
	    		habilitar = false;
	    		System.out.println(id);
	    	}
	    	if(pdj.teclado.LEFT == true && habilitar) {
	    		if(id <= 0) {
	    			id = enemigos.size()-1;
	    		}
	    		else {
	    			id--;
	    		}
	    		habilitar = false;
	    		System.out.println(id);
	    	}
	    	if(!pdj.teclado.RIGHT && !pdj.teclado.LEFT && !pdj.teclado.ENTER) {
	    		habilitar = true;
	    	}
	    	if(pdj.teclado.ENTER == true && habilitar) {
	    		habilitar = false;
	    		return enemigos.get(id);
	    	}
	    }
	    return null;
	}

	private Unidad elegirObjetivoAliado() {
	    // Lista de aliados en zonas 5 a 8
	    ArrayList<Unidad> aliados = new ArrayList<>();
	    for (Unidad unidad : unidades) {
	    	if(unidad.hp > 0) {
	    		if (unidad.aliado) {
		            aliados.add(unidad);
		        }
	    	}
	    }

	    // Elegir un objetivo al azar (puedes personalizar esto)
	    if (!aliados.isEmpty()) {
	        return aliados.get((int) (Math.random() * aliados.size()));
	    }
	    return null;
	}


}
