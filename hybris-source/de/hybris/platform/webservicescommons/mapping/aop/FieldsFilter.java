package de.hybris.platform.webservicescommons.mapping.aop;

import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.mapping.FieldSetBuilder;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;

public class FieldsFilter
{
    private final HttpServletRequest request;
    private final DataMapper dataMapper;
    private final FieldSetBuilder fieldSetBuilder;
    private boolean disableFullMapping = false;


    public FieldsFilter(HttpServletRequest request, DataMapper dataMapper, FieldSetBuilder fieldSetBuilder)
    {
        this.request = request;
        this.dataMapper = dataMapper;
        this.fieldSetBuilder = fieldSetBuilder;
    }


    public Object filterFields(ProceedingJoinPoint joinPoint) throws Throwable
    {
        Object result = joinPoint.proceed(joinPoint.getArgs());
        result = process(result);
        return result;
    }


    protected Object process(Object result)
    {
        if(result == null)
        {
            return null;
        }
        String fields = getFieldsetLevel();
        if(this.disableFullMapping && "FULL".equals(fields))
        {
            return result;
        }
        Set<String> fieldSet = this.fieldSetBuilder.createFieldSet(result.getClass(), "destination", fields);
        return this.dataMapper.map(result, result.getClass(), fieldSet);
    }


    protected String getFieldsetLevel()
    {
        String parameter = this.request.getParameter("fields");
        return StringUtils.isNotEmpty(parameter) ? parameter : "DEFAULT";
    }


    public void setDisableFullMapping(boolean disableFullMapping)
    {
        this.disableFullMapping = disableFullMapping;
    }


    protected DataMapper getDataMapper()
    {
        return this.dataMapper;
    }


    protected FieldSetBuilder getFieldSetBuilder()
    {
        return this.fieldSetBuilder;
    }


    protected HttpServletRequest getRequest()
    {
        return this.request;
    }


    protected boolean isDisableFullMapping()
    {
        return this.disableFullMapping;
    }
}
