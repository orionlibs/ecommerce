package de.hybris.platform.cockpit.services.config.jaxb.editor;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"groupOrCustomGroup"})
@XmlRootElement(name = "editor")
public class Editor
{
    @XmlElements({@XmlElement(name = "group", type = Group.class), @XmlElement(name = "custom-group", type = CustomGroup.class)})
    protected List<Object> groupOrCustomGroup;


    public List<Object> getGroupOrCustomGroup()
    {
        if(this.groupOrCustomGroup == null)
        {
            this.groupOrCustomGroup = new ArrayList();
        }
        return this.groupOrCustomGroup;
    }
}
