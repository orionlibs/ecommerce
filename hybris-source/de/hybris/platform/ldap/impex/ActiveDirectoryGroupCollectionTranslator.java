package de.hybris.platform.ldap.impex;

import de.hybris.platform.impex.jalo.header.StandardColumnDescriptor;
import de.hybris.platform.impex.jalo.translators.AbstractValueTranslator;
import de.hybris.platform.impex.jalo.translators.CollectionValueTranslator;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.Config;
import org.apache.log4j.Logger;

public class ActiveDirectoryGroupCollectionTranslator extends CollectionValueTranslator
{
    private static final Logger log = Logger.getLogger(ActiveDirectoryGroupCollectionTranslator.class.getName());
    private static final String DEFAULT_GROUP_ID = "dn";
    protected static String groupid = "dn";


    protected static String getGroupId()
    {
        if("dn".equalsIgnoreCase(groupid) &&
                        shouldConvertToChar())
        {
            return " to_char({dn}) ";
        }
        return " {" + groupid + " } ";
    }


    private static final boolean shouldConvertToChar()
    {
        return (Config.isOracleUsed() || Config.isHanaUsed());
    }


    public ActiveDirectoryGroupCollectionTranslator()
    {
        super((CollectionType)TypeManager.getInstance()
                        .getComposedType(Principal.class)
                        .getAttributeDescriptor("groups")
                        .getAttributeType(), (AbstractValueTranslator)new PrincipalGroupTranslator());
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
