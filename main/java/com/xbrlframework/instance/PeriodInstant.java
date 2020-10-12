/*
 * created by github.com/marcioAlexandre
 * Jun01, 2018
 * 
 */

package com.xbrlframework.instance;

public class PeriodInstant implements Period {
	
	private final String periodInstantName = "xbrl:periodInstant"; 
	private String periodInstantValue;
	
	public PeriodInstant(String periodInstantValue) {
		this.periodInstantValue = periodInstantValue;
	}
	
	public String getInstantPeriodvalue() {
		return periodInstantValue;
	}
	public void setInstantePeriodvalue(String periodInstantValue) {
		this.periodInstantValue = periodInstantValue;
	}
	public String getInstantePeriodName() {
		return periodInstantName;
	}

}
