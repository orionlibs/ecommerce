package de.hybris.platform.masterserver.collector.system.impl.spring;

import com.google.common.collect.ImmutableList;
import de.hybris.platform.core.Registry;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

public class SpringConfigOverviewProvider
{
    private final Function<Collection<BeanInfo>, String> serializer = new JsonSerializer()::toJson;


    public String getCurrentTenantCoreContextOverviewAsJson()
    {
        return this.serializer.apply(getAvailableBeansForCurrentTenant());
    }


    Collection<BeanInfo> getAvailableBeansForCurrentTenant()
    {
        return getAvailableBeans(Registry.getCoreApplicationContext());
    }


    List<BeanInfo> getAvailableBeans(ApplicationContext ctx)
    {
        if(!(ctx instanceof ConfigurableApplicationContext))
        {
            return Collections.emptyList();
        }
        ConfigurableListableBeanFactory beanFactory = ((ConfigurableApplicationContext)ctx).getBeanFactory();
        List<BeanInfo> parentBeans = getAvailableBeans(ctx.getParent());
        List<BeanInfo> myBeans = extractBeans(beanFactory);
        return (List<BeanInfo>)ImmutableList.builder().addAll(parentBeans).addAll(myBeans).build();
    }


    private List<BeanInfo> extractBeans(ConfigurableListableBeanFactory beanFactory)
    {
        BeanInfoProvider builder = BeanInfoProvider.from(beanFactory);
        Objects.requireNonNull(builder);
        return (List<BeanInfo>)StreamSupport.stream(Spliterators.spliteratorUnknownSize(beanFactory.getBeanNamesIterator(), 0), false).map(builder::getBeanInfo)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList());
    }
}
