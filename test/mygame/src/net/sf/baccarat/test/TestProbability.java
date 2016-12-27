package net.sf.baccarat.test;

import org.apache.log4j.Logger;

import com.tongqu.game.framework.util.TwoTuple;

/**
 * 测试概率.
 * 
 * @author WYR
 *
 */
public class TestProbability {

	private static Logger logger = Logger.getLogger(TestProbability.class);
	
	private BaccaratCard baccaratCard = null;
	
	public TestProbability () {
		this.baccaratCard = new BaccaratCard();
	}
	
	private void init() {
		baccaratCard.shuffleCards();
	}
	
	private boolean isEnd() {
		return (baccaratCard.getLeftCardsCnt() < 6);
	}
	
	private void dealCard() {
		int playerCardCnt = baccaratCard.getPlayerCardCnt();
		if (playerCardCnt == 0) {
			// 同时发两张牌
			baccaratCard.dealCard();

			// 庄对或闲对
			if (baccaratCard.isBankerPair()) {
//				tableLogic.betAreaPairBalance(table, BetArea.bankerPair);
			}
			if (baccaratCard.isPlayerPair()) {
//				tableLogic.betAreaPairBalance(table, BetArea.playerPair);
			}
		} else {
			if (baccaratCard.getNextDealCardArea() == null) {
				baccaratCard.isNextDealCard();
			}
			Card[] cardArr = new Card[1];
			if (baccaratCard.getNextDealCardArea() == BetArea.player) {
				baccaratCard.dealPlayerCard();
			} else if (baccaratCard.getNextDealCardArea() == BetArea.banker) {
				baccaratCard.dealBankerCard();
			} else {
				logger.info("不应该继续发牌！！");
			}
		}
	}
	
	private void recordResult(byte status, boolean isPlayerPair, boolean isBankerPair) {
		ResultRecord resultRecord = baccaratCard.getResultRecord();
		resultRecord.recordBgOc(resultRecord.getStatus(status));
		resultRecord.recordSimOc(resultRecord.getStatus(status));
		resultRecord.recordSimPairOc(resultRecord.getStatus(status), isPlayerPair, isBankerPair);
	}
	
	private int[] getLastResultStatus(int length) {
		ResultRecord resultRecord = baccaratCard.getResultRecord();
		int[][] simpleResult = resultRecord.getSimpleResult();
		
		int[] ret = new int[length];
		int idx = 0;
		for (int col=simpleResult[0].length-1;col>=0;col--) {
			for (int row=simpleResult.length-1;row>=0;row--) {
				if (idx >= length) {
					break;
				}
				if (simpleResult[row][col] > 0) {
					ret[idx] = simpleResult[row][col];
					idx += 1;
				}
			}
		}
		return ret;
	}
	
	void run(int timesOfTurn, Strategy strategy) {
		for (int i=1;i<=timesOfTurn;i++) {
			init();
			for (int j=1;!isEnd();j++) {
				//发牌阶段
				dealCard();
				TwoTuple<Boolean, BetArea> result = baccaratCard.isNextDealCard();
				logger.info("turn=" + i + ", set=" + j + ", 0次发牌: 庄区点数" + baccaratCard.getBankerAreaPoint() + " 闲区点数" + baccaratCard.getPlayerAreaPoint());
				for (int k=1;result.first;k++) {
					dealCard();
					result = baccaratCard.isNextDealCard();
					logger.info("turn=" + i + ", set=" + j + ", " + k + "次发牌: 庄区点数" + baccaratCard.getBankerAreaPoint() + " 闲区点数" + baccaratCard.getPlayerAreaPoint());
				}
				
				//算结果
				byte status = 0;
				boolean isBankerWin = baccaratCard.isBankerWin();
				boolean isTie = baccaratCard.isTie();
				if (isTie) {
					//tableLogic.tieAreaBalance(table);
					status = BetArea.tie.getInd();
					logger.info(String.format("turn=%d, set=%d, 和局;", i, j));
				} else if (isBankerWin) {
					//tableLogic.bankerAreaBalance(table);
					status = BetArea.banker.getInd();
					logger.info(String.format("turn=%d, set=%d, 庄赢;", i, j));
				} else {
					//tableLogic.playerAreaBalance(table);
					status = BetArea.player.getInd();
					logger.info(String.format("turn=%d, set=%d, 闲赢;", i, j));
				}
				baccaratCard.init();
				
				//录单
				recordResult(status, baccaratCard.isPlayerPair(), baccaratCard.isBankerPair());
				
				//出牌策略
				int[] recentResults = getLastResultStatus(strategy.getLength());
				strategy.decide(recentResults, i, j);
				logger.info("\n\n");
			}
		}
	}


	public static void main(String[] args) {
		TestProbability tp = new TestProbability();
		Strategy strategy = new Strategy();
		tp.run(10000, strategy);
		strategy.printCoin();
	}

}

class Strategy {
	
	private static Logger logger = Logger.getLogger(Strategy.class);
	/** 闲赢 */
	private static final int PLAYERWIN = 2;
	/** 庄赢 */
	private static final int BANKERWIN = 1;
	/** 和 */
	private static final int TIE = 3;
	/** 上局闲赢后和1 (和2 7) */
	private static final int PLAYERWINTIE = 5;
	/** 上局庄赢后和1 (和2 6) */
	private static final int BANKERWINTIE = 4;
	
	private final int unit = 200;
	private int coin = 100000;
	
	int getLength(){
		return 5;
	}
	
	double getOdds(int status) {
		if (status > 3) {
			logger.warn(String.format("status=%d这是什么鬼？", status));
		}
		switch (status) {
			case BANKERWIN : return 0.95d;
			case PLAYERWIN : return 1.00d;
			default : return 0.00d;
		}
		
	}
	
	void decide(int[] recentResult, int turnID, int setID) {
		if ( recentResult[1]==recentResult[2] && recentResult[2]==recentResult[3] && recentResult[3]==recentResult[4] && recentResult[4]>0) {
			int delta = 0;
			if (recentResult[0]==recentResult[1]) {
				delta = -unit;
			} else {
				delta = (int)(unit * getOdds(recentResult[0]));
			}
			coin += delta;
			logger.info(String.format("\t\t\t\t\tturn=%d, set=%d, 下注=%d, 结果=%d, 剩余coin=%d", turnID, setID, unit, delta, coin));
		}
	}
	
	void printCoin() {
		logger.info(String.format("finally coin=%d", coin));
	}
}
