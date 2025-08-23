package de.hybris.platform.acceleratorcms.model.restrictions;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.restrictions.AbstractRestrictionModel;
import de.hybris.platform.commerceservices.enums.UiExperienceLevel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class CMSUiExperienceRestrictionModel extends AbstractRestrictionModel
{
    public static final String _TYPECODE = "CMSUiExperienceRestriction";
    public static final String UIEXPERIENCE = "uiExperience";


    public CMSUiExperienceRestrictionModel()
    {
    }


    public CMSUiExperienceRestrictionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CMSUiExperienceRestrictionModel(CatalogVersionModel _catalogVersion, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CMSUiExperienceRestrictionModel(CatalogVersionModel _catalogVersion, ItemModel _owner, String _uid)
    {
        setCatalogVersion(_catalogVersion);
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "uiExperience", type = Accessor.Type.GETTER)
    public UiExperienceLevel getUiExperience()
    {
        return (UiExperienceLevel)getPersistenceContext().getPropertyValue("uiExperience");
    }


    @Accessor(qualifier = "uiExperience", type = Accessor.Type.SETTER)
    public void setUiExperience(UiExperienceLevel value)
    {
        getPersistenceContext().setPropertyValue("uiExperience", value);
    }
}
