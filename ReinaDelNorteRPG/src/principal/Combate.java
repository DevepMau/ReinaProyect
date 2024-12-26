package principal;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
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
	String[] acciones = new String[4];
	int id = 0;
	int pos = 0;
	int timer = 100;
	int tamañoLista = 0;
	int numeroDeInstruccion = 0;
	int instruccionElegida = -1;
	boolean habilitar = true;
	boolean turnoJugador = true;
	Rectangle selector;
	
	public Combate(PanelDeJuego pdj) {
		this.pdj = pdj;
		crearTablero();
		acciones[0] = "ATACAR";
		acciones[1] = "DEFENDER";
		acciones[2] = "HABILIDAD";
		acciones[3] = "USAR OBJETO";
		selector = new Rectangle(0, 0, pdj.tamañoDeBaldosa*2, pdj.tamañoDeBaldosa/8);
		unidades.add(new Recluta(zonas.get(0), true, pdj));
		unidades.add(new Recluta(zonas.get(0), false, pdj));
		unidades.add(new Soldado(zonas.get(0), true, pdj));
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
		if(turnoJugador) {
			menuDeOpciones(acciones, pdj.tamañoDeBaldosa*3 , pdj.altoDePantalla - pdj.tamañoDeBaldosa*(acciones.length-1), 160);
			if(instruccionElegida == 0) {
				g2.setColor(Color.YELLOW);
				g2.fillRect(selector.x, selector.y, selector.width, selector.height);
			}
		}
		
		//CARTEL DE TURNO///////////////////////////////////////////////
		g2.setColor(Color.white);
		g2.drawRect(pdj.tamañoDeBaldosa*4, pdj.tamañoDeBaldosa*5, pdj.tamañoDeBaldosa*8, pdj.tamañoDeBaldosa);
		if(turnoJugador) {
			g2.setColor(Color.BLUE);
			g2.fillRect(pdj.tamañoDeBaldosa*4, pdj.tamañoDeBaldosa*5, pdj.tamañoDeBaldosa*8, pdj.tamañoDeBaldosa);
			g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30f));
			g2.setColor(Color.white);
			g2.drawString("Turno: "+unidades.get(id).nombre, pdj.tamañoDeBaldosa*5, pdj.tamañoDeBaldosa*5+32);
		}
		else {
			g2.setColor(Color.RED);
			g2.fillRect(pdj.tamañoDeBaldosa*4, pdj.tamañoDeBaldosa*5, pdj.tamañoDeBaldosa*8, pdj.tamañoDeBaldosa);
			g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30f));
			g2.setColor(Color.white);
			g2.drawString("Turno: "+unidades.get(id).nombre, pdj.tamañoDeBaldosa*5, pdj.tamañoDeBaldosa*5+32);
		}
		
	}
	
	//METODOS PARA JUGAR////////////////////////////////////////////////////
	public void realizarTurno() {
		ArrayList<Unidad> enemigos = new ArrayList<>();
	    ArrayList<Unidad> aliados = new ArrayList<>();
	    for (Unidad unidad : unidades) {
	        if (unidad.hp > 0) {
	            if (!unidad.aliado) {
	                enemigos.add(unidad);
	            } else {
	                aliados.add(unidad);
	            }
	        }
	    }
	    if(unidades.get(id).vivo) {
	    	if(unidades.get(id).aliado) {
		    	turnoJugador = true;
		    	if(instruccionElegida == -1) {
		    		instruccionElegida = elegirAccion();
		    	}
		    	else if(instruccionElegida == 0) {
		    		Unidad unidadSeleccionada = elegirUnidad(enemigos);
			    	if(unidadSeleccionada != null) {
			    		unidades.get(id).atacar(unidadSeleccionada);
			    		if(unidadSeleccionada.hp <= 0) {
			    			pos = 0;
			    		}
			    		if(id >= unidades.size()-1) {
			    			id = 0;
			    		}
			    		else {
			    			id++;
			    		}
			    		instruccionElegida = -1;
			    	}
		    	}
		    }
		    else {
		    	turnoJugador = false;
		    	if(timer == 0) {
		    		unidades.get(id).atacar(elegirObjetivoAleatorio(aliados));
			    	if(id >= unidades.size()-1) {
		    			id = 0;
		    		}
		    		else {
		    			id++;
		    		}
			    	timer = 100;
		    	}
		    	timer--;
		    }
	    }
	    else {
	    	if(id >= unidades.size()-1) {
	    		id = 0;
	    	}
	    	else {
		    	id++;
	    	}
	    }
	}
	
	//METODOS PARA ELEGIR UNIDADES//////////////////////////////////////////
	private Unidad elegirUnidad(ArrayList<Unidad> unidades) {
	    if (!unidades.isEmpty()) {
	    	actualizarSelector(unidades.get(pos));
	    	if(pdj.teclado.RIGHT == true && habilitar) {
	    		if(pos >= unidades.size()-1) {
	    			pos = 0;
	    		}
	    		else {
	    			pos++;
	    		}
	    		habilitar = false;
	    	}
	    	if(pdj.teclado.LEFT == true && habilitar) {
	    		if(pos <= 0) {
	    			pos = unidades.size()-1;
	    		}
	    		else {
	    			pos--;
	    		}
	    		habilitar = false;
	    	}
	    	if(!pdj.teclado.RIGHT && !pdj.teclado.LEFT && !pdj.teclado.ENTER) {
	    		habilitar = true;
	    	}
	    	if(pdj.teclado.ENTER == true && habilitar) {
	    		habilitar = false;
	    		return unidades.get(pos);
	    	}
	    }
	    return null;
	}
	
	private Unidad elegirObjetivoAleatorio(ArrayList<Unidad> unidades) {
	    if (!unidades.isEmpty()) {
	        return unidades.get((int) (Math.random() * unidades.size()));
	    }
	    return null;
	}
	
	//SUBMENUS//////////////////////////////////////////////////////////////////
	public void menuDeOpciones(String[] opciones, int posX, int posY, int anchoVentana) {
		dibujarVentana(posX, posY-8, anchoVentana, pdj.tamañoDeBaldosa*(opciones.length-1)+8);
		//OPCIONES Y RECUADRO DE SELECCION
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 24f));
		for (int i = 0; i < opciones.length; i++) {
			g2.setColor(Color.white);
			g2.drawString(opciones[i], posX+pdj.tamañoDeBaldosa/4, posY+24);
			if(numeroDeInstruccion == i) {
				if(instruccionElegida == -1) {
					g2.setColor(Color.WHITE);
					g2.fillRoundRect(posX+5, posY, anchoVentana-10 , 32, 5, 5);
					g2.setColor(Color.BLACK);
					g2.drawString(opciones[i], posX+pdj.tamañoDeBaldosa/4, posY+24);
				}
				else {
					g2.setColor(Color.white);
					g2.drawRoundRect(posX+5, posY, anchoVentana-10 , 32, 5, 5);
				}
			}
			posY += (pdj.tamañoDeBaldosa/2)+(pdj.tamañoDeBaldosa/4);
		}
	}
	
	public int elegirAccion() {
		if(pdj.teclado.RIGHT == true && habilitar) {
    		if(numeroDeInstruccion >= acciones.length-1) {
    			numeroDeInstruccion = 0;
    		}
    		else {
    			numeroDeInstruccion++;
    		}
    		habilitar = false;
    	}
    	if(pdj.teclado.LEFT == true && habilitar) {
    		if(numeroDeInstruccion <= 0) {
    			numeroDeInstruccion = acciones.length-1;
    		}
    		else {
    			numeroDeInstruccion--;
    		}
    		habilitar = false;
    	}
    	if(!pdj.teclado.RIGHT && !pdj.teclado.LEFT && !pdj.teclado.ENTER) {
    		habilitar = true;
    	}
    	if(pdj.teclado.ENTER == true && habilitar) {
    		habilitar = false;
    		return instruccionElegida = numeroDeInstruccion;
    	}
    return -1;
	}
	
	//METODOS DE TABLERO////////////////////////////////////////////////////////
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
		g2.fillRoundRect(x+5, y+5, width, height, 10, 10);
		
		c = new Color(255, 255, 255);
		g2.setColor(c);
		//g2.setStroke(new BasicStroke(5));
		g2.drawRoundRect(x+5, y+5, width-10, height-10, 10, 10);
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
	
	public void actualizarSelector(Unidad unidad) {
		if(unidad != null) {
			selector.setLocation(unidad.posX, unidad.posY+pdj.tamañoDeBaldosa*2);
		}
	}
}
