package com.keysight.lang.detector.model;

import java.io.Serializable;


class MDMAccountId  implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -4078751850636628581L;


	private String systemId;
	
	
    private String systemName;
	
    private String interfaceStatus;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((interfaceStatus == null) ? 0 : interfaceStatus.hashCode());
		result = prime * result
				+ ((systemId == null) ? 0 : systemId.hashCode());
		result = prime * result
				+ ((systemName == null) ? 0 : systemName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MDMAccountId other = (MDMAccountId) obj;
		if (interfaceStatus == null) {
			if (other.interfaceStatus != null)
				return false;
		} else if (!interfaceStatus.equals(other.interfaceStatus))
			return false;
		if (systemId == null) {
			if (other.systemId != null)
				return false;
		} else if (!systemId.equals(other.systemId))
			return false;
		if (systemName == null) {
			if (other.systemName != null)
				return false;
		} else if (!systemName.equals(other.systemName))
			return false;
		return true;
	}

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
    
}
