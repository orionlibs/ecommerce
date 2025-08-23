package de.hybris.platform.cockpit.services.config.jaxb.listview;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dynamicColumnProviders")
public class DynamicColumnProviders
{
    @XmlElements({@XmlElement(name = "spring-bean")})
    protected List<String> springBeans;


    public List<String> getSpringBeans()
    {
        return this.springBeans;
    }


    public void setSpringBeans(List<String> providers)
    {
        this.springBeans = providers;
    }
}
