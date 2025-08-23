package com.hybris.backoffice.solrsearch.populators;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.converters.impl.AbstractPopulatingConverter;
import java.util.Collections;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class BackofficeIndexedPropertyPopulatorLoaderTest
{
    @InjectMocks
    private BackofficeIndexedPropertyPopulatorLoader populatorLoader;
    @Mock
    private BeanFactory beanFacotry;
    @Mock
    private ApplicationContext applicationContext;
    @Mock
    private BackofficeIndexedPropertyPopulator populator;


    @Before
    public void setUp()
    {
        ((BeanFactory)Mockito.doReturn(new String[] {"indexedPropertyConverter"}).when(this.beanFacotry)).getAliases((String)Matchers.any());
        ((ApplicationContext)Mockito.doReturn(this.populator).when(this.applicationContext)).getBean("defaultBackofficeIndexedPropertyPopulator");
    }


    @Test
    public void postProcessAfterInitialization()
    {
        AbstractPopulatingConverter populatingConverter = (AbstractPopulatingConverter)Mockito.mock(AbstractPopulatingConverter.class);
        ((AbstractPopulatingConverter)Mockito.doReturn(Collections.singletonList((Populator)Mockito.mock(Populator.class))).when(populatingConverter)).getPopulators();
        this.populatorLoader.postProcessAfterInitialization(populatingConverter, "defaultBackofficeIndexedPropertyPopulator");
        ArgumentCaptor<List> argumentCaptor = ArgumentCaptor.forClass(List.class);
        ((AbstractPopulatingConverter)Mockito.verify(populatingConverter)).setPopulators((List)argumentCaptor.capture());
        Assertions.assertThat((List)argumentCaptor.getValue()).hasSize(2);
    }
}
