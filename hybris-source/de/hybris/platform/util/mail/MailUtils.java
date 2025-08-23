package de.hybris.platform.util.mail;

import de.hybris.platform.util.Config;
import de.hybris.platform.util.localization.Localization;
import javax.naming.NamingException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.log4j.Logger;

public final class MailUtils
{
    private static final Logger LOG = Logger.getLogger(MailUtils.class);
    private static final String LOC_SERVER_EMPTY = "exception.core.mailutils.server.empty";
    private static final String LOC_POP3_SERVER_EMPTY = "exception.core.mailutils.pop3.server.empty";
    private static final String LOC_POP3_USER_EMPTY = "exception.core.mailutils.pop3.user.empty";
    private static final String LOC_PARAMETER_EMPTY = "exception.core.mailutils.parameter.empty";
    private static final String LOC_ADDRESS_EMPTY = "exception.core.mailutils.address.empty";
    private static final String LOC_ADDRESS_INVALID = "exception.core.mailutils.address.invalid";


    public static void validateEmailServer(String server) throws EmailException
    {
        if(server == null || StringUtils.isEmpty(server))
        {
            throw new EmailException(Localization.getLocalizedString("exception.core.mailutils.server.empty"));
        }
    }


    public static void validatePop3(String popServer, String popUser, String popPwd) throws EmailException
    {
        if(popServer == null || StringUtils.isEmpty(popServer))
        {
            throw new EmailException(Localization.getLocalizedString("exception.core.mailutils.pop3.server.empty"));
        }
        if(popUser == null || StringUtils.isEmpty(popUser))
        {
            throw new EmailException(Localization.getLocalizedString("exception.core.mailutils.pop3.user.empty"));
        }
    }


    public static void validateParameter(String parameter, String type) throws EmailException
    {
        if(parameter == null || StringUtils.isEmpty(parameter))
        {
            throw new EmailException(Localization.getLocalizedString("exception.core.mailutils.parameter.empty", new String[] {type}));
        }
    }


    public static void validateEmailAddress(String address, String type) throws EmailException
    {
        if(address == null || StringUtils.isEmpty(address))
        {
            throw new EmailException(Localization.getLocalizedString("exception.core.mailutils.address.empty", new String[] {type}));
        }
        int atIndex = address.indexOf('@');
        if(atIndex < 0)
        {
            throw new EmailException(Localization.getLocalizedString("exception.core.mailutils.address.invalid", new String[] {type, address}));
        }
        if(atIndex == 0)
        {
            throw new EmailException(Localization.getLocalizedString("exception.core.mailutils.address.invalid", new String[] {type, address}));
        }
        String lastPart = address.substring(atIndex);
        int lastDotIndex = lastPart.lastIndexOf('.');
        if(lastDotIndex == -1)
        {
            throw new EmailException(Localization.getLocalizedString("exception.core.mailutils.address.invalid", new String[] {type, address}));
        }
        if(lastDotIndex == lastPart.length() - 1)
        {
            throw new EmailException(Localization.getLocalizedString("exception.core.mailutils.address.invalid", new String[] {type, address}));
        }
        if(lastDotIndex == 1)
        {
            throw new EmailException(Localization.getLocalizedString("exception.core.mailutils.address.invalid", new String[] {type, address}));
        }
    }


    public static Email getPreConfiguredEmail() throws EmailException
    {
        String fromJNDI = Config.getString(Config.Params.MAIL_FROM_JNDI, null);
        String server = Config.getString(Config.Params.MAIL_SMTP_SERVER, null);
        String user = Config.getString(Config.Params.MAIL_SMTP_USER, null);
        String password = Config.getString(Config.Params.MAIL_SMTP_PASSWORD, null);
        String from = Config.getString(Config.Params.MAIL_FROM, null);
        String replyto = Config.getString(Config.Params.MAIL_REPLYTO, null);
        boolean popBeforeSmtp = Config.getBoolean(Config.Params.MAIL_POP3_BEFORESMTP, false);
        boolean useTLS = Config.getBoolean(Config.Params.MAIL_USE_TLS, false);
        String popServer = Config.getString(Config.Params.MAIL_POP3_SERVER, null);
        String popUser = Config.getString(Config.Params.MAIL_POP3_USER, null);
        String popPassword = Config.getString(Config.Params.MAIL_POP3_PASSWORD, null);
        int smtpport = -1;
        try
        {
            smtpport = Config.getInt(Config.Params.MAIL_SMTP_PORT, -1);
        }
        catch(NumberFormatException e)
        {
            LOG.warn("Malformed " + Config.Params.MAIL_SMTP_PORT + " property specified, using default. (" + e
                            .getMessage() + ")");
        }
        HtmlEmail email = new HtmlEmail();
        if(fromJNDI != null)
        {
            try
            {
                email.setMailSessionFromJNDI(fromJNDI);
            }
            catch(NamingException e)
            {
                throw new EmailException(e);
            }
            catch(RuntimeException e)
            {
                throw new EmailException(e);
            }
        }
        else
        {
            validateEmailServer(server);
            email.setHostName(server);
            if(smtpport > -1)
            {
                email.setSmtpPort(smtpport);
            }
            validateEmailAddress(from, Config.Params.MAIL_FROM);
            email.setFrom(from);
            if(replyto != null && !StringUtils.isEmpty(replyto))
            {
                validateEmailAddress(replyto, Config.Params.MAIL_REPLYTO);
                email.addReplyTo(replyto);
            }
            if(user != null && !StringUtils.isEmpty(user))
            {
                email.setAuthentication(user, password);
            }
            email.setTLS(useTLS);
            if(popBeforeSmtp)
            {
                validatePop3(popServer, popUser, popPassword);
                email.setPopBeforeSmtp(popBeforeSmtp, popServer, popUser, popPassword);
            }
        }
        return (Email)email;
    }
}
