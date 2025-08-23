package de.hybris.platform.mediaweb.assertions.assertj;

import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

public class Assertions extends Assertions
{
    public static HttpStatusAssert assertThat(HttpStatus actual)
    {
        return new HttpStatusAssert(actual);
    }


    public static HttpResponseAssert assertThat(HttpServletResponse actual)
    {
        return new HttpResponseAssert(actual);
    }
}
