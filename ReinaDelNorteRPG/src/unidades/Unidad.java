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

import principal.Estadisticas;
import principal.Habilidades;
import principal.PanelDeJuego;
import principal.Utilidades;
import principal.Zona;

public class Unidad {
	//SISTEMA////////////////////////////////////////////////////////
	PanelDeJuego pdj;
	Graphics2D g2;
	//POSICIONAMIENTO////////////////////////////////////////////////
	private Zona zona;
	private int posX = 0;
	private int posY = 0;
	//INDICADORES DE SALUD Y TEXTO DE UNIDAD///////////////////////////
	int barraHP = 72;
	private Color colorDeMensaje;
	private Color colorDeDaño;
	private String textoDañoRecibido;
	private String textoInformativo;
	private int alturaPorClase = 0;
	private int alturaDeAccesorio = 0;
	private int alturaDeBarraHP = 0;
	//VARIABLES DE HABILIDADES///////////////////////////////////////
	private String[] listaDeHabilidades = new String[1];
	private String tipoDeAccion = "";
	private int habilidadElegida = -1;
	private int cdHabilidad1 = 0;
	private int cdHabilidad2 = 0;
	private boolean habilidad1 = true;
	private boolean habilidad2 = true;
	private boolean objetivoUnico = true;
	//ESTADOS Y COMPORTAMIENTOS//////////////////////////////////////
	private boolean esAliado = false;
	private boolean estaActivo = true;
	private boolean estaVivo = true;
	private boolean mostrarFaltas = false;
	private boolean estaStun;
	private boolean estaEnamorado;
	private boolean marcado;
	private boolean EfectoDeEstado;
	private boolean dañoEspecial;
	//ACCIONES//////////////////////////////////////////////////////
	private boolean realizaUnCritico;
	//TIMERS DE ESTADOS/////////////////////////////////////////////
	private int timerPrecavido = -1;
	private int timerAgresivo = -1;
	private int timerAcelerado = -1;
	private int timerPotenciado = -1;
	private int timerSangrando = -1;
	private int timerMotivado = -1;
	private int timerRdcDefAcc = -1;
	private int timerLisiado = -1;
	private int timerIncendiado = -1;
	private int timerMarcado = -1;
	private int timerJuzgado = -1;
	private int timerTendencia = -1;
	private int timerDesmoralizar = -1;
	//ELEMENTOS DE ESTADOS///////////////////////////////////////////
	private int valorSangrado;
	private int rdcDefAcc = 0;
	//VARIABLES PARA LA SACUDIDA/////////////////////////////////////
	private boolean enSacudida = false;
	private int duracionSacudida = 0;
	private int desplazamientoSacudidaX = 0;
	private int desplazamientoSacudidaY = 0;
	private int desplazarDañoRecibido;
	//MOVIMIENTO DE UNIDAD//////////////////////////////////////////
	private int direccion = 1;
	private int tiempoMov = 20;
	private int imageMov = 0;
    //ELEMENTOS DE LA UNIDAD////////////////////////////////////////
	public BufferedImage[] imagenesBody = new BufferedImage[5];
	public BufferedImage[] imagenesDead = new BufferedImage[5];
	private int idFaccion;
	private int genero;
	private int idTez = 0;
	private String nombre;
	private String clase;
	private String tipo;
	private Unidad unidadObjetivoEnemigo = null;
	private String nombreDeUltimoObjetivo = "";
	private int cantAliadas;
	private int cantEnemigas;
	//IMAGENES DE LA UNIDAD////////////////////////////////////////
	Image piso = configurarImagen("/efectos/floor", 4);
	//ACUMULADORES DE LA UNIDAD////////////////////////////////////
	private int escudos = 0;
	private int faltasCometidas = 0;
	private int neocreditos = 0;
	private int neoCreditosRecientes = 0;
	private int dañoCausado = 0;
	private int puñosAcumulados = 0;
	private int cargarEnamoramiento = 0;
	//ESTADISTICAS DE LA UNIDAD/////////////////////////////////////
	private int hp;
	private int hpMax;
	private int sp;
	private int spMax;
	private int atq;
	private int def;
	private int vel;
	private int eva;
	private int pcrt;
	private int bloq;
	private double dcrt;
	//MODIFICADORES DE STATS////////////////////////////////////////
	private int hpMaxMod = 0;
	private int spMaxMod = 0;
	private int atqMod = 0;
	private int defMod = 0;
	private int velMod = 0;
	private int evaMod = 0;
	private int pcrtMod = 0;
	private int bloqMod = 0;
	private double dcrtMod = 0;
	////////////////////////////////////////////////////////////////
	public Unidad(Zona zona,boolean aliado, PanelDeJuego pdj) {
		this.pdj = pdj;
		this.textoInformativo = "";
		this.textoDañoRecibido = "";
		this.setZona(zona);
		this.setPosX(zona.x);
		this.setPosY(zona.y);
		this.setAliado(aliado);
		this.genero = elegirAleatorio(2);
		this.desplazarDañoRecibido = aliado ? 384 : 96;
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
                realizaUnCritico = false;
                desplazamientoSacudidaX = 0;
                desplazamientoSacudidaY = 0;
                desplazarDañoRecibido = getPosY();
                textoInformativo = "";
                textoDañoRecibido = "";
                cancelarTextoDeEfectos();
            }
        }
        if(getHP() <= 0) {
        	setAlive(false);
        }
    }	
	//DIBUJAR////////////////////////////////////////////////////////////////////////////////
	public void dibujar(Graphics2D g2) {
        this.g2 = g2; 
        g2.drawImage(piso, posX, posY+16, null);
        dibujarVida();
        if(isEstaEnamorado()) {dibujarCorazon(g2);}
        dibujarEscudos(g2);
        if(getMostrarFaltas()) {dibujarFaltas(g2);}
        int dibujarX = getPosX() + desplazamientoSacudidaX;
        int dibujarY = getPosY() + desplazamientoSacudidaY;
        if(isAlive()) {
        	mostrarImagenes(g2, dibujarX+10, dibujarY-20+getAlturaPorClase(), imageMov);
        } 
        else {
        	mostrarMuerte(g2, dibujarX+10, dibujarY-20, imageMov);
        }
        //MOSTRAR DAÑO RECIBIDO Y EFECTOS////////////////////////////
        if(this.getTimerMarcado() > 0) {
        	Image marca = configurarImagen("/efectos/mark", 4);
        	g2.drawImage(marca, dibujarX, dibujarY, null);
        }
        if(this.isEfectoDeEstado()) {
        	g2.setColor(colorDeMensaje);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20f));
            g2.drawString(this.getTextoInformativo(), getPosX()+84, desplazarDañoRecibido-48);
        }
        else if(this.realizaUnCritico) {
        	g2.setColor(Color.YELLOW);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20f));
            g2.drawString(this.getTextoDañoRecibido(), getPosX()+84, desplazarDañoRecibido-48);
        }
        else if(this.isDañoEspecial()) {
        	g2.setColor(colorDeDaño);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16f));
            g2.drawString(this.getTextoDañoRecibido() , getPosX()+84, desplazarDañoRecibido-48);
        }
        else {
        	g2.setColor(Color.WHITE);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16f));
            g2.drawString(this.getTextoDañoRecibido(), getPosX()+84, desplazarDañoRecibido-48);
        }
        if(this.getEstaStun()) {
        	Image KO = configurarImagen("/efectos/stun", 3);
        	g2.drawImage(KO, dibujarX+10, dibujarY, null);
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

	public void recibirDaño(int damage, boolean isCritical, Unidad unidad) {
		int daño = damage;
		if(this.getHPMax() > unidad.getHPMax() && unidad.getClase() == "Puño Furioso") {
			daño += (this.getHPMax()-unidad.getHPMax())/4;
		}
	    int SEId = 2;
	    int reduccion = (int) (daño * ((this.getDef() + this.getDefMod()) / 100.0));
        int dañoFinal = Math.max(1, daño - reduccion);
        String textoMostrado = "";
	    if (this.elegirAleatorio(100) < (this.getEva() + this.getEvaMod()) && unidad.getClase() != "Dragon Pirotecnico") {
	    	pdj.ReproducirSE(6);
	        textoMostrado = "MISS!";
	        Habilidades.setearEfectoDeEstado(this, textoMostrado, Color.white);
	        Habilidades.ganarNeoCreditos(this, 10);
	    }
	    else {
	    	if(this.elegirAleatorio(100) < (this.getBloq() + this.getBloqMod())) {
	    		pdj.ReproducirSE(9);
	    		this.setHP(this.getHP() - (dañoFinal/4));
		        textoMostrado = "BLOCK!";
		        Habilidades.setearEfectoDeEstado(this, textoMostrado, Color.white);
		        Habilidades.ganarNeoCreditos(this, 12);
	    	}
	    	else if (this.escudos > 0) {
		        escudos--;
		        pdj.ReproducirSE(9);
		        textoMostrado = "BREAK!";
		        Habilidades.setearEfectoDeEstado(this, textoMostrado, Color.white);
		        Habilidades.ganarNeoCreditos(unidad, 7);
		        contarFaltas(unidad, 1);
		    } else {
		    	if (isCritical) {
			    	SEId = 3;
			    	this.realizaUnCritico = true;
			        textoMostrado = "CRITICAL " + dañoFinal + "!";
			        Habilidades.setearDaño(this, textoMostrado, Color.yellow);
			    } else {
			        textoMostrado = "" + dañoFinal;
			        Habilidades.setearDaño(this, textoMostrado, Color.white);
			    }
		        this.setHP(this.getHP() - dañoFinal);
		        pdj.ReproducirSE(SEId);
		        Habilidades.ganarNeoCreditos(unidad, 5);
		        unidad.robarVida(dañoFinal, this);
		        if(unidad.getClase() == "Novata Timida") {
		        	Habilidades.maldicionAleatoria(this);
		        }
		        contarFaltas(unidad, 1);
		    }
	    }  
	}
	
	public void realizarAtaque(Unidad unidad, ArrayList<Unidad> enemigos) {
		Unidad defensor = getDefensor(enemigos);
		this.setNombreDeUltimoObjetivo(unidad.getClase());
		boolean isCritical = Math.random() <= ((this.getPCRT() + this.getPcrtMod()) / 100.0);
		if(unidad != null) {
			int daño = this.getAtq() + this.getAtqMod();
			if (isCritical) {
				daño *= (this.getDCRT() + this.getDcrtMod());
			}
			if(defensor != null) {
				if(defensor.elegirAleatorio(100) < (defensor.getBloq() + defensor.getBloqMod())) {
					defensor.recibirDaño(daño, isCritical, this);
				}
				else {
					unidad.recibirDaño(daño, isCritical, this);
				}
			}
			else {
				unidad.recibirDaño(daño, isCritical, this);
			}
			this.reflejarDaño(unidad, daño);
		}
	}
	
	public Unidad getDefensor(ArrayList<Unidad> unidades) {
		if(!unidades.isEmpty()) {
			for(Unidad unidad : unidades) {
				if(unidad.getClase() == "Influencer") {
					return unidad;
				}
			}
		}
		return null;
	}

	public void realizarAtaqueEnemigo(ArrayList<Unidad> unidades) {
		Unidad defensor = getDefensor(unidades);
		Unidad objetivo = elegirObjetivo(unidades);
		if(!unidades.isEmpty()) {
			this.setNombreDeUltimoObjetivo(objetivo.getClase());
		}
		boolean isCritical = Math.random() <= ((this.getPCRT() + this.getPcrtMod()) / 100.0);  
		if(objetivo != null) {
			int daño = this.getAtq() + this.getAtqMod();
			if (isCritical) {
				daño *= (this.getDCRT() + this.getDcrtMod());
			}
			if(defensor != null) {
				if(defensor.elegirAleatorio(100) < (defensor.getBloq() + defensor.getBloqMod())) {
					defensor.recibirDaño(daño, isCritical, this);
				}
				else {
					objetivo.recibirDaño(daño, isCritical, this);
				}
			}
			else {
				objetivo.recibirDaño(daño, isCritical, this);
			}
			this.reflejarDaño(objetivo, daño);
			if(this.getClase() == "Dragon Pirotecnico") {
				this.setUnidadObjetivoEnemigo(objetivo);
			}
		}
	}
	
	public void reflejarDaño(Unidad unidad, int daño) {
		if(unidad.getClase() == "Shaolin Escolar" || unidad.getClase() == "Aspirante A Dragon") {
			int i = (daño/2);
			if(i > 1) {
				this.recibirDaño(i, false, this);
			}
			else {
				this.recibirDaño(1, false, this);
			}
		}
	}
	
	public void recibirGolpesMúltiples(int daño,int cant, boolean isCritical, Unidad unidad) {
	    int numGolpes = cant;
	    new Thread(() -> {
	        for (int i = 0; i < numGolpes; i++) {
	            try {
	                Thread.sleep(300);
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
	            this.recibirDaño(dañoActual, golpeCritico, unidad);
	        }
	    }).start();
	}
	
	public void robarVida(int daño, Unidad unidad) {
		Color c = new Color(255, 255, 0);
		if(unidad.getTimerMarcado() > 0) {
			Habilidades.restaurarHPPlano(this, daño/2);
			Habilidades.setearEfectoDeEstado(this, "+" + daño/2, c);
		}
	}
	
	public void contarFaltas(Unidad unidad, int cant) {
		if(this.getGenero() == 0) {
			Estadisticas.aumentarFaltas(unidad, cant);
		}
	}
	
	public void pasivaDeClase(ArrayList<Unidad> aliados, ArrayList<Unidad> enemigos) {
		iniciarTimersDeEstado();
		actualizarTimer(this::getTimerJuzgado, this::setTimerJuzgado, () -> Habilidades.cancelarPenalizarUnidad(this, aliados, enemigos));
		if(this.getNombreDeUltimoObjetivo() == "Novata Timida") {
			Habilidades.penalizarUnidad(this, aliados, enemigos);
			this.setNombreDeUltimoObjetivo("");
		}
		Habilidades.restaurarSP(this, 5);
		Habilidades.repartirNeoCreditos(aliados, this);
		if(!enemigos.isEmpty()) {
			for(Unidad unidad : enemigos) {
				if(unidad.getClase() == "Delegada") {
					System.out.println();
					this.setMostrarFaltas(true);
				}
			}
		}
	}
	
	public void iniciarTimersDeEstado() {
		actualizarHemorragia();
		actualizarIncendiar();
	    actualizarTimer(this::getTimerPrecavido, this::setTimerPrecavido, () -> Habilidades.cancelarAumentarProteccion(this));
	    actualizarTimer(this::getTimerAgresivo, this::setTimerAgresivo, () -> Habilidades.cancelarAumentarAgresividad(this));
	    actualizarTimer(this::getTimerAcelerado, this::setTimerAcelerado, () -> Habilidades.cancelarAumentarAgilidad(this));
	    actualizarTimer(this::getTimerPotenciado, this::setTimerPotenciado, () -> Habilidades.cancelarPotenciarUnidad(this));
	    actualizarTimer(this::getTimerMotivado, this::setTimerMotivado, () -> Habilidades.cancelarMotivarUnidad(this));
	    actualizarTimer(this::getTimerLisiado, this::setTimerLisiado, () -> Habilidades.cancelarDestruirMovilidad(this));
	    actualizarTimer(this::getTimerMarcado, this::setTimerMarcado, () -> Habilidades.desmarcarUnidad(this));
	    actualizarTimer(this::getTimerTendencia, this::setTimerTendencia, () -> Habilidades.cancelarTendencia(this));
	    actualizarTimer(this::getTimerDesmoralizar, this::setTimerDesmoralizar, () -> Habilidades.cancelarDesmoralizarUnidad(this));
	}

	private void actualizarTimer(Supplier<Integer> getter, Consumer<Integer> setter, Runnable habilidad) {
	    if (getter.get() > 0) {
	        setter.accept(getter.get() - 1);
	    } else if (getter.get() == 0) {
	        habilidad.run();
	        setter.accept(-1);
	    }
	}
	
	private void actualizarHemorragia() {
		if(this.getTimerSangrando() > 0) {
			this.setTimerSangrando(this.getTimerSangrando() - 1);
			Habilidades.provocarHemorragia(this, pdj);
		}
	}
	
	private void actualizarIncendiar() {
		if(this.getTimerIncendiado() > 0) {
			this.setTimerIncendiado(this.getTimerIncendiado() - 1);
			Habilidades.provocarIncendiar(this, pdj);
		}
	}
	
	public void usarHabilidadOfensiva(Unidad unidad, boolean puedeEsquivar, boolean puedeBloquear, int dañoAdicional, Runnable habilidad) { 
	    int daño = this.getAtq() + this.getAtqMod() + dañoAdicional;
	    int reduccion = (int) (daño * ((unidad.getDef() + unidad.getDefMod()) / 100.0));
	    int dañoFinal = Math.max(1, daño - reduccion);
	    if (puedeEsquivar && unidad.elegirAleatorio(100) < (unidad.getEva() + unidad.getEvaMod())) {
	        pdj.ReproducirSE(6);
	        Habilidades.setearEfectoDeEstado(unidad, "MISS!", Color.white);
	    } else {
	        if (puedeBloquear && this.elegirAleatorio(100) < (unidad.getBloq() + unidad.getBloqMod())) {
	            pdj.ReproducirSE(9);
	            unidad.setHP(unidad.getHP() - (dañoFinal / 4));
	            Habilidades.setearEfectoDeEstado(unidad, "BLOCK!", Color.white);
	        } else if (unidad.getEscudos() > 0) {
	            unidad.setEscudos(unidad.getEscudos() - 1);
	            pdj.ReproducirSE(9);
	            Habilidades.setearEfectoDeEstado(unidad, "BREAK!", Color.white);
	        } else {
	            unidad.setHP(unidad.getHP() - dañoFinal);
	            pdj.ReproducirSE(3);
	            habilidad.run();
	        }
	    }
	}
	
	public void usarHabilidadEnemigo(ArrayList<Unidad> unidades) {}	
	public void usarHabilidad(Unidad unidad, ArrayList<Unidad> unidades) {}
	//METODOS VARIOS///////////////////////////////////////////////////////
	public void cancelarTextoDeEfectos() {
		this.setEfectoDeEstado(false);
		this.setDañoEspecial(false);
	}
	
	public int porcentajeHP(int valor) {
	    return ((valor / this.getHPMax()) * 100);
	}
	
	public int porcentajeSP(int valor) {
	    return ((valor / this.getSPMax()) * 100);
	}
	//METODOS VISUALES/////////////////////////////////////////////////////////
	public void dibujarVida() {
	    g2.setFont(g2.getFont().deriveFont(Font.BOLD, 15f));
	    g2.setColor(Color.white);
	    
	    String nombreCompleto = getClase();
	    int hp = calcularBarraHP();
	    int altura = -pdj.tamañoDeBaldosa - (pdj.tamañoDeBaldosa / 8) + 16;
	    int posX = getPosX() + 10;
	    int posY = getPosY() - 10 + altura + this.getAlturaDeBarraHP();

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
	    g2.fillRoundRect(posX, getPosY() + altura + this.getAlturaDeBarraHP(), barraHP, pdj.tamañoDeBaldosa / 5, 5, 5);

	    g2.setColor(Color.red);
	    g2.fillRoundRect(posX, getPosY() + altura + this.getAlturaDeBarraHP(), hp, pdj.tamañoDeBaldosa / 5, 5, 5);

	    c = new Color(255, 255, 255);
	    g2.setColor(c);
	    g2.setStroke(new BasicStroke(2));
	    g2.drawRoundRect(posX, getPosY() + altura + this.getAlturaDeBarraHP(), barraHP, pdj.tamañoDeBaldosa / 5, 5, 5);
	}
	
	public void dibujarEscudos(Graphics2D g2) {
	    int altura = -pdj.tamañoDeBaldosa - (pdj.tamañoDeBaldosa / 8) + 16;
	    int posX = getPosX() + 85;
	    int posY = getPosY() - 8 + altura + this.getAlturaDeBarraHP();

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
	    int posY = getPosY() + 25 + altura + this.getAlturaDeBarraHP();
	    
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
	    int posY = getPosY() + altura + this.getAlturaDeBarraHP();
	    Image corazon = configurarImagen("/efectos/heart", 4);
	    g2.drawImage(corazon, posX, posY+this.getAlturaDeAccesorio()-20+imageMov*2, null);
	}	
	
	public void efectosVisualesPersonalizados(Graphics2D g2){}
	
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
			this.setIdTez(obtenerValorEntre(1,3));
		}
		else if(this.idFaccion == 2) {
			this.setIdTez(obtenerValorEntre(4,5));
		}
		else if(this.idFaccion == 4) {
			if(this.clase != "Novata Cauta" && this.clase != "Novata Confiable") {
				this.setIdTez(obtenerValorEntre(1,5));
			}
		}
		else {
			this.setIdTez(obtenerValorEntre(1,5));
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
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void generarCuerpo() {
		pdj.idr.cargarImagenesDeUnidad(this);
	}
	
	public void cargarDummyMuerto() {
		pdj.idr.cargarImagenesDeDummyMuerto(this);
	}
	
	public void mostrarImagenes(Graphics2D g2, int posX, int posY, int dezplazamiento) {
		g2.drawImage(imagenesBody[2], posX, posY+46, null);
		g2.drawImage(imagenesBody[1], posX, posY+28+dezplazamiento, null);
		g2.drawImage(imagenesBody[0], posX, posY+dezplazamiento*2, null);
		g2.drawImage(imagenesBody[4], posX-1, posY+this.getAlturaDeAccesorio()-20+dezplazamiento*2, null);
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
	public String[] getListaDeHabilidades() {return listaDeHabilidades;}
	public void setListaDeHabilidades(String[] habilidades) {this.listaDeHabilidades = habilidades;}
	public int getHabilidadElegida() {return habilidadElegida;}
	public void setHabilidadElegida(int habilidadElegida) {this.habilidadElegida = habilidadElegida;}
	public int getCdHabilidad1() {return cdHabilidad1;}
	public void setCdHabilidad1(int cdHabilidad1) {this.cdHabilidad1 = cdHabilidad1;}
	public int getCdHabilidad2() {return cdHabilidad2;}
	public void setCdHabilidad2(int cdHabilidad2) {this.cdHabilidad2 = cdHabilidad2;}
	public boolean isHabilidad1() {return habilidad1;}
	public void setHabilidad1(boolean habilidad1) {this.habilidad1 = habilidad1;}
	public boolean isHabilidad2() {return habilidad2;}
	public void setHabilidad2(boolean habilidad2) {this.habilidad2 = habilidad2;}
	public String getAccion() {return tipoDeAccion;}
	public void setAccion(String accion) {this.tipoDeAccion = accion;}
	public int getAlturaBarraHP() {return this.getAlturaDeBarraHP();}
	public void setAlturaBarraHP(int altura) {this.setAlturaDeBarraHP(altura);}
	public String getTextoInformativo() {return textoInformativo;}
	public void setTextoInformativo(String textoInformativo) {this.textoInformativo = textoInformativo;}
	public String getTextoDañoRecibido() {return textoDañoRecibido;}
	public void setTextoDañoRecibido(String textoDañoRecibido) {this.textoDañoRecibido = textoDañoRecibido;}
	public boolean isObjetivoUnico() {return objetivoUnico;}
	public void setObjetivoUnico(boolean singleTarget) {this.objetivoUnico = singleTarget;}
	public int getIdTez() {return idTez;}
	public void setIdTez(int idTez) {this.idTez = idTez;}
	public boolean getMostrarFaltas() {return this.mostrarFaltas;}
	public void setMostrarFaltas(boolean boo) {this.mostrarFaltas = boo;}
	public Unidad getUnidadObjetivoEnemigo() {return unidadObjetivoEnemigo;}
	public void setUnidadObjetivoEnemigo(Unidad unidadObjetivoEnemigo) {this.unidadObjetivoEnemigo = unidadObjetivoEnemigo;}
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
	public Color getColorDeMensaje() {return colorDeMensaje;}
	public void setColorDeMensaje(Color colorDeMensaje) {this.colorDeMensaje = colorDeMensaje;}
	public Color getColorDeDaño() {return colorDeDaño;}
	public void setColorDeDaño(Color colorDeDaño) {this.colorDeDaño = colorDeDaño;}
	public boolean isEfectoDeEstado() {return EfectoDeEstado;}
	public void setEfectoDeEstado(boolean efectoDeEstado) {EfectoDeEstado = efectoDeEstado;}
	public boolean isDañoEspecial() {return dañoEspecial;}
	public void setDañoEspecial(boolean dañoEspecial) {this.dañoEspecial = dañoEspecial;}
	public void setValorSangrado(int valorSangrado) {this.valorSangrado = valorSangrado;}
	public int getRdcDefAcc() {return rdcDefAcc;}
	public void setRdcDefAcc(int rdcDefAcc) {this.rdcDefAcc = rdcDefAcc;}
	public int getAlturaPorClase() {return alturaPorClase;}
	public void setAlturaPorClase(int alturaPorClase) {this.alturaPorClase = alturaPorClase;}
	public int getAlturaDeAccesorio() {return alturaDeAccesorio;}
	public void setAlturaDeAccesorio(int alturaDeAccesorio) {this.alturaDeAccesorio = alturaDeAccesorio;}
	public int getAlturaDeBarraHP() {return alturaDeBarraHP;}
	public void setAlturaDeBarraHP(int alturaDeBarraHP) {this.alturaDeBarraHP = alturaDeBarraHP;}
	public String getNombreDeUltimoObjetivo() {
		return nombreDeUltimoObjetivo;
	}
	public void setNombreDeUltimoObjetivo(String nombreDeUltimoObjetivo) {
		this.nombreDeUltimoObjetivo = nombreDeUltimoObjetivo;
	}
	public int getCantEnemigas() {
		return cantEnemigas;
	}
	public void setCantEnemigas(int cantEnemigas) {
		this.cantEnemigas = cantEnemigas;
	}
	public int getCantAliadas() {
		return cantAliadas;
	}
	public void setCantAliadas(int cantAliadas) {
		this.cantAliadas = cantAliadas;
	}
	//G&S DE ESTADOS///////////////////////////////////////////////////////////
	public boolean isAliado() {return esAliado;}
	public void setAliado(boolean aliado) {this.esAliado = aliado;}
	public boolean isAlive() {return estaVivo;}
	public void setAlive(boolean vivo) {this.estaVivo = vivo;}
	public boolean isEstaActivo() {return estaActivo;}
	public void setEstaActivo(boolean estaActivo) {this.estaActivo = estaActivo;}
	public boolean getEstaStun() {return estaStun;}
	public void setEstaStun(boolean estaKO) {this.estaStun = estaKO;}
	public boolean isEstaEnamorado() {return estaEnamorado;}
	public void setEstaEnamorado(boolean estaEnamorado) {this.estaEnamorado = estaEnamorado;}
	public boolean isMarcado() {return marcado;}
	public void setMarcado(boolean marcado) {this.marcado = marcado;}
	//G&S ESTADISTICAS/////////////////////////////////////////////////////////
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
	public int getBloq() {return bloq;}
	public void setBloq(int capacidadBloqueo) {this.bloq = capacidadBloqueo;}
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
	public int getBloqMod() {return bloqMod;}
	public void setBloqMod(int blockMod) {this.bloqMod = blockMod;}
	//G&S TIMERS///////////////////////////////////////////////////////////////
	public int getTimerPrecavido() {return timerPrecavido;}
	public void setTimerPrecavido(int timerPrecavido) {this.timerPrecavido = timerPrecavido;}
	public int getTimerAgresivo() {return timerAgresivo;}
	public void setTimerAgresivo(int timerAgresivo) {this.timerAgresivo = timerAgresivo;}
	public int getTimerAcelerado() {return timerAcelerado;}
	public void setTimerAcelerado(int timerAcelerado) {this.timerAcelerado = timerAcelerado;}
	public int getTimerPotenciado() {return timerPotenciado;}
	public void setTimerPotenciado(int timerPotenciado) {this.timerPotenciado = timerPotenciado;}
	public int getTimerSangrando() {return timerSangrando;}
	public void setTimerSangrando(int timerSangrando) {this.timerSangrando = timerSangrando;}
	public int getTimerMotivado() {return timerMotivado;}
	public void setTimerMotivado(int timerMotivado) {this.timerMotivado = timerMotivado;}
	public int getTimerRdcDefAcc() {return timerRdcDefAcc;}
	public void setTimerRdcDefAcc(int timerRdcDefAcc) {this.timerRdcDefAcc = timerRdcDefAcc;}
	public int getTimerLisiado() {return timerLisiado;}
	public void setTimerLisiado(int timerLisiado) {this.timerLisiado = timerLisiado;}
	public int getValorSangrado() {return valorSangrado;}
	public int getTimerIncendiado() {return timerIncendiado;}
	public void setTimerIncendiado(int timerIncendiado) {this.timerIncendiado = timerIncendiado;}
	public int getTimerMarcado() {return timerMarcado;}
	public void setTimerMarcado(int timerMarcado) {this.timerMarcado = timerMarcado;}
	public int getTimerJuzgado() {
		return timerJuzgado;
	}
	public void setTimerJuzgado(int timerJuzgado) {
		this.timerJuzgado = timerJuzgado;
	}
	public int getTimerTendencia() {
		return timerTendencia;
	}
	public void setTimerTendencia(int timerTendencia) {
		this.timerTendencia = timerTendencia;
	}
	public int getTimerDesmoralizar() {
		return timerDesmoralizar;
	}
	public void setTimerDesmoralizar(int timerDesmoralizar) {
		this.timerDesmoralizar = timerDesmoralizar;
	}
	//G&S ACUMULADORES/////////////////////////////////////////////////////////
	public int getEscudos() {return this.escudos;}
	public void setEscudos(int cant) {this.escudos = cant;}
	public int getFaltasCometidas() {return this.faltasCometidas;}
	public void setFaltasCometidas(int cant) {this.faltasCometidas = cant;}
	public int getNeocreditos() {return neocreditos;}
	public void setNeocreditos(int neocreditos) {this.neocreditos = neocreditos;}
	public int getNeoCreditosRecientes() {return neoCreditosRecientes;}
	public void setNeoCreditosRecientes(int neoCreditosRecientes) {this.neoCreditosRecientes = neoCreditosRecientes;}
	public int getDañoCausado() {return dañoCausado;}
	public void setDañoCausado(int dañoCausado) {this.dañoCausado = dañoCausado;}
	public int getPuñosAcumulados() {return puñosAcumulados;}
	public void setPuñosAcumulados(int puñosAcumulados) {this.puñosAcumulados = puñosAcumulados;}
	public int getCargarEnamoramiento() {return cargarEnamoramiento;}
	public void setCargarEnamoramiento(int cargarEnamoramiento) {this.cargarEnamoramiento = cargarEnamoramiento;}
	///////////////////////////////////////////////////////////////////////////

}
