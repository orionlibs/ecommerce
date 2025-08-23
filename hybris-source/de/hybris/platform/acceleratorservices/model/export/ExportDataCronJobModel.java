package de.hybris.platform.acceleratorservices.model.export;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.internal.model.ServicelayerJobModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.store.BaseStoreModel;
import java.util.List;

public class ExportDataCronJobModel extends CronJobModel
{
    public static final String _TYPECODE = "ExportDataCronJob";
    public static final String _JOBCRONJOBRELATION = "JobCronJobRelation";
    public static final String BASESTORE = "baseStore";
    public static final String CMSSITE = "cmsSite";
    public static final String LANGUAGE = "language";
    public static final String CURRENCY = "currency";
    public static final String USER = "user";
    public static final String THIRDPARTYHOST = "thirdPartyHost";
    public static final String THIRDPARTYUSERNAME = "thirdPartyUsername";
    public static final String THIRDPARTYPASSWORD = "thirdPartyPassword";
    public static final String DATAGENERATIONPIPELINE = "dataGenerationPipeline";
    public static final String HISTORYENTRIES = "historyEntries";


    public ExportDataCronJobModel()
    {
    }


    public ExportDataCronJobModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ExportDataCronJobModel(String _dataGenerationPipeline, ServicelayerJobModel _job)
    {
        setDataGenerationPipeline(_dataGenerationPipeline);
        setJob((JobModel)_job);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ExportDataCronJobModel(String _dataGenerationPipeline, ServicelayerJobModel _job, ItemModel _owner)
    {
        setDataGenerationPipeline(_dataGenerationPipeline);
        setJob((JobModel)_job);
        setOwner(_owner);
    }


    @Accessor(qualifier = "baseStore", type = Accessor.Type.GETTER)
    public BaseStoreModel getBaseStore()
    {
        return (BaseStoreModel)getPersistenceContext().getPropertyValue("baseStore");
    }


    @Accessor(qualifier = "cmsSite", type = Accessor.Type.GETTER)
    public CMSSiteModel getCmsSite()
    {
        return (CMSSiteModel)getPersistenceContext().getPropertyValue("cmsSite");
    }


    @Accessor(qualifier = "currency", type = Accessor.Type.GETTER)
    public CurrencyModel getCurrency()
    {
        return (CurrencyModel)getPersistenceContext().getPropertyValue("currency");
    }


    @Accessor(qualifier = "dataGenerationPipeline", type = Accessor.Type.GETTER)
    public String getDataGenerationPipeline()
    {
        return (String)getPersistenceContext().getPropertyValue("dataGenerationPipeline");
    }


    @Accessor(qualifier = "historyEntries", type = Accessor.Type.GETTER)
    public List<ExportDataHistoryEntryModel> getHistoryEntries()
    {
        return (List<ExportDataHistoryEntryModel>)getPersistenceContext().getPropertyValue("historyEntries");
    }


    @Accessor(qualifier = "job", type = Accessor.Type.GETTER)
    public ServicelayerJobModel getJob()
    {
        return (ServicelayerJobModel)super.getJob();
    }


    @Accessor(qualifier = "language", type = Accessor.Type.GETTER)
    public LanguageModel getLanguage()
    {
        return (LanguageModel)getPersistenceContext().getPropertyValue("language");
    }


    @Accessor(qualifier = "thirdPartyHost", type = Accessor.Type.GETTER)
    public String getThirdPartyHost()
    {
        return (String)getPersistenceContext().getPropertyValue("thirdPartyHost");
    }


    @Accessor(qualifier = "thirdPartyPassword", type = Accessor.Type.GETTER)
    public String getThirdPartyPassword()
    {
        return (String)getPersistenceContext().getPropertyValue("thirdPartyPassword");
    }


    @Accessor(qualifier = "thirdPartyUsername", type = Accessor.Type.GETTER)
    public String getThirdPartyUsername()
    {
        return (String)getPersistenceContext().getPropertyValue("thirdPartyUsername");
    }


    @Accessor(qualifier = "user", type = Accessor.Type.GETTER)
    public UserModel getUser()
    {
        return (UserModel)getPersistenceContext().getPropertyValue("user");
    }


    @Accessor(qualifier = "baseStore", type = Accessor.Type.SETTER)
    public void setBaseStore(BaseStoreModel value)
    {
        getPersistenceContext().setPropertyValue("baseStore", value);
    }


    @Accessor(qualifier = "cmsSite", type = Accessor.Type.SETTER)
    public void setCmsSite(CMSSiteModel value)
    {
        getPersistenceContext().setPropertyValue("cmsSite", value);
    }


    @Accessor(qualifier = "currency", type = Accessor.Type.SETTER)
    public void setCurrency(CurrencyModel value)
    {
        getPersistenceContext().setPropertyValue("currency", value);
    }


    @Accessor(qualifier = "dataGenerationPipeline", type = Accessor.Type.SETTER)
    public void setDataGenerationPipeline(String value)
    {
        getPersistenceContext().setPropertyValue("dataGenerationPipeline", value);
    }


    @Accessor(qualifier = "historyEntries", type = Accessor.Type.SETTER)
    public void setHistoryEntries(List<ExportDataHistoryEntryModel> value)
    {
        getPersistenceContext().setPropertyValue("historyEntries", value);
    }


    @Accessor(qualifier = "job", type = Accessor.Type.SETTER)
    public void setJob(JobModel value)
    {
        if(value == null || value instanceof ServicelayerJobModel)
        {
            super.setJob(value);
        }
        else
        {
            throw new IllegalArgumentException("Given value is not instance of de.hybris.platform.servicelayer.internal.model.ServicelayerJobModel");
        }
    }


    @Accessor(qualifier = "language", type = Accessor.Type.SETTER)
    public void setLanguage(LanguageModel value)
    {
        getPersistenceContext().setPropertyValue("language", value);
    }


    @Accessor(qualifier = "thirdPartyHost", type = Accessor.Type.SETTER)
    public void setThirdPartyHost(String value)
    {
        getPersistenceContext().setPropertyValue("thirdPartyHost", value);
    }


    @Accessor(qualifier = "thirdPartyPassword", type = Accessor.Type.SETTER)
    public void setThirdPartyPassword(String value)
    {
        getPersistenceContext().setPropertyValue("thirdPartyPassword", value);
    }


    @Accessor(qualifier = "thirdPartyUsername", type = Accessor.Type.SETTER)
    public void setThirdPartyUsername(String value)
    {
        getPersistenceContext().setPropertyValue("thirdPartyUsername", value);
    }


    @Accessor(qualifier = "user", type = Accessor.Type.SETTER)
    public void setUser(UserModel value)
    {
        getPersistenceContext().setPropertyValue("user", value);
    }
}
