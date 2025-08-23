package de.hybris.platform.catalog.synchronization;

import de.hybris.platform.catalog.SynchronizationPersistenceException;
import de.hybris.platform.catalog.jalo.synchronization.GenericCatalogCopyContext;
import de.hybris.platform.catalog.jalo.synchronization.LegacySynchronizationPersistenceAdapter;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloObjectNoLongerValidException;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.servicelayer.exceptions.AttributeNotSupportedException;
import de.hybris.platform.servicelayer.exceptions.ModelLoadingException;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.jeeapi.YNoSuchEntityException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;

public class ServiceLayerSynchronizationPersistenceAdapter extends LegacySynchronizationPersistenceAdapter
{
    private static final Logger LOG = Logger.getLogger(ServiceLayerSynchronizationPersistenceAdapter.class);
    private ModelService modelService;
    private UserService userService;
    private CommonI18NService commonI18NService;
    private final SessionService sessionService;


    public ServiceLayerSynchronizationPersistenceAdapter(GenericCatalogCopyContext copyContext)
    {
        super(copyContext);
        ApplicationContext ctx = Registry.getCoreApplicationContext();
        this.modelService = (ModelService)ctx.getBean("modelService", ModelService.class);
        this.userService = (UserService)ctx.getBean("userService", UserService.class);
        this.sessionService = (SessionService)ctx.getBean("sessionService", SessionService.class);
        this.commonI18NService = (CommonI18NService)ctx.getBean("commonI18NService", CommonI18NService.class);
    }


    public Item create(ComposedType targetType, Map<String, Object> attributes) throws SynchronizationPersistenceException
    {
        try
        {
            return (Item)this.sessionService.executeInLocalView((SessionExecutionBody)new Object(this, targetType, attributes));
        }
        catch(Exception e)
        {
            throw new SynchronizationPersistenceException(e);
        }
    }


    private void updateSLContext()
    {
        if(getCopyContext() != null)
        {
            for(Map.Entry<String, Object> attr : (Iterable<Map.Entry<String, Object>>)getCopyContext().getCtx().getAttributes().entrySet())
            {
                this.sessionService.setAttribute(attr.getKey(), attr.getValue());
            }
        }
    }


    public void remove(Item item) throws SynchronizationPersistenceException
    {
        try
        {
            this.sessionService.executeInLocalView((SessionExecutionBody)new Object(this, item));
        }
        catch(ModelRemovalException mre)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Could not remove item " + item
                                .getPK() + "(" + item.getComposedType().getCode() + ") (" + item.isAlive() + ") due to " + mre
                                .getCause().getMessage() + "!!!!");
            }
            handleModelRemovalException(item, mre);
        }
        catch(Exception e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Could not remove item " + item.getPK() + "(" + item.getComposedType().getCode() + ") due to (general issue - " + e
                                .getClass() + ") " + e
                                .getMessage() + "!!!!");
            }
            handleRemovalException(item, e);
        }
    }


    public Map<String, Object> read(Item item, Set<String> attributes) throws SynchronizationPersistenceException
    {
        return readInternal(jaloOnly -> {
            CaseInsensitiveMap<String, Object> caseInsensitiveMap = new CaseInsensitiveMap();
            updateSLContext();
            ItemModel model = asModel(item);
            for(String attribute : attributes)
            {
                try
                {
                    if("europe1Taxes".equalsIgnoreCase(attribute) || "europe1Prices".equalsIgnoreCase(attribute) || "europe1Discounts".equalsIgnoreCase(attribute))
                    {
                        continue;
                    }
                    caseInsensitiveMap.put(attribute, this.modelService.toPersistenceLayer(this.modelService.getAttributeValue(model, attribute)));
                }
                catch(AttributeNotSupportedException e)
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("value of attribute '" + attribute + "' cannot be read with modelService and will be read with Item.getAttribute method");
                    }
                    jaloOnly.add(attribute);
                }
            }
            return (Map)caseInsensitiveMap;
        }jaloOnly -> super.read(item, jaloOnly));
    }


    public Map<String, Object> readLocalized(Item item, Set<String> attributes, Set<Language> languages) throws SynchronizationPersistenceException
    {
        return readInternal(jaloOnly -> {
            Item.ItemAttributeMap locSourceValues = new Item.ItemAttributeMap();
            ItemModel model = asModel(item);
            for(String q : attributes)
            {
                try
                {
                    Map<Language, Object> values = new LinkedHashMap<>(languages.size());
                    for(Language l : languages)
                    {
                        values.put(l, this.modelService.toPersistenceLayer(this.modelService.getAttributeValue(model, q, this.commonI18NService.getLocaleForLanguage((LanguageModel)this.modelService.get(l)))));
                    }
                    locSourceValues.put(q, values);
                }
                catch(AttributeNotSupportedException e)
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("value of attribute '" + q + "' cannot be read with modelService and will be read with Item.getAttribute method");
                    }
                    jaloOnly.add(q);
                }
            }
            return (Map)locSourceValues;
        }jaloOnly -> super.readLocalized(item, jaloOnly, languages));
    }


    private Map<String, Object> readInternal(Function<Set<String>, Map<String, Object>> read, Function<Set<String>, Map<String, Object>> readJaloOnly)
    {
        Set<String> jaloOnly = new HashSet<>();
        Map<String, Object> slValues = (Map<String, Object>)this.sessionService.executeInLocalView((SessionExecutionBody)new Object(this, read, jaloOnly));
        if(!jaloOnly.isEmpty())
        {
            slValues.putAll(readJaloOnly.apply(jaloOnly));
        }
        return slValues;
    }


    private void handleModelRemovalException(Item itemToRemove, ModelRemovalException exception) throws SynchronizationPersistenceException
    {
        handleRemovalException(itemToRemove, exception.getCause());
    }


    protected boolean shouldRetry(Throwable exception)
    {
        return (super.shouldRetry(exception) || isPartOfItemAlreadyRemoved(exception));
    }


    protected boolean canIgnoreItemRemovedException(Item itemToRemove, Throwable exception)
    {
        boolean canIgnore = super.canIgnoreItemRemovedException(itemToRemove, exception);
        if(canIgnore)
        {
            return true;
        }
        if(exception instanceof YNoSuchEntityException)
        {
            PK unableToRemoveItem = ((YNoSuchEntityException)exception).getPk();
            if(itemToRemove.getPK().equals(unableToRemoveItem))
            {
                return true;
            }
            LOG.warn("got YNoSuchEntityException [" + exception + "] which doesnt seem to belong to scheduled item " + itemToRemove
                            .getPK() + "(" + itemToRemove.getComposedType().getCode() + ")");
        }
        return false;
    }


    private boolean isPartOfItemAlreadyRemoved(Throwable exception)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Checking whether failure is related to dependent already removed item ...");
        }
        if(exception instanceof JaloObjectNoLongerValidException)
        {
            PK failedPK = ((JaloObjectNoLongerValidException)exception).getJaloObjectPK();
            return checkItemModelNotExists(failedPK);
        }
        if(exception instanceof YNoSuchEntityException)
        {
            return checkItemModelNotExists(((YNoSuchEntityException)exception).getPk());
        }
        return false;
    }


    private boolean checkItemModelNotExists(PK pk)
    {
        try
        {
            return ((Boolean)this.sessionService.executeInLocalView((SessionExecutionBody)new Object(this, pk))).booleanValue();
        }
        catch(ModelLoadingException | de.hybris.platform.jalo.JaloItemNotFoundException | YNoSuchEntityException | JaloObjectNoLongerValidException modelLoadingException)
        {
            return true;
        }
    }


    public void update(Item toUpdate, Map<String, Object> attributes) throws SynchronizationPersistenceException
    {
        doUpdate(toUpdate, attributes.entrySet());
    }


    public void resetUnitOfWork()
    {
        this.modelService.detachAll();
    }


    public void disableTransactions()
    {
        this.modelService.disableTransactions();
    }


    public void clearTransactionsSettings()
    {
        this.modelService.clearTransactionsSettings();
    }


    public void update(Item toUpdate, Map.Entry<String, Object> attributes) throws SynchronizationPersistenceException
    {
        doUpdate(toUpdate, Collections.singleton(attributes));
    }


    private void doUpdate(Item toUpdate, Collection<Map.Entry<String, Object>> attributes) throws SynchronizationPersistenceException
    {
        Collection<Map.Entry<String, Object>> filteredAttributes = (Collection<Map.Entry<String, Object>>)attributes.stream().filter(e -> !"europe1Taxes".equalsIgnoreCase((String)e.getKey())).filter(e -> !"europe1Prices".equalsIgnoreCase((String)e.getKey()))
                        .filter(e -> !"europe1Discounts".equalsIgnoreCase((String)e.getKey())).collect(Collectors.toSet());
        ComposedType jaloType = toUpdate.getComposedType();
        try
        {
            this.sessionService.executeInLocalView((SessionExecutionBody)new Object(this, toUpdate, jaloType, filteredAttributes));
        }
        catch(Exception e)
        {
            throw new SynchronizationPersistenceException(e);
        }
    }


    private Map<String, Object> setModelValues(ComposedType jaloType, ItemModel model, Collection<Map.Entry<String, Object>> values)
    {
        Map<String, Object> unsupportedValues = null;
        for(Map.Entry<String, Object> entry : values)
        {
            String attribute = entry.getKey();
            Object value = entry.getValue();
            try
            {
                setModelValue(jaloType, model, attribute, value);
            }
            catch(AttributeNotSupportedException e)
            {
                if(unsupportedValues == null)
                {
                    unsupportedValues = new HashMap<>();
                }
                unsupportedValues.put(attribute, value);
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Can't set " + jaloType.getCode() + "." + attribute + "=" + value + " for " + model + " , " + e
                                    .getMessage() + ". Will try Jalo layer instead.");
                }
            }
        }
        return unsupportedValues;
    }


    private void setModelValue(ComposedType jaloType, ItemModel model, String attribute, Object value)
    {
        if(jaloType.getAttributeDescriptorIncludingPrivate(attribute).isLocalized())
        {
            Map<Language, Object> map = (Map<Language, Object>)value;
            if(MapUtils.isNotEmpty(map))
            {
                setModelLocalizedValue(model, attribute, map);
            }
        }
        else
        {
            setModelValue(model, attribute, value);
        }
    }


    private void setModelValue(ItemModel model, String attribute, Object value) throws AttributeNotSupportedException
    {
        if(model instanceof de.hybris.platform.core.model.link.LinkModel && value instanceof PK)
        {
            this.modelService.setAttributeValue(model, attribute, (Map)this.modelService.get((PK)value));
        }
        else if(model instanceof UserModel && "password".equalsIgnoreCase(attribute))
        {
            LOG.warn("'password' attribute is no longer used in ServiceLayer mode. Use encodedPassword and proper encoding instead.");
            this.userService.setPassword((UserModel)model, (String)value, "*");
        }
        else
        {
            this.modelService.setAttributeValue(model, attribute, this.modelService.toModelLayer(value));
        }
    }


    private void setModelLocalizedValue(ItemModel model, String attribute, Map<Language, Object> value) throws AttributeNotSupportedException
    {
        Map<LanguageModel, Object> combinedSLValues = (Map<LanguageModel, Object>)this.modelService.toModelLayer(value);
        this.modelService.setAttributeValue(model, attribute, combinedSLValues);
    }


    private <T extends ItemModel> T asModel(Item item) throws JaloObjectNoLongerValidException
    {
        if(item instanceof de.hybris.platform.jalo.enumeration.EnumerationValue)
        {
            return (T)this.modelService.get(item, "EnumerationValue");
        }
        return (T)this.modelService.get(item);
    }


    private <T extends Item> T asJalo(ItemModel model)
    {
        return (T)this.modelService.getSource(model);
    }


    private void setNonSLValuesIfNecessary(Item toUpdate, Map<String, Object> nonServiceLayerValues) throws SynchronizationPersistenceException
    {
        if(MapUtils.isNotEmpty(nonServiceLayerValues))
        {
            super.update(toUpdate, nonServiceLayerValues);
        }
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }
}
