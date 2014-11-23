package choco;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.UserStreamAdapter;

public final class BotStreamListener extends UserStreamAdapter {
	private static final Logger LOGGER = Logger.getLogger("BotStreamListener");

	private final Twitter twitter;
	private final long id;
	private final File file;
	private Status status;

	public BotStreamListener(Twitter twitter, long id, File file) {
		this.twitter = twitter;
		this.id = id;
		this.file = file;
	}

	@Override
	public void onStatus(Status status) {
		this.status = status;
		checkText();
	}

	private void checkText() {
		String text = status.getText();
		if (text != null && !text.contains("RT")) {
			try {
				if (status.getUser().getId() == id) {
					adminCommand();
				}
				reply();
			} catch (TwitterException e) {
				LOGGER.info(() -> e.getMessage());
			}
		}
	}

	@Override
	public void onFollow(User source, User from) {
		try {
			if (id != source.getId()) {
				Thread.sleep(100);
				twitter.createFriendship(source.getId());
			}
		} catch (TwitterException | InterruptedException e) {
			LOGGER.info(() -> e.getMessage());
		}
	}

	private void reply() throws TwitterException {
		String text = status.getText();
		if (text.contains("チョコ")) {
			getStatus(Choco.choco());
		} else if (text.contains("gemochi")) {
			getStatus(Gemochi.gemochi());
		} else if (text.contains("(@") && text.endsWith(")")) {
			getStatus(UpdateName.name(twitter, text));
		}
	}

	private void getStatus(String str) throws TwitterException {
		twitter.updateStatus(new StatusUpdate("@" + status.getUser().getScreenName() + " " + str).inReplyToStatusId(status.getId()));
	}

	private void adminCommand() throws TwitterException {
		String text = status.getText();
		if (text.startsWith("add")) {
			text = text.replaceAll("\r", "").replaceAll("\n", "\\\\n").substring(4);
			try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8))) {
				bw.write(text);
				bw.newLine();
				twitter.sendDirectMessage(id, "「" + text.substring(0, 20) + "...」のツイートが正常に登録されました");
			} catch (IOException e) {
				LOGGER.warning(() -> e.getMessage());
				twitter.sendDirectMessage(id, "「" + text.substring(0, 20) + "...」のツイートの登録に失敗しました");
			}
			twitter.destroyStatus(status.getId());
		} else if (text.startsWith("@hai_choco_agano delete")) {
			List<String> list = new ArrayList<>();
			try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
				String tx = twitter.showStatus(status.getInReplyToStatusId()).getText().replaceAll("\r", "").replaceAll("\n", "\\\\n");
				String line;
				while ((line = br.readLine()) != null) {
					if (!tx.equals(line)) {
						list.add(line);
					}
				}
				twitter.sendDirectMessage(id, "「" + tx.substring(0, 20) + "...」のツイートが正常に削除されました");
			} catch (IOException e) {
				e.printStackTrace();
			}
			try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
				for (int i = 0; i < list.size(); i++) {
					bw.write(list.get(i));
					bw.newLine();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			twitter.destroyStatus(status.getId());
		}
	}
}
