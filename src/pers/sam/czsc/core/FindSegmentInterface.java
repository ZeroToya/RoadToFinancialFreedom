package pers.sam.czsc.core;

import java.util.List;

import pers.sam.czsc.dto.StrokeDTO;

/**
 * �߶λ��ֽӿ�
 * @author Administrator
 *
 */
public interface FindSegmentInterface {
	
	/**
	 * ����ֱ�����strokeList
	 * @param strokeList
	 */
	public void findSegment(List <StrokeDTO> strokeList);
	
}
