package de.hybris.platform.impex.jalo.imp;

import com.google.common.collect.Iterables;
import de.hybris.platform.core.Constants;
import de.hybris.platform.core.PK;
import de.hybris.platform.directpersistence.LegacyFlagsUtils;
import de.hybris.platform.hmc.jalo.SavedValues;
import de.hybris.platform.impex.ImpExImportCUDHandler;
import de.hybris.platform.impex.enums.ImpExProcessModeEnum;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.ImpExManager;
import de.hybris.platform.impex.jalo.header.DocumentIDColumnDescriptor;
import de.hybris.platform.impex.jalo.header.HeaderDescriptor;
import de.hybris.platform.impex.jalo.header.HeaderValidationException;
import de.hybris.platform.impex.jalo.header.SpecialColumnDescriptor;
import de.hybris.platform.impex.jalo.header.StandardColumnDescriptor;
import de.hybris.platform.impex.jalo.header.UnresolvedValueException;
import de.hybris.platform.impex.jalo.translators.NotifiedSpecialValueTranslator;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.impex.impl.SLImpexImportCUDHandler;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.tx.TransactionBody;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

public class DefaultImportProcessor implements ImportProcessor
{
    private static final Logger LOG = Logger.getLogger(DefaultImportProcessor.class.getName());
    private ImpExImportReader reader = null;
    private Language defaultLanguage = null;
    private ValueLineTranslator valueLineTrans;
    private HeaderDescriptor existingItemResolverHeader;
    private ExistingItemResolver existingItemResolver;
    private ImpExImportCUDHandler cudImportProcessor;
    private ImpExImportCUDHandler forcedLegacyImportProcessor;
    private final EnumerationValue relaxedMode = ImpExManager.getImportRelaxedMode();
    private final boolean onlyRetryOnInterceptorException = Config.getBoolean("impex.retry.on.interceptorexceptions.only", false);


    public void init(ImpExImportReader reader)
    {
        this.reader = reader;
        this.defaultLanguage = JaloSession.getCurrentSession().getSessionContext().getLanguage();
        this.cudImportProcessor = createCUDHandler(reader);
        this.forcedLegacyImportProcessor = (ImpExImportCUDHandler)new DefaultImpExImportCUDHandler(reader);
    }


    protected ImpExImportCUDHandler createCUDHandler(ImpExImportReader reader)
    {
        return (ImpExImportCUDHandler)new SLImpexImportCUDHandler(reader);
    }


    public ImpExImportCUDHandler getHandlerForLine(ValueLine line)
    {
        try
        {
            enableLegacyFlagWhenLineIsUsingJaloOnlyFeatures(line);
            if(LegacyFlagsUtils.isLegacyFlagEnabled(LegacyFlagsUtils.LegacyFlag.IMPEX, Boolean.valueOf(this.reader.isLegacyMode())))
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("###Switching to legacy Import Processor ###");
                }
                return this.forcedLegacyImportProcessor;
            }
            return this.cudImportProcessor;
        }
        finally
        {
            LegacyFlagsUtils.clearLegacySetting(LegacyFlagsUtils.LegacyFlag.IMPEX);
        }
    }


    protected void enableLegacyFlagWhenLineIsUsingJaloOnlyFeatures(ValueLine line)
    {
        if(line.isUsingJaloOnlyFeatures())
        {
            LegacyFlagsUtils.enableLegacyFlag(LegacyFlagsUtils.LegacyFlag.IMPEX);
        }
    }


    public ImpExImportReader getReader()
    {
        return this.reader;
    }


    protected void adjustSessionSettings()
    {
        SessionContext ctx = JaloSession.getCurrentSession().createLocalSessionContext();
        ctx.setLanguage(null);
        ctx.setAttribute("disableRestrictions", Boolean.TRUE);
        ctx.setAttribute("disableRestrictionGroupInheritance", Boolean.TRUE);
        ctx.setAttribute("use.fast.algorithms", Boolean.TRUE);
        ctx.setAttribute("import.mode", Boolean.TRUE);
        ctx.setAttribute("disable.attribute.check", Boolean.TRUE);
    }


    protected void restoreSessionSettings()
    {
        JaloSession.getCurrentSession().removeLocalSessionContext();
    }


    public Item processItemData(ValueLine valueLine) throws ImpExException
    {
        return processItemData_Impl(valueLine);
    }


    public Item processItemData_TX(ValueLine valueLine) throws ImpExException
    {
        try
        {
            return (Item)Transaction.current().execute((TransactionBody)new Object(this, valueLine));
        }
        catch(Exception e)
        {
            if(e instanceof ImpExException)
            {
                throw (ImpExException)e;
            }
            if(e instanceof RuntimeException)
            {
                throw (RuntimeException)e;
            }
            throw new RuntimeException(e);
        }
    }


    public Item processItemData_Impl(ValueLine valueLine) throws ImpExException
    {
        try
        {
            adjustSessionSettings();
            HeaderDescriptor header = valueLine.getHeader();
            if(header == null)
            {
                valueLine.markUnresolved();
                return null;
            }
            ComposedType targetType = valueLine.getComposedType();
            assertTargetTypePermitted(header, targetType, valueLine);
            boolean singletonMode = header.getDefaultComposedType().isSingleton();
            Collection<Item> existingItems = null;
            try
            {
                existingItems = getExistingItemResolver(valueLine).findExisting(getValueLineTranslator(valueLine), valueLine);
            }
            catch(UnresolvedValueException e)
            {
                if(header.isRemoveMode())
                {
                    LOG.info("cannot find the item to remove: " + e.getMessage());
                    existingItems = Collections.EMPTY_LIST;
                    for(StandardColumnDescriptor cd : header.getUniqueAttributeColumns(valueLine.getComposedType()))
                    {
                        ValueLine.ValueEntry valueEntry = valueLine.getValueEntry(cd.getValuePosition());
                        if(valueEntry != null && valueEntry.isUnresolved())
                        {
                            valueEntry.resolve("<unresolved - ignored for removal>");
                        }
                    }
                }
                else
                {
                    valueLine.markUnresolved("error finding existing item : " + e.getMessage());
                    return null;
                }
            }
            if(header.isRemoveMode())
            {
                return processRemoveLine(existingItems, valueLine);
            }
            if((header.isUpdateMode() && !singletonMode) || (singletonMode && !existingItems.isEmpty()))
            {
                return processUpdateLine(existingItems, valueLine, targetType, singletonMode);
            }
            if(header.isInsertMode() || singletonMode)
            {
                return processInsertLine(existingItems, valueLine, targetType);
            }
            if(header.isInsertUpdateMode())
            {
                return processInsertUpdateLine(existingItems, valueLine, targetType);
            }
            throw new ImpExException("unknown mode " + header.getMode().getCode());
        }
        finally
        {
            restoreSessionSettings();
        }
    }


    protected void assertTargetTypePermitted(HeaderDescriptor header, ComposedType targetType, ValueLine valueLine) throws InsufficientDataException
    {
        if(!header.isPermittedType(targetType))
        {
            throw new InsufficientDataException(valueLine, "target type " + targetType.getCode() + " is not permitted by current header - " + header
                            .getOmittedTypesMessages(targetType.getPK()), 3);
        }
    }


    protected Item processRemoveLine(Collection<Item> existingItems, ValueLine valueLine) throws ImpExException
    {
        HeaderDescriptor header = valueLine.getHeader();
        if(existingItems.isEmpty())
        {
            valueLine.resolve((Item)null, Collections.EMPTY_LIST);
            getExistingItemResolver(valueLine).notifyItemCreatedOrRemoved(getValueLineTranslator(valueLine), valueLine);
            return null;
        }
        Item ret = null;
        int count = 0;
        for(Item i : existingItems)
        {
            ValueLine valueLine1 = valueLine;
            if(count == 0)
            {
                ret = i;
            }
            else
            {
                valueLine1 = valueLine.createCopy();
                valueLine.addHiddenLine(valueLine1);
            }
            processRemoveLine(i, valueLine1, header);
            count++;
        }
        getExistingItemResolver(valueLine).notifyItemCreatedOrRemoved(getValueLineTranslator(valueLine), valueLine);
        return ret;
    }


    private Item processUpdateLine(Collection<Item> existingItems, ValueLine valueLine, ComposedType targetType, boolean singletonMode) throws ImpExException
    {
        try
        {
            return (Item)Transaction.current().execute((TransactionBody)new Object(this, existingItems, valueLine, targetType, singletonMode));
        }
        catch(Exception e)
        {
            valueLine.clearProcessedItemPK();
            if(e instanceof ImpExException)
            {
                throw (ImpExException)e;
            }
            throw new ImpExException(e);
        }
    }


    private Item processUpdateLineInternal(Collection<Item> existingItems, ValueLine valueLine, ComposedType targetType, boolean singletonMode) throws ImpExException
    {
        HeaderDescriptor header = valueLine.getHeader();
        if(header.isInsertMode() && singletonMode)
        {
            LOG.warn("However it is INSERT mode, UPDATE mode is used, because type '" + header.getDefaultComposedType().getCode() + "' is a singleton and an instance of this type already exists.");
        }
        if(existingItems.isEmpty() && !singletonMode)
        {
            valueLine.markUnresolved("no existing item found for update");
            return null;
        }
        Item ret = null;
        for(Iterator<Item> iter = existingItems.iterator(); iter.hasNext(); )
        {
            Item item = iter.next();
            ValueLine valueLine1 = valueLine;
            if(ret == null)
            {
                ret = item;
            }
            else
            {
                valueLine1 = valueLine.createCopy();
                valueLine.addHiddenLine(valueLine1);
            }
            processUpdateLine(item, valueLine1, header, targetType);
        }
        return ret;
    }


    protected Item processInsertLine(Collection<Item> existingItems, ValueLine valueLine, ComposedType targetType) throws ImpExException
    {
        try
        {
            return (Item)Transaction.current().execute((TransactionBody)new Object(this, existingItems, valueLine, targetType));
        }
        catch(Exception e)
        {
            valueLine.clearProcessedItemPK();
            if(e instanceof ImpExException)
            {
                throw (ImpExException)e;
            }
            throw new ImpExException(e);
        }
    }


    protected Item processInsertLineInternal(Collection<Item> existingItems, ValueLine valueLine, ComposedType targetType) throws ImpExException
    {
        HeaderDescriptor header = valueLine.getHeader();
        if(!existingItems.isEmpty())
        {
            if(existingItems.size() > 1)
            {
                throw new ImpExException("batch inserts are not supported - found more than one item for value line " + valueLine);
            }
            if(this.reader.isSecondPass())
            {
                if(valueLine.isUnrecoverable())
                {
                    throw new ImpExException("unrecoverable line in second pass");
                }
                Item item = existingItems.iterator().next();
                processUpdateLine(item, valueLine, header, targetType);
                return item;
            }
            PK pk = ((Item)Iterables.get(existingItems, 0)).getPK();
            valueLine.markUnrecoverable("Cannot insert. Item exists: [" + pk + "]");
            valueLine.setConflictingItemPK(pk);
            throw new ItemConflictException((Item)existingItems.iterator().next(), valueLine);
        }
        Item ret = processInsertLine(valueLine, header, targetType);
        long time1 = System.currentTimeMillis();
        getExistingItemResolver(valueLine).notifyItemCreatedOrRemoved(getValueLineTranslator(valueLine), valueLine);
        if(LOG.isDebugEnabled())
        {
            LOG.debug("updating cache took " + System.currentTimeMillis() - time1 + "ms");
        }
        return ret;
    }


    private Item processInsertUpdateLine(Collection<Item> existingItems, ValueLine valueLine, ComposedType targetType) throws ImpExException
    {
        try
        {
            return (Item)Transaction.current().execute((TransactionBody)new Object(this, existingItems, valueLine, targetType));
        }
        catch(Exception e)
        {
            valueLine.clearProcessedItemPK();
            if(e instanceof ImpExException)
            {
                throw (ImpExException)e;
            }
            throw new ImpExException(e);
        }
    }


    private Item processInsertUpdateLineInternal(Collection<Item> existingItems, ValueLine valueLine, ComposedType targetType) throws ImpExException
    {
        HeaderDescriptor header = valueLine.getHeader();
        if(existingItems == null || existingItems.isEmpty())
        {
            Item ret = processInsertLine(valueLine, header, targetType);
            long time1 = System.currentTimeMillis();
            getExistingItemResolver(valueLine).notifyItemCreatedOrRemoved(getValueLineTranslator(valueLine), valueLine);
            if(LOG.isDebugEnabled())
            {
                LOG.debug("updating cache took " + System.currentTimeMillis() - time1 + "ms");
            }
            return ret;
        }
        if(existingItems.size() > 1)
        {
            throw new ImpExException("batch inserts are not supported - found more than one item for value line " + valueLine);
        }
        Item item = existingItems.iterator().next();
        processUpdateLine(item, valueLine, header, targetType);
        return item;
    }


    protected Item processInsertLine(ValueLine valueLine, HeaderDescriptor header, ComposedType targetType) throws ImpExException
    {
        long timeTranslate = 0L;
        long timeWrite = 0L;
        long timeStart = System.currentTimeMillis();
        if(!header.isPermittedTypeForInsert(targetType))
        {
            throw new InsufficientDataException(valueLine, "target type " + targetType.getCode() + " is not permitted by current header - " + header
                            .getOmittedTypesMessages(targetType.getPK()), 3);
        }
        if(valueLine.getTypeCode() == null && valueLine.getHeader().getConfiguredComposedType().isAbstract())
        {
            throw new ImpExException(
                            "Configured type " + valueLine.getHeader().getConfiguredComposedType().getCode() + " is abstract and you want to insert " + valueLine + ". Please specify the subtype of the item you want to create at the first column of the value line or change the configured type.");
        }
        Set<StandardColumnDescriptor> columns = new LinkedHashSet(header.getColumnsForCreation(targetType));
        Map<StandardColumnDescriptor, Object> map = getValueLineTranslator(valueLine).translateColumnValues(columns, valueLine, null);
        timeTranslate = System.currentTimeMillis() - timeStart;
        boolean tryAnyway = false;
        if(hasUnresolvedMandatoryOrInitialColumns(columns, valueLine))
        {
            if(this.reader.getValidationMode().equals(this.relaxedMode))
            {
                tryAnyway = true;
            }
            else
            {
                valueLine.markUnresolved("cannot create due to unresolved mandatory/initial columns");
                return null;
            }
        }
        try
        {
            long time1 = System.currentTimeMillis();
            Item newOne = processItemCreation(targetType, map, valueLine);
            timeWrite = System.currentTimeMillis() - time1;
            Set<? extends StandardColumnDescriptor> partOfColumns = header.getPartOfColumns(targetType);
            if(!partOfColumns.isEmpty())
            {
                time1 = System.currentTimeMillis();
                Map<StandardColumnDescriptor, Object> partOfValues = getValueLineTranslator(valueLine).translateColumnValues(partOfColumns, valueLine, newOne);
                timeTranslate = timeTranslate + System.currentTimeMillis() - time1;
                time1 = System.currentTimeMillis();
                processItemUpdate(newOne, partOfValues, valueLine);
                timeWrite = timeWrite + System.currentTimeMillis() - time1;
                columns.addAll(partOfColumns);
            }
            time1 = System.currentTimeMillis();
            for(Iterator<SpecialColumnDescriptor> iterator2 = header.getSpecificColumns(SpecialColumnDescriptor.class).iterator(); iterator2.hasNext(); )
            {
                SpecialColumnDescriptor scd = iterator2.next();
                ValueLine.ValueEntry valueEntry = valueLine.getValueEntry(scd.getValuePosition());
                scd.performImport((valueEntry != null) ? valueEntry.getCellValue() : null, newOne);
            }
            for(Iterator<SpecialColumnDescriptor> iterator1 = header.getSpecificColumns(SpecialColumnDescriptor.class).iterator(); iterator1.hasNext(); )
            {
                SpecialColumnDescriptor scd = iterator1.next();
                if(scd.getValueTranslator() instanceof NotifiedSpecialValueTranslator)
                {
                    ((NotifiedSpecialValueTranslator)scd.getValueTranslator()).notifyTranslationEnd(valueLine, header, newOne);
                }
            }
            for(Iterator<DocumentIDColumnDescriptor> iter = header.getSpecificColumns(DocumentIDColumnDescriptor.class).iterator(); iter.hasNext(); )
            {
                DocumentIDColumnDescriptor dcd = iter.next();
                ValueLine.ValueEntry valueEntry = valueLine.getValueEntry(dcd.getValuePosition());
                dcd.registerIDForItem(valueEntry.getCellValue(), newOne);
            }
            timeWrite = timeWrite + System.currentTimeMillis() - time1;
            valueLine.resolve(newOne, columns);
            if(LOG.isDebugEnabled())
            {
                LOG.debug("performed INSERT of " + newOne.getPK()
                                .toString() + " in " + System.currentTimeMillis() - timeStart + "ms (trans:" + timeTranslate + "ms, write:" + timeWrite + "ms)");
            }
            return newOne;
        }
        catch(Exception e)
        {
            if(tryAnyway)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("failed to process item creation using incomplete values - dumping for second run");
                }
                valueLine.markUnresolved("error creating item despite missing mandatory/initial columns : " + e.getMessage());
            }
            else
            {
                handleExceptionDuringImport(valueLine, e, ImpExProcessModeEnum.UPDATE, "Failed to process item creation due to ModelSavingException. Retrying in the next turn.");
            }
            return null;
        }
    }


    protected void throwImpExOrRuntimeException(Exception e) throws ImpExException
    {
        if(e instanceof ImpExException)
        {
            throw (ImpExException)e;
        }
        if(e instanceof RuntimeException)
        {
            throw (RuntimeException)e;
        }
        throw new ImpExException(e);
    }


    protected void handleExceptionDuringImport(ValueLine valueLine, Exception e, ImpExProcessModeEnum mode, String message) throws ImpExException
    {
        if(shouldRetryAfterException(e, mode))
        {
            logExceptionDuringImport(e, message);
            valueLine.markUnresolved("Exception : " + e.getMessage());
        }
        else
        {
            throwImpExOrRuntimeException(e);
        }
    }


    protected void logExceptionDuringImport(Exception e, String message)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug(message, e);
        }
        else
        {
            LOG.info(message);
        }
    }


    protected boolean shouldRetryAfterException(Exception exception, ImpExProcessModeEnum mode)
    {
        Throwable expectedModelSavingException = Utilities.getRootCauseOfType(exception, ModelSavingException.class);
        if(expectedModelSavingException != null)
        {
            if(this.onlyRetryOnInterceptorException)
            {
                return expectedModelSavingException.getCause() instanceof de.hybris.platform.servicelayer.interceptor.InterceptorException;
            }
            return true;
        }
        return false;
    }


    protected void processUpdateLine(Item item, ValueLine valueLine, HeaderDescriptor header, ComposedType targetType) throws InsufficientDataException, ImpExException
    {
        long timeTranslate = 0L;
        long timeWrite = 0L;
        long time1 = System.currentTimeMillis();
        long timeStart = time1;
        ComposedType type = item.getComposedType();
        if(!valueLine.getHeader().getConfiguredComposedType().isAssignableFrom((Type)item.getComposedType()))
        {
            throw new ImpExException("Composed type " + item.getComposedType().getCode() + " of found item with PK " + item
                            .getPK().toString() + " is not an instance of specified type " + valueLine
                            .getHeader().getConfiguredComposedType().getCode());
        }
        if(!valueLine.getHeader().isPermittedTypeForUpdate(item.getComposedType()))
        {
            throw new ImpExException("Composed type " + item.getComposedType().getCode() + " of found item with PK " + item
                            .getPK().toString() + " is not permitted in Insert_Update mode, because of: " + valueLine
                            .getHeader().getOmittedTypesMessages(item.getComposedTypePK()));
        }
        Set columns = header.getColumnsForUpdate(type);
        Map<StandardColumnDescriptor, Object> values = getValueLineTranslator(valueLine).translateColumnValues(columns, valueLine, item);
        try
        {
            time1 = System.currentTimeMillis();
            timeTranslate = time1 - timeStart;
            processItemUpdate(item, values, valueLine);
            timeWrite = System.currentTimeMillis() - time1;
            time1 = System.currentTimeMillis();
            for(Iterator<SpecialColumnDescriptor> iterator1 = header.getSpecificColumns(SpecialColumnDescriptor.class).iterator(); iterator1.hasNext(); )
            {
                SpecialColumnDescriptor scd = iterator1.next();
                ValueLine.ValueEntry valueEntry = valueLine.getValueEntry(scd.getValuePosition());
                scd.performImport((valueEntry != null) ? valueEntry.getCellValue() : null, item);
            }
            for(Iterator<SpecialColumnDescriptor> iter = header.getSpecificColumns(SpecialColumnDescriptor.class).iterator(); iter.hasNext(); )
            {
                SpecialColumnDescriptor scd = iter.next();
                if(scd.getValueTranslator() instanceof NotifiedSpecialValueTranslator)
                {
                    ((NotifiedSpecialValueTranslator)scd.getValueTranslator()).notifyTranslationEnd(valueLine, header, item);
                }
            }
            for(Iterator<DocumentIDColumnDescriptor> it = header.getSpecificColumns(DocumentIDColumnDescriptor.class).iterator(); it.hasNext(); )
            {
                DocumentIDColumnDescriptor dcd = it.next();
                ValueLine.ValueEntry valueEntry = valueLine.getValueEntry(dcd.getValuePosition());
                dcd.registerIDForItem(valueEntry.getCellValue(), item);
            }
            timeWrite = timeWrite + System.currentTimeMillis() - time1;
            valueLine.resolve(item, columns);
            if(LOG.isDebugEnabled())
            {
                LOG.debug("performed UPDATE of " + item
                                .getPK().toString() + " in " + System.currentTimeMillis() - timeStart + "ms (trans:" + timeTranslate + "ms, write:" + timeWrite + "ms)");
            }
        }
        catch(Exception e)
        {
            handleExceptionDuringImport(valueLine, e, ImpExProcessModeEnum.UPDATE, "Failed to process item update due to ModelSavingException. Retrying in the next turn.");
        }
    }


    protected void processRemoveLine(Item item, ValueLine valueLine, HeaderDescriptor header) throws ImpExException
    {
        try
        {
            processItemRemoval(item, valueLine);
            for(SpecialColumnDescriptor scd : header.getSpecificColumns(SpecialColumnDescriptor.class))
            {
                ValueLine.ValueEntry valueEntry = valueLine.getValueEntry(scd.getValuePosition());
                scd.performImport((valueEntry != null) ? valueEntry.getCellValue() : null, item);
            }
            for(SpecialColumnDescriptor scd : header.getSpecificColumns(SpecialColumnDescriptor.class))
            {
                if(scd.getValueTranslator() instanceof NotifiedSpecialValueTranslator)
                {
                    ((NotifiedSpecialValueTranslator)scd.getValueTranslator()).notifyTranslationEnd(valueLine, header, item);
                }
            }
            valueLine.resolve(item, Collections.EMPTY_LIST);
        }
        catch(ConsistencyCheckException e)
        {
            valueLine.markUnresolved("could not remove item " + item.getPK() + " due to " + e.getMessage());
        }
        catch(Exception e)
        {
            handleExceptionDuringImport(valueLine, e, ImpExProcessModeEnum.REMOVE, "Failed to process item deletion due to ModelSavingException. Retrying in the next turn.");
        }
    }


    protected boolean hasUnresolvedMandatoryOrInitialColumns(Collection<StandardColumnDescriptor> columns, ValueLine valueLine)
    {
        for(Iterator<StandardColumnDescriptor> it = columns.iterator(); it.hasNext(); )
        {
            StandardColumnDescriptor columnDescritor = it.next();
            if((columnDescritor.isMandatory() || columnDescritor.isInitialOnly()) && valueLine
                            .isUnresolved(Collections.singleton(columnDescritor)))
            {
                return true;
            }
        }
        return false;
    }


    protected String getSavedValuesMessage(SavedValues savedValues, Item existingItem, ValueLine valueLine)
    {
        String oldMsg = savedValues.getModifiedItemDisplayString();
        return this.reader.getCurrentLocation() + this.reader.getCurrentLocation();
    }


    protected void processItemRemoval(Item toRemove, ValueLine valueLine) throws ConsistencyCheckException
    {
        getHandlerForLine(valueLine).delete(toRemove, valueLine);
    }


    protected void processItemUpdate(Item toUpdate, Map<StandardColumnDescriptor, Object> attributeValueMappings, ValueLine valueLine) throws ImpExException
    {
        Map<String, Object> attributeValues = translateValueMappings(toUpdate, attributeValueMappings);
        getHandlerForLine(valueLine).update(toUpdate, attributeValues, valueLine);
    }


    protected SessionContext getCreationContext(ComposedType targetType, Map<StandardColumnDescriptor, Object> attributeValueMappings, ValueLine valueLine)
    {
        SessionContext myCtx = JaloSession.getCurrentSession().createSessionContext();
        myCtx.setAttribute("dont.change.existing.links", Boolean.TRUE);
        myCtx.setAttribute(Constants.DISABLE_CYCLIC_CHECKS, Boolean.TRUE);
        return myCtx;
    }


    protected Item processItemCreation(ComposedType targetType, Map<StandardColumnDescriptor, Object> attributeValueMappings, ValueLine valueLine) throws ImpExException
    {
        Map<String, Object> attributeValues = translateValueMappings(null, attributeValueMappings);
        return getHandlerForLine(valueLine).create(targetType, attributeValues, valueLine);
    }


    protected Map<String, Object> translateValueMappings(Item existingItem, Map<StandardColumnDescriptor, Object> attributeValueMappings) throws ImpExException
    {
        if(!attributeValueMappings.isEmpty())
        {
            Item.ItemAttributeMap valueMap = new Item.ItemAttributeMap(attributeValueMappings.size());
            for(Map.Entry<StandardColumnDescriptor, Object> e : attributeValueMappings.entrySet())
            {
                StandardColumnDescriptor columnDescriptor = e.getKey();
                if(columnDescriptor.isLocalized())
                {
                    Map<Language, Object> allLocValues = (Map<Language, Object>)valueMap.get(columnDescriptor.getQualifier());
                    if(allLocValues == null)
                    {
                        allLocValues = new HashMap<>();
                        valueMap.put(columnDescriptor.getQualifier(), allLocValues);
                    }
                    allLocValues.put(getLanguage(columnDescriptor), e.getValue());
                    continue;
                }
                valueMap.put(columnDescriptor.getQualifier(), e.getValue());
            }
            return (Map<String, Object>)valueMap;
        }
        return Collections.EMPTY_MAP;
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected boolean isDebugEnabled()
    {
        return LOG.isDebugEnabled();
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected void debug(String msg)
    {
        LOG.debug(msg);
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected boolean isInfoEnabled()
    {
        return LOG.isInfoEnabled();
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected void info(String msg)
    {
        LOG.info(msg);
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected void warn(String msg)
    {
        LOG.warn(msg);
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected void error(String msg)
    {
        LOG.error(msg);
    }


    protected Language getLanguage(StandardColumnDescriptor columnDescriptor) throws InsufficientDataException, HeaderValidationException
    {
        Language ret = columnDescriptor.getLanguage();
        if(ret == null)
        {
            if(this.defaultLanguage == null)
            {
                throw new InsufficientDataException("cannot import localized column without default session language", 0);
            }
            return this.defaultLanguage;
        }
        return ret;
    }


    protected ValueLineTranslator getValueLineTranslator(ValueLine valueLine)
    {
        if(this.valueLineTrans == null)
        {
            this.valueLineTrans = (ValueLineTranslator)new DefaultValueLineTranslator();
        }
        return this.valueLineTrans;
    }


    protected ExistingItemResolver getExistingItemResolver(ValueLine valueLine) throws HeaderValidationException
    {
        if(this.existingItemResolver == null || !this.existingItemResolverHeader.equals(valueLine.getHeader()))
        {
            this.existingItemResolverHeader = valueLine.getHeader();
            if("true".equalsIgnoreCase(this.existingItemResolverHeader.getDescriptorData().getModifier("cacheUnique")))
            {
                if(valueLine.getHeader().getDefaultComposedType().isSingleton())
                {
                    throw new HeaderValidationException("cacheUnique modifier is not allowed for Singleton types, please remove cacheUnique modifier", 131);
                }
                this.existingItemResolver = createExistingItemsResolver(this.existingItemResolverHeader, true);
            }
            else
            {
                this.existingItemResolver = createExistingItemsResolver(this.existingItemResolverHeader, false);
            }
        }
        return this.existingItemResolver;
    }


    protected ExistingItemResolver createExistingItemsResolver(HeaderDescriptor header, boolean useCache) throws HeaderValidationException
    {
        return useCache ? (ExistingItemResolver)new CachingExistingItemResolver(header) : (ExistingItemResolver)new DefaultExistingItemResolver();
    }
}
