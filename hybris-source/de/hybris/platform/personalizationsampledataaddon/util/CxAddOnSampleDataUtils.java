package de.hybris.platform.personalizationsampledataaddon.util;

import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.impex.jalo.header.AbstractColumnDescriptor;
import de.hybris.platform.impex.jalo.imp.ImpExImportReader;
import de.hybris.platform.personalizationservices.model.CxAbstractActionModel;
import de.hybris.platform.personalizationservices.model.CxCustomizationsGroupModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

public final class CxAddOnSampleDataUtils
{
    private static final Logger LOG = LoggerFactory.getLogger(CxAddOnSampleDataUtils.class);


    private CxAddOnSampleDataUtils()
    {
        throw new IllegalStateException("CxAddOnSampleDataUtils is an utility class");
    }


    public static final void verifyIfCxActionExists(Map<Integer, String> impexLine, Integer codeIndex, ImpExImportReader impexReader, int cvIndex)
    {
        if(codeIndex.intValue() < 0 || cvIndex < 0 || !impexLine.containsKey(codeIndex) || cvIndex >= impexReader
                        .getCurrentHeader().getColumns().size())
        {
            LOG.debug("Wrong parameter 'index' passed to function");
            return;
        }
        ApplicationContext context = Registry.getApplicationContext();
        FlexibleSearchService flexibleSearchService = (FlexibleSearchService)context.getBean("flexibleSearchService", FlexibleSearchService.class);
        CatalogVersionService catalogVersionService = (CatalogVersionService)context.getBean("catalogVersionService", CatalogVersionService.class);
        String cv = ((AbstractColumnDescriptor)impexReader.getCurrentHeader().getColumns().get(cvIndex)).getDescriptorData().getModifier("default");
        String[] cvArray = cv.split(":");
        CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion(cvArray[0], cvArray[1]);
        CxAbstractActionModel action = new CxAbstractActionModel();
        action.setCode(impexLine.get(codeIndex));
        action.setCatalogVersion(catalogVersion);
        try
        {
            flexibleSearchService.getModelByExample(action);
            impexLine.clear();
        }
        catch(RuntimeException ex)
        {
            LOG.info("Action {} is not exists.", action.getTarget());
        }
    }


    public static final void verifyIfCxCustomizationsGroupExists(Map<Integer, String> impexLine, Integer codeIndex, ImpExImportReader impexReader, int cvIndex)
    {
        if(codeIndex.intValue() < 0 || cvIndex < 0 || !impexLine.containsKey(codeIndex) || cvIndex >= impexReader
                        .getCurrentHeader().getColumns().size())
        {
            LOG.debug("Wrong parameter 'index' passed to function");
            return;
        }
        ApplicationContext context = Registry.getApplicationContext();
        FlexibleSearchService flexibleSearchService = (FlexibleSearchService)context.getBean("flexibleSearchService", FlexibleSearchService.class);
        CatalogVersionService catalogVersionService = (CatalogVersionService)context.getBean("catalogVersionService", CatalogVersionService.class);
        String cv = ((AbstractColumnDescriptor)impexReader.getCurrentHeader().getColumns().get(cvIndex)).getDescriptorData().getModifier("default");
        String[] cvArray = cv.split(":");
        CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion(cvArray[0], cvArray[1]);
        CxCustomizationsGroupModel customizationsGroup = new CxCustomizationsGroupModel();
        customizationsGroup.setCode(impexLine.get(codeIndex));
        customizationsGroup.setCatalogVersion(catalogVersion);
        try
        {
            flexibleSearchService.getModelByExample(customizationsGroup);
            impexLine.clear();
        }
        catch(RuntimeException ex)
        {
            LOG.info("Model {} is note exists.", customizationsGroup.getCode());
        }
    }
}
