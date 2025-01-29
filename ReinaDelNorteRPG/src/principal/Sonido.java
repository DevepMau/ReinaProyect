package principal;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sonido {

	Clip clip;
	URL[] urlSonidos = new URL[30];

	public Sonido() {

		urlSonidos[0] = getClass().getResource("/sonidos/menu-scroll.wav");
		urlSonidos[1] = getClass().getResource("/sonidos/menu-select.wav");
		urlSonidos[2] = getClass().getResource("/sonidos/attack-normal.wav");
		urlSonidos[3] = getClass().getResource("/sonidos/attack-critical.wav");
		urlSonidos[4] = getClass().getResource("/sonidos/skill-heal.wav");
		urlSonidos[5] = getClass().getResource("/sonidos/mate-use.wav");
		urlSonidos[6] = getClass().getResource("/sonidos/side-step.wav");
		urlSonidos[7] = getClass().getResource("/sonidos/motivated.wav");
		urlSonidos[8] = getClass().getResource("/sonidos/unmotivated.wav");
		urlSonidos[9] = getClass().getResource("/sonidos/break-shield.wav");

	}

	public void cargarArchivo(int i) {

		try {

			AudioInputStream ais = AudioSystem.getAudioInputStream(urlSonidos[i]);
			clip = AudioSystem.getClip();
			clip.open(ais);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void reproducir() {

		clip.start();

	}

	public void repetir() {

		clip.loop(Clip.LOOP_CONTINUOUSLY);

	}

	public void detener() {

		clip.stop();

	}

}
