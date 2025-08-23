package de.hybris.platform.cockpit.forms.login;

import com.google.common.base.Preconditions;
import de.hybris.platform.cockpit.helpers.LocaleHelper;
import de.hybris.platform.cockpit.model.login.UserSessionSettings;
import de.hybris.platform.cockpit.services.SystemService;
import de.hybris.platform.cockpit.services.login.LoginService;
import de.hybris.platform.cockpit.session.UISession;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.core.AuthenticationException;
import org.zkoss.util.Locales;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.ComponentNotFoundException;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.Label;
import org.zkoss.zul.Window;

public class LoginForm
{
    private static final Logger LOG = LoggerFactory.getLogger(LoginForm.class);
    private static final String ERROR_LABEL_CSS_CLASS = "loginErrorLabel";
    private List<LanguageModel> available;
    private Window dialogWindow;
    private String login;
    private LoginService loginService;
    private LocaleHelper localeHelper;
    private String password;
    private boolean remember = false;
    private ComboitemRenderer renderer;
    private LanguageModel selectedLanguage;
    private SystemService systemService;
    private String viewComponentURI;
    private Converter<AuthenticationException, Label> exceptionToLabelConverter;


    public void doClear()
    {
        this.login = getDefaultUsername();
        this.password = getDefaultUserPassword();
        this.remember = false;
        this.selectedLanguage = null;
        this.available = null;
    }


    public void doLogout()
    {
        try
        {
            doClear();
            UISession currentSession = UISessionUtils.getCurrentSession();
            getLoginService().doLogout(getRequest(), getRespone());
            if(currentSession != null)
            {
                currentSession.logout();
            }
            Sessions.getCurrent().invalidate();
        }
        finally
        {
            Executions.sendRedirect("/login.zul");
        }
    }


    public boolean doOK()
    {
        boolean success = false;
        Session session = Executions.getCurrent().getDesktop().getSession();
        try
        {
            session.setAttribute("px_preferred_locale", getLoginService().getLocale(getSelectedLanguage()));
            getLoginService().setSessionLanguage(getSelectedLanguage());
            success = true;
        }
        catch(IllegalArgumentException iae)
        {
            LOG.warn("Login failed.", iae);
            success = false;
        }
        return success;
    }


    public List<LanguageModel> getAvailableLanguages()
    {
        if(this.available == null)
        {
            this.available = new ArrayList<>(getSystemService().getAvailableLanguages());
        }
        return this.available;
    }


    @Deprecated(since = "2005")
    public String getDefaultUsername()
    {
        return "";
    }


    @Deprecated(since = "2005")
    public String getDefaultUserPassword()
    {
        return "";
    }


    public String getLogin()
    {
        return this.login;
    }


    public void setLogin(String login)
    {
        Preconditions.checkArgument((login != null));
        this.login = login;
    }


    public LoginService getLoginService()
    {
        return this.loginService;
    }


    @Required
    public void setLoginService(LoginService loginService)
    {
        this.loginService = loginService;
    }


    public String getPassword()
    {
        return this.password;
    }


    public void setPassword(String password)
    {
        Preconditions.checkArgument((password != null));
        this.password = password;
    }


    public ComboitemRenderer getRenderer()
    {
        if(this.renderer == null)
        {
            this.renderer = (ComboitemRenderer)new Object(this);
        }
        return this.renderer;
    }


    public LanguageModel getSelectedLanguage()
    {
        return this.selectedLanguage;
    }


    public void setSelectedLanguage(LanguageModel selectedLanguage)
    {
        this.selectedLanguage = selectedLanguage;
    }


    public Label getStatusLabel()
    {
        Label statusLabel = null;
        if(this.dialogWindow != null)
        {
            statusLabel = (Label)this.dialogWindow.getFellow("status");
        }
        return statusLabel;
    }


    public SystemService getSystemService()
    {
        return this.systemService;
    }


    @Required
    public void setSystemService(SystemService systemService)
    {
        this.systemService = systemService;
    }


    public String getViewComponentURI()
    {
        return this.viewComponentURI;
    }


    @Required
    public void setViewComponentURI(String viewComponentURI)
    {
        this.viewComponentURI = viewComponentURI;
    }


    public void init(Window dialogWindow)
    {
        this.dialogWindow = dialogWindow;
        if(this.dialogWindow != null && getRequest().getParameter("login_error") != null)
        {
            AuthenticationException exception = (AuthenticationException)getRequest().getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
            processLoginFailure(dialogWindow, exception);
        }
        else if(dialogWindow != null)
        {
            ((Label)dialogWindow.getFellow("status")).setValue(null);
        }
        try
        {
            Session session = Executions.getCurrent().getDesktop().getSession();
            LanguageModel currentLang = getSystemService().getLanguageForLocale(Locales.getCurrent());
            getLoginService().setSessionLanguage(currentLang);
            UserSessionSettings currentSettings = getLoginService().getCurrentSessionSettings();
            session.setAttribute("px_preferred_locale", currentSettings.getLocale());
            session.setAttribute("px_preferred_time_zone", currentSettings.getTimeZone());
            Combobox box = (Combobox)dialogWindow.getFellow("box");
            Object object = new Object(this, box);
            box.addEventListener("onSelect", (EventListener)object);
            box.addEventListener("onChange", (EventListener)object);
            setSelectedLanguage(currentLang);
            for(LanguageModel l : getAvailableLanguages())
            {
                Comboitem item = new Comboitem();
                try
                {
                    getRenderer().render(item, l);
                    box.appendChild((Component)item);
                }
                catch(Exception e)
                {
                    LOG.error("error loading language combobox : " + e.getMessage(), e);
                }
            }
            if(getSelectedLanguage() != null)
            {
                int pos = getAvailableLanguages().indexOf(getSelectedLanguage());
                if(pos > -1 && box.getChildren().size() > pos)
                {
                    box.setSelectedIndex(pos);
                }
            }
        }
        catch(ComponentNotFoundException componentNotFoundException)
        {
        }
        catch(ClassCastException classCastException)
        {
        }
    }


    public boolean isLoginRequired()
    {
        return (UISessionUtils.getCurrentSession().getUser() == null);
    }


    public boolean isRemember()
    {
        return this.remember;
    }


    public void setRemember(boolean remember)
    {
        this.remember = remember;
    }


    @Required
    public void setLocaleHelper(LocaleHelper localeHelper)
    {
        this.localeHelper = localeHelper;
    }


    public void show()
    {
        doClear();
        if(UISessionUtils.getCurrentSession().getUser() != null)
        {
            Sessions.getCurrent().invalidate();
            Executions.sendRedirect("/");
        }
        ((Window)Executions.createComponents(getViewComponentURI(), null, null)).doOverlapped();
    }


    protected HttpServletRequest getRequest()
    {
        HttpServletRequestWrapper wrapper = (HttpServletRequestWrapper)Executions.getCurrent().getNativeRequest();
        return (HttpServletRequest)wrapper.getRequest();
    }


    protected HttpServletResponse getRespone()
    {
        HttpServletResponseWrapper wrapper = (HttpServletResponseWrapper)Executions.getCurrent().getNativeResponse();
        return (HttpServletResponse)wrapper.getResponse();
    }


    private void processLoginFailure(Window win, AuthenticationException exception)
    {
        if(win == null)
        {
            return;
        }
        Label label = (Label)win.getFellow("status");
        prepareLabel(label, exception);
        label.setVisible(true);
    }


    protected void prepareLabel(Label label, AuthenticationException exception)
    {
        if(exception == null || getExceptionToLabelConverter() == null)
        {
            label.setValue(Labels.getLabel("wrong_credentials"));
            UITools.modifySClass((HtmlBasedComponent)label, "loginErrorLabel", true);
            return;
        }
        getExceptionToLabelConverter().convert(exception, label);
    }


    protected Converter<AuthenticationException, Label> getExceptionToLabelConverter()
    {
        return this.exceptionToLabelConverter;
    }


    public void setExceptionToLabelConverter(Converter<AuthenticationException, Label> exceptionToLabelConverter)
    {
        this.exceptionToLabelConverter = exceptionToLabelConverter;
    }
}
