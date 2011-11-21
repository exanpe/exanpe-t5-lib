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

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;

import fr.exanpe.t5.lib.constants.ExanpeEventConstants;

public class VerticalMenuTest
{
    @Inject
    private Logger logger;

    @OnEvent(ExanpeEventConstants.VERTICALMENU_EVENT)
    Object handleSelectMenu(String menuId)
    {
        System.out.println(">> select menu item id: " + menuId);
        String targetUrl = "http://www.google.fr";
        if ("external".equals(menuId))
        {
            URL external = null;
            try
            {
                external = new URL(targetUrl);
            }
            catch (MalformedURLException ex)
            {
                logger.error(String.format("Incorrect external url: %s", targetUrl));
                throw new RuntimeException(ex);
            }
            return external;
        }
        return this;
    }
}
