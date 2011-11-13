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

import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.SelectModelFactory;

import exanpe.t5.lib.demo.bean.Country;
import exanpe.t5.lib.demo.encoders.CountryEncoder;
import exanpe.t5.lib.demo.services.DataService;
import fr.exanpe.t5.lib.constants.ExanpeEventConstants;

public class LoggerTest
{
    @Inject
    private SelectModelFactory selectModelFactory;

    @Property
    private SelectModel countryModel;

    @Property
    private String countryValue;

    @Inject
    private DataService dataService;

    @SetupRender
    public void ini()
    {
        // invoke my service to find all countries, e.g. in the database
        List<Country> countries = dataService.getCountryList();

        // create a SelectModel from my list of countries
        countryModel = selectModelFactory.create(countries, "name");
    }

    @OnEvent(value = ExanpeEventConstants.SELECTLOADER_EVENT)
    public SelectModel populateSelect(String value)
    {
        throw new IllegalArgumentException("log me that");
    }

    public ValueEncoder<Country> getCountryEncoder()
    {
        return new CountryEncoder(dataService);
    }

}
