package net.sf.baccarat.test;

import org.apache.log4j.Logger;

public class ResultRecord {
	Logger logger = Logger.getLogger(ResultRecord.class);
	/**
	 * 闲赢
	 */
	private static final int PLAYERWIN = 2;
	/**
	 * 庄赢
	 */
	private static final int BANKERWIN = 1;
	/**
	 * 和
	 */
	private static final int TIE = 3;
	/**
	 * 上局闲赢后和1 (和2 7)
	 */
	private static final int PLAYERWINTIE = 5;
	/**
	 * 上局庄赢后和1 (和2 6)
	 */
	private static final int BANKERWINTIE = 4;
	/**
	 * 总行数
	 */
	private static final int OUTCOMEROW = 6;

	private int[][] simpleOutcome = new int[OUTCOMEROW][18];

	private int[][] bigOutcome = new int[OUTCOMEROW][18];

	private int[][] simPairOutcom = new int[OUTCOMEROW][18];

	private int simpOcNextRow = 0;
	private int simpOcNextCol = 0;

	private int simPairOcNextRow = 0;
	private int simPairOcNextCol = 0;

	private int bgOcRow = 0;
	private int bgOcCol = 0;
	private int bgLastStatus = 0;
	// 记录大路路单甩开的列数
	private int bgOcColExtend = 0;

	public void init() {
		// simpleOutcome = new int[OUTCOMEROW][18];
		// bigOutcome = new int[OUTCOMEROW][18];
		for (int i = 0; i < simpleOutcome.length; i++) {
			for (int j = 0; j < simpleOutcome[i].length; j++) {
				simpleOutcome[i][j] = 0;
			}
		}
		logger.info("路单 init simpleOutcome!");
		for (int i = 0; i < bigOutcome.length; i++) {
			for (int j = 0; j < bigOutcome[i].length; j++) {
				bigOutcome[i][j] = 0;
			}
		}
		logger.info("路单 init bigOutcome!");
		for (int i = 0; i < simPairOutcom.length; i++) {
			for (int j = 0; j < simPairOutcom[i].length; j++) {
				simPairOutcom[i][j] = 0;
			}
		}

		simpOcNextRow = 0;
		simpOcNextCol = 0;
		simPairOcNextRow = 0;
		simPairOcNextCol = 0;
		bgOcRow = 0;
		bgOcCol = 0;
		bgLastStatus = 0;
		bgOcColExtend = 0;
	}

	/**
	 * 扩展数组容量
	 */
	private void bigResultExtend() {
		int col = bigOutcome[0].length;
		int newCol = col + 6;
		int[][] newBigResult = new int[OUTCOMEROW][newCol];

		for (int i = 0; i < OUTCOMEROW; i++) {
			System.arraycopy(bigOutcome[i], 0, newBigResult[i], 0, bigOutcome[i].length);
		}
		setBigResult(newBigResult);
	}

	/**
	 * 记录简单路单
	 * 
	 * @param status
	 */
	public void recordSimOc(OutComStatus status) {
		simpleOutcome[simpOcNextRow][simpOcNextCol] = status.getStatus();
		simpOcNextRow++;
		if (simpOcNextRow == OUTCOMEROW) {
			simpOcNextCol++;
			simpOcNextRow = 0;
		}
	}

	public void recordSimPairOc(OutComStatus status, boolean isPlayerPair, boolean isBankerPair) {
		simPairOutcom[simPairOcNextRow][simPairOcNextCol] = getOutComStatus(status, isPlayerPair, isBankerPair).getStatus();
		simPairOcNextRow++;
		if (simPairOcNextRow == OUTCOMEROW) {
			simPairOcNextCol++;
			simPairOcNextRow = 0;
		}
	}

	/**
	 * 记录大路路单闲赢
	 * 
	 * private void recordBgOcPlayerWin() { // 刚开始记录 if(bgLastStatus == 0) {
	 * bigOutcome[bgOcRow][bgOcCol] = PLAYERWIN; return; }
	 * 
	 * if(bgLastStatus == PLAYERWIN) { bgOcRow ++; // 到达最后一行，需要甩出 if(bgOcRow ==
	 * OUTCOMEROW) { bgOcRow--; bgOcCol++; bgOcColExtend++;
	 * bigOutcome[bgOcRow][bgOcCol] = PLAYERWIN; return; } // 此处已经有数值，需要甩出
	 * if(bigOutcome[bgOcRow][bgOcCol] !=0 ){ bgOcRow--; bgOcCol++;
	 * bgOcColExtend++; bigOutcome[bgOcRow][bgOcCol] = PLAYERWIN; return; }
	 * 
	 * int leftCol = 0; if(bgOcCol > 0) { leftCol = bgOcCol-1; } int nextRow =
	 * 5; if(bgOcRow < 5) { nextRow = bgOcRow+1; }
	 * 
	 * // 查看 左边、下边 都没有相同的 if(bigOutcome[nextRow][bgOcCol] != PLAYERWIN ) {
	 * if(bigOutcome[bgOcRow][leftCol] != PLAYERWIN) {
	 * bigOutcome[bgOcRow][bgOcCol] = PLAYERWIN; } else { bgOcRow--; bgOcCol++;
	 * bgOcColExtend++; bigOutcome[bgOcRow][bgOcCol] = PLAYERWIN; return; } }
	 * else { bgOcRow--; bgOcCol++; bgOcColExtend++;
	 * bigOutcome[bgOcRow][bgOcCol] = PLAYERWIN; return; } }
	 * 
	 * if(bgLastStatus != PLAYERWIN) { bgOcCol -= (bgOcColExtend-1); bgOcRow =
	 * 0; bgOcColExtend = 0;
	 * 
	 * while(bigOutcome[bgOcRow+1][bgOcCol] == PLAYERWIN) { bgOcCol ++; }
	 * bigOutcome[bgOcRow][bgOcCol] = PLAYERWIN; }
	 * 
	 * bgLastStatus = PLAYERWIN; }
	 */

	/**
	 * 记录和记录
	 */
	private void recordBgOcTie() {

		// 刚开始记录
		if (bgLastStatus == 0) {
			bigOutcome[bgOcRow][bgOcCol] = 100;
			bgLastStatus = TIE;
			return;
		}

		if (bgLastStatus == PLAYERWIN) {
			bigOutcome[bgOcRow][bgOcCol] = PLAYERWINTIE;
			bgLastStatus = PLAYERWINTIE;
		} else if (bgLastStatus == BANKERWIN) {
			bigOutcome[bgOcRow][bgOcCol] = BANKERWINTIE;
			bgLastStatus = BANKERWINTIE;
		} else if (bgLastStatus == TIE) {
			bigOutcome[bgOcRow][bgOcCol] += 1;
			bgLastStatus = TIE;
		} else {
			bigOutcome[bgOcRow][bgOcCol] = bigOutcome[bgOcRow][bgOcCol] + 2;
		}
	}

	/**
	 * 记录大路路单闲赢
	 */
	private void recordBgOcWin(int winner) {
		// 刚开始记录
		if (bgLastStatus == 0) {
			bigOutcome[bgOcRow][bgOcCol] = winner;
			bgLastStatus = winner;
			return;
		}
		// 扩展大路路单的数组
		if (bgOcCol == bigOutcome[0].length - 1) {
			bigResultExtend();
		}
		if (bgLastStatus == winner || bgLastStatus == winner + 3) {
			bgOcRow++;
			// 到达最后一行，需要甩出
			if (bgOcRow == OUTCOMEROW) {
				bgOcRow--;
				bgOcCol++;
				bgOcColExtend++;
				bigOutcome[bgOcRow][bgOcCol] = winner;
				bgLastStatus = winner;
				return;
			}
			// 此处已经有数值，需要甩出
			if (bigOutcome[bgOcRow][bgOcCol] != 0) {
				bgOcRow--;
				bgOcCol++;
				bgOcColExtend++;
				bigOutcome[bgOcRow][bgOcCol] = winner;
				bgLastStatus = winner;
				return;
			}

			int leftCol = 0;
			if (bgOcCol > 0) {
				leftCol = bgOcCol - 1;
			}
			int nextRow = 5;
			if (bgOcRow < 5) {
				nextRow = bgOcRow + 1;
			}

			// 查看 左边、下边 都没有相同的
			int nextRowVa = bigOutcome[nextRow][bgOcCol];
			int leftColVa = bigOutcome[bgOcRow][leftCol];
			int nextRowVaHand = bigOutcome[nextRow][bgOcCol]-winner-3;
			int leftColVaHand = bigOutcome[bgOcRow][leftCol]-winner-3;
			
			if (  (nextRowVa != winner &&(nextRowVaHand<0|| (nextRowVaHand>=0 && (nextRowVaHand)%2 !=0)))) {
				if ( (leftColVa != winner &&(leftColVaHand<0 || (leftColVaHand>=0 && (leftColVaHand)%2 !=0)))) {
//			if (bigOutcome[nextRow][bgOcCol] != winner) {
//				if (bigOutcome[bgOcRow][leftCol] != winner) {
					bigOutcome[bgOcRow][bgOcCol] = winner;
				} else {
					bgOcRow--;
					bgOcCol++;
					bgOcColExtend++;
					bigOutcome[bgOcRow][bgOcCol] = winner;
				}
			} else {
				bgOcRow--;
				bgOcCol++;
				bgOcColExtend++;
				bigOutcome[bgOcRow][bgOcCol] = winner;
			}
		} else if (bgLastStatus != winner) {
			bgOcCol -= (bgOcColExtend - 1);
			bgOcRow = 0;
			bgOcColExtend = 0;

//			while (bigOutcome[bgOcRow + 1][bgOcCol] == winner) {
			while (bigOutcome[bgOcRow + 1][bgOcCol] == winner || bigOutcome[bgOcRow][bgOcCol]!=0) {
				bgOcCol++;
			}
			bigOutcome[bgOcRow][bgOcCol] = winner;
		}
		bgLastStatus = winner;
	}

	/**
	 * status 庄赢 闲赢 和
	 * 
	 * @param status
	 */
	public void recordBgOc(OutComStatus status) {
		if (status == OutComStatus.bankerWin || status == OutComStatus.playerWin) {
			recordBgOcWin(status.getStatus());
		} else {
			recordBgOcTie();
		}
	}

	public int[][] getSimpleResult() {
		return simpleOutcome;
	}

	public void setSimpleResult(int[][] simpleResult) {
		this.simpleOutcome = simpleResult;
	}

	public int[][] getBigResult() {
		return bigOutcome;
	}

	public void setBigResult(int[][] bigResult) {
		this.bigOutcome = bigResult;
	}

	public int[][] getSimpleOutcome() {
		return simpleOutcome;
	}

	public void setSimpleOutcome(int[][] simpleOutcome) {
		this.simpleOutcome = simpleOutcome;
	}

	public int[][] getBigOutcome() {
		return bigOutcome;
	}

	public void setBigOutcome(int[][] bigOutcome) {
		this.bigOutcome = bigOutcome;
	}

	public int[][] getSimPairOutcom() {
		return simPairOutcom;
	}

	public void setSimPairOutcom(int[][] simPairOutcom) {
		this.simPairOutcom = simPairOutcom;
	}

	public OutComStatus getStatus(int a) {
		OutComStatus status = null;
		switch (a) {
			case 2 :
				status = OutComStatus.playerWin;
				break;
			case 1 :
				status = OutComStatus.bankerWin;
				break;
			case 3 :
				status = OutComStatus.tie;
				break;
			default :
				break;
		}
		return status;
	}

	/**
	 * 获取路单的状态
	 * 
	 * @param status
	 * @param isPlayerPair
	 * @param isBankerPair
	 * @return
	 */
	static OutComStatus getOutComStatus(OutComStatus status, boolean isPlayerPair, boolean isBankerPair) {
		if (isBankerPair && isPlayerPair) {
			if (status == OutComStatus.tie) {
				return OutComStatus.tiePBPair;
			} else if (status == OutComStatus.playerWin) {
				return OutComStatus.playerPBPair;
			} else if (status == OutComStatus.bankerWin) {
				return OutComStatus.bankerPBPair;
			}
		} else if (isBankerPair) {
			if (status == OutComStatus.tie) {
				return OutComStatus.tieBPair;
			} else if (status == OutComStatus.playerWin) {
				return OutComStatus.playerBPair;
			} else if (status == OutComStatus.bankerWin) {
				return OutComStatus.bankerBPair;
			}
		} else if (isPlayerPair) {
			if (status == OutComStatus.tie) {
				return OutComStatus.tiePPair;
			} else if (status == OutComStatus.playerWin) {
				return OutComStatus.playerPPair;
			} else if (status == OutComStatus.bankerWin) {
				return OutComStatus.bankerPPair;
			}
		} else {
			return status;
		}
		return status;
	}

	public static void main(String[] args) {
		// int[][] array1 = {{1,2,3},{1,4,3}};
		// int[][] array2 = new int[2][4];
		// for(int i=0; i<array1.length; i++) {
		// System.arraycopy(array1[i], 0, array2[i], 0, array1[i].length);
		// }
		// for(int i=0; i<array2.length; i++) {
		// System.out.println(Arrays.toString(array2[i]));
		// }

		// List<Integer> outcome = new ArrayList<Integer>();
		// int[] outcome =
		// {1,1,1,1,1,1,1,1,2,2,2,2,2,1,1,1,1,1,1,1,1,3,2,2,1,3};
//		int[] outcome = {3, 1, 1, 1, 3, 1, 1, 1, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 2, 1, 3};
		// 因为有闲和 后有个闲
//		int[] outcome = {2,2,2,2,2,2,2,2,2,2,2,3, 1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 3, 2, 2, 2, 2};
//		int[] outcome = {2,2,2,2,2,2,2,2,2,2,2,3, 1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 3, 2, 2, 2, 2,1,1,1,1,2,2,2,2};
		// 首行拐弯
//		int[] outcome = {2,2,2,2,2,2,3,2,2,2,2,2,2,2,2,1,2,2,2,3,2,1,2,2,2,2,2,1,1,2,2,2,2,2,2,3,3,1,2,2,2,2,1,2,2,2,1,2,2,2,2,2,2,2};
		int[] outcome = {3,3,3,3,3,2,2,2,2,2,2,3,2,2,2,2,2,2,2,2,1,2,2,2,3,2,1,2,2,2,2,2,1,1,2,2,2,2,2,2,3,3,1,2,2,2,2,1,2,2,2,1,2,2,2,2,2,2,2};
//		int[] outcome = {3,3,3,3,3,3,2,2,2,2,2,2,3,2,2,2,2,2,2,2,2,1,2,2,2,3,2,1,2,2,2,2,2,1,1,2,2,2,2,2,2,3,3,1,2,2,2,2,1,2,2,2};
//		int[] outcome = {3,3,3,2,2,2,2,2,2,3,2,2,2,2,2,2,2,2,1,2,2,2,3,2,1,2,2,2,2,2,1,1};
		ResultRecord record = new ResultRecord();
		for (int i = 0; i < outcome.length; i++) {
			record.recordBgOc(record.getStatus(outcome[i]));
		}
		int[][] bigOutcome = record.getBigResult();
		for (int j = 0; j < bigOutcome.length; j++) {
			for (int i = 0; i < bigOutcome[1].length; i++) {
				System.out.print(bigOutcome[j][i]);
				System.out.print(" ");
			}
			System.out.println();
		}
	}
}
