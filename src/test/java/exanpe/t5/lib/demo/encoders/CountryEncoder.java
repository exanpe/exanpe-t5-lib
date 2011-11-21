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

package exanpe.t5.lib.demo.encoders;

import org.apache.tapestry5.ValueEncoder;

import exanpe.t5.lib.demo.bean.Country;
import exanpe.t5.lib.demo.services.DataService;

/**
 * Encoder for {@link Country} objects
 * 
 * @author lguerin
 */
public class CountryEncoder implements ValueEncoder<Country>
{

    private DataService dataService;

    public CountryEncoder(DataService dataService)
    {
        this.dataService = dataService;
    }

    public String toClient(Country value)
    {
        return String.valueOf(value.getId());
    }

    public Country toValue(String clientValue)
    {
        if (clientValue != null && !"".equalsIgnoreCase(clientValue)) { return dataService.findCountryById(clientValue); }
        return null;
    }

}
