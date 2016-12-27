package net.sf.baccarat.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.tongqu.game.framework.util.TwoTuple;

/**
 * 百家乐牌
 * 
 * @author Administrator
 * 
 */
public class BaccaratCard {


	private List<Integer> cardVaList = new ArrayList<Integer>();

	/**
	 * 8副牌 = 52*8
	 */
	private static final int len = 416;

	/**
	 * 闲区牌
	 */
	private Card[] playerCard = new Card[3];

	/**
	 * 庄区牌
	 */
	private Card[] bankerCard = new Card[3];

	/**
	 * 庄区牌的张数
	 */
	private int bankerCardCnt = 0;

	/**
	 * 闲区牌的张数
	 */
	private int playerCardCnt = 0;

	/**
	 * 已发牌的索引
	 */
	private int sendCardInd = 0;

	ResultRecord resultRecord = null;

	BetArea nextDealCardArea = null;

	private BetArea showCardArea = BetArea.player;

	/**
	 * 发牌的轮数
	 */
	private int dealCardRound = 1;

	boolean isPlayerPair = false;
	boolean isBankerPair = false;

	public BaccaratCard() {
		for (int i = 0; i < len; i++) {
			cardVaList.add(i);
			Random random = new Random(System.currentTimeMillis());
			Collections.shuffle(cardVaList, random);
		}
		// 创建路单
		resultRecord = new ResultRecord();
	}

	/**
	 * 重新洗牌
	 */
	public void shuffleCards() {
		this.sendCardInd = 0;
		Random random = new Random(System.currentTimeMillis());
		Collections.shuffle(cardVaList, random);
		// 初始化路单
		resultRecord.init();
	}

	/**
	 * 根据8副牌中的数值获取Card对象
	 * 
	 * @param va
	 * @return
	 */
	public Card getCard(int va) {
		va = va % 52;
		return new Card(va);
	}

	/**
	 * 初始化发牌数据
	 */
	private void initDealCardData() {
		for (int i = 0; i < 3; i++) {
			playerCard[i] = null;
			bankerCard[i] = null;
		}
		playerCardCnt = 0;
		bankerCardCnt = 0;
		nextDealCardArea = null;
		showCardArea = BetArea.player;
		dealCardRound = 1;
		isPlayerPair = false;
		isBankerPair = false;
	}

	public void init() {
		// 初始化闲区跟庄区的牌值
		initDealCardData();
	}

	/**
	 * 发牌
	 */
	public void dealCard() {
		initDealCardData();
		// 闲
		playerCard[0] = getCard(this.getCardVaList().get(sendCardInd));
		// playerCard[0] = new Card(0);
		this.sendCardInd++;
		// 庄
		bankerCard[0] = getCard(this.getCardVaList().get(sendCardInd));
		// bankerCard[0] = new Card(0);
		this.sendCardInd++;
		// 闲
		playerCard[1] = getCard(this.getCardVaList().get(sendCardInd));
		// playerCard[1] = new Card(3);
		this.sendCardInd++;
		// 庄
		bankerCard[1] = getCard(this.getCardVaList().get(sendCardInd));
		// bankerCard[1] = new Card(4);
		this.sendCardInd++;
		bankerCardCnt = 2;
		playerCardCnt = 2;
	}

	/**
	 * 给闲发最后一张牌
	 */
	public void dealPlayerCard() {
		// 闲
		playerCard[2] = getCard(this.getCardVaList().get(sendCardInd));
		// playerCard[2] = new Card(6);
		this.sendCardInd++;
		playerCardCnt = 3;
		showCardArea = BetArea.player;
		dealCardRound++;
	}

	/**
	 * 庄发最后一张牌
	 */
	public void dealBankerCard() {
		// 庄
		bankerCard[2] = getCard(this.getCardVaList().get(sendCardInd));
		// bankerCard[2] = new Card(8);
		this.sendCardInd++;
		bankerCardCnt = 3;
		showCardArea = BetArea.banker;
		dealCardRound++;
	}

	/**
	 * 修改牌的状态为正在咪牌
	 */
	public void modifyCardStatusToShowing() {
		if (showCardArea == BetArea.player) {
			for (int i = 0; i < playerCardCnt; i++) {

				if (playerCard[i].getStatus() == CardStatus.closed) {
					playerCard[i].setStatus(CardStatus.show);
				}
			}
		} else {
			for (int i = 0; i < bankerCardCnt; i++) {
				if (bankerCard[i].getStatus() == CardStatus.closed) {
					bankerCard[i].setStatus(CardStatus.show);
				}
			}
		}
	}

	/**
	 * 修改牌的状态为打开
	 * 
	 * @param betArea
	 */
	public void modifyCardStatusToOpen(BetArea betArea) {
		if (betArea == BetArea.player) {
			for (int i = 0; i < playerCardCnt; i++) {
				playerCard[i].setStatus(CardStatus.open);
			}
		} else {
			for (int i = 0; i < bankerCardCnt; i++) {
				bankerCard[i].setStatus(CardStatus.open);
			}
		}
	}

	/**
	 * 获取闲区点数
	 * 
	 * @return
	 */
	public int getPlayerAreaPoint() {
		int point = 0;
		for (int i = 0; i < playerCardCnt; i++) {
			point += playerCard[i].point;
		}
		return point % 10;
	}

	/**
	 * 获取庄区点数
	 * 
	 * @return
	 */
	public int getBankerAreaPoint() {
		int point = 0;
		for (int i = 0; i < bankerCardCnt; i++) {
			point += bankerCard[i].point;
		}
		return point % 10;
	}

	public int getPoint(BetArea betArea) {
		if (betArea == BetArea.banker) {
			return getBankerAreaPoint();
		}
		return getPlayerAreaPoint();
	}

	/**
	 * 判断是否是闲对
	 * 
	 * @return
	 */
	public boolean isPlayerPair() {
		boolean result = false;
		if (playerCard[0] == null || playerCard[1] == null) {
			return result;
		}
		if (playerCard[0].number == playerCard[1].number) {
			isPlayerPair = true;
			result = true;
		}
		return result;
	}

	/**
	 * 判断是否是庄对
	 * 
	 * @return
	 */
	public boolean isBankerPair() {
		boolean result = false;
		if (bankerCard[0] == null || bankerCard[1] == null) {
			return result;
		}
		if (bankerCard[0].number == bankerCard[1].number) {
			isBankerPair = true;
			result = true;
		}
		return result;
	}

	/**
	 * 判断庄区跟闲区是否是“和”
	 * 
	 * @return
	 */
	public boolean isTie() {
		boolean result = false;
		if (getBankerAreaPoint() == getPlayerAreaPoint()) {
			result = true;
		}
		return result;
	}

	/**
	 * 是否是庄赢
	 * 
	 * @return
	 */
	public boolean isBankerWin() {
		boolean result = false;
		if (getBankerAreaPoint() > getPlayerAreaPoint()) {
			result = true;
		}
		return result;
	}

	/**
	 * 判断是否继续发牌，如果发牌将发给谁。
	 * 
	 * @return
	 */
	public TwoTuple<Boolean, BetArea> isNextDealCard() {

		// if(bankerCardCnt==3 && playerCardCnt==3) {
		// return new TwoTuple<Boolean, BetArea>(false, null);
		// }

		if (bankerCardCnt == 3) {
			return new TwoTuple<Boolean, BetArea>(false, null);
		}

		int playerPoint = getPlayerAreaPoint();
		int bankerPoint = getBankerAreaPoint();

		if (playerPoint > 7 && playerCardCnt == 2) {
			return new TwoTuple<Boolean, BetArea>(false, null);
		}
		if (bankerPoint > 7) {
			return new TwoTuple<Boolean, BetArea>(false, null);
		}

		if (playerCardCnt == 2) {

			if (playerPoint == 7) {
				if (bankerPoint == 7 || bankerPoint == 6) {
					return new TwoTuple<Boolean, BetArea>(false, null);
				}
				if (bankerPoint <= 5) {
					nextDealCardArea = BetArea.banker;
					return new TwoTuple<Boolean, BetArea>(true, BetArea.banker);
				}
			}

			if (playerPoint == 6) {
				if (bankerPoint == 7 || bankerPoint == 6) {
					return new TwoTuple<Boolean, BetArea>(false, null);
				}
				if (bankerPoint <= 5) {
					nextDealCardArea = BetArea.banker;
					return new TwoTuple<Boolean, BetArea>(true, BetArea.banker);
				}
			}
			nextDealCardArea = BetArea.player;
			return new TwoTuple<Boolean, BetArea>(true, BetArea.player);
		}

		int playerThirdCardP = playerCard[playerCardCnt - 1].point;
		if (playerCardCnt == 3) {
			// if (bankerPoint == 3 && playerPoint == 8) {
			if (bankerPoint == 3 && playerThirdCardP == 8) {
				return new TwoTuple<Boolean, BetArea>(false, null);
			}
			// if (bankerPoint == 4 && (playerPoint == 1 || playerPoint == 8 ||
			// playerPoint == 9 || playerPoint == 0)) {
			if (bankerPoint == 4 && (playerThirdCardP == 1 || playerThirdCardP == 8 || playerThirdCardP == 9 || playerThirdCardP == 0)) {
				return new TwoTuple<Boolean, BetArea>(false, null);
			}
			// if (bankerPoint == 5 && (playerPoint == 1 || playerPoint == 2 ||
			// playerPoint == 3 || playerPoint == 8 || playerPoint == 9 ||
			// playerPoint == 0)) {
			if (bankerPoint == 5
					&& (playerThirdCardP == 1 || playerThirdCardP == 2 || playerThirdCardP == 3 || playerThirdCardP == 8 || playerThirdCardP == 9 || playerThirdCardP == 0)) {
				return new TwoTuple<Boolean, BetArea>(false, null);
			}
			// if (bankerPoint == 6
			// && (playerPoint == 1 || playerPoint == 2 || playerPoint == 3 ||
			// playerPoint == 4 || playerPoint == 5 || playerPoint == 8
			// || playerPoint == 9 || playerPoint == 0)) {
			if (bankerPoint == 6
					&& (playerThirdCardP == 1 || playerThirdCardP == 2 || playerThirdCardP == 3 || playerThirdCardP == 4 || playerThirdCardP == 5
							|| playerThirdCardP == 8 || playerThirdCardP == 9 || playerThirdCardP == 0)) {
				return new TwoTuple<Boolean, BetArea>(false, null);
			}
			if (bankerPoint == 7) {
				return new TwoTuple<Boolean, BetArea>(false, null);
			}
			nextDealCardArea = BetArea.banker;
			return new TwoTuple<Boolean, BetArea>(true, BetArea.banker);
		}

		return new TwoTuple<Boolean, BetArea>(false, null);
	}

	/**
	 * 获取整幅牌的剩余张数
	 * 
	 * @return
	 */
	public int getLeftCardsCnt() {
		return len - sendCardInd;
	}

	private void printCard() {
	}

	public List<Integer> getCardVaList() {
		return cardVaList;
	}

	public void setCardVaList(List<Integer> cardVaList) {
		this.cardVaList = cardVaList;
	}

	public static int getLen() {
		return len;
	}

	public Card[] getPlayerCard() {
		return playerCard;
	}

	public void setPlayerCard(Card[] playerCard) {
		this.playerCard = playerCard;
	}

	public Card[] getBankerCard() {
		return bankerCard;
	}

	public void setBankerCard(Card[] bankerCard) {
		this.bankerCard = bankerCard;
	}

	public int getBankerCardCnt() {
		return bankerCardCnt;
	}

	public void setBankerCardCnt(int bankerCardCnt) {
		this.bankerCardCnt = bankerCardCnt;
	}

	public int getPlayerCardCnt() {
		return playerCardCnt;
	}

	public void setPlayerCardCnt(int playerCardCnt) {
		this.playerCardCnt = playerCardCnt;
	}

	public ResultRecord getResultRecord() {
		return resultRecord;
	}

	public void setResultRecord(ResultRecord resultRecord) {
		this.resultRecord = resultRecord;
	}

	public BetArea getNextDealCardArea() {
		return nextDealCardArea;
	}

	public void setNextDealCardArea(BetArea nextDealCardArea) {
		this.nextDealCardArea = nextDealCardArea;
	}

	public BetArea getShowCardArea() {
		return showCardArea;
	}

	public void setShowCardArea(BetArea showCardArea) {
		this.showCardArea = showCardArea;
	}

	public int getDealCardRound() {
		return dealCardRound;
	}

	public void setDealCardRound(int dealCardRound) {
		this.dealCardRound = dealCardRound;
	}

	public void setPlayerPair(boolean isPlayerPair) {
		this.isPlayerPair = isPlayerPair;
	}

	public void setBankerPair(boolean isBankerPair) {
		this.isBankerPair = isBankerPair;
	}

	public static void main(String[] args) {
		BaccaratCard card1 = new BaccaratCard();
		for (int i = 0; i < card1.getCardVaList().size(); i++) {
			System.out.print(card1.getCardVaList().get(i) + " ");
		}
		System.out.println();
		System.out.println(card1.getCard(412));
		BaccaratCard card2 = new BaccaratCard();
		for (int i = 0; i < card2.getCardVaList().size(); i++) {
			System.out.print(card2.getCardVaList().get(i) + " ");
		}
	}

}
