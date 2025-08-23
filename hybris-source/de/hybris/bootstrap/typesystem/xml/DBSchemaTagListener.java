package de.hybris.bootstrap.typesystem.xml;

import de.hybris.bootstrap.xml.ObjectProcessor;
import de.hybris.bootstrap.xml.ParseAbortException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class DBSchemaTagListener extends AbstractTypeSystemTagListener
{
    public DBSchemaTagListener(AbstractTypeSystemTagListener parent)
    {
        super(parent, "database-schema");
    }


    protected Collection createSubTagListeners()
    {
        return Collections.singletonList(new DBTypeMappingTagListener(this));
    }


    protected Object processEndElement(ObjectProcessor processor) throws ParseAbortException
    {
        String dbName = getAttribute("database");
        String primKey = getAttribute("primary-key");
        String nullStr = getAttribute("null");
        String notNullStr = getAttribute("not-null");
        Map<String, String> typeMappings = getSubTagValueMap("type-mapping");
        getParser().addDBTypeMapping(dbName, primKey, nullStr, notNullStr, typeMappings);
        return null;
    }
}
