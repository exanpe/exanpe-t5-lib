package exanpe.t5.lib.demo.pages.security;

import fr.exanpe.t5.lib.annotation.Authorize;

@Authorize(all = "ROLE_ADMIN")
public class AdminOnly
{

}
