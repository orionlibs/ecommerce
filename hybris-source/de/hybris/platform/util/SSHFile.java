package de.hybris.platform.util;

public class SSHFile
{
    String userHost;
    String filename;
    String url;


    public SSHFile(String sshFile)
    {
        parseSSHFile(sshFile);
    }


    private void parseSSHFile(String sshFile)
    {
        int delimPos = sshFile.indexOf(',');
        if(delimPos == -1)
        {
            this.url = "";
        }
        else
        {
            this.url = sshFile.substring(delimPos + 1).trim();
            sshFile = sshFile.substring(0, delimPos);
        }
        delimPos = sshFile.indexOf(':');
        if(delimPos == -1)
        {
            this.userHost = sshFile.trim();
            this.filename = ".";
        }
        else
        {
            this.userHost = sshFile.substring(0, delimPos).trim();
            this.filename = sshFile.substring(delimPos + 1, sshFile.length()).trim();
        }
    }


    public String getUserAndHost()
    {
        return this.userHost;
    }


    public String getFilename()
    {
        return this.filename;
    }


    public String getURL()
    {
        return this.url;
    }


    public String toString()
    {
        return this.userHost + ":" + this.userHost + "," + this.filename;
    }
}
