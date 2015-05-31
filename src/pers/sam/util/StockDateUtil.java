package pers.sam.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A������/ʱ�丨����
 * @author lizandeng(Sam Lee)
 * @version 2015-5-31 ����03:02:45
 */
public class StockDateUtil {
	
	public static final String YYYY_MM_DD="yyyy-MM-dd";
	
	public static final String YYYY_MM_DD_HH_MM_SS="yyyy-MM-dd HH:mm:ss";
	
	public static final String OPEN_TIME_SUFFIX = " 09:30:00";
	
	public static final String CLOSE_TIME_SUFFIX = " 15:00:00";
	
	public static final SimpleDateFormat SDF_TIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static final SimpleDateFormat SDF_DAY = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * ����yyyy-MM-dd�ó�����ʱ��
	 * @param dateStr
	 * @return
	 * @throws ParseException 
	 */
	public static String getDayOpenTime(final String dateStr) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(sdf.parse(dateStr+OPEN_TIME_SUFFIX));
	}

	/**
	 * ����yyyy-MM-dd�ó�����ʱ��
	 * @param dateStr
	 * @return
	 * @throws ParseException 
	 */
	public static String getDayCloseTime(final String dateStr) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(sdf.parse(dateStr+CLOSE_TIME_SUFFIX));
	}
	
	/**
	 * ����yyyy/MM/dd-HH:mm�ó�5����֮ǰ��ʱ���
	 * @param dateStr
	 * @return
	 * @throws ParseException
	 */
	public static String get5MinOpenTime(final String dateStr) throws ParseException{
		
		SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD);
		String dayStr=(dateStr.split("-")[0]).replace("/", "-");
		dayStr=sdf.format(sdf.parse(dayStr));
		
		sdf = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
		Date date = null;
		if("13:00".equals(dateStr.split("-")[1])){//�������ݣ����⴦�� 2014/09/29-13:00
			date = sdf.parse(dayStr+" "+"11:25:00");
		}else{
			date = sdf.parse(dayStr+" "+dateStr.split("-")[1]+":00");
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(Calendar.MINUTE, -5);
			date = c.getTime();
		}
		return sdf.format(date);
	}
	
	/**
	 * ����yyyy/MM/dd-HH:mm�ó���ǰ��ʱ���
	 * @param dateStr
	 * @return
	 * @throws ParseException
	 */
	public static String get5MinCloseTime(final String dateStr) throws ParseException{
		
		SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD);
		String dayStr=(dateStr.split("-")[0]).replace("/", "-");
		dayStr=sdf.format(sdf.parse(dayStr));
		
		sdf = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
		Date date =null;
		if("13:00".equals(dateStr.split("-")[1])){
			date = sdf.parse(dayStr+" "+"11:30:00");;
		}else{
			date = sdf.parse(dayStr+" "+dateStr.split("-")[1]+":00");
		} 

		return sdf.format(date);
	}		
	
	/**
	 * ����yyyy/MM/dd-HH:mm�ó�30����֮ǰ��ʱ���
	 * @param dateStr
	 * @return
	 * @throws ParseException
	 */
	public static String get30MinOpenTime(final String dateStr) throws ParseException{
		
		SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD);
		String dayStr=(dateStr.split("-")[0]).replace("/", "-");
		dayStr=sdf.format(sdf.parse(dayStr));
		
		sdf = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
		Date date = null;
		if("13:00".equals(dateStr.split("-")[1])){//�������ݣ����⴦�� 2012/09/13-13:00
			date = sdf.parse(dayStr+" "+"11:00:00");
		}else{
			date = sdf.parse(dayStr+" "+dateStr.split("-")[1]+":00");
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(Calendar.MINUTE, -30);
			date = c.getTime();
		}
		return sdf.format(date);
	}
	
		
	/**
	 * ����yyyy/MM/dd-HH:mm�ó���ǰ��ʱ���
	 * @param dateStr
	 * @return
	 * @throws ParseException
	 */
	public static String get30MinCloseTime(final String dateStr) throws ParseException{
		
		SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD);
		String dayStr=(dateStr.split("-")[0]).replace("/", "-");
		dayStr=sdf.format(sdf.parse(dayStr));
		
		sdf = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
		Date date =null;
		if("13:00".equals(dateStr.split("-")[1])){
			date = sdf.parse(dayStr+" "+"11:30:00");;
		}else{
			date = sdf.parse(dayStr+" "+dateStr.split("-")[1]+":00");
		} 

		return sdf.format(date);
	}	
	
	/**
	 * ����yyyy/MM/dd�ó���ǰ�������ʱ��
	 * @param dateStr
	 * @return
	 * @throws ParseException
	 */
	public static String getWeekCloseTime(final String dateStr) throws ParseException{
		
		SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD);
		String dayStr=dateStr.replace("/", "-");
		dayStr=sdf.format(sdf.parse(dayStr));
		
		sdf = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
		Date date = sdf.parse(dayStr+CLOSE_TIME_SUFFIX);

		return sdf.format(date);
	}	
	
	/**
	 * ����yyyy/MM/dd�ó���ǰ������Ӧ��һ�ĵĿ���ʱ��
	 * @param dateStr
	 * @return
	 * @throws ParseException
	 */
	public static String getWeekOpenTime(final String dateStr) throws ParseException{
		
		SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD);
		String dayStr=dateStr.replace("/", "-");
		
		Date date = sdf.parse(dayStr);
		
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, 2);
		
		dayStr = sdf.format(c.getTime());
		sdf = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
		date = sdf.parse(dayStr+OPEN_TIME_SUFFIX);

		return sdf.format(date);
	}
	
	/**
	 * ����yyyy/MM/dd�ó���ǰ������Ӧһ�µĵĿ���ʱ��
	 * @param dateStr
	 * @return
	 * @throws ParseException
	 */
	public static String getMonthOpenTime(final String dateStr) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD);
		String dayStr=dateStr.replace("/", "-");
		Date date = sdf.parse(dayStr);
		
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, 1);
		
		dayStr = sdf.format(c.getTime());
		sdf = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
		date = sdf.parse(dayStr+OPEN_TIME_SUFFIX);

		return sdf.format(date);
	}
	
	/**
	 * ����yyyy/MM/dd�ó���ǰ������Ӧһ�µĵ�����ʱ��
	 * @param dateStr
	 * @return
	 * @throws ParseException
	 */
	public static String getMonthCloseTime(final String dateStr) throws ParseException{
		
		SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD);
		String dayStr=dateStr.replace("/", "-");
		
		Date date = sdf.parse(dayStr);
		sdf = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
		date = sdf.parse(dayStr+CLOSE_TIME_SUFFIX);

		return sdf.format(date);
	}
	
	
	/**
	 * @param args
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws ParseException {
//		String dateStr ="2012/09/13-10:00";
//		
//		SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD);
//		String dayStr=(dateStr.split("-")[0]).replace("/", "-");
//		dayStr=sdf.format(sdf.parse(dayStr));
//		
//		sdf = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
//		Date date = sdf.parse(dayStr+" "+dateStr.split("-")[1]+":00");
//		
//		System.out.println(sdf.format(date));
//		
//		Calendar c = Calendar.getInstance();
//		c.setTime(date);
//		c.add(Calendar.MINUTE, -30);
//		System.out.println(sdf.format(c.getTime()));
		
		String dateStr = "1990/12/31";
		System.out.println(getMonthOpenTime(dateStr));
		System.out.println(getMonthCloseTime(dateStr));
		
//		SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD);
//		Date date = sdf.parse(dateStr);
//		
//		Calendar c = Calendar.getInstance();
//		c.setTime(date);
//		
//		c.set(Calendar.DAY_OF_WEEK, 2);
//		
//		System.out.println(sdf.format(c.getTime()));
		
	}	
}
