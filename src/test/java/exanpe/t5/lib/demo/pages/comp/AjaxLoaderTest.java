package exanpe.t5.lib.demo.pages.comp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import exanpe.t5.lib.demo.bean.User;
import exanpe.t5.lib.demo.services.DataService;
import fr.exanpe.t5.lib.components.AjaxLoader;

/**
 * The demo page for the {@link AjaxLoader} component.
 * 
 * @author lguerin
 */
@Import(stylesheet =
{ "${exanpe.asset-base}/css/exanpe-t5-lib-skin.css" })
public class AjaxLoaderTest
{
    @Inject
    private DataService dataService;

    @Property
    private User userRow;

    @Property
    private String date;

    public void onActivate()
    {
        date = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format(new Date());
    }

    public List<User> getUsers1() throws InterruptedException
    {
        List<User> result;
        result = dataService.getListUsersWithLatence(2000);
        return result;
    }

    public List<User> getUsers2() throws InterruptedException
    {
        List<User> result;
        result = dataService.getListUsersWithLatence(5000);
        return result;
    }

}
