package pers.sam.czsc.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import pers.sam.czsc.core.DivideSectionInterface;
import pers.sam.czsc.core.FindPeakAndBottomInterface;
import pers.sam.czsc.core.FindSegmentInterface;
import pers.sam.czsc.core.impl.DivideSectionImpl1;
import pers.sam.czsc.core.impl.FindPeakAndBottomImpl1;
import pers.sam.czsc.core.impl.FindPeakAndBottomImpl2;
import pers.sam.czsc.core.impl.FindSegmentImpl1;
import pers.sam.czsc.core.impl.FindSegmentImpl3;
import pers.sam.czsc.dto.FeatureElementDTO;
import pers.sam.czsc.dto.MergeLineDTO;
import pers.sam.czsc.dto.TouchDTO;
import pers.sam.czsc.test.Test601600_30min;
import pers.sam.dto.StockKLinePriceDTO;
import pers.sam.util.GetStockDataFromSqliteUtil;
import pers.sam.util.PeriodUtil;
import pers.sam.util.StockDateUtil;

public class ZenTheoryUtil {
	
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	private static Logger logger=Logger.getLogger(ZenTheoryUtil.class);
	
	public static void main(String args[]){
		
		String stockCode ="999999";
		
		List<StockKLinePriceDTO> priceList = GetStockDataFromSqliteUtil.getStockMonthData(stockCode,"1990-12-31","2014-07-30");
		
		/**
		 * �ʲ���ʼ��
		 */
		String lastAction = "sell";
		
		Double totalMoney = new Double(10000.00);
		Double assetValue = new Double(10000.00);
		
		Double stockValue = new Double(0);
		Double stockNum = new Double(0);
		Double cashValue = new Double(0);
		
		String trend = "up";//up
		
		/**
		 * K�ߺϲ������׷�����Ϣ
		 */
		List<MergeLineDTO> mergeLineList = new ArrayList();
		
		//��2004-2-6��ʼ
		StockKLinePriceDTO priceDTO = priceList.get(0);
		MergeLineDTO mergeLineDTO = new MergeLineDTO();
		mergeLineDTO.setStickNumber(1);
		mergeLineDTO.setBeginTime(priceDTO.getBeginTime());
		mergeLineDTO.setEndTime(priceDTO.getEndTime());
		mergeLineDTO.setHigh(priceDTO.getHigh());
		mergeLineDTO.setLow(priceDTO.getLow());
		mergeLineList.add(mergeLineDTO);
		
		/**
		 * ��ʼģ�⣬���׹���
		 */
		
		
		FindPeakAndBottomInterface findPeakAndBottomIntf= new FindPeakAndBottomImpl1();
		for(int i = 1;i<priceList.size();i++){
			
			StockKLinePriceDTO today = priceList.get(i);
//			System.out.println(today.getBeginTime()+" "+today.getEndTime());
			
			//1.K�ߺϲ�,������ڰ�����ϵ
			MergeLineDTO lastMLineDTO = getLastMergeLineDTO(mergeLineList);
			if(isInclusive(lastMLineDTO,
					today.getHigh(),today.getLow())){
				merge(lastMLineDTO,today,trend);
			}else{//�޺ϲ�����ŵ�resultList��
				if(isUp(lastMLineDTO,today.getHigh(),today.getLow())){
				    trend ="up";
				}else if(isDown(lastMLineDTO,today.getHigh(),today.getLow())){
				   	trend ="down";
				}
				MergeLineDTO thisMLineDTO = new MergeLineDTO();
				thisMLineDTO.setStickNumber(1);
				thisMLineDTO.setBeginTime(today.getBeginTime());
				thisMLineDTO.setEndTime(today.getEndTime());
				thisMLineDTO.setHigh(today.getHigh());
				thisMLineDTO.setLow(today.getLow());
				mergeLineList.add(thisMLineDTO);
			}
			
			//2.�����׷���
			findPeakAndBottomIntf.findPeakAndBottom(mergeLineList);
			
			//3.�� ����
//			divideSection(mergeLineList);
			
			//4.�߶λ���
//			List segmentList = getSegment(mergeLineList);
		}
		
		System.out.println(mergeLineList.size());
		
		
		for(int i =0;i<mergeLineList.size();i++){
			MergeLineDTO dto = mergeLineList.get(i);
			
			System.out.println(dto.getBeginTime()+"**"+dto.getIsBottom()+"**"+dto.getIsPeak());
			
			if(dto.getIsPeak().equals("Y")){
				System.out.println(StockDateUtil.SDF_TIME.format(dto.getBeginTime())+"\t"+
						StockDateUtil.SDF_TIME.format(dto.getEndTime())+"\t"+
						"�ϲ�["+dto.getStickNumber()+"]��K��"+"\t"+
						"��["+dto.getLow()+"]["+dto.getHigh()+"]");
			}
			if(dto.getIsBottom().equals("Y")){
				System.out.println(StockDateUtil.SDF_TIME.format(dto.getBeginTime())+"\t"+
						StockDateUtil.SDF_TIME.format(dto.getEndTime())+"\t"+
						"�ϲ�["+dto.getStickNumber()+"]��K��"+"\t"+
						"�� ["+dto.getLow()+"]["+dto.getHigh()+"]");
			}
		}
		
//		divideSection(mergeLineList);
	}
	
	/**
	 * ����������
	 * k�ߺϲ������͡��ʡ��߶�
	 * @param priceList
	 * @throws Exception 
	 */
	public static void parseKLineData(List<StockKLinePriceDTO> priceList) throws Exception{
		
		List<MergeLineDTO> mergeLineList = new ArrayList();
		
		StockKLinePriceDTO priceDTO = priceList.get(0);
		MergeLineDTO mergeLineDTO = new MergeLineDTO();
		mergeLineDTO.setStickNumber(1);
		mergeLineDTO.setBeginTime(priceDTO.getBeginTime());
		mergeLineDTO.setEndTime(priceDTO.getEndTime());
		mergeLineDTO.setHigh(priceDTO.getHigh());
		mergeLineDTO.setLow(priceDTO.getLow());
		mergeLineDTO.getMemberList().add(priceDTO);
		mergeLineList.add(mergeLineDTO);
		
		String trend = "up";//down
		
		/**
		 * 1.K�ߺϲ����Ҷ��׷���
		 */
		
		FindPeakAndBottomInterface findPeakAndBottomIntf
		 	= new FindPeakAndBottomImpl2();
		
		for(int i = 1;i<priceList.size();i++){
			
			StockKLinePriceDTO today = priceList.get(i);
//			System.out.println(today.getBeginTime()+" "+today.getEndTime());
			
			//1.K�ߺϲ�,������ڰ�����ϵ
			MergeLineDTO lastMLineDTO = ZenTheoryUtil.getLastMergeLineDTO(mergeLineList);
			if(ZenTheoryUtil.isInclusive(lastMLineDTO,
					today.getHigh(),today.getLow())){
				ZenTheoryUtil.merge(lastMLineDTO,today,trend);
			}else{//�޺ϲ�����ŵ�resultList��
				if(ZenTheoryUtil.isUp(lastMLineDTO,today.getHigh(),today.getLow())){
				    trend ="up";
				}else if(ZenTheoryUtil.isDown(lastMLineDTO,today.getHigh(),today.getLow())){
				   	trend ="down";
				}
				MergeLineDTO thisMLineDTO = new MergeLineDTO();
				thisMLineDTO.setStickNumber(1);
				thisMLineDTO.setBeginTime(today.getBeginTime());
				thisMLineDTO.setEndTime(today.getEndTime());
				thisMLineDTO.setHigh(today.getHigh());
				thisMLineDTO.setLow(today.getLow());
				thisMLineDTO.getMemberList().add(today);
				mergeLineList.add(thisMLineDTO);
			}
			
			//2.Ѱ�Ҷ��׷���
			findPeakAndBottomIntf.findPeakAndBottom(mergeLineList);
//			ZenTheoryUtil.findPeakAndBottomByModel2(mergeLineList);
		}
		
		System.out.println(mergeLineList.size());
		
		for(int i =0;i<mergeLineList.size();i++){
			MergeLineDTO dto = mergeLineList.get(i);
			
			System.out.println(i+": "+StockDateUtil.SDF_TIME.format(dto.getBeginTime())+" -- "+
					StockDateUtil.SDF_TIME.format(dto.getEndTime())+"**"+dto.getIsBottom()+"**"+dto.getIsPeak()+"**"+dto.getStickNumber());
		}
		
		//3.�ֱ�
		System.out.println("--------------�ֱ�-------------");
		DivideSectionInterface divideSectionIntf = new DivideSectionImpl1();
		boolean sectionResultArray[] = divideSectionIntf.divideSection(mergeLineList);
		
		List <TouchDTO>touchList = new ArrayList();
		TouchDTO touch = null;
		for(int i = 0;i<mergeLineList.size();i++){
			if(sectionResultArray[i]==true){
				if(touch == null){
					touch = new TouchDTO();
					touch.setStartMLine(mergeLineList.get(i));
				}else{
					touch.setEndMLine(mergeLineList.get(i));
					
					if(touch.getStartMLine().getIsBottom().equals("Y")
							&&touch.getEndMLine().getIsPeak().equals("Y")){
						touch.setDirection("up");
					}else if(touch.getStartMLine().getIsPeak().equals("Y")
							&&touch.getEndMLine().getIsBottom().equals("Y")){
						touch.setDirection("down");
					}
					touchList.add(touch);
					touch = new TouchDTO();
					touch.setStartMLine(mergeLineList.get(i));
				}
			}
		}
		
		
		for(int i = 0;i<touchList.size();i++){
			TouchDTO touchDTO = touchList.get(i);
			System.out.println(StockDateUtil.SDF_TIME.format(touchDTO.getStartMLine().getBeginTime()) + "  "
					+ StockDateUtil.SDF_TIME.format(touchDTO.getEndMLine().getEndTime()) + " "
					+ touchDTO.getDirection());
		}
		
		//4.�߶�
		System.out.println("--------------�ֶ�-------------");
		FindSegmentInterface findSegmentIntf = new FindSegmentImpl1();
		findSegmentIntf.findSegment(touchList);
	}
	
	/**
	 * ��ȡ��һ���ϲ�M Line
	 */
	public static MergeLineDTO getLastMergeLineDTO(List<MergeLineDTO> mergeLineList){
		
		return mergeLineList.get(mergeLineList.size()-1);
	}
	
	/**
	 * ���ڰ�����ϵ����ϲ�
	 * �������ϣ��ߵ�ĸߵ㣬�͵�ĸߵ�
	 * �������£��ߵ�ĵ͵㣬�͵�ĵ͵�
	 * @param mergeSticksDTO
	 * @param nextDTO
	 */
	public static void merge(MergeLineDTO mergeLineDTO,StockKLinePriceDTO todayDTO,String trend){
		
		if(trend.equals("up")){
			mergeLineDTO.setHigh(
					Math.max(mergeLineDTO.getHigh(), todayDTO.getHigh()));
			mergeLineDTO.setLow(
					Math.max(mergeLineDTO.getLow(), todayDTO.getLow()));
			mergeLineDTO.setEndTime(todayDTO.getEndTime());
			
		}else if(trend.equals("down")){
			mergeLineDTO.setHigh(
					Math.min(mergeLineDTO.getHigh(), todayDTO.getHigh()));
			mergeLineDTO.setLow(
					Math.min(mergeLineDTO.getLow(), todayDTO.getLow()));
			mergeLineDTO.setEndTime(todayDTO.getEndTime());
		}
		mergeLineDTO.setEndTime(todayDTO.getEndTime());
		mergeLineDTO.getMemberList().add(todayDTO);
		mergeLineDTO.setStickNumber(mergeLineDTO.getStickNumber()+1);
		
	}	
	
	/**
	 * �Ƿ��а�����ϵ
	 */
	public static boolean isInclusive(MergeLineDTO dto,Double highPrice,Double lowPrice){
		
		if((dto.getHigh().compareTo(highPrice)>=0
				&&dto.getLow().compareTo(lowPrice)<=0)
		   ||(dto.getHigh().compareTo(highPrice)<=0
					&&dto.getLow().compareTo(lowPrice)>=0))
			return true;
		
		return false;
	}
	
	/**
	 * ����
	 * @param dto
	 * @param highPrice
	 * @param lowPrice
	 * @return
	 */
	public static boolean isDown(MergeLineDTO dto,Double highPrice,Double lowPrice){
		
		if(dto.getHigh()>highPrice&&
				dto.getLow()>lowPrice)
		   return true;
		else
		   return false;
	}
	
	/**
	 * ����
	 * @param dto
	 * @param highPrice
	 * @param lowPrice
	 * @return
	 */
	public static boolean isUp(MergeLineDTO dto,Double highPrice,Double lowPrice){
		if(dto.getHigh()<highPrice&&
				dto.getLow()<lowPrice)
		   return true;
		else
		   return false;
	}
	
	/**
	 * ȡ�÷ֱ�����,�����ƽ�(������Χ���任��һ�ʵķ���)
	 * @param stockCode
	 * @param period
	 * @param startDay
	 * @param endDay
	 * @return
	 */
	public static List<TouchDTO> getTouchListByBruceForce(String stockCode,String period,String startDay,String endDay) {
		
		logger.info("*******************getTouchListByBruceForce begin***********************");
		//stockCode ="601600";
		List<StockKLinePriceDTO> priceList = null;
		logger.info("��ȡ"+stockCode+" "+period+"����"+",��ѯ����["+startDay+"~"+endDay+"]");
		if (PeriodUtil.PERIOD_5_MIN.equals(period)) {
			priceList = GetStockDataFromSqliteUtil.getStock5MinDataByTime(
					stockCode, startDay + " 09:30:00", endDay + " 15:00:00");
		} else if (PeriodUtil.PERIOD_30_MIN.equals(period)) {
			priceList = GetStockDataFromSqliteUtil.getStock30MinDataByTime(
					stockCode, startDay + " 09:30:00", endDay + " 15:00:00");
		} else if (PeriodUtil.PERIOD_DAY.equals(period)) {
			priceList = GetStockDataFromSqliteUtil.getDayStockData(stockCode,
					startDay, endDay);
		} else if (PeriodUtil.PERIOD_WEEK.equals(period)) {
			priceList = GetStockDataFromSqliteUtil.getStockWeekData(stockCode,
					startDay,endDay);
		} else if (PeriodUtil.PERIOD_MONTH.equals(period)) {
			priceList =	GetStockDataFromSqliteUtil.getStockMonthData(stockCode,
					startDay,endDay);
		}
		
		//��ʼ��һ�ʵķ���
		String trend = "down";
		System.out.println(priceList.size());
		List<TouchDTO> touchList = null;
		
		//�����ƽ�
		for(int i = 0;i<priceList.size();i++){
			
			try {
				trend = "up";
				
				logger.error("-*-��ʼ���ڣ�" + priceList.get(i).getBeginTime()
						+ " trend:" + trend + "�ֱʿ�ʼ...");
				
				touchList = getTouchList(priceList.subList(i, priceList.size()), trend);
				if(touchList.size()!=0){
					break;
				}
			} catch (Exception e) {
				logger.error("-*-��ʼ���ڣ�" + priceList.get(i).getBeginTime()
						+ " trend:" + trend + "�ֱʲ��ɹ���");
				e.printStackTrace();
			}
			
			try {
				trend = "down";
				
				logger.error("-*-��ʼ���ڣ�" + priceList.get(i).getBeginTime()
						+ " trend:" + trend + "�ֱʿ�ʼ...");
				
				touchList = getTouchList(priceList.subList(i, priceList.size()), trend);
				if(touchList.size()!=0){
					break;
				}
			} catch (Exception e) {
				logger.error("-*-��ʼ���ڣ�" + priceList.get(i).getBeginTime()
						+ " trend:" + trend + "�ֱʲ��ɹ���");
				e.printStackTrace();
			}
			
		}
		
		logger.info("*******************getTouchListByBruceForce end**************************");
		
		return touchList;
	}
	
	/**
	 * ��ȡ�ֱ�����
	 * @param priceList
	 * @param trend
	 * @return
	 * @throws Exception 
	 */
	private static List<TouchDTO> getTouchList(List<StockKLinePriceDTO> priceList,
			String trend) throws Exception {
		/**
		 * K�ߺϲ������׷�����Ϣ
		 */
		List<MergeLineDTO> mergeLineList = new ArrayList();
		
		//��1990-01-01��ʼ
		StockKLinePriceDTO priceDTO = priceList.get(0);
		MergeLineDTO mergeLineDTO = new MergeLineDTO();
		mergeLineDTO.setStickNumber(1);
		mergeLineDTO.setBeginTime(priceDTO.getBeginTime());
		mergeLineDTO.setEndTime(priceDTO.getEndTime());
		mergeLineDTO.setHigh(priceDTO.getHigh());
		mergeLineDTO.setLow(priceDTO.getLow());
		mergeLineDTO.getMemberList().add(priceDTO);
		mergeLineList.add(mergeLineDTO);
		
		/**
		 * ��ʼģ�⣬���׹���
		 */
		FindPeakAndBottomInterface findPeakAndBottomIntf = new FindPeakAndBottomImpl2();
		
		for(int i = 1;i<priceList.size();i++){
			
			StockKLinePriceDTO today = priceList.get(i);
//			System.out.println(today.getBeginTime()+" "+today.getEndTime());
			
			//1.K�ߺϲ�,������ڰ�����ϵ
			MergeLineDTO lastMLineDTO = ZenTheoryUtil.getLastMergeLineDTO(mergeLineList);
			if(ZenTheoryUtil.isInclusive(lastMLineDTO,
					today.getHigh(),today.getLow())){
				ZenTheoryUtil.merge(lastMLineDTO,today,trend);
			}else{//�޺ϲ�����ŵ�resultList��
				if(ZenTheoryUtil.isUp(lastMLineDTO,today.getHigh(),today.getLow())){
				    trend ="up";
				}else if(ZenTheoryUtil.isDown(lastMLineDTO,today.getHigh(),today.getLow())){
				   	trend ="down";
				}
				MergeLineDTO thisMLineDTO = new MergeLineDTO();
				thisMLineDTO.setStickNumber(1);
				thisMLineDTO.setBeginTime(today.getBeginTime());
				thisMLineDTO.setEndTime(today.getEndTime());
				thisMLineDTO.setHigh(today.getHigh());
				thisMLineDTO.setLow(today.getLow());
				thisMLineDTO.getMemberList().add(today);
				mergeLineList.add(thisMLineDTO);
			}
			
			//2.�����׷���
			findPeakAndBottomIntf.findPeakAndBottom(mergeLineList);
//			ZenTheoryUtil.findPeakAndBottomByModel2(mergeLineList);
		}
		
		System.out.println(mergeLineList.size());
		
		for(int i =0;i<mergeLineList.size();i++){
			MergeLineDTO dto = mergeLineList.get(i);
			
			logger.info(i+": "+StockDateUtil.SDF_TIME.format(dto.getBeginTime())+" -- "+
					StockDateUtil.SDF_TIME.format(dto.getEndTime())+"**"+dto.getIsBottom()+"**"+dto.getIsPeak()+"**"+dto.getStickNumber());
		}
		
		//3.�ֱ�
		DivideSectionInterface divideSectionIntf = new DivideSectionImpl1();
		boolean sectionResultArray[] = divideSectionIntf.divideSection(mergeLineList);
		
		List <TouchDTO>touchList = new ArrayList();
		TouchDTO touch = null;
		for(int i = 0;i<mergeLineList.size();i++){
			if(sectionResultArray[i]==true){
				if(touch == null){
					touch = new TouchDTO();
					touch.setStartMLine(mergeLineList.get(i));
				}else{
					touch.setEndMLine(mergeLineList.get(i));
					
					if(touch.getStartMLine().getIsBottom().equals("Y")
							&&touch.getEndMLine().getIsPeak().equals("Y")){
						touch.setDirection("up");
					}else if(touch.getStartMLine().getIsPeak().equals("Y")
							&&touch.getEndMLine().getIsBottom().equals("Y")){
						touch.setDirection("down");
					}
					touchList.add(touch);
					
					touch = new TouchDTO();
					touch.setStartMLine(mergeLineList.get(i));
				}
			}
		}
		
		logger.info("--------------�ֱʽ��-------------");
		for(int i = 0;i<touchList.size();i++){
			TouchDTO touchDTO = touchList.get(i);
			logger.info(StockDateUtil.SDF_TIME.format(touchDTO.getStartMLine().getBeginTime()) + "  "
					+ StockDateUtil.SDF_TIME.format(touchDTO.getEndMLine().getEndTime()) + " "
					+ touchDTO.getDirection());
		}
		
		return touchList;
	}
	
}
