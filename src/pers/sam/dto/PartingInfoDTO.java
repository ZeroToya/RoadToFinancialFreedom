package pers.sam.dto;

import java.util.Date;

/**
 * �ֱ�״̬DTO��stock_parting_info������"����"
 * @author lizandeng(Sam Lee)
 * @version 2015-5-31 ����11:25:56
 */
public class PartingInfoDTO {
	
	//���͹�����
	public static String PARTING_FORMING="P[0]-forming";
	//����ȷ������Ϊ�ʵĹ�����
	public static String PARTING_EXTENDING="P[1]-extending";
	
	//���ϱ�
	public static String DIRECTION_UP ="D[1]-up";
	//���±�
	public static String DIRECTION_DOWN ="D[-1]-down";
	
	/**
	 * ��Ʊ����
	 */
	private String stockCode;
	
	/**
	 * ��������
	 */
	private String period;
	
	/**
	 * ���ͳ��ֵ�ʱ���
	 */
	private Date partingDate;
	
	/**
	 * �ʵķ��������ϻ������±��У�
	 */
	private String partingDirection;
	
	/**
	 * ��ǰ��״̬��PARTING_STATUS��
	 */
	private String partingStatus;

	public String getStockCode() {
		return stockCode;
	}

	public void setStockCode(String stockCode) {
		this.stockCode = stockCode;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public Date getPartingDate() {
		return partingDate;
	}

	public void setPartingDate(Date partingDate) {
		this.partingDate = partingDate;
	}

	public String getPartingDirection() {
		return partingDirection;
	}

	public void setPartingDirection(String partingDirection) {
		this.partingDirection = partingDirection;
	}

	public String getPartingStatus() {
		return partingStatus;
	}

	public void setPartingStatus(String partingStatus) {
		this.partingStatus = partingStatus;
	} 
}
