package com.keysight.lang.detector.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;


@Entity(name="MDM_ACCOUNT")
@IdClass(MDMAccountId.class)
public class MDMAccount{

	@Id
	@Column(name="SYSTEM_ID")
    private String systemId;
	
	@Id
	@Column(name="SYSTEM_NAME")
    private String systemName;
	
	@Id
	@Column(name="INTERFACE_STATUS")
    private String interfaceStatus;
	
	@Column(name="ACCNT_NAME")
    private String accountName;

	@Column(name="LOCALE")
    private String locale;
    
	public String getSystemId() {
		return systemId;
	}
	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}
	public String getSystemName() {
		return systemName;
	}
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	public String getInterfaceStatus() {
		return interfaceStatus;
	}
	public void setInterfaceStatus(String interfaceStatus) {
		this.interfaceStatus = interfaceStatus;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}
	@Override
	public String toString() {
		return "MDMAccount [systemId=" + systemId + ", systemName="
				+ systemName + ", interfaceStatus=" + interfaceStatus
				+ ", accountName=" + accountName + ", locale=" + locale + "]";
	}


}
