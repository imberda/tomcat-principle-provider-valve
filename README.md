Tomcat Principle Provider Valve
===============================
Provides the ability to run your tomcat web app utilising container managed security on the your developer workstation without having to configure, provide or deal with authentcation.

This deals with the case where you have a tomcat web app with a web.xml containing something like the following:

    ...
    <security-constraint>
        <web-resource-collection>
            <web-resource-name>some Resources which are protected</web-resource-name>
            <url-pattern>/*</url-pattern>
        </web-resource-collection>
        <auth-constraint>
            <role-name>role_123</role-name>
        </auth-constraint>
    </security-constraint>

    <security-role>
        <description>
            The role required to access the resources
        </description>
        <role-name>role123</role-name>
    </security-role>
    ...
    
Here notice that the element "\<login-config\>" is conspicuous by its absence. This means that although the web app expects the servlet container to police access to its resources its not saying how authentication should be performed. This is most commonly used in situations where custom authentication (i.e. not form based or basic auth) are being used.

If you have the above configuration in your webapp's web.xml and you access "/test.html" Tomcat will return you a "403 Forbidden" response code. This is because if neither knows who you are or what roles you are in. To get around this (in development!) you can add the following into the Tomcat "server.xml" file:

    <Valve className="com.imberda.PrincipleProviderValve" userName="john_doe" roles="role_abc,role_123"/>

In effect this will create a "Principle" in the request context and set the username to "john_doe" and set the roles "role_abc"  and role_123". The "userName" and "roles" can be set as XML attributes as shown above or can be set as JVM system properties (using "principle.provider.username" and "principle.provider.roles" respectively - note that if both system property and XML attributes are both set, then the system property take precedence).

**You also need to build this project into a Jar file and place it into your Tomcat "lib" directory and restart Tomcat for the changes to take effect.**

Happy developing.....
