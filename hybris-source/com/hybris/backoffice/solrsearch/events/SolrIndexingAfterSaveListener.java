package com.hybris.backoffice.solrsearch.events;

import com.hybris.backoffice.ApplicationUtils;
import com.hybris.backoffice.solrsearch.services.BackofficeFacetSearchConfigService;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.servicelayer.exceptions.ModelLoadingException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.tx.AfterSaveEvent;
import de.hybris.platform.tx.AfterSaveListener;
import de.hybris.platform.util.ViewResultItem;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

@Deprecated(since = "1808", forRemoval = true)
public class SolrIndexingAfterSaveListener implements AfterSaveListener
{
    private static final Logger LOG = LoggerFactory.getLogger(SolrIndexingAfterSaveListener.class);
    private ModelService modelService;
    private TypeService typeService;
    private SessionService sessionService;
    private UserService userService;
    private BackofficeFacetSearchConfigService backofficeFacetSearchConfigService;
    private SolrIndexSynchronizationStrategy solrIndexSynchronizationStrategy;
    private Set<Integer> ignoredTypeCodes = new HashSet<>();


    public void afterSave(Collection<AfterSaveEvent> events)
    {
        try
        {
            tryAfterSave(events);
        }
        catch(RuntimeException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Unexpected exception while executing afterSave method ", e);
            }
            else
            {
                LOG.warn(e.getMessage());
            }
        }
    }


    private void tryAfterSave(Collection<AfterSaveEvent> events)
    {
        if(ApplicationUtils.isPlatformReady())
        {
            Map<SolrIndexOperation, List<AfterSaveEvent>> eventsBySolrIndexOperation = (Map<SolrIndexOperation, List<AfterSaveEvent>>)events.stream().collect(
                            Collectors.groupingBy(this::evaluateIndexOperation));
            if(eventsBySolrIndexOperation != null)
            {
                Set<PK> removedPks = (Set<PK>)((List)eventsBySolrIndexOperation.getOrDefault(SolrIndexOperation.REMOVE, Collections.emptyList())).stream().map(AfterSaveEvent::getPk).collect(Collectors.toSet());
                prepareAndHandleEvents(SolrIndexOperation.REMOVE, removedPks);
                Set<PK> changedPks = (Set<PK>)((List)eventsBySolrIndexOperation.getOrDefault(SolrIndexOperation.CHANGE, Collections.emptyList())).stream().map(AfterSaveEvent::getPk).collect(Collectors.toSet());
                changedPks.removeAll(removedPks);
                prepareAndHandleEvents(SolrIndexOperation.CHANGE, changedPks);
            }
        }
    }


    protected SolrIndexOperation evaluateIndexOperation(AfterSaveEvent e)
    {
        return ((e.getType() ^ 0x2) == 0) ? SolrIndexOperation.REMOVE : SolrIndexOperation.CHANGE;
    }


    protected void prepareAndHandleEvents(SolrIndexOperation operation, Set<PK> events)
    {
        Map<String, List<PK>> groupedByTypes = groupByTypes(events, operation);
        groupedByTypes.entrySet().stream()
                        .filter(entry -> this.backofficeFacetSearchConfigService.isSolrSearchConfiguredForType((String)entry.getKey()))
                        .forEach(group -> handleChanges(operation, (String)group.getKey(), (List<PK>)group.getValue()));
    }


    protected Map<String, List<PK>> groupByTypes(Set<PK> events, SolrIndexOperation solrIndexOperation)
    {
        return (Map<String, List<PK>>)events.stream().filter(pk -> !getIgnoredTypeCodes().contains(Integer.valueOf(pk.getTypeCode())))
                        .collect(Collectors.groupingBy(pk -> findTypeCode(solrIndexOperation, pk)));
    }


    protected String findTypeCode(SolrIndexOperation solrIndexOperation, PK pk)
    {
        String typeCode = null;
        if(solrIndexOperation != SolrIndexOperation.REMOVE)
        {
            try
            {
                Object entity = this.modelService.get(pk);
                typeCode = getType(entity);
            }
            catch(ModelLoadingException | de.hybris.platform.jalo.JaloObjectNoLongerValidException e)
            {
                LOG.debug("Trying to update nonexistant item", e);
            }
        }
        if(typeCode == null)
        {
            try
            {
                ComposedType composedType = TypeManager.getInstance().getRootComposedType(pk.getTypeCode());
                typeCode = composedType.getCode();
            }
            catch(JaloItemNotFoundException e)
            {
                LOG.debug("TypeCode lookup failed", (Throwable)e);
            }
        }
        return typeCode;
    }


    public String getType(Object object)
    {
        if(object == null)
        {
            return null;
        }
        if(object instanceof ItemModel)
        {
            return ((ItemModel)object).getItemtype();
        }
        if(object instanceof HybrisEnumValue)
        {
            return ((HybrisEnumValue)object).getType();
        }
        if(object instanceof ViewResultItem)
        {
            return ((ViewResultItem)object).getComposedType().getCode();
        }
        return object.getClass().getName();
    }


    protected void handleChange(SolrIndexOperation solrIndexOperation, String typeCode, PK pk)
    {
        switch(null.$SwitchMap$com$hybris$backoffice$solrsearch$events$SolrIndexingAfterSaveListener$SolrIndexOperation[solrIndexOperation.ordinal()])
        {
            case 1:
                handleVariantsChange(solrIndexOperation, typeCode, pk);
                this.solrIndexSynchronizationStrategy.updateItem(typeCode, pk.getLongValue());
                break;
            case 2:
                this.solrIndexSynchronizationStrategy.removeItem(typeCode, pk.getLongValue());
                break;
        }
    }


    protected void handleChanges(SolrIndexOperation solrIndexOperation, String typeCode, List<PK> pks)
    {
        switch(null.$SwitchMap$com$hybris$backoffice$solrsearch$events$SolrIndexingAfterSaveListener$SolrIndexOperation[solrIndexOperation.ordinal()])
        {
            case 1:
                pks.forEach(pk -> handleVariantsChange(solrIndexOperation, typeCode, pk));
                this.solrIndexSynchronizationStrategy.updateItems(typeCode, pks);
                break;
            case 2:
                this.solrIndexSynchronizationStrategy.removeItems(typeCode, pks);
                break;
        }
    }


    private void handleVariantsChange(SolrIndexOperation solrIndexOperation, String typeCode, PK pk)
    {
        if(this.typeService.isAssignableFrom("Product", typeCode))
        {
            getSessionService().executeInLocalView((SessionExecutionBody)new Object(this, pk, solrIndexOperation), (UserModel)
                            getUserService().getAdminUser());
        }
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public BackofficeFacetSearchConfigService getBackofficeFacetSearchConfigService()
    {
        return this.backofficeFacetSearchConfigService;
    }


    @Required
    public void setBackofficeFacetSearchConfigService(BackofficeFacetSearchConfigService backofficeFacetSearchConfigService)
    {
        this.backofficeFacetSearchConfigService = backofficeFacetSearchConfigService;
    }


    public SolrIndexSynchronizationStrategy getSolrIndexSynchronizationStrategy()
    {
        return this.solrIndexSynchronizationStrategy;
    }


    @Required
    public void setSolrIndexSynchronizationStrategy(SolrIndexSynchronizationStrategy solrIndexSynchronizationStrategy)
    {
        this.solrIndexSynchronizationStrategy = solrIndexSynchronizationStrategy;
    }


    public TypeService getTypeService()
    {
        return this.typeService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    public SessionService getSessionService()
    {
        return this.sessionService;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    public UserService getUserService()
    {
        return this.userService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    public Set<Integer> getIgnoredTypeCodes()
    {
        return this.ignoredTypeCodes;
    }


    public void setIgnoredTypeCodes(Set<Integer> ignoredTypeCodes)
    {
        this.ignoredTypeCodes = ignoredTypeCodes;
    }
}
