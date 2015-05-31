package pers.sam.czsc.core.impl;

import java.util.List;

import pers.sam.czsc.core.FindPeakAndBottomInterface;
import pers.sam.czsc.dto.MergeLineDTO;

/**
 * Ѱ�Ҷ��׷���--ʵ��1
 * @author Administrator
 *
 */
public class FindPeakAndBottomImpl1 implements FindPeakAndBottomInterface {
	
	public void findPeakAndBottom(List<MergeLineDTO> mergeLineList) {
		
		if(mergeLineList.size()<3){
			return;
		}
		
		//���͵ײ��ܹ���K��,dist��Ϊ����
		int dist = 1;
		for(int i = 1;i<mergeLineList.size()-1;i++){
			if(dist == 1){
				MergeLineDTO firstDTO = (MergeLineDTO) mergeLineList.get(i-1);
				MergeLineDTO middleDTO = (MergeLineDTO) mergeLineList.get(i);
				MergeLineDTO lastDTO = (MergeLineDTO) mergeLineList.get(i+1);
				
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

}
