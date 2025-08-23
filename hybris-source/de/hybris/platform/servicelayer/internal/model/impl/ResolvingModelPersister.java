package de.hybris.platform.servicelayer.internal.model.impl;

import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.internal.model.ModelPersister;
import de.hybris.platform.servicelayer.internal.model.impl.wrapper.ModelWrapper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.collections.CollectionUtils;

public class ResolvingModelPersister implements ModelPersister
{
    public Collection<ModelWrapper> persist(Collection<ModelWrapper> wrappers) throws ModelSavingException
    {
        if(!wrappers.isEmpty())
        {
            Set<ModelWrapper> done = new LinkedHashSet<>((int)(wrappers.size() / 0.75D) + 1);
            Collection<ModelWrapper> allOther = getAllOther(wrappers);
            Collection<ModelWrapper> allNew = CollectionUtils.subtract(wrappers, allOther);
            saveNewOnes(new ArrayList<>(allNew), done);
            saveOthers(getAllOther(wrappers), done);
            return done;
        }
        return Collections.EMPTY_SET;
    }


    protected void saveOthers(Collection<ModelWrapper> others, Set<ModelWrapper> done)
    {
        for(ModelWrapper wr : others)
        {
            wr.save(done, false);
            done.add(wr);
        }
    }


    protected void saveNewOnes(List<ModelWrapper> newOnes, Set<ModelWrapper> done)
    {
        boolean updated;
        do
        {
            updated = false;
            for(ListIterator<ModelWrapper> it = newOnes.listIterator(); it.hasNext(); )
            {
                ModelWrapper wr = it.next();
                if(wr.save(done, false))
                {
                    done.add(wr);
                    it.remove();
                    updated = true;
                }
            }
        }
        while(!newOnes.isEmpty() && updated);
        if(!newOnes.isEmpty())
        {
            List<ModelWrapper> partly = new ArrayList<>(newOnes.size());
            do
            {
                updated = false;
                ListIterator<ModelWrapper> it;
                for(it = newOnes.listIterator(); it.hasNext(); )
                {
                    ModelWrapper wr = it.next();
                    if(wr.save(done, true))
                    {
                        done.add(wr);
                        partly.add(wr);
                        it.remove();
                        updated = true;
                    }
                }
                for(it = partly.listIterator(); it.hasNext(); )
                {
                    ModelWrapper wr = it.next();
                    if(wr.save(done, true))
                    {
                        done.add(wr);
                        if(!wr.getConverter().isModified(wr.getModel()))
                        {
                            it.remove();
                            updated = true;
                        }
                    }
                }
            }
            while((!newOnes.isEmpty() || !partly.isEmpty()) && updated);
            if(!newOnes.isEmpty() || !partly.isEmpty())
            {
                List<Map<String, ?>> dirtyAttributes = getDirtyAttributesReport(newOnes, partly);
                throw new ModelSavingException("dependency error saving models - not created = " + newOnes + " , partial = " + partly + ", dirty attributes: " + dirtyAttributes);
            }
        }
    }


    List<Map<String, ?>> getDirtyAttributesReport(List<ModelWrapper> newOnes, List<ModelWrapper> partly)
    {
        Stream<ModelWrapper> wrappers = Stream.concat(newOnes.stream(), partly.stream());
        return (List<Map<String, ?>>)wrappers.map(wrapper -> wrapper.getConverter().getDirtyAttributes(wrapper.getModel()))
                        .collect(Collectors.toList());
    }


    protected Collection<ModelWrapper> getAllOther(Collection<ModelWrapper> wrappers)
    {
        Collection<ModelWrapper> ret = null;
        for(ModelWrapper wr : wrappers)
        {
            if(!wr.isNew())
            {
                if(ret == null)
                {
                    ret = new ArrayList<>(wrappers.size());
                }
                ret.add(wr);
            }
        }
        return (ret != null) ? ret : Collections.EMPTY_LIST;
    }
}
