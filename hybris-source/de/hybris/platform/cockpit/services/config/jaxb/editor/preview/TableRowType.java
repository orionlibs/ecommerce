package de.hybris.platform.cockpit.services.config.jaxb.editor.preview;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TableRowType", propOrder = {"column"})
public class TableRowType
{
    @XmlElement(required = true)
    protected List<TableColumnType> column;


    public List<TableColumnType> getColumn()
    {
        if(this.column == null)
        {
            this.column = new ArrayList<>();
        }
        return this.column;
    }
}
