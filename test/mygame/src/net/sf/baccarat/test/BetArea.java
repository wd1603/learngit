package net.sf.baccarat.test;

public enum BetArea {
	// 1:庄 2:闲 3:和 4:庄对 5:闲对
	/**
	 * 庄
	 */
	banker((byte) 1),
	/**
	 * 闲
	 */
	player((byte) 2),
	/**
	 * 和
	 */
	tie((byte) 3),
	/**
	 * 庄对
	 */
	bankerPair((byte) 4),
	/**
	 * 闲对
	 */
	playerPair((byte) 5);

	private byte ind;

	BetArea(byte ind) {
		this.ind = ind;
	}

	public byte getInd() {
		return ind;
	}

	public void setInd(byte ind) {
		this.ind = ind;
	}

	public static BetArea getBeatArea(byte ind) {
		BetArea betArea = null;
		for (BetArea item : BetArea.values()) {
			if (item.getInd() == ind) {
				betArea = item;
				break;
			}
		}
		return betArea;
	}
	public static void main(String[] args) {
		System.out.println(BetArea.getBeatArea((byte) 2));
	}

}
