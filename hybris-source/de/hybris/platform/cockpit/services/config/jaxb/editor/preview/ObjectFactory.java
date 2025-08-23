package de.hybris.platform.cockpit.services.config.jaxb.editor.preview;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory
{
    private static final QName _Root_QNAME = new QName("", "root");


    public SectionsListType createSectionsListType()
    {
        return new SectionsListType();
    }


    public ColumnsTitlesType createColumnsTitlesType()
    {
        return new ColumnsTitlesType();
    }


    public RowType createRowType()
    {
        return new RowType();
    }


    public TableColumnType createTableColumnType()
    {
        return new TableColumnType();
    }


    public TableRowType createTableRowType()
    {
        return new TableRowType();
    }


    public ReferenceType createReferenceType()
    {
        return new ReferenceType();
    }


    public AttributeType createAttributeType()
    {
        return new AttributeType();
    }


    public AttributesType createAttributesType()
    {
        return new AttributesType();
    }


    public TitleType createTitleType()
    {
        return new TitleType();
    }


    public SectionType createSectionType()
    {
        return new SectionType();
    }


    public TableRowsType createTableRowsType()
    {
        return new TableRowsType();
    }


    @XmlElementDecl(namespace = "", name = "root")
    public JAXBElement<SectionsListType> createRoot(SectionsListType value)
    {
        return new JAXBElement(_Root_QNAME, SectionsListType.class, null, value);
    }
}
