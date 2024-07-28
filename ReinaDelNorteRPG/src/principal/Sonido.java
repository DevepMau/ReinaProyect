package principal;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sonido {

	Clip clip;
	URL[] urlSonidos = new URL[30];

	public Sonido() {

		urlSonidos[0] = getClass().getResource("/sonidos/key_sound.wav");

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
