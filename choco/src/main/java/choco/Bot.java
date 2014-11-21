package choco;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import twitter4j.Twitter;
import twitter4j.TwitterException;

public class Bot implements Runnable {
	private final Twitter twitter;
	private final long sleepSecond;
	private final File file;
	private boolean closed = false;
	private List<String> list = new ArrayList<>();

	public Bot(Twitter twitter, int sleepSecond, File file) {
		this.twitter = twitter;
		this.sleepSecond = sleepSecond * 1000;
		this.file = file;
	}

	@Override
	public void run() {
		while (!closed) {
			try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
				String line;
				while ((line = br.readLine()) != null) {
					list.add(line.replaceAll("\\\\n", "\n"));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			Collections.shuffle(list);
			for (int i = 0; i < 6; i++) {
				try {
					twitter.updateStatus(list.get(i));
					Thread.sleep(sleepSecond);
				} catch (InterruptedException | TwitterException e) {
				}
			}
		}
	}

	public void close() {
		this.closed = true;
	}
}
