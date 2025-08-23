package de.hybris.platform.hmc.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.util.config.ConfigIntf;

public class ConfigConstants
{
    private final ConfigIntf config = Registry.getMasterTenant().getConfig();
    public int BUFFERSIZE = this.config.getInt("hmc.buffersize", 512);
    public boolean CASEINSENSITIVESTRINGSEARCH = this.config.getBoolean("hmc.caseinsensitivestringsearch", true);
    public String CONTENTTYPE = this.config.getString("hmc.contenttype", "text/html");
    public boolean DEBUG_SHOWJSPCOMMENTS = this.config.getBoolean("hmc.debug.showjspcomments", false);
    public boolean DEFAULT_AUTOLOGIN = this.config.getBoolean("hmc.default.autologin", false);
    public int DEFAULT_COLUMNLAYOUT_COLUMNWIDTH = this.config.getInt("hmc.default.columnlayout.columnwidth", 371);
    public int DEFAULT_LABELWIDTH = this.config.getInt("hmc.default.labelwidth", 100);
    public String DEFAULT_LOGIN = this.config.getString("hmc.default.login", DEFAULTS.DEFAULT_LOGIN);
    public String DEFAULT_PASSWORD = this.config.getString("hmc.default.password", DEFAULTS.DEFAULT_PASSWORD);
    public int DEFAULT_TABLE_COLUMNWIDTH = this.config.getInt("hmc.default.table.columnwidth", 100);
    public int DEFAULT_VALUEWIDTH = this.config.getInt("hmc.default.valuewidth", 224);
    public boolean DEVELOPERMODE = this.config.getBoolean("hmc.developermode", false);
    public boolean ENABLE_SCROLLBAR = this.config.getBoolean("hmc.enable.scrollbar", true);
    public String ENCODING = this.config.getString("hmc.encoding", "UTF-8");
    public boolean HTML_ESCAPE = this.config.getBoolean("hmc.html.escape", true);
    public boolean REDIRECT_ABSOLUTE = this.config.getBoolean("hmc.redirect.absolute", false);
    public String RESULT_RANGES = this.config.getString("hmc.result.ranges", "20,50,100,500,1000,*");
    public String RESULT_RANGES_DEFAULT = this.config.getString("hmc.result.ranges.default", "50");
    public int RESULTLIST_MINHEIGHT = this.config.getInt("hmc.resultlist.minheight", 75);
    public boolean SHOW_ITEMHISTORY = this.config.getBoolean("hmc.show.itemhistory", true);
    public boolean SHOWQUERY = this.config.getBoolean("hmc.showquery", false);
    public int STORING_MODIFIEDVALUES_SIZE = this.config.getInt("hmc.storing.modifiedvalues.size", 20);
    public boolean STRUCTURE_DB = this.config.getBoolean("hmc.structure.db", false);
    public String STRUCTURE_FILENAME = this.config.getString("hmc.structure.filename", DEFAULTS.STRUCTURE_FILENAME);
    public boolean TOUCHFORREPLICATION = this.config.getBoolean("hmc.touchforreplication", true);
    public boolean USERPROFILE_ACTIVE = this.config.getBoolean("hmc.userprofile.active", false);
    public String WINDOW_ABOUT = this.config.getString("hmc.window.about", "400x300");
    public String WINDOW_ACTIONRESULT = this.config.getString("hmc.window.actionresult", "533x400");
    public String WINDOW_EDITOR = this.config.getString("hmc.window.editor", "80%");
    public String WINDOW_ORGANIZER = this.config.getString("hmc.window.organizer", "80%");
    public String WINDOW_SHORTCUT = this.config.getString("hmc.window.shortcut", "80%");
    public String WINDOW_TYPEEXPORT_RESULT = this.config.getString("hmc.window.typeexport.result", "640x480");
    public long WINDOWTIMEOUT = this.config.getLong("hmc.windowtimeout", 240L);
    public String HMC_MEDIA_FOLDER = this.config.getString("hmc.media.folder", "hmc");
    public boolean HMC_ORGANIZERLIST_SCROLLHEADER = this.config.getBoolean("hmc.organizerlist.scrollheader", false);
    private static ConfigConstants cfgconstants;


    private ConfigConstants()
    {
        this.config.registerConfigChangeListener((ConfigIntf.ConfigChangeListener)new Object(this));
    }


    public static ConfigConstants getInstance()
    {
        if(cfgconstants == null)
        {
            cfgconstants = new ConfigConstants();
        }
        return cfgconstants;
    }
}
