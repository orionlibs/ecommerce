package de.hybris.platform.cockpit.components;

import de.hybris.platform.util.HexUtils;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkforge.fckez.FCKeditor;
import org.zkoss.zk.ui.Sessions;

public class CockpitFCKEditor extends FCKeditor
{
    private static final Logger LOG = LoggerFactory.getLogger(CockpitFCKEditor.class);
    private final Map<String, String> attributes = new HashMap<>();
    private static final String FCK_CONFIG_PATH = "/cockpit/customcomponents/cockpit-fck-config.js";
    private static final String GUILANGUAGE = "FCKConfig.DefaultLanguage";
    private static final String TOOLBARICONS = "FCKConfig.ToolbarSets[\"Default\"]";
    private String customConfigUrl = "/cockpit/customcomponents/cockpit-fck-config.js";
    public static final String COCKPIT_FCK_EDITOR_PROPERTY = "de.hybris.platform.cockpit.components.CockpitFCKEditor[property]";


    public CockpitFCKEditor(String langIso, String spellCheckerScript, Skin skin)
    {
        if(langIso == null)
        {
            this.attributes.put("FCKConfig.AutoDetectLanguage", "true");
        }
        else
        {
            this.attributes.put("FCKConfig.AutoDetectLanguage", "false");
            setGuiLanguage(langIso, false);
        }
        if(spellCheckerScript != null)
        {
            this.attributes.put("FCKConfig.SpellChecker", "'SpellerPages'");
            this.attributes.put("FCKConfig.SpellerPagesServerScript", "'" + spellCheckerScript + "'");
        }
        setToolbarIcons("[\n['Source','Preview','ShowBlocks','Print', 'Undo','Redo'," + (
                                        (spellCheckerScript != null) ? "'-','SpellCheck'," : "")
                                        + "'-','Find','Replace','-','SelectAll','RemoveFormat','-','Subscript','Superscript','-','Outdent','Indent','Blockquote'],\n'/',\n['Bold','Italic','Underline','StrikeThrough','-','JustifyLeft','JustifyCenter','JustifyRight','JustifyFull','-','OrderedList','UnorderedList','-','Link','Unlink','Anchor','-','Table','Rule','SpecialChar','PageBreak'],\n'/',\n['FontFormat','FontName','FontSize']\n]",
                        false);
        if(skin.equals(Skin.SILVER))
        {
            this.attributes.put("FCKConfig.SkinPath", "FCKConfig.BasePath + 'skins/silver/'");
        }
        applySettings();
    }


    public void addEditorAttribute(String attributeKey, String attributeValue)
    {
        this.attributes.put(attributeKey, attributeValue);
    }


    public void deleteEditorAttribute(String attributeKey)
    {
        if(this.attributes.containsKey(attributeKey))
        {
            this.attributes.remove(attributeKey);
        }
    }


    public Map<String, String> getEditorAttributes()
    {
        return Collections.unmodifiableMap(this.attributes);
    }


    public void applySettings()
    {
        if(this.customConfigUrl != null)
        {
            String property = createPropertyString();
            if(StringUtils.isNotEmpty(property))
            {
                super.setCustomConfigurationsPath(this.customConfigUrl + "?" + this.customConfigUrl);
            }
            else
            {
                super.setCustomConfigurationsPath(null);
            }
        }
        else
        {
            super.setCustomConfigurationsPath(null);
        }
    }


    public void setGuiLanguage(String isocode)
    {
        setGuiLanguage(isocode, true);
    }


    protected void setGuiLanguage(String isocode, boolean apply)
    {
        this.attributes.put("FCKConfig.DefaultLanguage", "'" + isocode + "'");
        if(apply)
        {
            applySettings();
        }
    }


    public String getGuiLanguage()
    {
        return this.attributes.get("FCKConfig.DefaultLanguage");
    }


    public void setToolbarIcons(String iconConfig)
    {
        setToolbarIcons(iconConfig, true);
    }


    protected void setToolbarIcons(String iconConfig, boolean apply)
    {
        if(iconConfig == null)
        {
            LOG.warn("Can not set toolbar configuration to null.");
        }
        else
        {
            this.attributes.put("FCKConfig.ToolbarSets[\"Default\"]", iconConfig.trim());
            if(apply)
            {
                applySettings();
            }
        }
    }


    public String getToolbarIcons()
    {
        return this.attributes.get("FCKConfig.ToolbarSets[\"Default\"]");
    }


    private String createPropertyString()
    {
        StringBuilder query = new StringBuilder();
        for(Map.Entry<String, String> entry : this.attributes.entrySet())
        {
            query.append((String)entry.getKey() + " = " + (String)entry.getKey() + ";\n");
        }
        try
        {
            byte[] queryBytes = query.toString().getBytes();
            Object nativeSession = Sessions.getCurrent().getNativeSession();
            if(nativeSession instanceof HttpSession)
            {
                Set<String> digests = getSessionDigests((HttpSession)nativeSession);
                String digestMessage = createDigest(queryBytes, "SHA-256");
                digests.add(digestMessage);
                String encodedKey = HexUtils.convert(queryBytes);
                return encodedKey;
            }
            LOG.warn("not inside http session context!");
            return "";
        }
        catch(Exception e)
        {
            LOG.error("cannot create property string: " + e.getMessage());
            return "";
        }
    }


    public static String createDigest(byte[] source, String algorithm) throws NoSuchAlgorithmException
    {
        MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
        byte[] digest = messageDigest.digest(source);
        StringBuilder digestMessage = new StringBuilder();
        for(int i = 0; i < digest.length; i++)
        {
            digestMessage.append(Integer.toHexString(0xFF & digest[i]));
        }
        return digestMessage.toString();
    }


    private Set<String> getSessionDigests(HttpSession httpSession)
    {
        Set<String> digests;
        Object sessionDigests = httpSession.getAttribute("de.hybris.platform.cockpit.components.CockpitFCKEditor[property]");
        if(sessionDigests instanceof Set)
        {
            digests = (Set<String>)sessionDigests;
        }
        else
        {
            digests = new HashSet<>();
            httpSession.setAttribute("de.hybris.platform.cockpit.components.CockpitFCKEditor[property]", digests);
        }
        return digests;
    }


    public void setCustomConfigurationsPath(String url)
    {
        this.customConfigUrl = url;
        super.setCustomConfigurationsPath(url);
    }
}
