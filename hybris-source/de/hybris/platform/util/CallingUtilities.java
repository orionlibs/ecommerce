package de.hybris.platform.util;

import de.hybris.platform.core.Registry;
import de.hybris.platform.licence.Licence;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javolution.xml.XMLBinding;
import javolution.xml.XMLObjectWriter;
import javolution.xml.stream.XMLStreamException;

public class CallingUtilities
{
    private static final String OVERRIDE_REQUEST_SCHEME = "override.request.scheme";


    public static String getInfoAddress(HttpServletRequest req)
    {
        StringBuilder b = new StringBuilder();
        String overrideScheme = Config.getParameter("override.request.scheme");
        b.append((overrideScheme != null && !"".equals(overrideScheme)) ? overrideScheme : req.getScheme());
        b.append("://www.hybris.de/getinfo.jsp");
        b.append("?info=" + getBase64InfoMap());
        b.append("&server=" + req.getServerName());
        return b.toString();
    }


    public static String getBase64InfoMap()
    {
        try
        {
            XMLBinding binding = new XMLBinding();
            binding.setAlias(HashMap.class, "Map");
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            XMLObjectWriter writer = (new XMLObjectWriter()).setOutput(out).setBinding(binding);
            writer.write(getInfoMap(), "PlatformInfo");
            writer.close();
            return Base64.encodeBytes(out.toByteArray(), 8);
        }
        catch(XMLStreamException e)
        {
            return "unabletowrite=" + e.getMessage();
        }
    }


    public static Map getInfoMap()
    {
        Map<Object, Object> map = new HashMap<>();
        map.put("buildver", Config.getParameter("build.version"));
        map.put("buildnumber", Config.getParameter("build.number"));
        map.put("licencesig", Base64.encodeBytes(Licence.getDefaultLicence().getSignature(), 8));
        map.put("clusterid", Config.getParameter(Config.Params.CLUSTER_ID));
        map.put("tenants", Integer.valueOf(Registry.getMasterTenant().getSlaveTenantIDs().size() + 1));
        map.put("availableprocessors", Integer.valueOf(Runtime.getRuntime().availableProcessors()));
        map.put("cachemax", Integer.valueOf(Registry.getCurrentTenant().getCache().getMaxAllowedSize()));
        map.put("licenceid", Licence.getDefaultLicence().getID().trim());
        return map;
    }
}
