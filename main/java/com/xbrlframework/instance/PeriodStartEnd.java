/*
 * created by github.com/marcioAlexandre
 * Jun01, 2018
 * 
 */
 
package com.xbrlframework.instance;

public class PeriodStartEnd implements Period {
	
	private final String startName = "xbrl:periodStart";
	private String startValue;
	
	private final String endName= "xbrl:periodEnd";
	private String endValue;
	
	
	public PeriodStartEnd(String startValue, String endValue) {
		this.startValue = startValue;
		this.endValue = endValue;
	}
	
	public String getStartValue() {
		return startValue;
	}
	public void setStartValue(String startValue) {
		this.startValue = startValue;
	}
	public String getEndValue() {
		return endValue;
	}
	public void setEndValue(String endValue) {
		this.endValue = endValue;
	}
	public String getStartName() {
		return startName;
	}
	public String getEndName() {
		return endName;
	}	

	
	
}
