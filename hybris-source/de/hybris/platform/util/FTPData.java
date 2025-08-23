package de.hybris.platform.util;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class FTPData
{
    public static final int AMOUNT_OF_PARAMS = 6;
    public static final String DEFAULT_VALUE_FTP_PORT = "-1";
    public static final String MEDIA_MANAGEMENT_TYPE = "media.management.type";
    private String user = "";
    private String password = "";
    private String host = "";
    private String path = "";
    private int ftp_port = -1;
    private String url = "";


    public FTPData(String ftpData)
    {
        parseFTPData(ftpData);
    }


    private void parseFTPData(String ftpData)
    {
        List<String> elements = new ArrayList();
        for(StringTokenizer tokenizer = new StringTokenizer(ftpData, "%"); tokenizer.hasMoreTokens(); )
        {
            elements.add(tokenizer.nextToken());
        }
        if(elements.size() < 6 || elements.size() > 6)
        {
            throw new RuntimeException("property media.management.type has invalid value");
        }
        if(elements.get(0) == null || ((String)elements.get(0)).equals(""))
        {
            throw new RuntimeException("property media.management.type has invalid value");
        }
        this.user = elements.get(0);
        this.password = elements.get(1);
        if(elements.get(2) == null || ((String)elements.get(2)).equals(""))
        {
            throw new RuntimeException("property media.management.type has invalid value");
        }
        this.host = elements.get(2);
        if(elements.get(3) != null && !((String)elements.get(3)).equals("") &&
                        !((String)elements.get(3)).equals("-1"))
        {
            try
            {
                this.ftp_port = Integer.parseInt(elements.get(3));
            }
            catch(NumberFormatException e)
            {
                throw new RuntimeException("property media.management.type has invalid value");
            }
        }
        if(elements.get(4) == null || ((String)elements.get(4)).equals(""))
        {
            throw new RuntimeException("property media.management.type has invalid value");
        }
        this.path = elements.get(4);
        this
                        .path = this.path.endsWith("/") ? (this.path + "sys_" + this.path) : (this.path + "/sys_" + this.path);
        if(elements.get(5) == null || ((String)elements.get(5)).equals(""))
        {
            throw new RuntimeException("property media.management.type has invalid value");
        }
        this.url = elements.get(5);
    }


    public String getUser()
    {
        return this.user;
    }


    public String getPassword()
    {
        return this.password;
    }


    public String getHost()
    {
        return this.host;
    }


    public int getPort()
    {
        return this.ftp_port;
    }


    public String getPath()
    {
        return this.path;
    }


    public String getURL()
    {
        return this.url;
    }
}
