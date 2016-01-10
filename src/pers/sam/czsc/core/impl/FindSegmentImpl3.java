package pers.sam.czsc.core.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import pers.sam.czsc.core.FindSegmentInterface;
import pers.sam.czsc.dto.FeatureElementDTO;
import pers.sam.czsc.dto.StrokeDTO;
import pers.sam.util.StockDateUtil;

/**
 * �߶λ���--ʵ����
 * ������һ�ڶ�Ԫ�ص����⡢������һ�ڶ�������µķַ�
 * 
 * @author Administrator
 *
 */
public class FindSegmentImpl3 implements FindSegmentInterface {
	
	public List<String> findSegment(List<StrokeDTO> strokeList) {
		
		List<String> resultList = new ArrayList<String>();
		
		LinkedList<StrokeDTO> processList = new LinkedList<StrokeDTO>();
		
		for(int i=0;i<strokeList.size();i++){
			processList.add((strokeList.get(i)).clone());
		}
		
		//ȡ��һ��ʼ�߶εķ���
		String segmentDirection = "";
		String nextSegmentDirection="";
		StrokeDTO startStrokeDTO = strokeList.get(0);
		if(startStrokeDTO.getDirection().equals("up")){
			segmentDirection = "up";
		}else{
			segmentDirection = "down";
		}
		
		//��һ�߶εĽ���λ��,��ʼΪ0
		int lastSegmentEndIndex = 0;
		
		//��һ������������
		boolean flag = true;
		while(flag){//һ���߶Σ�����һ�Ρ�
			
			flag = false;
			
			if(lastSegmentEndIndex+3>processList.size()){
				flag = false;
				break;
			}
			
			//һ�߶����������ʣ����Դ��������Ԫ�ؿ�ʼ���
			for(int i=lastSegmentEndIndex+3;i<processList.size();i=i+2){
				//����i�Ƿֽ��
				
				//��һԪ�أ���һԪ�ؾ����Ըü���ת�۵�ǰ�߶ε����һ������Ԫ�أ��θߣ�
				List<FeatureElementDTO> beforeElementList = mergeFeatureElement(
						processList, segmentDirection.equals("up") ? "down"
								: "up", lastSegmentEndIndex, i-1);
				
				FeatureElementDTO firstElement = getFirstElement(
						beforeElementList, segmentDirection);
				
				// �ҵ��ڶ�����Ԫ�أ��������кϲ��󣬼���׼�������У�
				List<FeatureElementDTO> afterElementList = mergeFeatureElement(
						processList, segmentDirection.equals("up") ? "down"
								: "up", i, processList.size()-1);
				if(afterElementList.size()<2){
					flag = false;
					break;
				}
				
				FeatureElementDTO secondElement = afterElementList.get(0);
				FeatureElementDTO thirdElement = afterElementList.get(1);
				
				// �ж�i������Ƿ��ǵڶ�Ԫ�صļ�ֵ
				if (hasHigherOrLowerPoint(secondElement, segmentDirection,
						processList,i)) {
					flag = false;
					continue;
				}
				
				//�Ƿ���ڷ���
				if(segmentDirection.equals("up")){
					//������
					if(!(firstElement.getHigh()<secondElement.getHigh()
							&&thirdElement.getHigh()<secondElement.getHigh()
							&&thirdElement.getLow()<secondElement.getLow())){
						flag = false;
						continue;//������ֱ������
					}
				}else if(segmentDirection.equals("down")){
					//�׷���
					if(!(firstElement.getLow()>secondElement.getLow()
							&&thirdElement.getLow()>secondElement.getLow()
							&&thirdElement.getHigh()>secondElement.getHigh())){
						flag = false;
						continue;//������ֱ������
					}
				}
				
				//���ֵ�һ�͵ڶ������
				if(!existsGapBetweenFirstAndSecondElement(segmentDirection,firstElement,processList.get(i))){
					//�ǵ�һ���������һԪ�غ͵ڶ�Ԫ����ȱ��
					//���ڲ��һ��ֳɹ�
					flag= true;
					
					//�ϲ��ڶ�����Ԫ�����а�����ϵ�ķֱʣ��Ӻ���ǰ����
					mergeProcessList(processList, thirdElement,
							segmentDirection.equals("up") ? "down" : "up");
					mergeProcessList(processList, secondElement,
							segmentDirection.equals("up") ? "down" : "up");
					
					lastSegmentEndIndex = i;
					
					nextSegmentDirection = segmentDirection.equals("up")?"down":"up";
					
					resultList.add(StockDateUtil.SDF_TIME.format(secondElement.getBeginTime()));
					System.out.println("�߶ζ˵�: "+
							StockDateUtil.SDF_TIME.format(secondElement.getBeginTime())+
							" ��һ�����");
					
//					System.out.println("�߶ζ˵�: "+
//							StockDateUtil.SDF_TIME.format(touchList.get(i).getStartMLine().getBeginTime())+"~"+
//							StockDateUtil.SDF_TIME.format(touchList.get(i).getEndMLine().getEndTime())+" point ");
					
				}else{
					//�ǵڶ����������һԪ�غ͵ڶ�Ԫ����ȱ��
					//��Ҫ��ʶ�ڶ����������Ƿ���ַ���
					String secondSegmentDirection = segmentDirection.equals("up")?"down":"up";
					
					// ��ȡ�ڶ���������
					List<FeatureElementDTO> secondElementList = mergeFeatureElement(
							processList,
							secondSegmentDirection.equals("up") ? "down" : "up",
							i, processList.size() - 1);
					
					if(secondElementList.size()<3){//���������������޴ӿ���
						flag = false;
						continue;
					}
					
					for(int j = 1;j<secondElementList.size()-1;j++){
						FeatureElementDTO aDTO = secondElementList.get(j-1);
						FeatureElementDTO bDTO = secondElementList.get(j);
						FeatureElementDTO cDTO = secondElementList.get(j+1);
						
						// �жϵڶ����͵ĵ�һԪ���Ƿ���ڳ���i�ļ�ֵ�ĵ�
						if (hasHigherOrLowerPoint(aDTO, segmentDirection,
								processList,i)) {
							flag = false;
							continue;
						}
						// �жϵڶ����͵ĵڶ�Ԫ���Ƿ���ڳ���i�ļ�ֵ�ĵ�
						if (hasHigherOrLowerPoint(bDTO, segmentDirection,
								processList,i)) {
							flag = false;
							continue;
						}
						// �жϵڶ����͵ĵ���Ԫ���Ƿ���ڳ���i�ļ�ֵ�ĵ�
						if (hasHigherOrLowerPoint(cDTO, segmentDirection,
								processList,i)) {
							flag = false;
							continue;
						}
						
						if(secondSegmentDirection.equals("down")){
							//�ڶ����������ǵ׷���
							if(bDTO.getLow()<aDTO.getLow()&&bDTO.getLow()<cDTO.getLow()
								&&bDTO.getHigh()<aDTO.getHigh()&&bDTO.getHigh()<cDTO.getHigh()){
								flag = true;
							}
						}else if(secondSegmentDirection.equals("up")){
							//�ڶ����������Ƕ�����
							if(bDTO.getLow()>aDTO.getLow()&&bDTO.getLow()>cDTO.getLow()
								&&bDTO.getHigh()>aDTO.getHigh()&&bDTO.getHigh()>cDTO.getHigh()){
								flag = true;
							}
						}
						
						if(flag == true){
							
							//�ϲ��ڶ�����Ԫ�����а�����ϵ�ķֱʣ��Ӻ���ǰ����
							mergeProcessList(
									processList,
									cDTO,
									secondSegmentDirection.equals("up") ? "down"
											: "up");
							mergeProcessList(
									processList,
									bDTO,
									secondSegmentDirection.equals("up") ? "down"
											: "up");
							
							resultList.add(StockDateUtil.SDF_TIME.format(secondElement.getBeginTime()));
							System.out.println("�߶ζ˵�: "+
									StockDateUtil.SDF_TIME.format(secondElement.getBeginTime())+" �ڶ������(1)");
							
							resultList.add(StockDateUtil.SDF_TIME.format(bDTO.getBeginTime()));
							System.out.println("�߶ζ˵�: "+
									StockDateUtil.SDF_TIME.format(bDTO.getBeginTime())+" �ڶ������(2)");
							
							//����õ�ʵ���ϵĽ�����
							lastSegmentEndIndex = findIndexByEndTime(
									processList, bDTO.getBeginTime());
							nextSegmentDirection = segmentDirection;
							break;
						}
					}
				}
				
				if(flag == true){//�ҵ��߶�,��һ��������߶η��򣬵ڶ����룬���߶���ԭ�߶�ͬ��
					segmentDirection = nextSegmentDirection;
					break;
				}
			}
			
			if(flag == false){//
				break;
			}
		}
		
		//������
//		for(int i = 0;i<resultIndexList.size();i++){
//			int resutIndex = (Integer)resultIndexList.get(i);
//			
//			TouchDTO touchDTO = touchList.get(resutIndex);
//			System.out.println("�߶��� "+
//					StockDateUtil.SDF_TIME.format(touchDTO.getStartMLine().getBeginTime())+"~"+
//					StockDateUtil.SDF_TIME.format(touchDTO.getEndMLine().getEndTime())+" point ");
//		}
		return resultList;
		
	}
	
	/**
	 * �����߶εĽ���ʱ�䣬�������������
	 * @param processList
	 * @param endTime
	 * @return
	 */
	public Integer findIndexByEndTime(LinkedList<StrokeDTO> processList,Date endTime){
		
		for(int i = processList.size()-1;i<processList.size();i--){
			StrokeDTO dto = processList.get(i);
			if(dto.getEndMLine().getEndTime().compareTo(endTime)>=0
					&&dto.getStartMLine().getBeginTime().compareTo(endTime)<=0){
				return new Integer(i);
			}
		}
		return null;
	}
	
	/**
	 * �ж�ĳ�ϲ�������������У��Ƿ��б�iԪ�ظ��߻���͵ĵ�
	 * 
	 * @param secondElement
	 * @param segmentDirection
	 * @return
	 */
	public boolean hasHigherOrLowerPoint(FeatureElementDTO secondElement,
			String segmentDirection, LinkedList<StrokeDTO> processList,Integer compareIndex) {
		
		StrokeDTO compareStroke = processList.get(compareIndex);

		Integer firstIndex = secondElement.getStrokeIndexList().get(0);
		Integer lastIndex = secondElement.getStrokeIndexList().get(
				secondElement.getStrokeIndexList().size() - 1);
		
		for(int i = firstIndex;i<=lastIndex;i++){
			
			if(i==compareIndex){
				continue;
			}
			
			StrokeDTO nStroke = processList.get(i);
			
			if("up".equals(segmentDirection)
					&&nStroke.getHigh()>compareStroke.getHigh()){
				//�߶η������ϣ����Һϲ�Ԫ�����ָ��ߵĵ�
				return true;
			}else if("down".equals(segmentDirection)
					&&nStroke.getLow()<compareStroke.getLow()){
				//�߶η������ϣ����Һϲ�Ԫ�����и��͵ĵ�
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * �ϲ��ڶ�����Ԫ�����а�����ϵ�ķֱʣ��Ӻ���ǰ����
	 */
	public static void mergeProcessList(LinkedList<StrokeDTO> processList,FeatureElementDTO featureElement,String featureDirection){
		
		List<Integer> strokeIndexList = featureElement.getStrokeIndexList();
		
		int startIndex = strokeIndexList.get(0);
		int endIndex = strokeIndexList.get(strokeIndexList.size()-1);
		
		StrokeDTO headDTO = processList.get(startIndex);
		StrokeDTO tailDTO = processList.get(endIndex);
		
		StrokeDTO newDTO = new StrokeDTO();
		newDTO.setDirection(featureDirection);
		newDTO.setStartMLine(headDTO.getStartMLine());
		newDTO.setEndMLine(tailDTO.getEndMLine());
		
		//�Ӻ���ǰɾ
		for(int i = endIndex;i>= startIndex;i--){
			processList.remove(i);
		}
		
		processList.add(startIndex, newDTO);
		
	}
	
	/*
	 * �жϵ�һ�ڶ�Ԫ�ؼ��Ƿ���ȱ�ڣ������Ǻϲ���ϵ��
	 */
	private boolean existsGapBetweenFirstAndSecondElement(
			String segmentDirection, FeatureElementDTO firstElement,
			StrokeDTO secondDTO) {
		return (
			(segmentDirection.equals("up")&&firstElement.getHigh()<Math.min(secondDTO.getEndMLine().getLow(), secondDTO.getStartMLine().getLow()))||
			(segmentDirection.equals("down")&&firstElement.getLow()>Math.max(secondDTO.getEndMLine().getHigh(), secondDTO.getStartMLine().getHigh()))
			);
	}
	
	/**
	 * ��ȡ��һԪ��
	 * ѡ��θߵ�/�ε͵�
	 * 
	 * 
	 * @param beforeElementList
	 * @return
	 */
	public FeatureElementDTO getFirstElement(List<FeatureElementDTO> beforeElementList,String segmentDirection){
		
		FeatureElementDTO firstElement = beforeElementList
				.get(beforeElementList.size() - 1);
		
		if(segmentDirection.equals("up")){
			//���ϵ��߶Σ��������������±ʣ�ȡ��ߵ��Ԫ��
			for(int i = beforeElementList.size()-1;i>=0;i--){
				FeatureElementDTO dto = beforeElementList.get(i);
				if(dto.getHigh()>firstElement.getHigh()){
					firstElement = dto;
				}
			}
		}else if(segmentDirection.equals("down")){
			//���µ��߶Σ��������������ϱʣ�ȡ��͵��Ԫ��
			for(int i = beforeElementList.size()-1;i>=0;i--){
				FeatureElementDTO dto = beforeElementList.get(i);
				if(dto.getLow()<firstElement.getLow()){
					firstElement = dto;
				}
			}
		}
		return firstElement;
	}
	
	/**
	 * �����������еĺϲ���ϵ
	 * 
	 * @return
	 */
	public static List<FeatureElementDTO> mergeFeatureElement(List<StrokeDTO> strokeList,
			String featureDirection, int startIndex, int endIndex) {
		
		//�ɷֱ���ץ����������
		List<FeatureElementDTO> featureElementList = new ArrayList<FeatureElementDTO>();
		for(int i = startIndex;i<=endIndex;i++){
			StrokeDTO strokeDTO = strokeList.get(i);
			if(strokeDTO.getDirection().equals(featureDirection)){
				FeatureElementDTO elementDTO = new FeatureElementDTO();
				elementDTO.setBeginTime(strokeDTO.getStartMLine().getBeginTime());
				elementDTO.setEndTime(strokeDTO.getEndMLine().getEndTime());
				if(strokeDTO.getDirection().equals("up")){
					elementDTO.setHigh(strokeDTO.getEndMLine().getHigh());
					elementDTO.setLow(strokeDTO.getStartMLine().getLow());
				}else if(strokeDTO.getDirection().equals("down")){
					elementDTO.setHigh(strokeDTO.getStartMLine().getHigh());
					elementDTO.setLow(strokeDTO.getEndMLine().getLow());	
				}
				elementDTO.getStrokeIndexList().add(i);
				featureElementList.add(elementDTO);
			}
		}
		
		boolean flag = true;
		while(flag){
			FeatureElementDTO  mergeDTO = new FeatureElementDTO();
			List<FeatureElementDTO> headList = new ArrayList<FeatureElementDTO>();
			List<FeatureElementDTO> tailLsit = new ArrayList<FeatureElementDTO>();
			
			flag = false;
			for(int i = 1;i<featureElementList.size();i++){
				FeatureElementDTO lastDTO = featureElementList.get(i-1);
				FeatureElementDTO thisDTO = featureElementList.get(i);
				
				//������ϵ
				if((lastDTO.getHigh()>=thisDTO.getHigh()&&lastDTO.getLow()<=thisDTO.getLow())
					||(thisDTO.getHigh()>=lastDTO.getHigh()&&thisDTO.getLow()<=lastDTO.getLow())
					){
					
					//�ϲ�
					mergeDTO.setBeginTime(lastDTO.getBeginTime());
					mergeDTO.setEndTime(thisDTO.getEndTime());
					mergeDTO.getStrokeIndexList().addAll(lastDTO.getStrokeIndexList());//���ǰ���зֱʱ��
					mergeDTO.getStrokeIndexList().addAll(thisDTO.getStrokeIndexList());//��Ӻ����зֱʱ��
					
					if(featureDirection.equals("up")){
						mergeDTO.setHigh(Math.min(lastDTO.getHigh(), thisDTO.getHigh()));
						mergeDTO.setLow(Math.min(lastDTO.getLow(), thisDTO.getLow()));
					}else if(featureDirection.equals("down")){
						mergeDTO.setHigh(Math.max(lastDTO.getHigh(), thisDTO.getHigh()));
						mergeDTO.setLow(Math.max(lastDTO.getLow(), thisDTO.getLow()));
					}
					
					flag=true;
					if(i!=0){
						headList = featureElementList.subList(0, i-1);
					}
					
					if(i!=featureElementList.size()-1){
						tailLsit = featureElementList.subList(i+1, featureElementList.size());
					}
					break;
				}
			}
			
			if(flag){
				featureElementList = new ArrayList<FeatureElementDTO>();
				featureElementList.addAll(headList);
				featureElementList.add(mergeDTO);
				featureElementList.addAll(tailLsit);
			}else{
				flag = false;
			}
		}
		
		return featureElementList;
	}
}
