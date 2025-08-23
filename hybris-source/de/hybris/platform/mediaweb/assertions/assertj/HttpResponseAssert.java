package de.hybris.platform.mediaweb.assertions.assertj;

import java.util.Objects;
import java.util.function.Function;
import javax.servlet.http.HttpServletResponse;
import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.Assertions;
import org.springframework.http.HttpStatus;

public class HttpResponseAssert extends AbstractObjectAssert<HttpResponseAssert, HttpServletResponse>
{
    public HttpResponseAssert(HttpServletResponse actual)
    {
        super(actual, HttpResponseAssert.class);
    }


    public HttpStatusAssert status()
    {
        Assertions.assertThat(this.actual).isNotNull();
        return new HttpStatusAssert(HttpStatus.valueOf(((HttpServletResponse)this.actual).getStatus()));
    }


    public HttpResponseAssert headerIsEqualTo(String header, String value)
    {
        ((AbstractObjectAssert)Assertions.assertThat(this.actual).isNotNull()).extracting(new Function[] {s -> s.getHeader(header)}).isEqualTo(value);
        return this;
    }


    public HttpResponseAssert headerContains(String header, String... values)
    {
        ((AbstractObjectAssert)Assertions.assertThat(this.actual).isNotNull()).extracting(new Function[] {s -> s.getHeaders(header)}).contains((Object[])Objects.requireNonNull(values));
        return this;
    }


    public HttpResponseAssert headerContainsOnly(String header, String... values)
    {
        ((AbstractObjectAssert)Assertions.assertThat(this.actual).isNotNull()).extracting(new Function[] {s -> s.getHeaders(header)}).containsOnly((Object[])Objects.requireNonNull(values));
        return this;
    }


    public HttpResponseAssert hasContentType(String contentType)
    {
        Assertions.assertThat(((HttpServletResponse)this.actual).getContentType()).isEqualToIgnoringCase(contentType);
        return this;
    }
}
