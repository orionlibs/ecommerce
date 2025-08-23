package de.hybris.platform.adaptivesearch.services.impl;

import de.hybris.platform.adaptivesearch.daos.AsConfigurationDao;
import de.hybris.platform.adaptivesearch.data.AsRankChange;
import de.hybris.platform.adaptivesearch.data.AsRankChangeType;
import de.hybris.platform.adaptivesearch.model.AbstractAsConfigurationModel;
import de.hybris.platform.adaptivesearch.services.AsConfigurationService;
import de.hybris.platform.adaptivesearch.strategies.AsCloneStrategy;
import de.hybris.platform.adaptivesearch.strategies.AsValidationStrategy;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.CollectionTypeModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.exceptions.AttributeNotSupportedException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultAsConfigurationService implements AsConfigurationService
{
    protected static final String CONFIGURATION_PARAM = "configuration";
    protected static final String TYPE_PARAM = "type";
    protected static final String PARENT_CONFIGURATION_PARAM = "parentConfiguration";
    protected static final String SOURCE_ATTRIBUTE_PARAM = "sourceAttribute";
    protected static final String TARGET_ATTRIBUTE_PARAM = "targetAttribute";
    protected static final String UID_PARAM = "uid";
    protected static final String UIDS_PARAM = "uids";
    private TypeService typeService;
    private ModelService modelService;
    private AsConfigurationDao asConfigurationDao;
    private AsCloneStrategy asCloneStrategy;
    private AsValidationStrategy asValidationStrategy;


    public <T extends AbstractAsConfigurationModel> Optional<T> getConfigurationForUid(Class<T> type, CatalogVersionModel catalogVersion, String uid)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("type", type);
        ServicesUtil.validateParameterNotNullStandardMessage("uid", uid);
        return this.asConfigurationDao.findConfigurationByUid(type, catalogVersion, uid);
    }


    public void refreshConfiguration(AbstractAsConfigurationModel configuration)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("configuration", configuration);
        this.modelService.refresh(configuration);
    }


    public <T extends AbstractAsConfigurationModel> T createConfiguration(Class<T> type)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("type", type);
        return (T)this.modelService.create(type);
    }


    public <T extends AbstractAsConfigurationModel> T cloneConfiguration(T configuration)
    {
        return (T)this.asCloneStrategy.clone((ItemModel)configuration);
    }


    public void saveConfiguration(AbstractAsConfigurationModel configuration)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("configuration", configuration);
        this.modelService.save(configuration);
    }


    public void removeConfiguration(AbstractAsConfigurationModel configuration)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("configuration", configuration);
        this.modelService.remove(configuration);
    }


    public boolean isValid(AbstractAsConfigurationModel configuration)
    {
        return this.asValidationStrategy.isValid((ItemModel)configuration);
    }


    public boolean moveConfiguration(AbstractAsConfigurationModel parentConfiguration, String sourceAttribute, String targetAttribute, String uid)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("parentConfiguration", parentConfiguration);
        ServicesUtil.validateParameterNotNullStandardMessage("sourceAttribute", sourceAttribute);
        ServicesUtil.validateParameterNotNullStandardMessage("targetAttribute", targetAttribute);
        ServicesUtil.validateParameterNotNullStandardMessage("uid", uid);
        findAndCheckConfigurationType(parentConfiguration, sourceAttribute);
        Class<? extends AbstractAsConfigurationModel> targetType = findAndCheckConfigurationType(parentConfiguration, targetAttribute);
        Optional<AbstractAsConfigurationModel> configurationOptional = findConfiguration(parentConfiguration, sourceAttribute, uid);
        if(configurationOptional.isEmpty())
        {
            return false;
        }
        AbstractAsConfigurationModel configuration = configurationOptional.get();
        AbstractAsConfigurationModel newConfiguration = (AbstractAsConfigurationModel)this.modelService.clone(configuration, targetType);
        this.modelService.remove(configuration);
        this.modelService.save(newConfiguration);
        this.modelService.refresh(parentConfiguration);
        return true;
    }


    public List<AsRankChange> rankBeforeConfiguration(AbstractAsConfigurationModel parentConfiguration, String attribute, String rankBeforeUid, String... uids)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("parentConfiguration", parentConfiguration);
        ServicesUtil.validateParameterNotNullStandardMessage("sourceAttribute", attribute);
        ServicesUtil.validateParameterNotNullStandardMessage("uids", uids);
        if(ArrayUtils.isEmpty((Object[])uids))
        {
            return Collections.emptyList();
        }
        findAndCheckConfigurationType(parentConfiguration, attribute);
        List<AbstractAsConfigurationModel> oldConfigurations = (List<AbstractAsConfigurationModel>)parentConfiguration.getProperty(attribute);
        ConfigurationInfo<AbstractAsConfigurationModel> rankBeforeConfigurationInfo = buildConfigurationInfo(oldConfigurations, rankBeforeUid);
        if(StringUtils.isNotBlank(rankBeforeUid) && !rankBeforeConfigurationInfo.isPresent())
        {
            return (List<AsRankChange>)Arrays.<String>stream(uids).map(this::createNoOperationRankChange).collect(Collectors.toList());
        }
        List<ConfigurationInfo<AbstractAsConfigurationModel>> configurationInfos = buildConfigurationInfos(oldConfigurations, uids);
        int index = calculateRankBeforeIndex(oldConfigurations, rankBeforeConfigurationInfo, configurationInfos);
        int changeIndex = index;
        List<AsRankChange> rankChanges = new ArrayList<>();
        List<AbstractAsConfigurationModel> configurations = new ArrayList<>();
        for(ConfigurationInfo<AbstractAsConfigurationModel> configurationInfo : configurationInfos)
        {
            if(configurationInfo.getConfiguration() != null)
            {
                int oldRank = configurationInfo.getRank();
                int newRank = changeIndex;
                rankChanges.add(createMoveRankChange(configurationInfo.getUid(), oldRank, newRank));
                configurations.add(configurationInfo.getConfiguration());
                changeIndex++;
                continue;
            }
            rankChanges.add(createNoOperationRankChange(configurationInfo.getUid()));
        }
        rerankConfigurations(parentConfiguration, attribute, index, configurations);
        return rankChanges;
    }


    protected int calculateRankBeforeIndex(List<AbstractAsConfigurationModel> oldConfigurations, ConfigurationInfo<AbstractAsConfigurationModel> rankBeforeConfigurationInfo, List<ConfigurationInfo<AbstractAsConfigurationModel>> configurationInfos)
    {
        int newRank;
        if(rankBeforeConfigurationInfo.isPresent())
        {
            newRank = rankBeforeConfigurationInfo.getRank();
        }
        else
        {
            newRank = oldConfigurations.isEmpty() ? 0 : oldConfigurations.size();
        }
        for(ConfigurationInfo<AbstractAsConfigurationModel> configurationInfo : configurationInfos)
        {
            if(configurationInfo.isPresent() && configurationInfo.getRank() < newRank)
            {
                newRank--;
            }
        }
        return newRank;
    }


    public List<AsRankChange> rankAfterConfiguration(AbstractAsConfigurationModel parentConfiguration, String attribute, String rankAfterUid, String... uids)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("parentConfiguration", parentConfiguration);
        ServicesUtil.validateParameterNotNullStandardMessage("sourceAttribute", attribute);
        ServicesUtil.validateParameterNotNullStandardMessage("uids", uids);
        if(ArrayUtils.isEmpty((Object[])uids))
        {
            return Collections.emptyList();
        }
        findAndCheckConfigurationType(parentConfiguration, attribute);
        List<AbstractAsConfigurationModel> oldConfigurations = (List<AbstractAsConfigurationModel>)parentConfiguration.getProperty(attribute);
        ConfigurationInfo<AbstractAsConfigurationModel> rankAfterConfigurationInfo = buildConfigurationInfo(oldConfigurations, rankAfterUid);
        if(StringUtils.isNotBlank(rankAfterUid) && !rankAfterConfigurationInfo.isPresent())
        {
            return (List<AsRankChange>)Arrays.<String>stream(uids).map(this::createNoOperationRankChange).collect(Collectors.toList());
        }
        List<ConfigurationInfo<AbstractAsConfigurationModel>> configurationInfos = buildConfigurationInfos(oldConfigurations, uids);
        int index = calculateRankAfterIndex(rankAfterConfigurationInfo, configurationInfos);
        int changeIndex = index;
        List<AsRankChange> rankChanges = new ArrayList<>();
        List<AbstractAsConfigurationModel> configurations = new ArrayList<>();
        for(ConfigurationInfo<AbstractAsConfigurationModel> configurationInfo : configurationInfos)
        {
            if(configurationInfo.getConfiguration() != null)
            {
                int oldRank = configurationInfo.getRank();
                int newRank = changeIndex;
                rankChanges.add(createMoveRankChange(configurationInfo.getUid(), oldRank, newRank));
                configurations.add(configurationInfo.getConfiguration());
                changeIndex++;
                continue;
            }
            rankChanges.add(createNoOperationRankChange(configurationInfo.getUid()));
        }
        rerankConfigurations(parentConfiguration, attribute, index, configurations);
        return rankChanges;
    }


    protected int calculateRankAfterIndex(ConfigurationInfo<AbstractAsConfigurationModel> rankBeforeConfigurationInfo, List<ConfigurationInfo<AbstractAsConfigurationModel>> configurationInfos)
    {
        int newRank;
        if(rankBeforeConfigurationInfo.isPresent())
        {
            newRank = rankBeforeConfigurationInfo.getRank() + 1;
        }
        else
        {
            newRank = 0;
        }
        for(ConfigurationInfo<AbstractAsConfigurationModel> configurationInfo : configurationInfos)
        {
            if(configurationInfo.isPresent() && configurationInfo.getRank() < newRank)
            {
                newRank--;
            }
        }
        return newRank;
    }


    public AsRankChange rerankConfiguration(AbstractAsConfigurationModel parentConfiguration, String attribute, String uid, int change)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("parentConfiguration", parentConfiguration);
        ServicesUtil.validateParameterNotNullStandardMessage("sourceAttribute", attribute);
        ServicesUtil.validateParameterNotNullStandardMessage("uid", uid);
        findAndCheckConfigurationType(parentConfiguration, attribute);
        List<AbstractAsConfigurationModel> configurations = (List<AbstractAsConfigurationModel>)parentConfiguration.getProperty(attribute);
        if(change == 0 || CollectionUtils.isEmpty(configurations))
        {
            return createNoOperationRankChange(uid);
        }
        ConfigurationInfo<AbstractAsConfigurationModel> configurationInfo = buildConfigurationInfo(configurations, uid);
        if(!configurationInfo.isPresent())
        {
            return createNoOperationRankChange(uid);
        }
        int oldRank = configurationInfo.getRank();
        int newRank = oldRank + change;
        if(newRank < 0 || newRank >= configurations.size())
        {
            return createNoOperationRankChange(uid);
        }
        rerankConfigurations(parentConfiguration, attribute, newRank,
                        Collections.singletonList(configurationInfo.getConfiguration()));
        return createMoveRankChange(configurationInfo.getUid(), oldRank, newRank);
    }


    protected <T extends AbstractAsConfigurationModel> Class<T> findAndCheckConfigurationType(AbstractAsConfigurationModel parentConfiguration, String attribute)
    {
        ComposedTypeModel parentType = this.typeService.getComposedTypeForClass(parentConfiguration.getClass());
        try
        {
            AttributeDescriptorModel attributeDescriptor = this.typeService.getAttributeDescriptor(parentType, attribute);
            if(!(attributeDescriptor.getAttributeType() instanceof CollectionTypeModel))
            {
                throw new AttributeNotSupportedException("cannot perform operation on attribute" + attribute, attribute);
            }
            CollectionTypeModel attributeType = (CollectionTypeModel)attributeDescriptor.getAttributeType();
            Class<?> configurationType = this.typeService.getModelClass(attributeType.getElementType().getCode());
            if(!AbstractAsConfigurationModel.class.isAssignableFrom(configurationType))
            {
                throw new AttributeNotSupportedException("cannot perform operation on attribute" + attribute, attribute);
            }
            return (Class)configurationType;
        }
        catch(UnknownIdentifierException e)
        {
            throw new AttributeNotSupportedException(e.getMessage(), e, attribute);
        }
    }


    protected <T extends AbstractAsConfigurationModel> Optional<T> findConfiguration(AbstractAsConfigurationModel parentConfiguration, String attribute, String uid)
    {
        if(StringUtils.isBlank(uid))
        {
            return Optional.empty();
        }
        List<T> configurations = (List<T>)parentConfiguration.getProperty(attribute);
        for(AbstractAsConfigurationModel abstractAsConfigurationModel : configurations)
        {
            if(configurationnMatches(abstractAsConfigurationModel, uid))
            {
                return Optional.of((T)abstractAsConfigurationModel);
            }
        }
        return Optional.empty();
    }


    protected boolean configurationnMatches(AbstractAsConfigurationModel configuration, String uid)
    {
        return (StringUtils.isNotBlank(uid) && Objects.equals(configuration.getUid(), uid));
    }


    protected <T extends AbstractAsConfigurationModel> ConfigurationInfo<T> buildConfigurationInfo(List<T> configurations, String uid)
    {
        if(StringUtils.isBlank(uid))
        {
            return new ConfigurationInfo(uid);
        }
        int index = 0;
        for(AbstractAsConfigurationModel abstractAsConfigurationModel : configurations)
        {
            if(configurationnMatches(abstractAsConfigurationModel, uid))
            {
                return new ConfigurationInfo(uid, abstractAsConfigurationModel, index);
            }
            index++;
        }
        return new ConfigurationInfo(uid);
    }


    protected <T extends AbstractAsConfigurationModel> List<ConfigurationInfo<T>> buildConfigurationInfos(List<T> configurations, String... uids)
    {
        return (List<ConfigurationInfo<T>>)Arrays.<String>stream(uids).map(uid -> buildConfigurationInfo(configurations, uid)).collect(Collectors.toList());
    }


    protected <T extends AbstractAsConfigurationModel> void rerankConfigurations(AbstractAsConfigurationModel parentConfiguration, String attribute, int newIndex, List<T> configurations)
    {
        if(CollectionUtils.isNotEmpty(configurations))
        {
            List<T> oldConfigurations = (List<T>)parentConfiguration.getProperty(attribute);
            List<T> newConfigurations = new ArrayList<>(oldConfigurations);
            newConfigurations.removeAll(configurations);
            newConfigurations.addAll(newIndex, configurations);
            parentConfiguration.setProperty(attribute, newConfigurations);
            this.modelService.save(parentConfiguration);
        }
    }


    protected AsRankChange createNoOperationRankChange(String uid)
    {
        AsRankChange rankChange = new AsRankChange();
        rankChange.setType(AsRankChangeType.NO_OPERATION);
        rankChange.setUid(uid);
        return rankChange;
    }


    protected AsRankChange createMoveRankChange(String uid, int oldRank, int newRank)
    {
        AsRankChange rankChange = new AsRankChange();
        rankChange.setType(AsRankChangeType.MOVE);
        rankChange.setUid(uid);
        rankChange.setOldRank(Integer.valueOf(oldRank));
        rankChange.setNewRank(Integer.valueOf(newRank));
        return rankChange;
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


    public ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public AsConfigurationDao getAsConfigurationDao()
    {
        return this.asConfigurationDao;
    }


    @Required
    public void setAsConfigurationDao(AsConfigurationDao asConfigurationDao)
    {
        this.asConfigurationDao = asConfigurationDao;
    }


    public AsCloneStrategy getAsCloneStrategy()
    {
        return this.asCloneStrategy;
    }


    @Required
    public void setAsCloneStrategy(AsCloneStrategy asCloneStrategy)
    {
        this.asCloneStrategy = asCloneStrategy;
    }


    public AsValidationStrategy getAsValidationStrategy()
    {
        return this.asValidationStrategy;
    }


    @Required
    public void setAsValidationStrategy(AsValidationStrategy asValidationStrategy)
    {
        this.asValidationStrategy = asValidationStrategy;
    }
}
