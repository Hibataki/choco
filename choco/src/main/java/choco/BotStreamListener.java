package choco;

import java.util.function.LongPredicate;
import java.util.function.Predicate;

import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.UserStreamAdapter;

final class BotStreamListener extends UserStreamAdapter {
	private final Twitter twitter;
	private final LongPredicate lp;
	private final Predicate<String> rt;
	private final Predicate<String> ps1;
	private final Predicate<String> ps2;
	private final Predicate<String> ps3;
	private String text;

	public BotStreamListener(Twitter twitter, long id) {
		this.twitter = twitter;
		this.lp = s -> s != id;
		this.rt = x -> x.contains("RT");
		this.ps1 = x -> x.contains("@hai_choco_agano チョコ");
		this.ps2 = x -> x.contains("@hai_choco_agano gemochi");
		this.ps3 = x -> x.contains("(@hai_choco_agano") && x.endsWith(")");
	}

	@Override
	public void onStatus(Status status) {
		text = status.getText();
		if (rt.test(text)) {
			return;
		}
		checkAndPost(ps1, getStatus(status, Choco.choco()));
		checkAndPost(ps2, getStatus(status, Gemochi.gemochi()));
		checkAndPost(ps3, getStatus(status, UpdateName.name(twitter, text)));
	}

	@Override
	public void onFollow(User source, User from) {
		try {
			if (lp.test(source.getId())) {
				Thread.sleep(100);
				twitter.createFriendship(source.getId());
			}
		} catch (TwitterException | InterruptedException ignore) {
		}
	}

	private void checkAndPost(Predicate<String> pre, StatusUpdate stup) {
		try {
			if (pre.test(text)) {
				twitter.updateStatus(stup);
				text = null;
			}
		} catch (TwitterException ignore) {
		}
	}

	private StatusUpdate getStatus(Status status, String str) {
		return new StatusUpdate("@" + status.getUser().getScreenName() + " " + str).inReplyToStatusId(status.getId());
	}
}
