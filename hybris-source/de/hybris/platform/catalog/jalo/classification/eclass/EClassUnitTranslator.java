package de.hybris.platform.catalog.jalo.classification.eclass;

import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.classification.ClassificationAttributeUnit;
import de.hybris.platform.catalog.jalo.classification.ClassificationSystem;
import de.hybris.platform.catalog.jalo.classification.ClassificationSystemVersion;
import de.hybris.platform.impex.jalo.header.HeaderValidationException;
import de.hybris.platform.impex.jalo.header.StandardColumnDescriptor;
import de.hybris.platform.impex.jalo.translators.SingleValueTranslator;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import org.apache.log4j.Logger;

public class EClassUnitTranslator extends SingleValueTranslator
{
    private static final Logger LOG = Logger.getLogger(EClassUnitTranslator.class);
    public static final String SYSTEM_NAME = "systemName";
    public static final String SYSTEM_VERSION = "systemVersion";
    private ClassificationSystemVersion target;


    public void init(StandardColumnDescriptor columnDescriptor)
    {
        super.init(columnDescriptor);
        String name = columnDescriptor.getDescriptorData().getModifier("systemName");
        String version = columnDescriptor.getDescriptorData().getModifier("systemVersion");
        if(name != null && name.length() > 0 && version != null && version.length() > 0)
        {
            ClassificationSystem classificationSystem = CatalogManager.getInstance().getClassificationSystem(name);
            if(classificationSystem != null)
            {
                this.target = (ClassificationSystemVersion)classificationSystem.getCatalogVersion(version);
            }
        }
    }


    public void validate(StandardColumnDescriptor columnDescriptor) throws HeaderValidationException
    {
        super.validate(columnDescriptor);
        if(this.target == null)
        {
            throw new HeaderValidationException(columnDescriptor.getHeader(), "missing systemName and/or systemVersion - cannot determine target system", 0);
        }
    }


    protected ClassificationAttributeUnit getOrCreateUnit(String code)
    {
        try
        {
            return this.target.getAttributeUnit(code);
        }
        catch(JaloItemNotFoundException e)
        {
            try
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("creating new classification unit '" + code + "'");
                }
                return this.target.createAttributeUnit(code, code);
            }
            catch(ConsistencyCheckException e1)
            {
                LOG.warn("could not create classification unit '" + code + "' due to " + e1.getMessage() + " - skipped", (Throwable)e1);
                return null;
            }
        }
    }


    protected Object convertToJalo(String expr, Item forItem)
    {
        return getOrCreateUnit(expr);
    }


    protected String convertToString(Object value)
    {
        return ((ClassificationAttributeUnit)value).getCode();
    }
}
