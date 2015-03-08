package pers.sam.dto;

/**
 * �ֱ�DTO
 * @author Administrator
 *
 */
public class TouchDTO implements Cloneable{
	
	//��ʼ
	private MergeLineDTO  startMLine;
	
	//����
	private MergeLineDTO endMLine;

	//�������ϻ�������  up/down
	private String direction;
	
	public MergeLineDTO getStartMLine() {
		return startMLine;
	}

	public void setStartMLine(MergeLineDTO startMLine) {
		this.startMLine = startMLine;
	}

	public MergeLineDTO getEndMLine() {
		return endMLine;
	}

	public void setEndMLine(MergeLineDTO endMLine) {
		this.endMLine = endMLine;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}
	
	
	public Double getHigh(){
		if("up".equals(direction)){
			return this.getEndMLine().getHigh();
		}else if("down".equals(direction)){
			return this.getStartMLine().getHigh();
		}
		return null;
	}
	
	public Double getLow(){
		if("up".equals(direction)){
			return this.getStartMLine().getLow();
		}else if("down".equals(direction)){
			return this.getEndMLine().getLow();
		}
		return null;
	}

	public TouchDTO clone() { 
		TouchDTO clone = null; 
        try{ 
            clone = (TouchDTO) super.clone(); 
 
        }catch(CloneNotSupportedException e){ 
            throw new RuntimeException(e); // won't happen 
        }
        return clone; 
    }
}
