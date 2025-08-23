package de.hybris.platform.servicelayer.internal.model.impl;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.servicelayer.internal.converter.ModelConverter;
import de.hybris.platform.servicelayer.internal.converter.PersistenceObject;
import de.hybris.platform.servicelayer.internal.converter.impl.ModelModificationListener;
import de.hybris.platform.servicelayer.internal.converter.impl.UpdateableModelConverter;
import de.hybris.platform.servicelayer.internal.model.ModelContext;
import de.hybris.platform.servicelayer.internal.model.impl.wrapper.ModelWrapper;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.util.Key;
import de.hybris.platform.util.WeakValueHashMap;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DefaultModelContext implements ModelContext
{
    private final Map<Key<ModelConverter, Object>, Object> _loadedOnes = (Map<Key<ModelConverter, Object>, Object>)new WeakValueHashMap();
    private final Map<Key<ModelConverter, Object>, Object> _modifiedOnes = new HashMap<>();
    private final Set<Object> _newOnes = new HashSet();
    private final ModelModificationListener modListener = (ModelModificationListener)new Object(this);


    public boolean isAttached(Object model, ModelConverter conv)
    {
        if(conv.isNew(model))
        {
            return this._newOnes.contains(model);
        }
        if(conv.isModified(model))
        {
            return this._modifiedOnes.containsKey(Key.get(conv, unwrapKey(conv.getPersistenceSource(model))));
        }
        return this._loadedOnes.containsKey(Key.get(conv, unwrapKey(conv.getPersistenceSource(model))));
    }


    public void afterPersist(Collection<ModelWrapper> saved)
    {
        for(ModelWrapper wr : saved)
        {
            Object model = wr.getModel();
            ModelConverter conv = wr.getConverter();
            if(wr.isNew())
            {
                this._newOnes.remove(model);
                this._newOnes.remove(new NewModelIdentityProxy(model));
                Key<ModelConverter, Object> key = Key.create(conv, unwrapKey(conv.getPersistenceSource(model)));
                if(conv.isModified(model))
                {
                    this._modifiedOnes.put(key, model);
                }
                else
                {
                    this._loadedOnes.put(key, model);
                }
                conv.beforeAttach(model, this);
                if(conv instanceof UpdateableModelConverter)
                {
                    ((UpdateableModelConverter)conv).addModelModificationListener(model, this.modListener);
                }
                continue;
            }
            Key searchedKey = Key.get(conv, unwrapKey(conv.getPersistenceSource(model)));
            if(conv.isModified(model))
            {
                if(this._loadedOnes.remove(searchedKey) == model)
                {
                    Key<ModelConverter, Object> key = Key.create(conv, unwrapKey(conv.getPersistenceSource(model)));
                    this._modifiedOnes.put(key, model);
                }
                continue;
            }
            if(this._modifiedOnes.remove(searchedKey) == model)
            {
                Key<ModelConverter, Object> key = Key.create(conv, unwrapKey(conv.getPersistenceSource(model)));
                this._loadedOnes.put(key, model);
            }
        }
    }


    public void afterDirectPersist(Collection<ModelWrapper> wrappers)
    {
        for(ModelWrapper wr : wrappers)
        {
            afterDirectPersist(wr);
        }
    }


    public void afterDirectPersist(ModelWrapper wr)
    {
        Object model = wr.getModel();
        ModelConverter conv = wr.getConverter();
        if(wr.isNew())
        {
            this._newOnes.remove(model);
            this._newOnes.remove(new NewModelIdentityProxy(model));
            Key<ModelConverter, Object> key = Key.create(conv, wr.getGeneratedPk());
            if(conv.isModified(model))
            {
                this._modifiedOnes.put(key, model);
            }
            else
            {
                this._loadedOnes.put(key, model);
            }
            conv.beforeAttach(model, this);
            if(conv instanceof UpdateableModelConverter)
            {
                ((UpdateableModelConverter)conv).addModelModificationListener(model, this.modListener);
            }
        }
        else
        {
            Key searchedKey = Key.get(conv, wr.getPk());
            if(conv.isModified(model))
            {
                if(this._loadedOnes.remove(searchedKey) == model)
                {
                    Key<ModelConverter, Object> key = Key.create(conv, wr.getPk());
                    this._modifiedOnes.put(key, model);
                }
            }
            else if(this._modifiedOnes.remove(searchedKey) == model)
            {
                Key<ModelConverter, Object> key = Key.create(conv, wr.getPk());
                this._loadedOnes.put(key, model);
            }
        }
    }


    protected void modificationStatusChanged(Object model, ModelConverter conv)
    {
        Key searchedKey = Key.get(conv, unwrapKey(conv.getPersistenceSource(model)));
        boolean modified = conv.isModified(model);
        if(modified)
        {
            Object loadedCache = this._loadedOnes.remove(searchedKey);
            if(loadedCache != null)
            {
                Key<ModelConverter, Object> key = Key.create(conv, unwrapKey(conv.getPersistenceSource(model)));
                this._modifiedOnes.put(key, model);
            }
        }
        else
        {
            Object modifiedCache = this._modifiedOnes.remove(searchedKey);
            if(modifiedCache != null)
            {
                Key<ModelConverter, Object> key = Key.create(conv, unwrapKey(conv.getPersistenceSource(model)));
                this._loadedOnes.put(key, model);
            }
        }
    }


    public String getStats()
    {
        return "loaded:" + this._loadedOnes.size() + ", _modified:" + this._modifiedOnes.size() + ", new:" + this._newOnes.size();
    }


    public void attach(Object model, Object source, ModelConverter conv)
    {
        ServicesUtil.validateParameterNotNull(model, "Parameter 'model' was null!");
        if(source != null)
        {
            Object prev;
            boolean modified = conv.isModified(model);
            Key<ModelConverter, Object> key = Key.create(conv, unwrapKey(source));
            if(modified)
            {
                prev = this._modifiedOnes.put(key, model);
            }
            else
            {
                prev = this._loadedOnes.put(key, model);
            }
            if(prev != null && !prev.equals(model))
            {
                throw new IllegalStateException("already got " + prev + " attached by " + source + " - cannot attach as " + model);
            }
            if(conv instanceof UpdateableModelConverter)
            {
                ((UpdateableModelConverter)conv).addModelModificationListener(model, this.modListener);
            }
            if(!this._newOnes.remove(model))
            {
                this._newOnes.remove(new NewModelIdentityProxy(model));
            }
        }
        else
        {
            this._newOnes.add(model);
        }
    }


    @Deprecated(since = "ages", forRemoval = true)
    protected Object convertKey(Object key)
    {
        return unwrapKey(key);
    }


    protected Object unwrapKey(Object key)
    {
        if(key instanceof Item)
        {
            return ((Item)key).getPK();
        }
        if(key instanceof PersistenceObject)
        {
            return unwrapKey((PersistenceObject)key);
        }
        return key;
    }


    private Object unwrapKey(PersistenceObject persistenceObject)
    {
        return persistenceObject.getPK();
    }


    public void detach(Object model, Object source, ModelConverter conv)
    {
        ServicesUtil.validateParameterNotNull(model, "Parameter 'model' was null!");
        this._newOnes.remove(model);
        if(source != null)
        {
            Key key = Key.get(conv, unwrapKey(source));
            if(this._loadedOnes.remove(key) == null)
            {
                this._modifiedOnes.remove(key);
            }
        }
        if(conv instanceof UpdateableModelConverter)
        {
            ((UpdateableModelConverter)conv).removeModelModificationListener(model, this.modListener);
        }
    }


    public Object getAttached(Object source, ModelConverter conv)
    {
        Key key = Key.get(conv, unwrapKey(source));
        Object ret = this._loadedOnes.get(key);
        return (ret != null) ? ret : this._modifiedOnes.get(key);
    }


    public void clear()
    {
        this._loadedOnes.clear();
        this._newOnes.clear();
        this._modifiedOnes.clear();
    }


    public Set<Object> getNew()
    {
        return this._newOnes.isEmpty() ? Collections.EMPTY_SET : new HashSet(this._newOnes);
    }


    public Set<Object> getModified()
    {
        return this._modifiedOnes.isEmpty() ? Collections.EMPTY_SET : new HashSet(this._modifiedOnes.values());
    }
}
