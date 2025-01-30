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

import javax.imageio.ImageIO;

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
	//INDICADORES DE SALUD Y NOMBRE//////////////////////////////////
	int barraHP = 72;
	private String dañoRecibido;
	private String curaRecibida;
	//ESTADOS Y COMPORTAMIENTOS//////////////////////////////////////
	private int idMate;
	private boolean esAliado = false;
	private boolean estaActivo = true;
	private boolean estaVivo = true;
	private boolean realizaUnCritico;
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
	private int idTez;
	private int escudos = 0;
	private int faltasCometidas = 0;
	private int neocreditos;
	private int dañoCausado = 0;
	private int puñosAcumulados;
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
	private double eva;
	private double pcrt;
	private double dcrt;
	//MODIFICADORES DE STATS////////////////////////////////////////
	private int hpMaxMod = 0;
	private int spMaxMod = 0;
	private int atqMod = 0;
	private int defMod = 0;
	private int velMod = 0;
	private double evaMod = 0;
	private double pcrtMod = 0;
	private double dcrtMod = 0;
	private int vidaPerdida = 0;
	////////////////////////////////////////////////////////////////
	public Unidad(Zona zona,boolean aliado, PanelDeJuego pdj) {
		this.pdj = pdj;
		this.dañoRecibido = "";
		this.curaRecibida = "";
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
                estaEsquivando = false;
                tomandoUnMate = false;
                esUnaHabilidad = false;
                estaGanandoSP = false;
                estaMotivado = false;
                estaDesmotivado = false;
                realizaUnCritico = false;
                estaLisiado = false;
                estaDebilitado = false;
                desplazamientoSacudidaX = 0;
                desplazamientoSacudidaY = 0;
                desplazarDañoRecibido = getPosY();
                dañoRecibido = "";
            }
        }
        if(getHP() <= 0) {
        	setAlive(false);
        }
    }	
	public void dibujar(Graphics2D g2) {
        this.g2 = g2;  
        dibujarVida();
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
        if(this.realizaUnaCuracion) {
            g2.setColor(Color.green);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16f));
            g2.drawString(curaRecibida , getPosX()+84, desplazarDañoRecibido-48);
        }
        else if(this.realizaUnCritico) {
        	g2.setColor(Color.YELLOW);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20f));
            g2.drawString(dañoRecibido, getPosX()+84, desplazarDañoRecibido-48);
        }
        else if(this.estaEsquivando) {
        	g2.setColor(Color.GRAY);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20f));
            g2.drawString("MISS!", getPosX()+84, desplazarDañoRecibido-48);
        }
        else if(this.getTomandoUnMate()) {
        	if(getIdMate() == 0) {
        		g2.setColor(Color.RED);
        		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16f));
                g2.drawString("HERVIDO", getPosX()+84, desplazarDañoRecibido-48);
                g2.setColor(Color.pink);
                g2.drawString("Y DULCE!", getPosX()+84, desplazarDañoRecibido-30);
        	}
        	if(getIdMate() == 1) {
        		g2.setColor(Color.RED);
        		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16f));
                g2.drawString("HERVIDO", getPosX()+84, desplazarDañoRecibido-48);
                g2.setColor(Color.GREEN);
                g2.drawString("Y AMARGO!", getPosX()+84, desplazarDañoRecibido-30);
        	}
        	if(getIdMate() == 2) {
        		g2.setColor(Color.BLUE);
        		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16f));
                g2.drawString("FRIO Y", getPosX()+84, desplazarDañoRecibido-48);
                g2.setColor(Color.pink);
                g2.drawString("DULCE!", getPosX()+84, desplazarDañoRecibido-30);
        	}
        	if(getIdMate() == 3) {
        		g2.setColor(Color.BLUE);
        		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16f));
                g2.drawString("FRIO Y", getPosX()+84, desplazarDañoRecibido-48);
                g2.setColor(Color.GREEN);
                g2.drawString("AMARGO!", getPosX()+84, desplazarDañoRecibido-30);
        	}
        	if(getIdMate() == 4) {
        		g2.setColor(Color.YELLOW);
        		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16f));
                g2.drawString("MATE", getPosX()+84, desplazarDañoRecibido-48);
                g2.drawString("PERFECTO!", getPosX()+84, desplazarDañoRecibido-30);
        	}
        }
        else if(this.esUnaHabilidad) {
        	g2.setColor(Color.cyan);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20f));
            g2.drawString(dañoRecibido+" IMPACT!!!", getPosX()+84, desplazarDañoRecibido-48);
        }
        else if(this.estaMotivado) {
        	g2.setColor(Color.ORANGE);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20f));
            g2.drawString("+HIGH!", getPosX()+84, desplazarDañoRecibido-48);
        }
        else if(this.estaDesmotivado) {
        	g2.setColor(Color.WHITE);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20f));
            g2.drawString(dañoRecibido, getPosX()+104, desplazarDañoRecibido-28);
            g2.setColor(Color.BLUE);
            g2.drawString("DOWN!", getPosX()+104, desplazarDañoRecibido-48);
        }
        else if(this.estaGanandoSP) {
        	g2.setColor(Color.cyan);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16f));
            g2.drawString(curaRecibida, getPosX()+84, desplazarDañoRecibido-48);
        }
        else if(this.estaLisiado) {
        	Color c = new Color(50, 100, 100);
        	g2.setColor(c);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16f));
            g2.drawString("HURT...", getPosX()+84, desplazarDañoRecibido-48);
        }
        else if(this.estaKO) {
        	Image KO = configurarImagen("/efectos/stun", 3);
        	g2.drawImage(KO, dibujarX+10, dibujarY, null);
        }
        else if(this.estaDebilitado) {
        	g2.setColor(Color.pink);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16f));
            g2.drawString("CASTIGADO!", getPosX()+84, desplazarDañoRecibido-48);
        }
        else {
        	g2.setColor(Color.WHITE);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16f));
            g2.drawString(dañoRecibido, getPosX()+84, desplazarDañoRecibido-48);
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
		if(isCritical) {
			dañoRecibido = "CRITICAL " + daño +"!";
			this.realizaUnCritico = true;
			SEId = 3;
		}
		else {
			dañoRecibido = "" + daño;
		}
		setearSacudida(true);
	    setDuracionSacudida(duracionSacudida);
		if(this.escudos > 0) {
			escudos--;
			pdj.ReproducirSE(9);
		}
		else {
			this.setHP(this.getHP() - daño);
			this.setVidaPerdida(this.getVidaPerdida() + daño);
			pdj.ReproducirSE(SEId);
		}
	    
	}
	
	public void realizarAtaque(Unidad unidad, ArrayList<Unidad> enemigos) {
		Unidad protector = this.getProtector(enemigos);
		boolean isCritical = Math.random() <= (this.getPCRT() + this.getPcrtMod());
		if(unidad != null) {
			boolean isMiss = Math.random() <= (unidad.getEva() + unidad.getEvaMod());
			int daño = Math.max(1, (this.getAtq() + this.getAtqMod()) - (unidad.getDef() + unidad.getDefMod())); 
			if(this.getClase() == "Puño Furioso" && this.getHPMax() < unidad.getHPMax()) {
				System.out.println("puño");
				daño += (unidad.getHPMax()/5);
			}	 
			if (isCritical) {
				daño *= (this.getDCRT() + this.getDcrtMod());
			}
			if(!isMiss) {
				int random = this.elegirAleatorio(2);
				if(protector != null && random == 1) {
					protector.recibirDaño(daño, isCritical, 20);
					contarFaltas(protector);
				}
				else {
					unidad.recibirDaño(daño, isCritical, 20);
					contarFaltas(unidad);
				}
				if(this.getTipo() == "Especialista") {
					this.restaurarSP(daño*2);
					estaGanandoSP = true;
					setearSacudida(true);
				    setDuracionSacudida(20);
				}
				if(this.getClase() == "Gaucho Moderno") {
					this.setPcrtMod(this.getPcrtMod()+0.05);
				}
				if(this.getIdFaccion() == 2 && this.getClase() != "Dragon Pirotecnico") {
					int NC = daño;
					if(this.getClase() == "Alumno Modelo") {
						NC = daño*3;
						
					}
					else {
						NC = this.obtenerValorEntre(1, daño);
					}
					this.setDañoCausado(NC);
					this.sumarNeocreditos(daño);
				}
			}
			else {
				unidad.evadirAtaque();
			}
			this.reflejarDaño(unidad, daño);
			this.robarVida(daño, unidad);
		}
	}

	public void realizarAtaqueEnemigo(ArrayList<Unidad> unidades) {
		Unidad protector = this.getProtector(unidades);
		Unidad objetivo = elegirObjetivo(unidades);
		boolean isCritical = Math.random() <= (this.getPCRT() + this.getPcrtMod());  
		if(objetivo != null) {
			boolean isMiss = Math.random() <= (objetivo.getEva() + objetivo.getEvaMod());
			int daño = Math.max(1, (this.getAtq() + this.getAtqMod()) - (objetivo.getDef() + objetivo.getDefMod()));
			if(this.getClase() == "Puño Furioso" && this.getHPMax() < objetivo.getHPMax()) {
				System.out.println("puño");
				daño += (objetivo.getHPMax()/5);
			} 
			if (isCritical) {
				daño *= (this.getDCRT() + this.getDcrtMod());
			}
			if(!isMiss) {
				int random = this.elegirAleatorio(2);
				if(protector != null && random == 1) {
					protector.recibirDaño(daño, isCritical, 20);
					contarFaltas(protector);
				}
				else {
					objetivo.recibirDaño(daño, isCritical, 20);
					contarFaltas(objetivo);
				}
				if(this.getTipo() == "Especialista") {
					this.restaurarSP(daño*2);
					estaGanandoSP = true;
					setearSacudida(true);
				    setDuracionSacudida(20);
				}
				if(this.getClase() == "Gaucho Moderno") {
					this.setPcrtMod(this.getPcrtMod()+0.05);
				}
				if(this.getIdFaccion() == 2 && this.getClase() != "Dragon Pirotecnico") {
					int NC = daño;
					if(this.getClase() == "Alumno Modelo") {
						NC = daño*3;
						
					}
					else {
						NC = this.obtenerValorEntre(1, daño);
					}
					this.setDañoCausado(NC);
					this.sumarNeocreditos(daño);
				}
			}
			else {
				objetivo.evadirAtaque();
			}
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
	
	public void evadirAtaque() {
		pdj.ReproducirSE(6);
		estaEsquivando = true;
		setearSacudida(true);
	    setDuracionSacudida(20);
	}
	
	public void restaurarHP(int curacion) {
		if((this.getHP() + curacion) > this.getHPMax()) {
			this.setHP(this.getHPMax());
		}
		else {
			this.setHP(this.getHP() + curacion);
		}
		curaRecibida = "+" + curacion;
	}
	
	public void restaurarSP(int energia) {
		if((this.getSP() + energia) > this.getSPMax()) {
			this.setSP(this.getSPMax());
		}
		else {
			this.setSP(this.getSP() + energia);
		}
		curaRecibida = "+" + energia;
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
	            recibirDaño(dañoActual, golpeCritico, 10);
	        }
	    }).start();
	}
	
	public void robarVida(int daño, Unidad unidad) {
		if(unidad.getEstaMarcado()) {
			if(this.getClase() == "Aspirante A Dragon") {
				this.restaurarHP(daño);
			}
			else {
				this.restaurarHP(daño-(daño/4));
			}
			this.restaurarHP(daño-(daño/4));
			this.setRealizandoCuracion(true);
			this.setDuracionSacudida(20);
			this.setearSacudida(true);
		}
	}
	
	public void contarFaltas(Unidad unidad) {
		if(unidad.getGenero() == 0) {
			this.faltasCometidas++;
		}
	}
	
	public void pasivaDeClase(ArrayList<Unidad> aliados, ArrayList<Unidad> enemigos) {
		if(!enemigos.isEmpty()) {
			for(Unidad unidad : enemigos) {
				if(unidad.getClase() == "Delegada") {
					this.setMostrarFaltas(true);
				}
			}
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
	
	public void usarHabilidadEnemigo(ArrayList<Unidad> unidades) {}	
	public void usarHabilidad(Unidad unidad, ArrayList<Unidad> unidades) {}	
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
	
	public void efectosVisualesPersonalizados(Graphics2D g2){}
	//METODOS DE ELECCION/////////////////////////////////////////////////////
	public Unidad elegirObjetivo(ArrayList<Unidad> unidades) {
	    if (!unidades.isEmpty()) {
	    	if(this.getClase() == "Puño Furioso") {
				Unidad unidadSeleccionada = null;
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
					imagenesBody[2] = configurarImagen("/imagenes/hombre/pantalon-pose",3);
					imagenesBody[1] = configurarImagen("/imagenes/hombre/camiseta-"+this.idTez, 3);
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
					imagenesBody[2] = configurarImagen("/imagenes/hombre/pantalon-pose",3);
					imagenesBody[1] = configurarImagen("/imagenes/mujer/cuerpo-top-"+this.idTez, 3);
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
	public boolean getEstaDebilitado() {return this.estaDebilitado;}
	public void setEstaDebilitado(boolean boo) {this.estaDebilitado = boo;}
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
	public double getPCRT() {return pcrt;}
	public void setPCRT(double pcrt) {this.pcrt = pcrt;}
	public double getDCRT() {return dcrt;}
	public void setDCRT(double dcrt) {this.dcrt = dcrt;}
	public double getEva() {return eva;}
	public void setEva(double eva) {this.eva = eva;}
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
	public double getEvaMod() {return evaMod;}
	public void setEvaMod(double evaMod) { this.evaMod = evaMod;}
	public double getPcrtMod() { return pcrtMod;}
	public void setPcrtMod(double pcrtMod) { this.pcrtMod = pcrtMod;}
	public double getDcrtMod() { return dcrtMod;}
	public void setDcrtMod(double dcrtMod) { this.dcrtMod = dcrtMod;}

}
