package com.hybris.pcmbackoffice.classificationgroupsbackoffice.facades;

import com.hybris.classificationgroupsservices.model.ClassFeatureGroupAssignmentModel;
import com.hybris.classificationgroupsservices.model.ClassFeatureGroupModel;
import com.hybris.classificationgroupsservices.services.ClassFeatureGroupAssignmentService;
import com.hybris.cockpitng.search.data.SearchQueryCondition;
import com.hybris.cockpitng.search.data.SearchQueryConditionList;
import com.hybris.cockpitng.search.data.SearchQueryData;
import com.hybris.cockpitng.search.data.pageable.Pageable;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ClassFeatureGroupAssignmentSearchFacadeTest
{
    @Mock
    private ClassFeatureGroupAssignmentService classFeatureGroupAssignmentService;
    @InjectMocks
    private ClassFeatureGroupAssignmentSearchFacade facade;


    @Test
    public void shouldReturnAllClassFeatureGroupAssignmentsIfParentClassificationClassIsNotDefined()
    {
        ClassificationClassModel classificationClass = null;
        ClassFeatureGroupAssignmentModel featureGroupAssignment = (ClassFeatureGroupAssignmentModel)Mockito.mock(ClassFeatureGroupAssignmentModel.class);
        SearchQueryData searchQueryData = mockSearchQueryData(classificationClass);
        BDDMockito.given(this.classFeatureGroupAssignmentService.findAllFeatureGroupAssignments()).willReturn(List.of(featureGroupAssignment));
        Pageable<ClassFeatureGroupAssignmentModel> result = this.facade.search(searchQueryData);
        Assertions.assertThat(result.getAllResults()).containsExactlyInAnyOrder((Object[])new ClassFeatureGroupAssignmentModel[] {featureGroupAssignment});
    }


    @Test
    public void shouldReturnEmptyListIfClassFeatureGroupAssignmentsHasDifferentClassificationClass()
    {
        ClassFeatureGroupAssignmentModel featureGroupAssignment = (ClassFeatureGroupAssignmentModel)Mockito.mock(ClassFeatureGroupAssignmentModel.class);
        ClassificationClassModel classificationClass = (ClassificationClassModel)Mockito.mock(ClassificationClassModel.class);
        SearchQueryData searchQueryData = mockSearchQueryData(classificationClass);
        BDDMockito.given(this.classFeatureGroupAssignmentService.findAllFeatureGroupAssignments()).willReturn(List.of(featureGroupAssignment));
        BDDMockito.given(featureGroupAssignment.getClassificationClass()).willReturn(Mockito.mock(ClassificationClassModel.class));
        Pageable<ClassFeatureGroupAssignmentModel> result = this.facade.search(searchQueryData);
        Assertions.assertThat(result.getAllResults()).isEmpty();
    }


    @Test
    public void shouldReturnListOfClassFeatureGroupAssignmentForMatchingClassificationClass()
    {
        ClassificationClassModel classificationClass = (ClassificationClassModel)Mockito.mock(ClassificationClassModel.class);
        ClassFeatureGroupAssignmentModel featureGroupAssignment = (ClassFeatureGroupAssignmentModel)Mockito.mock(ClassFeatureGroupAssignmentModel.class);
        SearchQueryData searchQueryData = mockSearchQueryData(classificationClass);
        BDDMockito.given(this.classFeatureGroupAssignmentService.findAllFeatureGroupAssignments()).willReturn(List.of(featureGroupAssignment));
        BDDMockito.given(featureGroupAssignment.getClassificationClass()).willReturn(classificationClass);
        BDDMockito.given(featureGroupAssignment.getClassFeatureGroup()).willReturn(null);
        Pageable<ClassFeatureGroupAssignmentModel> result = this.facade.search(searchQueryData);
        Assertions.assertThat(result.getAllResults()).containsExactlyInAnyOrder((Object[])new ClassFeatureGroupAssignmentModel[] {featureGroupAssignment});
    }


    @Test
    public void shouldFilterOutClassFeatureGroupAssignmentIfGroupIsAlreadyAssigned()
    {
        ClassFeatureGroupAssignmentModel featureGroupAssignment = (ClassFeatureGroupAssignmentModel)Mockito.mock(ClassFeatureGroupAssignmentModel.class);
        ClassificationClassModel classificationClass = (ClassificationClassModel)Mockito.mock(ClassificationClassModel.class);
        SearchQueryData searchQueryData = mockSearchQueryData(classificationClass);
        BDDMockito.given(this.classFeatureGroupAssignmentService.findAllFeatureGroupAssignments()).willReturn(List.of(featureGroupAssignment));
        BDDMockito.given(featureGroupAssignment.getClassificationClass()).willReturn(classificationClass);
        BDDMockito.given(featureGroupAssignment.getClassFeatureGroup()).willReturn(Mockito.mock(ClassFeatureGroupModel.class));
        Pageable<ClassFeatureGroupAssignmentModel> result = this.facade.search(searchQueryData);
        Assertions.assertThat(result.getAllResults()).isEmpty();
    }


    private SearchQueryData mockSearchQueryData(ClassificationClassModel classificationClass)
    {
        SearchQueryData searchQueryData = (SearchQueryData)Mockito.mock(SearchQueryData.class);
        SearchQueryConditionList queryConditionList = (SearchQueryConditionList)Mockito.mock(SearchQueryConditionList.class);
        SearchQueryCondition classificationClassCondition = (SearchQueryCondition)Mockito.mock(SearchQueryCondition.class);
        List list = Lists.newArrayList((Object[])new SearchQueryConditionList[] {queryConditionList});
        BDDMockito.given(queryConditionList.getConditions()).willReturn(List.of(classificationClassCondition));
        BDDMockito.given(searchQueryData.getConditions()).willReturn(list);
        BDDMockito.given(classificationClassCondition.getValue()).willReturn(classificationClass);
        BDDMockito.given(Integer.valueOf(searchQueryData.getPageSize())).willReturn(Integer.valueOf(1));
        return searchQueryData;
    }
}
