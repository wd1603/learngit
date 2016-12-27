package net.sf.calc;

public class FigureOut {

	public void run(int feeYear, int payYears,int beginAge, int endAge, double interestRate) {
		double paidAmount = 0.00d;
		double valueAmount = 0.00d;
		for (int age=beginAge;age<=endAge;age++) {
			int passedYear = age-beginAge;//0-19共20次
			double interest = 0.00d;
			if (passedYear>0){
				interest = valueAmount * interestRate;
				valueAmount += interest;
			}
			if (passedYear < 20) {
				paidAmount += feeYear;
				valueAmount += feeYear;
			}
			System.out.println(String.format("year:%d, age:%d, paidAmount:%f, interest:%f, valueAmount:%f", passedYear, age, paidAmount, interest, valueAmount));
		}
		
	}
	
	
	public static void main(String[] args) {
		FigureOut fo = new FigureOut();
		//fo.run(7344, 20, 33, 70, 0.026d);//华夏关爱宝, 
		//fo.run(3170, 20, 33, 70, 0.05625d);//阳光随e保, 20年花63400。如果20年以外出险，保险公司需要年利率5.625%，才能在我70岁的时候通过利息赚回30万。20年内出险保险公司赔钱。
		fo.run(4174, 20, 33, 70, 0.05d);
	}

}
