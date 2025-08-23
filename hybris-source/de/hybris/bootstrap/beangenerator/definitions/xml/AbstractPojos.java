package de.hybris.bootstrap.beangenerator.definitions.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "abstractPojos", propOrder = {"beanOrEnum"})
public class AbstractPojos
{
    @XmlElements({@XmlElement(name = "bean", type = Bean.class), @XmlElement(name = "enum", type = Enum.class)})
    protected List<AbstractPojo> beanOrEnum;


    public List<AbstractPojo> getBeanOrEnum()
    {
        if(this.beanOrEnum == null)
        {
            this.beanOrEnum = new ArrayList<>();
        }
        return this.beanOrEnum;
    }
}
