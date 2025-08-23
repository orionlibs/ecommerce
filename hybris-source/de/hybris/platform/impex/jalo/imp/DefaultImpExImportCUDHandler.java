package de.hybris.platform.impex.jalo.imp;

import de.hybris.platform.core.Constants;
import de.hybris.platform.core.Registry;
import de.hybris.platform.hmc.jalo.SavedValues;
import de.hybris.platform.impex.ImpExImportCUDHandler;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.header.HeaderDescriptor;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloConnection;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.log4j.Logger;

public class DefaultImpExImportCUDHandler implements ImpExImportCUDHandler
{
    private static final Logger LOG = Logger.getLogger(DefaultImpExImportCUDHandler.class);
    private final ImpExImportReader reader;


    public DefaultImpExImportCUDHandler(ImpExImportReader reader)
    {
        this.reader = reader;
    }


    public void delete(Item toRemove, ValueLine valueLine) throws ConsistencyCheckException
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("removing item " + toRemove.getPK() + " for line " + valueLine);
        }
        SessionService sessionService = (SessionService)Registry.getApplicationContext().getBean("sessionService", SessionService.class);
        Map<String, Object> sessionParameters = getSessionParameters(valueLine);
        try
        {
            sessionService.executeInLocalViewWithParams(sessionParameters, (SessionExecutionBody)new Object(this, toRemove));
        }
        catch(WrappedImpExException e)
        {
            throw (ConsistencyCheckException)e.getWrappedException();
        }
        if(shouldCreateHMCSavedValues())
        {
            SavedValues savedValues = JaloConnection.getInstance().logItemRemoval(toRemove, false);
            savedValues.setModifiedItemDisplayString(getSavedValuesMessage(savedValues));
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug("removed item " + toRemove.getPK());
        }
    }


    protected void doRemove(Item toRemove) throws ConsistencyCheckException
    {
        toRemove.remove();
    }


    public void update(Item toUpdate, Map<String, Object> values, ValueLine valueLine) throws ImpExException
    {
        SessionService sessionService = (SessionService)Registry.getApplicationContext().getBean("sessionService", SessionService.class);
        Map<String, Object> sessionParameters = getItemModificationSessionParameters(toUpdate, valueLine);
        try
        {
            sessionService.executeInLocalViewWithParams(sessionParameters, (SessionExecutionBody)new Object(this, toUpdate, values, valueLine));
        }
        catch(WrappedImpExException e)
        {
            throw (ImpExException)e.getWrappedException();
        }
    }


    protected void doUpdateReadOnlyAttributes(Item toUpdate, Map<String, Object> values, ValueLine valueLine) throws ImpExException
    {
        try
        {
            ComposedType composedType = toUpdate.getComposedType();
            for(Iterator<Map.Entry<String, Object>> iter = values.entrySet().iterator(); iter.hasNext(); )
            {
                Map.Entry<String, Object> entry = iter.next();
                AttributeDescriptor attributeDescriptor = composedType.getAttributeDescriptorIncludingPrivate(entry
                                .getKey());
                if(!attributeDescriptor.isWritable())
                {
                    toUpdate.setAttribute(entry.getKey(), entry.getValue());
                    iter.remove();
                }
            }
        }
        catch(JaloInvalidParameterException e)
        {
            throw new ImpExException(e, "line " + valueLine
                            .getLineNumber() + ": cannot update " + toUpdate.getPK() + " with values " + values + " due to " + e
                            .getMessage(), 0);
        }
        catch(JaloSecurityException e)
        {
            throw new ImpExException(e, "line " + valueLine
                            .getLineNumber() + ": cannot update " + toUpdate.getPK() + " with values " + values + " due to " + e
                            .getMessage(), 0);
        }
        catch(JaloBusinessException e)
        {
            throw new ImpExException(e, "line " + valueLine
                            .getLineNumber() + ": cannot update " + toUpdate.getPK() + " with values " + values + " due to " + e
                            .getMessage(), 0);
        }
    }


    protected void doUpdate(Item toUpdate, Map<String, Object> values, ValueLine valueLine) throws ImpExException
    {
        try
        {
            toUpdate.setAllAttributes(values);
        }
        catch(JaloInvalidParameterException e)
        {
            throw new ImpExException(e, "line " + valueLine
                            .getLineNumber() + ": cannot update " + toUpdate.getPK() + " with values " + values + " due to " + e
                            .getMessage(), 0);
        }
        catch(JaloSecurityException e)
        {
            throw new ImpExException(e, "line " + valueLine
                            .getLineNumber() + ": cannot update " + toUpdate.getPK() + " with values " + values + " due to " + e
                            .getMessage(), 0);
        }
        catch(JaloBusinessException e)
        {
            throw new ImpExException(e, "line " + valueLine
                            .getLineNumber() + ": cannot update " + toUpdate.getPK() + " with values " + values + " due to " + e
                            .getMessage(), 0);
        }
    }


    private Map<String, Object> getPreviousValuesMap(Item existingItem, Map<String, Object> attributeValues)
    {
        try
        {
            return existingItem.getAllAttributes(JaloSession.getCurrentSession().getSessionContext(), (Item.AttributeFilter)new Object(this, attributeValues));
        }
        catch(Exception e)
        {
            LOG.error("could not read previous values of item " + existingItem + " and attributes " + attributeValues.keySet() + " due to " + e
                            .getMessage(), e);
            return Collections.EMPTY_MAP;
        }
    }


    public Item create(ComposedType targetType, Map<String, Object> values, ValueLine valueLine) throws ImpExException
    {
        SessionService sessionService = (SessionService)Registry.getApplicationContext().getBean("sessionService", SessionService.class);
        Map<String, Object> sessionParameters = getItemCreationSessionParameters(valueLine);
        try
        {
            return (Item)sessionService.executeInLocalViewWithParams(sessionParameters, (SessionExecutionBody)new Object(this, targetType, values, valueLine));
        }
        catch(WrappedImpExException e)
        {
            throw (ImpExException)e.getWrappedException();
        }
    }


    private Map<String, Object> getItemCreationSessionParameters(ValueLine valueLine)
    {
        Map<String, Object> parameters = new HashMap<>(getSessionParameters(valueLine));
        parameters.put("impex.creation", Boolean.TRUE);
        return parameters;
    }


    private Map<String, Object> getItemModificationSessionParameters(Item itemToUpdate, ValueLine valueLine)
    {
        Map<String, Object> parameters = new HashMap<>(getSessionParameters(valueLine));
        parameters.put("impex.modification", Boolean.TRUE);
        parameters.put("impex.modification.pk", Collections.singleton(itemToUpdate.getPK()));
        return parameters;
    }


    protected Map<String, Object> getSessionParameters(ValueLine valueLine)
    {
        HeaderDescriptor header = valueLine.getHeader();
        return header.getInterceptorRelatedParameters();
    }


    private Item createInternal(ComposedType targetType, Map<String, Object> values, ValueLine valueLine) throws ImpExException
    {
        Item ret = doCreate(targetType, values, valueLine);
        valueLine.setComposedType(targetType);
        if(shouldCreateHMCSavedValues())
        {
            SavedValues savedValues = JaloConnection.getInstance().logItemCreation(ret, values);
            savedValues.setModifiedItemDisplayString(getSavedValuesMessage(savedValues));
        }
        return ret;
    }


    protected Item doCreate(ComposedType targetType, Map<String, Object> values, ValueLine valueLine) throws ImpExException
    {
        try
        {
            return targetType.newInstance(getCreationContext(), values);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable nested = e.getCause();
            Throwable cause = (nested != null) ? nested : (Throwable)e;
            throw new ImpExException((nested != null) ? nested : e, "line " + valueLine.getLineNumber() + ": cannot create " + targetType
                            .getCode() + " with values " + values + " due to " + cause.getMessage(), 0);
        }
        catch(JaloAbstractTypeException e)
        {
            throw new ImpExException(e, "line " + valueLine.getLineNumber() + ": cannot create " + targetType.getCode() + " with values " + values + " since type was abstract", 0);
        }
    }


    private SessionContext getCreationContext()
    {
        SessionContext myCtx = JaloSession.getCurrentSession().createSessionContext();
        myCtx.setAttribute("dont.change.existing.links", Boolean.TRUE);
        myCtx.setAttribute(Constants.DISABLE_CYCLIC_CHECKS, Boolean.TRUE);
        return myCtx;
    }


    private String getSavedValuesMessage(SavedValues savedValues)
    {
        String oldMsg = savedValues.getModifiedItemDisplayString();
        return this.reader.getCurrentLocation() + this.reader.getCurrentLocation();
    }


    private boolean shouldCreateHMCSavedValues()
    {
        return this.reader.isCreateHMCSavedValues();
    }
}
