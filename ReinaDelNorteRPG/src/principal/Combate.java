package principal;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import unidades.PayadorPicante;
import unidades.AlumnoModelo;
import unidades.ShaolinEscolar;
import unidades.CebadorDeMate;
import unidades.DragonPirotecnico;
import unidades.GauchoModerno;
import unidades.HeroeFederal;
import unidades.MedicoTradicionalista;
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
	private int idTurno = 0;
	private int idUnidad = 0;
	private int contadorDeTurnos = 0;
	private int posEnLista = 0;
	private int timer = 100;
	private boolean nombrarUnidades = true;
	private boolean posicionarUnidades = true;
	private boolean ordenarUnidades = true;
	private boolean seleccionarHabilidades = false;
	//CONTROL DE TURNO Y BOTONES/////////////////////////////////////////////
	private boolean habilitarBoton = true;
	private boolean turnoJugador = true; 
	//CONTROL DE INSTRUCCIONES///////////////////////////////////////////////
	private int numeroDeInstruccion = 0;
	private int instruccionElegida = -1;
	private int numeroDeHabilidad = 0;
	private int habilidadElegida = -1;
	private Rectangle selector;
	private Rectangle resaltador;
	private BufferedImage indicador = configurarImagen("/efectos/selector", 4);
	private BufferedImage lockOn = configurarImagen("/efectos/lock-on", 4);
	//OBJETOS ESPECIALES/////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////
	
	public Combate(PanelDeJuego pdj) {
		this.pdj = pdj;
		crearTablero();
		selector = new Rectangle(0, 0, pdj.tamañoDeBaldosa*2, pdj.tamañoDeBaldosa/8);
		resaltador = new Rectangle(0, 0, 0, 0);
		acciones[0] = "ATACAR";
		acciones[1] = "HABILIDAD";
		acciones[2] = "USAR OBJETO";
		//unidades.put(0, new CebadorDeMate(zonas.get(0), true, pdj));
		//unidades.put(1, new HeroeFederal(zonas.get(0), true, pdj));
		//unidades.put(2, new PayadorPicante(zonas.get(0), true, pdj));
		//unidades.put(3, new DragonPirotecnico(zonas.get(0), false, pdj));
		//unidades.put(4, new GauchoModerno(zonas.get(0), true, pdj));
		//unidades.put(5, new GauchoModerno(zonas.get(0), false, pdj));
		//unidades.put(6, new PayadorPicante(zonas.get(0), true, pdj));	
		//unidades.put(7, new CebadorDeMate(zonas.get(0), false, pdj));	
		//unidades.put(8, new RespetableCiudadano(zonas.get(0), false, pdj));
		//unidades.put(6, new RespetableCiudadano(zonas.get(0), false, pdj));
		unidades.put(2, new AlumnoModelo(zonas.get(0), true, pdj));
		unidades.put(5, new AlumnoModelo(zonas.get(0), true, pdj));
		unidades.put(4, new AlumnoModelo(zonas.get(0), true, pdj));
		unidades.put(3, new AlumnoModelo(zonas.get(0), true, pdj));
		//unidades.put(1, new DragonPirotecnico(zonas.get(0), true, pdj));
		//unidades.put(2, new MedicoTradicionalista(zonas.get(0), false, pdj));
	}
	//METODOS PRINCIPALES///////////////////////////////////////////////////////
	public void actualizar() {
		//SE NOMBRAN A LAS UNIDADES/////////////////////////////////////////////
		if(nombrarUnidades) {
			nombrarUnidades(unidades);
			nombrarUnidades = false;
			contadorDeTurnos = unidades.size();
			for(int clave : unidades.keySet()) {
				velocidades.put(clave, (unidades.get(clave).getVel()+unidades.get(clave).getVelMod()));
			}
		}
		if(ordenarUnidades) {
			ordenarUnidades = false;
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
				g2.drawImage(lockOn, selector.x, selector.y, null);
			}
		}
		if(unidadEnTurno != null && seleccionarHabilidades) {
			generarMenu(unidadEnTurno.getListaDeHabilidades(), pdj.tamañoDeBaldosa*6+10 , pdj.altoDePantalla - pdj.tamañoDeBaldosa*(acciones.length-1)+36, 160, numeroDeHabilidad, habilidadElegida);
		}
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
	    if(contadorDeTurnos == unidades.size()) {
	    	for(int clave : unidades.keySet()) {
	    		velocidades.put(clave, (unidades.get(clave).getVel()+unidades.get(clave).getVelMod()));
			}
	    	turnos = obtenerClavesOrdenadasPorVelocidad(velocidades);
	    	if(!enemigos.isEmpty()) {
	    		for(Unidad unidad : enemigos) {
		    		unidad.setEstaActivo(true);
		    		if(unidad.getEstaKO()) {
		    			unidad.setEstaKO(false);
		    		}
		    	}
	    	}
	    	if(!aliados.isEmpty()) {
	    		for(Unidad unidad : aliados) {
		    		unidad.setEstaActivo(true);
		    		if(unidad.getEstaKO()) {
		    			unidad.setEstaKO(false);
		    		}
		    	}
	    	}
	    	contadorDeTurnos = 0;
	    }
	    //SE DEFINE EL ID DE LA UNIDAD PARA EL SIGUIENTE TURNO////////////////////////////////
	    idTurno = turnos.charAt(idUnidad) - '0';
	    if(unidades.get(idTurno).isAlive() && unidades.get(idTurno).isEstaActivo()) {
	    	actualizarSelector(unidades.get(idTurno), resaltador);
	    	//COMPRUEBA QUE LA UNIDAD SEA ALIADA//////////////////////////////////////////////
	    	if(unidades.get(idTurno).isAliado()) {
	    		retrocederAccion();
	    		unidadSeleccionada = unidades.get(idTurno);
		    	turnoJugador = true;
		    	if(instruccionElegida == -1) {
		    		instruccionElegida = elegirAccion();
		    	}
		    	//ATAQUE JUGADOR//////////////////////////////////////////////////////////////
		    	else if(instruccionElegida == 0) {
		    		Unidad unidadSeleccionada = elegirUnidad(enemigos);
			    	if(unidadSeleccionada != null) {
			    		unidades.get(idTurno).realizarAtaque(unidadSeleccionada);
			    		unidades.get(idTurno).setEstaActivo(false);
			    		unidades.get(idTurno).pasivaDeClase(aliados, enemigos);
			    		if(unidadSeleccionada.getHP() <= 0) {
			    			posEnLista = 0;
			    		}
			    		if(idUnidad >= unidades.size()-1) {
			    			idUnidad = 0;
			    		}
			    		else {
			    			idUnidad++;
			    		}
			    		instruccionElegida = -1;
			    		posEnLista = 0;
			    		contadorDeTurnos++;
			    	}
		    	}
		    	//HABILIDAD JUGADOR//////////////////////////////////////////////////////////
		    	else if(instruccionElegida == 1) {
		    		Unidad unidadSeleccionada = null;
		    		ArrayList<Unidad> unidadesObjetivo = null;
		    		//ASIGNAMOS LA UNIDAD EN TURNO////////////////////////////////////////////
		    		unidadEnTurno = unidades.get(idTurno);
		    		if(unidadEnTurno != null) {
		    			//SI HAY UNIDAD EN TURNO, SE ELIGE LA HABILIDAD//////////////////////
		    			if(unidadEnTurno.getHabilidadElegida() == -1) {
		    				seleccionarHabilidades = true;
		    				unidades.get(idTurno).setHabilidadElegida(elegirHabilidad(unidades.get(idTurno).getListaDeHabilidades()));
		    				unidades.get(idTurno).configurarTipoDeaccion();
		    			}
		    			else {
		    				if(unidadEnTurno.isObjetivoUnico()) {
		    					if(unidadEnTurno.cumpleReqDeHab1()) {
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
		    					if(unidadEnTurno.cumpleReqDeHab2()) {
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
			    			unidades.get(idTurno).usarHabilidad(unidadSeleccionada, unidadesObjetivo);
			    			unidades.get(idTurno).setEstaActivo(false);
		    				unidades.get(idTurno).setHabilidadElegida(-1);
			    			if(idUnidad >= unidades.size()-1) {
				    			idUnidad = 0;
				    		}
				    		else {
				    			idUnidad++;
				    		}
			    			//SE RESETEAN LAS VARIABLES DE USO////////////////////////////////
				    		instruccionElegida = -1;
				    		habilidadElegida = -1;
				    		numeroDeInstruccion = 0;
				    		numeroDeHabilidad = 0;
				    		posEnLista = 0;
				    		unidadEnTurno.setHabilidadElegida(-1);
				    		seleccionarHabilidades = false;
				    		contadorDeTurnos++;
			    		}
		    		}
		    	}
		    }
	    	//ACCIONES ENEMIGAS/////////////////////////////////////////////////////////
		    else {
		    	unidadSeleccionada = null;
		    	turnoJugador = false;
		    	if(timer == 0) {
		    		numeroDeInstruccion = 0;
		    		unidades.get(idTurno).realizarAccion(aliados, enemigos);
			    	if(idUnidad >= unidades.size()-1) {
		    			idUnidad = 0;
		    		}
		    		else {
		    			idUnidad++;
		    		}
			    	timer = 100;
			    	contadorDeTurnos++;
			    	unidades.get(idTurno).setEstaActivo(false);
		    	}
		    	posEnLista = 0;
		    	timer--;
		    }
	    }
	    else {
	    	contadorDeTurnos++;
	    	if(idUnidad >= unidades.size()-1) {
	    		idUnidad = 0;
	    	}
	    	else {
		    	idUnidad++;
	    	}
	    }
	}
	
	//METODOS DE ELECCION////////////////////////////////////////////////////
	private Unidad elegirUnidad(ArrayList<Unidad> unidades) {
	    if (!unidades.isEmpty()) {
	    	actualizarSelector(unidades.get(posEnLista), selector);
	    	if(pdj.teclado.RIGHT == true && habilitarBoton) {
	    		if(posEnLista >= unidades.size()-1) {
	    			posEnLista = 0;
	    		}
	    		else {
	    			posEnLista++;
	    		}
	    		habilitarBoton = false;
	    		pdj.ReproducirSE(1);
	    	}
	    	if(pdj.teclado.LEFT == true && habilitarBoton) {
	    		if(posEnLista <= 0) {
	    			posEnLista = unidades.size()-1;
	    		}
	    		else {
	    			posEnLista--;
	    		}
	    		habilitarBoton = false;
	    		pdj.ReproducirSE(1);
	    	}
	    	if(!pdj.teclado.RIGHT && !pdj.teclado.LEFT && !pdj.teclado.Z) {
	    		habilitarBoton = true;
	    	}
	    	if(pdj.teclado.Z == true && habilitarBoton) {
	    		habilitarBoton = false;
	    		return unidades.get(posEnLista);
	    	}
	    }
	    return null;
	}
	
	public int elegirAccion() {
		if(pdj.teclado.DOWN == true && habilitarBoton) {
    		if(numeroDeInstruccion >= acciones.length-1) {
    			numeroDeInstruccion = 0;
    		}
    		else {
    			numeroDeInstruccion++;
    		}
    		habilitarBoton = false;
    		pdj.ReproducirSE(0);
    	}
    	if(pdj.teclado.UP == true && habilitarBoton) {
    		if(numeroDeInstruccion <= 0) {
    			numeroDeInstruccion = acciones.length-1;
    		}
    		else {
    			numeroDeInstruccion--;
    		}
    		habilitarBoton = false;
    		pdj.ReproducirSE(0);
    	}
    	if(!pdj.teclado.UP && !pdj.teclado.DOWN && !pdj.teclado.X && !pdj.teclado.Z) {
    		habilitarBoton = true;
    	}
    	if(pdj.teclado.Z == true && habilitarBoton) {
    		habilitarBoton = false;
    		pdj.ReproducirSE(1);
    		return instruccionElegida = numeroDeInstruccion;
    	}
    return -1;
	}
	
	public int elegirHabilidad(String[] habilidades) {
		if(pdj.teclado.DOWN == true && habilitarBoton) {
    		if(numeroDeHabilidad >= habilidades.length-1) {
    			numeroDeHabilidad = 0;
    		}
    		else {
    			numeroDeHabilidad++;
    		}
    		habilitarBoton = false;
    		pdj.ReproducirSE(0);
    	}
    	if(pdj.teclado.UP == true && habilitarBoton) {
    		if(numeroDeHabilidad <= 0) {
    			numeroDeHabilidad = habilidades.length-1;
    		}
    		else {
    			numeroDeHabilidad--;
    		}
    		habilitarBoton = false;
    		pdj.ReproducirSE(0);
    	}
    	if(!pdj.teclado.UP && !pdj.teclado.DOWN && !pdj.teclado.X && !pdj.teclado.Z) {
    		habilitarBoton = true;
    	}
    	if(pdj.teclado.Z == true && habilitarBoton) {
    		habilitarBoton = false;
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
	    	if(unidad.getAtqMod() < 0) {
	    		g2.setColor(Color.RED);
	    		g2.drawString(""+unidad.getAtqMod(), posX + 60, posY + ajusteY + 152);
		    }
		    else {
		    	g2.drawString("+"+unidad.getAtqMod(), posX + 60, posY + ajusteY + 152);
		    }
	    }
	    if(unidad.getDefMod() != 0) {
	    	if(unidad.getDefMod() < 0){
	    		g2.setColor(Color.RED);
	    		g2.drawString(""+unidad.getDefMod(), posX + 60, posY + ajusteY + 176);
	    	}
	    	else {
	    		g2.drawString("+"+unidad.getDefMod(), posX + 60, posY + ajusteY + 176);
	    	}
	    }  
	    if((unidad.getPCRT()+unidad.getPcrtMod()) > 0) {
	    	if(unidad.getPcrtMod() != 0) {
	    		g2.setColor(Color.yellow);
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
			g2.drawString("Turno: "+unidades.get(idUnidad).getNombre(), pdj.tamañoDeBaldosa*5, pdj.tamañoDeBaldosa*5+32);
		}
		else {
			g2.setColor(Color.RED);
			g2.fillRect(pdj.tamañoDeBaldosa*4, pdj.tamañoDeBaldosa*5, pdj.tamañoDeBaldosa*8, pdj.tamañoDeBaldosa);
			g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30f));
			g2.setColor(Color.white);
			g2.drawString("Turno: "+unidades.get(idUnidad).getNombre(), pdj.tamañoDeBaldosa*5, pdj.tamañoDeBaldosa*5+32);
		}
	}
	
	public void dibujarResaltadorDeUnidad() {
		g2.drawImage(indicador, resaltador.x, resaltador.y, null);
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
	
	public BufferedImage configurarImagen(String rutaImagen, int escala) {
	    Utilidades uTool = new Utilidades();
	    BufferedImage imagen = null;
	    try {
	        imagen = ImageIO.read(getClass().getResourceAsStream(rutaImagen + ".png"));
	        imagen = uTool.escalarImagen(imagen, imagen.getWidth()/2*escala, imagen.getHeight()/2*escala);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return imagen;

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
			resaltador.setLocation(unidad.getPosX(), unidad.getPosY());
		}
	}
	
	public void nombrarUnidades(HashMap<Integer, Unidad> unidades) {
		for (Unidad unidad : unidades.values()) {
			unidad.setNombre(pdj.gdn.generarNombreCompleto(unidad.getGenero()));
		}
	}
	
	public void retrocederAccion() {
		if(pdj.teclado.X == true && habilitarBoton) {
    		habilitarBoton = false;
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
