package de.hybris.platform.core.systemsetup;

import com.google.common.base.Stopwatch;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.model.type.SearchRestrictionModel;
import de.hybris.platform.core.systemsetup.datacreator.internal.CoreDataCreator;
import de.hybris.platform.jalo.media.MediaManager;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.Set;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

@SystemSetup(extension = "core")
public class CoreSystemSetup
{
    private static final Logger LOG = Logger.getLogger(CoreSystemSetup.class);
    private FlexibleSearchService flexibleSearchService;
    private ModelService modelService;
    private MediaManager mediaManager;
    private Set<CoreDataCreator> dataCreators;


    @SystemSetup(type = SystemSetup.Type.ESSENTIAL, process = SystemSetup.Process.ALL)
    public synchronized void createEssentialData(SystemSetupContext context) throws Exception
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Executing Essential DataCreators: ");
            LOG.debug("----------------------------------");
        }
        Stopwatch globalSW = Stopwatch.createStarted();
        Stopwatch dataCreatorSW = Stopwatch.createUnstarted();
        for(CoreDataCreator dataCreator : this.dataCreators)
        {
            try
            {
                dataCreatorSW.start();
                dataCreator.populateDatabase();
                dataCreatorSW.stop();
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Executed '" + dataCreator.getClass().getSimpleName() + "' (" + dataCreatorSW + ")");
                }
            }
            catch(Exception e)
            {
                LOG.warn("Failed to execute '" + dataCreator.getClass().getSimpleName() + "': ", e);
            }
            finally
            {
                dataCreatorSW.reset();
            }
        }
        globalSW.stop();
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Executing Essential DataCreators took: " + globalSW);
        }
        updateExistingRestrictionsActiveFlag();
        this.mediaManager.getOrCreateRootMediaFolder();
    }


    private void updateExistingRestrictionsActiveFlag()
    {
        FlexibleSearchQuery fsQuery = new FlexibleSearchQuery("SELECT {pk} FROM {SearchRestriction} WHERE {active} IS NULL");
        SearchResult<SearchRestrictionModel> result = this.flexibleSearchService.search(fsQuery);
        for(SearchRestrictionModel searchRestriction : result.getResult())
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Activating search restriction: " + searchRestriction);
            }
            searchRestriction.setActive(Boolean.TRUE);
            this.modelService.save(searchRestriction);
        }
    }


    @Required
    public void setDataCreators(Set<CoreDataCreator> dataCreators)
    {
        this.dataCreators = dataCreators;
    }


    @Required
    public void setMediaManager(MediaManager mediaManager)
    {
        this.mediaManager = mediaManager;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
