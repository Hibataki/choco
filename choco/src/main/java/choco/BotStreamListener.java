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
	private static final String CHOCO = "ﾊｨ ﾁｮｺ(＊´∀`)ノ■ 阿賀野を可愛がってくれてありがと！ これからも頑張っちゃうからよろしくねっ！ きらり～ん☆彡";
	private Twitter twitter;
	private LongPredicate lp;
	private Predicate<String> ps;
	private Predicate<String> ps2;

	public BotStreamListener(Twitter twitter, long id) {
		this.twitter = twitter;
		this.lp = s -> s != id;
		this.ps = x -> x.contains("@hai_choco_agano チョコ");
		this.ps2 = x -> x.contains("@hai_choco_agano gemochi");
	}

	@Override
	public void onStatus(Status status) {
		try {
			if (ps.test(status.getText())) {
				twitter.updateStatus(getChoco(status, CHOCO));
			} else if (ps2.test(status.getText())) {
				twitter.updateStatus(getChoco(status, Gemochi.gemochi()));
			}
		} catch (TwitterException ignore) {
		}
	}

	@Override
	public void onFollow(User source, User from) {
		try {
			if (lp.test(source.getId()))
				twitter.createFriendship(source.getId());
		} catch (TwitterException ignore) {
		}
	}

	private StatusUpdate getChoco(Status status, String str) {
		return new StatusUpdate("@" + status.getUser().getScreenName() + " " + str).inReplyToStatusId(status.getId());
	}
}
