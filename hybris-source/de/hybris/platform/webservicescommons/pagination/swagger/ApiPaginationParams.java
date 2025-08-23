package de.hybris.platform.webservicescommons.pagination.swagger;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ApiImplicitParams({@ApiImplicitParam(name = "pageSize", value = "Page size", required = false, dataType = "int", paramType = "query"), @ApiImplicitParam(name = "currentPage", value = "Current page number", required = false, dataType = "int", paramType = "query"),
                @ApiImplicitParam(name = "needsTotal", value = "Request total count", required = false, dataType = "boolean", paramType = "query"), @ApiImplicitParam(name = "sort", value = "Attribute sorting", required = false, dataType = "string", paramType = "query"),
                @ApiImplicitParam(name = "asc", value = "Define sorting order", required = false, dataType = "boolean", paramType = "query")})
public @interface ApiPaginationParams
{
}
