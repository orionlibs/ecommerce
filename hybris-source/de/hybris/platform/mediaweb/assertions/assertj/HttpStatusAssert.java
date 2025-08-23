package de.hybris.platform.mediaweb.assertions.assertj;

import org.assertj.core.api.AbstractObjectAssert;
import org.springframework.http.HttpStatus;

public class HttpStatusAssert extends AbstractObjectAssert<HttpStatusAssert, HttpStatus>
{
    public HttpStatusAssert(HttpStatus actual)
    {
        super(actual, HttpStatusAssert.class);
    }


    public HttpStatusAssert is1xxInformational()
    {
        isNotNull();
        if(!((HttpStatus)this.actual).is1xxInformational())
        {
            failWithMessage("expected %s to be 1xx", new Object[] {this.actual});
        }
        return this;
    }


    public HttpStatusAssert is2xxSuccessful()
    {
        isNotNull();
        if(!((HttpStatus)this.actual).is2xxSuccessful())
        {
            failWithMessage("expected %s to be 2xx", new Object[] {this.actual});
        }
        return this;
    }


    public HttpStatusAssert is3xxRedirection()
    {
        isNotNull();
        if(!((HttpStatus)this.actual).is3xxRedirection())
        {
            failWithMessage("expected %s to be 3xx", new Object[] {this.actual});
        }
        return this;
    }


    public HttpStatusAssert is4xxClientError()
    {
        isNotNull();
        if(!((HttpStatus)this.actual).is4xxClientError())
        {
            failWithMessage("expected %s to be 4xx", new Object[] {this.actual});
        }
        return this;
    }


    public HttpStatusAssert is5xxServerError()
    {
        isNotNull();
        if(!((HttpStatus)this.actual).is5xxServerError())
        {
            failWithMessage("expected %s to be 5xx", new Object[] {this.actual});
        }
        return this;
    }


    public HttpStatusAssert isEqualTo(int expected)
    {
        return (HttpStatusAssert)isEqualTo(HttpStatus.resolve(expected));
    }
}
