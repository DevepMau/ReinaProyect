package principal;

import unidades.Unidad;

public class Estadisticas {
	
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
	
	public static void aumentarEscudos(Unidad unidad, int valor) {
		unidad.setEscudos(unidad.getEscudos() + valor);
	}
	
	public static void aumentarFaltas(Unidad unidad, int valor) {
		unidad.setFaltasCometidas(unidad.getFaltasCometidas() + valor);
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
	
	public static void reducirEscudos(Unidad unidad, int valor) {
		unidad.setEscudos(unidad.getEscudos() - valor);
	}
	
	public static void reducirFaltas(Unidad unidad, int valor) {
		unidad.setFaltasCometidas(unidad.getFaltasCometidas() - valor);
	}

}
