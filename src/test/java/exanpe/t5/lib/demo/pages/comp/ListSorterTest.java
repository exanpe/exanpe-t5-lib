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

/**
 * 
 */
package exanpe.t5.lib.demo.pages.comp;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;

import fr.exanpe.t5.lib.components.ListSorter;

/**
 * The demo page for the {@link ListSorter} component.
 * 
 * @author jmaupoux
 */
public class ListSorterTest
{
    @Property
    @Persist
    private List<String> list;

    @Property
    private String element;

    void onActivate()
    {
        if (list == null)
        {
            list = new ArrayList<String>();
            list.add("String1");
            list.add("String2");
        }
    }

}
