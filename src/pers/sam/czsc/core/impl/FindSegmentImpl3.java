package pers.sam.czsc.core.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import pers.sam.czsc.core.FindSegmentInterface;
import pers.sam.dto.FeatureElementDTO;
import pers.sam.dto.TouchDTO;
import pers.sam.util.StockDateUtil;

/**
 * �߶λ���--ʵ����
 * ������һ�ڶ�Ԫ�ص����⡢������һ�ڶ�������µķַ�
 * 
 * @author Administrator
 *
 */
public class FindSegmentImpl3 implements FindSegmentInterface {
	
	public void findSegment(List<TouchDTO> touchList) {
		
		List<Integer> resultIndexList = new ArrayList<Integer>();
		
		List<TouchDTO> processList = new LinkedList<TouchDTO>();
		
		for(int i=0;i<touchList.size();i++){
			processList.add((touchList.get(i)).clone());
		}
		
		//ȡ��һ��ʼ�߶εķ���
		String segmentDirection = "";
		TouchDTO startTouchDTO = touchList.get(0);
		if(startTouchDTO.getDirection().equals("up")){
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
			
			if(lastSegmentEndIndex+3>touchList.size()){
				flag = false;
				break;
			}
			
			//һ�߶����������ʣ����Դ��������Ԫ�ؿ�ʼ���
			for(int i=lastSegmentEndIndex+3;i<touchList.size();i=i+2){
				//����i�Ƿֽ��
				
				//��һԪ�أ���һԪ�ؾ����Ըü���ת�۵�ǰ�߶ε����һ������Ԫ�أ��θߣ�
				List<FeatureElementDTO> beforeElementList = mergeFeatureElement(
						touchList, segmentDirection.equals("up") ? "down"
								: "up", lastSegmentEndIndex, i-1);
				
				FeatureElementDTO firstElement = getFirstElement(
						beforeElementList, segmentDirection);
				
				// �ҵ��ڶ�����Ԫ�أ��������кϲ��󣬼���׼�������У�
				List<FeatureElementDTO> afterElementList = mergeFeatureElement(
						touchList, segmentDirection.equals("up") ? "down"
								: "up", i, touchList.size()-1);
				if(afterElementList.size()<2){
					flag = false;
					break;
				}
				
				FeatureElementDTO secondElement = afterElementList.get(0);
				FeatureElementDTO thirdElement = afterElementList.get(1);
				
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
				if(!existsGapBetweenFirstAndSecondElement(segmentDirection,firstElement,touchList.get(i))){
					//�ǵ�һ���������һԪ�غ͵ڶ�Ԫ����ȱ��
					//���ڲ��һ��ֳɹ�
					flag= true;
					resultIndexList.add(i);
					lastSegmentEndIndex = i;
					
					System.out.println("�߶ζ˵�: "+
							StockDateUtil.SDF_TIME.format(touchList.get(i).getStartMLine().getBeginTime())+"~"+
							StockDateUtil.SDF_TIME.format(touchList.get(i).getEndMLine().getEndTime())+" point ");
					
				}else{
					//�ǵڶ����������һԪ�غ͵ڶ�Ԫ����ȱ��
					//��Ҫ��ʶ�ڶ����������Ƿ���ַ���
					String secondSegmentDirection = segmentDirection.equals("up")?"down":"up";
					
					//��ȡ�ڶ���������
					List<FeatureElementDTO> secondElementList = mergeFeatureElement(touchList,segmentDirection,
							i, touchList.size()-1);
					
					if(secondElementList.size()<3){//���������������޴ӿ���
						flag = false;
						continue;
					}
					
					for(int j = 1;j<secondElementList.size()-1;j++){
						FeatureElementDTO aDTO = secondElementList.get(i-1);
						FeatureElementDTO bDTO = secondElementList.get(i);
						FeatureElementDTO cDTO = secondElementList.get(i+1);
						
						if(segmentDirection.equals("up")){
							//�ڶ����������ǵ׷���
							if(bDTO.getLow()<aDTO.getLow()&&bDTO.getLow()<cDTO.getLow()
								&&bDTO.getHigh()<aDTO.getHigh()&&bDTO.getHigh()<cDTO.getHigh()){
								flag = true;
//								break;
							}
						}else if(segmentDirection.equals("down")){
							//�ڶ����������Ƕ�����
							if(bDTO.getLow()>aDTO.getLow()&&bDTO.getLow()>cDTO.getLow()
								&&bDTO.getHigh()>aDTO.getHigh()&&bDTO.getHigh()>cDTO.getHigh()){
								flag = true;
//								break;
							}							
						}
						
						if(flag == true){
							resultIndexList.add(i);
							resultIndexList.add(j);
							lastSegmentEndIndex = j;
							break;
						}
						
					}
				}
				
				if(flag == true){//�����ǵڼ������,���߶η���
					segmentDirection = segmentDirection.equals("up")?"down":"up";
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
		
	}
	
	/*
	 * �жϵ�һ�ڶ�Ԫ�ؼ��Ƿ���ȱ�ڣ������Ǻϲ���ϵ��
	 */
	private boolean existsGapBetweenFirstAndSecondElement(
			String segmentDirection, FeatureElementDTO firstElement,
			TouchDTO secondDTO) {
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
	public static List<FeatureElementDTO> mergeFeatureElement(List<TouchDTO> touchList,
			String featureDirection, int startIndex, int endIndex) {
		
		//�ɷֱ���ץ����������
		List<FeatureElementDTO> featureElementList = new ArrayList<FeatureElementDTO>();
		for(int i = startIndex;i<=endIndex;i++){
			TouchDTO touchDTO = touchList.get(i);
			if(touchDTO.getDirection().equals(featureDirection)){
				FeatureElementDTO elementDTO = new FeatureElementDTO();
				elementDTO.setBeginTime(touchDTO.getStartMLine().getBeginTime());
				elementDTO.setEndTime(touchDTO.getEndMLine().getEndTime());
				if(touchDTO.getDirection().equals("up")){
					elementDTO.setHigh(touchDTO.getEndMLine().getHigh());
					elementDTO.setLow(touchDTO.getStartMLine().getLow());
				}else if(touchDTO.getDirection().equals("down")){
					elementDTO.setHigh(touchDTO.getStartMLine().getHigh());
					elementDTO.setLow(touchDTO.getEndMLine().getLow());	
				}
				elementDTO.setElementIndex(i);
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
					
					if(featureDirection.equals("up")){
						mergeDTO.setHigh(Math.max(lastDTO.getHigh(), thisDTO.getHigh()));
						mergeDTO.setLow(Math.max(lastDTO.getLow(), thisDTO.getLow()));
					}else if(featureDirection.equals("down")){
						mergeDTO.setHigh(Math.min(lastDTO.getHigh(), thisDTO.getHigh()));
						mergeDTO.setLow(Math.min(lastDTO.getLow(), thisDTO.getLow()));
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
