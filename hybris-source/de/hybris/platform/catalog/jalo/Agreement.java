package de.hybris.platform.catalog.jalo;

import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import java.util.HashSet;
import java.util.Set;

public class Agreement extends GeneratedAgreement
{
    @SLDSafe(portingClass = "UniqueAttributesInterceptor,MandatoryAttributesValidator")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Set missing = new HashSet();
        if(((!checkMandatoryAttribute("id", allAttributes, missing) ? 1 : 0) | (
                        !checkMandatoryAttribute("enddate", allAttributes, missing) ? 1 : 0)) != 0)
        {
            throw new JaloInvalidParameterException("Missing " + missing + " for creating a new Agreement", 0);
        }
        return super.createItem(ctx, type, allAttributes);
    }


    @SLDSafe(portingClass = "de.hybris.platform.catalog.interceptors.AgreementPrepareInterceptor", portingMethod = "get")
    public void setCatalogVersion(SessionContext ctx, CatalogVersion catalogVersion)
    {
        super.setCatalogVersion(ctx, catalogVersion);
        setProperty(ctx, "Catalog", (catalogVersion != null) ? catalogVersion.getCatalog() : null);
    }
}
