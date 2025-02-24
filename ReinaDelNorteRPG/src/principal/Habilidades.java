package principal;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import unidades.Unidad;

public class Habilidades {
	
	public static void setearEfectoDeEstado(Unidad unidad, String texto, Color color) {
		unidad.setColorDeMensaje(color);
		unidad.setTextoInformativo(texto);
		unidad.setearSacudida(true);
		unidad.setDuracionSacudida(15);
		unidad.setEfectoDeEstado(true);
	}
	
	public static void setearDaño(Unidad unidad, String texto, Color color) {
		unidad.setColorDeDaño(color);
		unidad.setTextoDañoRecibido(texto);
		unidad.setearSacudida(true);
		unidad.setDuracionSacudida(15);
		unidad.setDañoEspecial(true);
	}
	
	//HABILIDADES CON ACUMULADOR////////////////////////////////////////////
	
	public static void oxidarArmadura(Unidad unidad) {
		Color color = new Color(181, 101, 29);
		if(unidad.getRdcDefAcc() < 3) {
			Estadisticas.reducirDefensa(unidad, 5);
			unidad.setRdcDefAcc(unidad.getRdcDefAcc() + 1);
		}
		Habilidades.setearEfectoDeEstado(unidad, "RUST!", color);		
	}
	
	public static void cancelarOxidarArmadura(Unidad unidad) {
		Estadisticas.aumentarDefensa(unidad, 5 * unidad.getRdcDefAcc());
	}
	
	//HABILIDADES DE MARCA//////////////////////////////////////////////////
	
	public static void marcarUnidad(Unidad unidad) {
		Color color = new Color(255, 255, 0);
		if(unidad.getTimerMarcado() == -1) {
			Estadisticas.reducirTasaBloqueo(unidad, 15);
			Estadisticas.reducirEvasion(unidad, 15);
			Estadisticas.reducirVelocidad(unidad, 15);
		}
		unidad.setMarcado(true);
		Habilidades.setearEfectoDeEstado(unidad, "SET!", color);
		unidad.setTimerMarcado(5);
	}
	
	public static void desmarcarUnidad(Unidad unidad) {
		Estadisticas.aumentarTasaBloqueo(unidad, 15);
		Estadisticas.aumentarEvasion(unidad, 15);
		Estadisticas.aumentarVelocidad(unidad, 15);
		unidad.setMarcado(false);
		unidad.setTimerMarcado(-1);
	}
	
	public static void explotarMarca(Unidad unidad, Unidad objetivo) {
		Color color = new Color(0, 255, 200);
		Habilidades.restaurarHP(unidad, 15);
		Estadisticas.aumentarAtaque(unidad, 2);
		Habilidades.setearEfectoDeEstado(objetivo, "BOOM!", color);
	}
	
	//HABILIDADES DE REGENERACION////////////////////////////////////////////
	
	public static void restaurarHPPlano(Unidad unidad, int valorCuracion) {
		int curacion = (unidad.getHP() + valorCuracion);
		if(curacion >= unidad.getHPMax()) {
			 unidad.setHP(unidad.getHPMax());
		}
		else {
			unidad.setHP(unidad.getHP() + valorCuracion);
		}
	}
	
	public static void restaurarHP(Unidad unidad, int porcentajeCuracion) {
		Color color = new Color(57, 255, 20);
		int curacion = (unidad.getHPMax() * porcentajeCuracion) / 100;
		if(curacion + unidad.getHP() > unidad.getHPMax()) {
			unidad.setHP(unidad.getHPMax());
		}
		else {
			unidad.setHP(unidad.getHP() + curacion);
		}
		Habilidades.setearEfectoDeEstado(unidad, "HEAL!", color);
	}
	
	public static void restaurarSP(Unidad unidad, int porcentajeEnergia) {
		int energia = (unidad.getSPMax() * porcentajeEnergia) / 100;
		 if(energia + unidad.getSP() > unidad.getSPMax()) {
				unidad.setSP(unidad.getSPMax());
			}
			else {
				unidad.setSP(unidad.getSP() + energia);
			}
	}
	
	//HABILIDADES ESPECIALES/////////////////////////////////////////////////
	
	public static void ganarNeoCreditos(Unidad unidad, int cant) {
		if(unidad.getIdFaccion() == 2) {
			if(unidad.getNeocreditos() + cant >= 100 || unidad.getNeocreditos() + cant*3 >= 100) {
				unidad.setNeocreditos(100);
				unidad.setNeoCreditosRecientes(cant);
			}
			else {
				if(unidad.getClase() == "Alumno Modelo") {
					unidad.setNeocreditos(unidad.getNeocreditos() + cant*3);
					unidad.setNeoCreditosRecientes(cant*3);
				}
				else {
					unidad.setNeocreditos(unidad.getNeocreditos() + cant);
					unidad.setNeoCreditosRecientes(cant);
				}
			}
			
		}
	}
	
	public static void repartirNeoCreditos(ArrayList<Unidad> unidades, Unidad unidad) {
		int cant = unidad.getNeoCreditosRecientes();
		if(!unidades.isEmpty()) {
			for(Unidad objetivo : unidades) {
				if(objetivo.getIdFaccion() == 2 && !objetivo.equals(unidad)) {
					if(objetivo.getNeocreditos() + cant/2 >= 100 || objetivo.getNeocreditos() + (cant*3)/2 >= 100) {
						objetivo.setNeocreditos(100);
					}
					else {
						objetivo.setNeocreditos(objetivo.getNeocreditos() + cant/2);
					}
				}
			}
		}
	}
	
	public static void stunearUnidad(Unidad unidad) {
		unidad.setEstaActivo(false);
		unidad.setEstaStun(true);
		unidad.setearSacudida(true);
		unidad.setDuracionSacudida(15);
	}
	
	public static void penalizarUnidad(Unidad unidad, ArrayList<Unidad> aliados, ArrayList<Unidad> enemigos) {
		Color color = new Color(139, 0, 0);
		int cantAliadas = contarMujeres(aliados);
		int cantEnemigas = contarMujeres(enemigos);
		if(unidad.getTimerJuzgado() == -1) {
			if(unidad.getGenero() == 0) {
				Estadisticas.reducirAtaque(unidad, 1 * cantEnemigas);
				Estadisticas.reducirProbCrit(unidad, 2 * cantEnemigas);
				unidad.setTimerJuzgado(5);
			}
			else if(unidad.getGenero() == 1) {
				Estadisticas.reducirAtaque(unidad, 1 * (cantAliadas + cantEnemigas));
				Estadisticas.reducirProbCrit(unidad, 2 * (cantAliadas + cantEnemigas));
				unidad.setTimerJuzgado(5);
			}
		}
		Habilidades.setearEfectoDeEstado(unidad, "JUDGE!", color);
		if(unidad.getCantAliadas() == 0 || unidad.getCantEnemigas() == 0) {
			unidad.setCantAliadas(cantAliadas);
			unidad.setCantEnemigas(cantEnemigas);
		}
	}
	
	public static void cancelarPenalizarUnidad(Unidad unidad, ArrayList<Unidad> aliados, ArrayList<Unidad> enemigos) {
		if(unidad.getGenero() == 0) {
			Estadisticas.aumentarAtaque(unidad, 1 * unidad.getCantEnemigas());
			Estadisticas.aumentarProbCrit(unidad, 2 * unidad.getCantEnemigas());
		}
		else {
			Estadisticas.aumentarAtaque(unidad, 1 * (unidad.getCantAliadas() + unidad.getCantEnemigas()));
			Estadisticas.aumentarProbCrit(unidad, 2 * (unidad.getCantAliadas() + unidad.getCantEnemigas()));
		}
		unidad.setCantAliadas(0);
		unidad.setCantEnemigas(0);
	}
	
	public static void maldicionAleatoria(Unidad unidad) {
		int i = elegirAleatorio(4);
		if(i == 0) {
			Estadisticas.reducirAtaque(unidad, 1);
		}
		else if(i == 1) {
			Estadisticas.reducirDefensa(unidad, 1);
		}
		else if(i == 2) {
			Estadisticas.reducirProbCrit(unidad, 1);
		}
		else {
			Estadisticas.reducirEvasion(unidad, 1);
		}
		
	}
	
	//HABILIDADES DE VARIOS HIT'S////////////////////////////////////////////
	
	public static void protegerUnidad(Unidad unidad, int valor, PanelDeJuego pdj) {
		Color color = new Color(70, 130, 180);
		new Thread(() -> {
		    try {
		        Thread.sleep(300);
		    } catch (InterruptedException e) {
		        e.printStackTrace();
		    }
		    pdj.ReproducirSE(4);
		    Estadisticas.aumentarEscudos(unidad, valor);
		    Habilidades.setearEfectoDeEstado(unidad, "GUARD!", color);
		}).start();	
	}
	
	public static void ataqueActivadorHemorragia(Unidad lanzador, Unidad objetivo) {
		Habilidades.setearEfectoDeEstado(objetivo, "BLEED!", Color.RED);
		objetivo.setValorSangrado(objetivo.getHPMax()/20 + lanzador.getAtqMod());
		objetivo.setTimerSangrando(5);
	}
	
	public static void provocarHemorragia(Unidad unidad, PanelDeJuego pdj) {
		Color color = new Color(255, 0, 0);
		new Thread(() -> {
		    try {
		        Thread.sleep(300);
		    } catch (InterruptedException e) {
		        e.printStackTrace();
		    }
		    pdj.ReproducirSE(2);
		    unidad.setHP(unidad.getHP() - unidad.getValorSangrado());
		    Habilidades.setearDaño(unidad, "-"+unidad.getValorSangrado(), color);
			unidad.setDañoEspecial(true);
		}).start();	
	}
	
	public static void provocarIncendiar(Unidad unidad, PanelDeJuego pdj) {
		Color color = new Color(255, 155, 0);
		new Thread(() -> {
		    try {
		        Thread.sleep(500);
		    } catch (InterruptedException e) {
		        e.printStackTrace();
		    }
		    pdj.ReproducirSE(2);
		    unidad.setHP(unidad.getHP() - unidad.getHPMax()/20);
		    Habilidades.setearDaño(unidad, "-"+unidad.getHPMax()/20, color);
			unidad.setDañoEspecial(true);
		}).start();	
	}
	
	public static void reportarUnidad(Unidad lanzador, Unidad objetivo, PanelDeJuego pdj) {
		Color color = new Color(139, 0, 0);
		pdj.ReproducirSE(2);
		int daño = lanzador.getAtq() + lanzador.getAtqMod();
		if(objetivo.getFaltasCometidas() >= 3) {
			objetivo.setHP(objetivo.getHP() - daño);
			objetivo.setFaltasCometidas(0);
			Estadisticas.aumentarAtaque(lanzador, 2);
			Estadisticas.reducirAtaque(objetivo, 2);
			Habilidades.stunearUnidad(objetivo);
			Habilidades.setearEfectoDeEstado(objetivo, "BENCH!", color);
		}
		else if(objetivo.getFaltasCometidas() >= 3) {
			objetivo.setHP(objetivo.getHP() - daño);
			objetivo.setFaltasCometidas(0);
			Estadisticas.aumentarAtaque(lanzador, 1);
			Estadisticas.reducirAtaque(objetivo, 1);
			Habilidades.setearEfectoDeEstado(objetivo, "SCOLD!", color);
		}
		else if(objetivo.getFaltasCometidas() >= 3) {
			objetivo.setHP(objetivo.getHP() - daño);
			objetivo.setFaltasCometidas(0);
			Habilidades.setearEfectoDeEstado(objetivo, "SCOLD!", color);
		}
		else {
			Estadisticas.aumentarFaltas(objetivo, 1);
			Habilidades.setearEfectoDeEstado(objetivo, "WARN!", color);
		}
	}
	
	public static void difamarUnidad(Unidad lanzador, Unidad objetivo) {
		Color color = new Color(204, 204, 0);
		Estadisticas.aumentarDefensa(lanzador, 1);
		Estadisticas.reducirDefensa(objetivo, 1);
		Habilidades.setearEfectoDeEstado(objetivo, "SLUR", color);
	}
	
	public static void tendencia(Unidad unidad) {
		Color color = new Color(255, 20, 147);
		if(unidad.getTimerTendencia() == -1) {
			Estadisticas.aumentarTasaBloqueo(unidad, 50);
			Estadisticas.aumentarDefensa(unidad, 10);
		}
		Habilidades.setearEfectoDeEstado(unidad, "TREND!", color);
		unidad.setTimerTendencia(5);
	}
	
	public static void cancelarTendencia(Unidad unidad) {
		Estadisticas.reducirTasaBloqueo(unidad, 30);
		Estadisticas.reducirDefensa(unidad, 10);
	}
	
	public static void desmoralizarUnidad(Unidad unidad) {
		Color color = new Color(255, 160, 180);
		if(unidad.getTimerDesmoralizar() == -1) {
			Estadisticas.reducirAtaque(unidad, 5);
			Estadisticas.reducirVelocidad(unidad, 5);
			Estadisticas.reducirEvasion(unidad, 5);
		}
		unidad.setHP(unidad.getHP() - unidad.getHPMax()/10);
		Habilidades.setearEfectoDeEstado(unidad, "DOWN!", color);
		unidad.setTimerDesmoralizar(5);
	}
	
	public static void cancelarDesmoralizarUnidad(Unidad unidad) {
		Estadisticas.aumentarAtaque(unidad, 5);
		Estadisticas.aumentarVelocidad(unidad, 5);
		Estadisticas.aumentarEvasion(unidad, 5);
	}
		
	//HABILIDADES COMPUESTAS/////////////////////////////////////////////////
	
	public static void darBonificacionAleatorio(ArrayList<Unidad> unidades) {
		if(!unidades.isEmpty()) {
			Unidad objetivo = unidades.get(elegirAleatorio(unidades.size()));
			int opcion = elegirAleatorio(4);
			if(opcion == 0) {
				Habilidades.aumentarAgilidad(objetivo);
			}
			else if(opcion == 1) {
				Habilidades.aumentarAgresividad(objetivo);
			}
			else if(opcion == 2) {
				Habilidades.aumentarProteccion(objetivo);
			}
		}
	}
	
	public static void aumentarAgresividad(Unidad unidad) {
		Color color = new Color(255, 0, 0);
		if(unidad.getTimerAgresivo() == -1) {
			Estadisticas.aumentarAtaque(unidad, 10);
			Estadisticas.aumentarProbCrit(unidad, 10);
		}
		Habilidades.setearEfectoDeEstado(unidad, "RAGE!", color);
		unidad.setTimerAgresivo(5);
	}
	
	public static void cancelarAumentarAgresividad(Unidad unidad) {
		Estadisticas.reducirAtaque(unidad, 10);
		Estadisticas.reducirProbCrit(unidad, 10);
	}
	
	public static void aumentarProteccion(Unidad unidad) {
		Color color = new Color(70, 130, 180);
		if(unidad.getTimerPrecavido() == -1) {
			Estadisticas.aumentarDefensa(unidad, 10);
			Estadisticas.aumentarTasaBloqueo(unidad, 10);
		}
		Habilidades.setearEfectoDeEstado(unidad, "HARD!", color);
		unidad.setTimerPrecavido(5);
	}
	
	public static void cancelarAumentarProteccion(Unidad unidad) {
		Estadisticas.reducirDefensa(unidad, 10);
		Estadisticas.reducirEvasion(unidad, 10);
	}
	
	public static void aumentarAgilidad(Unidad unidad) {
		Color color = new Color(0, 155, 0);
		if(unidad.getTimerAcelerado() == -1) {
			Estadisticas.aumentarEvasion(unidad, 10);
			Estadisticas.aumentarVelocidad(unidad, 10);
		}
		Habilidades.setearEfectoDeEstado(unidad, "FAST!", color);
		unidad.setTimerAcelerado(5);	
	}
	
	public static void cancelarAumentarAgilidad(Unidad unidad) {
		Estadisticas.reducirEvasion(unidad, 10);
		Estadisticas.reducirVelocidad(unidad, 10);
	}
	
	public static void potenciarUnidad(Unidad unidad) {
		Color color = new Color(255, 255, 0);
		if(unidad.getTimerPotenciado() == -1) {
			Estadisticas.aumentarAtaque(unidad, 10);
			Estadisticas.aumentarDefensa(unidad, 10);
			Estadisticas.aumentarEvasion(unidad, 10);
			Estadisticas.aumentarProbCrit(unidad, 10);
			Estadisticas.aumentarTasaBloqueo(unidad, 10);
			Estadisticas.aumentarVelocidad(unidad, 10);
		}
		Habilidades.setearEfectoDeEstado(unidad, "OP!", color);
		unidad.setTimerPotenciado(5);
	}
	
	public static void cancelarPotenciarUnidad(Unidad unidad) {
		Estadisticas.reducirAtaque(unidad, 10);
		Estadisticas.reducirDefensa(unidad, 10);
		Estadisticas.reducirEvasion(unidad, 10);
		Estadisticas.reducirProbCrit(unidad, 10);
		Estadisticas.reducirTasaBloqueo(unidad, 10);
		Estadisticas.reducirVelocidad(unidad, 10);
	}
	
	public static void motivarUnidad(Unidad unidad) {
		Color color = new Color(255, 140, 0);
		if(unidad.getTimerMotivado() == -1) {
			Estadisticas.aumentarAtaque(unidad, 10);
			Estadisticas.aumentarVelocidad(unidad, 10);
			Estadisticas.aumentarEvasion(unidad, 10);
		}
		Habilidades.setearEfectoDeEstado(unidad, "DANCE!", color);
		unidad.setTimerMotivado(5);
	}
	
	public static void cancelarMotivarUnidad(Unidad unidad) {
		Estadisticas.reducirAtaque(unidad, 10);
		Estadisticas.reducirVelocidad(unidad, 10);
		Estadisticas.reducirEvasion(unidad, 10);
	}
	
	public static void destruirMovilidad(Unidad unidad) {
		Color color = new Color(75, 0, 130);
		if(unidad.getTimerLisiado() == -1) {
			Estadisticas.reducirVelocidad(unidad, 100);
			Estadisticas.reducirEvasion(unidad, 100);
		}
		Habilidades.setearEfectoDeEstado(unidad, "OUT!", color);
		unidad.setTimerLisiado(5);
		stunearUnidad(unidad);
	}
	
	public static void cancelarDestruirMovilidad(Unidad unidad) {
		Estadisticas.aumentarVelocidad(unidad, 100);
		Estadisticas.aumentarEvasion(unidad, 100);
	}
	
	//METODOS VARIOS/////////////////////////////////////////////////////////////////
	private static int contarMujeres(ArrayList<Unidad> unidades) {
		int cont = 0;
		for(Unidad unidad : unidades) {
			if(unidad.getGenero() == 0) {
				cont++;
			}
		}
		return cont;
	}
	
	public static int elegirAleatorio(int i) {
	    Random random = new Random();
	    return random.nextInt(i);
	}
}
