package unidades;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

import principal.GeneradorDeNombres;
import principal.PanelDeJuego;
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
	private boolean aliado = false;
	private boolean activo = true;
	private boolean vivo = true;
	private boolean esCritico;
	private boolean esCurar;
	private boolean esEsquivado;
	private boolean habilidadOn = false;
	private int duracionHabilidad = 50;
	//VARIABLES PARA LA SACUDIDA/////////////////////////////////////
	private boolean enSacudida = false;
	private int duracionSacudida = 0; // Duración en frames
	private int desplazamientoSacudidaX = 0;
	private int desplazamientoSacudidaY = 0;
	private int desplazarDañoRecibido;
    //ESTADISTICAS DE LA UNIDAD//////////////////////////////////////
	private String nombre;
	private String clase;
	private int idFaccion;
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
	////////////////////////////////////////////////////////////////
	public Unidad(Zona zona,boolean aliado, PanelDeJuego pdj) {
		this.pdj = pdj;
		this.zona = zona;
		this.dañoRecibido = "";
		this.curaRecibida = "";
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
	}
	//METODOS PRINCIPALES///////////////////////////////////////////
	public void actualizar() {
        if (enSacudida) {
            if (duracionSacudida > 0) {
                desplazamientoSacudidaX = (int) (Math.random() * 10 - 5);
                desplazamientoSacudidaY = (int) (Math.random() * 10 - 5);
                duracionSacudida--;
                desplazarDañoRecibido--;
                
            } else {
                enSacudida = false;
                esCurar = false;
                esEsquivado = false;
                desplazamientoSacudidaX = 0;
                desplazamientoSacudidaY = 0;
                desplazarDañoRecibido = getPosY();
                dañoRecibido = "";
                esCritico = false;
            }
        }
        if(getHP() <= 0) {
        	setAlive(false);
        }
        if(habilidadOn) {
        	if(duracionHabilidad > 0) {
        		duracionHabilidad--;
        	}
        	else {
        		duracionHabilidad = 50;
        		habilidadOn = false;
        	}
        }
    }
	
	public void dibujar(Graphics2D g2) {
        this.g2 = g2;
        
        dibujarVida();
        
        if (isAliado()) {
            g2.setColor(Color.BLUE);
        } else {
            g2.setColor(Color.RED);
        }
        if(!isAlive()) {
        	g2.setColor(Color.YELLOW);
        }
        int dibujarX = getPosX() + desplazamientoSacudidaX;
        int dibujarY = getPosY() + desplazamientoSacudidaY;
        g2.fillRect(dibujarX + 24, dibujarY - 24, pdj.tamañoDeBaldosa, pdj.tamañoDeBaldosa * 2);
        
        if(isHabilidadOn()) {
        	dibujarEfecto(g2);
        }
        
        //MOSTRAR DAÑO RECIBIDO////////////////////////////
        if(this.esCurar) {
            g2.setColor(Color.green);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16f));
            g2.drawString(curaRecibida , getPosX()+84, desplazarDañoRecibido-48);
        }
        else if(this.esCritico) {
        	g2.setColor(Color.YELLOW);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20f));
            g2.drawString(dañoRecibido, getPosX()+84, desplazarDañoRecibido-48);
        }
        else if(this.esEsquivado) {
        	g2.setColor(Color.GRAY);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 20f));
            g2.drawString("MISS!", getPosX()+84, desplazarDañoRecibido-48);
        }
        else {
        	g2.setColor(Color.WHITE);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16f));
            g2.drawString(dañoRecibido, getPosX()+84, desplazarDañoRecibido-48);
        }

        
    }
	//METODOS DE ACCION///////////////////////////////////////////////////////
	public void realizarAccion(ArrayList<Unidad> enemigos, ArrayList<Unidad> aliados) {
		int accion = elegirAleatorio(4);
		if(accion == 0 && puedeUsarHabilidad()) {
			usarHabilidadEnemigo(aliados);
		}
		else {
			realizarAtaqueEnemigo(enemigos);
		}
	}
	
	public void recibirDaño(int daño, boolean isCritical) {
		int SEId = 2;
		this.setHP(this.getHP() - daño);
		if(isCritical) {
			dañoRecibido = "CRITICAL " + daño +"!";
			this.esCritico = true;
			SEId = 3;
		}
		else {
			dañoRecibido = "" + daño;
		}

	    enSacudida = true;
	    duracionSacudida = 20;
	    pdj.ReproducirSE(SEId);
	    
	}
	
	public void realizarAtaque(Unidad unidad) {
		boolean isCritical = Math.random() <= (this.getPCRT() + this.getPcrtMod());
		boolean isMiss = Math.random() <= (unidad.getEva() + unidad.getEvaMod());	 
		int daño = Math.max(1, (this.getAtq() + this.getAtqMod()) - (unidad.getDef() + unidad.getDefMod())); 	 
		if (isCritical) {
			daño *= (this.dcrt + this.getDcrtMod());
		}
		if(!isMiss) {
			unidad.recibirDaño(daño, isCritical);
		}	
	}
	
	public void realizarAtaqueEnemigo(ArrayList<Unidad> unidades) {
		Unidad objetivo = elegirObjetivo(unidades);
		boolean isCritical = Math.random() <= (this.getPCRT() + this.getPcrtMod());
		boolean isMiss = Math.random() <= (objetivo.getEva() + objetivo.getEvaMod());
	    
		if(objetivo != null) {
			int daño = Math.max(1, (this.getAtq() + this.getAtqMod()) - (objetivo.getDef() + objetivo.getDefMod()));
	    	 
			if (isCritical) {
				daño *= (this.getDCRT() + this.getDcrtMod());
			}
			if(!isMiss) {
				objetivo.recibirDaño(daño, isCritical);
			}
			else {
				objetivo.evadirAtaque();
			}
		}
	}
	
	public void evadirAtaque() {
		pdj.ReproducirSE(6);
		esEsquivado = true;
		enSacudida = true;
	    duracionSacudida = 20;
	}
	
	public void restaurarHP(int curacion) {
		if((this.getHP() + curacion) > this.getHPMax()) {
			this.setHP(this.getHPMax());
		}
		else {
			this.setHP(this.getHP() + curacion);
		}
		curaRecibida = "+" + curacion;
		esCurar = true;
		enSacudida = true;
	    duracionSacudida = 20;
	    pdj.ReproducirSE(4);
	}
	
	public void usarHabilidadEnemigo(ArrayList<Unidad> unidades) {}
	
	public void usarHabilidad(Unidad unidad) {}
	
	//METODOS VISUALES/////////////////////////////////////////////////////////
	public void dibujarVida() {
	    g2.setFont(g2.getFont().deriveFont(Font.BOLD, 15f));
	    g2.setColor(Color.white);
	    
	    String nombreCompleto = getClase();
	    int hp = calcularBarraHP();
	    int altura = -pdj.tamañoDeBaldosa - (pdj.tamañoDeBaldosa / 8);
	    int posX = getPosX() + 10;
	    int posY = getPosY() - 10 + altura;

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
	    g2.fillRoundRect(posX, getPosY() + altura, barraHP, pdj.tamañoDeBaldosa / 5, 5, 5);

	    g2.setColor(Color.red);
	    g2.fillRoundRect(posX, getPosY() + altura, hp, pdj.tamañoDeBaldosa / 5, 5, 5);

	    c = new Color(255, 255, 255);
	    g2.setColor(c);
	    g2.setStroke(new BasicStroke(2));
	    g2.drawRoundRect(posX, getPosY() + altura, barraHP, pdj.tamañoDeBaldosa / 5, 5, 5);
	}
	
	public void dibujarEfecto(Graphics2D g2) {}
	
	//METODOS DE ELECCION/////////////////////////////////////////////////////
	public Unidad elegirObjetivo(ArrayList<Unidad> unidades) {
	    if (!unidades.isEmpty()) {
	        return unidades.get((int) (Math.random() * unidades.size()));
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
	
	public boolean puedeUsarHabilidad() {
		return false;
	}

	//GETTERS & SETTERS////////////////////////////////////////////////////////
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getHP() {
		return hp;
	}

	public void setHP(int hp) {
		this.hp = hp;
	}

	public int getHPMax() {
		return hpMax;
	}

	public void setHPMax(int hpMax) {
		this.hpMax = hpMax;
	}

	public int getSP() {
		return sp;
	}

	public void setSP(int sp) {
		this.sp = sp;
	}

	public int getSPMax() {
		return spMax;
	}

	public void setSPMax(int spMax) {
		this.spMax = spMax;
	}

	public int getAtq() {
		return atq;
	}

	public void setAtq(int atq) {
		this.atq = atq;
	}

	public int getDef() {
		return def;
	}

	public int getVel() {
		return vel;
	}
	public void setVel(int vel) {
		this.vel = vel;
	}
	public void setDef(int def) {
		this.def = def;
	}

	public double getPCRT() {
		return pcrt;
	}

	public void setPCRT(double pcrt) {
		this.pcrt = pcrt;
	}
	
	public double getDCRT() {
		return pcrt;
	}

	public void setDCRT(double dcrt) {
		this.dcrt = dcrt;
	}

	public double getEva() {
		return eva;
	}
	public void setEva(double eva) {
		this.eva = eva;
	}
	public boolean isAliado() {
		return aliado;
	}

	public void setAliado(boolean aliado) {
		this.aliado = aliado;
	}

	public boolean isAlive() {
		return vivo;
	}

	public void setAlive(boolean vivo) {
		this.vivo = vivo;
	}

	public boolean isHabilidadOn() {
		return habilidadOn;
	}
	public void setHabilidadOn(boolean habilidadOn) {
		this.habilidadOn = habilidadOn;
	}
	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}
	
	public int getGenero() {
		return genero;
	}
	public int getIdFaccion() {
		return idFaccion;
	}
	public void setIdFaccion(int idFaccion) {
		this.idFaccion = idFaccion;
	}
	public String getClase() {
		return clase;
	}
	public void setClase(String clase) {
		this.clase = clase;
	}
	public int getHpMaxMod() {
		return hpMaxMod;
	}
	public void setHpMaxMod(int hpMaxMod) {
		this.hpMaxMod = hpMaxMod;
	}
	public int getSpMaxMod() {
		return spMaxMod;
	}
	public void setSpMaxMod(int spMaxMod) {
		this.spMaxMod = spMaxMod;
	}
	public int getAtqMod() {
		return atqMod;
	}
	public void setAtqMod(int atqMod) {
		this.atqMod = atqMod;
	}
	public int getDefMod() {
		return defMod;
	}
	public void setDefMod(int defMod) {
		this.defMod = defMod;
	}
	public int getVelMod() {
		return velMod;
	}
	public void setVelMod(int velMod) {
		this.velMod = velMod;
	}
	public double getEvaMod() {
		return evaMod;
	}
	public void setEvaMod(double evaMod) {
		this.evaMod = evaMod;
	}
	public double getPcrtMod() {
		return pcrtMod;
	}
	public void setPcrtMod(double pcrtMod) {
		this.pcrtMod = pcrtMod;
	}
	public double getDcrtMod() {
		return dcrtMod;
	}
	public void setDcrtMod(double dcrtMod) {
		this.dcrtMod = dcrtMod;
	}

}
