package principal;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import unidades.Elite;
import unidades.Especialista;
import unidades.CebadorDeMate;
import unidades.Combatiente;
import unidades.Unidad;

public class Combate {
	//RECURSOS Y ESTRUCTURAS/////////////////////////////////////////////////
	PanelDeJuego pdj;
	Graphics2D g2;
	private HashMap<Integer, Zona> zonas = new HashMap<>();
	private ArrayList<Unidad> unidades = new ArrayList<>();
	private Unidad unidadSeleccionada = null;
	//CONFIGURACION GENERAL//////////////////////////////////////////////////
	private String[] acciones = new String[3];
	private int id = 0;
	private int pos = 0;
	private int timer = 100;
	private boolean unidadesSinNombre = true;
	//CONTROL DE TURNO Y BOTONES/////////////////////////////////////////////
	private boolean habilitar = true;
	private boolean turnoJugador = true;
	//CONTROL DE INSTRUCCIONES///////////////////////////////////////////////
	private int numeroDeInstruccion = 0;
	private int instruccionElegida = -1;
	private Rectangle selector;
	private Rectangle resaltador;
	//OBJETOS ESPECIALES/////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////
	
	public Combate(PanelDeJuego pdj) {
		this.pdj = pdj;
		crearTablero();
		selector = new Rectangle(0, 0, pdj.tamañoDeBaldosa*2, pdj.tamañoDeBaldosa/8);
		resaltador = new Rectangle(0, 0, pdj.tamañoDeBaldosa*2, pdj.tamañoDeBaldosa/8);
		acciones[0] = "ATACAR";
		acciones[1] = "HABILIDAD";
		acciones[2] = "USAR OBJETO";
		unidades.add(new CebadorDeMate(zonas.get(0), true, pdj));
		unidades.add(new Combatiente(zonas.get(0), false, pdj));
		unidades.add(new CebadorDeMate(zonas.get(0), true, pdj));
		//unidades.add(new Elite(zonas.get(0), true, pdj));
		
	}
	//METODOS PRINCIPALES///////////////////////////////////////////////////////
	public void actualizar() {
		if(unidadesSinNombre) {
			nombrarUnidades(unidades);
			unidadesSinNombre = false;
		}
		
		int idAli = 5;
		int idEne = 1;
		for (Unidad unidad : unidades) {
		    if(unidad.isAliado() && idAli <= 8) {
		    	unidad.posicionar(zonas.get(idAli));
		    	idAli++;
		    }
		    else if(!unidad.isAliado() && idEne <= 4) {
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
		
		dibujarResaltadorDeUnidad();
		
		for (Unidad unidad : unidades) {
		    unidad.dibujar(g2);
		}
		if(turnoJugador) {
			menuDeOpciones(acciones, pdj.tamañoDeBaldosa*3 , pdj.altoDePantalla - pdj.tamañoDeBaldosa*(acciones.length-1), 160);
			if(instruccionElegida == 0 || instruccionElegida == 1) {
				g2.setColor(Color.YELLOW);
				g2.fillRect(selector.x, selector.y, selector.width, selector.height);
			}
		}
		
		//dibujarCartelDeTurno();
		if(unidadSeleccionada != null) {
			dibujarEstadisticasUnidad(unidadSeleccionada);
			dibujarTriangulo(pdj.anchoDePantalla-pdj.tamañoDeBaldosa*4+(-4), pdj.altoDePantalla-pdj.tamañoDeBaldosa*4+(-32));
		}	
		
	}
	
	//METODOS PARA JUGAR//////////////////////////////////////////////////////
	public void realizarTurno() {
		ArrayList<Unidad> enemigos = new ArrayList<>();
	    ArrayList<Unidad> aliados = new ArrayList<>();
	    for (Unidad unidad : unidades) {
	        if (unidad.getHP() > 0) {
	            if (!unidad.isAliado()) {
	                enemigos.add(unidad);
	            } else {
	                aliados.add(unidad);
	            }
	        }
	    }
	    if(unidades.get(id).isAlive()) {
	    	actualizarSelector(unidades.get(id), resaltador);
	    	if(unidades.get(id).isAliado()) {
	    		unidadSeleccionada = unidades.get(id);
		    	turnoJugador = true;
		    	if(instruccionElegida == -1) {
		    		instruccionElegida = elegirAccion();
		    	}
		    	//ATAQUE JUGADOR////////////////////////////////////////
		    	else if(instruccionElegida == 0) {
		    		Unidad unidadSeleccionada = elegirUnidad(enemigos);
			    	if(unidadSeleccionada != null) {
			    		unidades.get(id).realizarAtaque(unidadSeleccionada);
			    		if(unidadSeleccionada.getHP() <= 0) {
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
		    	//HABILIDAD JUGADOR/////////////////////////////////////
		    	else if(instruccionElegida == 1) {
		    		Unidad unidadSeleccionada = elegirUnidad(aliados);
		    		if(unidadSeleccionada != null) {
		    			unidades.get(id).usarHabilidad(unidadSeleccionada);
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
		    	unidadSeleccionada = null;
		    	turnoJugador = false;
		    	if(timer == 0) {
		    		numeroDeInstruccion = 0;
		    		unidades.get(id).realizarAccion(aliados, enemigos);
			    	if(id >= unidades.size()-1) {
		    			id = 0;
		    		}
		    		else {
		    			id++;
		    		}
			    	timer = 100;
		    	}
		    	pos = 0;
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
	
	//METODOS DE ELECCION////////////////////////////////////////////////////
	private Unidad elegirUnidad(ArrayList<Unidad> unidades) {
	    if (!unidades.isEmpty()) {
	    	actualizarSelector(unidades.get(pos), selector);
	    	if(pdj.teclado.RIGHT == true && habilitar) {
	    		if(pos >= unidades.size()-1) {
	    			pos = 0;
	    		}
	    		else {
	    			pos++;
	    		}
	    		habilitar = false;
	    		pdj.ReproducirSE(1);
	    	}
	    	if(pdj.teclado.LEFT == true && habilitar) {
	    		if(pos <= 0) {
	    			pos = unidades.size()-1;
	    		}
	    		else {
	    			pos--;
	    		}
	    		habilitar = false;
	    		pdj.ReproducirSE(1);
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
	
	public int elegirAccion() {
		if(pdj.teclado.RIGHT == true && habilitar) {
    		if(numeroDeInstruccion >= acciones.length-1) {
    			numeroDeInstruccion = 0;
    		}
    		else {
    			numeroDeInstruccion++;
    		}
    		habilitar = false;
    		pdj.ReproducirSE(0);
    	}
    	if(pdj.teclado.LEFT == true && habilitar) {
    		if(numeroDeInstruccion <= 0) {
    			numeroDeInstruccion = acciones.length-1;
    		}
    		else {
    			numeroDeInstruccion--;
    		}
    		habilitar = false;
    		pdj.ReproducirSE(0);
    	}
    	if(!pdj.teclado.RIGHT && !pdj.teclado.LEFT && !pdj.teclado.ENTER) {
    		habilitar = true;
    	}
    	if(pdj.teclado.ENTER == true && habilitar) {
    		habilitar = false;
    		pdj.ReproducirSE(1);
    		return instruccionElegida = numeroDeInstruccion;
    	}
    return -1;
	}
	
	//METODOS DE MENU/////////////////////////////////////////////////////////
	public void menuDeOpciones(String[] opciones, int posX, int posY, int anchoVentana) {
		dibujarVentana(posX, posY-24, anchoVentana, pdj.tamañoDeBaldosa*(opciones.length-1)+24);
		//OPCIONES Y RECUADRO DE SELECCION
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 24f));
		for (int i = 0; i < opciones.length; i++) {
			g2.setColor(Color.white);
			g2.drawString(opciones[i], posX+pdj.tamañoDeBaldosa/4, posY+8);
			if(numeroDeInstruccion == i) {
				if(instruccionElegida == -1) {
					g2.setColor(Color.WHITE);
					g2.fillRoundRect(posX+5, posY-16, anchoVentana-10 , 32, 5, 5);
					g2.setColor(Color.BLACK);
					g2.drawString(opciones[i], posX+pdj.tamañoDeBaldosa/4, posY+8);
				}
				else {
					g2.setColor(Color.white);
					g2.drawRoundRect(posX+5, posY-16, anchoVentana-10 , 32, 5, 5);
				}
			}
			posY += (pdj.tamañoDeBaldosa/2)+(pdj.tamañoDeBaldosa/4);
		}
	}
		
	//METODOS GRAFICOS////////////////////////////////////////////////////////
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
	
	public void dibujarEstadisticasUnidad(Unidad unidad) {
	    int ancho = pdj.tamañoDeBaldosa * 3;
	    int alto = pdj.tamañoDeBaldosa * 4;
	    int posX = pdj.anchoDePantalla - ancho - 2;
	    int posY = pdj.altoDePantalla - alto - 2;
	    int ajusteY = -24;
	    boolean dobleLinea = false;

	    g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20f));
	    String nombreCompleto = unidad.getNombre();
	    FontMetrics metrics = g2.getFontMetrics();
	    int maxAnchoNombre = ancho - 12;

	    String nombre = nombreCompleto;
	    String apellido = "";

	    if (metrics.stringWidth(nombreCompleto) > maxAnchoNombre) {
	    	dobleLinea = true;
	        int espacioIndex = nombreCompleto.lastIndexOf(' ');
	        if (espacioIndex > 0) {
	            nombre = nombreCompleto.substring(0, espacioIndex).trim();
	            apellido = nombreCompleto.substring(espacioIndex + 1).trim();

	            if (metrics.stringWidth(nombre) > maxAnchoNombre) {
	                nombre = ajustarTexto(nombre, metrics, maxAnchoNombre);
	            }
	            if (metrics.stringWidth(apellido) > maxAnchoNombre) {
	                apellido = ajustarTexto(apellido, metrics, maxAnchoNombre);
	            }
	        }
	    }
	    
	    g2.setColor(Color.white);
	    if(dobleLinea) {
	    	g2.drawRoundRect(posX, posY-24, ancho, alto+24, 10, 10);
	    }
	    else {
	    	g2.drawRoundRect(posX, posY, ancho, alto, 10, 10);
	    }
	    
	    if (unidad.getGenero() == 1) {
	        g2.setColor(Color.CYAN);
	    } else {
	        g2.setColor(Color.PINK);
	    }

	    if(dobleLinea) {
	    	g2.drawString(nombre, posX + 8, posY+ ajusteY + 24);
	    }
	    else {
	    	g2.drawString(nombre, posX + 8, posY + 24);
	    }
	    if (!apellido.isEmpty()) {
	        g2.drawString(apellido, posX + 8, posY+ ajusteY + 48);
	    }
	    g2.setFont(g2.getFont().deriveFont(Font.BOLD, 18f));
	    g2.setColor(Color.white);
	    g2.drawString(unidad.getClase(), posX + 8, posY + ajusteY + 72);
	    g2.drawString("HP: "+unidad.getHP()+"/"+unidad.getHPMax(), posX + 8, posY + ajusteY + 104);
	    if(unidad.getSPMax() == 0) {
	    	g2.drawString("SP: -/- ", posX + 8, posY + ajusteY + 128);
	    }
	    else {
	    	g2.drawString("SP: "+unidad.getSP()+"/"+unidad.getSPMax(), posX + 8, posY + ajusteY + 128);
	    }
	    g2.drawString("ATQ: "+unidad.getAtq(), posX + 8, posY + ajusteY + 152); 
	    g2.drawString("DEF: "+unidad.getDef(), posX + 8, posY + ajusteY + 176);
	    
	    g2.setColor(Color.yellow);
	    if(unidad.getAtqMod() != 0) {
	    	g2.drawString("+"+unidad.getAtqMod(), posX + 60, posY + ajusteY + 152);
	    }
	    if(unidad.getDefMod() != 0) {
	    	g2.drawString("+"+unidad.getDefMod(), posX + 60, posY + ajusteY + 176);
	    }  
	    if((unidad.getPCRT()+unidad.getPcrtMod()) > 0) {
	    	if(unidad.getPcrtMod() != 0) {
	    		double d = unidad.getPCRT()+unidad.getPcrtMod();
	    		g2.drawString("P.CRT: "+(d)*100+"%", posX + 8, posY + ajusteY + 200);
	    	}
	    	else {
	    		g2.setColor(Color.white);
	    		g2.drawString("P.CRT: "+(unidad.getPCRT())*100+"%", posX + 8, posY + ajusteY + 200);
	    	}
	    }
	    else {
	    	g2.setColor(Color.white);
	    	g2.drawString("P.CRT: ---", posX + 8, posY + ajusteY + 200);
	    }
	    
	}

	private String ajustarTexto(String texto, FontMetrics metrics, int maxAncho) {
	    while (metrics.stringWidth(texto + "...") > maxAncho && texto.length() > 0) {
	        texto = texto.substring(0, texto.length() - 1);
	    }
	    return texto + "...";
	}
	
	public void dibujarCartelDeTurno() {
		g2.setColor(Color.white);
		g2.drawRect(pdj.tamañoDeBaldosa*4, pdj.tamañoDeBaldosa*5, pdj.tamañoDeBaldosa*8, pdj.tamañoDeBaldosa);
		if(turnoJugador) {
			g2.setColor(Color.BLUE);
			g2.fillRect(pdj.tamañoDeBaldosa*4, pdj.tamañoDeBaldosa*5, pdj.tamañoDeBaldosa*8, pdj.tamañoDeBaldosa);
			g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30f));
			g2.setColor(Color.white);
			g2.drawString("Turno: "+unidades.get(id).getNombre(), pdj.tamañoDeBaldosa*5, pdj.tamañoDeBaldosa*5+32);
		}
		else {
			g2.setColor(Color.RED);
			g2.fillRect(pdj.tamañoDeBaldosa*4, pdj.tamañoDeBaldosa*5, pdj.tamañoDeBaldosa*8, pdj.tamañoDeBaldosa);
			g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30f));
			g2.setColor(Color.white);
			g2.drawString("Turno: "+unidades.get(id).getNombre(), pdj.tamañoDeBaldosa*5, pdj.tamañoDeBaldosa*5+32);
		}
	}
	
	public void dibujarResaltadorDeUnidad() {
		g2.setColor(Color.orange);
		g2.fillRect(resaltador.x, resaltador.y-20, pdj.tamañoDeBaldosa*2, 20);
		g2.fillRect(resaltador.x, resaltador.y-96, 20, pdj.tamañoDeBaldosa*2);
		g2.fillRect(resaltador.x+pdj.tamañoDeBaldosa*2-20, resaltador.y-96, 20, pdj.tamañoDeBaldosa*2);
	}
	
	public void dibujarTriangulo(int posX, int posY) {
        int[] xPoints = {50+posX, 25+posX, 50+posX};
        int[] yPoints = {40+posY, 50+posY, 60+posY};

        g2.setColor(Color.BLACK);
        g2.fillPolygon(xPoints, yPoints, 3);
        g2.setColor(Color.WHITE);
        g2.drawPolygon(xPoints, yPoints, 3);
        g2.setColor(Color.BLACK);
        g2.fillRect(posX+48, posY+41, 3, 18);
        g2.setColor(Color.WHITE);
    }
	//VERIFICACION Y ACTUALIZACION///////////////////////////////////////////
	public boolean verificar(int n) {
		if(n == 0 || (10 <= n && n <= 13) || (34 <= n && n <= 37) || n == 32) {
			return true;
		}
		return false;
	}
	
	public void actualizarSelector(Unidad unidad, Rectangle resaltador) {
		if(unidad != null) {
			resaltador.setLocation(unidad.getPosX(), unidad.getPosY()+pdj.tamañoDeBaldosa*2);
		}
	}
	
	public void nombrarUnidades(ArrayList<Unidad> unidades) {
		for(Unidad unidad: unidades) {
			unidad.setNombre(pdj.gdn.generarNombreCompleto(unidad.getGenero()));
		}
	}
	
}
