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

	public BotStreamListener(Twitter twitter, long id) {
		this.twitter = twitter;
		this.lp = s -> s != id;
		this.ps = "@hai_choco_agano チョコ"::contains;
	}

	@Override
	public void onStatus(Status status) {
		try {
			if (ps.test(status.getText()))
				twitter.updateStatus(getChoco(status));
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

	private StatusUpdate getChoco(Status status) {
		return new StatusUpdate("@" + status.getUser().getScreenName() + " " + CHOCO).inReplyToStatusId(status.getId());
	}
}
