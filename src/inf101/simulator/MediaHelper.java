package inf101.simulator;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MediaHelper {
	private final static Map<String, Image> images = new HashMap<>();
	private final static Map<String, MediaPlayer> sounds = new HashMap<>();
	private final static List<String> imagePath = Arrays.asList("", "images/");
	private final static List<String> soundPath = Arrays.asList("", "sounds/");

	public static Image getImage(String fileName) {
		Image img = images.get(fileName);

		if (img != null) {
			return img;
		}

		for (String p : imagePath) {
			img = tryLoadImage(p + fileName);
			if (img != null) {
				break;
			}
		}

		if (img == null) // fallback
			img = new Image(fileName);
		
		images.put(fileName, img);
		
		return img;
	}

	private static Image tryLoadImage(String pathName) {
		InputStream stream = MediaHelper.class.getResourceAsStream(pathName);
		if (stream == null)
			return null;
		else
			return new Image(stream);
	}
	public static MediaPlayer getSound(String fileName) {
		
		MediaPlayer sound = sounds.get(fileName);

		if (sound != null) {
			return sound;
		}

		for (String p : soundPath) {
			sound = tryLoadSound(p + fileName);
			if (sound != null) {
				break;
			}
		}

		if (sound == null) // fallback
			throw new IllegalStateException("Sound " + fileName + " does not exist");
		
		sounds.put(fileName, sound);
		
		return sound;
	}
	
	private static MediaPlayer tryLoadSound(String pathName) {
		URL url = MediaHelper.class.getResource(pathName);
		if (url == null)
			return null;
		Media media = new Media(url.toString());
		if (media == null)
			return null;
		else
			return new MediaPlayer(media);
	}
}
