package pers.sam.czsc.core;

import java.util.List;

import pers.sam.czsc.dto.MergeLineDTO;

/**
 * �ֱ� �ӿ�
 * @author Administrator
 *
 */
public interface DivideSectionInterface {
	
	/**
	 * �ֱ����߼�
	 * ����û���ҵ������ֱ���׳��쳣
	 * @param mergeSticksList
	 * @return
	 * @throws Exception
	 */
	public boolean [] divideSection(List<MergeLineDTO> mergeSticksList) throws Exception;
	
}
