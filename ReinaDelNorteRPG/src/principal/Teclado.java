package principal;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Teclado implements KeyListener {

	PanelDeJuego pdj;
	public boolean UP, DOWN, RIGHT, LEFT, ENTER, ESCAPE;
	//DEBUG
	boolean comprobarTiempoDeDibujado = false;
	
	public Teclado(PanelDeJuego pdj) {
		this.pdj = pdj;
	}

	@Override
	public void keyTyped(KeyEvent e) {	
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		int codigo = e.getKeyCode();
		
		//MODO COMBATE
		if(pdj.estadoDeJuego == pdj.COMBATE) {
			if(codigo == KeyEvent.VK_RIGHT) {
				pdj.combate.numeroOpcion++;
				if(pdj.combate.numeroOpcion > 7) {
					pdj.combate.numeroOpcion = 0;
				}
			}
			if(codigo == KeyEvent.VK_LEFT) {
				pdj.combate.numeroOpcion--;
				if(pdj.combate.numeroOpcion < 0) {
					pdj.combate.numeroOpcion = 7;
				}
			}
			if(codigo == KeyEvent.VK_ENTER) {
				if(pdj.combate.opcionElegida == -1) {
					pdj.combate.opcionElegida = pdj.combate.numeroOpcion;
				}
			}
			if(codigo == KeyEvent.VK_ESCAPE) {
				pdj.combate.opcionElegida = -1;
			}
		}
		
		if(codigo == KeyEvent.VK_UP) {
			UP = true;
		}
		if(codigo == KeyEvent.VK_DOWN) {
			DOWN = true;
		}
		if(codigo == KeyEvent.VK_LEFT) {
			LEFT = true;
		}
		if(codigo == KeyEvent.VK_RIGHT) {
			RIGHT = true;
		}
		if(codigo == KeyEvent.VK_ENTER) {
			ENTER = true;
		}
		if(codigo == KeyEvent.VK_ESCAPE) {
			ESCAPE = true;
		}
		//MODO DEBUG
		if(codigo == KeyEvent.VK_T) {
			if(comprobarTiempoDeDibujado == false) {
				comprobarTiempoDeDibujado = true;
			}
			else if(comprobarTiempoDeDibujado == true) {
				comprobarTiempoDeDibujado = false;
			}
		}		

	}

	@Override
	public void keyReleased(KeyEvent e) {
		int codigo = e.getKeyCode();

		if(codigo == KeyEvent.VK_UP) {
			UP = false;
		}
		if(codigo == KeyEvent.VK_DOWN) {
			DOWN = false;
		}
		if(codigo == KeyEvent.VK_RIGHT) {
			RIGHT = false;
		}
		if(codigo == KeyEvent.VK_LEFT) {
			LEFT = false;
		}
		if(codigo == KeyEvent.VK_ENTER) {
			ENTER = false;
		}
		if(codigo == KeyEvent.VK_ESCAPE) {
			ESCAPE = false;
		}


	}

}