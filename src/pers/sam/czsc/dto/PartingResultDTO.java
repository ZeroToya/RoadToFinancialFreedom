package pers.sam.czsc.dto;

import java.util.List;

/**
 * �ֱʽ�����󣬰��������֣�
 * 1���ֱ��� 2�� K�ߺϲ�����б� 3���ֱʽ������
 * @author lizandeng(Sam Lee)
 * @version 2015-6-2 ����09:14:03
 */
public class PartingResultDTO {
	
	//�ֱ��б�
	public List<StrokeDTO> strokeList;
	//K�ߺϲ�����б�
	public List<MergeLineDTO> mergeLineList;
	//�ֱʽ�����飨��mergeLineListһ�𣬿ɵó����յķֱʽ����
	public boolean sectionResultArray[];

	public List<StrokeDTO> getStrokeList() {
		return strokeList;
	}

	public void setStrokeList(List<StrokeDTO> strokeList) {
		this.strokeList = strokeList;
	}

	public List<MergeLineDTO> getMergeLineList() {
		return mergeLineList;
	}

	public void setMergeLineList(List<MergeLineDTO> mergeLineList) {
		this.mergeLineList = mergeLineList;
	}

	public boolean[] getSectionResultArray() {
		return sectionResultArray;
	}

	public void setSectionResultArray(boolean[] sectionResultArray) {
		this.sectionResultArray = sectionResultArray;
	}
	
}
