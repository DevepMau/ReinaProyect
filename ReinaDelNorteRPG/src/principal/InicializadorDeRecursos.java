package principal;

import unidades.Unidad;

public class InicializadorDeRecursos {

	PanelDeJuego pdj;

	public InicializadorDeRecursos(PanelDeJuego pdj) {
		this.pdj = pdj;
	}

	public void establecerObjetos() {

	}
	
	public void establecerNPCs() {

	}
	
	public void establecerPersonajes() {
		
	}
	
	public void cargarImagenesDeDummyMuerto(Unidad unidad) {
		unidad.imagenesBody[4] = null;
		unidad.imagenesDead[2] = pdj.ui.configurarImagen("/imagenes/dummy/dead-piernas", 3);
		unidad.imagenesDead[1] = pdj.ui.configurarImagen("/imagenes/dummy/dead-cuerpo", 3);
		unidad.imagenesDead[0] = pdj.ui.configurarImagen("/imagenes/dummy/dead-cabeza", 3);
		unidad.imagenesDead[3] = pdj.ui.configurarImagen("/imagenes/dummy/dead-manos", 3);
	}
	
	public void cargarImagenesDeUnidad(Unidad unidad) {
		unidad.definirIdTez();
		if(unidad.getIdFaccion() == 1) {
			unidad.imagenesBody[4] = pdj.ui.configurarImagen("/imagenes/accesorios/boina1",3);
			String color = unidad.elegirEquipo();
			if(unidad.getGenero() == 1) {
				//SI ES HOMBRE/////////////////////////////////////////////////////////////////////////
				if(unidad.getClase() == "Heroe Federal") {
					unidad.imagenesBody[3] = pdj.ui.configurarImagen("/imagenes/unisex/caballo-"+unidad.getIdTez(), 3);
					unidad.imagenesBody[2] = pdj.ui.configurarImagen("/imagenes/hombre/bici-boy", 3);
					unidad.imagenesBody[1] = pdj.ui.configurarImagen("/imagenes/unisex/cuerpo-heroe", 3);
					//imagenesBody[0] = configurarImagen("/imagenes/hombre/cabeza-boy-"+this.idTez, 3);
					unidad.setAlturaPorClase(-10);
				}
				else if(unidad.getClase() == "Cebador De Mate") {
					unidad.imagenesBody[3] = pdj.ui.configurarImagen("/imagenes/unisex/mates-"+color+"-"+unidad.getIdTez(), 3);
					unidad.imagenesBody[2] = pdj.ui.configurarImagen("/imagenes/hombre/pantalon-1",3);
					unidad.imagenesBody[1] = pdj.ui.configurarImagen("/imagenes/unisex/cuerpo"+unidad.getIdTez(), 3);
					unidad.imagenesBody[0] = pdj.ui.configurarImagen("/imagenes/hombre/cabeza-boy-"+unidad.getIdTez(), 3);
				}
				else if(unidad.getClase() == "Payador Picante") {
					unidad.imagenesBody[3] = pdj.ui.configurarImagen("/imagenes/unisex/guitarra-"+unidad.getIdTez(), 3);
					unidad.imagenesBody[2] = pdj.ui.configurarImagen("/imagenes/hombre/pantalon-1",3);
					unidad.imagenesBody[1] = pdj.ui.configurarImagen("/imagenes/unisex/cuerpo"+unidad.getIdTez(), 3);
					unidad.imagenesBody[0] = pdj.ui.configurarImagen("/imagenes/hombre/cabeza-boy-"+unidad.getIdTez(), 3);
				}
				else if(unidad.getClase() == "Gaucho Moderno") {
					unidad.imagenesBody[3] = pdj.ui.configurarImagen("/imagenes/hombre/cutter-"+unidad.getIdTez(),3);
					unidad.imagenesBody[2] = pdj.ui.configurarImagen("/imagenes/hombre/pantalon-subido-"+unidad.getIdTez(),3);
					unidad.imagenesBody[1] = pdj.ui.configurarImagen("/imagenes/unisex/cuerpo-gaucho", 3);
					unidad.imagenesBody[0] = pdj.ui.configurarImagen("/imagenes/hombre/cabeza-boy-"+unidad.getIdTez(), 3);
				}
			}
			//SI ES MUJER////////////////////////////////////////////////////////////////////////////
			else {
				if(unidad.getClase() == "Heroe Federal") {
					unidad.imagenesBody[3] = pdj.ui.configurarImagen("/imagenes/unisex/caballo-"+unidad.getIdTez(), 3);
					unidad.imagenesBody[2] = pdj.ui.configurarImagen("/imagenes/mujer/bici-girl-"+unidad.getIdTez(), 3);
					unidad.imagenesBody[1] = pdj.ui.configurarImagen("/imagenes/unisex/cuerpo-heroe", 3);
					unidad.imagenesBody[0] = pdj.ui.configurarImagen("/imagenes/mujer/cabeza-girl-"+unidad.getIdTez(), 3);
					unidad.setAlturaPorClase(-10);
				}
				if(unidad.getClase() == "Cebador De Mate") {
					unidad.imagenesBody[3] = pdj.ui.configurarImagen("/imagenes/unisex/mates-"+color+"-"+unidad.getIdTez(), 3);
					unidad.imagenesBody[2] = pdj.ui.configurarImagen("/imagenes/mujer/falda-"+unidad.getIdTez(),3);
					unidad.imagenesBody[1] = pdj.ui.configurarImagen("/imagenes/unisex/cuerpo"+unidad.getIdTez(), 3);
					unidad.imagenesBody[0] = pdj.ui.configurarImagen("/imagenes/mujer/cabeza-girl-"+unidad.getIdTez(), 3);
				}
				else if(unidad.getClase() == "Payador Picante") {
					unidad.imagenesBody[3] = pdj.ui.configurarImagen("/imagenes/unisex/guitarra-"+unidad.getIdTez(), 3);
					unidad.imagenesBody[2] = pdj.ui.configurarImagen("/imagenes/mujer/falda-"+unidad.getIdTez(),3);
					unidad.imagenesBody[1] = pdj.ui.configurarImagen("/imagenes/unisex/cuerpo"+unidad.getIdTez(), 3);
					unidad.imagenesBody[0] = pdj.ui.configurarImagen("/imagenes/mujer/cabeza-girl-"+unidad.getIdTez(), 3);
				}
				else if(unidad.getClase() == "Gaucho Moderno") {
					unidad.imagenesBody[3] = pdj.ui.configurarImagen("/imagenes/hombre/cutter-"+unidad.getIdTez(),3);
					unidad.imagenesBody[2] = pdj.ui.configurarImagen("/imagenes/mujer/falda-"+unidad.getIdTez(),3);
					unidad.imagenesBody[1] = pdj.ui.configurarImagen("/imagenes/unisex/cuerpo-gaucho", 3);
					unidad.imagenesBody[0] = pdj.ui.configurarImagen("/imagenes/mujer/cabeza-girl-"+unidad.getIdTez(), 3);
				}
			}
		}
		else if(unidad.getIdFaccion() == 2) {
			//SI ES HOMBRE/////////////////////////////////////////////////////////////////////////
			if(unidad.getGenero() == 1) {
				if(unidad.getClase() == "Alumno Modelo") {
					unidad.imagenesBody[3] = pdj.ui.configurarImagen("/imagenes/unisex/libro-"+unidad.getIdTez(), 3);
					unidad.imagenesBody[2] = pdj.ui.configurarImagen("/imagenes/hombre/pantalon-1",3);
					unidad.imagenesBody[1] = pdj.ui.configurarImagen("/imagenes/unisex/cuerpo"+unidad.getIdTez(), 3);
					unidad.imagenesBody[0] = pdj.ui.configurarImagen("/imagenes/hombre/cabeza-boy-"+unidad.getIdTez(), 3);
				}
				else if(unidad.getClase() == "Shaolin Escolar") {
					unidad.imagenesBody[3] = pdj.ui.configurarImagen("/imagenes/unisex/manos-shaolin-"+unidad.getIdTez(), 3);
					unidad.imagenesBody[2] = pdj.ui.configurarImagen("/imagenes/hombre/pantalon-shaolin",3);
					unidad.imagenesBody[1] = pdj.ui.configurarImagen("/imagenes/unisex/cuerpo"+unidad.getIdTez(), 3);
					unidad.imagenesBody[0] = pdj.ui.configurarImagen("/imagenes/hombre/cabeza-shaolin-"+unidad.getIdTez(), 3);
				}
				else if(unidad.getClase() == "Medico Tradicionalista") {
					unidad.imagenesBody[3] = pdj.ui.configurarImagen("/imagenes/unisex/quimicos-"+unidad.getIdTez(), 3);
					unidad.imagenesBody[2] = pdj.ui.configurarImagen("/imagenes/hombre/pantalon-1",3);
					unidad.imagenesBody[1] = pdj.ui.configurarImagen("/imagenes/unisex/cuerpo"+unidad.getIdTez(), 3);
					unidad.imagenesBody[0] = pdj.ui.configurarImagen("/imagenes/hombre/cabeza-boy-"+unidad.getIdTez(), 3);
				}
				else if(unidad.getClase() == "Dragon Pirotecnico") {
					unidad.imagenesBody[4] = pdj.ui.configurarImagen("/imagenes/accesorios/dragon", 3);
					unidad.imagenesBody[3] = pdj.ui.configurarImagen("/imagenes/unisex/cañon-"+unidad.getIdTez(), 3);
					unidad.imagenesBody[2] = pdj.ui.configurarImagen("/imagenes/hombre/pantalon-dragon",3);
					unidad.imagenesBody[1] = pdj.ui.configurarImagen("/imagenes/unisex/cuerpo"+unidad.getIdTez(), 3);
					unidad.imagenesBody[0] = pdj.ui.configurarImagen("/imagenes/hombre/cabeza-boy-"+unidad.getIdTez(), 3);
					unidad.setAlturaDeAccesorio(5);
					unidad.setAlturaDeBarraHP(-20);
				}
			}
			else {
				//SI ES MUJER/////////////////////////////////////////////////////////////////////////
				if(unidad.getClase() == "Alumno Modelo") {
					unidad.imagenesBody[3] = pdj.ui.configurarImagen("/imagenes/unisex/libro-"+unidad.getIdTez(), 3);
					unidad.imagenesBody[2] = pdj.ui.configurarImagen("/imagenes/mujer/falda-"+unidad.getIdTez(),3);
					unidad.imagenesBody[1] = pdj.ui.configurarImagen("/imagenes/unisex/cuerpo"+unidad.getIdTez(), 3);
					unidad.imagenesBody[0] = pdj.ui.configurarImagen("/imagenes/mujer/cabeza-girl-"+unidad.getIdTez(), 3);
				}
				else if(unidad.getClase() == "Shaolin Escolar") {
					unidad.imagenesBody[3] = pdj.ui.configurarImagen("/imagenes/unisex/manos-shaolin-"+unidad.getIdTez(), 3);
					unidad.imagenesBody[2] = pdj.ui.configurarImagen("/imagenes/mujer/falda-"+unidad.getIdTez(),3);
					unidad.imagenesBody[1] = pdj.ui.configurarImagen("/imagenes/unisex/cuerpo"+unidad.getIdTez(), 3);
					unidad.imagenesBody[0] = pdj.ui.configurarImagen("/imagenes/mujer/cabeza-girl-"+unidad.getIdTez(), 3);
				}
				else if(unidad.getClase() == "Medico Tradicionalista") {
					unidad.imagenesBody[3] = pdj.ui.configurarImagen("/imagenes/unisex/quimicos-"+unidad.getIdTez(), 3);
					unidad.imagenesBody[2] = pdj.ui.configurarImagen("/imagenes/mujer/falda-"+unidad.getIdTez(),3);
					unidad.imagenesBody[1] = pdj.ui.configurarImagen("/imagenes/unisex/cuerpo"+unidad.getIdTez(), 3);
					unidad.imagenesBody[0] = pdj.ui.configurarImagen("/imagenes/mujer/cabeza-girl-"+unidad.getIdTez(), 3);
				}
				else if(unidad.getClase() == "Dragon Pirotecnico") {
					unidad.imagenesBody[4] = pdj.ui.configurarImagen("/imagenes/accesorios/dragon", 3);
					unidad.imagenesBody[3] = pdj.ui.configurarImagen("/imagenes/unisex/cañon-"+unidad.getIdTez(), 3);
					unidad.imagenesBody[2] = pdj.ui.configurarImagen("/imagenes/mujer/falda-dragon-"+unidad.getIdTez(),3);
					unidad.imagenesBody[1] = pdj.ui.configurarImagen("/imagenes/unisex/cuerpo"+unidad.getIdTez(), 3);
					unidad.imagenesBody[0] = pdj.ui.configurarImagen("/imagenes/mujer/cabeza-girl-"+unidad.getIdTez(), 3);
					unidad.setAlturaDeAccesorio(5);
					unidad.setAlturaDeBarraHP(-20);
				}
			}
		}
		else if(unidad.getIdFaccion() == 3) {
			String color = unidad.elegirColor();
			//SI ES HOMBRE/////////////////////////////////////////////////////////////////////////
			if(unidad.getGenero() == 1) {
				if(unidad.getClase() == "Niño Cheung") {
					unidad.imagenesBody[3] = pdj.ui.configurarImagen("/imagenes/unisex/manos-pose-"+unidad.getIdTez(), 3);
					unidad.imagenesBody[2] = pdj.ui.configurarImagen("/imagenes/hombre/pantalon-pose",3);
					unidad.imagenesBody[1] = pdj.ui.configurarImagen("/imagenes/hombre/cuerpo-chino-boy", 3);
					unidad.imagenesBody[0] = pdj.ui.configurarImagen("/imagenes/hombre/cabeza-boy-"+unidad.getIdTez(), 3);
				}
				else if(unidad.getClase() == "Puño Furioso") {
					unidad.imagenesBody[3] = pdj.ui.configurarImagen("/imagenes/unisex/nunchaku-"+color+"-"+unidad.getIdTez(), 3);
					unidad.imagenesBody[2] = pdj.ui.configurarImagen("/imagenes/unisex/cortos-pose-"+unidad.getIdTez(),3);
					unidad.imagenesBody[1] = pdj.ui.configurarImagen("/imagenes/unisex/cuerpo-chaqueta", 3);
					unidad.imagenesBody[0] = pdj.ui.configurarImagen("/imagenes/hombre/cabeza-boy-"+unidad.getIdTez(), 3);
				}
				else if(unidad.getClase() == "Maestro Del Chi") {
					unidad.imagenesBody[3] = pdj.ui.configurarImagen("/imagenes/unisex/manos-pose-"+unidad.getIdTez(), 3);
					unidad.imagenesBody[2] = pdj.ui.configurarImagen("/imagenes/hombre/piernas-maestro-boy",3);
					unidad.imagenesBody[1] = pdj.ui.configurarImagen("/imagenes/hombre/cuerpo-maestro-boy", 3);
					unidad.imagenesBody[0] = pdj.ui.configurarImagen("/imagenes/hombre/cabeza-boy-"+unidad.getIdTez(), 3);
				}
				else if(unidad.getClase() == "Aspirante A Dragon") {
					unidad.imagenesBody[3] = pdj.ui.configurarImagen("/imagenes/unisex/nunchaku-rojo-"+unidad.getIdTez(), 3);
					unidad.imagenesBody[2] = pdj.ui.configurarImagen("/imagenes/unisex/piernas-amarillo",3);
					unidad.imagenesBody[1] = pdj.ui.configurarImagen("/imagenes/unisex/cuerpo-amarillo", 3);
					unidad.imagenesBody[0] = pdj.ui.configurarImagen("/imagenes/hombre/cabeza-boy-"+unidad.getIdTez(), 3);
				}
			}
			else {
				//SI ES MUJER/////////////////////////////////////////////////////////////////////////
				if(unidad.getClase() == "Niño Cheung") {
					unidad.imagenesBody[3] = pdj.ui.configurarImagen("/imagenes/unisex/manos-pose-"+unidad.getIdTez(), 3);
					unidad.imagenesBody[2] = pdj.ui.configurarImagen("/imagenes/mujer/falda-pose-"+unidad.getIdTez(),3);
					unidad.imagenesBody[1] = pdj.ui.configurarImagen("/imagenes/mujer/cuerpo-chino-girl", 3);
					unidad.imagenesBody[0] = pdj.ui.configurarImagen("/imagenes/mujer/cabeza-girl-"+unidad.getIdTez(), 3);
				}
				else if(unidad.getClase() == "Puño Furioso") {
					unidad.imagenesBody[3] = pdj.ui.configurarImagen("/imagenes/unisex/nunchaku-"+color+"-"+unidad.getIdTez(), 3);
					unidad.imagenesBody[2] = pdj.ui.configurarImagen("/imagenes/unisex/cortos-pose-"+unidad.getIdTez(),3);
					unidad.imagenesBody[1] = pdj.ui.configurarImagen("/imagenes/unisex/cuerpo-chaqueta", 3);
					unidad.imagenesBody[0] = pdj.ui.configurarImagen("/imagenes/mujer/cabeza-coleta-"+unidad.getIdTez(), 3);
				}
				else if(unidad.getClase() == "Maestro Del Chi") {
					unidad.imagenesBody[3] = pdj.ui.configurarImagen("/imagenes/unisex/manos-pose-"+unidad.getIdTez(), 3);
					unidad.imagenesBody[2] = pdj.ui.configurarImagen("/imagenes/mujer/piernas-maestro-girl-"+unidad.getIdTez(),3);
					unidad.imagenesBody[1] = pdj.ui.configurarImagen("/imagenes/mujer/cuerpo-maestro-girl", 3);
					unidad.imagenesBody[0] = pdj.ui.configurarImagen("/imagenes/mujer/cabeza-girl-"+unidad.getIdTez(), 3);
				}
				else if(unidad.getClase() == "Aspirante A Dragon") {
					unidad.imagenesBody[3] = pdj.ui.configurarImagen("/imagenes/unisex/nunchaku-rojo-"+unidad.getIdTez(), 3);
					unidad.imagenesBody[2] = pdj.ui.configurarImagen("/imagenes/unisex/piernas-amarillo",3);
					unidad.imagenesBody[1] = pdj.ui.configurarImagen("/imagenes/unisex/cuerpo-amarillo", 3);
					unidad.imagenesBody[0] = pdj.ui.configurarImagen("/imagenes/mujer/cabeza-bollos-"+unidad.getIdTez(), 3);
				}
			}
		}
		else if(unidad.getIdFaccion() == 4) {
			if(unidad.getGenero() == 0) {
				if(unidad.getClase() == "Novata Timida") {
					unidad.imagenesBody[3] = pdj.ui.configurarImagen("/imagenes/unisex/manos-timida-"+unidad.getIdTez(), 3);
					unidad.imagenesBody[2] = pdj.ui.configurarImagen("/imagenes/mujer/falda-"+unidad.getIdTez(),3);
					unidad.imagenesBody[1] = pdj.ui.configurarImagen("/imagenes/unisex/cuerpo"+unidad.getIdTez(), 3);
					unidad.imagenesBody[0] = pdj.ui.configurarImagen("/imagenes/mujer/cabeza-timida-"+unidad.getIdTez(), 3);
				}
				else if(unidad.getClase() == "Novata Cauta") {
					unidad.imagenesBody[3] = pdj.ui.configurarImagen("/imagenes/unisex/manos-"+unidad.getIdTez(), 3);
					unidad.imagenesBody[2] = pdj.ui.configurarImagen("/imagenes/mujer/falda-"+unidad.getIdTez(),3);
					unidad.imagenesBody[1] = pdj.ui.configurarImagen("/imagenes/unisex/cuerpo"+unidad.getIdTez(), 3);
					unidad.imagenesBody[0] = pdj.ui.configurarImagen("/imagenes/mujer/cabeza-girl-"+unidad.getIdTez(), 3);
				}
				else if(unidad.getClase() == "Novata Confiable") {
					unidad.imagenesBody[3] = pdj.ui.configurarImagen("/imagenes/unisex/manos-seña-"+unidad.getIdTez(), 3);
					unidad.imagenesBody[2] = pdj.ui.configurarImagen("/imagenes/mujer/falda-"+unidad.getIdTez(),3);
					unidad.imagenesBody[1] = pdj.ui.configurarImagen("/imagenes/unisex/cuerpo"+unidad.getIdTez(), 3);
					unidad.imagenesBody[0] = pdj.ui.configurarImagen("/imagenes/mujer/cabeza-coleta-"+unidad.getIdTez(), 3);
				}
				else if(unidad.getClase() == "Delegada") {
					unidad.imagenesBody[4] = pdj.ui.configurarImagen("/imagenes/accesorios/mano-de-lente-"+unidad.getIdTez(),3);
					unidad.imagenesBody[3] = pdj.ui.configurarImagen("/imagenes/unisex/manos-libreta-"+unidad.getIdTez(), 3);
					unidad.imagenesBody[2] = pdj.ui.configurarImagen("/imagenes/mujer/falda-"+unidad.getIdTez(),3);
					unidad.imagenesBody[1] = pdj.ui.configurarImagen("/imagenes/unisex/cuerpo"+unidad.getIdTez(), 3);
					unidad.imagenesBody[0] = pdj.ui.configurarImagen("/imagenes/mujer/cabeza-delegada-"+unidad.getIdTez(), 3);
				}
				else if(unidad.getClase() == "Influencer") {
					unidad.imagenesBody[3] = pdj.ui.configurarImagen("/imagenes/unisex/manos-celular-"+unidad.getIdTez(), 3);
					unidad.imagenesBody[2] = pdj.ui.configurarImagen("/imagenes/mujer/falda-"+unidad.getIdTez(),3);
					unidad.imagenesBody[1] = pdj.ui.configurarImagen("/imagenes/unisex/cuerpo"+unidad.getIdTez(), 3);
					unidad.imagenesBody[0] = pdj.ui.configurarImagen("/imagenes/mujer/cabeza-influencer-"+unidad.getIdTez(), 3);
				}
				else if(unidad.getClase() == "Idol Galactica") {
					//imagenesBody[4] = configurarImagen("/imagenes/accesorios/dragon", 3);
					unidad.imagenesBody[3] = pdj.ui.configurarImagen("/imagenes/unisex/manos-idol-"+unidad.getIdTez(), 3);
					unidad.imagenesBody[2] = pdj.ui.configurarImagen("/imagenes/mujer/falda-olanes",3);
					unidad.imagenesBody[1] = pdj.ui.configurarImagen("/imagenes/mujer/cuerpo-idol", 3);
					unidad.imagenesBody[0] = pdj.ui.configurarImagen("/imagenes/mujer/cabeza-idol-"+unidad.getIdTez(), 3);
					//alturaDeAccesorio = 5;
					//this.alturaDeBarraHP = -20;
				}
			}
		}
		else {
			unidad.imagenesBody[4] = null;
			unidad.imagenesBody[3] = pdj.ui.configurarImagen("/imagenes/dummy/dummy-manos",3);
			unidad.imagenesBody[2] = pdj.ui.configurarImagen("/imagenes/dummy/dummy-piernas",3);
			unidad.imagenesBody[1] = pdj.ui.configurarImagen("/imagenes/dummy/dummy-cuerpo", 3);
			unidad.imagenesBody[0] = pdj.ui.configurarImagen("/imagenes/dummy/dummy-cabeza", 3);
		}
	}

}
