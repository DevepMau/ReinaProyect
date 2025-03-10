package principal;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Teclado implements KeyListener {

	PanelDeJuego pdj;
	public boolean UP, DOWN, RIGHT, LEFT, ENTER, ESCAPE, Z, X, C;
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
		if(pdj.estadoDeJuego == 1) {
			if(codigo == KeyEvent.VK_W) {
				//pdj.combate.dañar = true;
			}
		}
		if(codigo == KeyEvent.VK_Z) {
			Z = true;
		}
		if(codigo == KeyEvent.VK_X) {
			X = true;
		}
		if(codigo == KeyEvent.VK_C) {
			C = true;
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

		if(codigo == KeyEvent.VK_Z) {
			Z = false;
		}
		if(codigo == KeyEvent.VK_X) {
			X = false;
		}
		if(codigo == KeyEvent.VK_C) {
			C = false;
		}
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