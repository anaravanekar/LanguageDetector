package com.keysight.lang.detector.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;


@Entity(name="MDM_ACCOUNT_ADDRESS")
public class MDMAccountAddress{

	@Id
	@Column(name="ROW_NO")
	private Integer rowNumber;

	/*@Id
	@Column(name="SYSTEM_ID")
    private String systemId;
	
	@Id
	@Column(name="SYSTEM_NAME")
    private String systemName;
	
	@Id
	@Column(name="INTERFACE_STATUS")
    private String interfaceStatus;*/
	
	@Column(name="ADDRESS")
    private String address;

	@Column(name="LOCALE")
    private String locale;

	
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(Integer rowNumber) {
		this.rowNumber = rowNumber;
	}

	@Override
	public String toString() {
		return "MDMAccountAddress{" +
				"rowNumber=" + rowNumber +
				", address='" + address + '\'' +
				", locale='" + locale + '\'' +
				'}';
	}
}
