package de.hybris.platform.cockpit.services.config.jaxb.editor.preview;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ColumnsTitlesType", propOrder = {"title"})
public class ColumnsTitlesType
{
    @XmlElement(required = true)
    protected List<TitleType> title;


    public List<TitleType> getTitle()
    {
        if(this.title == null)
        {
            this.title = new ArrayList<>();
        }
        return this.title;
    }
}
