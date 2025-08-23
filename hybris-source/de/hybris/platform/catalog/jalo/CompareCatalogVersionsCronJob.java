package de.hybris.platform.catalog.jalo;

import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import java.util.HashSet;
import java.util.Set;

public class CompareCatalogVersionsCronJob extends GeneratedCompareCatalogVersionsCronJob
{
    @ForceJALO(reason = "something else")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Set missing = new HashSet();
        if(((!checkMandatoryAttribute("sourceVersion", allAttributes, missing) ? 1 : 0) | (
                        !checkMandatoryAttribute("targetVersion", allAttributes, missing) ? 1 : 0)) != 0)
        {
            throw new JaloInvalidParameterException("missing " + missing + " for creating a new CompareCatalogVersionsCronJob", 0);
        }
        return super.createItem(ctx, type, allAttributes);
    }
}
