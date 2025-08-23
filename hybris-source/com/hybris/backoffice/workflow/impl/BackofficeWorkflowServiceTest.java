package com.hybris.backoffice.workflow.impl;

import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.workflow.WorkflowStatus;
import de.hybris.platform.workflow.model.WorkflowModel;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import org.fest.assertions.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BackofficeWorkflowServiceTest
{
    private static final int START_INDEX = 0;
    private static final int PAGE_SIZE = 5;
    private static final Date TODAY = toDate(LocalDateTime.now());
    private static final Date TOMORROW = toDate(LocalDateTime.now().plusDays(1L));
    @Spy
    private BackofficeWorkflowService workflowService = new BackofficeWorkflowService();
    private EnumSet<WorkflowStatus> allWorkflowStatuses = EnumSet.allOf(WorkflowStatus.class);


    private static Date toDate(LocalDateTime localDateTime)
    {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }


    @Test
    public void testAllWorkflowsWhenDateRangeIncorrect()
    {
        List<WorkflowModel> workflows = this.workflowService.getAllWorkflows(this.allWorkflowStatuses, TOMORROW, TODAY);
        ((BackofficeWorkflowService)Mockito.verify(this.workflowService, Mockito.times(1))).dateToBeforeDateFrom(TOMORROW, TODAY);
        Assertions.assertThat(workflows).isEmpty();
    }


    @Test
    public void testAllWorkflowsPageWhenDateRangeIncorrect()
    {
        SearchResult<WorkflowModel> workflows = this.workflowService.getAllWorkflows(this.allWorkflowStatuses, TOMORROW, TODAY, 0, 5);
        ((BackofficeWorkflowService)Mockito.verify(this.workflowService, Mockito.times(1))).dateToBeforeDateFrom(TOMORROW, TODAY);
        Assertions.assertThat(workflows.getResult()).isEmpty();
    }


    @Test
    public void testAllAdhocWorkflowsWhenDateRangeIncorrect()
    {
        List<WorkflowModel> adhocWorkflows = this.workflowService.getAllAdhocWorkflows(this.allWorkflowStatuses, TOMORROW, TODAY);
        ((BackofficeWorkflowService)Mockito.verify(this.workflowService, Mockito.times(1))).dateToBeforeDateFrom(TOMORROW, TODAY);
        Assertions.assertThat(adhocWorkflows).isEmpty();
    }


    @Test
    public void testAllAdhocWorkflowsPageWhenDateRangeIncorrect()
    {
        SearchResult<WorkflowModel> adhocWorkflows = this.workflowService.getAllAdhocWorkflows(this.allWorkflowStatuses, TOMORROW, TODAY, 0, 5);
        ((BackofficeWorkflowService)Mockito.verify(this.workflowService, Mockito.times(1))).dateToBeforeDateFrom(TOMORROW, TODAY);
        Assertions.assertThat(adhocWorkflows.getResult()).isEmpty();
    }
}
