package principal;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import unidades.Unidad;

public class Imagenes {
    
    // DEAD-DUMMY
	public Image cabezaDD, manosDD, piernasDD, cuerpoDD;
	
	// ESTADOS
    public Image shieldUp, swordUp, bootUp, blockUp, dashUp, bulleyeUp;
    public Image shieldDown, swordDown, bootDown, blockDown, dashDown, bulleyeDown;
    public Image bleed, burn, fragile;
    
    // MATES
    public Image mateVerde, mateRojo, mateAzul, mateAmarillo;
    
    // ATAQUES
    public Image corte1, corte2, corte3, corte4, corte5;
    public Image golpe1, golpe2, golpe3, golpe4, golpe5;
    
    // OTROS
    public Image piso, flash, piedra, estandarteHalcon, anular;
    
    // 

    // Constructor
    public Imagenes() {
        try {
            inicializarImagenes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void inicializarImagenes() throws IOException { 
    	
		piernasDD = configurarImagen("/imagenes/dummy/dead-piernas", 3);
		cuerpoDD = configurarImagen("/imagenes/dummy/dead-cuerpo", 3);
		cabezaDD = configurarImagen("/imagenes/dummy/dead-cabeza", 3);
		manosDD = configurarImagen("/imagenes/dummy/dead-manos", 3);
    	
        shieldUp = configurarImagen("/estados/escudo-up", 4);
        swordUp = configurarImagen("/estados/espada-up", 4);
        bootUp = configurarImagen("/estados/bota-up", 4);
        blockUp = configurarImagen("/estados/mano-up", 4);
        dashUp = configurarImagen("/estados/dash-up", 4);
        bulleyeUp = configurarImagen("/estados/diana-up", 4);
        shieldDown = configurarImagen("/estados/escudo-down", 4);
        swordDown = configurarImagen("/estados/espada-down", 4);
        bootDown = configurarImagen("/estados/bota-down", 4);
        blockDown = configurarImagen("/estados/mano-down", 4);
        dashDown = configurarImagen("/estados/dash-down", 4);
        bulleyeDown = configurarImagen("/estados/diana-down", 4);
        bleed = configurarImagen("/estados/sangre", 4);
        burn = configurarImagen("/estados/fuego", 4);
        
        fragile = configurarImagen("/estados/quebrado", 4);
        mateVerde = configurarImagen("/efectos/mate-bota", 4);
        mateRojo = configurarImagen("/efectos/mate-espada", 4);
        mateAzul = configurarImagen("/efectos/mate-escudo", 4);
        mateAmarillo = configurarImagen("/efectos/mate-op", 4);
        
        corte1 = configurarImagen("/impacto/corte-1", 3);
        corte2 = configurarImagen("/impacto/corte-2", 3);
        corte3 = configurarImagen("/impacto/corte-3", 3);
        corte4 = configurarImagen("/impacto/corte-4", 3);
        corte5 = configurarImagen("/impacto/corte-5", 3);
        
        golpe1 = configurarImagen("/impacto/golpe-1", 3);
        golpe2 = configurarImagen("/impacto/golpe-2", 3);
        golpe3 = configurarImagen("/impacto/golpe-3", 3);
        golpe4 = configurarImagen("/impacto/golpe-4", 3);
        golpe5 = configurarImagen("/impacto/golpe-5", 3);
        
        anular = configurarImagen("/efectos/stun", 3);
        piso = configurarImagen("/efectos/floor", 4);
    	flash = configurarImagen("/efectos/flash", 3);
    	piedra = configurarImagen("/efectos/stone", 3);
    	estandarteHalcon = configurarImagen("/imagenes/accesorios/estandarte-halcon", 3);
    }

    private BufferedImage configurarImagen(String rutaImagen, int escala) throws IOException {
        Utilidades uTool = new Utilidades();
        BufferedImage imagen = ImageIO.read(getClass().getResourceAsStream(rutaImagen + ".png"));
        return uTool.escalarImagen(imagen, imagen.getWidth() / 2 * escala, imagen.getHeight() / 2 * escala);
    }
}
