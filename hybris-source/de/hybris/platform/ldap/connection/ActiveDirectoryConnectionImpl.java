package de.hybris.platform.ldap.connection;

import com.sun.jndi.ldap.LdapCtxFactory;
import de.hybris.platform.ldap.constants.LDAPConstants;
import de.hybris.platform.ldap.exception.LDAPOperationException;
import de.hybris.platform.ldap.exception.LDAPUnavailableException;
import de.hybris.platform.ldap.jalo.LDAPManager;
import de.hybris.platform.util.Config;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import javax.naming.AuthenticationException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import javax.naming.ldap.PagedResultsControl;
import javax.naming.ldap.PagedResultsResponseControl;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class ActiveDirectoryConnectionImpl extends JNDIConnectionImpl
{
    private static final Logger LOG = Logger.getLogger(ActiveDirectoryConnectionImpl.class.getName());
    private Boolean pagingSupported;


    ActiveDirectoryConnectionImpl(ConnectionData connectionData) throws LDAPUnavailableException
    {
        super(connectionData);
    }


    private boolean detectIfPagingSupported()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Checking for paging support ...");
            LOG.debug("PagedResultsControl.OID: 1.2.840.113556.1.4.319");
        }
        boolean pagingSupported = false;
        try
        {
            Attributes attrs = this.ldapConnection.getAttributes("", new String[] {"supportedControl"});
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Supported Controls:");
                NamingEnumeration<? extends Attribute> controls = attrs.getAll();
                while(controls.hasMore())
                {
                    LOG.debug("Control: " + controls.next().toString());
                }
            }
            pagingSupported = String.valueOf(attrs).contains("1.2.840.113556.1.4.319");
            if(LOG.isDebugEnabled())
            {
                LOG.debug("'Paging Control' (1.2.840.113556.1.4.319) found? " + pagingSupported);
            }
        }
        catch(NamingException e)
        {
            LOG.error("Can't detect paging support!", e);
            return false;
        }
        return pagingSupported;
    }


    private boolean isPagingSupported()
    {
        if(this.pagingSupported == null)
        {
            this.pagingSupported = Boolean.valueOf(detectIfPagingSupported());
        }
        return this.pagingSupported.booleanValue();
    }


    protected Collection<LDAPGenericObject> rawSearchSubTree(String searchbase, String filter, int limit, int timeout, String[] returnAttributes) throws LDAPOperationException, LDAPUnavailableException, NamingException
    {
        int pageSize = (Config.getParameter(LDAPConstants.CONFIG.PROJECT_PROPERTIES_KEY.PAGESIZE) != null) ? Integer.parseInt(Config.getParameter(LDAPConstants.CONFIG.PROJECT_PROPERTIES_KEY.PAGESIZE)) : 100;
        if(pageSize < 1 || !isPagingSupported())
        {
            LOG.debug("No paging is used.");
            return super.rawSearchSubTree(searchbase, filter, limit, timeout, returnAttributes);
        }
        return rawSearchSubTreeInternal(searchbase, filter, limit, timeout, returnAttributes, pageSize);
    }


    private Collection<LDAPGenericObject> rawSearchSubTreeInternal(String searchbase, String filter, int limit, int timeout, String[] returnAttributes, int pageSize) throws LDAPOperationException, LDAPUnavailableException, NamingException
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("rawSearchSubTree( " + searchbase + "," + filter + "," + limit + "," + timeout + ",<returnAttributes>) ");
        }
        searchbase = appendRootDN(searchbase);
        searchbase = LDAPManager.getInstance().cleanse(searchbase);
        if(returnAttributes == null || returnAttributes.length == 0)
        {
            returnAttributes = new String[] {"objectClass"};
        }
        int count = 0;
        List<LDAPGenericObject> result = new ArrayList<>();
        while(count < this.maxRetries)
        {
            SearchControls constraints = new SearchControls();
            constraints.setSearchScope(2);
            if(limit > -1)
            {
                constraints.setCountLimit(limit);
            }
            if(timeout > -1)
            {
                constraints.setTimeLimit(timeout);
            }
            constraints.setReturningAttributes(returnAttributes);
            if(LOG.isDebugEnabled())
            {
                LOG.debug("used pagesize: " + pageSize);
            }
            byte[] cookie = null;
            try
            {
                Control[] ctls = {new PagedResultsControl(pageSize, false)};
                this.ldapConnection.setRequestControls(ctls);
                do
                {
                    NamingEnumeration<SearchResult> results = this.ldapConnection.search(searchbase, filter, constraints);
                    while(results != null && results.hasMoreElements())
                    {
                        SearchResult searchResult = results.nextElement();
                        LDAPGenericObject genericObject = new LDAPGenericObject((LdapContext)searchResult.getObject(), searchResult.getName(), searchbase);
                        try
                        {
                            genericObject.setAttributes(searchResult.getAttributes());
                        }
                        catch(NamingException e)
                        {
                            LOG.warn("IGNORING: " + e.getMessage(), e);
                        }
                        result.add(genericObject);
                    }
                    cookie = parseControls(this.ldapConnection.getResponseControls());
                    this.ldapConnection.setRequestControls(new Control[] {new PagedResultsControl(pageSize, cookie, false)});
                }
                while(cookie != null && cookie.length != 0);
                return result;
            }
            catch(NamingException e)
            {
                if(JNDIConnectionManager.checkIfExceptionIsFailable(e))
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug(" rawSearchSubTree calling retryConnection()");
                    }
                    this.ldapConnection = retryConnection(e);
                }
                else
                {
                    LOG.warn("Operation failed: " + e.toString(), e);
                    throw LDAPOperationException.createLDAPException(e.getMessage());
                }
            }
            catch(IOException e)
            {
                LOG.warn("Operation failed: " + e.toString(), e);
                throw LDAPOperationException.createLDAPException(e.getMessage());
            }
            catch(NullPointerException e)
            {
                LOG.error(e.toString(), e);
                return Collections.EMPTY_LIST;
            }
            count++;
        }
        throw new LDAPUnavailableException("LDAP pooling failure: Max retries exceeded");
    }


    protected Collection<LDAPGenericObject> rawSearchSubTree_UsingRangeRetrieval(String searchbase, String filter, int limit, int timeout, String[] returnAttributes) throws LDAPOperationException, LDAPUnavailableException, NamingException
    {
        // Byte code:
        //   0: getstatic de/hybris/platform/ldap/connection/ActiveDirectoryConnectionImpl.LOG : Lorg/apache/log4j/Logger;
        //   3: invokevirtual isDebugEnabled : ()Z
        //   6: ifeq -> 25
        //   9: getstatic de/hybris/platform/ldap/connection/ActiveDirectoryConnectionImpl.LOG : Lorg/apache/log4j/Logger;
        //   12: aload_1
        //   13: aload_2
        //   14: iload_3
        //   15: iload #4
        //   17: <illegal opcode> makeConcatWithConstants : (Ljava/lang/String;Ljava/lang/String;II)Ljava/lang/String;
        //   22: invokevirtual debug : (Ljava/lang/Object;)V
        //   25: aload_0
        //   26: aload_1
        //   27: invokevirtual appendRootDN : (Ljava/lang/String;)Ljava/lang/String;
        //   30: astore_1
        //   31: invokestatic getInstance : ()Lde/hybris/platform/ldap/jalo/LDAPManager;
        //   34: aload_1
        //   35: invokevirtual cleanse : (Ljava/lang/String;)Ljava/lang/String;
        //   38: astore_1
        //   39: aload #5
        //   41: ifnull -> 50
        //   44: aload #5
        //   46: arraylength
        //   47: ifne -> 61
        //   50: iconst_1
        //   51: anewarray java/lang/String
        //   54: dup
        //   55: iconst_0
        //   56: ldc 'objectClass'
        //   58: aastore
        //   59: astore #5
        //   61: iconst_0
        //   62: istore #6
        //   64: new java/util/ArrayList
        //   67: dup
        //   68: invokespecial <init> : ()V
        //   71: astore #7
        //   73: iload #6
        //   75: aload_0
        //   76: getfield maxRetries : I
        //   79: if_icmpge -> 469
        //   82: iconst_0
        //   83: istore #8
        //   85: iconst_0
        //   86: istore #9
        //   88: bipush #10
        //   90: istore #10
        //   92: bipush #9
        //   94: istore #11
        //   96: iconst_0
        //   97: istore #12
        //   99: ldc ''
        //   101: astore #13
        //   103: iconst_1
        //   104: istore #14
        //   106: iconst_1
        //   107: istore #15
        //   109: new javax/naming/directory/SearchControls
        //   112: dup
        //   113: invokespecial <init> : ()V
        //   116: astore #16
        //   118: aload #16
        //   120: iconst_2
        //   121: invokevirtual setSearchScope : (I)V
        //   124: iload_3
        //   125: iconst_m1
        //   126: if_icmple -> 136
        //   129: aload #16
        //   131: iload_3
        //   132: i2l
        //   133: invokevirtual setCountLimit : (J)V
        //   136: iload #4
        //   138: iconst_m1
        //   139: if_icmple -> 149
        //   142: aload #16
        //   144: iload #4
        //   146: invokevirtual setTimeLimit : (I)V
        //   149: iload #12
        //   151: ifne -> 374
        //   154: iload #9
        //   156: iload #11
        //   158: <illegal opcode> makeConcatWithConstants : (II)Ljava/lang/String;
        //   163: astore #13
        //   165: aload #5
        //   167: arraylength
        //   168: iconst_1
        //   169: iadd
        //   170: anewarray java/lang/String
        //   173: astore #17
        //   175: aload #5
        //   177: iconst_0
        //   178: aload #17
        //   180: iconst_0
        //   181: aload #5
        //   183: arraylength
        //   184: aload #17
        //   186: arraylength
        //   187: invokestatic min : (II)I
        //   190: invokestatic arraycopy : (Ljava/lang/Object;ILjava/lang/Object;II)V
        //   193: aload #17
        //   195: aload #17
        //   197: arraylength
        //   198: iconst_1
        //   199: isub
        //   200: aload #13
        //   202: <illegal opcode> makeConcatWithConstants : (Ljava/lang/String;)Ljava/lang/String;
        //   207: aastore
        //   208: aload #16
        //   210: aload #17
        //   212: invokevirtual setReturningAttributes : ([Ljava/lang/String;)V
        //   215: iload #15
        //   217: ifeq -> 346
        //   220: aload_0
        //   221: getfield ldapConnection : Ljavax/naming/ldap/LdapContext;
        //   224: aload_1
        //   225: aload_2
        //   226: aload #16
        //   228: invokeinterface search : (Ljava/lang/String;Ljava/lang/String;Ljavax/naming/directory/SearchControls;)Ljavax/naming/NamingEnumeration;
        //   233: astore #18
        //   235: aload #18
        //   237: invokeinterface hasMoreElements : ()Z
        //   242: ifeq -> 288
        //   245: aload #18
        //   247: invokeinterface nextElement : ()Ljava/lang/Object;
        //   252: checkcast javax/naming/directory/SearchResult
        //   255: astore #19
        //   257: iconst_0
        //   258: istore #15
        //   260: new de/hybris/platform/ldap/connection/LDAPGenericObject
        //   263: dup
        //   264: aload #19
        //   266: aload_1
        //   267: invokespecial <init> : (Ljavax/naming/directory/SearchResult;Ljava/lang/String;)V
        //   270: astore #20
        //   272: iinc #8, 1
        //   275: aload #7
        //   277: aload #20
        //   279: invokeinterface add : (Ljava/lang/Object;)Z
        //   284: pop
        //   285: goto -> 235
        //   288: goto -> 329
        //   291: astore #18
        //   293: aload #18
        //   295: invokevirtual skipReferral : ()Z
        //   298: istore #15
        //   300: iload #15
        //   302: ifeq -> 311
        //   305: aload #18
        //   307: invokevirtual getReferralContext : ()Ljavax/naming/Context;
        //   310: pop
        //   311: goto -> 329
        //   314: astore #18
        //   316: getstatic de/hybris/platform/ldap/connection/ActiveDirectoryConnectionImpl.LOG : Lorg/apache/log4j/Logger;
        //   319: aload #18
        //   321: invokevirtual getMessage : ()Ljava/lang/String;
        //   324: aload #18
        //   326: invokevirtual warn : (Ljava/lang/Object;Ljava/lang/Throwable;)V
        //   329: iload #9
        //   331: bipush #10
        //   333: iadd
        //   334: istore #9
        //   336: iload #11
        //   338: bipush #10
        //   340: iadd
        //   341: istore #11
        //   343: goto -> 215
        //   346: iconst_1
        //   347: istore #12
        //   349: getstatic de/hybris/platform/ldap/connection/ActiveDirectoryConnectionImpl.LOG : Lorg/apache/log4j/Logger;
        //   352: invokevirtual isDebugEnabled : ()Z
        //   355: ifeq -> 371
        //   358: getstatic de/hybris/platform/ldap/connection/ActiveDirectoryConnectionImpl.LOG : Lorg/apache/log4j/Logger;
        //   361: iload #8
        //   363: <illegal opcode> makeConcatWithConstants : (I)Ljava/lang/String;
        //   368: invokevirtual debug : (Ljava/lang/Object;)V
        //   371: goto -> 149
        //   374: aload #7
        //   376: areturn
        //   377: astore #17
        //   379: aload #17
        //   381: invokestatic checkIfExceptionIsFailable : (Ljavax/naming/NamingException;)Z
        //   384: ifeq -> 418
        //   387: getstatic de/hybris/platform/ldap/connection/ActiveDirectoryConnectionImpl.LOG : Lorg/apache/log4j/Logger;
        //   390: invokevirtual isDebugEnabled : ()Z
        //   393: ifeq -> 405
        //   396: getstatic de/hybris/platform/ldap/connection/ActiveDirectoryConnectionImpl.LOG : Lorg/apache/log4j/Logger;
        //   399: ldc_w ' rawSearchSubTree_UsingRangeRetrieval calling retryConnection()'
        //   402: invokevirtual debug : (Ljava/lang/Object;)V
        //   405: aload_0
        //   406: aload_0
        //   407: aload #17
        //   409: invokevirtual retryConnection : (Ljava/lang/Exception;)Ljavax/naming/ldap/LdapContext;
        //   412: putfield ldapConnection : Ljavax/naming/ldap/LdapContext;
        //   415: goto -> 443
        //   418: getstatic de/hybris/platform/ldap/connection/ActiveDirectoryConnectionImpl.LOG : Lorg/apache/log4j/Logger;
        //   421: aload #17
        //   423: invokevirtual toString : ()Ljava/lang/String;
        //   426: <illegal opcode> makeConcatWithConstants : (Ljava/lang/String;)Ljava/lang/String;
        //   431: invokevirtual warn : (Ljava/lang/Object;)V
        //   434: aload #17
        //   436: invokevirtual getMessage : ()Ljava/lang/String;
        //   439: invokestatic createLDAPException : (Ljava/lang/String;)Lde/hybris/platform/ldap/exception/LDAPOperationException;
        //   442: athrow
        //   443: goto -> 463
        //   446: astore #17
        //   448: getstatic de/hybris/platform/ldap/connection/ActiveDirectoryConnectionImpl.LOG : Lorg/apache/log4j/Logger;
        //   451: aload #17
        //   453: invokevirtual toString : ()Ljava/lang/String;
        //   456: aload #17
        //   458: invokevirtual error : (Ljava/lang/Object;Ljava/lang/Throwable;)V
        //   461: aconst_null
        //   462: areturn
        //   463: iinc #6, 1
        //   466: goto -> 73
        //   469: new de/hybris/platform/ldap/exception/LDAPUnavailableException
        //   472: dup
        //   473: ldc_w 'LDAP pooling failure: Max retries exceeded'
        //   476: invokespecial <init> : (Ljava/lang/String;)V
        //   479: athrow
        // Line number table:
        //   Java source line number -> byte code offset
        //   #333	-> 0
        //   #335	-> 9
        //   #339	-> 25
        //   #340	-> 31
        //   #342	-> 39
        //   #344	-> 50
        //   #347	-> 61
        //   #349	-> 64
        //   #351	-> 73
        //   #354	-> 82
        //   #355	-> 85
        //   #356	-> 88
        //   #357	-> 92
        //   #358	-> 96
        //   #359	-> 99
        //   #362	-> 103
        //   #363	-> 106
        //   #367	-> 109
        //   #368	-> 118
        //   #369	-> 124
        //   #371	-> 129
        //   #373	-> 136
        //   #375	-> 142
        //   #381	-> 149
        //   #383	-> 154
        //   #385	-> 165
        //   #386	-> 175
        //   #387	-> 187
        //   #386	-> 190
        //   #388	-> 193
        //   #390	-> 208
        //   #393	-> 215
        //   #397	-> 220
        //   #398	-> 235
        //   #400	-> 245
        //   #402	-> 257
        //   #403	-> 260
        //   #404	-> 272
        //   #405	-> 275
        //   #406	-> 285
        //   #424	-> 288
        //   #408	-> 291
        //   #412	-> 293
        //   #415	-> 300
        //   #418	-> 305
        //   #424	-> 311
        //   #421	-> 314
        //   #423	-> 316
        //   #426	-> 329
        //   #427	-> 336
        //   #430	-> 346
        //   #431	-> 349
        //   #433	-> 358
        //   #436	-> 371
        //   #438	-> 374
        //   #440	-> 377
        //   #442	-> 379
        //   #444	-> 387
        //   #446	-> 396
        //   #449	-> 405
        //   #453	-> 418
        //   #454	-> 434
        //   #461	-> 443
        //   #457	-> 446
        //   #459	-> 448
        //   #460	-> 461
        //   #462	-> 463
        //   #463	-> 466
        //   #464	-> 469
        // Local variable table:
        //   start	length	slot	name	descriptor
        //   257	28	19	searchResult	Ljavax/naming/directory/SearchResult;
        //   272	13	20	genericObject	Lde/hybris/platform/ldap/connection/LDAPGenericObject;
        //   235	53	18	namingEnumeration	Ljavax/naming/NamingEnumeration;
        //   293	18	18	e	Ljavax/naming/ReferralException;
        //   316	13	18	e	Ljavax/naming/NamingException;
        //   175	196	17	returnedAtts	[Ljava/lang/String;
        //   379	64	17	e	Ljavax/naming/NamingException;
        //   448	15	17	e	Ljava/lang/NullPointerException;
        //   85	381	8	totalResults	I
        //   88	378	9	start	I
        //   92	374	10	step	I
        //   96	370	11	finish	I
        //   99	367	12	finished	Z
        //   103	363	13	range	Ljava/lang/String;
        //   106	360	14	followReferral	Z
        //   109	357	15	moreReferrals	Z
        //   118	348	16	constraints	Ljavax/naming/directory/SearchControls;
        //   0	480	0	this	Lde/hybris/platform/ldap/connection/ActiveDirectoryConnectionImpl;
        //   0	480	1	searchbase	Ljava/lang/String;
        //   0	480	2	filter	Ljava/lang/String;
        //   0	480	3	limit	I
        //   0	480	4	timeout	I
        //   0	480	5	returnAttributes	[Ljava/lang/String;
        //   64	416	6	count	I
        //   73	407	7	result	Ljava/util/List;
        // Local variable type table:
        //   start	length	slot	name	signature
        //   73	407	7	result	Ljava/util/List<Lde/hybris/platform/ldap/connection/LDAPGenericObject;>;
        // Exception table:
        //   from	to	target	type
        //   149	376	377	javax/naming/NamingException
        //   149	376	446	java/lang/NullPointerException
        //   220	288	291	javax/naming/ReferralException
        //   220	288	314	javax/naming/NamingException
    }


    public boolean checkPassword(String searchbase, String login, char[] plainPassword)
    {
        boolean fastbind = (Config.getParameter(LDAPConstants.CONFIG.PROJECT_PROPERTIES_KEY.FASTBINDCONNECTION) != null && Boolean.parseBoolean(
                        Config.getParameter(LDAPConstants.CONFIG.PROJECT_PROPERTIES_KEY.FASTBINDCONNECTION)));
        if(fastbind)
        {
            return checkPasswordUseFastBind(login, plainPassword);
        }
        return checkPasswordDontUseFastBind(login, plainPassword);
    }


    private boolean checkPasswordUseFastBind(String login, char[] plainPassword)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("checkPasswordUseFastBind( " + login + ", <password> )");
            this.connectionManager.dumpConfiguration();
        }
        if(plainPassword.length == 0)
        {
            LOG.warn("Empty password submitted, which is not supported!");
            return false;
        }
        try
        {
            Hashtable<String, String> env = this.connectionData.getJNDIEnvironment();
            env.put("java.naming.factory.initial", LdapCtxFactory.class.getCanonicalName());
            env.put("java.naming.security.authentication",
                            Config.getParameter(LDAPConstants.CONFIG.PROJECT_PROPERTIES_KEY.JNDIAUTHENTICATION));
            if(StringUtils.isNotEmpty(Config.getParameter(LDAPConstants.CONFIG.PROJECT_PROPERTIES_KEY.SECURITYPROTOCOL)))
            {
                env.put("java.naming.security.protocol",
                                Config.getParameter(LDAPConstants.CONFIG.PROJECT_PROPERTIES_KEY.SECURITYPROTOCOL));
                env.put("java.naming.provider.url", this.connectionManager.getServerURL().replace("ldap:", "ldaps:"));
                if(StringUtils.isNotEmpty(Config.getParameter(LDAPConstants.CONFIG.PROJECT_PROPERTIES_KEY.SOCKETFACTORY)))
                {
                    env.put("java.naming.ldap.factory.socket",
                                    Config.getParameter(LDAPConstants.CONFIG.PROJECT_PROPERTIES_KEY.SOCKETFACTORY));
                }
            }
            else
            {
                env.put("java.naming.provider.url", this.connectionManager.getServerURL());
            }
            Control[] connCtls = {(Control)new FastBindConnectionControl(this)};
            InitialLdapContext ctx = new InitialLdapContext(env, connCtls);
            ctx.addToEnvironment("java.naming.security.principal", login);
            ctx.addToEnvironment("java.naming.security.credentials", new String(plainPassword));
            ctx.reconnect(connCtls);
            if(LOG.isDebugEnabled())
            {
                LOG.debug("is authenticated");
            }
            return true;
        }
        catch(AuthenticationException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug(e.getMessage(), e);
                LOG.debug("is not authenticated!");
            }
            return false;
        }
        catch(NamingException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug(e.getMessage(), e);
                LOG.debug("is not authenticated!");
            }
            return false;
        }
    }


    private boolean checkPasswordDontUseFastBind(String login, char[] plainPassword)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("checkPasswordDontUseFastBind( " + login + ", <password> )");
            this.connectionManager.dumpConfiguration();
        }
        if(plainPassword.length == 0)
        {
            LOG.warn("Empty password submitted, which is not supported!");
            return false;
        }
        InitialLdapContext ctx = null;
        Hashtable<String, String> env = null;
        boolean success = false;
        StringBuilder principal = new StringBuilder();
        try
        {
            principal.append(login);
            env = this.connectionData.getJNDIEnvironment();
            env.put("java.naming.factory.initial", LdapCtxFactory.class.getCanonicalName());
            env.put("java.naming.security.principal", principal.toString());
            env.put("java.naming.security.credentials", new String(plainPassword));
            env.put("java.naming.security.authentication",
                            Config.getParameter(LDAPConstants.CONFIG.PROJECT_PROPERTIES_KEY.JNDIAUTHENTICATION));
            if(StringUtils.isNotEmpty(Config.getParameter(LDAPConstants.CONFIG.PROJECT_PROPERTIES_KEY.SECURITYPROTOCOL)))
            {
                env.put("java.naming.security.protocol",
                                Config.getParameter(LDAPConstants.CONFIG.PROJECT_PROPERTIES_KEY.SECURITYPROTOCOL));
                env.put("java.naming.provider.url", this.connectionManager.getServerURL().replace("ldap:", "ldaps:"));
                if(StringUtils.isNotEmpty(Config.getParameter(LDAPConstants.CONFIG.PROJECT_PROPERTIES_KEY.SOCKETFACTORY)))
                {
                    env.put("java.naming.ldap.factory.socket",
                                    Config.getParameter(LDAPConstants.CONFIG.PROJECT_PROPERTIES_KEY.SOCKETFACTORY));
                }
            }
            else
            {
                env.put("java.naming.provider.url", this.connectionManager.getServerURL());
            }
            ctx = new InitialLdapContext(env, null);
            success = true;
        }
        catch(NamingException e)
        {
            LOG.error("Authentication failed!");
            LOG.error("java.naming.provider.url: '" + this.connectionManager.getServerURL() + "'");
            LOG.error("java.naming.security.principal: '" + principal.toString() + "'");
            LOG.error(e.getMessage(), e);
        }
        finally
        {
            wipePassword(plainPassword, env);
            try
            {
                if(ctx != null)
                {
                    ctx.close();
                }
            }
            catch(NamingException e)
            {
                LOG.error("close() failed: Reason:" + e.toString(), e);
            }
        }
        return success;
    }


    protected byte[] parseControls(Control[] controls) throws NamingException
    {
        byte[] cookie = null;
        if(controls != null)
        {
            for(int i = 0; i < controls.length; i++)
            {
                if(controls[i] instanceof PagedResultsResponseControl)
                {
                    PagedResultsResponseControl prrc = (PagedResultsResponseControl)controls[i];
                    cookie = prrc.getCookie();
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug(">>Next Page \n");
                    }
                }
            }
        }
        return (cookie == null) ? new byte[0] : cookie;
    }


    public void changePassword(DirContext ctx, String argRDN, String oldPassword, String newPassword) throws NamingException
    {
        ModificationItem[] modificationItem = new ModificationItem[2];
        try
        {
            modificationItem[0] = new ModificationItem(3, new BasicAttribute("unicodePwd",
                            encodePassword(oldPassword)));
            modificationItem[1] = new ModificationItem(1, new BasicAttribute("unicodePwd",
                            encodePassword(newPassword)));
        }
        catch(UnsupportedEncodingException e1)
        {
            throw new RuntimeException(e1);
        }
        try
        {
            ctx.modifyAttributes(argRDN, modificationItem);
        }
        catch(NamingException e1)
        {
            throw e1;
        }
    }
}
