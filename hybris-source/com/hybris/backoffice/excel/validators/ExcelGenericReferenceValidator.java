package com.hybris.backoffice.excel.validators;

import com.hybris.backoffice.excel.data.ExcelWorkbook;
import com.hybris.backoffice.excel.data.ExcelWorksheet;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.translators.generic.RequiredAttribute;
import com.hybris.backoffice.excel.translators.generic.factory.RequiredAttributesFactory;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import com.hybris.backoffice.excel.validators.util.ExcelWorkbookEntriesService;
import com.hybris.backoffice.excel.validators.util.WorksheetEntryKey;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.CollectionTypeModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.MapTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.type.TypeService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class ExcelGenericReferenceValidator implements ExcelValidator
{
    protected static final String VALIDATION_MESSAGE_KEY = "excel.import.validation.generic.translator.not.existing.item";
    private static final String WORKBOOK_ENTRIES_CACHE_KEY = "workbookEntries";
    private RequiredAttributesFactory requiredAttributesFactory;
    private FlexibleSearchService flexibleSearchService;
    private ExcelWorkbookEntriesService excelWorkbookEntriesService;
    private Collection<String> blacklistedTypes;
    private TypeService typeService;


    public boolean canHandle(ImportParameters importParameters, AttributeDescriptorModel attributeDescriptor)
    {
        return (importParameters.isCellValueNotBlank() && !isBlacklisted(attributeDescriptor));
    }


    protected boolean isBlacklisted(AttributeDescriptorModel attribute)
    {
        for(String blacklistedType : this.blacklistedTypes)
        {
            if(this.typeService.isAssignableFrom(blacklistedType, attribute.getAttributeType().getCode()))
            {
                return true;
            }
        }
        return false;
    }


    public ExcelValidationResult validate(ImportParameters importParameters, AttributeDescriptorModel attributeDescriptor, Map<String, Object> context)
    {
        populateContextIfNeeded(context);
        RequiredAttribute requiredAttribute = getRequiredAttributesFactory().create(attributeDescriptor);
        return validateRequiredAttribute(requiredAttribute, importParameters, context);
    }


    public ExcelValidationResult validateRequiredAttribute(RequiredAttribute requiredAttribute, ImportParameters importParameters, Map<String, Object> context)
    {
        return new ExcelValidationResult(recursivelyValidateAllLevels(requiredAttribute, importParameters, context));
    }


    private void populateContextIfNeeded(Map<String, Object> context)
    {
        if(context.containsKey("workbookEntries"))
        {
            return;
        }
        HashSet<Object> workbookEntries = new HashSet();
        context.put("workbookEntries", workbookEntries);
        ExcelWorkbook excelWorkbook = (ExcelWorkbook)context.get(ExcelWorkbook.class.getCanonicalName());
        if(excelWorkbook == null)
        {
            return;
        }
        for(ExcelWorksheet worksheet : excelWorkbook.getWorksheets())
        {
            Collection<WorksheetEntryKey> entriesKeys = this.excelWorkbookEntriesService.generateEntryKeys(worksheet);
            for(WorksheetEntryKey entryKey : entriesKeys)
            {
                workbookEntries
                                .addAll(generateCacheKeysForType(entryKey.getRequiredAttribute(), entryKey.getUniqueAttributesValues()));
            }
        }
    }


    private Collection<String> generateCacheKeysForType(RequiredAttribute requiredAttribute, Map<String, String> params)
    {
        List<String> cacheKeys = new ArrayList<>();
        cacheKeys.add(createCacheKey(getTypeCode(requiredAttribute.getTypeModel()), requiredAttribute, params));
        cacheKeys.addAll((Collection<? extends String>)((ComposedTypeModel)requiredAttribute.getTypeModel()).getAllSubTypes().stream()
                        .map(subtype -> createCacheKey(getTypeCode((TypeModel)subtype), requiredAttribute, params)).collect(Collectors.toList()));
        cacheKeys.addAll((Collection<? extends String>)((ComposedTypeModel)requiredAttribute.getTypeModel()).getAllSuperTypes().stream()
                        .map(superType -> createCacheKey(getTypeCode((TypeModel)superType), requiredAttribute, params)).collect(Collectors.toList()));
        return cacheKeys;
    }


    protected List<ValidationMessage> recursivelyValidateAllLevels(RequiredAttribute rootUniqueAttribute, ImportParameters importParameters, Map<String, Object> context)
    {
        List<ValidationMessage> messages = new ArrayList<>();
        for(RequiredAttribute child : rootUniqueAttribute.getChildren())
        {
            messages.addAll(recursivelyValidateAllLevels(child, importParameters, context));
        }
        if(messages.isEmpty())
        {
            messages.addAll(validateSingleLevel(rootUniqueAttribute, importParameters, context));
        }
        return messages;
    }


    protected List<ValidationMessage> validateSingleLevel(RequiredAttribute rootUniqueAttribute, ImportParameters importParameters, Map<String, Object> context)
    {
        List<ValidationMessage> validationMessages = new ArrayList<>();
        for(Map<String, String> params : (Iterable<Map<String, String>>)importParameters.getMultiValueParameters())
        {
            Objects.requireNonNull(validationMessages);
            validateSingleValue(rootUniqueAttribute, params, context).ifPresent(validationMessages::add);
        }
        return validationMessages;
    }


    protected Optional<ValidationMessage> validateSingleValue(RequiredAttribute rootUniqueAttribute, Map<String, String> importParameters, Map<String, Object> context)
    {
        String cacheKey = createCacheKey(getTypeCode(rootUniqueAttribute.getTypeModel()), rootUniqueAttribute, importParameters);
        if(context.containsKey(cacheKey))
        {
            return Optional.empty();
        }
        Optional<FlexibleSearchQuery> flexibleSearchQuery = buildFlexibleSearchQuery(rootUniqueAttribute, importParameters, context);
        if(flexibleSearchQuery.isPresent())
        {
            SearchResult<ItemModel> searchResult = this.flexibleSearchService.search(flexibleSearchQuery.get());
            int count = searchResult.getCount();
            if(count != 1)
            {
                if(((Set)context.get("workbookEntries")).contains(cacheKey))
                {
                    return Optional.empty();
                }
                return Optional.of(prepareValidationMessage(rootUniqueAttribute, importParameters));
            }
            ItemModel foundUniqueValue = searchResult.getResult().get(0);
            context.put(cacheKey, foundUniqueValue);
        }
        return Optional.empty();
    }


    protected ValidationMessage prepareValidationMessage(RequiredAttribute rootUniqueAttribute, Map<String, String> importParameters)
    {
        Map<String, String> allAtomicParams = findAllAtomicParams(rootUniqueAttribute, importParameters);
        return new ValidationMessage("excel.import.validation.generic.translator.not.existing.item", new Serializable[] {getTypeCode(rootUniqueAttribute.getTypeModel()), allAtomicParams
                        .toString()});
    }


    private static String createCacheKey(String typeCode, RequiredAttribute rootUniqueAttribute, Map<String, String> params)
    {
        Collection<String> allAtomicParams = findAllAtomicParams(rootUniqueAttribute, params).values();
        return String.format("%s_%s", new Object[] {typeCode, String.join("_", (Iterable)allAtomicParams)});
    }


    private static Map<String, String> findAllAtomicParams(RequiredAttribute rootUniqueAttribute, Map<String, String> params)
    {
        Map<String, String> atomicParams = new LinkedHashMap<>();
        if(rootUniqueAttribute.getTypeModel() instanceof de.hybris.platform.core.model.type.AtomicTypeModel)
        {
            String key = String.format("%s.%s", new Object[] {rootUniqueAttribute.getEnclosingType(), rootUniqueAttribute.getQualifier()});
            atomicParams.put(key, params.get(key));
        }
        for(RequiredAttribute child : rootUniqueAttribute.getChildren())
        {
            atomicParams.putAll(findAllAtomicParams(child, params));
        }
        return atomicParams;
    }


    protected Optional<FlexibleSearchQuery> buildFlexibleSearchQuery(RequiredAttribute rootUniqueAttribute, Map<String, String> importParameters, Map context)
    {
        TypeModel typeModel = rootUniqueAttribute.getTypeModel();
        String typeCode = getTypeCode(typeModel);
        if(rootUniqueAttribute.getChildren().isEmpty())
        {
            return Optional.empty();
        }
        QueryBuilder queryBuilder = new QueryBuilder(typeCode);
        for(int i = 0; i < rootUniqueAttribute.getChildren().size(); i++)
        {
            RequiredAttribute child = rootUniqueAttribute.getChildren().get(i);
            String fullQualifier = String.format("%s.%s", new Object[] {child.getEnclosingType(), child.getQualifier()});
            if(child.getTypeModel() instanceof de.hybris.platform.core.model.type.AtomicTypeModel)
            {
                String value = (String)StringUtils.defaultIfEmpty(importParameters.get(fullQualifier), "");
                queryBuilder.withParam(child.getQualifier(), value);
            }
            else
            {
                String cacheKey = createCacheKey(getTypeCode(child.getTypeModel()), child, importParameters);
                Object foundObject = context.get(cacheKey);
                if(foundObject instanceof ItemModel)
                {
                    queryBuilder.withParam(child.getQualifier(), Long.valueOf(((ItemModel)foundObject).getPk().getLongValue()));
                }
            }
        }
        return Optional.of(new FlexibleSearchQuery(queryBuilder.build(), queryBuilder.params));
    }


    private static String getTypeCode(TypeModel typeModel)
    {
        if(typeModel instanceof CollectionTypeModel)
        {
            return ((CollectionTypeModel)typeModel).getElementType().getCode();
        }
        if(typeModel instanceof MapTypeModel)
        {
            return getTypeCode(((MapTypeModel)typeModel).getReturntype());
        }
        return typeModel.getCode();
    }


    public RequiredAttributesFactory getRequiredAttributesFactory()
    {
        return this.requiredAttributesFactory;
    }


    @Required
    public void setRequiredAttributesFactory(RequiredAttributesFactory requiredAttributesFactory)
    {
        this.requiredAttributesFactory = requiredAttributesFactory;
    }


    public FlexibleSearchService getFlexibleSearchService()
    {
        return this.flexibleSearchService;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    @Required
    public void setExcelWorkbookEntriesService(ExcelWorkbookEntriesService excelWorkbookEntriesService)
    {
        this.excelWorkbookEntriesService = excelWorkbookEntriesService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    protected TypeService getTypeService()
    {
        return this.typeService;
    }


    @Required
    public void setBlacklistedTypes(Collection<String> blacklistedTypes)
    {
        this.blacklistedTypes = blacklistedTypes;
    }
}
