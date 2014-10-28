package choco;

import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.UserStreamAdapter;

class BotStreamListener extends UserStreamAdapter {
	private static final String CHOCO = "ﾊｨ ﾁｮｺ(＊´∀`)ノ■ 阿賀野を可愛がってくれてありがと！ これからも頑張っちゃうからよろしくねっ！ きらり～ん☆彡";
	private Twitter twitter;
	private long id;

	public BotStreamListener(Twitter twitter, long id) {
		this.twitter = twitter;
		this.id = id;
	}

	@Override
	public void onStatus(Status status) {
		if (status.getText().contains("@hai_choco_agano チョコ")) {
			try {
				twitter.updateStatus(new StatusUpdate("@" + status.getUser().getScreenName() + " " + CHOCO).inReplyToStatusId(status.getId()));
			} catch (TwitterException e) {
			}
		}
	}

	@Override
	public void onFollow(User source, User from) {
		if (source.getId() != id) {
			try {
				twitter.createFriendship(source.getId());
			} catch (TwitterException e) {
			}
		}
	}
}
