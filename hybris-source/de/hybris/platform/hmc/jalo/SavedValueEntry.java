package de.hybris.platform.hmc.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.encryption.ValueEncryptor;
import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;

public class SavedValueEntry extends GeneratedSavedValueEntry
{
    private static final Logger LOG = Logger.getLogger(SavedValueEntry.class);


    @ForceJALO(reason = "something else")
    public Object getNewValue(SessionContext ctx)
    {
        return decryptIfNeeded(super.getNewValue(ctx));
    }


    @ForceJALO(reason = "something else")
    public Object getOldValue(SessionContext ctx)
    {
        return decryptIfNeeded(super.getOldValue(ctx));
    }


    protected Object encryptIfNeeded(AttributeDescriptor descriptor, Object value)
    {
        if(descriptor.isEncrypted() && value instanceof String)
        {
            ValueEncryptor enc = Registry.getMasterTenant().getValueEncryptor();
            try
            {
                return enc.encrypt((String)value);
            }
            catch(Exception e)
            {
                LOG.warn("Unable to encode attribute: " + descriptor.getQualifier() + " because of: " + e.getMessage() + ". Storing value in plain text.");
                return value;
            }
        }
        return value;
    }


    protected Object decryptIfNeeded(Object value)
    {
        if(getOldValueAttributeDescriptor().isEncrypted() && value instanceof String)
        {
            ValueEncryptor enc = Registry.getMasterTenant().getValueEncryptor();
            try
            {
                return enc.decrypt((String)value);
            }
            catch(Exception e)
            {
                LOG.warn("Value of attribute '" + getModifiedAttribute() + "' marked as encrypted is stored as a plain text!");
                return value;
            }
        }
        return value;
    }


    @ForceJALO(reason = "something else")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Set missing = new HashSet();
        if(((!checkMandatoryAttribute("parent", allAttributes, missing) ? 1 : 0) | (
                        !checkMandatoryAttribute("modifiedAttribute", allAttributes, missing) ? 1 : 0) | (
                        !checkMandatoryAttribute("OldValueAttributeDescriptor", allAttributes, missing) ? 1 : 0)) != 0)
        {
            throw new JaloInvalidParameterException("missing " + missing + " to create a new " + type.getCode(), 0);
        }
        allAttributes.put("newValue",
                        encryptIfNeeded((AttributeDescriptor)allAttributes
                                        .getAllInitialValues().get("OldValueAttributeDescriptor".toLowerCase()), allAttributes
                                        .getAllInitialValues().get("newValue".toLowerCase())));
        allAttributes.put("oldValue",
                        encryptIfNeeded((AttributeDescriptor)allAttributes
                                        .getAllInitialValues().get("OldValueAttributeDescriptor".toLowerCase()), allAttributes
                                        .getAllInitialValues().get("oldValue".toLowerCase())));
        allAttributes.setAttributeMode("parent", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("modifiedAttribute", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("OldValueAttributeDescriptor", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("oldValue", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("newValue", Item.AttributeMode.INITIAL);
        return super.createItem(ctx, type, allAttributes);
    }


    public String toString()
    {
        if(getImplementation() == null)
        {
            return super.toString();
        }
        StringBuilder buf = new StringBuilder();
        buf.append("SavedValueEntry of attribute: '");
        buf.append(getModifiedAttribute());
        buf.append("'");
        return buf.toString();
    }
}
