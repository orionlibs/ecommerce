package de.hybris.platform.ldap.impex;

import de.hybris.platform.impex.jalo.header.StandardColumnDescriptor;
import de.hybris.platform.impex.jalo.translators.AbstractValueTranslator;
import de.hybris.platform.impex.jalo.translators.CollectionValueTranslator;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.TypeManager;
import org.apache.log4j.Logger;

public class ExtractUserGroupTranslator extends CollectionValueTranslator
{
    private static final Logger log = Logger.getLogger(ExtractUserGroupTranslator.class.getName());
    protected static String groupid = "dn";


    public ExtractUserGroupTranslator()
    {
        super((CollectionType)TypeManager.getInstance()
                        .getComposedType(Principal.class)
                        .getAttributeDescriptor("groups")
                        .getAttributeType(), (AbstractValueTranslator)new UserGroupTranslator());
    }


    public void init(StandardColumnDescriptor columnDescriptor)
    {
        super.init(columnDescriptor);
        String customIdentifier = columnDescriptor.getDescriptorData().getModifier("groupid");
        if(customIdentifier != null && customIdentifier.length() > 0)
        {
            groupid = customIdentifier;
        }
    }


    public Object importValue(String valueExpr, Item forItem) throws JaloInvalidParameterException
    {
        if(log.isDebugEnabled())
        {
            log.debug("importValue( " + valueExpr + ", " + forItem + " )");
        }
        if(forItem == null)
        {
            setError();
            return null;
        }
        Object result = super.importValue(valueExpr, forItem);
        clearStatus();
        return result;
    }
}
