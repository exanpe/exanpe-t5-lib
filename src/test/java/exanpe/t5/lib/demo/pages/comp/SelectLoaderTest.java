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

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.internal.SelectModelImpl;
import org.apache.tapestry5.internal.services.EnumValueEncoderFactory;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.util.EnumSelectModel;

import exanpe.t5.lib.demo.bean.CountryEnum;
import exanpe.t5.lib.demo.services.DataService;
import fr.exanpe.t5.lib.constants.ExanpeEventConstants;

public class SelectLoaderTest
{
    @Property
    private ValueEncoder<CountryEnum> countryEncoder = new EnumValueEncoderFactory().create(CountryEnum.class);

    @Property
    private SelectModel country;

    @Property
    private String countryValue;

    @Property
    @Persist
    private SelectModel city;

    @Property
    private String cityValue;

    @Inject
    private Messages messages;

    @Inject
    private DataService dataService;

    @SetupRender
    public void ini()
    {
        country = new EnumSelectModel(CountryEnum.class, messages);
        city = new SelectModelImpl(new OptionModelImpl[0]);
    }

    @OnEvent(value = ExanpeEventConstants.SELECTLOADER_EVENT)
    public SelectModel populateSelect(String value)
    {
        city = new EnumSelectModel(dataService.getCityFromCountry(CountryEnum.valueOf(value)), messages);

        return city;
    }
}
