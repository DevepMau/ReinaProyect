package unidades;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;
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
	private int idMate;
	private boolean esAliado = false;
	private boolean estaActivo = true;
	private boolean estaVivo = true;
	private boolean realizaUnCritico;
	private boolean realizaUnaCuracion;
	private boolean estaEsquivando;
	private boolean tomandoUnMate;
	private boolean esUnaHabilidad;
	private boolean estaGanandoSP;
	private boolean estaMotivado;
	private boolean estaDesmotivado;
	private boolean estaLisiado;
	private boolean estaKO;
	//VARIABLES PARA LA SACUDIDA/////////////////////////////////////
	private boolean enSacudida = false;
	private int duracionSacudida = 0; // Duración en frames
	private int desplazamientoSacudidaX = 0;
	private int desplazamientoSacudidaY = 0;
	private int desplazarDañoRecibido;
    //ELEMENTOS DE LA UNIDAD/////////////////////////
	private String tipoDeAccion = "";
	private String[] listaDeHabilidades = new String[1];
	private int habilidadElegida = -1;
	private boolean objetivoUnico = true;
	private int idFaccion;
	//ESTADISTICAS DE LA UNIDAD/////////////////////////////////////
	private String nombre;
	private String clase;
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
	}
	//METODOS PRINCIPALES///////////////////////////////////////////
	public void actualizar() {
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
                estaKO = false;
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
        if (isAliado()) {
            g2.setColor(Color.BLUE);
        } else {
            g2.setColor(Color.RED);
        }
        //if(!)
        if(!isAlive()) {
        	g2.setColor(Color.YELLOW);
        }
        int dibujarX = getPosX() + desplazamientoSacudidaX;
        int dibujarY = getPosY() + desplazamientoSacudidaY;
        g2.fillRect(dibujarX + 24, dibujarY - 24, pdj.tamañoDeBaldosa, pdj.tamañoDeBaldosa * 2);
        //MOSTRAR DAÑO RECIBIDO////////////////////////////
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
            g2.drawString(dañoRecibido, getPosX()+84, desplazarDañoRecibido-48);
            g2.setColor(Color.CYAN);
            g2.drawString("& DOWN!", getPosX()+104, desplazarDañoRecibido-48);
        }
        else if(this.estaGanandoSP) {
        	g2.setColor(Color.cyan);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16f));
            g2.drawString("+SP", getPosX()+84, desplazarDañoRecibido-48);
        }
        else if(this.estaLisiado) {
        	Color c = new Color(50, 100, 100);
        	g2.setColor(c);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16f));
            g2.drawString("HURT...", getPosX()+84, desplazarDañoRecibido-48);
        }
        else if(this.estaKO) {
        	//Color c = new Color(50, 100, 100);
        	g2.setColor(Color.white);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16f));
            g2.drawString("KNOCK OUT!", getPosX()+84, desplazarDañoRecibido-48);
        }
        else {
        	g2.setColor(Color.WHITE);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16f));
            g2.drawString(dañoRecibido, getPosX()+84, desplazarDañoRecibido-48);
        }   
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
	public void recibirDaño(int daño, boolean isCritical) {
		int SEId = 2;
		this.setHP(this.getHP() - daño);
		if(isCritical) {
			dañoRecibido = "CRITICAL " + daño +"!";
			this.realizaUnCritico = true;
			SEId = 3;
		}
		else {
			dañoRecibido = "" + daño;
		}
		this.setVidaPerdida(this.getVidaPerdida() + daño);
	    setearSacudida(true);
	    setDuracionSacudida(20);
	    pdj.ReproducirSE(SEId);
	    
	}
	public void realizarAtaque(Unidad unidad) {
		boolean isCritical = Math.random() <= (this.getPCRT() + this.getPcrtMod());
		if(unidad != null) {
			boolean isMiss = Math.random() <= (unidad.getEva() + unidad.getEvaMod());	 
			int daño = Math.max(1, (this.getAtq() + this.getAtqMod()) - (unidad.getDef() + unidad.getDefMod())); 	 
			if (isCritical) {
				daño *= (this.getDCRT() + this.getDcrtMod());
			}
			if(!isMiss) {
				unidad.recibirDaño(daño, isCritical);
			}
			else {
				unidad.evadirAtaque();
			}
		}
	}
	public void realizarAtaqueEnemigo(ArrayList<Unidad> unidades) {
		Unidad objetivo = elegirObjetivo(unidades);
		boolean isCritical = Math.random() <= (this.getPCRT() + this.getPcrtMod());  
		if(objetivo != null) {
			boolean isMiss = Math.random() <= (objetivo.getEva() + objetivo.getEvaMod());
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
		realizaUnaCuracion = true;
		setearSacudida(true);
	    setDuracionSacudida(20);
	    pdj.ReproducirSE(4);
	}	
	public void usarHabilidadEnemigo(ArrayList<Unidad> unidades) {}	
	public void usarHabilidad(Unidad unidad, ArrayList<Unidad> unidades) {}	
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
	//GETTERS & SETTERS STATS BASE/////////////////////////////////////////////
	public int getGenero() {return genero;}
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
