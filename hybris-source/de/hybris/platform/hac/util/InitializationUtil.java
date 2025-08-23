package de.hybris.platform.hac.util;

import de.hybris.platform.hac.data.dto.BeautifulInitializationData;
import de.hybris.platform.util.JspContext;
import java.io.StringWriter;
import java.io.Writer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspWriter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockJspWriter;

public class InitializationUtil
{
    public static JspContext createMockJspContext(BeautifulInitializationData data)
    {
        return createMockJspContext(data, new StringWriter());
    }


    public static JspContext createMockJspContext(BeautifulInitializationData data, Writer targetWriter)
    {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        MockJspWriter mockJspWriter = new MockJspWriter(targetWriter);
        mockRequest(data, mockRequest);
        return new JspContext((JspWriter)mockJspWriter, (HttpServletRequest)mockRequest, (HttpServletResponse)mockHttpServletResponse);
    }


    public static JspContext createMockJspContextWithMockRequest(MockHttpServletRequest mockRequest, Writer writer)
    {
        MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        MockJspWriter mockJspWriter = new MockJspWriter(writer);
        return new JspContext((JspWriter)mockJspWriter, (HttpServletRequest)mockRequest, (HttpServletResponse)mockHttpServletResponse);
    }


    public static void mockRequest(BeautifulInitializationData data, MockHttpServletRequest request)
    {
        Object object = new Object(request);
        data.traverse((BeautifulInitializationData.InitUpdateConfigWalker)object);
    }
}
