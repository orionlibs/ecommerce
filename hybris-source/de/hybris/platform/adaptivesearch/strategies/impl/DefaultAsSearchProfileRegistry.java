package de.hybris.platform.adaptivesearch.strategies.impl;

import de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProfileActivationMapping;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProfileMapping;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProfileRegistry;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class DefaultAsSearchProfileRegistry implements AsSearchProfileRegistry, ApplicationContextAware, InitializingBean
{
    private ApplicationContext applicationContext;
    private Map<String, AsSearchProfileMapping> searchProfileMappings;
    private List<AsSearchProfileActivationMapping> searchProfileActivationMappings;


    public ApplicationContext getApplicationContext()
    {
        return this.applicationContext;
    }


    public void setApplicationContext(ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
    }


    public void afterPropertiesSet()
    {
        initializeSearchProfileMappings();
        initializeSearchProfileActivationMappings();
    }


    protected void initializeSearchProfileMappings()
    {
        Map<String, DefaultAsSearchProfileMapping> beans = this.applicationContext.getBeansOfType(DefaultAsSearchProfileMapping.class);
        this
                        .searchProfileMappings = (Map<String, AsSearchProfileMapping>)beans.values().stream().collect(Collectors.toMap(DefaultAsSearchProfileMapping::getType, mapping -> mapping));
    }


    protected void initializeSearchProfileActivationMappings()
    {
        Map<String, DefaultAsSearchProfileActivationMapping> beans = this.applicationContext.getBeansOfType(DefaultAsSearchProfileActivationMapping.class);
        this
                        .searchProfileActivationMappings = (List<AsSearchProfileActivationMapping>)beans.values().stream().sorted(this::compareSearchProfileActivationMappings).collect(Collectors.toList());
    }


    protected int compareSearchProfileActivationMappings(DefaultAsSearchProfileActivationMapping mapping1, DefaultAsSearchProfileActivationMapping mapping2)
    {
        return Integer.compare(mapping2.getPriority(), mapping1.getPriority());
    }


    public AsSearchProfileMapping getSearchProfileMapping(AbstractAsSearchProfileModel searchProfile)
    {
        return this.searchProfileMappings.get(searchProfile.getClass().getName());
    }


    public Map<String, AsSearchProfileMapping> getSearchProfileMappings()
    {
        return Collections.unmodifiableMap(this.searchProfileMappings);
    }


    public List<AsSearchProfileActivationMapping> getSearchProfileActivationMappings()
    {
        return Collections.unmodifiableList(this.searchProfileActivationMappings);
    }
}
