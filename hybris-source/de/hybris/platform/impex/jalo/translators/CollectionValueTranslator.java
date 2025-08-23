package de.hybris.platform.impex.jalo.translators;

import de.hybris.platform.impex.constants.ImpExConstants;
import de.hybris.platform.impex.jalo.header.HeaderValidationException;
import de.hybris.platform.impex.jalo.header.StandardColumnDescriptor;
import de.hybris.platform.impex.jalo.util.ImpExUtils;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.util.CSVUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class CollectionValueTranslator extends AbstractValueTranslator
{
    private static final Logger LOG = Logger.getLogger(CollectionValueTranslator.class.getName());
    private final CollectionType targetType;
    private final AbstractValueTranslator elementTranslator;
    private boolean ignoreNullElements = true;
    private boolean globalAdd;
    private boolean globalRemove;
    private boolean globalMerge;
    private char collectionValueDelimiter = ',';
    private int queryChunkSize = 0;
    private StandardColumnDescriptor columnDescriptor;
    private boolean combined;


    public CollectionValueTranslator(CollectionType targetType, AbstractValueTranslator elementTranslator)
    {
        this.targetType = targetType;
        this.elementTranslator = elementTranslator;
    }


    public CollectionValueTranslator(CollectionType targetType, AbstractValueTranslator elementTranslator, char delimiter)
    {
        this(targetType, elementTranslator);
        this.collectionValueDelimiter = delimiter;
    }


    public void validate(StandardColumnDescriptor columnDescriptor) throws HeaderValidationException
    {
        super.validate(columnDescriptor);
        this.elementTranslator.validate(columnDescriptor);
    }


    public void init(StandardColumnDescriptor descriptor)
    {
        super.init(descriptor);
        this.globalAdd = false;
        this.globalRemove = false;
        this.globalMerge = false;
        this.columnDescriptor = descriptor;
        this.ignoreNullElements = !"false".equalsIgnoreCase(descriptor.getDescriptorData().getModifier("ignorenull"));
        this.elementTranslator.init(descriptor);
        String customDelimiter = descriptor.getDescriptorData().getModifier("collection-delimiter");
        if(customDelimiter != null && customDelimiter.length() > 0)
        {
            this.collectionValueDelimiter = customDelimiter.charAt(0);
        }
        String mode = descriptor.getDescriptorData().getModifier("mode");
        if(mode != null)
        {
            if(mode.trim().equalsIgnoreCase("append"))
            {
                this.globalAdd = true;
            }
            else if(mode.trim().equalsIgnoreCase("remove"))
            {
                this.globalRemove = true;
            }
            else if(mode.trim().equalsIgnoreCase("merge"))
            {
                this.globalMerge = true;
            }
        }
        this.queryChunkSize = ImpExUtils.calculateQueryChunkSize(descriptor);
        this.combined = descriptor.getHeader().getReader().isCombinedSearchEnabled();
    }


    public Object importValue(String valueExpr, Item forItem) throws JaloInvalidParameterException
    {
        clearStatus();
        if(forItem != null && (this.globalAdd || this.globalRemove || this.globalMerge || (
                        !isEmpty(valueExpr) && valueExpr.indexOf("(+)") != -1) || (
                        !isEmpty(valueExpr) && valueExpr.indexOf("(-)") != -1) || (
                        !isEmpty(valueExpr) && valueExpr.indexOf("(+?)") != -1)))
        {
            try
            {
                Collection currentValues;
                if(this.columnDescriptor.isLocalized())
                {
                    try
                    {
                        Language language = this.columnDescriptor.getLanguage();
                        SessionContext ctx = JaloSession.getCurrentSession().createSessionContext();
                        if(language != null)
                        {
                            ctx.setLanguage(language);
                        }
                        if(ctx.getLanguage() == null)
                        {
                            throw new JaloSystemException("Processed column " + this.columnDescriptor
                                            .getHeader().getTypeCode() + "." + this.columnDescriptor
                                            .getQualifier() + " is a localized collection but there is no language provided, please specify a language modifier for column");
                        }
                        currentValues = (Collection)forItem.getAttribute(ctx, this.columnDescriptor.getQualifier());
                    }
                    catch(HeaderValidationException e)
                    {
                        if(LOG.isDebugEnabled())
                        {
                            LOG.debug(e);
                        }
                        setError();
                        return null;
                    }
                }
                else
                {
                    currentValues = (Collection)forItem.getAttribute(null, this.columnDescriptor.getQualifier());
                }
                return processModifyCollection(valueExpr, forItem, currentValues);
            }
            catch(Exception e)
            {
                throw new JaloSystemException(e);
            }
        }
        Collection ret = this.targetType.newInstance();
        if(!isEmpty(valueExpr))
        {
            boolean useAllExpressionsQuery = (this.combined && getElementTranslator() instanceof ItemExpressionTranslator);
            List<String> expressions = null;
            List<ImpExConstants.ImpExActions> doAppendSet = null;
            for(Iterator<String> iter = splitAndUnescape(valueExpr).iterator(); iter.hasNext(); )
            {
                String token = iter.next();
                if(useAllExpressionsQuery)
                {
                    if(expressions == null)
                    {
                        expressions = new ArrayList<>(20);
                        doAppendSet = new ArrayList<>();
                    }
                    doAppendSet.add(ImpExConstants.ImpExActions.ADD);
                    expressions.add(token);
                    continue;
                }
                processItem(token, forItem, ret, ImpExConstants.ImpExActions.ADD, !this.ignoreNullElements);
            }
            if(useAllExpressionsQuery && expressions != null)
            {
                processItems(expressions, forItem, doAppendSet, ret, !this.ignoreNullElements);
                if(this.columnDescriptor.isLocalized() && StringUtils.isEmpty(this.columnDescriptor.getLanguageIso()))
                {
                    boolean missingOrInvalidLanguage = true;
                    try
                    {
                        missingOrInvalidLanguage = (this.columnDescriptor.getLanguage() == null);
                    }
                    catch(HeaderValidationException hve)
                    {
                        if(LOG.isDebugEnabled())
                        {
                            LOG.debug(hve);
                        }
                        setError();
                        missingOrInvalidLanguage = true;
                    }
                    if(missingOrInvalidLanguage)
                    {
                        LOG.warn("Localized collection will be modified for " + this.columnDescriptor.getHeader()
                                        .getTypeCode() + "." + this.columnDescriptor
                                        .getQualifier() + ", however language was not provided explicitly (session language used instead)");
                    }
                }
            }
        }
        else
        {
            processItem(null, forItem, ret, ImpExConstants.ImpExActions.ADD, false);
        }
        if(ret.isEmpty() && !wasUnresolved())
        {
            setEmpty();
        }
        return ret;
    }


    private Object processModifyCollection(String valueExpr, Item forItem, Collection currentValues)
    {
        Collection ret = this.targetType.newInstance();
        if(currentValues != null)
        {
            ret.addAll(currentValues);
        }
        if(!isEmpty(valueExpr))
        {
            boolean useAllExpressionsQuery = (this.combined && getElementTranslator() instanceof ItemExpressionTranslator);
            List<String> expressions = null;
            List<ImpExConstants.ImpExActions> doAppendSet = null;
            for(String token : splitAndUnescape(valueExpr))
            {
                ImpExConstants.ImpExActions doAppend;
                boolean appendPrefix = token.startsWith("(+)");
                boolean removePrefix = token.startsWith("(-)");
                boolean mergePrefix = token.startsWith("(+?)");
                if(appendPrefix || (!removePrefix && !mergePrefix && this.globalAdd))
                {
                    if(appendPrefix)
                    {
                        token = token.substring("(+)".length()).trim();
                    }
                    doAppend = ImpExConstants.ImpExActions.ADD;
                }
                else if(removePrefix || (!mergePrefix && this.globalRemove))
                {
                    if(removePrefix)
                    {
                        token = token.substring("(-)".length()).trim();
                    }
                    doAppend = ImpExConstants.ImpExActions.REMOVE;
                }
                else if(mergePrefix || this.globalMerge)
                {
                    if(mergePrefix)
                    {
                        token = token.substring("(+?)".length()).trim();
                    }
                    doAppend = ImpExConstants.ImpExActions.MERGE;
                }
                else
                {
                    throw new JaloSystemException("Invalid syntax! The assigned collectionvalues must either have one of the prefixes '(+)' or '(-)' or modifier mode must be set ");
                }
                if(useAllExpressionsQuery)
                {
                    if(expressions == null)
                    {
                        expressions = new ArrayList<>(20);
                        doAppendSet = new ArrayList<>();
                    }
                    doAppendSet.add(doAppend);
                    expressions.add(isEmpty(token) ? null : token);
                    continue;
                }
                processItem(token, forItem, ret, doAppend, !this.ignoreNullElements);
            }
            if(useAllExpressionsQuery && expressions != null)
            {
                processItems(expressions, forItem, doAppendSet, ret, !this.ignoreNullElements);
            }
        }
        else
        {
            ImpExConstants.ImpExActions processType = this.globalRemove ? ImpExConstants.ImpExActions.REMOVE : ImpExConstants.ImpExActions.ADD;
            processItem(null, forItem, ret, processType, false);
        }
        return ret;
    }


    protected boolean processItem(String token, Item forItem, Collection col, boolean append, boolean allowNull)
    {
        if(append)
        {
            return processItem(token, forItem, col, ImpExConstants.ImpExActions.ADD, allowNull);
        }
        return processItem(token, forItem, col, ImpExConstants.ImpExActions.REMOVE, allowNull);
    }


    protected boolean processItem(String token, Item forItem, Collection<Object> col, ImpExConstants.ImpExActions append, boolean allowNull)
    {
        Object element = this.elementTranslator.importValue(isEmpty(token) ? null : token, forItem);
        if(this.elementTranslator.wasUnresolved())
        {
            setError();
            return false;
        }
        if(element == null && !allowNull)
        {
            return false;
        }
        if(append == ImpExConstants.ImpExActions.ADD)
        {
            col.add(element);
        }
        else if(append == ImpExConstants.ImpExActions.REMOVE)
        {
            col.remove(element);
        }
        else if(!col.contains(element))
        {
            col.add(element);
        }
        return true;
    }


    protected void processItems(List<String> expressions, Item forItem, BitSet doAppendSet, Collection coll, boolean allowNull)
    {
        List<ImpExConstants.ImpExActions> actionsSet = new ArrayList<>();
        for(int i = 0; i < doAppendSet.length(); i++)
        {
            if(doAppendSet.get(i))
            {
                actionsSet.add(ImpExConstants.ImpExActions.ADD);
            }
            else
            {
                actionsSet.add(ImpExConstants.ImpExActions.REMOVE);
            }
        }
        processItems(expressions, forItem, actionsSet, coll, allowNull);
    }


    protected void processItems(List<String> expressions, Item forItem, List<ImpExConstants.ImpExActions> doAppendSet, Collection<Object> coll, boolean allowNull)
    {
        ItemExpressionTranslator[] tmp = new ItemExpressionTranslator[expressions.size()];
        Arrays.fill((Object[])tmp, getElementTranslator());
        List<ItemExpressionTranslator.CombinedSearchResult> results = ItemExpressionTranslator.convertAllToJalo(Arrays.asList(tmp), expressions, this.queryChunkSize);
        int size = expressions.size();
        for(int i = 0; i < size; i++)
        {
            ItemExpressionTranslator.CombinedSearchResult res = results.get(i);
            if(res.isUnresolved())
            {
                setError();
            }
            else if(res.getElement() != null || allowNull)
            {
                if(doAppendSet.get(i) == ImpExConstants.ImpExActions.ADD)
                {
                    coll.add(res.getElement());
                }
                else if(doAppendSet.get(i) == ImpExConstants.ImpExActions.REMOVE)
                {
                    coll.remove(res.getElement());
                }
                else if(!coll.contains(res.getElement()))
                {
                    coll.add(res.getElement());
                }
            }
        }
    }


    public String exportValue(Object value) throws JaloInvalidParameterException
    {
        clearStatus();
        if(value == null || ((Collection)value).isEmpty())
        {
            return "";
        }
        List<String> strings = new LinkedList();
        for(Iterator iter = ((Collection)value).iterator(); iter.hasNext(); )
        {
            strings.add(this.elementTranslator.exportValue(iter.next()));
        }
        return joinAndEscape(strings);
    }


    public AbstractValueTranslator getElementTranslator()
    {
        return this.elementTranslator;
    }


    protected boolean isEmpty(String collStr)
    {
        return (collStr == null || collStr.length() == 0);
    }


    protected String joinAndEscape(List strings)
    {
        return CSVUtils.joinAndEscape(strings, null, this.collectionValueDelimiter, false);
    }


    protected List<String> splitAndUnescape(String valueExpr)
    {
        return CSVUtils.splitAndUnescape(valueExpr, new char[] {this.collectionValueDelimiter}, false);
    }


    protected char getCollectionValueDelimiter()
    {
        return this.collectionValueDelimiter;
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected final boolean isDelimiter(String collStr, int pos, char character)
    {
        return (this.collectionValueDelimiter == character);
    }
}
