package exanpe.t5.lib.demo.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;

public class SameUserPasswordAP implements AuthenticationProvider
{

    public Authentication authenticate(Authentication authentication) throws AuthenticationException
    {
        String username = authentication.getName();
        System.out.println(username);
        GrantedAuthorityImpl ga = new GrantedAuthorityImpl("ROLE_" + username);
        List<GrantedAuthority> l = new ArrayList<GrantedAuthority>();
        l.add(ga);

        return new UsernamePasswordAuthenticationToken(new User(username, username, true, true, true, true, l), username, l);
    }

    public boolean supports(Class<? extends Object> authentication)
    {
        return true;
    }

}
