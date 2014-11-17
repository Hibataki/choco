package choco;

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

	public BotStreamListener(Twitter twitter, long id) {
		this.twitter = twitter;
		this.id = id;
	}

	@Override
	public void onStatus(Status status) {
		String text = status.getText();
		if (text != null && !text.contains("RT")) {
			try {
				if (text.contains("(@hai_choco_agano") && text.endsWith(")")) {
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
