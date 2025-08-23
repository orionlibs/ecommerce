package de.hybris.platform.cockpit.services.config.jaxb.advancedsearch;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "shortcutValueList", propOrder = {"value"})
public class ShortcutValueList
{
    @XmlElement(required = true)
    protected List<ShortcutValue> value;


    public List<ShortcutValue> getValue()
    {
        if(this.value == null)
        {
            this.value = new ArrayList<>();
        }
        return this.value;
    }
}
