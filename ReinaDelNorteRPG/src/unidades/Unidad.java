package unidades;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.imageio.ImageIO;
import principal.Habilidades;
import principal.PanelDeJuego;
import principal.Utilidades;
import principal.Zona;

public class Unidad {
	//SISTEMA////////////////////////////////////////////////////////
	PanelDeJuego pdj;
	Graphics2D g2;
	//GestorDeHabilidades gdh;
	//POSICIONAMIENTO////////////////////////////////////////////////
	private Zona zona;
	private int posX = 0;
	private int posY = 0;
	//INDICADORES DE SALUD Y NOMBRE//////////////////////////////////
	int barraHP = 72;
	private String textoDañoRecibido;
	private String textoCuracionRecibida;
	private String textoInformativo;
	private int cdHabilidad1 = 0;
	private int cdHabilidad2 = 0;
	private boolean habilidad1 = true;
	private boolean habilidad2 = true;
	//ESTADOS Y COMPORTAMIENTOS//////////////////////////////////////
	private int idMate;
	private boolean esAliado = false;
	private boolean estaActivo = true;
	private boolean estaVivo = true;	
	private boolean estaMarcado;
	private boolean realizaUnaCuracion;
	private boolean estaEsquivando;
	private boolean tomandoUnMate;
	private boolean esUnaHabilidad;
	private boolean estaGanandoSP;
	private boolean estaMotivado;
	private boolean estaDesmotivado;
	private boolean estaLisiado;
	private boolean estaDebilitado;
	private boolean estaKO;
	private boolean estaBloqueando;
	private boolean estaEnamorado;
	//ACCIONES//////////////////////////////////////////////////////
	private boolean realizaUnCritico;
	private boolean evadiendo;
	private boolean rompiendo;
	private boolean curando;
	//ESTADOS////////////////////////////////////////////////////////
	private boolean precavido;
	private boolean agresivo;
	private boolean acelerado;
	private boolean potenciado;
	//CONTADORES DE ESTADOS//////////////////////////////////////////
	private int timerPrecavido = -1;
	private int timerAgresivo = -1;
	private int timerAcelerado = -1;
	private int timerPotenciado = -1;
	//VARIABLES PARA LA SACUDIDA/////////////////////////////////////
	private boolean enSacudida = false;
	private int duracionSacudida = 0; // Duración en frames
	private int desplazamientoSacudidaX = 0;
	private int desplazamientoSacudidaY = 0;
	private int desplazarDañoRecibido;
    //ELEMENTOS DE LA UNIDAD/////////////////////////
	private BufferedImage[] imagenesBody = new BufferedImage[5];
	private BufferedImage[] imagenesDead = new BufferedImage[5];
	private int tiempoMov = 20;
	private int imageMov = 0;
	private int direccion = 1; // 1 para incrementar, -1 para decrementar
	private String tipoDeAccion = "";
	private String[] listaDeHabilidades = new String[1];
	private int habilidadElegida = -1;
	private boolean objetivoUnico = true;
	private boolean mostrarFaltas = false;
	private int alturaPorClase = 0;
	private int alturaDeAccesorio = 0;
	private int alturaDeBarraHP = 0;
	private int idFaccion;
	private int idTez = 0;
	private int escudos = 0;
	private int faltasCometidas = 0;
	private int neocreditos;
	private int dañoCausado = 0;
	private int puñosAcumulados;
	private int cargarEnamoramiento = 0;
	private int contador = 5;
	//ESTADISTICAS DE LA UNIDAD/////////////////////////////////////
	private String nombre;
	private String clase;
	private String tipo;
	private int genero;
	private int hp;
	private int hpMax;
	private int sp;
	private int spMax;
	private int atq;
	private int def;
	private int vel;
	private int eva;
	private int pcrt;
	private double dcrt;
	//MODIFICADORES DE STATS////////////////////////////////////////
	private int hpMaxMod = 0;
	private int spMaxMod = 0;
	private int atqMod = 0;
	private int defMod = 0;
	private int velMod = 0;
	private int evaMod = 0;
	private int pcrtMod = 0;
	private double dcrtMod = 0;
	private double capacidadBloqueo;
	private int vidaPerdida = 0;
	////////////////////////////////////////////////////////////////
	public Unidad(Zona zona,boolean aliado, PanelDeJuego pdj) {
		this.pdj = pdj;
		//this.gdh = gdh;
		this.textoInformativo = "";
		this.textoDañoRecibido = "";
		this.textoCuracionRecibida = "";
		this.setZona(zona);
		this.setTomandoUnMate(false);
		this.setIdMate(-1);
		this.setPosX(zona.x);
		this.setPosY(zona.y);
		this.setAliado(aliado);
		this.genero = elegirAleatorio(2);
		if(aliado) {
			desplazarDañoRecibido = 384;
		}
		else {
			desplazarDañoRecibido = 96;
		}
		this.generarCuerpo();
		this.cargarDummyMuerto();
	}
	//METODOS PRINCIPALES///////////////////////////////////////////
	public void actualizar() {
		if (tiempoMov > 0) {
		    if (tiempoMov % 5 == 0) {
		        imageMov += direccion;
		    }
		    tiempoMov--;
		} else {
		    tiempoMov = 25;
		    // Cambiar la dirección
		    direccion *= -1;
		}
        if (estaEnSacudida()) {
            if (getDuracionSacudida() > 0) {
            	setDuracionSacudida(getDuracionSacudida() - 1);
                desplazamientoSacudidaX = (int) (Math.random() * 10 - 5);
                desplazamientoSacudidaY = (int) (Math.random() * 10 - 5);
                desplazarDañoRecibido--;
                
            } else {
                setearSacudida(false);
                realizaUnaCuracion = false; 
                realizaUnCritico = false;
                desplazamientoSacudidaX = 0;
                desplazamientoSacudidaY = 0;
                desplazarDañoRecibido = getPosY();
                textoInformativo = "";
                textoDañoRecibido = "";
                textoCuracionRecibida = "";
                cancelarTextoDeEfectos();
            }
        }
        if(getHP() <= 0) {
        	setAlive(false);
        }
    }	
	public void dibujar(Graphics2D g2) {
        this.g2 = g2;  
        dibujarVida();
        if(isEstaEnamorado()) {
        	dibujarCorazon(g2);
        }
        dibujarEscudos(g2);
        if(getMostrarFaltas()) {
        	dibujarFaltas(g2);
        }
        int dibujarX = getPosX() + desplazamientoSacudidaX;
        int dibujarY = getPosY() + desplazamientoSacudidaY;
        if(isAlive()) {
        	mostrarImagenes(g2, dibujarX+10, dibujarY-20+alturaPorClase, imageMov);
        } 
        else {
        	mostrarMuerte(g2, dibujarX+10, dibujarY-20, imageMov);
        }
        //MOSTRAR DAÑO RECIBIDO Y EFECTOS////////////////////////////
        if(this.estaMarcado) {
        	Image marca = configurarImagen("/efectos/mark", 4);
        	g2.drawImage(marca, dibujarX, dibujarY, null);
        }
        
        if(this.isCurando()) {
            g2.setColor(Color.green);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16f));
            g2.drawString(this.getTextoInformativo() , getPosX()+84, desplazarDañoRecibido-48);
        }
        else if(this.realizaUnCritico) {
        	g2.setColor(Color.YELLOW);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20f));
            g2.drawString(this.getTextoDañoRecibido(), getPosX()+84, desplazarDañoRecibido-48);
        }
        else if(this.isEvadiendo()) {
        	g2.setColor(Color.GRAY);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20f));
            g2.drawString(this.getTextoInformativo(), getPosX()+84, desplazarDañoRecibido-48);
        }
        else if(this.isRompiendo()) {
        	g2.setColor(Color.WHITE);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20f));
            g2.drawString(this.getTextoInformativo(), getPosX()+84, desplazarDañoRecibido-48);
        }
        else if(this.isPrecavido()) {
        	Color c = new Color(55,55,255);
        	g2.setColor(c);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20f));
            g2.drawString(this.getTextoInformativo() , getPosX()+84, desplazarDañoRecibido-48);
        }
        else if(this.isAgresivo()) {
        	Color c = new Color(255,55,55);
        	g2.setColor(c);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20f));
            g2.drawString(this.getTextoInformativo() , getPosX()+84, desplazarDañoRecibido-48);
        }
        else if(this.isAcelerado()) {
        	Color c = new Color(55,255,255);
        	g2.setColor(c);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20f));
            g2.drawString(this.getTextoInformativo() , getPosX()+84, desplazarDañoRecibido-48);
        }
        else if(this.isPotenciado()) {
        	Color c = new Color(255,255,55);
        	g2.setColor(c);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20f));
            g2.drawString(this.getTextoInformativo() , getPosX()+84, desplazarDañoRecibido-48);
        }
        else {
        	g2.setColor(Color.WHITE);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16f));
            g2.drawString(this.getTextoDañoRecibido(), getPosX()+84, desplazarDañoRecibido-48);
        }
        /////////
        
        
        if(this.estaKO) {
        	Image KO = configurarImagen("/efectos/stun", 3);
        	g2.drawImage(KO, dibujarX+10, dibujarY, null);
        }
        else if(this.estaBloqueando) {
        	g2.setColor(Color.GRAY);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20f));
            g2.drawString("BLOCK", getPosX()+84, desplazarDañoRecibido-48);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16f));
        }
        
        
        efectosVisualesPersonalizados(g2);
    }
	//METODOS DE ACCION///////////////////////////////////////////////////////
	public void realizarAccion(ArrayList<Unidad> enemigos, ArrayList<Unidad> aliados) {
		int accion = elegirAleatorio(2);
		if(accion == 0 && cumpleReqDeHab1()) {
			setHabilidadElegida(0);
			usarHabilidadEnemigo(enemigos);
		}
		else {
			realizarAtaqueEnemigo(enemigos);
		}
	}
	
	public void recibirDaño(int daño, boolean isCritical, int duracionSacudida) {
	    int SEId = 2;
	    int reduccion = (int) (daño * ((this.getDef() + this.getDefMod()) / 100.0));
        int dañoFinal = Math.max(1, daño - reduccion);
        String textoMostrado = "";
	    if (this.elegirAleatorio(100) < (this.getEva() + this.getEvaMod())) {
	    	pdj.ReproducirSE(6);
	    	this.setEvadiendo(true);
	        textoMostrado = "MISS!";
	        Habilidades.setearEstado(this, textoMostrado);
	    }
	    else {
	    	if (this.escudos > 0) {
		        escudos--;
		        pdj.ReproducirSE(9);
		        this.setRompiendo(true);
		        textoMostrado = "BREAK!";
		        Habilidades.setearEstado(this, textoMostrado);
		    } else {
		    	if (isCritical) {
			    	SEId = 3;
			    	this.realizaUnCritico = true;
			        textoMostrado = "CRITICAL " + dañoFinal + "!";
			    } else {
			        textoMostrado = "" + dañoFinal;
			    }
		        this.setHP(this.getHP() - dañoFinal);
		        pdj.ReproducirSE(SEId);
		        Habilidades.setearDaño(this, textoMostrado);
		    }
	    }  
	}
	
	public void realizarAtaque(Unidad unidad, ArrayList<Unidad> enemigos) {
		boolean isCritical = Math.random() <= ((this.getPCRT() + this.getPcrtMod()) / 100);
		if(unidad != null) {
			int daño = this.getAtq() + this.getAtqMod();
			if (isCritical) {
				daño *= (this.getDCRT() + this.getDcrtMod());
			}
			unidad.recibirDaño(daño, isCritical, 20);
			this.reflejarDaño(unidad, daño);
			this.robarVida(daño, unidad);
		}
	}

	public void realizarAtaqueEnemigo(ArrayList<Unidad> unidades) {
		Unidad objetivo = elegirObjetivo(unidades);
		boolean isCritical = Math.random() <= ((this.getPCRT() + this.getPcrtMod()) / 100);  
		if(objetivo != null) {
			int daño = this.getAtq() + this.getAtqMod();
			if (isCritical) {
				daño *= (this.getDCRT() + this.getDcrtMod());
			}
			objetivo.recibirDaño(daño, isCritical, 20);
			this.reflejarDaño(objetivo, daño);
			robarVida(daño, objetivo);
		}
	}
	
	public Unidad getProtector(ArrayList<Unidad> unidades) {
		if(!unidades.isEmpty()) {
			for(Unidad unidad : unidades) {
				if(unidad.getClase() == "Influencer") {
					return unidad;
				}
			}
		}
		return null;
	}
	
	public boolean puedeBloquear() {
		return Math.random() <= (this.getCapacidadBloqueo());  
	}	
	
	public void reflejarDaño(Unidad unidad, int daño) {
		if(unidad.getClase() == "Shaolin Escolar" || unidad.getClase() == "Aspirante A Dragon") {
			int i = (daño/2);
			if(i > 1) {
				this.recibirDaño(i, false, 20);
			}
			else {
				this.recibirDaño(1, false, 20);
			}
		}
	}
	
	public void recibirGolpesMúltiples(int daño,int cant, boolean isCritical) {
	    int numGolpes = cant;
	    new Thread(() -> {
	        for (int i = 0; i < numGolpes; i++) {
	            try {
	                Thread.sleep(200);
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	            boolean golpeCritico = false;
	            if (isCritical) {
	                golpeCritico = elegirAleatorio(2) == 0; // 50% probabilidad de crítico
	            }
	            // Aplicar daño reducido para los golpes iniciales
	            int dañoActual = (i == numGolpes - 1) ? daño : (daño/2);
	            if(dañoActual == 0) {
	            	dañoActual = 1;
	            }
	            recibirDaño(dañoActual, golpeCritico, 10);
	        }
	    }).start();
	}
	
	public void robarVida(int daño, Unidad unidad) {
		if(unidad.getEstaMarcado()) {
			if(this.getClase() == "Aspirante A Dragon") {
			}
			else {
			}
			this.setRealizandoCuracion(true);
			this.setDuracionSacudida(20);
			this.setearSacudida(true);
		}
	}
	
	public void contarFaltas(Unidad unidad, int cant) {
		if(unidad.getGenero() == 0) {
			this.setFaltasCometidas(this.getFaltasCometidas() + cant);
			//this.faltasCometidas++;
		}
	}
	
	public void pasivaDeClase(ArrayList<Unidad> aliados, ArrayList<Unidad> enemigos) {
		iniciarTimersDeEstado();
		Habilidades.restaurarSP(this, 5);
		if(!enemigos.isEmpty()) {
			for(Unidad unidad : enemigos) {
				if(unidad.getClase() == "Delegada") {
					this.setMostrarFaltas(true);
				}
			}
		}
	}
	
	public void iniciarTimersDeEstado() {
	    actualizarTimer(this::getTimerPrecavido, this::setTimerPrecavido, () -> Habilidades.reducirProteccion(this));
	    actualizarTimer(this::getTimerAgresivo, this::setTimerAgresivo, () -> Habilidades.reducirAgresividad(this));
	    actualizarTimer(this::getTimerAcelerado, this::setTimerAcelerado, () -> Habilidades.reducirAgilidad(this));
	    actualizarTimer(this::getTimerPotenciado, this::setTimerPotenciado, () -> Habilidades.debilitarUnidad(this));
	}

	private void actualizarTimer(Supplier<Integer> getter, Consumer<Integer> setter, Runnable habilidad) {
	    if (getter.get() > 0) {
	        setter.accept(getter.get() - 1);
	    } else if (getter.get() == 0) {
	        habilidad.run();
	        setter.accept(-1);
	    }
	}
	
	public void sumarNeocreditos(int neocreditos) {
		int i = neocreditos + this.neocreditos;
		if(i > 100) {
			this.neocreditos = 100;
		}
		else {
			this.neocreditos += neocreditos;
		}
	}
	
	public void usarHabilidadEnemigo(ArrayList<Unidad> unidades) {
	}	
	public void usarHabilidad(Unidad unidad, ArrayList<Unidad> unidades) {}
	//METODOS VARIOS///////////////////////////////////////////////////////
	public void cancelarTextoDeEfectos() {
		this.setPrecavido(false);
		this.setAgresivo(false);
		this.setAcelerado(false);
		this.setPotenciado(false);
		this.setEvadiendo(false);
		this.setRompiendo(false);
		this.setCurando(false);
	}
	
	public int porcentajeHP(int valor) {
	    return (int) ((valor / this.getHPMax()) * 100);
	}
	
	public int porcentajeSP(int valor) {
	    return (int) ((valor / this.getSPMax()) * 100);
	}
	//METODOS VISUALES/////////////////////////////////////////////////////////
	public void dibujarVida() {
	    g2.setFont(g2.getFont().deriveFont(Font.BOLD, 15f));
	    g2.setColor(Color.white);
	    
	    String nombreCompleto = getClase();
	    int hp = calcularBarraHP();
	    int altura = -pdj.tamañoDeBaldosa - (pdj.tamañoDeBaldosa / 8) + 16;
	    int posX = getPosX() + 10;
	    int posY = getPosY() - 10 + altura + this.alturaDeBarraHP;

	    //MEDIR ANCHO DE TEXTO
	    FontMetrics metrics = g2.getFontMetrics();
	    int anchoTexto = metrics.stringWidth(nombreCompleto);
	    int anchoBarraHP = barraHP;

	    if (anchoTexto > anchoBarraHP) {
	        String[] partes = nombreCompleto.split(" ", 2);
	        String primeraParte = partes[0];
	        String segundaParte = (partes.length > 1) ? partes[1] : "";
	        int anchoTexto1 = metrics.stringWidth(primeraParte);
	        int anchoTexto2 = metrics.stringWidth(segundaParte);

	        g2.drawString(primeraParte, posX+((anchoBarraHP-anchoTexto1)/2), posY-16);
	        g2.drawString(segundaParte, posX+((anchoBarraHP-anchoTexto2)/2), posY -16 + metrics.getHeight());
	    } else {
	        g2.drawString(nombreCompleto, posX+((anchoBarraHP-anchoTexto)/2), posY);
	    }

	    // Dibujar la barra de vida
	    Color c = new Color(0, 0, 0, 200);
	    g2.setColor(c);
	    g2.fillRoundRect(posX, getPosY() + altura + this.alturaDeBarraHP, barraHP, pdj.tamañoDeBaldosa / 5, 5, 5);

	    g2.setColor(Color.red);
	    g2.fillRoundRect(posX, getPosY() + altura + this.alturaDeBarraHP, hp, pdj.tamañoDeBaldosa / 5, 5, 5);

	    c = new Color(255, 255, 255);
	    g2.setColor(c);
	    g2.setStroke(new BasicStroke(2));
	    g2.drawRoundRect(posX, getPosY() + altura + this.alturaDeBarraHP, barraHP, pdj.tamañoDeBaldosa / 5, 5, 5);
	}
	
	public void dibujarEscudos(Graphics2D g2) {
	    int altura = -pdj.tamañoDeBaldosa - (pdj.tamañoDeBaldosa / 8) + 16;
	    int posX = getPosX() + 85;
	    int posY = getPosY() - 8 + altura + this.alturaDeBarraHP;

	    if (this.escudos > 0) {
	        // Dibujar el rectángulo del escudo
	        g2.setColor(Color.gray);
	        g2.fillRect(posX, posY, 18, 16);
	        g2.setColor(Color.WHITE);
	        g2.drawString("" + this.escudos, posX + 5, posY + 16);

	        // Dibujar el triángulo apuntando hacia abajo
	        int[] xPoints = {posX, posX + 9, posX + 18}; // Puntos X
	        int[] yPoints = {posY + 16, posY + 24, posY + 16}; // Puntos Y

	        g2.setColor(Color.gray);
	        g2.fillPolygon(xPoints, yPoints, 3);
	    }
	}
	
	public void dibujarFaltas(Graphics2D g2) {
		int altura = -pdj.tamañoDeBaldosa - (pdj.tamañoDeBaldosa / 8) + 16;
	    int posX = getPosX() + 75;
	    int posY = getPosY() + 25 + altura + this.alturaDeBarraHP;
	    
	    if(this.faltasCometidas == 1) {
	    	g2.setColor(Color.ORANGE);
	    	g2.drawString("X", posX, posY);
	    }
	    else if(this.faltasCometidas == 2) {
	    	g2.setColor(Color.ORANGE);
	    	g2.drawString("X X", posX-10, posY);
	    }
	    else if(this.faltasCometidas > 2) {
	    	g2.setColor(Color.RED);
	    	g2.drawString("X X X", posX-20, posY);
	    }
	}	
	public void dibujarCorazon(Graphics2D g2) {
		int altura = -pdj.tamañoDeBaldosa - (pdj.tamañoDeBaldosa / 8) + 16;
	    int posX = getPosX();
	    int posY = getPosY() + altura + this.alturaDeBarraHP;
	    Image corazon = configurarImagen("/efectos/heart", 4);
	    g2.drawImage(corazon, posX, posY+this.alturaDeAccesorio-20+imageMov*2, null);
	}	
	public void efectosVisualesPersonalizados(Graphics2D g2){}
	//METODOS DE VARIOS///////////////////////////////////////////////////////
	//METODOS DE ELECCION/////////////////////////////////////////////////////
	public Unidad elegirObjetivo(ArrayList<Unidad> unidades) {
		Unidad unidadSeleccionada = null;
	    if (!unidades.isEmpty()) {
	    	if(this.getClase() == "Puño Furioso") {
			    int mayorPorcentajeHP = Integer.MIN_VALUE;
			    for (Unidad unidad : unidades) {
			    	int HPUnidad = unidad.getHPMax();
			        if (HPUnidad > mayorPorcentajeHP) {
			        	mayorPorcentajeHP = HPUnidad;
			            unidadSeleccionada = unidad;
			        }
			    }
			    return unidadSeleccionada;
			}
	    	else if(this.getTipo() == "Elite") {
	    		int menorPorcentajeHP = Integer.MAX_VALUE;
	    	    for (Unidad unidad : unidades) {
	    	    	int porcentajeHP = (unidad.getHP() * 100) / unidad.getHPMax();;
	    	        if (porcentajeHP < menorPorcentajeHP) {
	    	            menorPorcentajeHP = porcentajeHP;
	    	            unidadSeleccionada = unidad;
	    	        }
	    	    }
	    	    return unidadSeleccionada;
	    	}
	    	else {
	    		return unidades.get((int) (Math.random() * unidades.size()));
	    	}
	    }
	    return null;
	}
	
	public int elegirAleatorio(int i) {
	    Random random = new Random();
	    return random.nextInt(i);
	}	
	public int obtenerValorEntre(int min, int max) {
	    if (min > max) {
	        throw new IllegalArgumentException("El valor mínimo no puede ser mayor que el máximo.");
	    }
	    return (int) (Math.random() * (max - min + 1) + min);
	}
	//METODOS AUXILIARES//////////////////////////////////////////////////////
	public void posicionar(Zona zona) {
		setPosX(zona.x);
		setPosY(zona.y);
	}
	
	public int calcularBarraHP() {
		return (getHP()*barraHP)/getHPMax();
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
	
	public void definirIdTez() {
		if(this.idFaccion == 1) {
			this.idTez = obtenerValorEntre(1,3);
		}
		else if(this.idFaccion == 2) {
			this.idTez = obtenerValorEntre(4,5);
		}
		else if(this.idFaccion == 4) {
			if(this.clase != "Novata Cauta" && this.clase != "Novata Confiable") {
				this.idTez = obtenerValorEntre(1,5);
			}
		}
		else {
			this.idTez = obtenerValorEntre(1,5);
		}
	}
	
	public String elegirEquipo() {
		int i = elegirAleatorio(3);
		String equipo = "";
		if(i == 0) {
			equipo = "boca";
		}
		else if(i == 1) {
			equipo = "river";
		}
		else {
			equipo = "arg";
		}
		return equipo;
	}
	
	public String elegirColor() {
		int i = elegirAleatorio(2);
		String equipo = "";
		if(i == 0) {
			equipo = "amarillo";
		}
		else {
			equipo = "negro";
		}
		return equipo;
	}
	
	public void generarCuerpo() {
		definirIdTez();
		cargarDummy();
		if(this.idFaccion == 1) {
			imagenesBody[4] = configurarImagen("/imagenes/accesorios/boina1",3);
			String color = elegirEquipo();
			if(this.getGenero() == 1) {
				//SI ES HOMBRE/////////////////////////////////////////////////////////////////////////
				if(this.clase == "Heroe Federal") {
					imagenesBody[3] = configurarImagen("/imagenes/unisex/caballo-"+this.idTez, 3);
					imagenesBody[2] = configurarImagen("/imagenes/hombre/bici-boy", 3);
					imagenesBody[1] = configurarImagen("/imagenes/unisex/cuerpo-heroe", 3);
					//imagenesBody[0] = configurarImagen("/imagenes/hombre/cabeza-boy-"+this.idTez, 3);
					alturaPorClase = -10;
				}
				else if(this.clase == "Cebador De Mate") {
					imagenesBody[3] = configurarImagen("/imagenes/unisex/mates-"+color+"-"+this.idTez, 3);
					imagenesBody[2] = configurarImagen("/imagenes/hombre/pantalon-1",3);
					imagenesBody[1] = configurarImagen("/imagenes/unisex/cuerpo"+this.idTez, 3);
					imagenesBody[0] = configurarImagen("/imagenes/hombre/cabeza-boy-"+this.idTez, 3);
				}
				else if(this.clase == "Payador Picante") {
					imagenesBody[3] = configurarImagen("/imagenes/unisex/guitarra-"+this.idTez, 3);
					imagenesBody[2] = configurarImagen("/imagenes/hombre/pantalon-1",3);
					imagenesBody[1] = configurarImagen("/imagenes/unisex/cuerpo"+this.idTez, 3);
					imagenesBody[0] = configurarImagen("/imagenes/hombre/cabeza-boy-"+this.idTez, 3);
				}
				else if(this.clase == "Gaucho Moderno") {
					imagenesBody[3] = configurarImagen("/imagenes/hombre/cutter-"+this.idTez,3);
					imagenesBody[2] = configurarImagen("/imagenes/hombre/pantalon-subido-"+this.idTez,3);
					imagenesBody[1] = configurarImagen("/imagenes/unisex/cuerpo-gaucho", 3);
					imagenesBody[0] = configurarImagen("/imagenes/hombre/cabeza-boy-"+this.idTez, 3);
				}
			}
			//SI ES MUJER////////////////////////////////////////////////////////////////////////////
			else {
				if(this.clase == "Heroe Federal") {
					imagenesBody[3] = configurarImagen("/imagenes/unisex/caballo-"+this.idTez, 3);
					imagenesBody[2] = configurarImagen("/imagenes/mujer/bici-girl-"+this.idTez, 3);
					imagenesBody[1] = configurarImagen("/imagenes/unisex/cuerpo-heroe", 3);
					imagenesBody[0] = configurarImagen("/imagenes/mujer/cabeza-girl-"+this.idTez, 3);
					alturaPorClase = -10;
				}
				if(this.clase == "Cebador De Mate") {
					imagenesBody[3] = configurarImagen("/imagenes/unisex/mates-"+color+"-"+this.idTez, 3);
					imagenesBody[2] = configurarImagen("/imagenes/mujer/falda-"+this.idTez,3);
					imagenesBody[1] = configurarImagen("/imagenes/unisex/cuerpo"+this.idTez, 3);
					imagenesBody[0] = configurarImagen("/imagenes/mujer/cabeza-girl-"+this.idTez, 3);
				}
				else if(this.clase == "Payador Picante") {
					imagenesBody[3] = configurarImagen("/imagenes/unisex/guitarra-"+this.idTez, 3);
					imagenesBody[2] = configurarImagen("/imagenes/mujer/falda-"+this.idTez,3);
					imagenesBody[1] = configurarImagen("/imagenes/unisex/cuerpo"+this.idTez, 3);
					imagenesBody[0] = configurarImagen("/imagenes/mujer/cabeza-girl-"+this.idTez, 3);
				}
				else if(this.clase == "Gaucho Moderno") {
					imagenesBody[3] = configurarImagen("/imagenes/hombre/cutter-"+this.idTez,3);
					imagenesBody[2] = configurarImagen("/imagenes/mujer/falda-"+this.idTez,3);
					imagenesBody[1] = configurarImagen("/imagenes/unisex/cuerpo-gaucho", 3);
					imagenesBody[0] = configurarImagen("/imagenes/mujer/cabeza-girl-"+this.idTez, 3);
				}
			}
		}
		else if(this.idFaccion == 2) {
			//SI ES HOMBRE/////////////////////////////////////////////////////////////////////////
			if(this.getGenero() == 1) {
				if(this.clase == "Alumno Modelo") {
					imagenesBody[3] = configurarImagen("/imagenes/unisex/libro-"+this.idTez, 3);
					imagenesBody[2] = configurarImagen("/imagenes/hombre/pantalon-1",3);
					imagenesBody[1] = configurarImagen("/imagenes/unisex/cuerpo"+this.idTez, 3);
					imagenesBody[0] = configurarImagen("/imagenes/hombre/cabeza-boy-"+this.idTez, 3);
				}
				else if(this.clase == "Shaolin Escolar") {
					imagenesBody[3] = configurarImagen("/imagenes/unisex/manos-shaolin-"+this.idTez, 3);
					imagenesBody[2] = configurarImagen("/imagenes/hombre/pantalon-shaolin",3);
					imagenesBody[1] = configurarImagen("/imagenes/unisex/cuerpo"+this.idTez, 3);
					imagenesBody[0] = configurarImagen("/imagenes/hombre/cabeza-shaolin-"+this.idTez, 3);
				}
				else if(this.clase == "Medico Tradicionalista") {
					imagenesBody[3] = configurarImagen("/imagenes/unisex/quimicos-"+this.idTez, 3);
					imagenesBody[2] = configurarImagen("/imagenes/hombre/pantalon-1",3);
					imagenesBody[1] = configurarImagen("/imagenes/unisex/cuerpo"+this.idTez, 3);
					imagenesBody[0] = configurarImagen("/imagenes/hombre/cabeza-boy-"+this.idTez, 3);
				}
				else if(this.clase == "Dragon Pirotecnico") {
					imagenesBody[4] = configurarImagen("/imagenes/accesorios/dragon", 3);
					imagenesBody[3] = configurarImagen("/imagenes/unisex/cañon-"+this.idTez, 3);
					imagenesBody[2] = configurarImagen("/imagenes/hombre/pantalon-dragon",3);
					imagenesBody[1] = configurarImagen("/imagenes/unisex/cuerpo"+this.idTez, 3);
					imagenesBody[0] = configurarImagen("/imagenes/hombre/cabeza-boy-"+this.idTez, 3);
					alturaDeAccesorio = 5;
					this.alturaDeBarraHP = -20;
				}
			}
			else {
				//SI ES MUJER/////////////////////////////////////////////////////////////////////////
				if(this.clase == "Alumno Modelo") {
					imagenesBody[3] = configurarImagen("/imagenes/unisex/libro-"+this.idTez, 3);
					imagenesBody[2] = configurarImagen("/imagenes/mujer/falda-"+this.idTez,3);
					imagenesBody[1] = configurarImagen("/imagenes/unisex/cuerpo"+this.idTez, 3);
					imagenesBody[0] = configurarImagen("/imagenes/mujer/cabeza-girl-"+this.idTez, 3);
				}
				else if(this.clase == "Shaolin Escolar") {
					imagenesBody[3] = configurarImagen("/imagenes/unisex/manos-shaolin-"+this.idTez, 3);
					imagenesBody[2] = configurarImagen("/imagenes/mujer/falda-"+this.idTez,3);
					imagenesBody[1] = configurarImagen("/imagenes/unisex/cuerpo"+this.idTez, 3);
					imagenesBody[0] = configurarImagen("/imagenes/mujer/cabeza-girl-"+this.idTez, 3);
				}
				else if(this.clase == "Medico Tradicionalista") {
					imagenesBody[3] = configurarImagen("/imagenes/unisex/quimicos-"+this.idTez, 3);
					imagenesBody[2] = configurarImagen("/imagenes/mujer/falda-"+this.idTez,3);
					imagenesBody[1] = configurarImagen("/imagenes/unisex/cuerpo"+this.idTez, 3);
					imagenesBody[0] = configurarImagen("/imagenes/mujer/cabeza-girl-"+this.idTez, 3);
				}
				else if(this.clase == "Dragon Pirotecnico") {
					imagenesBody[4] = configurarImagen("/imagenes/accesorios/dragon", 3);
					imagenesBody[3] = configurarImagen("/imagenes/unisex/cañon-"+this.idTez, 3);
					imagenesBody[2] = configurarImagen("/imagenes/mujer/falda-dragon-"+this.idTez,3);
					imagenesBody[1] = configurarImagen("/imagenes/unisex/cuerpo"+this.idTez, 3);
					imagenesBody[0] = configurarImagen("/imagenes/mujer/cabeza-girl-"+this.idTez, 3);
					alturaDeAccesorio = 5;
					this.alturaDeBarraHP = -20;
				}
			}
		}
		else if(this.idFaccion == 3) {
			String color = elegirColor();
			//SI ES HOMBRE/////////////////////////////////////////////////////////////////////////
			if(this.getGenero() == 1) {
				if(this.clase == "Niño Cheung") {
					imagenesBody[3] = configurarImagen("/imagenes/unisex/manos-pose-"+this.idTez, 3);
					imagenesBody[2] = configurarImagen("/imagenes/hombre/pantalon-pose",3);
					imagenesBody[1] = configurarImagen("/imagenes/hombre/cuerpo-chino-boy", 3);
					imagenesBody[0] = configurarImagen("/imagenes/hombre/cabeza-boy-"+this.idTez, 3);
				}
				else if(this.clase == "Puño Furioso") {
					imagenesBody[3] = configurarImagen("/imagenes/unisex/nunchaku-"+color+"-"+this.idTez, 3);
					imagenesBody[2] = configurarImagen("/imagenes/unisex/cortos-pose-"+this.idTez,3);
					imagenesBody[1] = configurarImagen("/imagenes/unisex/cuerpo-chaqueta", 3);
					imagenesBody[0] = configurarImagen("/imagenes/hombre/cabeza-boy-"+this.idTez, 3);
				}
				else if(this.clase == "Maestro Del Chi") {
					imagenesBody[3] = configurarImagen("/imagenes/unisex/manos-pose-"+this.idTez, 3);
					imagenesBody[2] = configurarImagen("/imagenes/hombre/piernas-maestro-boy",3);
					imagenesBody[1] = configurarImagen("/imagenes/hombre/cuerpo-maestro-boy", 3);
					imagenesBody[0] = configurarImagen("/imagenes/hombre/cabeza-boy-"+this.idTez, 3);
				}
				else if(this.clase == "Aspirante A Dragon") {
					imagenesBody[3] = configurarImagen("/imagenes/unisex/nunchaku-rojo-"+this.idTez, 3);
					imagenesBody[2] = configurarImagen("/imagenes/unisex/piernas-amarillo",3);
					imagenesBody[1] = configurarImagen("/imagenes/unisex/cuerpo-amarillo", 3);
					imagenesBody[0] = configurarImagen("/imagenes/hombre/cabeza-boy-"+this.idTez, 3);
				}
			}
			else {
				//SI ES MUJER/////////////////////////////////////////////////////////////////////////
				if(this.clase == "Niño Cheung") {
					imagenesBody[3] = configurarImagen("/imagenes/unisex/manos-pose-"+this.idTez, 3);
					imagenesBody[2] = configurarImagen("/imagenes/mujer/falda-pose-"+this.idTez,3);
					imagenesBody[1] = configurarImagen("/imagenes/mujer/cuerpo-chino-girl", 3);
					imagenesBody[0] = configurarImagen("/imagenes/mujer/cabeza-girl-"+this.idTez, 3);
				}
				else if(this.clase == "Puño Furioso") {
					imagenesBody[3] = configurarImagen("/imagenes/unisex/nunchaku-"+color+"-"+this.idTez, 3);
					imagenesBody[2] = configurarImagen("/imagenes/unisex/cortos-pose-"+this.idTez,3);
					imagenesBody[1] = configurarImagen("/imagenes/unisex/cuerpo-chaqueta", 3);
					imagenesBody[0] = configurarImagen("/imagenes/mujer/cabeza-coleta-"+this.idTez, 3);
				}
				else if(this.clase == "Maestro Del Chi") {
					imagenesBody[3] = configurarImagen("/imagenes/unisex/manos-pose-"+this.idTez, 3);
					imagenesBody[2] = configurarImagen("/imagenes/mujer/piernas-maestro-girl-"+this.idTez,3);
					imagenesBody[1] = configurarImagen("/imagenes/mujer/cuerpo-maestro-girl", 3);
					imagenesBody[0] = configurarImagen("/imagenes/mujer/cabeza-girl-"+this.idTez, 3);
				}
				else if(this.clase == "Aspirante A Dragon") {
					imagenesBody[3] = configurarImagen("/imagenes/unisex/nunchaku-rojo-"+this.idTez, 3);
					imagenesBody[2] = configurarImagen("/imagenes/unisex/piernas-amarillo",3);
					imagenesBody[1] = configurarImagen("/imagenes/unisex/cuerpo-amarillo", 3);
					imagenesBody[0] = configurarImagen("/imagenes/mujer/cabeza-bollos-"+this.idTez, 3);
				}
			}
		}
		else if(this.idFaccion == 4) {
			if(this.getGenero() == 0) {
				if(this.clase == "Novata Timida") {
					imagenesBody[3] = configurarImagen("/imagenes/unisex/manos-timida-"+this.idTez, 3);
					imagenesBody[2] = configurarImagen("/imagenes/mujer/falda-"+this.idTez,3);
					imagenesBody[1] = configurarImagen("/imagenes/unisex/cuerpo"+this.idTez, 3);
					imagenesBody[0] = configurarImagen("/imagenes/mujer/cabeza-timida-"+this.idTez, 3);
				}
				else if(this.clase == "Novata Cauta") {
					imagenesBody[3] = configurarImagen("/imagenes/unisex/manos-"+this.idTez, 3);
					imagenesBody[2] = configurarImagen("/imagenes/mujer/falda-"+this.idTez,3);
					imagenesBody[1] = configurarImagen("/imagenes/unisex/cuerpo"+this.idTez, 3);
					imagenesBody[0] = configurarImagen("/imagenes/mujer/cabeza-girl-"+this.idTez, 3);
				}
				else if(this.clase == "Novata Confiable") {
					imagenesBody[3] = configurarImagen("/imagenes/unisex/manos-seña-"+this.idTez, 3);
					imagenesBody[2] = configurarImagen("/imagenes/mujer/falda-"+this.idTez,3);
					imagenesBody[1] = configurarImagen("/imagenes/unisex/cuerpo"+this.idTez, 3);
					imagenesBody[0] = configurarImagen("/imagenes/mujer/cabeza-coleta-"+this.idTez, 3);
				}
				else if(this.clase == "Delegada") {
					imagenesBody[4] = configurarImagen("/imagenes/accesorios/mano-de-lente-"+this.idTez,3);
					imagenesBody[3] = configurarImagen("/imagenes/unisex/manos-libreta-"+this.idTez, 3);
					imagenesBody[2] = configurarImagen("/imagenes/mujer/falda-"+this.idTez,3);
					imagenesBody[1] = configurarImagen("/imagenes/unisex/cuerpo"+this.idTez, 3);
					imagenesBody[0] = configurarImagen("/imagenes/mujer/cabeza-delegada-"+this.idTez, 3);
				}
				else if(this.clase == "Influencer") {
					imagenesBody[3] = configurarImagen("/imagenes/unisex/manos-celular-"+this.idTez, 3);
					imagenesBody[2] = configurarImagen("/imagenes/mujer/falda-"+this.idTez,3);
					imagenesBody[1] = configurarImagen("/imagenes/unisex/cuerpo"+this.idTez, 3);
					imagenesBody[0] = configurarImagen("/imagenes/mujer/cabeza-influencer-"+this.idTez, 3);
				}
				else if(this.clase == "Idol Galactica") {
					//imagenesBody[4] = configurarImagen("/imagenes/accesorios/dragon", 3);
					imagenesBody[3] = configurarImagen("/imagenes/unisex/manos-idol-"+this.idTez, 3);
					imagenesBody[2] = configurarImagen("/imagenes/mujer/falda-olanes",3);
					imagenesBody[1] = configurarImagen("/imagenes/mujer/cuerpo-idol", 3);
					imagenesBody[0] = configurarImagen("/imagenes/mujer/cabeza-idol-"+this.idTez, 3);
					//alturaDeAccesorio = 5;
					//this.alturaDeBarraHP = -20;
				}
			}
		}
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void cargarDummy() {
		imagenesBody[4] = null;
		imagenesBody[3] = configurarImagen("/imagenes/dummy/dummy-manos",3);
		imagenesBody[2] = configurarImagen("/imagenes/dummy/dummy-piernas",3);
		imagenesBody[1] = configurarImagen("/imagenes/dummy/dummy-cuerpo", 3);
		imagenesBody[0] = configurarImagen("/imagenes/dummy/dummy-cabeza", 3);
	}
	public void cargarDummyMuerto() {
		imagenesBody[4] = null;
		imagenesDead[2] = configurarImagen("/imagenes/dummy/dead-piernas", 3);
		imagenesDead[1] = configurarImagen("/imagenes/dummy/dead-cuerpo", 3);
		imagenesDead[0] = configurarImagen("/imagenes/dummy/dead-cabeza", 3);
		imagenesDead[3] = configurarImagen("/imagenes/dummy/dead-manos", 3);
	}
	public void mostrarImagenes(Graphics2D g2, int posX, int posY, int dezplazamiento) {
		g2.drawImage(imagenesBody[2], posX, posY+46, null);
		g2.drawImage(imagenesBody[1], posX, posY+28+dezplazamiento, null);
		g2.drawImage(imagenesBody[0], posX, posY+dezplazamiento*2, null);
		g2.drawImage(imagenesBody[4], posX-1, posY+this.alturaDeAccesorio-20+dezplazamiento*2, null);
		g2.drawImage(imagenesBody[3], posX, posY+24+dezplazamiento, null);
	}
	public void mostrarMuerte(Graphics2D g2, int posX, int posY, int dezplazamiento) {
		g2.drawImage(imagenesDead[2], posX, posY+46, null);
		g2.drawImage(imagenesDead[1], posX, posY+28+dezplazamiento, null);
		g2.drawImage(imagenesDead[0], posX, posY+dezplazamiento*2, null);
		g2.drawImage(imagenesDead[3], posX, posY+24+dezplazamiento, null);
	}
	public boolean cumpleReqDeHab1() {return false;}
	public boolean cumpleReqDeHab2() {return false;}
	public void configurarTipoDeaccion() {}
	//GETTERS & SETTERS////////////////////////////////////////////////////////
	public boolean isAliado() {return esAliado;}
	public void setAliado(boolean aliado) {this.esAliado = aliado;}
	public boolean isAlive() {return estaVivo;}
	public void setAlive(boolean vivo) {this.estaVivo = vivo;}
	public boolean isEstaActivo() {return estaActivo;}
	public void setEstaActivo(boolean estaActivo) {this.estaActivo = estaActivo;}
	public boolean isObjetivoUnico() {return objetivoUnico;}
	public void setObjetivoUnico(boolean singleTarget) {this.objetivoUnico = singleTarget;}
	public String getTextoInformativo() {
		return textoInformativo;
	}
	public void setTextoInformativo(String textoInformativo) {
		this.textoInformativo = textoInformativo;
	}
	public String getTextoDañoRecibido() {
		return textoDañoRecibido;
	}
	public void setTextoDañoRecibido(String textoDañoRecibido) {
		this.textoDañoRecibido = textoDañoRecibido;
	}
	public String getTextoCuracionRecibida() {
		return textoCuracionRecibida;
	}
	public void setTextoCuracionRecibida(String textoCuracionRecibida) {
		this.textoCuracionRecibida = textoCuracionRecibida;
	}
	//GETTERS & SETTERS DE EFECTOS//////////////////////////////////////////////
	public boolean getRealizaCuracion() {return this.realizaUnaCuracion;}
	public void setRealizandoCuracion(boolean boo) {this.realizaUnaCuracion = boo;}
	public boolean getEstaMarcado() {return this.estaMarcado;}
	public void setEstaMArcado(boolean boo) {this.estaMarcado = boo;}
	public boolean getTomandoUnMate() {return tomandoUnMate;}
	public void setTomandoUnMate(boolean esMate) {this.tomandoUnMate = esMate;}
	public boolean getEstaMotivado() {return estaMotivado;}
	public void setEstaMotivado(boolean esMotivar) {this.estaMotivado = esMotivar;}
	public boolean getEstaDesmotivado() {return estaDesmotivado;}
	public void setEstaDesmotivado(boolean esDesmotivar) {this.estaDesmotivado = esDesmotivar;}
	public boolean getEsUnaHabilidad() {return esUnaHabilidad;}
	public void setEsUnaHabilidad(boolean eshabilidad) {this.esUnaHabilidad = eshabilidad;}
	public boolean getEstaGanandoSP() {return estaGanandoSP;}
	public void setEstaGanandoSP(boolean esGanarSP) {this.estaGanandoSP = esGanarSP;}
	public boolean getEstaLisiado() {return estaLisiado;}
	public void setEstaLisiado(boolean estaLisiado) {this.estaLisiado = estaLisiado;}
	public boolean getEstaKO() {return estaKO;}
	public void setEstaKO(boolean estaKO) {this.estaKO = estaKO;}
	public boolean isEstaEnamorado() {return estaEnamorado;}
	public void setEstaEnamorado(boolean estaEnamorado) {this.estaEnamorado = estaEnamorado;}
	public boolean getEstaDebilitado() {return this.estaDebilitado;}
	public void setEstaDebilitado(boolean boo) {this.estaDebilitado = boo;}
	public boolean isEstaBloqueando() {return estaBloqueando;}
	public void setEstaBloqueando(boolean estaBloqueando) {this.estaBloqueando = estaBloqueando;}
	//GETTER & SETTERS MISCELANEOS///////////////////////////////////////////////
	public Zona getZona() {return zona;}
	public void setZona(Zona zona) {this.zona = zona;}
	public int getPosX() {return posX;}
	public void setPosX(int posX) {this.posX = posX;}
	public int getPosY() {return posY;}
	public void setPosY(int posY) {this.posY = posY;}
	public boolean estaEnSacudida() {return enSacudida;}
	public void setearSacudida(boolean enSacudida) {this.enSacudida = enSacudida;}
	public int getDuracionSacudida() {return duracionSacudida;}
	public void setDuracionSacudida(int duracionSacudida) {this.duracionSacudida = duracionSacudida;}
	public int getVidaPerdida() {return vidaPerdida;}
	public void setVidaPerdida(int vida) {this.vidaPerdida = vida;}
	public int getIdMate() {return idMate;}
	public void setIdMate(int idMate) {this.idMate = idMate;}
	public String[] getListaDeHabilidades() {return listaDeHabilidades;}
	public void setListaDeHabilidades(String[] habilidades) {this.listaDeHabilidades = habilidades;}
	public int getHabilidadElegida() {return habilidadElegida;}
	public void setHabilidadElegida(int habilidadElegida) {this.habilidadElegida = habilidadElegida;}
	public String getAccion() {return tipoDeAccion;}
	public void setAccion(String accion) {this.tipoDeAccion = accion;}
	public int getAlturaBarraHP() {return this.alturaDeBarraHP;}
	public void setAlturaBarraHP(int altura) {this.alturaDeBarraHP = altura;}
	public int getEscudos() {return this.escudos;}
	public void setEscudos(int cant) {this.escudos = cant;}
	public int getFaltasCometidas() {return this.faltasCometidas;}
	public void setFaltasCometidas(int cant) {this.faltasCometidas = cant;}
	public boolean getMostrarFaltas() {return this.mostrarFaltas;}
	public void setMostrarFaltas(boolean boo) {this.mostrarFaltas = boo;}
	public int getNeocreditos() {return neocreditos;}
	public void setNeocreditos(int neocreditos) {this.neocreditos = neocreditos;}
	public int getDañoCausado() {return dañoCausado;}
	public void setDañoCausado(int dañoCausado) {this.dañoCausado = dañoCausado;}
	public int getPuñosAcumulados() {return puñosAcumulados;}
	public void setPuñosAcumulados(int puñosAcumulados) {this.puñosAcumulados = puñosAcumulados;}
	public int getCargarEnamoramiento() {return cargarEnamoramiento;}
	public void setCargarEnamoramiento(int cargarEnamoramiento) {this.cargarEnamoramiento = cargarEnamoramiento;}
	public int getContador() {return contador;}
	public void setContador(int contador) {this.contador = contador;}
	//GETTERS & SETTERS STATS BASE/////////////////////////////////////////////
	public String getTipo() {return tipo;}
	public void setTipo(String st) {this.tipo = st;}
	public int getGenero() {return genero;}
	public void setGenero(int i) {this.genero = i;}
	public int getIdFaccion() {return idFaccion;}
	public void setIdFaccion(int idFaccion) {this.idFaccion = idFaccion;}
	public String getClase() {return clase;}
	public void setClase(String clase) {this.clase = clase;}
	public String getNombre() {return nombre;}
	public void setNombre(String nombre) {this.nombre = nombre;}
	public int getHP() {return hp;}
	public void setHP(int hp) {this.hp = hp;}
	public int getHPMax() {return hpMax;}
	public void setHPMax(int hpMax) {this.hpMax = hpMax;}
	public int getSP() {return sp;}
	public void setSP(int sp) {this.sp = sp;}
	public int getSPMax() {return spMax;}
	public void setSPMax(int spMax) {this.spMax = spMax;}
	public int getAtq() {return atq;}
	public void setAtq(int atq) {this.atq = atq;}
	public int getDef() {return def;}
	public int getVel() {return vel;}
	public void setVel(int vel) {this.vel = vel;}
	public void setDef(int def) {this.def = def;}
	public int getPCRT() {return pcrt;}
	public void setPCRT(int pcrt) {this.pcrt = pcrt;}
	public double getDCRT() {return dcrt;}
	public void setDCRT(double dcrt) {this.dcrt = dcrt;}
	public int getEva() {return eva;}
	public void setEva(int eva) {this.eva = eva;}
	//GETTERS & SETTERS STATS MOD//////////////////////////////////////////////
	public int getHpMaxMod() {return hpMaxMod;}
	public void setHpMaxMod(int hpMaxMod) {this.hpMaxMod = hpMaxMod;}
	public int getSpMaxMod() {return spMaxMod;}
	public void setSpMaxMod(int spMaxMod) {this.spMaxMod = spMaxMod;}
	public int getAtqMod() {return atqMod;}
	public void setAtqMod(int atqMod) {this.atqMod = atqMod;}
	public int getDefMod() {return defMod;}
	public void setDefMod(int defMod) {this.defMod = defMod;}
	public int getVelMod() {return velMod;}
	public void setVelMod(int velMod) {this.velMod = velMod;}
	public int getEvaMod() {return evaMod;}
	public void setEvaMod(int evaMod) { this.evaMod = evaMod;}
	public int getPcrtMod() { return pcrtMod;}
	public void setPcrtMod(int pcrtMod) { this.pcrtMod = pcrtMod;}
	public double getDcrtMod() { return dcrtMod;}
	public void setDcrtMod(double dcrtMod) { this.dcrtMod = dcrtMod;}
	public double getCapacidadBloqueo() {return capacidadBloqueo;}
	public void setCapacidadBloqueo(double capacidadBloqueo) {this.capacidadBloqueo = capacidadBloqueo;}
	//CD Y HABILIDADES/////////////////////////////////////////////////////////////////////
	public int getCdHabilidad1() {return cdHabilidad1;}
	public void setCdHabilidad1(int cdHabilidad1) {this.cdHabilidad1 = cdHabilidad1;}
	public int getCdHabilidad2() {return cdHabilidad2;}
	public void setCdHabilidad2(int cdHabilidad2) {this.cdHabilidad2 = cdHabilidad2;}
	public boolean isHabilidad1() {return habilidad1;}
	public void setHabilidad1(boolean habilidad1) {this.habilidad1 = habilidad1;}
	public boolean isHabilidad2() {return habilidad2;}
	public void setHabilidad2(boolean habilidad2) {this.habilidad2 = habilidad2;}
	//ESTADOS//////////////////////////////////////////////////////////////////////////////
	public boolean isPrecavido() {return precavido;}
	public void setPrecavido(boolean precavido) {this.precavido = precavido;}
	public boolean isAgresivo() {return agresivo;}
	public void setAgresivo(boolean agresivo) {this.agresivo = agresivo;}
	public boolean isAcelerado() {return acelerado;}
	public void setAcelerado(boolean acelerado) {this.acelerado = acelerado;}
	public boolean isPotenciado() {return potenciado;}
	public void setPotenciado(boolean potenciado) {this.potenciado = potenciado;}
	public boolean isEvadiendo() {
		return evadiendo;
	}
	public void setEvadiendo(boolean evadiendo) {
		this.evadiendo = evadiendo;
	}
	public boolean isRompiendo() {
		return rompiendo;
	}
	public void setRompiendo(boolean rompiendo) {
		this.rompiendo = rompiendo;
	}
	public boolean isCurando() {
		return curando;
	}
	public void setCurando(boolean curando) {
		this.curando = curando;
	}
	public int getTimerPrecavido() {
		return timerPrecavido;
	}
	public void setTimerPrecavido(int timerPrecavido) {
		this.timerPrecavido = timerPrecavido;
	}
	public int getTimerAgresivo() {
		return timerAgresivo;
	}
	public void setTimerAgresivo(int timerAgresivo) {
		this.timerAgresivo = timerAgresivo;
	}
	public int getTimerAcelerado() {
		return timerAcelerado;
	}
	public void setTimerAcelerado(int timerAcelerado) {
		this.timerAcelerado = timerAcelerado;
	}
	public int getTimerPotenciado() {
		return timerPotenciado;
	}
	public void setTimerPotenciado(int timerPotenciado) {
		this.timerPotenciado = timerPotenciado;
	}

}
