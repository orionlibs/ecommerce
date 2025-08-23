package de.hybris.platform.masterserver.collector.business.impl;

import com.google.common.collect.ImmutableMap;
import com.hybris.statistics.collector.BusinessStatisticsCollector;
import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.context.ApplicationContext;

public class OrdersCollector implements BusinessStatisticsCollector<Map<String, Map<String, Object>>>
{
    private final FlexibleSearchService flexibleSearchService;
    private final SessionService sessionService;
    private final UserService userService;


    public OrdersCollector()
    {
        ApplicationContext ctx = Registry.getApplicationContext();
        this.flexibleSearchService = (FlexibleSearchService)ctx.getBean("flexibleSearchService", FlexibleSearchService.class);
        this.sessionService = (SessionService)ctx.getBean("sessionService", SessionService.class);
        this.userService = (UserService)ctx.getBean("userService", UserService.class);
    }


    public Map<String, Map<String, Object>> collectStatistics()
    {
        Map<String, Object> result = new HashMap<>();
        result.put("numOrders", getNumOrders());
        return (Map<String, Map<String, Object>>)ImmutableMap.builder().put("orders", result).build();
    }


    private Integer getNumOrders()
    {
        return (Integer)this.sessionService.executeInLocalView((SessionExecutionBody)new Object(this));
    }
}
