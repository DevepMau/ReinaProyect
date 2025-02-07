package principal;

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
	
	//HABILIDADES COMPUSTAS/////////////////////////////////////////////////
	
	//HABILIDADES COMPUSTAS/////////////////////////////////////////////////
	
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
	
	public static void aumentarAgresividad(Unidad unidad) {
		aumentarAtaque(unidad, 5);
		aumentarProbCrit(unidad, 5);
	}
	
	public static void aumentarProteccion(Unidad unidad) {
		aumentarDefensa(unidad, 5);
		aumentarEvasion(unidad, 5);
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

	
}
