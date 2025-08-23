package de.hybris.platform.webservicescommons.jaxb;

import com.google.common.collect.Sets;
import de.hybris.platform.webservicescommons.jaxb.metadata.MetadataSourceFactory;
import de.hybris.platform.webservicescommons.jaxb.util.ReflectionUtils;
import de.hybris.platform.webservicescommons.mapping.SubclassRegistry;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import org.eclipse.persistence.jaxb.JAXBContextFactory;
import org.eclipse.persistence.jaxb.metadata.MetadataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import springfox.documentation.spring.web.json.Json;
import springfox.documentation.swagger.web.UiConfiguration;

public class MoxyJaxbContextFactoryImpl implements JaxbContextFactory
{
    private static final Logger LOG = LoggerFactory.getLogger(MoxyJaxbContextFactoryImpl.class);
    private static final Set<Class<?>> baseExcludeClasses;
    private List<Class<?>> excludeClasses = new ArrayList<>();
    private List<Class<?>> otherClasses = new ArrayList<>();
    private SubclassRegistry subclassRegistry;
    private List<Class<?>> typeAdapters = new ArrayList<>();
    private Boolean wrapCollections;
    private int analysisDepth;
    private MetadataSourceFactory metadataSourceFactory;


    public JAXBContext createJaxbContext(Class... classes) throws JAXBException
    {
        Set<Class<?>> allClasses = computeAllClasses(classes);
        Map<String, Object> properties = computeProperties(allClasses);
        Class<?>[] classesArray = (Class[])allClasses.<Class<?>[]>toArray((Class<?>[][])new Class[0]);
        return JAXBContextFactory.createContext(classesArray, properties);
    }


    protected Map<String, Object> computeProperties(Set<Class<?>> allClasses)
    {
        Map<String, Object> properties = new HashMap<>();
        List<MetadataSource> mappings = new ArrayList<>();
        Set<Class<?>> metadataClasses = excludeClasses(allClasses);
        LOG.debug("Metadata classes: {}", metadataClasses);
        for(Class<?> clazz : metadataClasses)
        {
            MetadataSource ms = this.metadataSourceFactory.createMetadataSource(clazz, this.typeAdapters, getWrapCollections());
            mappings.add(ms);
        }
        properties.put("eclipselink.oxm.metadata-source", mappings);
        return properties;
    }


    protected Set<Class<?>> computeAllClasses(Class... classes)
    {
        Set<Class<?>> inputClasses = Sets.newHashSet((Object[])classes);
        LOG.debug("Input classes: {}", inputClasses);
        Set<Class<?>> allClasses = Sets.newHashSet(inputClasses);
        allClasses.addAll(this.otherClasses);
        Queue<ClassOnLevel> workQueue = (Queue<ClassOnLevel>)excludeClasses(allClasses).stream().map(c -> new ClassOnLevel(c, 0)).collect(Collectors.toCollection(LinkedList::new));
        while(!workQueue.isEmpty())
        {
            ClassOnLevel item = workQueue.poll();
            Set<Class<?>> details = computeDetailsForClass(item.clazz, item.level);
            details.stream().filter(c -> !allClasses.contains(c))
                            .forEach(c -> workQueue.add(new ClassOnLevel(c, item.level + 1)));
            allClasses.addAll(details);
        }
        LOG.debug("All classes: {}", allClasses);
        return allClasses;
    }


    protected Set<Class<?>> computeDetailsForClass(Class<?> clazz, int currentDepth)
    {
        Set<Class<?>> result = Sets.newHashSet();
        if(currentDepth >= getAnalysisDepth())
        {
            return result;
        }
        result.addAll(getAllSuperClasses(clazz));
        result.addAll(getInnerFields(clazz, 1));
        result.addAll(computeOtherClasses(result));
        result.addAll(expandGenericSuperclasses(result));
        return result;
    }


    protected Set<Class<?>> computeOtherClasses(Set<Class<?>> allClasses)
    {
        return (Set<Class<?>>)allClasses.stream()
                        .flatMap(c -> getSubclassRegistry().getAllSubclasses(c).stream())
                        .map(c -> c)
                        .collect(Collectors.toSet());
    }


    protected Set<Class<?>> expandGenericSuperclasses(Set<Class<?>> allClasses)
    {
        return (Set<Class<?>>)allClasses.stream()
                        .map(Class::getGenericSuperclass)
                        .map(xva$0 -> ReflectionUtils.extractActualType(new Type[] {xva$0})).flatMap(Collection::stream)
                        .filter(c -> !Object.class.equals(c))
                        .map(c -> c)
                        .collect(Collectors.toSet());
    }


    protected Set<Class<?>> excludeClasses(Set<Class<?>> classes)
    {
        Set<Class<?>> result = (Set<Class<?>>)classes.stream().filter(c -> !c.isPrimitive()).filter(c -> (c.getPackage() != null && !c.getPackage().getName().startsWith("java."))).collect(Collectors.toSet());
        result.removeAll(baseExcludeClasses);
        result.removeAll(this.excludeClasses);
        return result;
    }


    public boolean supports(Class<?> clazz)
    {
        return !excludeClasses(Sets.newHashSet((Object[])new Class[] {clazz})).isEmpty();
    }


    protected static List<Class<?>> getAllSuperClasses(Class<?> clazz)
    {
        List<Class<?>> classList = new ArrayList<>();
        Class<?> currentClass = clazz;
        while(currentClass != null && !currentClass.equals(Object.class))
        {
            classList.add(currentClass);
            currentClass = currentClass.getSuperclass();
        }
        return classList;
    }


    protected Set<Class<?>> getInnerFields(Class<?> clazz, int depth)
    {
        Set<Class<?>> result = new HashSet<>();
        if(depth <= 0)
        {
            return result;
        }
        Set<Class<?>> visitedClasses = new HashSet<>();
        Queue<ClassOnLevel> workQueue = new LinkedList<>();
        workQueue.add(new ClassOnLevel(clazz, 0));
        while(!workQueue.isEmpty())
        {
            ClassOnLevel item = workQueue.poll();
            Class<?> itemClass = item.clazz;
            int itemLevel = item.level;
            if(visitedClasses.contains(itemClass))
            {
                continue;
            }
            Set<Class<?>> newClasses = getClassesFromFields(itemClass, visitedClasses);
            if(itemLevel + 1 < depth)
            {
                List<ClassOnLevel> newItems = (List<ClassOnLevel>)newClasses.stream().map(cl -> new ClassOnLevel(cl, itemLevel + 1)).collect(Collectors.toList());
                workQueue.addAll(newItems);
            }
            result.addAll(newClasses);
            visitedClasses.add(itemClass);
        }
        return result;
    }


    protected Set<Class<?>> getClassesFromFields(Class<?> itemClass, Set<Class<?>> visitedClasses)
    {
        Set<Class<?>> result = new HashSet<>();
        Collection<Field> fields = ReflectionUtils.getAllFields(itemClass);
        for(Field field : fields)
        {
            Class<?> fieldClass = field.getType();
            if(baseExcludeClasses.contains(fieldClass) || visitedClasses.contains(fieldClass))
            {
                continue;
            }
            Collection<Class<?>> types = ReflectionUtils.extractTypes(field);
            for(Class<?> c : types)
            {
                if(baseExcludeClasses.contains(c))
                {
                    continue;
                }
                result.add(c);
            }
        }
        return result;
    }


    public List<Class<?>> getOtherClasses()
    {
        return this.otherClasses;
    }


    public void setOtherClasses(List<Class<?>> otherClasses)
    {
        this.otherClasses = otherClasses;
    }


    public List<Class<?>> getTypeAdapters()
    {
        return this.typeAdapters;
    }


    public void setTypeAdapters(List<Class<?>> typeAdapters)
    {
        this.typeAdapters = typeAdapters;
    }


    public Boolean getWrapCollections()
    {
        return this.wrapCollections;
    }


    public void setWrapCollections(Boolean wrapCollections)
    {
        this.wrapCollections = wrapCollections;
    }


    public int getAnalysisDepth()
    {
        return this.analysisDepth;
    }


    public void setAnalysisDepth(int analysisDepth)
    {
        this.analysisDepth = analysisDepth;
    }


    public List<Class<?>> getExcludeClasses()
    {
        return this.excludeClasses;
    }


    public void setExcludeClasses(List<Class<?>> excludeClasses)
    {
        this.excludeClasses = excludeClasses;
    }


    public void setMetadataSourceFactory(MetadataSourceFactory metadataSourceFactory)
    {
        this.metadataSourceFactory = metadataSourceFactory;
    }


    public SubclassRegistry getSubclassRegistry()
    {
        return this.subclassRegistry;
    }


    public void setSubclassRegistry(SubclassRegistry subclassRegistry)
    {
        this.subclassRegistry = subclassRegistry;
    }


    static
    {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(Object.class);
        classes.add(String.class);
        classes.add(Character.class);
        classes.add(char.class);
        classes.add(Number.class);
        classes.add(Byte.class);
        classes.add(byte.class);
        classes.add(Short.class);
        classes.add(short.class);
        classes.add(Integer.class);
        classes.add(int.class);
        classes.add(Long.class);
        classes.add(long.class);
        classes.add(Float.class);
        classes.add(float.class);
        classes.add(Double.class);
        classes.add(double.class);
        classes.add(Boolean.class);
        classes.add(boolean.class);
        classes.add(Void.class);
        classes.add(void.class);
        classes.add(Date.class);
        classes.add(BigInteger.class);
        classes.add(BigDecimal.class);
        classes.add(Json.class);
        classes.add(UiConfiguration.class);
        baseExcludeClasses = Collections.unmodifiableSet(classes);
    }
}
