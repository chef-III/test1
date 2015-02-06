package google_speech;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.net.MalformedURLException;
import java.net.URL;
import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;

public class SimpleTextToSpeech {
	private static final String TEXT_TO_SPEECH_SERVICE = "http://translate.google.com/translate_tts";
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:11.0) "
			+ "Gecko/20100101 Firefox/11.0";

	public static void main(String[] args) throws Exception {
		String lang = "de";

		String language = lang;
		String text1 = "Warnung. Hüllenbruch auf Deck 9";
		String gf = "\"";
		String text = gf + text1 + gf;
		text = URLEncoder.encode(text, "utf-8");
		new SimpleTextToSpeech().go(language, text);
	}

	public void go(String language, String text) throws Exception {
		// Create url based on input params
		String strUrl = TEXT_TO_SPEECH_SERVICE + "?" + "tl=" + language + "&q="
				+ text;
		URL url = new URL(strUrl);

		// Etablish connection
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		// Get method
		connection.setRequestMethod("GET");
		// Set User-Agent to "mimic" the behavior of a web browser. In this
		// example, I used my browser's info
		connection.addRequestProperty("User-Agent", USER_AGENT);
		connection.connect();

		// Get content
		BufferedInputStream bufIn = new BufferedInputStream(
				connection.getInputStream());
		byte[] buffer = new byte[1024];
		int n;
		ByteArrayOutputStream bufOut = new ByteArrayOutputStream();
		while ((n = bufIn.read(buffer)) > 0) {
			bufOut.write(buffer, 0, n);
		}

		String dir = "F:\\Speeches\\";
		String songName = "output.mp3";
		String path = dir + songName;
		File output = new File(path);
		BufferedOutputStream out = new BufferedOutputStream(
				new FileOutputStream(output));
		out.write(bufOut.toByteArray());
		out.flush();
		out.close();

		BasicPlayer player = new BasicPlayer();
		try {
			player.open(new URL("file:///" + path));
			player.play();
		} catch (BasicPlayerException | MalformedURLException e) {
			e.printStackTrace();
		}

		System.out.println("Fertsch");
	}

}