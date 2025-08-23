package com.hybris.datahub.dto.extension;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"name", "type", "exportURL", "userName", "password", "exportCodes", "targetItems"})
public class TargetSystem
{
    @XmlElement(required = true)
    private String name;
    @XmlElement(required = true)
    private String type;
    @XmlElement(required = true)
    @XmlSchemaType(name = "anyURI")
    private String exportURL;
    @XmlElement(required = true)
    private String userName;
    @XmlElement(required = true)
    private String password;
    @XmlElement(required = true)
    private ExportCodes exportCodes;
    @XmlElement(required = true)
    private TargetItems targetItems;


    public String getName()
    {
        return this.name;
    }


    public void setName(String value)
    {
        this.name = value;
    }


    public TargetSystem named(String name)
    {
        setName(name);
        return this;
    }


    public String getType()
    {
        return this.type;
    }


    public void setType(String value)
    {
        this.type = value;
    }


    public TargetSystem ofType(String type)
    {
        setType(type);
        return this;
    }


    public String getExportURL()
    {
        return this.exportURL;
    }


    public void setExportURL(String value)
    {
        this.exportURL = value;
    }


    public TargetSystem withExportUrl(String url)
    {
        setExportURL(url);
        return this;
    }


    public String getUserName()
    {
        return this.userName;
    }


    public void setUserName(String value)
    {
        this.userName = value;
    }


    public TargetSystem withExportUser(String uname)
    {
        setUserName(uname);
        return this;
    }


    public String getPassword()
    {
        return this.password;
    }


    public void setPassword(String value)
    {
        this.password = value;
    }


    public TargetSystem withExportPassword(String pwd)
    {
        setPassword(pwd);
        return this;
    }


    public ExportCodes getExportCodes()
    {
        return this.exportCodes;
    }


    public void setExportCodes(ExportCodes value)
    {
        this.exportCodes = value;
    }


    public TargetItems getTargetItems()
    {
        return this.targetItems;
    }


    public void setTargetItems(TargetItems value)
    {
        this.targetItems = value;
    }


    public TargetSystem withItemType(TargetItems.Item type)
    {
        if(this.targetItems == null)
        {
            this.targetItems = new TargetItems();
        }
        this.targetItems.getItemList().add(type);
        return this;
    }


    public boolean matchesExpected(TargetSystem expected)
    {
        if(expected == null)
        {
            return false;
        }
        boolean matchesName = (expected.getName() == null || expected.getName().equals(this.name));
        boolean matchesType = (expected.getType() == null || expected.getType().equals(this.type));
        boolean matchesExportURL = (expected.getExportURL() == null || expected.getExportURL().equals(this.exportURL));
        boolean matchesUserName = (expected.getUserName() == null || expected.getUserName().equals(this.userName));
        return (matchesName && matchesType && matchesExportURL && matchesUserName);
    }


    public String toString()
    {
        return "TargetSystem{name='" + this.name + "', type='" + this.type + "', exportURL='" + this.exportURL + "', userName='*******', password='*******', exportCodes=" + this.exportCodes + ", targetItems=" + this.targetItems + "}";
    }
}
