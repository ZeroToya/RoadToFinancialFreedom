package pers.sam.czsc.core;

import java.util.List;

import pers.sam.dto.MergeLineDTO;

/**
 * �ֱ� �ӿ�
 * @author Administrator
 *
 */
public interface DivideSectionInterface {
	
	public boolean [] divideSection(List<MergeLineDTO> mergeSticksList);
	
}
