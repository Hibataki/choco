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
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import twitter4j.Twitter;
import twitter4j.TwitterException;

public class Bot implements Callable<List<String>> {
	private static final Logger LOGGER = Logger.getLogger("Bot");
	private static final long HOUR = 3600000;
	private static final Random INITRANDOM = new Random();

	private final Twitter twitter;
	private final long sleepSecond;
	private final File file;

	public Bot(Twitter twitter, int sleepSecond, File file) {
		this.twitter = twitter;
		this.sleepSecond = sleepSecond * 1000;
		this.file = file;
	}

	public void listPost(List<String> list) {
		Random random = new Random(INITRANDOM.nextLong());
		long now = 0;
		while (now < HOUR) {
			try {
				twitter.updateStatus(list.get(random.nextInt(list.size())));
				LOGGER.info("Posting Tweet.");
				Thread.sleep(sleepSecond);
				now += sleepSecond;
			} catch (TwitterException | InterruptedException e) {
				LOGGER.warning(e.getMessage());
			}
		}
	}

	@Override
	public synchronized List<String> call() {
		List<String> list = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
			String line;
			while ((line = br.readLine()) != null) {
				list.add(line.replaceAll("\\\\n", "\n"));
			}
			Collections.shuffle(list);
			LOGGER.info("Loading TextFile");
		} catch (IOException e) {
			LOGGER.warning(e.getMessage());
		}
		return list;
	}
}
