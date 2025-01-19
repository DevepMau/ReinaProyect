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
import java.util.stream.Collectors;

import unidades.Elite;
import unidades.PayadorTartamudo;
import unidades.CebadorDeMate;
import unidades.GauchoModerno;
import unidades.Unidad;

public class Combate {
	//RECURSOS Y ESTRUCTURAS/////////////////////////////////////////////////
	PanelDeJuego pdj;
	Graphics2D g2;
	private HashMap<Integer, Zona> zonas = new HashMap<>();
	private HashMap<Integer, Unidad> unidades = new HashMap<>();
	private HashMap<Integer, Integer> velocidades = new HashMap<>();
	private Unidad unidadSeleccionada = null;
	private Unidad unidadEnTurno = null;
	//CONFIGURACION GENERAL//////////////////////////////////////////////////
	private String[] acciones = new String[3];
	private String turnos = "";
	private int turnoId = 0;
	private int id = 0;
	private int pos = 0;
	private int timer = 100;
	private boolean unidadesSinNombre = true;
	private boolean posicionarUnidades = true;
	private boolean ordenarListaDeUnidades = true;
	//CONTROL DE TURNO Y BOTONES/////////////////////////////////////////////
	private boolean habilitar = true;
	private boolean turnoJugador = true;
	private boolean seleccionarHabilidades = false; 
	//CONTROL DE INSTRUCCIONES///////////////////////////////////////////////
	private int numeroDeInstruccion = 0;
	private int instruccionElegida = -1;
	private int numeroDeHabilidad = 0;
	private int habilidadElegida = -1;
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
		unidades.put(0, new PayadorTartamudo(zonas.get(0), true, pdj));
		unidades.put(1, new PayadorTartamudo(zonas.get(0), false, pdj));
		unidades.put(2, new GauchoModerno(zonas.get(0), true, pdj));
		//unidades.put(3, new GauchoModerno(zonas.get(0), false, pdj));
		unidades.put(4, new CebadorDeMate(zonas.get(0), true, pdj));
		unidades.put(5, new PayadorTartamudo(zonas.get(0), false, pdj));
		//unidades.put(6, new CebadorDeMate(zonas.get(0), true, pdj));
		//unidades.put(7, new CebadorDeMate(zonas.get(0), false, pdj));

		
	}
	//METODOS PRINCIPALES///////////////////////////////////////////////////////
	public void actualizar() {
		//SE NOMBRAN A LAS UNIDADES/////////////////////////////////////////////
		if(unidadesSinNombre) {
			nombrarUnidades(unidades);
			unidadesSinNombre = false;
			for(int clave : unidades.keySet()) {
				velocidades.put(clave, unidades.get(clave).getVel());
			}
		}
		if(ordenarListaDeUnidades) {
			turnos = obtenerClavesOrdenadasPorVelocidad(velocidades);
	        System.out.println(turnos);
			ordenarListaDeUnidades = false;
		}
		if(posicionarUnidades) {
			int idAli = 5;
			int idEne = 1;
			for (Unidad unidad : unidades.values()) {
			    if(unidad.isAliado() && idAli <= 8) {
			    	unidad.posicionar(zonas.get(idAli));
			    	idAli++;
			    }
			    else if(!unidad.isAliado() && idEne <= 4) {
			    	unidad.posicionar(zonas.get(idEne));
			    	idEne++;
			    }
			}
			posicionarUnidades = false;
		}
		for (Unidad unidad : unidades.values()) {
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
		
		for (Unidad unidad : unidades.values()) {
		    unidad.dibujar(g2);
		}
		if(turnoJugador) {
			generarMenu(acciones, pdj.tamañoDeBaldosa*3 , pdj.altoDePantalla - pdj.tamañoDeBaldosa*(acciones.length-1), 160, numeroDeInstruccion, instruccionElegida);
			if(instruccionElegida == 0 || instruccionElegida == 1) {
				g2.setColor(Color.YELLOW);
				g2.fillRect(selector.x, selector.y, selector.width, selector.height);
			}
		}
		
		if(unidadEnTurno != null && seleccionarHabilidades) {
			generarMenu(unidadEnTurno.getHabilidades(), pdj.tamañoDeBaldosa*6+10 , pdj.altoDePantalla - pdj.tamañoDeBaldosa*(acciones.length-1)+36, 160, numeroDeHabilidad, habilidadElegida);
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
	    
	    for (Unidad unidad : unidades.values()) {
	        if (unidad.getHP() > 0) {
	            if (!unidad.isAliado()) {
	                enemigos.add(unidad);
	            } else {
	                aliados.add(unidad);
	            }
	        }
	    }
	    //SE DEFINE EL ID DE LA UNIDAD PARA EL SIGUIENTE TURNO////////////////////////////////
	    turnoId = turnos.charAt(id) - '0';
	    //SE COMPRUEBA QUE LA UNIDAD ESTE VIVA////////////////////////////////////////////////
	    if(unidades.get(turnoId).isAlive()) {
	    	//SE ACTUALIZA EL SELECTOR////////////////////////////////////////////////////////
	    	actualizarSelector(unidades.get(turnoId), resaltador);
	    	//COMPRUEBA QUE LA UNIDAD SEA ALIADA//////////////////////////////////////////////
	    	if(unidades.get(turnoId).isAliado()) {
	    		retrocederAccion();
	    		unidadSeleccionada = unidades.get(turnoId);
		    	turnoJugador = true;
		    	if(instruccionElegida == -1) {
		    		instruccionElegida = elegirAccion();
		    	}
		    	//ATAQUE JUGADOR//////////////////////////////////////////////////////////////
		    	else if(instruccionElegida == 0) {
		    		Unidad unidadSeleccionada = elegirUnidad(enemigos);
			    	if(unidadSeleccionada != null) {
			    		unidades.get(turnoId).realizarAtaque(unidadSeleccionada);
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
			    		pos = 0;
			    	}
		    	}
		    	//HABILIDAD JUGADOR//////////////////////////////////////////////////////////
		    	else if(instruccionElegida == 1) {
		    		Unidad unidadSeleccionada = null;
		    		ArrayList<Unidad> unidadesObjetivo = null;
		    		//ASIGNAMOS LA UNIDAD EN TURNO////////////////////////////////////////////
		    		unidadEnTurno = unidades.get(turnoId);
		    		if(unidadEnTurno != null) {
		    			//SI HAY UNIDAD EN TURNO, SE ELIGE LA HABILIDAD//////////////////////
		    			if(unidadEnTurno.getHabilidadElegida() == -1) {
		    				seleccionarHabilidades = true;
		    				unidades.get(turnoId).setHabilidadElegida(elegirHabilidad(unidades.get(turnoId).getHabilidades()));
		    				unidades.get(turnoId).establecerTipoDeaccion();
		    			}
		    			else {
		    				if(unidadEnTurno.isSingleTarget()) {
		    					if(unidadEnTurno.cumpleLosRequisitos()) {
			    					//SI HAY ELEGIDA UNA HABILIDAD, SE DEFINE SI ES OFENSIVA O DE APOYO/
				    				if(unidadEnTurno.getAccion() == "ATACAR") {
				    					unidadSeleccionada = elegirUnidad(enemigos);
				    				}
				    				else {
				    					unidadSeleccionada = elegirUnidad(aliados);
				    				}
			    				}
			    				else {
			    					seleccionarHabilidades = false;
			    					unidadEnTurno.setHabilidadElegida(-1);
			    					habilidadElegida = -1;
			    					instruccionElegida = -1;
			    				}
		    				}
		    				else {
		    					if(unidadEnTurno.cumpleLosReqHab2()) {
			    					//SI HAY ELEGIDA UNA HABILIDAD, SE DEFINE SI ES OFENSIVA O DE APOYO/
				    				if(unidadEnTurno.getAccion() == "ATACAR") {
				    					unidadesObjetivo = enemigos;
				    				}
				    				else {
				    					unidadesObjetivo = aliados;
				    				}
			    				}
			    				else {
			    					seleccionarHabilidades = false;
			    					unidadEnTurno.setHabilidadElegida(-1);
			    					habilidadElegida = -1;
			    					instruccionElegida = -1;
			    				}
		    				}		
		    			}
		    			//SI SE ELIGIO UN OBJETIVO, SE USA LA HABILIDAD///////////////////////
		    			if(unidadSeleccionada != null || unidadesObjetivo != null) {
			    			unidades.get(turnoId).usarHabilidad(unidadSeleccionada, unidadesObjetivo);
		    				unidades.get(turnoId).setHabilidadElegida(-1);
			    			if(id >= unidades.size()-1) {
				    			id = 0;
				    		}
				    		else {
				    			id++;
				    		}
			    			//SE RESETEAN LAS VARIABLES DE USO////////////////////////////////
				    		instruccionElegida = -1;
				    		habilidadElegida = -1;
				    		numeroDeInstruccion = 0;
				    		numeroDeHabilidad = 0;
				    		pos = 0;
				    		unidadEnTurno.setHabilidadElegida(-1);
				    		seleccionarHabilidades = false;
				    		//unidadEnTurno = null;
			    		}
		    		}
		    	}
		    }
		    else {
		    	unidadSeleccionada = null;
		    	turnoJugador = false;
		    	if(timer == 0) {
		    		numeroDeInstruccion = 0;
		    		unidades.get(turnoId).realizarAccion(aliados, enemigos);
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
		if(pdj.teclado.DOWN == true && habilitar) {
    		if(numeroDeInstruccion >= acciones.length-1) {
    			numeroDeInstruccion = 0;
    		}
    		else {
    			numeroDeInstruccion++;
    		}
    		habilitar = false;
    		pdj.ReproducirSE(0);
    	}
    	if(pdj.teclado.UP == true && habilitar) {
    		if(numeroDeInstruccion <= 0) {
    			numeroDeInstruccion = acciones.length-1;
    		}
    		else {
    			numeroDeInstruccion--;
    		}
    		habilitar = false;
    		pdj.ReproducirSE(0);
    	}
    	if(!pdj.teclado.UP && !pdj.teclado.DOWN && !pdj.teclado.ESCAPE && !pdj.teclado.ENTER) {
    		habilitar = true;
    	}
    	if(pdj.teclado.ENTER == true && habilitar) {
    		habilitar = false;
    		pdj.ReproducirSE(1);
    		return instruccionElegida = numeroDeInstruccion;
    	}
    return -1;
	}
	
	public int elegirHabilidad(String[] habilidades) {
		if(pdj.teclado.DOWN == true && habilitar) {
    		if(numeroDeHabilidad >= habilidades.length-1) {
    			numeroDeHabilidad = 0;
    		}
    		else {
    			numeroDeHabilidad++;
    		}
    		habilitar = false;
    		pdj.ReproducirSE(0);
    	}
    	if(pdj.teclado.UP == true && habilitar) {
    		if(numeroDeHabilidad <= 0) {
    			numeroDeHabilidad = habilidades.length-1;
    		}
    		else {
    			numeroDeHabilidad--;
    		}
    		habilitar = false;
    		pdj.ReproducirSE(0);
    	}
    	if(!pdj.teclado.UP && !pdj.teclado.DOWN && !pdj.teclado.ESCAPE && !pdj.teclado.ENTER) {
    		habilitar = true;
    	}
    	if(pdj.teclado.ENTER == true && habilitar) {
    		habilitar = false;
    		pdj.ReproducirSE(1);
    		return habilidadElegida = numeroDeHabilidad;
    	}
    return -1;
	}
	//METODOS DE MENU/////////////////////////////////////////////////////////	
	public void generarMenu(String[] opciones, int posX, int posY, int anchoVentana, int variableDeSelecctor, int variableElegida) {
		int alto = pdj.tamañoDeBaldosa*(opciones.length);
		int modX = 5;
		int modW = -10;
		int modY = 0, modH = 0;
		
		if(alto == 144) {
			modY = -24;
			modH = -24;
		}
		else if(alto == 96) {
			modY = -24;
			modH = -12;
		}
		else {
			modY = -24;
			modH = 0;
		}
		dibujarVentana(posX+modX, posY+modY, anchoVentana+modW, alto+modH);
		//OPCIONES Y RECUADRO DE SELECCION
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 24f));
		for (int i = 0; i < opciones.length; i++) {
			g2.setColor(Color.white);
			g2.drawString(opciones[i], posX+pdj.tamañoDeBaldosa/4, posY+8);
			if(variableDeSelecctor == i) {
				if(variableElegida == -1) {
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
		g2.fillRoundRect(x+5, y, width-10, height, 10, 10);
		
		c = new Color(255, 255, 255);
		g2.setColor(c);
		g2.drawRoundRect(x, y, width, height, 10, 10);
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
	
	public void nombrarUnidades(HashMap<Integer, Unidad> unidades) {
		for (Unidad unidad : unidades.values()) {
			unidad.setNombre(pdj.gdn.generarNombreCompleto(unidad.getGenero()));
		}
	}
	
	public void retrocederAccion() {
		if(pdj.teclado.ESCAPE == true && habilitar) {
    		habilitar = false;
    		pdj.ReproducirSE(0);
    		instruccionElegida = -1;
    		habilidadElegida = -1;
    		if(unidadEnTurno != null) {
    			unidadEnTurno.setHabilidadElegida(-1);
    		}
    		unidadEnTurno = null;
    	}
	}
	
	public String obtenerClavesOrdenadasPorVelocidad(HashMap<Integer, Integer> velocidades) {
        return velocidades.entrySet()
                .stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .map(entry -> entry.getKey().toString())
                .collect(Collectors.joining());
    }
	
}
