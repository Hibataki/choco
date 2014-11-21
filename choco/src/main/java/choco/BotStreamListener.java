package choco;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
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

	public BotStreamListener(Twitter twitter, long id, File file) {
		this.twitter = twitter;
		this.id = id;
		this.file = file;
	}

	@Override
	public void onStatus(Status status) {
		String text = status.getText();
		if (text != null && !text.contains("RT")) {
			try {
				if (status.getUser().getId() == id) {
					adminCommand(text, status);
				} else if (text.contains("(@hai_choco_agano") && text.endsWith(")")) {
					twitter.updateStatus(getStatus(status, UpdateName.name(twitter, text)));
				} else if (text.contains("@hai_choco_agano チョコ")) {
					twitter.updateStatus(getStatus(status, Choco.choco()));
				} else if (text.contains("@hai_choco_agano gemochi")) {
					twitter.updateStatus(getStatus(status, Gemochi.gemochi()));
				}
			} catch (TwitterException e) {
				LOGGER.info(() -> e.getMessage());
			}
		}
	}

	private void adminCommand(String text, Status status) throws TwitterException {
		if (text.startsWith("add")) {
			text = text.replaceAll("\r", "").replaceAll("\n", "\\\\n").substring(4);
			twitter.destroyStatus(status.getId());
			try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8))) {
				bw.write(text);
				bw.newLine();
				twitter.sendDirectMessage(id, text.substring(0, 20) + "...のツイートが正常に登録されました");
			} catch (IOException e) {
				LOGGER.warning(() -> e.getMessage());
				twitter.sendDirectMessage(id, text.substring(0, 20) + "...のツイートの登録に失敗しました");
			}
		}
		// TODO delete, editの追加
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

	private StatusUpdate getStatus(Status status, String str) {
		return new StatusUpdate("@" + status.getUser().getScreenName() + " " + str).inReplyToStatusId(status.getId());
	}
}
