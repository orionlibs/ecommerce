package de.hybris.platform.cockpit.util;

import de.hybris.platform.cockpit.components.CockpitFCKEditor;
import de.hybris.platform.util.HexUtils;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CockpitFCKConfigServlet extends HttpServlet
{
    private static final Logger LOG = LoggerFactory.getLogger(CockpitFCKConfigServlet.class);


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        if(request.getRequestURI().trim().endsWith("web/js/ext/FCKeditor/fckstyles.xml"))
        {
            String content = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n<Styles>\n\t<Style name=\"Marker: Yellow\" element=\"span\">\n\t\t<Style name=\"background-color\" value=\"Yellow\" />\n\t</Style>\n\t<Style name=\"Marker: Green\" element=\"span\">\n\t\t<Style name=\"background-color\" value=\"Lime\" />\n\t</Style>\n\n\t<Style name=\"Big\" element=\"big\" />\n\t<Style name=\"Small\" element=\"small\" />\n\t<Style name=\"Typewriter\" element=\"tt\" />\n\n\t<Style name=\"Computer Code\" element=\"code\" />\n\t<Style name=\"Keyboard Phrase\" element=\"kbd\" />\n\t<Style name=\"Sample Text\" element=\"samp\" />\n\t<Style name=\"Variable\" element=\"var\" />\n\n\t<Style name=\"Deleted Text\" element=\"del\" />\n\t<Style name=\"Inserted Text\" element=\"ins\" />\n\n\t<Style name=\"Cited Work\" element=\"cite\" />\n\t<Style name=\"Inline Quotation\" element=\"q\" />\n\n\t<Style name=\"Language: RTL\" element=\"span\">\n\t\t<Attribute name=\"dir\" value=\"rtl\" />\n\t</Style>\n\t<Style name=\"Language: LTR\" element=\"span\">\n\t\t<Attribute name=\"dir\" value=\"ltr\" />\n\t</Style>\n\t<Style name=\"Language: RTL Strong\" element=\"bdo\">\n\t\t<Attribute name=\"dir\" value=\"rtl\" />\n\t</Style>\n\t<Style name=\"Language: LTR Strong\" element=\"bdo\">\n\t\t<Attribute name=\"dir\" value=\"ltr\" />\n\t</Style>\n\n\t<!-- Object Styles -->\n\n\t<Style name=\"Image on Left\" element=\"img\">\n\t\t<Attribute name=\"style\" value=\"padding: 5px; margin-right: 5px\" />\n\t\t<Attribute name=\"border\" value=\"2\" />\n\t\t<Attribute name=\"align\" value=\"left\" />\n\t</Style>\n\t<Style name=\"Image on Right\" element=\"img\">\n\t\t<Attribute name=\"style\" value=\"padding: 5px; margin-left: 5px\" />\n\t\t<Attribute name=\"border\" value=\"2\" />\n\t\t<Attribute name=\"align\" value=\"right\" />\n\t</Style>\n</Styles>";
            response.getOutputStream()
                            .println("<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n<Styles>\n\t<Style name=\"Marker: Yellow\" element=\"span\">\n\t\t<Style name=\"background-color\" value=\"Yellow\" />\n\t</Style>\n\t<Style name=\"Marker: Green\" element=\"span\">\n\t\t<Style name=\"background-color\" value=\"Lime\" />\n\t</Style>\n\n\t<Style name=\"Big\" element=\"big\" />\n\t<Style name=\"Small\" element=\"small\" />\n\t<Style name=\"Typewriter\" element=\"tt\" />\n\n\t<Style name=\"Computer Code\" element=\"code\" />\n\t<Style name=\"Keyboard Phrase\" element=\"kbd\" />\n\t<Style name=\"Sample Text\" element=\"samp\" />\n\t<Style name=\"Variable\" element=\"var\" />\n\n\t<Style name=\"Deleted Text\" element=\"del\" />\n\t<Style name=\"Inserted Text\" element=\"ins\" />\n\n\t<Style name=\"Cited Work\" element=\"cite\" />\n\t<Style name=\"Inline Quotation\" element=\"q\" />\n\n\t<Style name=\"Language: RTL\" element=\"span\">\n\t\t<Attribute name=\"dir\" value=\"rtl\" />\n\t</Style>\n\t<Style name=\"Language: LTR\" element=\"span\">\n\t\t<Attribute name=\"dir\" value=\"ltr\" />\n\t</Style>\n\t<Style name=\"Language: RTL Strong\" element=\"bdo\">\n\t\t<Attribute name=\"dir\" value=\"rtl\" />\n\t</Style>\n\t<Style name=\"Language: LTR Strong\" element=\"bdo\">\n\t\t<Attribute name=\"dir\" value=\"ltr\" />\n\t</Style>\n\n\t<!-- Object Styles -->\n\n\t<Style name=\"Image on Left\" element=\"img\">\n\t\t<Attribute name=\"style\" value=\"padding: 5px; margin-right: 5px\" />\n\t\t<Attribute name=\"border\" value=\"2\" />\n\t\t<Attribute name=\"align\" value=\"left\" />\n\t</Style>\n\t<Style name=\"Image on Right\" element=\"img\">\n\t\t<Attribute name=\"style\" value=\"padding: 5px; margin-left: 5px\" />\n\t\t<Attribute name=\"border\" value=\"2\" />\n\t\t<Attribute name=\"align\" value=\"right\" />\n\t</Style>\n</Styles>");
        }
        else
        {
            String query = request.getQueryString();
            Object sessionDigests = request.getSession().getAttribute("de.hybris.platform.cockpit.components.CockpitFCKEditor[property]");
            if(sessionDigests instanceof Set)
            {
                Set<String> digests = (Set<String>)sessionDigests;
                if(CollectionUtils.isNotEmpty(digests))
                {
                    byte[] hexDigest = checkDigest(query, digests);
                    if(hexDigest != null)
                    {
                        response.getOutputStream().write(hexDigest);
                        return;
                    }
                }
            }
            LOG.error("request query invalid: " + query);
        }
    }


    private byte[] checkDigest(String query, Set<String> target)
    {
        try
        {
            byte[] source = HexUtils.convert(query);
            String digest = CockpitFCKEditor.createDigest(source, "SHA-256");
            if(target.contains(digest))
            {
                return source;
            }
            return null;
        }
        catch(NoSuchAlgorithmException ne)
        {
            LOG.error("SHA-256 not supported!");
            return null;
        }
        catch(IllegalArgumentException ie)
        {
            LOG.error("query illegal: " + ie.getMessage());
            return null;
        }
    }
}
