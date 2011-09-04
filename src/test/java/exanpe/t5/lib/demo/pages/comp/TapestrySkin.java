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

import java.util.List;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.util.EnumSelectModel;
import org.apache.tapestry5.util.EnumValueEncoder;

import exanpe.t5.lib.demo.bean.User;
import exanpe.t5.lib.demo.services.DataService;

@Import(stylesheet = "${exanpe.asset-base}/css/exanpe-t5-lib-skin.css")
public class TapestrySkin
{
    @Inject
    private DataService dataService;

    @Inject
    private Messages messages;

    @Property
    private User userRow;

    @Property
    @Persist
    private Integer current;

    @InjectComponent
    private Grid grid1;

    @Inject
    private AssetSource assetSource;

    public Asset getSelectImg()
    {
        return assetSource.getExpandedAsset("${exanpe.asset-base}/img/t5/select.png");
    }

    public Asset getDeselectImg()
    {
        return assetSource.getExpandedAsset("${exanpe.asset-base}/img/t5/deselect.png");
    }

    public List<User> getUsers()
    {
        return dataService.getListUsers();
    }

    @Property
    private EvenOdd evenOdd;

    @SetupRender
    public void init()
    {
        evenOdd = new EvenOdd();
        if (current == null)
        {
            current = 1;
        }
        grid1.setCurrentPage(current);
    }

    /** Auto COmplete **/
    @Property
    private String country;

    public List<String> onProvideCompletionsFromCountryName(String partial)
    {
        return dataService.getListString();
    }

    /** Palette **/

    @Property
    private final ValueEncoder<PaletteEnum> encoder = new EnumValueEncoder(PaletteEnum.class);

    @Property
    private final SelectModel model = new EnumSelectModel(PaletteEnum.class, messages);

    @Property
    private List<PaletteEnum> selected;

    public enum PaletteEnum
    {
        TEST, TEST2, TEST3, TEST4;
    }

    /** BeanEditor */
    @Property
    private User userEdit;

    /**
     * @see http://jumpstart.doublenegative.com.au/jumpstart/examples/tables/alternatinggrid
     * @author jmaupoux
     */
    public class EvenOdd
    {

        private boolean even = true;

        /**
         * Returns "even" or "odd". Whatever it returns on one invocation, it will return the
         * opposite on the next. By
         * default, the first value returned is "even".
         */
        public String getNext()
        {
            String result = even ? "even" : "odd";
            even = !even;
            return result;
        }

        public boolean isEven()
        {
            return even;
        }

        /**
         * Overrides the even flag.
         */
        public void setEven(boolean value)
        {
            even = value;
        }
    }

}
