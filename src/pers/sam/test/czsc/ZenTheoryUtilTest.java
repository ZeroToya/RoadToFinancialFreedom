	package pers.sam.test.czsc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import pers.sam.czsc.core.DivideSectionInterface;
import pers.sam.czsc.core.FindPeakAndBottomInterface;
import pers.sam.czsc.core.impl.DivideSectionImpl1;
import pers.sam.czsc.core.impl.FindPeakAndBottomImpl1;
import pers.sam.czsc.core.impl.FindPeakAndBottomImpl2;
import pers.sam.czsc.util.ZenTheoryUtil;
import pers.sam.dto.MergeLineDTO;
import pers.sam.dto.StockKLinePriceDTO;
import pers.sam.util.SqliteDataUtil;

public class ZenTheoryUtilTest extends TestCase {

	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public void atestDivideSection(){
		
		String stockCode ="T00001";
		
		List<StockKLinePriceDTO> priceList = SqliteDataUtil.getTestStockData(stockCode);
		
		System.out.println(priceList.size());
		
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
		mergeLineList.add(mergeLineDTO);
		
		String trend = "up";
		
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
				mergeLineList.add(thisMLineDTO);
			}
			
			//2.�����׷���
			findPeakAndBottomIntf.findPeakAndBottom(mergeLineList);
//			ZenTheoryUtil.findPeakAndBottomByModel2(mergeLineList);
			
			//3.�� ����
//			divideSection(mergeLineList);
			
			//4.�߶λ���
//			List segmentList = getSegment(mergeLineList);
		}
		
		System.out.println(mergeLineList.size());
		
		for(int i =0;i<mergeLineList.size();i++){
			MergeLineDTO dto = mergeLineList.get(i);
			
			System.out.println(sdf.format(dto.getBeginTime())+"-"+
					sdf.format(dto.getEndTime())+"**"+dto.getIsBottom()+"**"+dto.getIsPeak()+"**"+dto.getStickNumber());
		}
		
//		ZenTheoryUtil.divideSection(mergeLineList);
		DivideSectionInterface divideSectionIntf = new DivideSectionImpl1();
		boolean sectionResultArray[] = divideSectionIntf.divideSection(mergeLineList);
		
	}
	
	public void test999999DivideSection() throws ParseException{
		
		String stockCode ="999999";
		
//		List<StockDayPriceDTO> priceList = 
//			SqliteDataUtil.getStockMonthData(stockCode,"2001-03-30","2005-8-31");
		
		List<StockKLinePriceDTO> priceList = 
		SqliteDataUtil.getStockMonthData(stockCode,"1990-12-31","2014-07-30");
		
		System.out.println(priceList.size());
		
		/**
		 * K�ߺϲ������׷�����Ϣ
		 */
		List<MergeLineDTO> mergeLineList = new ArrayList();
		
		
		//��������Ķ��͵ף����ٺ����ĵ�����
		MergeLineDTO firstMLineDTO = new MergeLineDTO();
		firstMLineDTO.setStickNumber(1);
		firstMLineDTO.setBeginTime(sdf.parse("1990-1-1"));
		firstMLineDTO.setEndTime(sdf.parse("1990-1-1"));
		firstMLineDTO.setHigh(0.0);
		firstMLineDTO.setLow(0.0);
		firstMLineDTO.setIsBottom("Y");
		firstMLineDTO.setIsPeak("N");
		mergeLineList.add(firstMLineDTO);
		
		//��1990-01-01��ʼ
		StockKLinePriceDTO priceDTO = priceList.get(0);
		MergeLineDTO mergeLineDTO = new MergeLineDTO();
		mergeLineDTO.setStickNumber(1);
		mergeLineDTO.setBeginTime(priceDTO.getBeginTime());
		mergeLineDTO.setEndTime(priceDTO.getEndTime());
		mergeLineDTO.setHigh(priceDTO.getHigh());
		mergeLineDTO.setLow(priceDTO.getLow());
		mergeLineList.add(mergeLineDTO);
		
		String trend = "up";
		
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
				mergeLineList.add(thisMLineDTO);
			}
			
			//2.�����׷���
			findPeakAndBottomIntf.findPeakAndBottom(mergeLineList);
//			ZenTheoryUtil.findPeakAndBottomByModel2(mergeLineList);
		}
		
		System.out.println(mergeLineList.size());
		
		for(int i =0;i<mergeLineList.size();i++){
			MergeLineDTO dto = mergeLineList.get(i);
			
			System.out.println(i+": "+sdf.format(dto.getBeginTime())+"-"+
					sdf.format(dto.getEndTime())+"**"+dto.getIsBottom()+"**"+dto.getIsPeak()+"**"+dto.getStickNumber());
		}
		
//		ZenTheoryUtil.divideSection(mergeLineList);
		
		DivideSectionInterface divideSectionIntf = new DivideSectionImpl1();
		boolean sectionResultArray[] = divideSectionIntf.divideSection(mergeLineList);
		
	}
	
	/**
	 * Ѱ�Ҷ��׷���
	 */
	public void atestfindPeakAndBottomByModel1(){
		
		String stockCode ="T00002";
		
		List<StockKLinePriceDTO> priceList = SqliteDataUtil.getTestStockData(stockCode);
		
		System.out.println(priceList.size());
		
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
		mergeLineList.add(mergeLineDTO);
		
		String trend = "up";
		
		
		
		for(int i = 1;i<priceList.size();i++){
			StockKLinePriceDTO today = priceList.get(i);
			
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
				mergeLineList.add(thisMLineDTO);
			}
		}
		
		FindPeakAndBottomInterface findPeakAndBottomIntf = new FindPeakAndBottomImpl1();
		findPeakAndBottomIntf.findPeakAndBottom(mergeLineList);
		
//		ZenTheoryUtil.findPeakAndBottomByModel1(mergeLineList);
		System.out.println(mergeLineList.size());
		for(int i = 0;i<mergeLineList.size();i++){
			MergeLineDTO dto = mergeLineList.get(i);
			
			System.out.println(sdf.format(dto.getBeginTime())+"-"+
					sdf.format(dto.getEndTime())+"**"+
					dto.getIsPeak()+"**"+
					dto.getIsBottom());
		}
	}
}
