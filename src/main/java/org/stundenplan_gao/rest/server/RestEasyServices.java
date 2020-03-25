package org.stundenplan_gao.rest.server;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.stundenplan_gao.rest.JWTFilter.JWTTokenFilter;
import org.stundenplan_gao.rest.JWTFilter.JWTUsernameFilter;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@ApplicationPath("/stundenplan")
public class RestEasyServices extends Application {

    private Set<Object> singletons = new HashSet<>();

    public RestEasyServices() {
        singletons.add(new StundenplanSchuelerService());
        singletons.add(new JWTTokenFilter());
        singletons.add(new JWTUsernameFilter());
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }

    @Override
    public Set<Class<?>> getClasses() {
        return super.getClasses();
    }

    @Override
    public Map<String, Object> getProperties() {
        return super.getProperties();
    }
}
