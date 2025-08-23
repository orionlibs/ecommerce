package de.hybris.platform.servicelayer.internal.model.impl;

import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.directpersistence.cache.SLDDataContainer;
import de.hybris.platform.directpersistence.read.RelationInformation;
import de.hybris.platform.directpersistence.read.RelationTypeNotSupportedException;
import de.hybris.platform.directpersistence.read.SLDRelationDAO;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.servicelayer.internal.converter.ReadParams;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

class SLDPersistenceObject implements PersistenceObjectInternal
{
    private final SLDDataContainer sldDataContainer;
    private final SourceTransformer sourceTransformer;
    private final SLDRelationDAO sldRelationDAO;


    public SLDPersistenceObject(SourceTransformer sourceTransformer, SLDDataContainer sldDataContainer, SLDRelationDAO sldRelationDAO)
    {
        Objects.requireNonNull(sourceTransformer, "sourceTransformer mustn't be null");
        Objects.requireNonNull(sldDataContainer, "sldDataContainer mustn't be null");
        Objects.requireNonNull(sldRelationDAO, "sldRelationDAO mustn't be null");
        this.sourceTransformer = sourceTransformer;
        this.sldDataContainer = sldDataContainer;
        this.sldRelationDAO = sldRelationDAO;
    }


    public boolean isBackedByJaloItem()
    {
        return false;
    }


    public Item getCorrespondingItem()
    {
        try
        {
            return JaloSession.getCurrentSession().getItem(getPK());
        }
        catch(JaloItemNotFoundException e)
        {
            return null;
        }
    }


    public String getTypeCode()
    {
        return this.sldDataContainer.getTypeCode();
    }


    public PK getPK()
    {
        return this.sldDataContainer.getPk();
    }


    public long getPersistenceVersion()
    {
        return this.sldDataContainer.getVersion();
    }


    public boolean isAlive()
    {
        return (this.sourceTransformer.getSLDContainerForPK(getPK()) != null);
    }


    public PersistenceObjectInternal getLatest()
    {
        SLDDataContainer latestContainer = this.sourceTransformer.getSLDContainerForPK(getPK());
        if(latestContainer == null || latestContainer == this.sldDataContainer)
        {
            return this;
        }
        return new SLDPersistenceObject(this.sourceTransformer, latestContainer, this.sldRelationDAO);
    }


    public Object readRawValue(ReadParams readParams)
    {
        Objects.requireNonNull(readParams, "readParams mustn't be null");
        PK langPK = readParams.getLangPK();
        PK currentLangPK = readParams.getCurrentLangPK();
        String qualifier = readParams.getSingleQualifier();
        if(readParams.isRelation(readParams.getSingleQualifier()))
        {
            ReadParams.RelationInfo relationInfo = readParams.getRelationInfo(qualifier);
            return getRelatedObjects(relationInfo, langPK, currentLangPK);
        }
        return transformToEmptyCollectionIfRequired(getValueFromContainer(qualifier, langPK, currentLangPK), readParams
                        .getExpectedType(qualifier));
    }


    public Map<String, Object> readRawValues(ReadParams readParams)
    {
        Objects.requireNonNull(readParams, "readParams mustn't be null");
        if(readParams.hasNoQualifiers())
        {
            return Collections.emptyMap();
        }
        PK langPK = readParams.getLangPK();
        PK currentLangPK = readParams.getCurrentLangPK();
        Set<String> qualifiersToRead = readParams.getAllQualifiers();
        HashMap<String, Object> result = new HashMap<>(qualifiersToRead.size());
        for(String qualifier : qualifiersToRead)
        {
            try
            {
                Object valueFromContainer = transformToEmptyCollectionIfRequired(
                                getValueFromContainer(qualifier, langPK, currentLangPK), readParams.getExpectedType(qualifier));
                result.put(qualifier, valueFromContainer);
            }
            catch(Exception e)
            {
                result.put(qualifier, e);
            }
        }
        return result;
    }


    public boolean isEnumerationType()
    {
        return "EnumerationType".equals(getTypeCode());
    }


    private Collection<?> getRelatedObjects(ReadParams.RelationInfo relationInfo, PK langPK, PK currentLangPK)
    {
        RelationInformation.Builder builder = RelationInformation.builder(getPK(), relationInfo.getRelationQualifier(), relationInfo
                        .isSource());
        if(relationInfo.isLocalized())
        {
            if(langPK != null)
            {
                builder.localized(langPK);
            }
            else if(currentLangPK != null)
            {
                builder.localized(currentLangPK);
            }
        }
        if(relationInfo.isOrdered())
        {
            builder.sorted();
        }
        if(relationInfo.isOneToManyRelation())
        {
            builder.oneToMany(relationInfo.isOneSide(), relationInfo.getRelationTypeName(), relationInfo.isPartoOf(), relationInfo
                            .getForeignKeyAttribute(), relationInfo.getOrderingAttribute(), relationInfo
                            .getOneToManyHandler());
        }
        if(relationInfo.isSet())
        {
            builder.returnSet();
        }
        try
        {
            Class<ItemModel> clazz;
            if(relationInfo.getRelationType() instanceof de.hybris.platform.jalo.enumeration.EnumerationType)
            {
                Class<HybrisEnumValue> clazz1 = HybrisEnumValue.class;
            }
            else
            {
                clazz = ItemModel.class;
            }
            return this.sldRelationDAO.getRelatedObjects(builder.build(), clazz);
        }
        catch(RelationTypeNotSupportedException e)
        {
            throw new CantReadValueFromPersistenceObject("Can't read object for relation " + relationInfo.getRelationQualifier(), e);
        }
    }


    private Object transformToEmptyCollectionIfRequired(Object value, Class<?> expectedType)
    {
        if(value != null || expectedType == null || !Collection.class.isAssignableFrom(expectedType))
        {
            return value;
        }
        if(Set.class.isAssignableFrom(expectedType))
        {
            return Collections.emptySet();
        }
        return Collections.emptyList();
    }


    private Object getValueFromContainer(String qualifier, PK langPK, PK currentLangPK)
    {
        SLDDataContainer.AttributeValue fallbackValue, value = this.sldDataContainer.getAttributeValue(qualifier, langPK);
        if(value != null)
        {
            return value.getValue();
        }
        if(langPK != null)
        {
            fallbackValue = this.sldDataContainer.getAttributeValue(qualifier, null);
        }
        else
        {
            fallbackValue = this.sldDataContainer.getAttributeValue(qualifier, currentLangPK);
        }
        return (fallbackValue == null) ? null : fallbackValue.getValue();
    }
}
