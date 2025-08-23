package de.hybris.y2ysync.task.internal;

import com.google.common.collect.Lists;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.TaskConditionModel;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskService;
import de.hybris.y2ysync.enums.Y2YSyncType;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
@UnitTest
public class SyncTaskFactoryTest
{
    @InjectMocks
    private final SyncTaskFactory service = new SyncTaskFactory();
    @Mock
    private ModelService modelService;
    @Mock
    private TaskService taskService;
    @Mock
    private TaskModel task1;
    @Mock
    private TaskModel task2;
    @Mock
    private TaskModel mainTask;
    @Mock
    private TaskConditionModel condition1;
    @Mock
    private TaskConditionModel condition2;
    @Mock
    private ComposedTypeModel composedType1;
    @Mock
    private ComposedTypeModel composedType2;


    @Before
    public void setUp() throws Exception
    {
        BDDMockito.given(this.modelService.create(TaskModel.class)).willReturn(this.task1, new Object[] {this.task2, this.mainTask});
        BDDMockito.given(this.modelService.create(TaskConditionModel.class)).willReturn(this.condition1, new Object[] {this.condition2});
        Mockito.lenient().when(this.composedType1.getCode()).thenReturn("Product");
        Mockito.lenient().when(this.composedType2.getCode()).thenReturn("Title");
    }


    @Test
    public void shouldRunChunkTasksForEachMediaPKPlusOneMainTaskWithConditions() throws Exception
    {
        String syncExecutionID = "testSyncExecutionId";
        PK productPk = PK.createFixedUUIDPK(100, 1L);
        PK titlePk = PK.createFixedUUIDPK(101, 1L);
        MediasForType mediasForType1 = MediasForType.builder().withComposedTypeCode(this.composedType1.getCode()).withImpExHeader(";foo;bar;baz;").withDataHubColumns("").withMediaPks(Lists.newArrayList((Object[])new PK[] {productPk})).withDataHubType("dataHubType").build();
        MediasForType mediasForType2 = MediasForType.builder().withComposedTypeCode(this.composedType1.getCode()).withImpExHeader(";baz;boom;maz;").withDataHubColumns("").withMediaPks(Lists.newArrayList((Object[])new PK[] {titlePk})).build();
        ArrayList<MediasForType> mediasPerType = Lists.newArrayList((Object[])new MediasForType[] {mediasForType1, mediasForType2});
        this.service.runSyncTasks("testSyncExecutionId", Y2YSyncType.ZIP, mediasPerType);
        ((TaskModel)Mockito.verify(this.task1)).setRunnerBean("itemChangesProcessor");
        ((TaskModel)Mockito.verify(this.task2)).setRunnerBean("itemChangesProcessor");
        ((TaskModel)Mockito.verify(this.task1)).setContext(this.service.getChunkTaskContext("testSyncExecutionId", Y2YSyncType.ZIP, productPk, mediasForType1));
        ((TaskModel)Mockito.verify(this.task2)).setContext(this.service.getChunkTaskContext("testSyncExecutionId", Y2YSyncType.ZIP, titlePk, mediasForType2));
        ((TaskModel)Mockito.verify(this.task1)).setExecutionDate(anyDate());
        ((TaskModel)Mockito.verify(this.task2)).setExecutionDate(anyDate());
        ((TaskConditionModel)Mockito.verify(this.condition1)).setUniqueID("testSyncExecutionId_" + productPk);
        ((TaskConditionModel)Mockito.verify(this.condition2)).setUniqueID("testSyncExecutionId_" + titlePk);
        ((TaskModel)Mockito.verify(this.mainTask)).setContext(this.service.getMainTaskContext("testSyncExecutionId"));
        ((TaskModel)Mockito.verify(this.mainTask)).setConditions(exactConditions(new TaskConditionModel[] {this.condition1, this.condition2}));
        ((TaskService)Mockito.verify(this.taskService)).scheduleTask(this.task1);
        ((TaskService)Mockito.verify(this.taskService)).scheduleTask(this.task2);
        ((TaskService)Mockito.verify(this.taskService)).scheduleTask(this.mainTask);
    }


    private static <T> T anyDate()
    {
        return (T)ArgumentMatchers.argThat((ArgumentMatcher)new DateArgumentMatcher());
    }


    private static <T> T exactConditions(TaskConditionModel... conditions)
    {
        return (T)ArgumentMatchers.argThat((ArgumentMatcher)new SetOfTaskConditions(conditions));
    }
}
