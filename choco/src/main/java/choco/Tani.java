package choco;

import java.util.Random;

public class Tani {
	private static final Random RANDOM;
	static {
		RANDOM = new Random(new Random().nextLong());
	}

	public static String tani() {
		switch (RANDOM.nextInt(2)) {
		case 0:
			return "落単カードﾒｪｪｧｧｰﾝｗｗｗｗｗ";
		case 1:
			return "単位修得\n\n\n\n\n\n\n\n\n\nなわけないじゃん";
		case 2:
			return "出席が足りませんぞｗｗｗんんｗｗｗｗｗｗｗ";
		default:
			return "単位修得おめでとう";
		}
	}

}
