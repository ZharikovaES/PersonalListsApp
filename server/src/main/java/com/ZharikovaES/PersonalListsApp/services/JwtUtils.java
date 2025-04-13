package com.ZharikovaES.PersonalListsApp.services;

import com.ZharikovaES.PersonalListsApp.models.Role;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class JwtUtils {

    private JwtUtils() {
        // private constructor to prevent instantiation
    }

    public static JwtAuthentication generate(DecodedJWT jwt) {
        JwtAuthentication jwtInfoToken = new JwtAuthentication();
        jwtInfoToken.setUsername(jwt.getSubject());
        jwtInfoToken.setFirstName(jwt.getClaim("firstName").asString());
        jwtInfoToken.setRoles(getRoles(jwt));
        return jwtInfoToken;
    }

    private static Set<Role> getRoles(DecodedJWT jwt) {
        List<String> roles = jwt.getClaim("roles").asList(String.class);
        Set<Role> roleSet = new HashSet<>();
        if (roles != null) {
            for (String role : roles) {
                roleSet.add(Role.valueOf(role));
            }
        }
        return roleSet;
    }
}
