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
