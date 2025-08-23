package de.hybris.platform.catalog.model.classification;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.KeywordModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class ClassificationKeywordModel extends KeywordModel
{
    public static final String _TYPECODE = "ClassificationKeyword";
    public static final String EXTERNALID = "externalID";


    public ClassificationKeywordModel()
    {
    }


    public ClassificationKeywordModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ClassificationKeywordModel(ClassificationSystemVersionModel _catalogVersion, String _keyword, LanguageModel _language)
    {
        setCatalogVersion((CatalogVersionModel)_catalogVersion);
        setKeyword(_keyword);
        setLanguage(_language);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ClassificationKeywordModel(ClassificationSystemVersionModel _catalogVersion, String _keyword, LanguageModel _language, ItemModel _owner)
    {
        setCatalogVersion((CatalogVersionModel)_catalogVersion);
        setKeyword(_keyword);
        setLanguage(_language);
        setOwner(_owner);
    }


    @Accessor(qualifier = "catalogVersion", type = Accessor.Type.GETTER)
    public ClassificationSystemVersionModel getCatalogVersion()
    {
        return (ClassificationSystemVersionModel)super.getCatalogVersion();
    }


    @Accessor(qualifier = "externalID", type = Accessor.Type.GETTER)
    public String getExternalID()
    {
        return (String)getPersistenceContext().getPropertyValue("externalID");
    }


    @Accessor(qualifier = "catalogVersion", type = Accessor.Type.SETTER)
    public void setCatalogVersion(CatalogVersionModel value)
    {
        if(value == null || value instanceof ClassificationSystemVersionModel)
        {
            super.setCatalogVersion(value);
        }
        else
        {
            throw new IllegalArgumentException("Given value is not instance of de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel");
        }
    }


    @Accessor(qualifier = "externalID", type = Accessor.Type.SETTER)
    public void setExternalID(String value)
    {
        getPersistenceContext().setPropertyValue("externalID", value);
    }
}
