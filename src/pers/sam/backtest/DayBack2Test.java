package pers.sam.backtest;

import java.text.SimpleDateFormat;
import java.util.List;

import pers.sam.dto.StockKLinePriceDTO;
import pers.sam.util.GetStockDataFromSqliteUtil;

public class DayBack2Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String stockCode ="000100";
		
		List<StockKLinePriceDTO> priceList = GetStockDataFromSqliteUtil.getDayStockData(stockCode,"","");
		
		/**
		 * 
		 * ������ԣ�macdС��0������macd������һ��Ķ�
		 * �������ԣ�macd����0������macd����һ��Ķ�
		 * 
		 */
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		String lastAction = "sell";
		
		Double totalMoney = new Double(10000.00);
		Double assetValue = new Double(10000.00);
		
		Double stockValue = new Double(0);
		Double stockNum = new Double(0);
		Double cashValue = new Double(0);
		
		for(int i = 0;i<priceList.size();i++){
			StockKLinePriceDTO today = priceList.get(i);
			
			if(lastAction.equals("sell")&&today.getMacd()>0){
				continue;
			}else if(lastAction.equals("buy")&&today.getMacd()<0){
				continue;
			}else if(lastAction.equals("sell")&&today.getMacd()<0){
				
				//�۲��Ƿ��������
				StockKLinePriceDTO lastday = priceList.get(i-1);
				
				if(today.getMacd()>lastday.getMacd()){//��ʼ����,����
					
					Double dealPrice = getTradePrice(today);
					totalMoney = assetValue;
					stockNum = Math.floor(totalMoney/dealPrice);
					cashValue = totalMoney - dealPrice*stockNum;
					stockValue =dealPrice*stockNum;
					assetValue = cashValue+stockValue;
					lastAction ="buy";
					
					System.out.println("���ڣ�"+sdf.format(today.getDay())+"����"+stockNum+"��," +
							"ÿ�ɼ۸�"+dealPrice+","+
							"�ֽ��ܼ�ֵ"+cashValue+","+
							"��Ʊ�ܼ�ֵ"+stockValue+","+
							"�ʲ��ܼ�ֵ"+assetValue);
				}
			}else if(lastAction.equals("buy")&&today.getMacd()>0){
				
				//�۲��Ƿ���������
				StockKLinePriceDTO lastday = priceList.get(i-1);
				if(today.getMacd()<lastday.getMacd()){
					Double dealPrice = getTradePrice(today);
					stockValue = dealPrice*stockNum;
					cashValue = cashValue + stockValue;
					stockValue = new Double(0);
					assetValue = cashValue+stockValue;
					lastAction ="sell";
					
					System.out.println("���ڣ�"+sdf.format(today.getDay())+"����"+stockNum+"��," +
							"ÿ�ɼ۸�"+dealPrice+","+
							"�ֽ��ܼ�ֵ"+cashValue+","+
							"��Ʊ�ܼ�ֵ"+"0"+","+
							"�ʲ��ܼ�ֵ"+assetValue);
				}
			}
		}
	}
	
	public static Double getTradePrice(StockKLinePriceDTO dto){
//		Double dealPrice = (dto.getOpen()+dto.getClose()+dto.getHigh()+dto.getLow())/4;
		Double dealPrice = (dto.getOpen()+dto.getClose())/2;
		return dealPrice;
	}

}
