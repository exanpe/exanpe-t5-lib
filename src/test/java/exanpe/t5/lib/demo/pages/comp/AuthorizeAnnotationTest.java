//
// Copyright 2011 EXANPE <exanpe@gmail.com>
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package exanpe.t5.lib.demo.pages.comp;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import fr.exanpe.t5.lib.annotation.Authorize;

public class AuthorizeAnnotationTest
{

    @Inject
    private ApplicationContext applicationContext;

    private AuthenticationManager authenticationManager;

    void onActivate()
    {
        authenticationManager = (AuthenticationManager) applicationContext.getBean(applicationContext.getBeanNamesForType(AuthenticationManager.class)[0]);
    }

    public void onActionFromUser()
    {

        authenticate("USER");
    }

    public void onActionFromAdmin()
    {
        authenticate("ADMIN");
    }

    private void authenticate(String role)
    {
        Authentication request = new UsernamePasswordAuthenticationToken(role, role);
        Authentication result = authenticationManager.authenticate(request);
        SecurityContextHolder.getContext().setAuthentication(result);
    }

    @Persist(PersistenceConstants.FLASH)
    @Property
    private String roleInvoked;

    @Authorize(all = "ROLE_USER")
    @OnEvent(value = EventConstants.ACTION, component = "act")
    public void invokeUser()
    {
        roleInvoked = "USER";
    }

    @Authorize(all = "ROLE_ADMIN")
    @OnEvent(value = EventConstants.ACTION, component = "act")
    public void invokeAdmin()
    {
        roleInvoked = "ADMIN";
    }

}
