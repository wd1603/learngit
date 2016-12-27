package net.sf.baccarat.test;

public enum OutComStatus {
	/**
	 * 闲赢
	 */
	playerWin((byte) 2),
	/**
	 * 庄赢
	 */
	bankerWin((byte) 1),
	/**
	 * 和
	 */
	tie((byte) 3),
	/**
	 * 闲和1
	 */
	playerTie((byte) 4),
	/**
	 * 庄和1
	 */
	bankerTie((byte) 5),
	/**
	 * 闲和2
	 */
	playerTie2((byte) 6),
	/**
	 * 庄和2
	 */
	bankerTie2((byte) 7),
	/**
	 * 闲和3
	 */
	playerTie3((byte) 8),
	/**
	 * 庄和3
	 */
	bankerTie3((byte) 9),
	/**
	 * 闲和4
	 */
	playerTie4((byte) 10),
	/**
	 * 庄和4
	 */
	bankerTie4((byte) 11),
	/**
	 * 闲赢闲对
	 */
	playerPPair((byte) 12),
	/**
	 * 闲赢庄对
	 */
	playerBPair((byte) 13),
	/**
	 * 闲赢闲对庄对
	 */
	playerPBPair((byte) 14),
	/**
	 * 庄赢闲对
	 */
	bankerPPair((byte) 15),
	/**
	 * 庄赢庄对
	 */
	bankerBPair((byte) 16),
	/**
	 * 庄赢闲庄对
	 */
	bankerPBPair((byte) 17),
	/**
	 * 和赢闲对
	 */
	tiePPair((byte) 18),
	/**
	 * 和赢庄对
	 */
	tieBPair((byte) 19),
	/**
	 * 和赢庄闲对
	 */
	tiePBPair((byte) 20);

	private byte status;

	OutComStatus(byte status) {
		this.status = status;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

}
