package pers.sam.czsc.core.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import pers.sam.czsc.core.DivideSectionInterface;
import pers.sam.czsc.dto.MergeLineDTO;
import pers.sam.czsc.util.ZenTheoryUtil;
import pers.sam.dto.StockKLinePriceDTO;
import pers.sam.util.StockDateUtil;

/**
 * �ֱʽӿ�--ʵ��1
 * @author Administrator
 *
 */
public class DivideSectionImpl1 implements DivideSectionInterface {
    
	private static Logger logger=Logger.getLogger(DivideSectionImpl1.class);
	
	/**
	 * �ֱ�
	 * @throws Exception 
	 */
	public boolean[] divideSection(List<MergeLineDTO> mergeSticksList) throws Exception {
		boolean [] pointArray = new boolean[mergeSticksList.size()];
		for(int i =0;i<pointArray.length;i++){
			pointArray[i] = false;
		}
		
		/**
		 * 1.Ԥ������������ڵĶ��Ƿ�ͬһ�ַ���
		 */
		MergeLineDTO lastPoint = null;
		for(int i = 0;i<mergeSticksList.size();i++){
			
			MergeLineDTO thisPoint = mergeSticksList.get(i);
			
			if(thisPoint.getIsBottom().equals("N")
					&&thisPoint.getIsPeak().equals("N")){
				continue;
			}
			
			if(lastPoint == null){
				lastPoint = thisPoint;
				pointArray[i] = true;
				continue;
			}
			
			if(lastPoint.getIsPeak().equals("Y")
					&&thisPoint.getIsPeak().equals("Y")){
				//ͬΪ����ȡ��ߵ�
				if(thisPoint.getHigh()>=lastPoint.getHigh()){
					lastPoint = thisPoint;
					pointArray[i] = true;
				}
				
			}else if(lastPoint.getIsBottom().equals("Y")
					&&thisPoint.getIsBottom().equals("Y")){
				//ͬΪ�ף�ȡ��͵�
				if(thisPoint.getLow()<=lastPoint.getLow()){
					lastPoint = thisPoint;
					pointArray[i] = true;
				}
				
			}else{
				lastPoint = thisPoint;
				pointArray[i] = true;
			}
		}
		
		/**
		 * 2.��̬�滮���ҳ�����[k,k+i]�Ƿ��ܳ�һ��
		 */
		List <Integer>pointIndexList = new ArrayList<Integer>();
		for(int i=0;i<pointArray.length;i++){
			if(pointArray[i]==true){
				pointIndexList.add(new Integer(i));
			}
		}
		//�ó�N�������±������
		Integer[] pointIndexArray = (Integer[]) pointIndexList
				.toArray(new Integer[pointIndexList.size()]);
		
		boolean [][] pointIndexMatrix = 
			new boolean[pointIndexArray.length][pointIndexArray.length];
		
		for(int i = 0;i<pointIndexArray.length;i++){
			for(int j = 0;j<pointIndexArray.length;j++){
				pointIndexMatrix[i][j] = false;
			}
		}
		
		findValidPointByDP(mergeSticksList,pointIndexArray,pointIndexMatrix);
		
		/**
		 * 3.������һ���õ��Ľ�����ó�������ķ���
		 */
		boolean [] resultArray = new boolean [pointIndexArray.length];
		boolean hasResult = false;
		for(int i = pointIndexArray.length-1;i>0;i--){
			
			for(int j = 0;j<resultArray.length;j++){
				resultArray[j]=false;
			}
			
			hasResult = 
				checkFinalTwigPartition(mergeSticksList,
						pointIndexArray,pointIndexMatrix,
						resultArray,i);
			if(hasResult==true){
				break;
			}
		}
		
		/**
		 * 4.����ֱʽ��
		 */
		System.out.println("�ֱʽ�� : "+hasResult);
		if(!hasResult){
			throw new Exception("����Ŀǰ�Ļ��ֹ���û���ҵ��ֱʵĽ��.");
		}
		
		
		for(int i = 0;i<resultArray.length;i++){
			if(resultArray[i]==true){
				MergeLineDTO dto = mergeSticksList.get(pointIndexArray[i]);
				if(dto.getIsPeak().equals("Y")){
					logger.info(pointIndexArray[i]+" "+StockDateUtil.SDF_TIME.format(dto.getBeginTime())+"\t"+
							StockDateUtil.SDF_TIME.format(dto.getEndTime())+"\t"+
							"�ϲ�["+dto.getStickNumber()+"]��K��"+"\t"+
							"��["+dto.getLow()+"]["+dto.getHigh()+"]");
				}
				if(dto.getIsBottom().equals("Y")){
					logger.info(pointIndexArray[i]+" "+StockDateUtil.SDF_TIME.format(dto.getBeginTime())+"\t"+
							StockDateUtil.SDF_TIME.format(dto.getEndTime())+"\t"+
							"�ϲ�["+dto.getStickNumber()+"]��K��"+"\t"+
							"�� ["+dto.getLow()+"]["+dto.getHigh()+"]");
				}
			}
		}
		
		/**
		 * 5.��װ����������طֱʽ��
		 */
		boolean [] actualResultArray = new boolean[mergeSticksList.size()];
		
		for(int i = 0;i<resultArray.length;i++){
			if(resultArray[i]==true){
				actualResultArray[pointIndexArray[i]] = true;
			}
		}
		
		return actualResultArray;
	}
	
	
	/**
	 * �鿴������ȷ�ıʻ���
	 * 
	 * @param mergeSticksList
	 * @param pointIndexArray
	 * @param pointIndexMatrix
	 * @return
	 */
	public static boolean checkFinalTwigPartition(List<MergeLineDTO> mergeSticksList,
			Integer[] pointIndexArray, boolean[][] pointIndexMatrix,
			boolean [] resultArray,int endIndex){
		
		return searchFinalTwigPartition(mergeSticksList,
				pointIndexArray,pointIndexMatrix,
				resultArray,0,endIndex);
		
	}
	
	public static boolean searchFinalTwigPartition(List<MergeLineDTO> mergeSticksList,
			Integer[] pointIndexArray, boolean[][] pointIndexMatrix,
			boolean [] resultArray,int index,int endIndex){
		
		if(index==endIndex){
			return true;
		}else{
			for(int i = index+1;i<=endIndex;i++){
				if(pointIndexMatrix[index][i]==true){
					resultArray[index]=true;
					resultArray[i]=true;
					if(searchFinalTwigPartition(mergeSticksList,
							pointIndexArray,pointIndexMatrix,
							resultArray,i,endIndex)){
						return true;
					}else{
						resultArray[index]=false;
						resultArray[i]=false;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * ͨ����̬�滮�����Ҿֲ��⣬�����pointIndexMatrix��
	 * @param mergeSticksList
	 * @param pointIndexArray
	 * @param pointIndexMatrix
	 */
	public static void findValidPointByDP(List<MergeLineDTO> mergeSticksList,
			Integer[] pointIndexArray, boolean[][] pointIndexMatrix) {
		
		for(int dist = 1;dist<pointIndexArray.length;dist=dist+2){
			//ȡ���� �����ݵ�һ�׶ε�Ԥ�������Ϊż���Ķ���ͬ�����
			processByDist(mergeSticksList, pointIndexArray, pointIndexMatrix,
					dist);
		}
	}
	
	/**
	 * ������Ϊdist�ķ�����ϣ��Ƿ��ܳ�һ��
	 * @param mergeSticksList
	 * @param pointIndexArray
	 * @param pointIndexMatrix
	 * @param dist
	 */
	public static void processByDist(List<MergeLineDTO> mergeSticksList,
			Integer[] pointIndexArray, boolean[][] pointIndexMatrix,int dist){
		
		for(int i = 0;i<pointIndexArray.length-dist;i++){
			boolean checkResult=
				check2PointIsMultiLine(mergeSticksList, pointIndexArray, pointIndexMatrix,
					i, i + dist);
			
			if(checkResult == true){
				pointIndexMatrix[i][i + dist] = false;
			}else{
				if(validatePeakAndBottom(mergeSticksList,
						pointIndexArray[i],pointIndexArray[i + dist])){
					pointIndexMatrix[i][i + dist]=true;
				}else{
					pointIndexMatrix[i][i + dist]=false;
				}
			}
		}
	}
	
	/**
	 * �ݹ鴦���𲽼��[startIndex��endIndex]�Ƿ��ܻ��ֳɶ��
	 * @param mergeSticksList
	 * @param pointIndexArray
	 * @param pointIndexMatrix
	 * @param startIndex
	 * @param endIndex
	 * @return
	 */
	public static boolean check2PointIsMultiLine(List<MergeLineDTO> mergeSticksList,
			Integer[] pointIndexArray, boolean[][] pointIndexMatrix,
			int startIndex, int endIndex) {
		
		if(startIndex==endIndex){
			return true;
		}else{
			for(int i = startIndex+1;i<=endIndex;i++){
				if(pointIndexMatrix[startIndex][i]==true){
					boolean result = 
						check2PointIsMultiLine(mergeSticksList,pointIndexArray,
							pointIndexMatrix,
							i,endIndex);
					if(result == true){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * У���Ƿ�����һ��
	 */
	private static boolean validatePeakAndBottom(
			List<MergeLineDTO> mergeSticksList,int startIndex,int endIndex){
		
		//1.�����㶥������ŵס���ױ�����Ŷ�
		MergeLineDTO startDTO = mergeSticksList.get(startIndex);
		MergeLineDTO endDTO = mergeSticksList.get(endIndex);
		if(startIndex==0){
			if(endDTO.getIsPeak().equals("N")&&
					!endDTO.getIsBottom().equals("N")){
				return false;
			}
		}else if((startDTO.getIsPeak().equals("Y")&&endDTO.getIsPeak().equals("Y"))
					||(startDTO.getIsBottom().equals("Y")&&endDTO.getIsBottom().equals("Y"))){
			return false;
		}
		
		//2.��������׷��;�����������󣬲�������K��
		if(endIndex - startIndex<3){
			return false;
		}
		
		//3.�����������K�ߺ͵׷��͵����K��֮�䣨����������K�ߣ��������ǰ�����ϵ��������3��������3��������K��
		int kLineNumber=0;
		
		//��߻�����ͼ��k��
		for (int i = 0; i < startDTO.getMemberList().size(); i++) {
			StockKLinePriceDTO dto = startDTO.getMemberList().get(i);
			if(startDTO.getIsPeak().equals("Y")&&
					dto.getHigh().equals(startDTO.getHigh())){//���������Ƕ�Ԫ��
				kLineNumber+=(startDTO.getMemberList().size()-i-1);
				break;
			}else if(startDTO.getIsBottom().equals("Y")&&
					dto.getLow().equals(startDTO.getLow())){//�ף������ǵ�Ԫ��
				kLineNumber+=(startDTO.getMemberList().size()-i-1);
				break;
			}
		}
		for (int i = 0; i < endDTO.getMemberList().size(); i++) {
			StockKLinePriceDTO dto = endDTO.getMemberList().get(i);
			if(endDTO.getIsBottom().equals("Y")&&
					dto.getLow().equals(endDTO.getLow())){//��,�����ǵ�Ԫ��
				kLineNumber+=(i);
				break;
			}else if(endDTO.getIsPeak().equals("Y")&&
					dto.getHigh().equals(endDTO.getHigh())){//���������Ƕ�Ԫ��
				kLineNumber+=(i);
				break;
			}
		}
		//�����м�Ԫ�ص�k�ߺϼ�
		for(int i = startIndex+1;i<endIndex;i++){
			MergeLineDTO dto = mergeSticksList.get(i);
			kLineNumber+=dto.getStickNumber();
		}
		if(kLineNumber<3){
			return false;
		}
		
		//4.���׷ֱ��Ǳ���(������ɷ��͵�Ԫ��)����ߺ����
		MergeLineDTO peakDTO = null;
		MergeLineDTO bottomDTO = null;
		if(startDTO.getIsPeak().equals("Y")){
			peakDTO = startDTO;
			bottomDTO = endDTO;
		}else{
			peakDTO = endDTO;
			bottomDTO = startDTO;
		}
		
		for(int i = (startIndex==0)?0:startIndex-1;i<= endIndex+1;i++){
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
		
		//5.�������м��������Ķ��͵�
		
		
		//6.������Ҫ�ж϶��׵������Ƿ����ص�
		
		return true;
	}
}
