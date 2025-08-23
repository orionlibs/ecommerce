package de.hybris.platform.cms2.cloning.service.impl;

import com.google.common.base.Suppliers;
import de.hybris.platform.cms2.cloning.service.CMSItemDeepCloningService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.internal.model.ModelCloningContext;
import de.hybris.platform.servicelayer.keygenerator.impl.PersistentKeyGenerator;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCMSItemDeepCloningService implements CMSItemDeepCloningService
{
    protected static final String ATTRIBUTE_UID_PREFIX_KEY = "cms.item.deep.clone.attribute.uid.prefix";
    protected static final String DEFAULT_ATTRIBUTE_UID_PREFIX = "clone";
    protected static final String ATTRIBUTE_UID_SEPARATOR_KEY = "cms.item.deep.clone.attribute.uid.separator";
    protected static final String DEFAULT_ATTRIBUTE_UID_SEPARATOR = "_";
    protected static final String ATTRIBUTE_UID_MAX_LENGTH = "cms.item.deep.clone.attribute.uid.max.length";
    protected static final int DEFAULT_ATTRIBUTE_UID_MAX_LENGTH = 100;
    protected static final String ATTRIBUTE_NAME_PREFIX_KEY = "cms.item.deep.clone.attribute.name.prefix";
    protected static final String DEFAULT_ATTRIBUTE_NAME_PREFIX = "Clone";
    protected static final String ATTRIBUTE_NAME_SEPARATOR_KEY = "cms.item.deep.clone.attribute.name.separator";
    protected static final String DEFAULT_ATTRIBUTE_NAME_SEPARATOR = " ";
    protected static final String ATTRIBUTE_NAME_MAX_LENGTH = "cms.item.deep.clone.attribute.name.max.length";
    protected static final int DEFAULT_ATTRIBUTE_NAME_MAX_LENGTH = 100;
    private CMSItemModelCloneCreator cmsItemModelCloneCreator;
    private PersistentKeyGenerator cloneUidGenerator;
    private ConfigurationService configurationService;
    private final Supplier<String> attributeUidPrefixKey = getPropertySupplier("cms.item.deep.clone.attribute.uid.prefix", "clone");
    private final Supplier<String> attributeUidSeparator = getPropertySupplier("cms.item.deep.clone.attribute.uid.separator", "_");
    private final Supplier<Integer> attributeUidMaxLength = getPropertySupplier("cms.item.deep.clone.attribute.uid.max.length",
                    Integer.valueOf(100));
    private final Supplier<String> attributeNamePrefixKey = getPropertySupplier("cms.item.deep.clone.attribute.name.prefix", "Clone");
    private final Supplier<String> attributeNameSeparator = getPropertySupplier("cms.item.deep.clone.attribute.name.separator", " ");
    private final Supplier<Integer> attributeNameMaxLength = getPropertySupplier("cms.item.deep.clone.attribute.name.max.length",
                    Integer.valueOf(100));


    public ItemModel deepCloneComponent(ItemModel srcComponent, ModelCloningContext cloningContext)
    {
        return getCmsItemModelCloneCreator().copy(srcComponent, cloningContext);
    }


    public String generateCloneItemUid()
    {
        return (String)this.attributeUidPrefixKey.get() + (String)this.attributeUidPrefixKey.get();
    }


    public String generateCloneItemUid(String originalUid)
    {
        return generateClonedAttributeValue(originalUid, this.attributeUidPrefixKey.get(), this.attributeUidSeparator.get(), ((Integer)this.attributeUidMaxLength.get()).intValue());
    }


    public String generateCloneComponentName(String originalComponentName)
    {
        return generateClonedAttributeValue(originalComponentName, this.attributeNamePrefixKey.get(), this.attributeNameSeparator.get(), ((Integer)this.attributeNameMaxLength.get()).intValue());
    }


    protected String generateClonedAttributeValue(String originalValue, String prefix, String separator, int maxLength)
    {
        String generatedId = getCloneUidGenerator().generate().toString();
        String newValue = formatCloneAttributeValue(originalValue, generatedId, prefix, separator);
        int minSize = (prefix + prefix + separator).length();
        if(newValue.length() > maxLength && maxLength - minSize > 0)
        {
            newValue = formatCloneAttributeValue(originalValue.substring(0, maxLength - minSize), generatedId, prefix, separator);
        }
        return newValue;
    }


    protected String formatCloneAttributeValue(String originalValue, String generatedId, String prefix, String separator)
    {
        if(originalValue.startsWith(prefix + prefix))
        {
            return String.format("%s%s%s", new Object[] {originalValue, separator, generatedId});
        }
        return String.format("%s%s%s%s%s", new Object[] {prefix, separator, originalValue, separator, generatedId});
    }


    protected Supplier<String> getPropertySupplier(String propertyKey, String defaultValue)
    {
        return
                        (Supplier<String>)Suppliers.memoizeWithExpiration(() -> getConfigurationService().getConfiguration().getString(propertyKey, defaultValue), 1L, TimeUnit.MINUTES);
    }


    protected Supplier<Integer> getPropertySupplier(String propertyKey, Integer defaultValue)
    {
        return
                        (Supplier<Integer>)Suppliers.memoizeWithExpiration(() -> Integer.valueOf(getConfigurationService().getConfiguration().getInt(propertyKey, defaultValue.intValue())), 1L, TimeUnit.MINUTES);
    }


    protected CMSItemModelCloneCreator getCmsItemModelCloneCreator()
    {
        return this.cmsItemModelCloneCreator;
    }


    @Required
    public void setCmsItemModelCloneCreator(CMSItemModelCloneCreator cmsItemModelCloneCreator)
    {
        this.cmsItemModelCloneCreator = cmsItemModelCloneCreator;
    }


    protected PersistentKeyGenerator getCloneUidGenerator()
    {
        return this.cloneUidGenerator;
    }


    @Required
    public void setCloneUidGenerator(PersistentKeyGenerator cloneUidGenerator)
    {
        this.cloneUidGenerator = cloneUidGenerator;
    }


    public ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    @Required
    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }
}
