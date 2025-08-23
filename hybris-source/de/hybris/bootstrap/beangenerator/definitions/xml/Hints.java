package de.hybris.bootstrap.beangenerator.definitions.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "hints", propOrder = {"hints"})
public class Hints
{
    @XmlElement(required = true, name = "hint")
    protected List<Hint> hints;


    public List<Hint> getHints()
    {
        if(this.hints == null)
        {
            this.hints = new ArrayList<>();
        }
        return this.hints;
    }
}
