package de.hybris.platform.auditreport.service.impl.velocity;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSONEncodingInputStreamDirective extends Directive
{
    public static final String DIRECTIVE_NAME = "encodeJsonIS";
    public static final String AMP = "&";
    public static final String AMP_ENTITY = "&amp;";
    public static final String LT = "<";
    public static final String LT_ENTITY = "&lt;";
    public static final String GT = ">";
    public static final String GT_ENTITY = "&gt;";
    private static final Logger LOGGER = LoggerFactory.getLogger(JSONEncodingInputStreamDirective.class);


    public String getName()
    {
        return "encodeJsonIS";
    }


    public int getType()
    {
        return 2;
    }


    public boolean render(InternalContextAdapter context, Writer writer, Node node) throws IOException, MethodInvocationException
    {
        Object is = node.jjtGetChild(0).value(context);
        if(is instanceof InputStream)
        {
            InputStream inputStream = (InputStream)is;
            Object encodeObj = node.jjtGetChild(1).value(context);
            boolean shouldEncode = (encodeObj == null || Boolean.TRUE.equals(encodeObj));
            List<String> lines = IOUtils.readLines(inputStream, StandardCharsets.UTF_8);
            for(String line : lines)
            {
                String replaceAll = shouldEncode ? encodeHTMLEntities(line) : line;
                IOUtils.write(replaceAll, writer);
            }
            return true;
        }
        LOGGER.warn("The directive can only handle {} instances", InputStream.class.getName());
        return false;
    }


    protected String encodeHTMLEntities(String text)
    {
        return text.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
    }
}
