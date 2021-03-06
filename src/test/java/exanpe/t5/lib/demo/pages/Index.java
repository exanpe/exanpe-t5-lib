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

package exanpe.t5.lib.demo.pages;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PageRenderLinkSource;

import exanpe.t5.lib.demo.pages.comp.PasswordStrengthCheckerTest;

/**
 * Default index page for all the tests.
 * 
 * @author lguerin
 */
public class Index
{
    @Inject
    private PageRenderLinkSource pageRender;

    @OnEvent(value = "passwordStrengthCheckerTest")
    Object goToPasswordStrengthCheckerTestPage()
    {
        Link link = pageRender.createPageRenderLink(PasswordStrengthCheckerTest.class);
        link.addParameter("param1", "test");
        return link;
    }
}
