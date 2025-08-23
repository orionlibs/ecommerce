package de.hybris.platform.servicelayer.model;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.servicelayer.StubLocaleProvider;
import de.hybris.platform.servicelayer.internal.model.attribute.DynamicAttributesProvider;
import de.hybris.platform.servicelayer.internal.model.impl.AttributeProvider;
import de.hybris.platform.servicelayer.internal.model.impl.LocaleProvider;
import de.hybris.platform.servicelayer.internal.model.impl.ModelValueHistory;
import de.hybris.platform.servicelayer.model.strategies.FetchStrategy;
import de.hybris.platform.servicelayer.model.strategies.SerializationStrategy;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ItemContextBuilder
{
    private String tenantID;
    private PK pk;
    private String itemType;
    private SerializationStrategy serializationStrategy;
    private ModelValueHistory valueHistory;
    private AttributeProvider attributeProvider;
    private LocaleProvider locProvider;
    private DynamicAttributesProvider dynamicAttributesProvider;
    private FetchStrategy fetchStrategy;


    public static ItemContextBuilder createDefaultBuilder(Class<? extends AbstractItemModel> clazz)
    {
        return (new DefaultNewModelContextFactory()).createNewBuilder(clazz);
    }


    public static ItemContextBuilder createMockContextBuilder(Class<? extends AbstractItemModel> clazz, PK pk, Locale currentLocale, Map<String, Object> attributeValues)
    {
        ItemContextBuilder builder = createDefaultBuilder(clazz);
        PK realPK = pk;
        if(pk == null)
        {
            Tenant tenant = Registry.getCurrentTenantNoFallback();
            if(tenant == null)
            {
                throw new IllegalStateException("Cannot create mock builder without explicit pk and without active tenant. Please either provide PK or ensure there is a tenant active.");
            }
            int typeCode = tenant.getPersistenceManager().getPersistenceInfo(builder.getItemType()).getItemTypeCode();
            realPK = PK.createFixedCounterPK(typeCode, System.nanoTime() / 10000L);
        }
        builder.setPk(realPK);
        builder.setLocaleProvider((LocaleProvider)new StubLocaleProvider(currentLocale));
        builder.setAttributeProvider((AttributeProvider)new Object(attributeValues));
        return builder;
    }


    public static ItemModelContext createDefaultContext(Class<? extends AbstractItemModel> clazz)
    {
        return (ItemModelContext)createDefaultBuilder(clazz).build();
    }


    public static ItemModelContext createMockContext(Class clazz, PK pk, Locale currentLocale, Object... attributesAsKeyValuesList)
    {
        return (ItemModelContext)createMockContextBuilder(clazz, pk, currentLocale, mapFromKeyValueArray(attributesAsKeyValuesList)).build();
    }


    public static ItemModelContext createMockContext(Class clazz, Map<String, Object> attributeValues)
    {
        return (ItemModelContext)createMockContextBuilder(clazz, null, Locale.getDefault(), attributeValues).build();
    }


    public static ItemModelContext createMockContext(Class clazz, Object... attributesAsKeyValuesList)
    {
        return (ItemModelContext)createMockContextBuilder(clazz, null, Locale.getDefault(),
                        mapFromKeyValueArray(attributesAsKeyValuesList)).build();
    }


    public static ItemModelContext createMockContext(Class clazz, Locale currentLocale, Map<String, Object> attributeValues)
    {
        return (ItemModelContext)createMockContextBuilder(clazz, null, currentLocale, attributeValues).build();
    }


    public static ItemModelContext createMockContext(Class clazz, Locale currentLocale, Object... attributesAsKeyValuesList)
    {
        return (ItemModelContext)createMockContextBuilder(clazz, null, currentLocale, mapFromKeyValueArray(attributesAsKeyValuesList)).build();
    }


    private static Map<String, Object> mapFromKeyValueArray(Object... attributesAsKeyValuesList)
    {
        Map<String, Object> attributes = null;
        if(attributesAsKeyValuesList != null)
        {
            attributes = new HashMap<>();
            for(int i = 0; i + 1 < attributesAsKeyValuesList.length; i += 2)
            {
                String key = (String)attributesAsKeyValuesList[i];
                Object value = attributesAsKeyValuesList[i + 1];
                attributes.put(key, value);
            }
        }
        return attributes;
    }


    public ItemModelInternalContext build()
    {
        return (ItemModelInternalContext)new ItemModelContextImpl(this);
    }


    public SerializationStrategy getSerializationStrategy()
    {
        return this.serializationStrategy;
    }


    public void setSerializationStrategy(SerializationStrategy serializationStrategy)
    {
        this.serializationStrategy = serializationStrategy;
    }


    public PK getPk()
    {
        return this.pk;
    }


    public void setPk(PK pk)
    {
        this.pk = pk;
    }


    public ModelValueHistory getValueHistory()
    {
        return this.valueHistory;
    }


    public void setValueHistory(ModelValueHistory valueHistory)
    {
        this.valueHistory = valueHistory;
    }


    public AttributeProvider getAttributeProvider()
    {
        return this.attributeProvider;
    }


    public void setAttributeProvider(AttributeProvider attributeProvider)
    {
        this.attributeProvider = attributeProvider;
    }


    public LocaleProvider getLocProvider()
    {
        return this.locProvider;
    }


    public void setLocaleProvider(LocaleProvider locProvider)
    {
        this.locProvider = locProvider;
    }


    public DynamicAttributesProvider getDynamicAttributesProvider()
    {
        return this.dynamicAttributesProvider;
    }


    public void setDynamicAttributesProvider(DynamicAttributesProvider dynamicAttributesProvider)
    {
        this.dynamicAttributesProvider = dynamicAttributesProvider;
    }


    public FetchStrategy getFetchStrategy()
    {
        return this.fetchStrategy;
    }


    public void setFetchStrategy(FetchStrategy fetchStrategy)
    {
        this.fetchStrategy = fetchStrategy;
    }


    public String getTenantID()
    {
        return this.tenantID;
    }


    public void setTenantID(String tenantID)
    {
        this.tenantID = tenantID;
    }


    public String getItemType()
    {
        return this.itemType;
    }


    public void setItemType(String itemType)
    {
        this.itemType = itemType;
    }
}
