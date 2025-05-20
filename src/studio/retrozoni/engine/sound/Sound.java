package studio.retrozoni.engine.sound;

import kuusisto.tinysound.TinySound;
import studio.retrozoni.engine.Configs;

import java.util.HashMap;
import java.util.Map;

public class Sound {
	
	private static Map<String, kuusisto.tinysound.Sound> sounds;

	public static synchronized void load() {
		TinySound.init();
        if(!TinySound.isInitialized())
			throw new RuntimeException("Error on load sounds!");
		TinySound.setGlobalVolume(1f);
		if(sounds == null)
			loadSounds();
	}

	private static synchronized void loadSounds() {
		sounds = new HashMap<>();
		Sounds[] names = Sounds.values();
		for(Sounds name : names) {
			kuusisto.tinysound.Sound sound = TinySound.loadSound(studio.retrozoni.engine.Engine.resourcesPath + "/audio/" + name.resource() + ".wav");
			sounds.put(name.resource(), sound);
		}
	}
	
	public static void play(Sounds sound) {
		if(!sounds.containsKey(sound.resource()))
			throw new RuntimeException("sound not exists");
		sounds.get(sound.resource()).play((double) Configs.Volum() / 100d);
	}

	public static void play(Sounds sound, double pan) {
		if(!sounds.containsKey(sound.resource()))
			throw new RuntimeException("sound not exists");
		sounds.get(sound.resource()).play((double) Configs.Volum() / 100d, pan);
	}

	public static void stopAllSounds() {
		if (sounds != null) {
			for (kuusisto.tinysound.Sound sound : sounds.values()) {
				sound.stop();
			}
		}
	}

	public static void stopAll() {
		stopAllSounds();
	}
	public static void dispose() {
		TinySound.shutdown();
	}

}
