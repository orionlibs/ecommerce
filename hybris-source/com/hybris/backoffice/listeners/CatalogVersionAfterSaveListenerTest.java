package com.hybris.backoffice.listeners;

import com.hybris.backoffice.catalogversioneventhandling.AvailableCatalogVersionsTag;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.tx.AfterSaveEvent;
import java.util.ArrayList;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CatalogVersionAfterSaveListenerTest
{
    private static final int CATALOG_VERSION_DEPLOYMENT_CODE = 601;
    @Mock
    private ModelService modelService;
    @Mock
    private AvailableCatalogVersionsTag availableCatalogVersionsTag;
    @InjectMocks
    @Spy
    private CatalogVersionAfterSaveListener testSubject;


    @Test
    public void shouldProceedEventsWhenPlatformIsReady()
    {
        setPlatformReady();
        this.testSubject.afterSave(getEventsWithCreateCatalogVersionEvents());
        ((CatalogVersionAfterSaveListener)Mockito.verify(this.testSubject)).handleEvent();
    }


    @Test
    public void shouldNotProceedEventsWhenPlatformIsNotInitialized()
    {
        setPlatformNotInitialized();
        this.testSubject.afterSave(getEventsWithCreateCatalogVersionEvents());
        ((CatalogVersionAfterSaveListener)Mockito.verify(this.testSubject, Mockito.never())).handleEvent();
    }


    @Test
    public void shouldProceedCreateAndRemoveEventsWhenPlatformIsInitialized()
    {
        setPlatformReady();
        this.testSubject.afterSave(getEventsWithCreateAndRemoveCatalogVersionEvents());
        ((CatalogVersionAfterSaveListener)Mockito.verify(this.testSubject)).handleEvent();
    }


    private void setPlatformReady()
    {
        Mockito.when(Boolean.valueOf(this.testSubject.shouldPerform())).thenReturn(Boolean.valueOf(true));
    }


    private void setPlatformNotInitialized()
    {
        Mockito.when(Boolean.valueOf(this.testSubject.shouldPerform())).thenReturn(Boolean.valueOf(false));
    }


    private Collection<AfterSaveEvent> getEventsWithCreateCatalogVersionEvents()
    {
        AfterSaveEvent createEventMock = (AfterSaveEvent)Mockito.mock(AfterSaveEvent.class);
        Mockito.when(Integer.valueOf(createEventMock.getType())).thenReturn(Integer.valueOf(4));
        PK pk = PK.createFixedUUIDPK(601, 1L);
        Mockito.when(createEventMock.getPk()).thenReturn(pk);
        Collection<AfterSaveEvent> events = new ArrayList<>();
        events.add(createEventMock);
        Mockito.lenient().when(this.modelService.get(pk)).thenReturn(Mockito.mock(CatalogVersionModel.class));
        return events;
    }


    private Collection<AfterSaveEvent> getEventsWithRemoveCatalogVersionEvents()
    {
        AfterSaveEvent removeEventMock = (AfterSaveEvent)Mockito.mock(AfterSaveEvent.class);
        Mockito.when(Integer.valueOf(removeEventMock.getType())).thenReturn(Integer.valueOf(2));
        PK pk = PK.createFixedUUIDPK(601, 1L);
        Mockito.when(removeEventMock.getPk()).thenReturn(pk);
        Collection<AfterSaveEvent> events = new ArrayList<>();
        events.add(removeEventMock);
        Mockito.when(this.modelService.get(pk)).thenReturn(Mockito.mock(CatalogVersionModel.class));
        return events;
    }


    private Collection<AfterSaveEvent> getEventsWithCreateAndRemoveCatalogVersionEvents()
    {
        AfterSaveEvent removeEventMock = (AfterSaveEvent)Mockito.mock(AfterSaveEvent.class);
        Mockito.lenient().when(Integer.valueOf(removeEventMock.getType())).thenReturn(Integer.valueOf(2));
        PK removePk = PK.createFixedUUIDPK(601, 1L);
        Mockito.lenient().when(removeEventMock.getPk()).thenReturn(removePk);
        AfterSaveEvent createEventMock = (AfterSaveEvent)Mockito.mock(AfterSaveEvent.class);
        Mockito.when(Integer.valueOf(createEventMock.getType())).thenReturn(Integer.valueOf(4));
        PK createPk = PK.createFixedUUIDPK(601, 2L);
        Mockito.when(createEventMock.getPk()).thenReturn(createPk);
        Collection<AfterSaveEvent> events = new ArrayList<>();
        events.add(createEventMock);
        events.add(removeEventMock);
        Mockito.lenient().when(this.modelService.get(createPk)).thenReturn(Mockito.mock(CatalogVersionModel.class));
        Mockito.lenient().when(this.modelService.get(removePk)).thenReturn(Mockito.mock(CatalogVersionModel.class));
        return events;
    }
}
