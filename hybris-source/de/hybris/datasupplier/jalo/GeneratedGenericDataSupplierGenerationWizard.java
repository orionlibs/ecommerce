package de.hybris.datasupplier.jalo;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.media.Media;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedGenericDataSupplierGenerationWizard extends GenericItem
{
    public static final String DATAHUB = "dataHub";
    public static final String EMS = "ems";
    public static final String SBG = "sbg";
    public static final String SAVEPAYLOAD = "savePayload";
    public static final String SUPPLYDELAY = "supplyDelay";
    public static final String DATAHUBPROPERTIESFILE = "datahubPropertiesFile";
    public static final String DATAHUBWEBAPP = "datahubWebapp";
    public static final String DATAHUBWEBAPPXML = "datahubWebappXml";
    public static final String EMSWEBAPP = "emsWebapp";
    public static final String EMSPOMFILE = "emsPomFile";
    public static final String SBGWEBAPP = "sbgWebapp";
    public static final String SBGPOMFILE = "sbgPomFile";
    public static final String SLDREGLOCATION = "sldregLocation";
    public static final String SLDREGCONFIGLOCATION = "sldregConfigLocation";
    public static final String SYSTEMID = "systemId";
    public static final String GENERATEDWAR = "generatedWar";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("dataHub", Item.AttributeMode.INITIAL);
        tmp.put("ems", Item.AttributeMode.INITIAL);
        tmp.put("sbg", Item.AttributeMode.INITIAL);
        tmp.put("savePayload", Item.AttributeMode.INITIAL);
        tmp.put("supplyDelay", Item.AttributeMode.INITIAL);
        tmp.put("datahubPropertiesFile", Item.AttributeMode.INITIAL);
        tmp.put("datahubWebapp", Item.AttributeMode.INITIAL);
        tmp.put("datahubWebappXml", Item.AttributeMode.INITIAL);
        tmp.put("emsWebapp", Item.AttributeMode.INITIAL);
        tmp.put("emsPomFile", Item.AttributeMode.INITIAL);
        tmp.put("sbgWebapp", Item.AttributeMode.INITIAL);
        tmp.put("sbgPomFile", Item.AttributeMode.INITIAL);
        tmp.put("sldregLocation", Item.AttributeMode.INITIAL);
        tmp.put("sldregConfigLocation", Item.AttributeMode.INITIAL);
        tmp.put("systemId", Item.AttributeMode.INITIAL);
        tmp.put("generatedWar", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Boolean isDataHub(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "dataHub");
    }


    public Boolean isDataHub()
    {
        return isDataHub(getSession().getSessionContext());
    }


    public boolean isDataHubAsPrimitive(SessionContext ctx)
    {
        Boolean value = isDataHub(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isDataHubAsPrimitive()
    {
        return isDataHubAsPrimitive(getSession().getSessionContext());
    }


    public void setDataHub(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "dataHub", value);
    }


    public void setDataHub(Boolean value)
    {
        setDataHub(getSession().getSessionContext(), value);
    }


    public void setDataHub(SessionContext ctx, boolean value)
    {
        setDataHub(ctx, Boolean.valueOf(value));
    }


    public void setDataHub(boolean value)
    {
        setDataHub(getSession().getSessionContext(), value);
    }


    public String getDatahubPropertiesFile(SessionContext ctx)
    {
        return (String)getProperty(ctx, "datahubPropertiesFile");
    }


    public String getDatahubPropertiesFile()
    {
        return getDatahubPropertiesFile(getSession().getSessionContext());
    }


    public void setDatahubPropertiesFile(SessionContext ctx, String value)
    {
        setProperty(ctx, "datahubPropertiesFile", value);
    }


    public void setDatahubPropertiesFile(String value)
    {
        setDatahubPropertiesFile(getSession().getSessionContext(), value);
    }


    public String getDatahubWebapp(SessionContext ctx)
    {
        return (String)getProperty(ctx, "datahubWebapp");
    }


    public String getDatahubWebapp()
    {
        return getDatahubWebapp(getSession().getSessionContext());
    }


    public void setDatahubWebapp(SessionContext ctx, String value)
    {
        setProperty(ctx, "datahubWebapp", value);
    }


    public void setDatahubWebapp(String value)
    {
        setDatahubWebapp(getSession().getSessionContext(), value);
    }


    public String getDatahubWebappXml(SessionContext ctx)
    {
        return (String)getProperty(ctx, "datahubWebappXml");
    }


    public String getDatahubWebappXml()
    {
        return getDatahubWebappXml(getSession().getSessionContext());
    }


    public void setDatahubWebappXml(SessionContext ctx, String value)
    {
        setProperty(ctx, "datahubWebappXml", value);
    }


    public void setDatahubWebappXml(String value)
    {
        setDatahubWebappXml(getSession().getSessionContext(), value);
    }


    public Boolean isEms(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "ems");
    }


    public Boolean isEms()
    {
        return isEms(getSession().getSessionContext());
    }


    public boolean isEmsAsPrimitive(SessionContext ctx)
    {
        Boolean value = isEms(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isEmsAsPrimitive()
    {
        return isEmsAsPrimitive(getSession().getSessionContext());
    }


    public void setEms(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "ems", value);
    }


    public void setEms(Boolean value)
    {
        setEms(getSession().getSessionContext(), value);
    }


    public void setEms(SessionContext ctx, boolean value)
    {
        setEms(ctx, Boolean.valueOf(value));
    }


    public void setEms(boolean value)
    {
        setEms(getSession().getSessionContext(), value);
    }


    public String getEmsPomFile(SessionContext ctx)
    {
        return (String)getProperty(ctx, "emsPomFile");
    }


    public String getEmsPomFile()
    {
        return getEmsPomFile(getSession().getSessionContext());
    }


    public void setEmsPomFile(SessionContext ctx, String value)
    {
        setProperty(ctx, "emsPomFile", value);
    }


    public void setEmsPomFile(String value)
    {
        setEmsPomFile(getSession().getSessionContext(), value);
    }


    public String getEmsWebapp(SessionContext ctx)
    {
        return (String)getProperty(ctx, "emsWebapp");
    }


    public String getEmsWebapp()
    {
        return getEmsWebapp(getSession().getSessionContext());
    }


    public void setEmsWebapp(SessionContext ctx, String value)
    {
        setProperty(ctx, "emsWebapp", value);
    }


    public void setEmsWebapp(String value)
    {
        setEmsWebapp(getSession().getSessionContext(), value);
    }


    public Media getGeneratedWar(SessionContext ctx)
    {
        return (Media)getProperty(ctx, "generatedWar");
    }


    public Media getGeneratedWar()
    {
        return getGeneratedWar(getSession().getSessionContext());
    }


    public void setGeneratedWar(SessionContext ctx, Media value)
    {
        setProperty(ctx, "generatedWar", value);
    }


    public void setGeneratedWar(Media value)
    {
        setGeneratedWar(getSession().getSessionContext(), value);
    }


    public Boolean isSavePayload(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "savePayload");
    }


    public Boolean isSavePayload()
    {
        return isSavePayload(getSession().getSessionContext());
    }


    public boolean isSavePayloadAsPrimitive(SessionContext ctx)
    {
        Boolean value = isSavePayload(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isSavePayloadAsPrimitive()
    {
        return isSavePayloadAsPrimitive(getSession().getSessionContext());
    }


    public void setSavePayload(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "savePayload", value);
    }


    public void setSavePayload(Boolean value)
    {
        setSavePayload(getSession().getSessionContext(), value);
    }


    public void setSavePayload(SessionContext ctx, boolean value)
    {
        setSavePayload(ctx, Boolean.valueOf(value));
    }


    public void setSavePayload(boolean value)
    {
        setSavePayload(getSession().getSessionContext(), value);
    }


    public Boolean isSbg(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "sbg");
    }


    public Boolean isSbg()
    {
        return isSbg(getSession().getSessionContext());
    }


    public boolean isSbgAsPrimitive(SessionContext ctx)
    {
        Boolean value = isSbg(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isSbgAsPrimitive()
    {
        return isSbgAsPrimitive(getSession().getSessionContext());
    }


    public void setSbg(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "sbg", value);
    }


    public void setSbg(Boolean value)
    {
        setSbg(getSession().getSessionContext(), value);
    }


    public void setSbg(SessionContext ctx, boolean value)
    {
        setSbg(ctx, Boolean.valueOf(value));
    }


    public void setSbg(boolean value)
    {
        setSbg(getSession().getSessionContext(), value);
    }


    public String getSbgPomFile(SessionContext ctx)
    {
        return (String)getProperty(ctx, "sbgPomFile");
    }


    public String getSbgPomFile()
    {
        return getSbgPomFile(getSession().getSessionContext());
    }


    public void setSbgPomFile(SessionContext ctx, String value)
    {
        setProperty(ctx, "sbgPomFile", value);
    }


    public void setSbgPomFile(String value)
    {
        setSbgPomFile(getSession().getSessionContext(), value);
    }


    public String getSbgWebapp(SessionContext ctx)
    {
        return (String)getProperty(ctx, "sbgWebapp");
    }


    public String getSbgWebapp()
    {
        return getSbgWebapp(getSession().getSessionContext());
    }


    public void setSbgWebapp(SessionContext ctx, String value)
    {
        setProperty(ctx, "sbgWebapp", value);
    }


    public void setSbgWebapp(String value)
    {
        setSbgWebapp(getSession().getSessionContext(), value);
    }


    public String getSldregConfigLocation(SessionContext ctx)
    {
        return (String)getProperty(ctx, "sldregConfigLocation");
    }


    public String getSldregConfigLocation()
    {
        return getSldregConfigLocation(getSession().getSessionContext());
    }


    public void setSldregConfigLocation(SessionContext ctx, String value)
    {
        setProperty(ctx, "sldregConfigLocation", value);
    }


    public void setSldregConfigLocation(String value)
    {
        setSldregConfigLocation(getSession().getSessionContext(), value);
    }


    public String getSldregLocation(SessionContext ctx)
    {
        return (String)getProperty(ctx, "sldregLocation");
    }


    public String getSldregLocation()
    {
        return getSldregLocation(getSession().getSessionContext());
    }


    public void setSldregLocation(SessionContext ctx, String value)
    {
        setProperty(ctx, "sldregLocation", value);
    }


    public void setSldregLocation(String value)
    {
        setSldregLocation(getSession().getSessionContext(), value);
    }


    public Long getSupplyDelay(SessionContext ctx)
    {
        return (Long)getProperty(ctx, "supplyDelay");
    }


    public Long getSupplyDelay()
    {
        return getSupplyDelay(getSession().getSessionContext());
    }


    public long getSupplyDelayAsPrimitive(SessionContext ctx)
    {
        Long value = getSupplyDelay(ctx);
        return (value != null) ? value.longValue() : 0L;
    }


    public long getSupplyDelayAsPrimitive()
    {
        return getSupplyDelayAsPrimitive(getSession().getSessionContext());
    }


    public void setSupplyDelay(SessionContext ctx, Long value)
    {
        setProperty(ctx, "supplyDelay", value);
    }


    public void setSupplyDelay(Long value)
    {
        setSupplyDelay(getSession().getSessionContext(), value);
    }


    public void setSupplyDelay(SessionContext ctx, long value)
    {
        setSupplyDelay(ctx, Long.valueOf(value));
    }


    public void setSupplyDelay(long value)
    {
        setSupplyDelay(getSession().getSessionContext(), value);
    }


    public String getSystemId(SessionContext ctx)
    {
        return (String)getProperty(ctx, "systemId");
    }


    public String getSystemId()
    {
        return getSystemId(getSession().getSessionContext());
    }


    public void setSystemId(SessionContext ctx, String value)
    {
        setProperty(ctx, "systemId", value);
    }


    public void setSystemId(String value)
    {
        setSystemId(getSession().getSessionContext(), value);
    }
}
