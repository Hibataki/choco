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
import java.util.Random;
import java.util.logging.Logger;

import twitter4j.Twitter;
import twitter4j.TwitterException;

public class Bot implements Runnable {
	private static final Logger LOGGER = Logger.getLogger("Bot");
	private static final long HOUR = 3600000;
	private static final Random RANDOM = new Random(new Random().nextLong());

	private final Twitter twitter;
	private final long sleepSecond;
	private final File file;

	private boolean closed = false;
	private List<String> list;

	public Bot(Twitter twitter, int sleepSecond, File file) {
		this.twitter = twitter;
		this.sleepSecond = sleepSecond * 1000;
		this.file = file;
	}

	@Override
	public void run() {
		while (!closed) {
			list = new ArrayList<>();
			try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
				String line;
				while ((line = br.readLine()) != null) {
					list.add(line.replaceAll("\\\\n", "\n"));
				}
				Collections.shuffle(list);
				LOGGER.info(() -> "Loading TextFile");

				long now = 0;
				while (now < HOUR) {
					twitter.updateStatus(list.get(RANDOM.nextInt(list.size())));
					Thread.sleep(sleepSecond);
					now += sleepSecond;
				}
			} catch (IOException | TwitterException | InterruptedException e) {
				LOGGER.warning(() -> e.getMessage());
			}
		}
	}

	public void close() {
		this.closed = true;
	}
}
