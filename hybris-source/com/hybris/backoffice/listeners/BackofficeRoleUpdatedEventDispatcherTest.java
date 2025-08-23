package com.hybris.backoffice.listeners;

import com.hybris.backoffice.model.user.BackofficeRoleModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.tx.AfterSaveEvent;
import java.util.ArrayList;
import java.util.Collection;
import org.fest.assertions.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BackofficeRoleUpdatedEventDispatcherTest
{
    private static final int BACKOFFICE_ROLE_DEPLOYMENT_CODE = 5;
    private static final PK PK_USER_GROUP = PK.createFixedUUIDPK(5, 1L);
    private static final BackofficeRoleModel ROLE_MODEL = (BackofficeRoleModel)Mockito.mock(BackofficeRoleModel.class);
    @InjectMocks
    @Spy
    private BackofficeRoleUpdatedEventDispatcher testSubject;
    @Mock
    private ModelService modelService;
    @Mock
    private EventService eventService;


    @Test
    public void shouldSendBackofficeRoleClusterAwareEventWhenBackofficeRoleUpdateIsPerformed()
    {
        Mockito.when(Boolean.valueOf(this.testSubject.shouldPerform())).thenReturn(Boolean.valueOf(true));
        this.testSubject.afterSave(getEventsWithBackofficeRoleUpdateEvents());
        ArgumentCaptor<BackofficeRoleUpdatedClusterAwareEvent> roleCapture = ArgumentCaptor.forClass(BackofficeRoleUpdatedClusterAwareEvent.class);
        ((EventService)Mockito.verify(this.eventService)).publishEvent((AbstractEvent)roleCapture.capture());
        BackofficeRoleUpdatedClusterAwareEvent event = (BackofficeRoleUpdatedClusterAwareEvent)roleCapture.getValue();
        Assertions.assertThat(event.getRoles()).containsOnly(new Object[] {PK_USER_GROUP});
    }


    @Test
    public void shouldNotBackofficeRoleClusterAwareEventWhenBackofficeRoleUpdateIsNotPerformed()
    {
        Mockito.when(Boolean.valueOf(this.testSubject.shouldPerform())).thenReturn(Boolean.valueOf(true));
        this.testSubject.afterSave(getEventsWithUserGroupUpdateEvents());
        ((EventService)Mockito.verify(this.eventService, Mockito.times(0))).publishEvent((AbstractEvent)Matchers.anyObject());
    }


    private Collection<AfterSaveEvent> getEventsWithBackofficeRoleUpdateEvents()
    {
        Collection<AfterSaveEvent> events = createAfterSaveEventCollection();
        Mockito.when(ROLE_MODEL.getItemtype()).thenReturn("BackofficeRole");
        Mockito.when(this.modelService.get(PK_USER_GROUP)).thenReturn(ROLE_MODEL);
        return events;
    }


    private Collection<AfterSaveEvent> getEventsWithUserGroupUpdateEvents()
    {
        Collection<AfterSaveEvent> events = createAfterSaveEventCollection();
        UserGroupModel userGroupModel = (UserGroupModel)Mockito.mock(UserGroupModel.class);
        Mockito.when(userGroupModel.getItemtype()).thenReturn("UserGroup");
        Mockito.when(this.modelService.get(PK_USER_GROUP)).thenReturn(userGroupModel);
        return events;
    }


    private Collection<AfterSaveEvent> createAfterSaveEventCollection()
    {
        AfterSaveEvent afterSaveEvent = (AfterSaveEvent)Mockito.mock(AfterSaveEvent.class);
        Mockito.when(afterSaveEvent.getPk()).thenReturn(PK_USER_GROUP);
        Collection<AfterSaveEvent> events = new ArrayList<>();
        events.add(afterSaveEvent);
        return events;
    }
}
