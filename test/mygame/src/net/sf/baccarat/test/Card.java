package net.sf.baccarat.test;

import java.io.Serializable;

public class Card implements Comparable<Card>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static String[] COLOR = {"方块", "梅花", "红桃", "黑桃"};

	private static String[] NUM = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};

	private static int[] POINT = {1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 0, 0, 0};

	// 牌值,用于初始化0-51
	public int value;
	// 花色
	public int color;
	// 牌面大小。
	public int number;
	// 点数
	public int point;

	// 角度
	public int angle;

	// 距离
	public int distance;

	// 状态 1:咪 2:亮开 3:合着
	public CardStatus status = CardStatus.closed;

	public boolean isOpen = false;

	public Card(int value) {
		this.value = value;
		if (value < 52) {
			color = value / NUM.length;
			number = value % NUM.length;
			point = POINT[number];
		}

	}

	/**
	 * 得到牌值 0- 51
	 * 
	 * @return
	 */
	public int getValue() {
		return value;
	}

	public int getColor() {
		return color;
	}

	/**
	 * 获取牌值 0-12 对应牌值为 3-2
	 * 
	 * @return
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * 比较第一张牌是否大于第二张牌
	 * 
	 * @param c1
	 * @param c2
	 * @return
	 */
	public static int compare(Card c1, Card c2) {
		return (c1.getValue() % 13) - (c2.getValue() % 13);
	}

	@Override
	public int compareTo(Card arg0) {
		return (arg0.getValue() % 13) - (this.getValue() % 13);
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public int getAngle() {
		return angle;
	}

	public void setAngle(int angle) {
		this.angle = angle;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public CardStatus getStatus() {
		return status;
	}

	public void setStatus(CardStatus status) {
		this.status = status;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	public String toString() {
		return COLOR[color] + ":" + NUM[number] + ":" + POINT[number] + "(" + status + ")";
	}
	public static void main(String[] args) {
		Card card = new Card(51);
		System.out.println(card.getNumber());
		System.out.println(card.getColor());
		System.out.println(card);

	}
}