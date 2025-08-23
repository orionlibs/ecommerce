package io.github.orionlibs.ecommerce.lifecycle;

import java.util.List;
import javax.sql.DataSource;
import net.ttddyy.dsproxy.ExecutionInfo;
import net.ttddyy.dsproxy.QueryInfo;
import net.ttddyy.dsproxy.listener.QueryExecutionListener;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DataSourceConfiguration
{
    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties primaryDataSourceProperties()
    {
        return new DataSourceProperties();
    }


    @Bean
    @Primary
    public DataSource primaryDataSource(@Qualifier("primaryDataSourceProperties") DataSourceProperties props)
    {
        DataSource realDs = props.initializeDataSourceBuilder().build();
        return ProxyDataSourceBuilder
                        .create(realDs)
                        .name("DB-PROXY")
                        .listener(new QueryExecutionListener()
                        {
                            @Override
                            public void beforeQuery(ExecutionInfo execInfo, List<QueryInfo> queryInfoList)
                            {
                                //Instant now = Instant.now();
                                for(QueryInfo qi : queryInfoList)
                                {
                                    String sql = qi.getQuery();
                                    //System.out.println("-------SQL running: " + sql);
                                }
                            }


                            @Override
                            public void afterQuery(ExecutionInfo execInfo, List<QueryInfo> queryInfoList)
                            {
                                //long durationMs = execInfo.getElapsedTime(); // in ms
                                //Instant now = Instant.now();
                                for(QueryInfo qi : queryInfoList)
                                {
                                    String sql = qi.getQuery();
                                    // persist asynchronously to avoid blocking the thread that’s doing your business logic
                                    //System.out.println("-------SQL ran: " + sql);
                                    //repo.save(new QueryLog(null, sql, now, durationMs));
                                }
                            }
                        })
                        .build();
        //return realDs;
    }
}
