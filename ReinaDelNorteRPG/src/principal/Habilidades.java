package principal;

import java.util.ArrayList;

import unidades.Unidad;

public class Habilidades {
	
	//HABILIDADES BASICAS/////////////////////////////////////////////////
	
	public static void aumentarHPMax(Unidad unidad, double porcentaje) {
	    int aumento = (int) (unidad.getHPMax() * (porcentaje / 100));
	    unidad.setHPMax(unidad.getHPMax() + aumento);
	}
	
	public static void aumentarSPMax(Unidad unidad, double porcentaje) {
	    int aumento = (int) (unidad.getSPMax() * (porcentaje / 100));
	    unidad.setSPMax(unidad.getSPMax() + aumento);
	}

	public static void aumentarAtaque(Unidad unidad, int valor) {
		unidad.setAtqMod(unidad.getAtqMod() + valor);
	}
	
	public static void aumentarDefensa(Unidad unidad, int valor) {
		unidad.setDefMod(unidad.getDefMod() + valor);
	}

	public static void aumentarVelocidad(Unidad unidad, int valor) {
		unidad.setVelMod(unidad.getVelMod() + valor);
	}

	public static void aumentarEvasion(Unidad unidad, int valor) {
		unidad.setEvaMod(unidad.getEvaMod() + valor);
	}
	
	public static void aumentarProbCrit(Unidad unidad, int valor) {
		unidad.setPcrtMod(unidad.getPcrtMod() + valor);
	}
	
	public static void aumentarTasaBloqueo(Unidad unidad, int valor) {
		unidad.setBloqMod(unidad.getBloqMod() + valor);
	}
	
	public static void reducirAtaque(Unidad unidad, int valor) {
		unidad.setAtqMod(unidad.getAtqMod() - valor);
	}
	
	public static void reducirDefensa(Unidad unidad, int valor) {
		unidad.setDefMod(unidad.getDefMod() - valor);
	}

	public static void reducirVelocidad(Unidad unidad, int valor) {
		unidad.setVelMod(unidad.getVelMod() - valor);
	}

	public static void reducirEvasion(Unidad unidad, int valor) {
		unidad.setEvaMod(unidad.getEvaMod() - valor);
	}
	
	public static void reducirProbCrit(Unidad unidad, int valor) {
		unidad.setPcrtMod(unidad.getPcrtMod() - valor);
	}
	
	public static void reducirTasaBloqueo(Unidad unidad, int valor) {
		unidad.setBloqMod(unidad.getBloqMod() - valor);
	}
	
	//HABILIDADES CON ACUMULADOR////////////////////////////////////////////
	
	public static void reducirDefensaAcc(Unidad unidad, int acc) {
		reducirDefensa(unidad, 5 * acc);
	}
	
	public static void aumentarDefensaAcc(Unidad unidad, int acc) {
		aumentarDefensa(unidad, 5 * acc);
		unidad.setRdcDefAcc(0);
	}
	
	//HABILIDADES COMPLEMENTARIAS///////////////////////////////////////////
	
	public static void setearEstado(Unidad unidad, String texto) {
		unidad.setTextoInformativo(texto);
		unidad.setearSacudida(true);
		unidad.setDuracionSacudida(20);
	}
	
	public static void setearDaño(Unidad unidad, String texto) {
		unidad.setTextoDañoRecibido(texto);
		unidad.setearSacudida(true);
		unidad.setDuracionSacudida(20);
	}
	
	//HABILIDADES DE REGENERACION////////////////////////////////////////////
	
	public static void restaurarHP(Unidad unidad, int porcentajeCuracion) {
		 int curacion = (unidad.getHPMax() * porcentajeCuracion) / 100;
		 if(curacion + unidad.getHP() > unidad.getHPMax()) {
				unidad.setHP(unidad.getHPMax());
			}
			else {
				unidad.setHP(unidad.getHP() + curacion);
			}
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
	
	//HABILIDADES ESPECIALES/////////////////////////////////////////////////
	
	public static void provocarHemorragia(Unidad unidad, PanelDeJuego pdj) {
		new Thread(() -> {
		    try {
		        Thread.sleep(500);
		    } catch (InterruptedException e) {
		        e.printStackTrace();
		    }
		    pdj.ReproducirSE(2);
		    unidad.setHP(unidad.getHP() - unidad.getValorSangrado());
		    Habilidades.setearDaño(unidad, "-"+unidad.getValorSangrado());
			unidad.setSangrando(true);
		}).start();	
	}
	
	public static void stunearUnidad(Unidad unidad) {
		unidad.setEstaActivo(false);
		unidad.setEstaStun(true);
	}
	
	//HABILIDADES COMPUESTAS/////////////////////////////////////////////////
	
	public static void aumentarAgresividad(Unidad unidad) {
		aumentarAtaque(unidad, 5);
		aumentarProbCrit(unidad, 5);
	}
	
	public static void aumentarProteccion(Unidad unidad) {
		aumentarDefensa(unidad, 5);
		aumentarTasaBloqueo(unidad, 5);
	}
	
	public static void aumentarAgilidad(Unidad unidad) {
		aumentarEvasion(unidad, 5);
		aumentarVelocidad(unidad, 5);
	}
	
	public static void potenciarUnidad(Unidad unidad) {
		aumentarAgresividad(unidad);
		aumentarProteccion(unidad);
		aumentarVelocidad(unidad, 5);
	}
	
	public static void motivarUnidad(Unidad unidad) {
		aumentarAgresividad(unidad);
		aumentarAgresividad(unidad);
		aumentarVelocidad(unidad, 10);
		aumentarEvasion(unidad, 10);
	}
	
	public static void renovarMovilidad(Unidad unidad) {
		aumentarVelocidad(unidad, 100);
		aumentarEvasion(unidad, 100);
	}
	
	public static void reducirAgresividad(Unidad unidad) {
		reducirAtaque(unidad, 5);
		reducirProbCrit(unidad, 5);
	}
	
	public static void reducirProteccion(Unidad unidad) {
		reducirDefensa(unidad, 5);
		reducirEvasion(unidad, 5);
	}
	
	public static void reducirAgilidad(Unidad unidad) {
		reducirEvasion(unidad, 5);
		reducirVelocidad(unidad, 5);
	}
	
	public static void debilitarUnidad(Unidad unidad) {
		reducirAgresividad(unidad);
		reducirProteccion(unidad);
		reducirVelocidad(unidad, 5);
	}
	
	public static void desmotivarUnidad(Unidad unidad) {
		reducirAgresividad(unidad);
		reducirAgresividad(unidad);
		reducirVelocidad(unidad, 10);
		reducirEvasion(unidad, 10);
	}
	
	public static void destruirMovilidad(Unidad unidad) {
		reducirVelocidad(unidad, 100);
		reducirEvasion(unidad, 100);
	}
	
}
