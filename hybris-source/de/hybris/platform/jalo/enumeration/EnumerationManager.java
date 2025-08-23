package de.hybris.platform.jalo.enumeration;

import de.hybris.platform.cache.Cache;
import de.hybris.platform.cache.InvalidationListener;
import de.hybris.platform.cache.InvalidationManager;
import de.hybris.platform.cache.InvalidationTopic;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInternalException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.Manager;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloDuplicateCodeException;
import de.hybris.platform.persistence.EJBInvalidParameterException;
import de.hybris.platform.persistence.EJBItemNotFoundException;
import de.hybris.platform.persistence.enumeration.EnumerationManagerEJB;
import de.hybris.platform.persistence.enumeration.EnumerationValueRemote;
import de.hybris.platform.persistence.type.ComposedTypeRemote;
import de.hybris.platform.persistence.type.EJBDuplicateCodeException;
import java.io.ObjectStreamException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Deprecated(since = "ages", forRemoval = false)
public class EnumerationManager extends Manager
{
    public static final String BEAN_NAME = "core.enumerationManager";
    public static final String ENUM_META_TYPE_CODE = "EnumerationMetaType";
    private final ConcurrentMap<String, EnumerationValue> enumerationValueCache = new ConcurrentHashMap<>(100, 0.75F, 64);
    private final EnumerationManagerInvalidationListener invalidationListener = new EnumerationManagerInvalidationListener(this);


    public void init()
    {
        registerInvalidationListener();
    }


    private void registerInvalidationListener()
    {
        InvalidationTopic topic = InvalidationManager.getInstance().getInvalidationTopic((Object[])new String[] {Cache.CACHEKEY_HJMP, Cache.CACHEKEY_ENTITY});
        topic.addInvalidationListener((InvalidationListener)this.invalidationListener);
    }


    public Class getRemoteManagerClass()
    {
        return EnumerationManagerEJB.class;
    }


    public static EnumerationManager getInstance()
    {
        return Registry.getCurrentTenant().getJaloConnection().getEnumerationManager();
    }


    protected void checkBeforeItemRemoval(SessionContext ctx, Item item) throws ConsistencyCheckException
    {
        if(item instanceof EnumerationValue)
        {
            super.checkBeforeItemRemoval(ctx, item);
        }
    }


    protected void notifyItemRemoval(SessionContext ctx, Item item)
    {
    }


    @Deprecated(since = "ages", forRemoval = false)
    public EnumerationType createDefaultEnumerationType(String code) throws JaloDuplicateCodeException
    {
        return createDefaultEnumerationType(null, code);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public EnumerationType createDefaultEnumerationType(PK pk, String code) throws JaloDuplicateCodeException
    {
        return createEnumerationType(pk, code, null);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public EnumerationType createEnumerationType(String code, ComposedType valueType) throws JaloDuplicateCodeException
    {
        return createEnumerationType(null, code, valueType);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public EnumerationType createEnumerationType(PK pkBase, String code, ComposedType valueType) throws JaloDuplicateCodeException
    {
        try
        {
            ComposedTypeRemote remoteItem = (ComposedTypeRemote)extractRequiredRemoteFromItem((Item)valueType, ComposedTypeRemote.class);
            EnumerationType type = (EnumerationType)wrap(((EnumerationManagerEJB)
                            getRemote()).createEnumerationType(pkBase, code, remoteItem));
            type.setProperty(ComposedType.DYNAMIC, Boolean.TRUE);
            return type;
        }
        catch(EJBDuplicateCodeException e)
        {
            throw new JaloDuplicateCodeException(e, 0);
        }
        catch(EJBInvalidParameterException e)
        {
            throw new JaloInvalidParameterException(e, e.getErrorCode());
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public EnumerationType getEnumerationType(String code) throws JaloItemNotFoundException
    {
        ComposedType type = getSession().getTypeManager().getComposedType(code);
        if(!(type instanceof EnumerationType))
        {
            throw new JaloItemNotFoundException("a type " + type + " was found, but it is no EnumerationType instance", 0);
        }
        return (EnumerationType)type;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public EnumerationValue createEnumerationValue(EnumerationType enumerationType, String valueCode) throws JaloInvalidParameterException, ConsistencyCheckException
    {
        return createEnumerationValue(null, enumerationType, valueCode);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public EnumerationValue createEnumerationValue(String enumerationTypeCode, String valueCode) throws JaloInvalidParameterException, ConsistencyCheckException
    {
        return createEnumerationValue(null, getEnumerationType(enumerationTypeCode), valueCode);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public EnumerationValue createEnumerationValue(PK pkBase, EnumerationType enumerationType, String valueCode) throws JaloInvalidParameterException, ConsistencyCheckException
    {
        if(!enumerationType.isDefaultEnum())
        {
            throw new JaloInvalidParameterException("Enum type " + enumerationType.getCode() + "is no default enum - create values via newInstance() instead", 0);
        }
        if(!enumerationType.isDynamic())
        {
            throw new ConsistencyCheckException("Enum type " + enumerationType
                            .getCode() + " is not dynamic - can not create new enum value " + valueCode + ". If you want to add a new value to this type you have to define the enum type as non dynamic at items.xml (needs system update afterwards). ", 0);
        }
        try
        {
            ComposedTypeRemote remoteItem = (ComposedTypeRemote)extractRequiredRemoteFromItem((Item)enumerationType, ComposedTypeRemote.class);
            EnumerationValueRemote remoteResult = ((EnumerationManagerEJB)getRemote()).createEnumerationValue(pkBase, remoteItem, valueCode,
                            getNextSequenceNumber(enumerationType));
            return (EnumerationValue)wrap(remoteResult);
        }
        catch(EJBInvalidParameterException e)
        {
            throw new JaloInvalidParameterException(e, 0);
        }
    }


    protected int getNextSequenceNumber(EnumerationType type)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT MAX({").append("sequenceNumber").append("}) FROM {").append(type.getCode()).append("}");
        List<Integer> res = getSession().getFlexibleSearch().search(sb.toString(), Collections.EMPTY_MAP, Collections.singletonList(Integer.class), true, true, 0, 1).getResult();
        Integer max = res.isEmpty() ? null : res.get(0);
        return (max != null) ? (max.intValue() + 1) : 0;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public EnumerationValue getEnumerationValue(EnumerationType enumerationType, String valueCode) throws JaloInvalidParameterException, JaloItemNotFoundException
    {
        String key = enumerationType.getCode() + "__" + enumerationType.getCode();
        EnumerationValue value = this.enumerationValueCache.get(key);
        if(value == null)
        {
            try
            {
                ComposedTypeRemote remoteItem = (ComposedTypeRemote)extractRequiredRemoteFromItem((Item)enumerationType, ComposedTypeRemote.class);
                EnumerationValueRemote remoteResult = ((EnumerationManagerEJB)getRemote()).getEnumerationValue(remoteItem, valueCode);
                value = (EnumerationValue)wrap(remoteResult);
            }
            catch(EJBItemNotFoundException e)
            {
                throw new JaloItemNotFoundException(e, 0);
            }
            catch(EJBInvalidParameterException e)
            {
                throw new JaloInvalidParameterException(e, 0);
            }
            this.enumerationValueCache.putIfAbsent(key, value);
        }
        value = (EnumerationValue)value.getCacheBoundItem();
        return value;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public EnumerationValue getEnumerationValue(String enumerationTypeCode, String valueCode) throws JaloInvalidParameterException, JaloItemNotFoundException
    {
        return getEnumerationValue(getEnumerationType(enumerationTypeCode), valueCode);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public List getEnumerationValues(EnumerationType enumerationType) throws JaloInvalidParameterException
    {
        return enumerationType.getValues();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public ComposedType getEnumerationMetaType()
    {
        try
        {
            return getSession().getTypeManager().getComposedType(EnumerationType.class);
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloInternalException(e);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection getAllEnumerationTypes()
    {
        return getEnumerationMetaType().getAllInstances();
    }


    public Object writeReplace() throws ObjectStreamException
    {
        return new EnumerationManagerSerializableDTO(getTenant());
    }
}
