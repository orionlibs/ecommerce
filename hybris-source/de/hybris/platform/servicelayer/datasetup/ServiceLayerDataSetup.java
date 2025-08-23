package de.hybris.platform.servicelayer.datasetup;

import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.impex.model.cronjob.ImpExExportJobModel;
import de.hybris.platform.servicelayer.cronjob.JobPerformable;
import de.hybris.platform.servicelayer.cronjob.TypeAwareJobPerformable;
import de.hybris.platform.servicelayer.internal.model.ServicelayerJobModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import java.util.Arrays;
import java.util.Map;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

@SystemSetup(extension = "processing")
public class ServiceLayerDataSetup implements ApplicationContextAware
{
    private static final String IMP_EX_EXPORT_JOB_CODE = "ImpEx-Export";
    private ModelService modelService;
    private FlexibleSearchService flexibleSearchService;
    private ApplicationContext applicationContext;


    @SystemSetup(type = SystemSetup.Type.ESSENTIAL, process = SystemSetup.Process.ALL)
    public void setup()
    {
        createJobPerformables();
        createExportJobIfNeeded();
    }


    private void createJobPerformables()
    {
        FlexibleSearchQuery fsq = new FlexibleSearchQuery("SELECT COUNT({pk}) FROM {ServicelayerJob} WHERE {springId}=?springid");
        fsq.setResultClassList(Arrays.asList((Class<?>[][])new Class[] {Integer.class}));
        Map<String, JobPerformable> beans = this.applicationContext.getBeansOfType(JobPerformable.class);
        for(Map.Entry<String, JobPerformable> entry : beans.entrySet())
        {
            fsq.addQueryParameter("springid", entry.getKey());
            if(((Integer)this.flexibleSearchService.search(fsq).getResult().get(0)).intValue() == 0)
            {
                ServicelayerJobModel servicelayerJobModel;
                if(entry.getValue() instanceof TypeAwareJobPerformable)
                {
                    TypeAwareJobPerformable jta = (TypeAwareJobPerformable)entry.getValue();
                    if(jta.createDefaultJob())
                    {
                        servicelayerJobModel = (ServicelayerJobModel)this.modelService.create(jta.getType());
                    }
                    else
                    {
                        continue;
                    }
                }
                else
                {
                    servicelayerJobModel = (ServicelayerJobModel)this.modelService.create(ServicelayerJobModel.class);
                }
                servicelayerJobModel.setCode(entry.getKey());
                servicelayerJobModel.setSpringId(entry.getKey());
                this.modelService.save(servicelayerJobModel);
            }
        }
    }


    private void createExportJobIfNeeded()
    {
        if(isExportJobExist())
        {
            return;
        }
        ImpExExportJobModel exportJob = (ImpExExportJobModel)this.modelService.create(ImpExExportJobModel.class);
        exportJob.setCode("ImpEx-Export");
        this.modelService.save(exportJob);
    }


    private boolean isExportJobExist()
    {
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT COUNT(pk) FROM {ImpExExportJob} WHERE {code}=?code");
        fQuery.setResultClassList(Arrays.asList((Class<?>[][])new Class[] {Integer.class}));
        fQuery.addQueryParameter("code", "ImpEx-Export");
        return (((Integer)this.flexibleSearchService.search(fQuery).getResult().get(0)).intValue() == 1);
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        this.applicationContext = applicationContext;
    }
}
