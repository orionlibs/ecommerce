package de.hybris.platform.cockpit.services.config.jaxb.editor.preview;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TableRowsType", propOrder = {"tableRow"})
public class TableRowsType
{
    @XmlElement(required = true)
    protected List<TableRowType> tableRow;


    public List<TableRowType> getTableRow()
    {
        if(this.tableRow == null)
        {
            this.tableRow = new ArrayList<>();
        }
        return this.tableRow;
    }
}
