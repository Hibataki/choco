package choco;

import java.text.NumberFormat;

public class Gemochi {
	public static String BR = System.lineSeparator();

	/**
	 * まておれのげもち
	 * @return げもち
	 */
	public static String gemochi() {
		String str = "まて" + bbop("おれのげもち") + BR +
				"　  　彡⌒ミ" + BR +
				"-=≡（ ՞ةڼ◔ ） 　　三(    ՞ةڼ◔)" + BR +
				"-=≡　/　つ_つ 　　　　三(    ՞ةڼ◔)"+ BR+
				"　-=  人　　Y 　　三(    ՞ةڼ◔)"+BR+
				"　　レ' ＼_フ";
		return str;
	}

	/**
	 * 某氏リスペクトbbop
	 * @param str bbop
	 * @return bbop
	 */
	public static String bbop(String str) {
		char[] chars = str.toCharArray();
		StringBuilder build = new StringBuilder();
		int length = chars.length;
		int count = 0;
		for (int i = 0; i < length; i++) {
			char c = chars[random(length)];
			build.append(c);
			if (c == chars[i]) {
				count++;
			}
		}

		double d = count != 0 ? 100.0 / length * count : 0;
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		build.append(" (").append(nf.format(d)).append("%)");
		return build.toString();
	}

	/**
	 * 0からi(最大10)までの擬似乱数を取得するメソッド
	 * @param i 上限値
	 * @return 上限値までの擬似乱数
	 */
	public static int random(int i) {
		if (i > 10) {
			throw new IllegalArgumentException();
		}
		int rand;
		for (;;) {
			rand = (int) (Math.random() * 10);
			if (rand < i) {
				return rand;
			}
		}
	}
}
