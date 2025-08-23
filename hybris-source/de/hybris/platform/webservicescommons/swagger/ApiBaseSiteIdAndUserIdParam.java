package de.hybris.platform.webservicescommons.swagger;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ApiImplicitParams({@ApiImplicitParam(name = "baseSiteId", value = "Base site identifier", required = true, dataType = "String", paramType = "path"),
                @ApiImplicitParam(name = "userId", value = "User identifier or one of the literals : 'current' for currently authenticated user, 'anonymous' for anonymous user", required = true, dataType = "String", paramType = "path")})
public @interface ApiBaseSiteIdAndUserIdParam
{
}
