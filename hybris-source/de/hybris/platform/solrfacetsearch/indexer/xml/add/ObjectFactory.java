package de.hybris.platform.solrfacetsearch.indexer.xml.add;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory
{
    public Add createAdd()
    {
        return new Add();
    }


    public DocType createDocType()
    {
        return new DocType();
    }


    public FieldType createFieldType()
    {
        return new FieldType();
    }
}
