package de.hybris.platform.catalog.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class CatalogVersionDifferenceModel extends ItemModel
{
    public static final String _TYPECODE = "CatalogVersionDifference";
    public static final String SOURCEVERSION = "sourceVersion";
    public static final String TARGETVERSION = "targetVersion";
    public static final String CRONJOB = "cronJob";
    public static final String DIFFERENCETEXT = "differenceText";
    public static final String DIFFERENCEVALUE = "differenceValue";


    public CatalogVersionDifferenceModel()
    {
    }


    public CatalogVersionDifferenceModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CatalogVersionDifferenceModel(CompareCatalogVersionsCronJobModel _cronJob, CatalogVersionModel _sourceVersion, CatalogVersionModel _targetVersion)
    {
        setCronJob(_cronJob);
        setSourceVersion(_sourceVersion);
        setTargetVersion(_targetVersion);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CatalogVersionDifferenceModel(CompareCatalogVersionsCronJobModel _cronJob, ItemModel _owner, CatalogVersionModel _sourceVersion, CatalogVersionModel _targetVersion)
    {
        setCronJob(_cronJob);
        setOwner(_owner);
        setSourceVersion(_sourceVersion);
        setTargetVersion(_targetVersion);
    }


    @Accessor(qualifier = "cronJob", type = Accessor.Type.GETTER)
    public CompareCatalogVersionsCronJobModel getCronJob()
    {
        return (CompareCatalogVersionsCronJobModel)getPersistenceContext().getPropertyValue("cronJob");
    }


    @Accessor(qualifier = "differenceText", type = Accessor.Type.GETTER)
    public String getDifferenceText()
    {
        return (String)getPersistenceContext().getPropertyValue("differenceText");
    }


    @Accessor(qualifier = "differenceValue", type = Accessor.Type.GETTER)
    public Double getDifferenceValue()
    {
        return (Double)getPersistenceContext().getPropertyValue("differenceValue");
    }


    @Accessor(qualifier = "sourceVersion", type = Accessor.Type.GETTER)
    public CatalogVersionModel getSourceVersion()
    {
        return (CatalogVersionModel)getPersistenceContext().getPropertyValue("sourceVersion");
    }


    @Accessor(qualifier = "targetVersion", type = Accessor.Type.GETTER)
    public CatalogVersionModel getTargetVersion()
    {
        return (CatalogVersionModel)getPersistenceContext().getPropertyValue("targetVersion");
    }


    @Accessor(qualifier = "cronJob", type = Accessor.Type.SETTER)
    public void setCronJob(CompareCatalogVersionsCronJobModel value)
    {
        getPersistenceContext().setPropertyValue("cronJob", value);
    }


    @Accessor(qualifier = "differenceText", type = Accessor.Type.SETTER)
    public void setDifferenceText(String value)
    {
        getPersistenceContext().setPropertyValue("differenceText", value);
    }


    @Accessor(qualifier = "differenceValue", type = Accessor.Type.SETTER)
    public void setDifferenceValue(Double value)
    {
        getPersistenceContext().setPropertyValue("differenceValue", value);
    }


    @Accessor(qualifier = "sourceVersion", type = Accessor.Type.SETTER)
    public void setSourceVersion(CatalogVersionModel value)
    {
        getPersistenceContext().setPropertyValue("sourceVersion", value);
    }


    @Accessor(qualifier = "targetVersion", type = Accessor.Type.SETTER)
    public void setTargetVersion(CatalogVersionModel value)
    {
        getPersistenceContext().setPropertyValue("targetVersion", value);
    }
}
