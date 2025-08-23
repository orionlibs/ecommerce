package com.hybris.datahub.dto.metadata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Set;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "TargetSystem")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TargetSystemData
{
    private String userName;
    private Set<String> exportCodes;
    private String exportUrl;
    private String targetSystemType;
    private String targetSystemName;
    private String password;


    public String getUserName()
    {
        return this.userName;
    }


    public void setUserName(String userName)
    {
        this.userName = userName;
    }


    public Set<String> getExportCodes()
    {
        return this.exportCodes;
    }


    public void setExportCodes(Set<String> exportCodes)
    {
        this.exportCodes = exportCodes;
    }


    public String getExportUrl()
    {
        return this.exportUrl;
    }


    public void setExportUrl(String exportUrl)
    {
        this.exportUrl = exportUrl;
    }


    public String getTargetSystemType()
    {
        return this.targetSystemType;
    }


    public void setTargetSystemType(String targetSystemType)
    {
        this.targetSystemType = targetSystemType;
    }


    public String getTargetSystemName()
    {
        return this.targetSystemName;
    }


    public void setTargetSystemName(String targetSystemName)
    {
        this.targetSystemName = targetSystemName;
    }


    public String getPassword()
    {
        return this.password;
    }


    public void setPassword(String password)
    {
        this.password = password;
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        TargetSystemData that = (TargetSystemData)o;
        if((this.exportCodes != null) ? !this.exportCodes.equals(that.exportCodes) : (that.exportCodes != null))
        {
            return false;
        }
        if((this.exportUrl != null) ? !this.exportUrl.equals(that.exportUrl) : (that.exportUrl != null))
        {
            return false;
        }
        if((this.password != null) ? !this.password.equals(that.password) : (that.password != null))
        {
            return false;
        }
        if((this.targetSystemName != null) ? !this.targetSystemName.equals(that.targetSystemName) : (that.targetSystemName != null))
        {
            return false;
        }
        if((this.targetSystemType != null) ? !this.targetSystemType.equals(that.targetSystemType) : (that.targetSystemType != null))
        {
            return false;
        }
        if((this.userName != null) ? !this.userName.equals(that.userName) : (that.userName != null))
        {
            return false;
        }
    }


    public int hashCode()
    {
        int result = (this.userName != null) ? this.userName.hashCode() : 0;
        result = 31 * result + ((this.exportCodes != null) ? this.exportCodes.hashCode() : 0);
        result = 31 * result + ((this.exportUrl != null) ? this.exportUrl.hashCode() : 0);
        result = 31 * result + ((this.targetSystemType != null) ? this.targetSystemType.hashCode() : 0);
        result = 31 * result + ((this.targetSystemName != null) ? this.targetSystemName.hashCode() : 0);
        result = 31 * result + ((this.password != null) ? this.password.hashCode() : 0);
        return result;
    }


    public String toString()
    {
        return "TargetSystemData{userName='" + this.userName + "', exportCodes='" + this.exportCodes + "', exportUrl='" + this.exportUrl + "', targetSystemType='" + this.targetSystemType + "', targetSystemName='" + this.targetSystemName + "', password='" + this.password + "'}";
    }
}
