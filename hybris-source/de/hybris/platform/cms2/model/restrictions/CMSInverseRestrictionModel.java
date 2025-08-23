package de.hybris.platform.cms2.model.restrictions;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class CMSInverseRestrictionModel extends AbstractRestrictionModel
{
    public static final String _TYPECODE = "CMSInverseRestriction";
    public static final String _ABSTRACTRESTRICTION2CMSINVERSERESTRICTION = "AbstractRestriction2CMSInverseRestriction";
    public static final String ORIGINALRESTRICTION = "originalRestriction";


    public CMSInverseRestrictionModel()
    {
    }


    public CMSInverseRestrictionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CMSInverseRestrictionModel(CatalogVersionModel _catalogVersion, AbstractRestrictionModel _originalRestriction, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setOriginalRestriction(_originalRestriction);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CMSInverseRestrictionModel(CatalogVersionModel _catalogVersion, AbstractRestrictionModel _originalRestriction, ItemModel _owner, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setOriginalRestriction(_originalRestriction);
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "originalRestriction", type = Accessor.Type.GETTER)
    public AbstractRestrictionModel getOriginalRestriction()
    {
        return (AbstractRestrictionModel)getPersistenceContext().getPropertyValue("originalRestriction");
    }


    @Accessor(qualifier = "originalRestriction", type = Accessor.Type.SETTER)
    public void setOriginalRestriction(AbstractRestrictionModel value)
    {
        getPersistenceContext().setPropertyValue("originalRestriction", value);
    }
}
