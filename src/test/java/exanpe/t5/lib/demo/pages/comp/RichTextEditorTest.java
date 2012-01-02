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

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import fr.exanpe.t5.lib.mixins.RichTextEditor;

/**
 * The demo page for {@link RichTextEditor} component
 * 
 * @author lguerin
 */
public class RichTextEditorTest
{
    @Property
    @Persist(PersistenceConstants.FLASH)
    private String richContent;

    @Property
    @Persist(PersistenceConstants.FLASH)
    private String richContent2;

    @Inject
    @Property
    private Messages messages;

    void onSubmit()
    {
        System.out.println(">>> Rich content: " + richContent);
        System.out.println(">>> Rich content2: " + richContent2);
    }
}
