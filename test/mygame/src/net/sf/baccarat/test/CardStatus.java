package net.sf.baccarat.test;

public enum CardStatus {
	// 状态 1:咪 2:亮开 3:合着

	/**
	 * 正在咪牌
	 */
	show((byte) 1),
	/**
	 * 牌已经亮开
	 */
	open((byte) 2),
	/**
	 * 牌合着
	 */
	closed((byte) 3);

	private byte status;

	CardStatus(byte status) {
		this.status = status;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

}
