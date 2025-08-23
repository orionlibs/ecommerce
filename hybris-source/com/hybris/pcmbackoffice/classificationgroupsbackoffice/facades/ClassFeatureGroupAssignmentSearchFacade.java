package com.hybris.pcmbackoffice.classificationgroupsbackoffice.facades;

import com.hybris.classificationgroupsservices.model.ClassFeatureGroupAssignmentModel;
import com.hybris.classificationgroupsservices.services.ClassFeatureGroupAssignmentService;
import com.hybris.cockpitng.editor.commonreferenceeditor.ReferenceEditorSearchFacade;
import com.hybris.cockpitng.search.data.SearchQueryCondition;
import com.hybris.cockpitng.search.data.SearchQueryConditionList;
import com.hybris.cockpitng.search.data.SearchQueryData;
import com.hybris.cockpitng.search.data.pageable.Pageable;
import com.hybris.cockpitng.search.data.pageable.PageableList;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;

public class ClassFeatureGroupAssignmentSearchFacade implements ReferenceEditorSearchFacade<ClassFeatureGroupAssignmentModel>
{
    private ClassFeatureGroupAssignmentService classFeatureGroupAssignmentService;


    public Pageable<ClassFeatureGroupAssignmentModel> search(SearchQueryData searchQueryData)
    {
        List<ClassFeatureGroupAssignmentModel> classFeatureGroupAssignments = this.classFeatureGroupAssignmentService.findAllFeatureGroupAssignments();
        List<ClassFeatureGroupAssignmentModel> filteredClassFeatureGroupAssignments = classFeatureGroupAssignments;
        Optional<ClassificationClassModel> optionalClassificationClass = extractClassificationClass(searchQueryData);
        if(optionalClassificationClass.isPresent())
        {
            ClassificationClassModel classificationClass = optionalClassificationClass.get();
            filteredClassFeatureGroupAssignments = (List<ClassFeatureGroupAssignmentModel>)classFeatureGroupAssignments.stream().filter(groupAssignment -> (classificationClass.equals(groupAssignment.getClassificationClass()) && groupAssignment.getClassFeatureGroup() == null))
                            .collect(Collectors.toList());
        }
        return (Pageable<ClassFeatureGroupAssignmentModel>)new PageableList(filteredClassFeatureGroupAssignments, searchQueryData.getPageSize());
    }


    private Optional<ClassificationClassModel> extractClassificationClass(SearchQueryData searchQueryData)
    {
        Objects.requireNonNull(SearchQueryConditionList.class);
        Objects.requireNonNull(ClassificationClassModel.class);
        Objects.requireNonNull(ClassificationClassModel.class);
        return searchQueryData.getConditions().stream().map(SearchQueryConditionList.class::cast).map(SearchQueryConditionList::getConditions).flatMap(Collection::stream).map(SearchQueryCondition::getValue).filter(ClassificationClassModel.class::isInstance).map(ClassificationClassModel.class::cast)
                        .findFirst();
    }


    @Required
    public void setClassFeatureGroupAssignmentService(ClassFeatureGroupAssignmentService classFeatureGroupAssignmentService)
    {
        this.classFeatureGroupAssignmentService = classFeatureGroupAssignmentService;
    }
}
