package com.imberda;

import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.realm.GenericPrincipal;
import org.apache.catalina.valves.ValveBase;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class PrincipleProviderValve extends ValveBase {

    private static final Log log = LogFactory.getLog(PrincipleProviderValve.class);

    public static final String USERNAME_PROPERTY_KEY = "principle.provider.username";
    public static final String ROLES_PROPERTY_KEY = "principle.provider.roles";

    private String userName;
    private String roles;

    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {
        if(log.isDebugEnabled()) log.debug("Entering PrincipleProviderValve valve pipeline");

        final String userName = getUserName();
        final List<String> roles = getRoles();

        final GenericPrincipal principal = new GenericPrincipal(userName, userName, roles);
        request.setUserPrincipal(principal);
        if(log.isDebugEnabled()) log.debug("Setting principle into request:: " + principal);

        if(log.isDebugEnabled()) log.debug("Invoking next valve in pipeline.");
        getNext().invoke(request, response);
        if(log.isDebugEnabled()) log.debug("Exiting PrincipleProviderValve valve");

    }

    protected String getUserName(){
        final String userName = (System.getProperty(USERNAME_PROPERTY_KEY) != null ? System.getProperty(USERNAME_PROPERTY_KEY) : this.userName);
        if(userName == null){
            throw new IllegalStateException("Principle username cannot be null. Set system property " + USERNAME_PROPERTY_KEY + " or 'userName' attribute in server.xml");
        }

        if(log.isDebugEnabled()) log.debug("Resolves userName: " + userName);
        return userName;
    }

    protected List<String> getRoles(){
        final String rolesString = (System.getProperty(ROLES_PROPERTY_KEY) != null ? System.getProperty(ROLES_PROPERTY_KEY) : this.roles);
        if(rolesString == null){
            throw new IllegalStateException("Principle role cannot be null. Set system property " + ROLES_PROPERTY_KEY + " or 'roles' attribute in server.xml");
        }

        final String[] roles = rolesString.split(",");

        if(log.isDebugEnabled()) log.debug("Resolves roles: " + rolesString);
        return Arrays.asList(roles);
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }
}
