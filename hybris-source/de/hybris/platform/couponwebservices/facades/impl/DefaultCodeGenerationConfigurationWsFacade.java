package de.hybris.platform.couponwebservices.facades.impl;

import de.hybris.platform.core.servicelayer.data.PaginationData;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.core.servicelayer.data.SortData;
import de.hybris.platform.couponservices.dao.CodeGenerationConfigurationDao;
import de.hybris.platform.couponservices.model.CodeGenerationConfigurationModel;
import de.hybris.platform.couponwebservices.CodeGenerationConfigurationNotFoundException;
import de.hybris.platform.couponwebservices.dto.CodeGenerationConfigurationWsDTO;
import de.hybris.platform.couponwebservices.facades.CodeGenerationConfigurationWsFacade;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.search.paginated.dao.PaginatedGenericDao;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.cache.annotation.Cacheable;

public class DefaultCodeGenerationConfigurationWsFacade implements CodeGenerationConfigurationWsFacade
{
    private Converter<CodeGenerationConfigurationModel, CodeGenerationConfigurationWsDTO> codeGenerationConfigurationWsDTOConverter;
    private CodeGenerationConfigurationDao codeGenerationConfigurationDao;
    private PaginatedGenericDao<CodeGenerationConfigurationModel> codeGenerationConfigurationPaginatedGenericDao;


    public SearchPageData<CodeGenerationConfigurationWsDTO> getCodeGenerationConfigurations(PaginationData paginationData, List<SortData> sortData)
    {
        SearchPageData<CodeGenerationConfigurationModel> searchPageData = new SearchPageData();
        searchPageData.setPagination(paginationData);
        searchPageData.setSorts(sortData);
        SearchPageData<CodeGenerationConfigurationModel> codeGenerationConfigurationSearchPageData = getCodeGenerationConfigurationPaginatedGenericDao().find(searchPageData);
        if(CollectionUtils.isEmpty(codeGenerationConfigurationSearchPageData.getResults()))
        {
            throw new CodeGenerationConfigurationNotFoundException("No Code Generation Configurations found on the System", "No Records");
        }
        return convertSearchResults(codeGenerationConfigurationSearchPageData);
    }


    protected SearchPageData<CodeGenerationConfigurationWsDTO> convertSearchResults(SearchPageData<CodeGenerationConfigurationModel> source)
    {
        SearchPageData<CodeGenerationConfigurationWsDTO> result = new SearchPageData();
        result.setPagination(source.getPagination());
        result.setSorts(source.getSorts());
        Objects.requireNonNull(getCodeGenerationConfigurationWsDTOConverter());
        result.setResults((List)source.getResults().stream().map(getCodeGenerationConfigurationWsDTOConverter()::convert).collect(Collectors.toList()));
        return result;
    }


    @Cacheable(value = {"codeGenerationConfigurationWsCache"}, key = "T(de.hybris.platform.webservicescommons.cache.CacheKeyGenerator).generateKey(false,false,'getCodeGenerationConfigurationWsDTO',#value)")
    public CodeGenerationConfigurationWsDTO getCodeGenerationConfigurationWsDTO(String codeGenerationConfigurationName)
    {
        CodeGenerationConfigurationModel codeGenerationConfigurationModel = (CodeGenerationConfigurationModel)getCodeGenerationConfigurationDao().findCodeGenerationConfigurationByName(codeGenerationConfigurationName)
                        .orElseThrow(() -> new CodeGenerationConfigurationNotFoundException("No Code Generation Configuration found for name [" + codeGenerationConfigurationName + "]", "invalid", "codeGenerationConfiguration"));
        return (CodeGenerationConfigurationWsDTO)getCodeGenerationConfigurationWsDTOConverter().convert(codeGenerationConfigurationModel);
    }


    protected Converter<CodeGenerationConfigurationModel, CodeGenerationConfigurationWsDTO> getCodeGenerationConfigurationWsDTOConverter()
    {
        return this.codeGenerationConfigurationWsDTOConverter;
    }


    @Required
    public void setCodeGenerationConfigurationWsDTOConverter(Converter<CodeGenerationConfigurationModel, CodeGenerationConfigurationWsDTO> codeGenerationConfigurationWsDTOConverter)
    {
        this.codeGenerationConfigurationWsDTOConverter = codeGenerationConfigurationWsDTOConverter;
    }


    protected CodeGenerationConfigurationDao getCodeGenerationConfigurationDao()
    {
        return this.codeGenerationConfigurationDao;
    }


    @Required
    public void setCodeGenerationConfigurationDao(CodeGenerationConfigurationDao codeGenerationConfigurationDao)
    {
        this.codeGenerationConfigurationDao = codeGenerationConfigurationDao;
    }


    protected PaginatedGenericDao<CodeGenerationConfigurationModel> getCodeGenerationConfigurationPaginatedGenericDao()
    {
        return this.codeGenerationConfigurationPaginatedGenericDao;
    }


    @Required
    public void setCodeGenerationConfigurationPaginatedGenericDao(PaginatedGenericDao<CodeGenerationConfigurationModel> codeGenerationConfigurationPaginatedGenericDao)
    {
        this.codeGenerationConfigurationPaginatedGenericDao = codeGenerationConfigurationPaginatedGenericDao;
    }
}
