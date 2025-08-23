package de.hybris.platform.jalo.security;

import com.google.common.collect.ImmutableMap;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserGroup;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.daos.UserDao;
import de.hybris.platform.util.CSVReader;
import de.hybris.platform.util.CSVWriter;
import de.hybris.platform.util.localization.Localization;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;

public class ImportExportUserRightsHelper
{
    private static final Logger log = Logger.getLogger(ImportExportUserRightsHelper.class.getName());
    public static final char DEFAULT_DATASEP = ',';
    private static final String DO_NOT_TOUCH_PASSWORD = "DO_NOT_TOUCH_PASSWORD";
    private char datasep = ',';
    private boolean flag_exportAllPrincipals = false;
    private final String firstcolumnheadername = "Type";
    private final String secoundcolumnheadername = "UID";
    private final String thirdcolumnheadername = "MemberOfGroups";
    private final String fourthcolumnheadername = "Password";
    private final String fifthcolumnheadername = "Target";
    private ArrayList defaultpermissionplatformlist;
    private final String itemtypeAttributenameSeparator = ".";
    private final String errormessage_incorrect_filecontent = "importexportuserrightshelper.import.exception.description";
    private CSVReader csvreader;
    private CSVWriter csvwriter;


    public ImportExportUserRightsHelper(Reader reader)
    {
        setDefaultPermissionListWithDefaultHybrisParam();
        this.csvreader = new CSVReader(reader);
    }


    public ImportExportUserRightsHelper(Reader reader, boolean exportallprincipals)
    {
        this(reader);
        setFlag_exportAllPrincipals(exportallprincipals);
    }


    public ImportExportUserRightsHelper(Reader reader, char textseparator, char fieldseparator, char dataseparator, boolean exportallprincipals)
    {
        this(reader, exportallprincipals);
        this.csvreader.setTextSeparator(textseparator);
        this.csvreader.setFieldSeparator(new char[] {fieldseparator});
        setDataSeparator(dataseparator);
    }


    public ImportExportUserRightsHelper(Reader reader, char textseparator, char fieldseparator, char dataseparator, boolean exportallprincipals, ArrayList permissionlist)
    {
        this(reader, textseparator, fieldseparator, dataseparator, exportallprincipals);
        this.defaultpermissionplatformlist = permissionlist;
    }


    public ImportExportUserRightsHelper(Writer writer)
    {
        setDefaultPermissionListWithDefaultHybrisParam();
        this.csvwriter = new CSVWriter(writer);
    }


    public ImportExportUserRightsHelper(Writer writer, boolean exportallprincipals)
    {
        this(writer);
        setFlag_exportAllPrincipals(exportallprincipals);
    }


    public ImportExportUserRightsHelper(Writer writer, char textseparator, char fieldseparator, char dataseparator, boolean exportallprincipals)
    {
        this(writer, exportallprincipals);
        this.csvwriter.setTextseparator(textseparator);
        this.csvwriter.setFieldseparator(fieldseparator);
        setDataSeparator(dataseparator);
    }


    public ImportExportUserRightsHelper(Writer writer, char textseparator, char fieldseparator, char dataseparator, boolean exportallprincipals, ArrayList permissionlist)
    {
        this(writer, textseparator, fieldseparator, dataseparator, exportallprincipals);
        this.defaultpermissionplatformlist = permissionlist;
    }


    private void addDataSetToPlatform(ArrayList<HashMap> csvdata)
    {
        Map erstezeile = csvdata.get(0);
        String typestring = (String)erstezeile.get(Integer.valueOf(0));
        String uidstring = (String)erstezeile.get(Integer.valueOf(1));
        List<String> groups = cutGroups((String)erstezeile.get(Integer.valueOf(2)));
        String password = (String)erstezeile.get(Integer.valueOf(3));
        boolean isPasswordProvided = ("DO_NOT_TOUCH_PASSWORD" != password);
        try
        {
            ComposedType ct = TypeManager.getInstance().getComposedType(typestring);
            if(UserGroup.class.isAssignableFrom(ct.getJaloClass()))
            {
                UserGroup ug;
                try
                {
                    ug = UserManager.getInstance().getUserGroupByGroupID(uidstring);
                }
                catch(JaloItemNotFoundException e)
                {
                    Map<Object, Object> m = new HashMap<>();
                    m.put("uid", uidstring);
                    try
                    {
                        ug = (UserGroup)ct.newInstance(m);
                    }
                    catch(JaloGenericCreationException e1)
                    {
                        throw new JaloSystemException(e1);
                    }
                    catch(JaloAbstractTypeException e1)
                    {
                        throw new RuntimeException(e1);
                    }
                }
                for(int i = 0; i < groups.size(); i++)
                {
                    UserGroup temp_ug = JaloSession.getCurrentSession().getUserManager().getUserGroupByGroupID(groups.get(i));
                    try
                    {
                        ug.addToGroup((PrincipalGroup)temp_ug);
                    }
                    catch(JaloInvalidParameterException jaloInvalidParameterException)
                    {
                    }
                }
                if(csvdata.size() > 1)
                {
                    csvdata.remove(0);
                    setTypeRights((Principal)ug, csvdata);
                }
            }
            else if(User.class.isAssignableFrom(ct.getJaloClass()))
            {
                User user = createOrModifyUser(ct, uidstring, isPasswordProvided, password);
                for(int i = 0; i < groups.size(); i++)
                {
                    UserGroup temp_u = JaloSession.getCurrentSession().getUserManager().getUserGroupByGroupID(groups.get(i));
                    try
                    {
                        user.addToGroup((PrincipalGroup)temp_u);
                    }
                    catch(JaloInvalidParameterException jaloInvalidParameterException)
                    {
                    }
                }
                if(csvdata.size() > 1)
                {
                    csvdata.remove(0);
                    setTypeRights((Principal)user, csvdata);
                }
            }
        }
        catch(JaloItemNotFoundException e)
        {
            System.err.println(e.getLocalizedMessage());
        }
    }


    private User createOrModifyUser(ComposedType ct, String uid, boolean isPasswordProvided, String password)
    {
        ImmutableMap immutableMap;
        UserModel user;
        UserDao userDao = (UserDao)Registry.getCoreApplicationContext().getBean("userDao", UserDao.class);
        ModelService modelService = (ModelService)Registry.getCoreApplicationContext().getBean("modelService", ModelService.class);
        SessionService sessionService = (SessionService)Registry.getCoreApplicationContext().getBean("sessionService", SessionService.class);
        UserModel existingUser = userDao.findUserByUID(uid);
        if(existingUser == null)
        {
            user = (UserModel)modelService.create(ct.getCode());
            user.setUid(uid);
            immutableMap = ImmutableMap.of("impex.creation", Boolean.TRUE);
        }
        else
        {
            user = existingUser;
            immutableMap = ImmutableMap.of("impex.modification", Boolean.TRUE, "impex.modification.pk",
                            Collections.singleton(user.getPk()));
        }
        if(isPasswordProvided)
        {
            user.setPassword(password);
        }
        sessionService.executeInLocalViewWithParams((Map)immutableMap, (SessionExecutionBody)new Object(this, modelService, user));
        return (User)JaloSession.lookupItem(user.getPk());
    }


    private List cutGroups(String groupstring)
    {
        StringTokenizer st = new StringTokenizer(groupstring, String.valueOf(getDataSeparator()));
        List<String> templist = new ArrayList();
        while(st.hasMoreTokens())
        {
            String element = st.nextToken();
            templist.add(element);
        }
        return templist;
    }


    private void exportOneUser(User u) throws IOException
    {
        Map umatrix = u.getItemPermissionsMap(this.defaultpermissionplatformlist);
        if(!umatrix.isEmpty() || isFlag_exportAllPrincipals())
        {
            Map<Object, Object> templist = new HashMap<>();
            templist.put(Integer.valueOf(0), u.getComposedType().getCode());
            templist.put(Integer.valueOf(1), u.getUID());
            boolean drin = false;
            String tempstring = "";
            for(Iterator<UserGroup> iterator1 = u.getAllGroups().iterator(); iterator1.hasNext(); )
            {
                UserGroup sub_ug = iterator1.next();
                tempstring = tempstring + tempstring + sub_ug.getUID();
                drin = true;
            }
            if(drin && tempstring.length() > 0)
            {
                tempstring = tempstring.substring(0, tempstring.length() - 1);
            }
            templist.put(Integer.valueOf(2), tempstring);
            templist.put(Integer.valueOf(3), (u.getEncodedPassword() == null) ? "" : u.getEncodedPassword());
            templist.put(Integer.valueOf(4), "");
            for(int i = 0; i < this.defaultpermissionplatformlist.size(); i++)
            {
                templist.put(Integer.valueOf(5 + i), "");
            }
            this.csvwriter.write(templist);
            for(Iterator<Map.Entry> iterator = umatrix.entrySet().iterator(); iterator.hasNext(); )
            {
                templist.clear();
                Map.Entry e = iterator.next();
                Item item = (Item)e.getKey();
                List permissionBooleanList = (List)e.getValue();
                templist.put(Integer.valueOf(0), "");
                templist.put(Integer.valueOf(1), "");
                templist.put(Integer.valueOf(2), "");
                templist.put(Integer.valueOf(3), "");
                if(item instanceof Type)
                {
                    templist.put(Integer.valueOf(4), ((Type)item).getCode());
                }
                else if(item instanceof AttributeDescriptor)
                {
                    templist.put(Integer.valueOf(4), ((AttributeDescriptor)item).getEnclosingType().getCode() + "." + ((AttributeDescriptor)item).getEnclosingType().getCode());
                }
                else
                {
                    templist.put(Integer.valueOf(4), "");
                }
                for(int j = 0; j < permissionBooleanList.size(); j++)
                {
                    templist.put(Integer.valueOf(5), writeCSVPermissionValue(permissionBooleanList.get(j)));
                }
                this.csvwriter.write(templist);
            }
        }
    }


    private void exportOneUserGroup(UserGroup ug) throws IOException
    {
        Map ugmatrix = ug.getItemPermissionsMap(this.defaultpermissionplatformlist);
        if(!ugmatrix.isEmpty() || isFlag_exportAllPrincipals())
        {
            Map<Object, Object> templist = new HashMap<>();
            templist.put(Integer.valueOf(0), ug.getComposedType().getCode());
            templist.put(Integer.valueOf(1), ug.getUID());
            boolean drin = false;
            String tempstring = "";
            for(Iterator<UserGroup> iterator1 = ug.getAllGroups().iterator(); iterator1.hasNext(); )
            {
                UserGroup sub_ug = iterator1.next();
                tempstring = tempstring + tempstring + sub_ug.getUID();
                drin = true;
            }
            if(drin && tempstring.length() > 0)
            {
                tempstring = tempstring.substring(0, tempstring.length() - 1);
            }
            templist.put(Integer.valueOf(2), tempstring);
            templist.put(Integer.valueOf(3), "");
            templist.put(Integer.valueOf(4), "");
            for(int i = 0; i < this.defaultpermissionplatformlist.size(); i++)
            {
                templist.put(Integer.valueOf(5 + i), "");
            }
            this.csvwriter.write(templist);
            for(Iterator<Map.Entry> iterator = ugmatrix.entrySet().iterator(); iterator.hasNext(); )
            {
                templist.clear();
                Map.Entry e = iterator.next();
                Item item = (Item)e.getKey();
                List permissionBooleanList = (List)e.getValue();
                templist.put(Integer.valueOf(0), "");
                templist.put(Integer.valueOf(1), "");
                templist.put(Integer.valueOf(2), "");
                templist.put(Integer.valueOf(3), "");
                if(item instanceof Type)
                {
                    templist.put(Integer.valueOf(4), ((Type)item).getCode());
                }
                else if(item instanceof AttributeDescriptor)
                {
                    templist.put(Integer.valueOf(4), ((AttributeDescriptor)item).getEnclosingType().getCode() + "." + ((AttributeDescriptor)item).getEnclosingType().getCode());
                }
                else
                {
                    templist.put(Integer.valueOf(4), "");
                }
                for(int j = 0; j < permissionBooleanList.size(); j++)
                {
                    templist.put(Integer.valueOf(5 + j), writeCSVPermissionValue(permissionBooleanList.get(j)));
                }
                this.csvwriter.write(templist);
            }
        }
    }


    public void exportSecurity()
    {
        Set allUserAndUserGroups = new HashSet();
        allUserAndUserGroups.addAll(UserManager.getInstance().getAllUserGroups());
        allUserAndUserGroups.addAll(UserManager.getInstance().getAllUsers());
        exportSecurity(allUserAndUserGroups);
    }


    public void exportSecurity(Set principallist)
    {
        List<Principal> users = new ArrayList();
        Set<Principal> grouppool = new HashSet();
        for(Iterator<Principal> i = principallist.iterator(); i.hasNext(); )
        {
            Principal p = i.next();
            if(p instanceof User)
            {
                users.add(p);
            }
            if(p instanceof UserGroup)
            {
                grouppool.add(p);
            }
            for(Iterator<Principal> sub_i = p.getAllGroups().iterator(); sub_i.hasNext(); )
            {
                grouppool.add(sub_i.next());
            }
        }
        Collection<UserGroup> root = new HashSet();
        for(Iterator<Principal> iter = grouppool.iterator(); iter.hasNext(); )
        {
            UserGroup ug = (UserGroup)iter.next();
            if(ug.getGroups().isEmpty())
            {
                root.add(ug);
            }
        }
        Set<UserGroup> done = new HashSet();
        try
        {
            writeCSVHeaderToWriter();
            while(!root.isEmpty())
            {
                Collection<Principal> newLevel = new HashSet();
                for(Iterator<UserGroup> iterator1 = root.iterator(); iterator1.hasNext(); )
                {
                    UserGroup ug = iterator1.next();
                    done.add(ug);
                    exportOneUserGroup(ug);
                    for(Iterator<Principal> iterator2 = ug.getMembers().iterator(); iterator2.hasNext(); )
                    {
                        Principal member = iterator2.next();
                        if(member instanceof UserGroup && !done.contains(member) && grouppool.contains(member))
                        {
                            newLevel.add(member);
                        }
                    }
                }
                Collection<Principal> collection1 = newLevel;
            }
            for(Iterator<Principal> iterator = users.iterator(); iterator.hasNext(); )
            {
                User em = (User)iterator.next();
                exportOneUser(em);
            }
        }
        catch(IOException e)
        {
            System.err.println(e);
        }
        finally
        {
            try
            {
                this.csvwriter.close();
            }
            catch(IOException iOException)
            {
            }
        }
    }


    public char getDataSeparator()
    {
        return this.datasep;
    }


    public ArrayList getDefaultPermissionList()
    {
        if(this.defaultpermissionplatformlist == null)
        {
            setDefaultPermissionListWithDefaultHybrisParam();
        }
        return this.defaultpermissionplatformlist;
    }


    public int importSecurity() throws JaloInvalidParameterException
    {
        int datasetcount = 0;
        ArrayList<Map<Integer, String>> csvdata = new ArrayList();
        this.csvreader.readNextLine();
        Map<Integer, String> csvlinemap = this.csvreader.getLine();
        if(csvlinemap == null)
        {
            throw new JaloInvalidParameterException("could not parse file", 5557);
        }
        if(!csvlinemap.containsValue("Type") || !csvlinemap.containsValue("UID") ||
                        !csvlinemap.containsValue("MemberOfGroups") || !csvlinemap.containsValue("Target"))
        {
            throw new JaloInvalidParameterException(Localization.getLocalizedString("importexportuserrightshelper.import.exception.description"), 5556);
        }
        boolean noPasswordColumn = !csvlinemap.containsValue("Password");
        while(this.csvreader.readNextLine())
        {
            csvlinemap = addPasswordFieldIfNeeded(this.csvreader.getLine(), noPasswordColumn);
            if(!csvlinemap.get(Integer.valueOf(0)).equals(""))
            {
                if(!csvdata.isEmpty())
                {
                    addDataSetToPlatform(csvdata);
                    datasetcount++;
                    csvdata.clear();
                }
                csvdata.add(csvlinemap);
                continue;
            }
            csvdata.add(csvlinemap);
        }
        if(!csvdata.isEmpty())
        {
            addDataSetToPlatform(csvdata);
            datasetcount++;
            csvdata.clear();
        }
        try
        {
            this.csvreader.close();
        }
        catch(IOException e)
        {
            System.err.println(e);
        }
        return datasetcount;
    }


    private Map<Integer, String> addPasswordFieldIfNeeded(Map<Integer, String> line, boolean noPasswordColumn)
    {
        if(noPasswordColumn)
        {
            HashMap<Integer, String> result = new HashMap<>();
            line.forEach((key, value) -> {
                if(key.intValue() >= 3)
                {
                    key = Integer.valueOf(key.intValue() + 1);
                }
                result.put(key, value);
            });
            result.put(Integer.valueOf(3), "DO_NOT_TOUCH_PASSWORD");
            return result;
        }
        return line;
    }


    public boolean isFlag_exportAllPrincipals()
    {
        return this.flag_exportAllPrincipals;
    }


    private Boolean readCSVPermissionValue(String o)
    {
        if("+".equals(o))
        {
            return Boolean.FALSE;
        }
        if("-".equals(o))
        {
            return Boolean.TRUE;
        }
        return null;
    }


    public void setDataSeparator(char dataseparator)
    {
        this.datasep = dataseparator;
    }


    public void setDefaultPermissionList(ArrayList permissionlist)
    {
        this.defaultpermissionplatformlist = permissionlist;
    }


    private void setDefaultPermissionListWithDefaultHybrisParam()
    {
        UserRight read = AccessManager.getInstance().getOrCreateUserRightByCode("read");
        UserRight create = AccessManager.getInstance().getOrCreateUserRightByCode("create");
        UserRight remove = AccessManager.getInstance().getOrCreateUserRightByCode("remove");
        UserRight change = AccessManager.getInstance().getOrCreateUserRightByCode("change");
        UserRight changeperm = AccessManager.getInstance().getOrCreateUserRightByCode("changerights");
        this.defaultpermissionplatformlist = new ArrayList();
        this.defaultpermissionplatformlist.add(read);
        this.defaultpermissionplatformlist.add(change);
        this.defaultpermissionplatformlist.add(create);
        this.defaultpermissionplatformlist.add(remove);
        this.defaultpermissionplatformlist.add(changeperm);
    }


    public void setFlag_exportAllPrincipals(boolean flag_usesAllPrincipals)
    {
        this.flag_exportAllPrincipals = flag_usesAllPrincipals;
    }


    private void setTypeRights(Principal p, List<HashMap> typesToSet)
    {
        Map<Object, Object> permissionItemMatrix = new HashMap<>(p.getItemPermissionsMap(this.defaultpermissionplatformlist));
        for(int i = 0; i < typesToSet.size(); i++)
        {
            List<Boolean> templist = new ArrayList();
            Map typeentry = typesToSet.get(i);
            int actualSize = typeentry.size();
            int requiredSize = 5 + this.defaultpermissionplatformlist.size();
            if(actualSize >= requiredSize)
            {
                String typename = (String)typeentry.get(Integer.valueOf(4));
                try
                {
                    Object ct = null;
                    if(typename.indexOf(".") == -1)
                    {
                        ct = JaloSession.getCurrentSession().getTypeManager().getComposedType(typename);
                    }
                    else
                    {
                        String type = typename.substring(0, typename.indexOf("."));
                        String attr = typename.substring(typename.indexOf(".") + 1, typename
                                        .length());
                        ComposedType x = JaloSession.getCurrentSession().getTypeManager().getComposedType(type);
                        ct = x.getAttributeDescriptor(attr);
                    }
                    for(int j = 0; j < this.defaultpermissionplatformlist.size(); j++)
                    {
                        Boolean value = readCSVPermissionValue((String)typeentry.get(Integer.valueOf(5 + j)));
                        templist.add(value);
                    }
                    permissionItemMatrix.put(ct, templist);
                }
                catch(JaloItemNotFoundException jaloItemNotFoundException)
                {
                }
            }
            else
            {
                log.warn("Cannot parse type \"" + (String)typeentry.get(Integer.valueOf(4)) + "\" for principal \"" + p.getUID() + "\". Not enough columns - ignoring line!");
            }
        }
        p.setItemPermissionsByMap(this.defaultpermissionplatformlist, permissionItemMatrix);
    }


    private void writeCSVHeaderToWriter() throws IOException
    {
        Map<Object, Object> csvHeaderNameMap = new HashMap<>();
        csvHeaderNameMap.put(Integer.valueOf(0), "Type");
        csvHeaderNameMap.put(Integer.valueOf(1), "UID");
        csvHeaderNameMap.put(Integer.valueOf(2), "MemberOfGroups");
        csvHeaderNameMap.put(Integer.valueOf(3), "Password");
        csvHeaderNameMap.put(Integer.valueOf(4), "Target");
        for(int i = 0; i < this.defaultpermissionplatformlist.size(); i++)
        {
            csvHeaderNameMap.put(Integer.valueOf(i + 5), ((UserRight)this.defaultpermissionplatformlist.get(i)).getCode());
        }
        this.csvwriter.write(csvHeaderNameMap);
    }


    private String writeCSVPermissionValue(Object o)
    {
        if(o instanceof Boolean)
        {
            if(o.equals(Boolean.TRUE))
            {
                return "-";
            }
            if(o.equals(Boolean.FALSE))
            {
                return "+";
            }
        }
        return ".";
    }
}
