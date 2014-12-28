package pers.sam.czsc.test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import pers.sam.dto.MergeLineDTO;
import pers.sam.dto.SegmentDTO;
import pers.sam.dto.StockKLinePriceDTO;
import pers.sam.util.SqliteDataUtil;

public class ZenTheoryTest {
	
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public static void main(String args[]){
		
		String stockCode ="000100";
		
		List<StockKLinePriceDTO> priceList = SqliteDataUtil.getDayStockData(stockCode);
		
		/**
		 * �ʲ���ʼ��
		 */
		String lastAction = "sell";
		
		Double totalMoney = new Double(10000.00);
		Double assetValue = new Double(10000.00);
		
		Double stockValue = new Double(0);
		Double stockNum = new Double(0);
		Double cashValue = new Double(0);
		
		String trend = "down";//up
		
		/**
		 * K�ߺϲ������׷�����Ϣ
		 */
		List<MergeLineDTO> mergeLineList = new ArrayList();
		
		//��2004-2-6��ʼ
		StockKLinePriceDTO priceDTO = priceList.get(5);
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
		for(int i = 6;i<priceList.size();i++){
			
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
			findPeakAndBottomByModel1(mergeLineList);
			
			//3.�� ����
//			divideSection(mergeLineList);
			
			//4.�߶λ���
//			List segmentList = getSegment(mergeLineList);
		}
		
		System.out.println(mergeLineList.size());
		divideSection(mergeLineList);
		
//		for(int i =0;i<mergeLineList.size();i++){
//			MergeLineDTO dto = mergeLineList.get(i);
//			
//			System.out.println(dto.getBeginTime()+"**"+dto.getIsBottom()+"**"+dto.getIsPeak());
//			
//			if(dto.getIsPeak().equals("Y")){
//				System.out.println(sdf.format(dto.getBeginTime())+"\t"+
//						sdf.format(dto.getEndTime())+"\t"+
//						"�ϲ�["+dto.getStickNumber()+"]��K��"+"\t"+
//						"��["+dto.getLow()+"]["+dto.getHigh()+"]");
//			}
//			if(dto.getIsBottom().equals("Y")){
//				System.out.println(sdf.format(dto.getBeginTime())+"\t"+
//						sdf.format(dto.getEndTime())+"\t"+
//						"�ϲ�["+dto.getStickNumber()+"]��K��"+"\t"+
//						"�� ["+dto.getLow()+"]["+dto.getHigh()+"]");
//			}
//		}
	}
	
	/**
	 * �ʻ���
	 * Ѱ�������Ķ����ͣ��׷���
	 * 
	 */
	public static void divideSection(List<MergeLineDTO> mergeSticksList){
		
		String trend ="";
		
		boolean [] sectionArray = new boolean[mergeSticksList.size()];
		
		for(int i =0;i<sectionArray.length;i++){
			sectionArray[i] = false;
		}
		
		boolean flag = breathFirstSearch(mergeSticksList,sectionArray,0);
		
		System.out.println(flag);
		
		for(int i = 0;i<sectionArray.length;i++  ){
			if(sectionArray[i]==true){
				MergeLineDTO dto = mergeSticksList.get(i);
				if(dto.getIsPeak().equals("Y")){
					System.out.println(sdf.format(dto.getBeginTime())+"\t"+
							sdf.format(dto.getEndTime())+"\t"+
							"�ϲ�["+dto.getStickNumber()+"]��K��"+"\t"+
							"��["+dto.getLow()+"]["+dto.getHigh()+"]");
				}
				if(dto.getIsBottom().equals("Y")){
					System.out.println(sdf.format(dto.getBeginTime())+"\t"+
							sdf.format(dto.getEndTime())+"\t"+
							"�ϲ�["+dto.getStickNumber()+"]��K��"+"\t"+
							"�� ["+dto.getLow()+"]["+dto.getHigh()+"]");
				}
			}
		}
		
	}
	
	/**
	 * �����������
	 * @param sectionArray
	 * @param index
	 */
	private static boolean breathFirstSearch(List<MergeLineDTO> mergeSticksList,
			boolean [] sectionArray,int index){
		
		if(index==sectionArray.length-1){
			//�˳������������û��Ψһ�⡢�����Ѿ��ҵ��ֽ�
			
			if(!isRightPartition(mergeSticksList,sectionArray,index-1)){
				return false;
			}
			
			sectionArray[index]=true;
			if(isRightPartition(mergeSticksList,sectionArray,index)){
				return true;
			}
			
			sectionArray[index]=false;
			if(isRightPartition(mergeSticksList,sectionArray,index)){
				return true;
			}
			
			return false;
		}else if(isRightPartition(mergeSticksList,sectionArray,index-1)
				||index==0||index==1){
			//��֦
			sectionArray[index]=true;
			breathFirstSearch(mergeSticksList,sectionArray,index+1);
			
			sectionArray[index]=false;
			breathFirstSearch(mergeSticksList,sectionArray,index+1);
			
		}
		
		return false;
	}
	
	/**
	 * �ж��Ƿ�startIndexΪֹ������ȷ�ķֽ�
	 * @param mergeSticksList
	 * @param sectionArray
	 * @param startIndex
	 * @return
	 */
	private static boolean isRightPartition(List<MergeLineDTO> mergeSticksList,
			boolean [] sectionArray,int endIndex){
		
		MergeLineDTO thisValidDto = null;
		
		int lastIndex = 0;
		MergeLineDTO lastValidDto = null;
		
		for(int i = 0;i<=endIndex;i++){
			if(sectionArray[i]==true){
				thisValidDto = (MergeLineDTO)mergeSticksList.get(i);
			}else{
				continue;
			}
			
			if(lastValidDto!=null&&
					!validatePeakAndBottom(mergeSticksList,lastIndex,i)){
				return false;
			}
			lastValidDto = thisValidDto;
			lastIndex = i;
		}
		
		return true;
	}
	
	/**
	 * У���Ƿ�����һ��
	 */
	private static boolean validatePeakAndBottom(
			List<MergeLineDTO> mergeSticksList,int startIndex,int endIndex){
		
		//1.���͵�֮��û������һK�ߣ��˴�������
		if(endIndex - startIndex<4){
			return false;
		}
		
		//2.�����㶥������ŵס���ױ�����Ŷ�
		MergeLineDTO startDTO = mergeSticksList.get(startIndex);
		MergeLineDTO endDTO = mergeSticksList.get(endIndex);
		if((startDTO.getIsPeak().equals("Y")&&endDTO.getIsPeak().equals("Y"))
				||(startDTO.getIsBottom().equals("Y")&&endDTO.getIsBottom().equals("Y"))){
			return false;
		}
		
		//3.���׷ֱ��Ǳ��е���ߺ����
		MergeLineDTO peakDTO = null;
		MergeLineDTO bottomDTO = null;
		if(startDTO.getIsPeak().equals("Y")){
			peakDTO = startDTO;
			bottomDTO = endDTO;
		}else{
			peakDTO = endDTO;
			bottomDTO = startDTO;
		}
		
		for(int i = startIndex;i<= endIndex;i++){
			MergeLineDTO dto = mergeSticksList.get(i);
			
			//���ڸ��ߵĵ�λ
			if(dto.getHigh()>peakDTO.getHigh()
					&&dto.getBeginTime()!=peakDTO.getBeginTime()){
				return false;
			}
			
			//���ڸ��͵ĵ�λ
			if(dto.getLow()<bottomDTO.getLow()
					&&dto.getBeginTime()!=bottomDTO.getBeginTime()){
				return false;
			}
		}
		
		//4.������Ҫ�ж϶��׵������Ƿ����ص�
		
		return true;
	}
	
	/**
	 * ��ȡ��һ���ϲ�M Line
	 */
	public static MergeLineDTO getLastMergeLineDTO(List<MergeLineDTO> mergeLineList){
		
		return mergeLineList.get(mergeLineList.size()-1);
	}
	
	/**
	 * ��ȡ��һ���߶�
	 */
	public static SegmentDTO getLastSegment(List<SegmentDTO> segmentList){
		return segmentList.get(segmentList.size()-1);
	}
	
	/**
	 * �߶λ���
	 * @param mergeSticksList
	 * @return
	 */
	public static List getSegment(List<MergeLineDTO> mergeLineList){
		
		if(mergeLineList.size()<3){
			return null;
		}
		
		List segmentList = new ArrayList();
//		MergeLineDTO firstMergeDTO = (MergeLineDTO)mergeLineList.get(0);
//		
//		SegmentDTO firstSegmentDTO = new SegmentDTO();
//		firstSegmentDTO.setBeginTime(firstMergeDTO.getBeginTime());
//		firstSegmentDTO.setEndTime(firstMergeDTO.getEndTime());
//		firstSegmentDTO.setHigh(firstMergeDTO.getHigh());
//		firstSegmentDTO.setLow(firstMergeDTO.getLow());
//		firstSegmentDTO.setTrend("down");
//		segmentList.add(firstSegmentDTO);
//		
//		for(int i = 0;i<mergeLineList.size();i++){
//			MergeLineDTO dto = mergeLineList.get(i);
//			if(){
//				
//			}
//		}
//		
		return segmentList;
	}
		
	
	/**
	 * Ѱ�Ҷ����ͣ��׷���
	 * 
	 */
	public static void findPeakAndBottomByModel1(List<MergeLineDTO> mergeSticksList){
		
		if(mergeSticksList.size()<3){
			return;
		}
		
		//���͵ײ��ܹ���K��,dist��Ϊ����
		int dist = 1;
		for(int i = 1;i<mergeSticksList.size()-1;i++){
			
			if(dist == 1){
				MergeLineDTO firstDTO = (MergeLineDTO) mergeSticksList.get(i-1);
				MergeLineDTO middleDTO = (MergeLineDTO) mergeSticksList.get(i);
				MergeLineDTO lastDTO = (MergeLineDTO) mergeSticksList.get(i+1);
				
				if(middleDTO.getHigh()>Math.max(
					  firstDTO.getHigh(), lastDTO.getHigh())
				   &&middleDTO.getLow()>Math.max(
						firstDTO.getLow(), lastDTO.getLow())){
					middleDTO.setIsPeak("Y");	
				   dist = 3;
				}
				if(middleDTO.getHigh()<Math.min(
						  firstDTO.getHigh(), lastDTO.getHigh())
				   &&middleDTO.getLow()<Math.min(
								firstDTO.getLow(), lastDTO.getLow())){
					middleDTO.setIsBottom("Y");
					dist = 3;
				}
			}else{
				dist--;
				continue;
			}
		}
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
	
}
