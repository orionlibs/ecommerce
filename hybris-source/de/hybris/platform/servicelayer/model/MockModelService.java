package de.hybris.platform.servicelayer.model;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.internal.model.ModelCloningContext;
import de.hybris.platform.servicelayer.internal.model.impl.AbstractModelService;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MockModelService extends AbstractModelService
{
    private final Map<PK, ItemModel> persistenceStore = new ConcurrentHashMap<>();


    public void refresh(Object model)
    {
        this.persistenceStore.get(((ItemModel)model).getPk());
    }


    public void remove(Object model)
    {
        this.persistenceStore.remove(((ItemModel)model).getPk());
    }


    public void remove(PK pk) throws ModelRemovalException
    {
        this.persistenceStore.remove(pk);
    }


    public boolean isUpToDate(Object model)
    {
        return true;
    }


    public void save(Object model)
    {
        this.persistenceStore.put(((ItemModel)model).getPk(), (ItemModel)model);
    }


    public void saveAll(Collection<? extends Object> models) throws ModelSavingException
    {
        for(Object o : models)
        {
            save(o);
        }
    }


    public boolean isUniqueConstraintErrorAsRootCause(Exception e)
    {
        return false;
    }


    public void attach(Object model)
    {
        throw new UnsupportedOperationException();
    }


    public void detach(Object model)
    {
        throw new UnsupportedOperationException();
    }


    public void detach(PK sourcePK)
    {
        throw new UnsupportedOperationException();
    }


    public void saveAll() throws ModelSavingException
    {
        throw new UnsupportedOperationException();
    }


    public <T> T clone(T original)
    {
        throw new UnsupportedOperationException();
    }


    public <T> T clone(Object original, Class<T> targetType)
    {
        throw new UnsupportedOperationException();
    }


    public <T> T clone(T original, ModelCloningContext ctx)
    {
        throw new UnsupportedOperationException();
    }


    public <T> T clone(Object original, Class<T> targetType, ModelCloningContext ctx)
    {
        throw new UnsupportedOperationException();
    }


    public <T> T create(Class typeAsModel)
    {
        throw new UnsupportedOperationException();
    }


    public <T> T create(String typeCode)
    {
        throw new UnsupportedOperationException();
    }


    public String getModelType(Class modelClass)
    {
        throw new UnsupportedOperationException();
    }


    public <T> T getSource(Object model)
    {
        throw new UnsupportedOperationException();
    }


    public <T> T get(Object source)
    {
        throw new UnsupportedOperationException();
    }


    public <T> T get(Object source, String conversionType)
    {
        throw new UnsupportedOperationException();
    }


    public <T> T get(PK sourcePK)
    {
        throw new UnsupportedOperationException();
    }


    public void initDefaults(Object model)
    {
        throw new UnsupportedOperationException();
    }


    public String getModelType(Object model)
    {
        throw new UnsupportedOperationException();
    }


    public <T> T getAttributeValue(Object model, String attributeQualifier)
    {
        throw new UnsupportedOperationException();
    }


    public <T> T getAttributeValue(Object model, String attributeQualifier, Locale locale)
    {
        throw new UnsupportedOperationException();
    }


    public <T> Map<Locale, T> getAttributeValues(Object model, String attributeQualifier, Locale... locales)
    {
        throw new UnsupportedOperationException();
    }


    public void setAttributeValue(Object model, String attributeQualifier, Object value)
    {
        throw new UnsupportedOperationException();
    }


    public <T> void setAttributeValue(Object model, String attributeQualifier, Map<Locale, T> values)
    {
        throw new UnsupportedOperationException();
    }


    public void detachAll()
    {
        throw new UnsupportedOperationException();
    }


    protected Object getModelForPersistentValue(Object persistentValue)
    {
        throw new UnsupportedOperationException();
    }


    protected Object getPersistentValueForModel(Object model)
    {
        throw new UnsupportedOperationException();
    }


    public boolean isModified(Object model)
    {
        return false;
    }


    public boolean isNew(Object model)
    {
        return false;
    }


    public boolean isRemoved(Object model)
    {
        return false;
    }


    public <T> T getByExample(T example)
    {
        throw new UnsupportedOperationException();
    }


    public Class getModelTypeClass(Class modelClass)
    {
        throw new UnsupportedOperationException();
    }


    public <T extends Collection> T getAll(Collection<? extends Object> sources, T result)
    {
        throw new UnsupportedOperationException();
    }


    public <T extends Collection> T getAll(Collection<? extends Object> sources, T result, String conversionType)
    {
        throw new UnsupportedOperationException();
    }


    public <T extends Collection> T getAllSources(Collection<? extends Object> models, T result)
    {
        throw new UnsupportedOperationException();
    }


    public void removeAll(Collection<? extends Object> models) throws ModelRemovalException
    {
        throw new UnsupportedOperationException();
    }


    public void removeAll(Object... models) throws ModelRemovalException
    {
        throw new UnsupportedOperationException();
    }


    public void enableTransactions()
    {
        throw new UnsupportedOperationException();
    }


    public void disableTransactions()
    {
        throw new UnsupportedOperationException();
    }


    public void clearTransactionsSettings()
    {
        throw new UnsupportedOperationException();
    }


    public void lock(PK itemPK)
    {
        throw new UnsupportedOperationException();
    }


    public void lock(Object source)
    {
        throw new UnsupportedOperationException();
    }


    public <T> T getWithLock(Object source)
    {
        throw new UnsupportedOperationException();
    }


    public boolean isAttached(Object model)
    {
        return false;
    }


    public boolean isSourceAttached(Object source)
    {
        return false;
    }
}
