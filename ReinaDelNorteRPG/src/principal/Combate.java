package principal;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.swing.Timer;

import unidades.PayadorPicante;
import unidades.PuñoFurioso;
import unidades.AlumnoModelo;
import unidades.AspiranteADragon;
import unidades.ShaolinEscolar;
import unidades.CebadorDeMate;
import unidades.MaestroDelChi;
import unidades.DragonPirotecnico;
import unidades.GauchoModerno;
import unidades.Delegada;
import unidades.HeroeFederal;
import unidades.DoncellaDelCiruelo;
import unidades.Influencer;
import unidades.MedicoTradicionalista;
import unidades.NiñoCheung;
import unidades.NovataTimida;
import unidades.Unidad;

public class Combate {
	//RECURSOS Y ESTRUCTURAS/////////////////////////////////////////////////
	PanelDeJuego pdj;
	Habilidades gdh;
	Graphics2D g2;
	private HashMap<Integer, Zona> zonas = new HashMap<>();
	private HashMap<Integer, Unidad> unidades = new HashMap<>();
	private HashMap<Integer, Integer> velocidades = new HashMap<>();
	private ArrayList<Unidad> unidadesAliadas = new ArrayList<Unidad>();
	private Unidad unidadSeleccionada = null;
	private Unidad unidadEnTurno = null;
	private Unidad unidadObservada = null;
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
	//TEXTO EN PANTALLA//////////////////////////////////////////////////////
	private int offsetTexto = 0; // Desplazamiento del texto
	private int velocidadDesplazamiento = 3; // Velocidad del desplazamiento
	private Timer timerTexto;
	private boolean abrirLibreta = false;
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
		//Nietos////////////////////////////////////////////////////////
		unidades.put(0, new CebadorDeMate(zonas.get(0), true, pdj));
		unidades.put(1, new GauchoModerno(zonas.get(0), true, pdj));
		unidades.put(2, new PayadorPicante(zonas.get(0), true, pdj));
		unidades.put(3, new HeroeFederal(zonas.get(0), true, pdj));
		unidades.put(4, new CebadorDeMate(zonas.get(0), false, pdj));
		unidades.put(5, new GauchoModerno(zonas.get(0), false, pdj));
		unidades.put(6, new PayadorPicante(zonas.get(0), false, pdj));
		unidades.put(7, new HeroeFederal(zonas.get(0), false, pdj));
		//Dragon////////////////////////////////////////////////////////
		//unidades.put(0, new AlumnoModelo(zonas.get(0), true, pdj));
		//unidades.put(1, new ShaolinEscolar(zonas.get(0), true, pdj));
		//unidades.put(2, new MedicoTradicionalista(zonas.get(0), true, pdj));
		//unidades.put(3, new DragonPirotecnico(zonas.get(0), true, pdj));
		//unidades.put(4, new AlumnoModelo(zonas.get(0), false, pdj));
		//unidades.put(5, new ShaolinEscolar(zonas.get(0), false, pdj));
		//unidades.put(6, new MedicoTradicionalista(zonas.get(0), false, pdj));
		//unidades.put(7, new DragonPirotecnico(zonas.get(0), false, pdj));
		//Puño/////////////////////////////////////////////////////////
		//unidades.put(0, new NiñoCheung(zonas.get(0), true, pdj));
		//unidades.put(1, new PuñoFurioso(zonas.get(0), true, pdj));
		//unidades.put(2, new MaestroDelChi(zonas.get(0), true, pdj));
		//unidades.put(3, new AspiranteADragon(zonas.get(0), true, pdj));
		//unidades.put(4, new NiñoCheung(zonas.get(0), false, pdj));
		//unidades.put(5, new PuñoFurioso(zonas.get(0), false, pdj));
		//unidades.put(6, new MaestroDelChi(zonas.get(0), false, pdj));
		//unidades.put(7, new AspiranteADragon(zonas.get(0), false, pdj));
		//MHJ///////////////////////////////////////////////////////////
		//unidades.put(0, new NovataTimida(zonas.get(0), true, pdj));
		//unidades.put(1, new Delegada(zonas.get(0), true, pdj));
		//unidades.put(2, new Influencer(zonas.get(0), true, pdj));
		//unidades.put(3, new DoncellaDelCiruelo(zonas.get(0), true, pdj));
		//unidades.put(4, new NovataTimida(zonas.get(0), true, pdj));
		//unidades.put(5, new Delegada(zonas.get(0), true, pdj));
		//unidades.put(6, new Influencer(zonas.get(0), true, pdj));
		//unidades.put(7, new DoncellaDelCiruelo(zonas.get(0), true, pdj));
		///////////////////////////////////////////////////////////////


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
		
		dibujarResaltadorDeUnidad();
		if(unidadEnTurno != null && seleccionarHabilidades) {
			menuDeHabilidades(unidadEnTurno);
		}
		if(turnoJugador) {
			menuDeAcciones();
			if(instruccionElegida == 0 || instruccionElegida == 1) {
				g2.drawImage(lockOn, selector.x, selector.y, null);
			}
			g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20f));
			g2.setColor(Color.white);
			g2.drawString("Press Z to accept.", pdj.anchoDePantalla - pdj.tamañoDeBaldosa*4 + 24, pdj.altoDePantalla - pdj.tamañoDeBaldosa * 2 + 32);
			g2.drawString("Press X to cancel.", pdj.anchoDePantalla - pdj.tamañoDeBaldosa*4 + 24, pdj.altoDePantalla - pdj.tamañoDeBaldosa * 2 + 56);
			g2.drawString("Press C for Notepad..", pdj.anchoDePantalla - pdj.tamañoDeBaldosa*4 + 6, pdj.altoDePantalla - pdj.tamañoDeBaldosa * 2 + 80);
			g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16f));
		}
		dibujarMarcoDeJuego();
		for (Unidad unidad : unidades.values()) {
		    unidad.dibujar(g2);
		}
		if(unidadObservada != null) {
			dibujarEstadosDeUnidad(unidadObservada);
		}
		if(unidadSeleccionada != null && abrirLibreta) {
			dibujarLibretaEscolar(unidadSeleccionada);
			//dibujarTriangulo(pdj.anchoDePantalla-pdj.tamañoDeBaldosa*4+(-4), pdj.altoDePantalla-pdj.tamañoDeBaldosa*4+(-32));
		}
	}
	//METODOS PARA JUGAR//////////////////////////////////////////////////////
	public void realizarTurno() {
		if(pdj.teclado.C == true && habilitarBoton) {
    		habilitarBoton = false;
    		abrirLibreta = !abrirLibreta;
    	}
		
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
	    if(!aliados.isEmpty()) {
	    	this.unidadesAliadas.addAll(aliados);
	    }
	    if(contadorDeTurnos == unidades.size()) {
	    	for(int clave : unidades.keySet()) {
	    		velocidades.put(clave, (unidades.get(clave).getVel()+unidades.get(clave).getVelMod()));
			}
	    	turnos = obtenerClavesOrdenadasPorVelocidad(velocidades);
	    	if(!enemigos.isEmpty()) {
	    		for(Unidad unidad : enemigos) {
		    		unidad.setEstaActivo(true);
		    		if(unidad.getEstaStun()) {
		    			unidad.setEstaStun(false);
		    		}
		    	}
	    	}
	    	if(!aliados.isEmpty()) {
	    		for(Unidad unidad : aliados) {
		    		unidad.setEstaActivo(true);
		    		if(unidad.getEstaStun()) {
		    			unidad.setEstaStun(false);
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
		    		unidadObservada = null;
		    	}
		    	//ATAQUE JUGADOR//////////////////////////////////////////////////////////////
		    	else if(instruccionElegida == 0) {
		    		Unidad unidadSeleccionada = elegirUnidad(enemigos);
			    	if(unidadSeleccionada != null) {
			    		unidades.get(idTurno).realizarAtaque(unidadSeleccionada, enemigos);
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
			    		unidadObservada = null;
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
			    					unidadObservada = null;
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
			    					unidadObservada = null;
			    				}
		    				}		
		    			}
		    			//SI SE ELIGIO UN OBJETIVO, SE USA LA HABILIDAD///////////////////////
		    			if(unidadSeleccionada != null || unidadesObjetivo != null) {
			    			unidades.get(idTurno).usarHabilidad(unidadSeleccionada, unidadesObjetivo);
			    			unidades.get(idTurno).pasivaDeClase(aliados, enemigos);
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
				    		unidadObservada = null;
			    		}
		    		}
		    	}
		    }
	    	//ACCIONES ENEMIGAS/////////////////////////////////////////////////////////
		    else {
		    	//unidadSeleccionada = null;
		    	unidadSeleccionada = unidades.get(idTurno);
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
	    		offsetTexto = 0;
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
	    		offsetTexto = 0;
	    		pdj.ReproducirSE(1);
	    	}
	    	if(!pdj.teclado.RIGHT && !pdj.teclado.LEFT && !pdj.teclado.Z && !pdj.teclado.C) {
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
    	if(!pdj.teclado.UP && !pdj.teclado.DOWN && !pdj.teclado.X && !pdj.teclado.Z && !pdj.teclado.C) {
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
    	if(!pdj.teclado.UP && !pdj.teclado.DOWN && !pdj.teclado.X && !pdj.teclado.Z && !pdj.teclado.C) {
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
	public void menuDeAcciones() {
		int posX = 8;
		int posY = pdj.altoDePantalla - pdj.tamañoDeBaldosa*3 - 8;
		int ancho = pdj.tamañoDeBaldosa*4 - 8;
		int alto = pdj.tamañoDeBaldosa*3;
		int posPalabras = posY + pdj.tamañoDeBaldosa;
		int separacion = pdj.tamañoDeBaldosa - (pdj.tamañoDeBaldosa/4);
		Color ventanaExterior = new Color(128, 20, 20); 
		Color ventanaExteriorSombra = new Color(90, 0, 0);
		Color ventanaInterior = new Color(250, 200, 150);
		Color ventanaInteriorSombra = new Color(60, 0, 0);
		Color lineas = new Color(90, 0, 0); 

		g2.setColor(ventanaExteriorSombra);
		g2.fillRoundRect(0, posY + 8, ancho, alto, 5, 5);
		g2.setColor(ventanaExterior);
		g2.fillRoundRect(posX, posY, ancho, alto, 5, 5);
		g2.setColor(ventanaInterior);
		g2.fillRoundRect(posX + 16, posY + 16, ancho - 32, alto - 32, 5, 5);
		g2.setColor(ventanaExteriorSombra);
		g2.fillRoundRect(posX + 16, posY + 16, ancho - 32, alto/20, 5, 5);
		g2.fillRoundRect(posX + ancho - 24, posY + 16, ancho/20, alto - 32, 5, 5);
		g2.setColor(lineas);
		g2.drawRoundRect(posX, posY, ancho, alto, 5, 5);
		g2.drawRoundRect(posX + 16, posY + 16, ancho - 32, alto - 32, 5, 5);
		
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 24f));
		for (int i = 0; i < acciones.length; i++) {
			g2.setColor(ventanaInteriorSombra);
			g2.drawString(acciones[i], separacion, posPalabras);
			
			if(numeroDeInstruccion == i) {
				if(instruccionElegida == -1) {
					g2.setColor(ventanaInteriorSombra);
					g2.fillRoundRect(posX + 17, posPalabras - 24, ancho - 39 , 32, 5, 5);
					g2.setColor(ventanaInterior);
					g2.drawString(acciones[i], separacion, posPalabras);
				}
				else {
					g2.setColor(ventanaInteriorSombra);
					g2.drawLine(posX + 16, posPalabras - 24, posX + 16 + ancho - 40, posPalabras - 24); // Línea superior
					g2.drawLine(posX + 16, posPalabras + 8, posX + 16 + ancho - 40, posPalabras + 8);   // Línea inferior
				}	
			}
			posPalabras += separacion;
		}
	}
	
	public void menuDeHabilidades(Unidad unidad) {
		int cantHab = unidad.getListaDeHabilidades().length;
		int ajusteY = 32;
		int ajusteAlto = 24;
		if(cantHab == 2) {
			ajusteY = 20;
			ajusteAlto = 12;
		}
		int posX = pdj.tamañoDeBaldosa*4 + 8;
		int posY = pdj.altoDePantalla - pdj.tamañoDeBaldosa*(cantHab) - ajusteY;
		int ancho = pdj.tamañoDeBaldosa*4 - 8;
		int alto = pdj.tamañoDeBaldosa*(cantHab) + ajusteAlto;
		int posPalabras = posY + pdj.tamañoDeBaldosa;
		int separacion = pdj.tamañoDeBaldosa - (pdj.tamañoDeBaldosa/4);
		int altoSombra = pdj.tamañoDeBaldosa*3;
		Color ventanaExterior = new Color(128, 20, 20); 
		Color ventanaExteriorSombra = new Color(90, 0, 0);
		Color ventanaInterior = new Color(250, 200, 150);
		Color ventanaInteriorSombra = new Color(60, 0, 0);
		Color lineas = new Color(90, 0, 0); 
		//UI HABILIDADES////////////////////////////////////////////////////
		g2.setColor(ventanaExteriorSombra);
		g2.fillRoundRect(posX - 8, posY + 8, ancho, alto, 5, 5);
		g2.setColor(ventanaExterior);
		g2.fillRoundRect(posX, posY, ancho, alto, 5, 5);
		g2.setColor(ventanaInterior);
		g2.fillRoundRect(posX + 16, posY + 16, ancho - 32, alto - 32, 5, 5);
		g2.setColor(ventanaExteriorSombra);
		g2.fillRoundRect(posX + 16, posY + 16, ancho - 32, altoSombra/20, 5, 5);
		g2.fillRoundRect(posX + ancho - 24, posY + 16, ancho/20, alto - 32, 5, 5);
		g2.setColor(lineas);
		g2.drawRoundRect(posX, posY, ancho, alto, 5, 5);
		g2.drawRoundRect(posX + 16, posY + 16, ancho - 32, alto - 32, 5, 5);
			
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 24f));
		for (int i = 0; i < unidad.getListaDeHabilidades().length ; i++) {
			g2.setColor(ventanaInteriorSombra);
			FontMetrics fm = g2.getFontMetrics();
		    int anchoTexto = fm.stringWidth(unidad.getListaDeHabilidades()[i]);
		    if (anchoTexto > 120) {
		    	Shape clipAnterior = g2.getClip();
		    	g2.setClip(posX + 16, posY + 16, ancho - 32, alto - 32);
		    	g2.drawString(unidad.getListaDeHabilidades()[i], posX + separacion - offsetTexto, posPalabras);
		    	g2.setClip(clipAnterior);
		    	iniciarDesplazamientoTexto(ancho, unidad.getListaDeHabilidades()[i]);
		        
		    } else {
		        g2.drawString(unidad.getListaDeHabilidades()[i], posX + separacion, posPalabras);
		    }
			if(i == 0) {
				if(!unidad.isHabilidad1()) {
					g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16f));
					g2.setColor(ventanaExterior);
					g2.fillRoundRect(posX + ancho + 8, posPalabras - 24, 32, 32, 100, 100);
					g2.setColor(ventanaInterior);
					g2.fillRoundRect(posX + ancho + 14, posPalabras - 18, 20, 20, 100, 100);
					g2.setColor(ventanaExteriorSombra);
					g2.drawRoundRect(posX + ancho + 8, posPalabras - 24, 32, 32, 100, 100);
					if(unidad.getCdHabilidad1() + 1 >= 10) {
						g2.drawString(unidad.getCdHabilidad1() + 1 + "", posX + ancho + 16, posPalabras - 2);
					}
					else {
						g2.drawString(unidad.getCdHabilidad1() + 1 + "", posX + ancho + 20, posPalabras - 2);
					}
					g2.setFont(g2.getFont().deriveFont(Font.BOLD, 24f));
				}
			}
			if(i == 1) {
				if(!unidad.isHabilidad2()) {
					g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16f));
					g2.setColor(ventanaExterior);
					g2.fillRoundRect(posX + ancho + 8, posPalabras - 24, 32, 32, 100, 100);
					g2.setColor(ventanaInterior);
					g2.fillRoundRect(posX + ancho + 14, posPalabras - 18, 20, 20, 100, 100);
					g2.setColor(ventanaExteriorSombra);
					g2.drawRoundRect(posX + ancho + 8, posPalabras - 24, 32, 32, 100, 100);
					if(unidad.getCdHabilidad2() + 1 >= 10) {
						g2.drawString(unidad.getCdHabilidad2() + 1 + "", posX + ancho + 16, posPalabras - 2);
					}
					else {
						g2.drawString(unidad.getCdHabilidad2() + 1 + "", posX + ancho + 20, posPalabras - 2);
					}
					g2.setFont(g2.getFont().deriveFont(Font.BOLD, 24f));
				}
			}
			
			if(numeroDeHabilidad == i) {
				if(habilidadElegida == -1) {
					g2.setColor(ventanaInteriorSombra);
					g2.fillRoundRect(posX + 17, posPalabras - 24, ancho - 39 , 32, 5, 5);
					g2.setColor(ventanaInterior);
					if (anchoTexto > 120) {
				    	Shape clipAnterior = g2.getClip();
				    	g2.setClip(posX + 16, posY + 16, ancho - 32, alto - 32);
				    	g2.drawString(unidad.getListaDeHabilidades()[i], posX + separacion - offsetTexto, posPalabras);
				    	g2.setClip(clipAnterior);
				    	iniciarDesplazamientoTexto(ancho, unidad.getListaDeHabilidades()[i]);
				        
				    } else {
				        g2.drawString(unidad.getListaDeHabilidades()[i], posX + separacion, posPalabras);
				    }
				}
				else {
					g2.setColor(ventanaInteriorSombra);
					g2.drawLine(posX + 16, posPalabras - 24, posX + 16 + ancho - 40, posPalabras - 24); // Línea superior
					g2.drawLine(posX + 16, posPalabras + 8, posX + 16 + ancho - 40, posPalabras + 8);   // Línea inferior
				}	
			}
			posPalabras += separacion;
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

	public void dibujarEstadosDeUnidad(Unidad unidad) {
		int posX = pdj.anchoDePantalla - pdj.tamañoDeBaldosa * 3;
	    int posY = + 24;
	    int ancho = pdj.tamañoDeBaldosa * 3 - 24 ;
	    int alto = pdj.tamañoDeBaldosa * 4;
	    int offsetX = 0;
	    int offsetY = 5;
	    int contadorEstados = 0;
	    
	    //VENTANA////////////////////////////////////////////
	    
	    Color ventanaExterior = new Color(128, 20, 20); 
		Color ventanaExteriorSombra = new Color(90, 0, 0);
		Color ventanaInterior = new Color(250, 200, 150);
		Color ventanaInteriorSombra = new Color(60, 0, 0);
		Color lineas = new Color(90, 0, 0); 
		
		g2.setColor(ventanaExteriorSombra);
		g2.fillRoundRect(posX - 24, posY -8, ancho + 32, alto + 32, 5, 5);
		g2.setColor(ventanaExterior);
	    g2.fillRoundRect(posX - 16, posY - 16, ancho + 32, alto + 32, 5, 5);
	    g2.setColor(ventanaInterior);
	    g2.fillRoundRect(posX, posY, ancho, alto, 5, 5);
	    g2.setColor(ventanaExteriorSombra);
	    g2.fillRoundRect(posX + ancho - 6, posY, ancho/20, alto, 2, 2);
	    g2.fillRoundRect(posX, posY, ancho, alto/20, 2, 2);
	    g2.setColor(lineas);
	    g2.drawRoundRect(posX, posY, ancho, alto, 5, 5);
	    g2.drawRoundRect(posX - 16, posY - 16, ancho + 32, alto + 32, 5, 5);
	    //ESTADOS///////////////////////////////////////////////////////////////
	    ArrayList<Image> iconos = new ArrayList<>();
	    if (unidad.getDef() + unidad.getDefMod() < 1) {
	        iconos.add(pdj.idr.fragile);
	    } else {
	        if (unidad.getDefMod() > 0) iconos.add(pdj.idr.shieldUp);
	        if (unidad.getDefMod() < 0) iconos.add(pdj.idr.shieldDown);
	    }
	    if (unidad.getAtqMod() > 0) iconos.add(pdj.idr.swordUp);
	    if (unidad.getAtqMod() < 0) iconos.add(pdj.idr.swordDown);
	    if (unidad.getVelMod() > 0) iconos.add(pdj.idr.bootUp);
	    if (unidad.getVelMod() < 0) iconos.add(pdj.idr.bootDown);
	    if (unidad.getEvaMod() > 0) iconos.add(pdj.idr.dashUp);
	    if (unidad.getEvaMod() < 0) iconos.add(pdj.idr.dashDown);  
	    if (unidad.getBloqMod() > 0) iconos.add(pdj.idr.blockUp);
	    if (unidad.getBloqMod() < 0) iconos.add(pdj.idr.blockDown);
	    if (unidad.getPcrtMod() > 0) iconos.add(pdj.idr.bulleyeUp);
	    if (unidad.getPcrtMod() < 0) iconos.add(pdj.idr.bulleyeDown);
	    if (unidad.getTimerSangrando() > 0) iconos.add(pdj.idr.bleed);
	    if (unidad.getTimerIncendiado() > 0) iconos.add(pdj.idr.burn);

	    for (Image icono : iconos) {
	        g2.drawImage(icono, posX + offsetX, posY + offsetY + pdj.tamañoDeBaldosa*2 + 16, null);
	        offsetX += icono.getWidth(null) - 4;
	        contadorEstados++;

	        if (contadorEstados == 4) {
	            offsetX = 0;  
	            offsetY += pdj.idr.shieldUp.getHeight(null) + 5; 
	        }
	    }
	    //DATOS///////////////////////////////////////////////////////////////
	    g2.setColor(ventanaInteriorSombra);
	    g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16f));
	    g2.drawString(unidad.getClase(), posX + 4, posY + 54);
	    g2.drawString("HP: "+unidad.getHP()+"/"+unidad.getHPMax(), posX + 4, posY + 78);
	    if(unidad.getSPMax() == 0) {
	    	g2.drawString("SP: -/- ", posX + 4, posY + 102);
	    }
	    else {
	    	g2.drawString("SP: "+unidad.getSP()+"/"+unidad.getSPMax(), posX + 4, posY + 102);
	    }
	    
	    g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20f));
	    g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20f));

	    FontMetrics fm = g2.getFontMetrics();
	    int anchoTexto = fm.stringWidth(unidad.getNombre());
	    if (anchoTexto > 72) {
	    	Shape clipAnterior = g2.getClip();
	    	g2.setClip(posX, posY, ancho, alto);
	    	// Dibujar el texto desplazado dentro del área recortada
	    	g2.drawString(unidad.getNombre(), posX + 8 - offsetTexto, posY + 32);
	    	g2.setClip(clipAnterior);
	    	iniciarDesplazamientoTexto(ancho, unidad.getNombre());
	        
	    } else {
	        g2.drawString(unidad.getNombre(), posX + 4, posY + 32);
	    }
	}
	
	public void iniciarDesplazamientoTexto(int ancho, String cadena) {
	    if (timerTexto == null) {
	        timerTexto = new Timer(100, e -> {
	            offsetTexto += velocidadDesplazamiento;
	            if (offsetTexto > g2.getFontMetrics().stringWidth(cadena)) {
	                offsetTexto = - ancho + 10; // Reiniciar cuando se salga completamente
	            }
	            pdj.repaint(); // Vuelve a dibujar la ventana
	        });
	        timerTexto.start();
	    }
	}
	
	
	public void dibujarMarcoDeJuego() {
		Color ventanaExterior = new Color(128, 20, 20); 
		Color ventanaExteriorSombra = new Color(90, 0, 0);
		
		g2.setColor(ventanaExterior);
		g2.fillRoundRect(0, 0, pdj.anchoDePantalla, 8, 5, 5);
		g2.fillRoundRect(0, pdj.altoDePantalla - 8, pdj.anchoDePantalla, 8, 5, 5);
		g2.fillRoundRect(0, 0, 8, pdj.altoDePantalla, 5, 5);
		g2.fillRoundRect(pdj.anchoDePantalla - 8, 0, 8, pdj.altoDePantalla, 5, 5);
		g2.setColor(ventanaExteriorSombra);
		g2.fillRoundRect(8, 8, pdj.anchoDePantalla - 16, 8, 5, 5);
		g2.fillRoundRect(pdj.anchoDePantalla -16, 8, 8, pdj.altoDePantalla - 16, 5, 5);
		g2.drawRoundRect(0, 0, pdj.anchoDePantalla, pdj.altoDePantalla, 5, 5);
		g2.drawRoundRect(8, 8, pdj.anchoDePantalla - 16, pdj.altoDePantalla - 16, 5, 5);
	}
	
	public void dibujarLibretaEscolar(Unidad unidad) {
		int ancho = pdj.tamañoDeBaldosa*8;
		int alto = pdj.tamañoDeBaldosa*6;
		int posX = ancho / 2;
		int posY = alto / 2;
		Color ventanaExteriorSombra = new Color(90, 0, 0);
		Color ventanaInterior = new Color(250, 200, 150);
		Color ventanaInteriorSombra = new Color(60, 0, 0);
		Color lineas = new Color(90, 0, 0);
		Color colorPositivo = new Color(0, 180, 0); // Verde para buffs
		Color colorNegativo = new Color(180, 0, 0); // Rojo para debuffs
		
		g2.setColor(ventanaExteriorSombra);
		g2.fillRoundRect( posX-16, posY+16, ancho+32, alto, 15, 15);
		g2.setColor(Color.white);
		g2.drawRoundRect( posX-16, posY+16, ancho+32, alto, 15, 15);
		g2.setColor(ventanaInterior);
		g2.fillRoundRect( posX, posY, ancho/2, alto, 15, 15);
		g2.fillRoundRect( posX + ancho/2, posY, ancho/2, alto, 15, 15);
		g2.setColor(lineas);
		g2.drawRoundRect( posX + 8, posY + 8, ancho/5, alto/4, 15, 15);
		g2.drawRoundRect( posX, posY, ancho/2, alto, 15, 15);
		g2.drawRoundRect( posX + ancho/2, posY, ancho/2, alto, 15, 15);
		
		g2.drawImage(unidad.imagenesBody[0], posY + pdj.tamañoDeBaldosa + 12, posY + 8, null);
		
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 18f));
		g2.setColor(new Color(150, 100, 50));
		g2.drawString("Name: ", posX + 8, posY + pdj.tamañoDeBaldosa * 2 + 12);
		g2.drawString("Type: ", posX + 8, posY + pdj.tamañoDeBaldosa * 2 + 52);
		g2.drawString("Faction: ", posX + 8, posY + pdj.tamañoDeBaldosa * 2 + 92);
		g2.drawString("Male:", posX + 96, posY + 32);
		g2.drawString("Female:", posX + 96, posY + 72);
		g2.setColor(ventanaInteriorSombra);
		if(unidades.get(0).getGenero() == 1) {
			g2.drawString("X", posX + 152, posY + 32);
		}
		else {
			g2.drawString("X", posX + 152, posY + 72);
		}
		g2.drawString(unidad.getNombre(), posX + 8, posY + pdj.tamañoDeBaldosa * 2 + 32);
		g2.drawString(unidad.getClase(), posX + 8, posY + pdj.tamañoDeBaldosa * 2 + 72);
		g2.drawString(this.nombreDeFaccion(unidad), posX + 8, posY + pdj.tamañoDeBaldosa * 2 + 112);
		//ESTADISTICAS//////////////////////////////////////////////////////////
		g2.setColor(new Color(150, 100, 50));
		g2.drawString("HP: ", posX + pdj.tamañoDeBaldosa * 4 + 24, posY + 32);
		g2.drawString("SP: ", posX + pdj.tamañoDeBaldosa * 4 + 24, posY + 62);
		g2.drawString("ATQ: ", posX + pdj.tamañoDeBaldosa * 4 + 24, posY + 92);
		g2.drawString("DEF: ", posX + pdj.tamañoDeBaldosa * 4 + 24, posY + 122);
		g2.drawString("EVA: ", posX + pdj.tamañoDeBaldosa * 4 + 24, posY + 152);
		g2.drawString("T.BLOQ: ", posX + pdj.tamañoDeBaldosa * 4 + 24, posY + 182);
		g2.drawString("VEL: ", posX + pdj.tamañoDeBaldosa * 4 + 24, posY + 212);
		g2.drawString("P.CRT: ", posX + pdj.tamañoDeBaldosa * 4 + 24, posY + 242);
		g2.drawString("D.CRT: ", posX + pdj.tamañoDeBaldosa * 4 + 24, posY + 272);
		g2.setColor(ventanaInteriorSombra);
		g2.drawString(""+unidad.getHP()+"/"+unidad.getHPMax(), posX + pdj.tamañoDeBaldosa * 4 + 64, posY + 32);
		if(unidades.get(0).getHPMax() > 0) {
			g2.drawString(""+unidad.getSP()+"/"+unidad.getSPMax(), posX + pdj.tamañoDeBaldosa * 4 + 64, posY + 62);
		}
		else {
			g2.drawString("--/--", posX + pdj.tamañoDeBaldosa * 4 + 64, posY + 62);
		}
		g2.drawString(""+unidad.getAtq(), posX + pdj.tamañoDeBaldosa * 4 + 64, posY + 92);
		g2.drawString(""+unidad.getDef()+" %", posX + pdj.tamañoDeBaldosa * 4 + 64, posY + 122);
		g2.drawString(""+unidad.getEva()+" %", posX + pdj.tamañoDeBaldosa * 4 + 64, posY + 152);
		g2.drawString(""+unidad.getBloq()+" %", posX + pdj.tamañoDeBaldosa * 4 + 80, posY + 182);
		g2.drawString(""+unidad.getVel()+" KM/H", posX + pdj.tamañoDeBaldosa * 4 + 64, posY + 212);
		g2.drawString(""+unidad.getPCRT()+" %", posX + pdj.tamañoDeBaldosa * 4 + 72, posY + 242);
		g2.drawString("x."+unidad.getDCRT(), posX + pdj.tamañoDeBaldosa * 4 + 72, posY + 272);
		//AUMENTOS Y DEBILITACIONES/////////////////////////////////////////////
		int modAtq = unidad.getAtqMod();
		g2.setColor(modAtq > 0 ? colorPositivo : (modAtq < 0 ? colorNegativo : ventanaInteriorSombra));
		g2.drawString(modAtq > 0 ? "(+" + modAtq + ")" : (modAtq < 0 ? "(" + modAtq + ")" : ""), posX + pdj.tamañoDeBaldosa * 4 + 96, posY + 92);

		// DEF
		int modDef = unidad.getDefMod();
		g2.setColor(modDef > 0 ? colorPositivo : (modDef < 0 ? colorNegativo : ventanaInteriorSombra));
		g2.drawString(modDef > 0 ? "(+" + modDef + "%)" : (modDef < 0 ? "(" + modDef + "%)" : ""), posX + pdj.tamañoDeBaldosa * 4 + 122, posY + 122);

		// Evasión (EVA)
		int modEva = unidad.getEvaMod();
		g2.setColor(modEva > 0 ? colorPositivo : (modEva < 0 ? colorNegativo : ventanaInteriorSombra));
		g2.drawString(modEva > 0 ? "(+" + modEva + "%)" : (modEva < 0 ? "(" + modEva + "%)" : ""), posX + pdj.tamañoDeBaldosa * 4 + 122, posY + 152);

		// Tasa de Bloqueo (T.BLOQ)
		int modBloq = unidad.getBloqMod();
		g2.setColor(modBloq > 0 ? colorPositivo : (modBloq < 0 ? colorNegativo : ventanaInteriorSombra));
		g2.drawString(modBloq > 0 ? "(+" + modBloq + "%)" : (modBloq < 0 ? "(" + modBloq + "%)" : ""), posX + pdj.tamañoDeBaldosa * 4 + 122, posY + 182);

		// Velocidad (VEL)
		int modVel = unidad.getVelMod();
		g2.setColor(modVel > 0 ? colorPositivo : (modVel < 0 ? colorNegativo : ventanaInteriorSombra));
		g2.drawString(modVel > 0 ? "(+" + modVel + " KM)" : (modVel < 0 ? "(" + modVel + " KM)" : ""), posX + pdj.tamañoDeBaldosa * 4 + 126, posY + 212);
		
		// Prob. crit (P.CRT)
		int modPCrt = unidad.getPcrtMod();
		g2.setColor(modPCrt > 0 ? colorPositivo : (modPCrt < 0 ? colorNegativo : ventanaInteriorSombra));
		g2.drawString(modPCrt > 0 ? "(+" + modPCrt + " %)" : (modPCrt < 0 ? "(" + modPCrt + " %)" : ""), posX + pdj.tamañoDeBaldosa * 4 + 122, posY + 242);
		////////////////////////////////////////////////////////////////////////

		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16f));
	}
	
	public String nombreDeFaccion(Unidad unidad) {
		if(unidad.getIdFaccion() == 1) {
			return "Nietos de la Patria Perdida";
		}
		else if(unidad.getIdFaccion() == 2) {
			return "Dragon-Ate-Sun";
		}
		else if(unidad.getIdFaccion() == 3) {
			return "El Piño y El Dragon";
		}
		else if(unidad.getIdFaccion() == 4) {
			return "Mei Hua Jie";
		}
		else {
			return "Unknown";
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
			resaltador.setLocation(unidad.getPosX(), unidad.getPosY()+16);
			if(!unidad.isAliado()) {
				unidadObservada = unidad;
			}
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
