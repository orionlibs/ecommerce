package de.hybris.platform.catalog.jalo;

import com.google.common.base.Preconditions;
import de.hybris.platform.catalog.jalo.classification.ClassificationClass;
import de.hybris.platform.catalog.jalo.classification.ClassificationSystemVersion;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.core.PK;
import de.hybris.platform.util.Config;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public abstract class ClassificationClassesResolver<T>
{
    private final T forItem;
    private final Set<ClassificationSystemVersion> requiredVersions;


    public ClassificationClassesResolver(T forItem, Collection<ClassificationSystemVersion> inVersions)
    {
        Preconditions.checkArgument((forItem != null));
        this.forItem = forItem;
        this
                        .requiredVersions = new HashSet<>((inVersions != null && !inVersions.isEmpty()) ? inVersions : CatalogManager.getInstance().getAllClassificationSystemVersions());
    }


    public T getItem()
    {
        return this.forItem;
    }


    public Set<ClassificationSystemVersion> getRequiredVersions()
    {
        return Collections.unmodifiableSet(this.requiredVersions);
    }


    public abstract Set<Category> getSuperCategories(T paramT);


    public Set<ClassificationClass> resolve()
    {
        if(this.forItem instanceof ClassificationClass)
        {
            return Collections.singleton((ClassificationClass)this.forItem);
        }
        boolean includeOnlyClosestClasses = "closest".equalsIgnoreCase(
                        Config.getParameter("classification.resolve.classes.mode"));
        Set<PK> controlSet = null;
        Set<ClassificationClass> ret = null;
        Map<ClassificationSystemVersion, Set<ClassificationClass>> requiredVersions = new HashMap<>();
        for(ClassificationSystemVersion clVer : this.requiredVersions)
        {
            requiredVersions.put(clVer, null);
        }
        Set<Category> currentLevel = getSuperCategories(this.forItem);
        while(currentLevel != null && !currentLevel.isEmpty() && !requiredVersions.isEmpty())
        {
            Set<Category> nextLevel = null;
            for(Category cat : currentLevel)
            {
                PK pk = cat.getPK();
                if(controlSet == null)
                {
                    controlSet = new HashSet<>();
                    controlSet.add(pk);
                }
                else if(!controlSet.add(pk))
                {
                    continue;
                }
                if(cat instanceof ClassificationClass)
                {
                    ClassificationSystemVersion clVer = ((ClassificationClass)cat).getSystemVersion();
                    if(requiredVersions.containsKey(clVer))
                    {
                        if(includeOnlyClosestClasses)
                        {
                            Set<ClassificationClass> matchSet = requiredVersions.get(clVer);
                            if(matchSet == null)
                            {
                                requiredVersions.put(clVer, matchSet = new LinkedHashSet<>());
                            }
                            matchSet.add((ClassificationClass)cat);
                            continue;
                        }
                        if(ret == null)
                        {
                            ret = new LinkedHashSet<>();
                        }
                        ret.add((ClassificationClass)cat);
                    }
                    continue;
                }
                if(nextLevel == null)
                {
                    nextLevel = new LinkedHashSet<>();
                }
                nextLevel.addAll(cat.getSupercategories());
            }
            if(includeOnlyClosestClasses)
            {
                for(Iterator<Map.Entry<ClassificationSystemVersion, Set<ClassificationClass>>> it = requiredVersions.entrySet().iterator(); it.hasNext(); )
                {
                    if(ret == null)
                    {
                        ret = new LinkedHashSet<>();
                    }
                    Map.Entry<ClassificationSystemVersion, Set<ClassificationClass>> entry = it.next();
                    if(entry.getValue() != null && !((Set)entry.getValue()).isEmpty())
                    {
                        ret.addAll(entry.getValue());
                        it.remove();
                    }
                }
            }
            currentLevel = nextLevel;
        }
        return (ret != null) ? ret : Collections.EMPTY_SET;
    }
}
