package pers.sam.dto;

import java.util.Date;

/**
 * ��������Ԫ��
 * @author Administrator
 *
 */
public class FeatureElementDTO {

	private Date beginTime;
	
	private Date endTime;
	
	private double high;
	
	private double low;
	
	//�ֱ������У���Ԫ�ص����
	private int elementIndex;
	
	public int getElementIndex() {
		return elementIndex;
	}

	public void setElementIndex(int elementIndex) {
		this.elementIndex = elementIndex;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public double getHigh() {
		return high;
	}

	public void setHigh(double high) {
		this.high = high;
	}

	public double getLow() {
		return low;
	}

	public void setLow(double low) {
		this.low = low;
	}
}
