package choco;

import java.text.NumberFormat;
import java.util.Random;

public final class Gemochi {
	public static final String SEPARATOR = System.lineSeparator();
	private static final NumberFormat FORMAT = NumberFormat.getInstance();
	private static final Random RANDOM;
	static {
		FORMAT.setMaximumFractionDigits(2);
		RANDOM = new Random(new Random().nextLong());
	}

	/**
	 * まておれのげもち
	 * @return げもち
	 */
	public static String gemochi() {
		return new StringBuilder("まて")
		.append(bbop("おれのげもち")).append(SEPARATOR)
		.append("　  　彡⌒ミ").append(SEPARATOR)
		.append("-=≡（ ՞ةڼ◔ ） 　　三(    ՞ةڼ◔)").append(SEPARATOR)
		.append("-=≡　/　つ_つ 　　　　三(    ՞ةڼ◔)").append(SEPARATOR)
		.append("　-=  人　　Y 　　三(    ՞ةڼ◔)").append(SEPARATOR)
		.append("　　レ' ＼_フ").toString();
	}

	/**
	 * 某氏リスペクトbbop
	 * @param str bbop
	 * @return bbop
	 */
	public static String bbop(String str) {
		char[] chars = str.toCharArray();
		StringBuilder build = new StringBuilder();
		int count = 0;

		for (int i = 0; i < chars.length; i++) {
			char c = chars[RANDOM.nextInt(i)];
			build.append(c);
			if (c == chars[i]) {
				count++;
			}
		}

		double d = count != 0 ? 100.0 / chars.length * count : 0;
		return build.append(" (").append(FORMAT.format(d)).append("%)").toString();
	}
}
