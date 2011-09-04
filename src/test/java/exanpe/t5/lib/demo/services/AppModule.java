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

package exanpe.t5.lib.demo.services;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.apache.tapestry5.ioc.services.Coercion;
import org.apache.tapestry5.ioc.services.CoercionTuple;
import org.apache.tapestry5.util.StringToEnumCoercion;

import exanpe.t5.lib.demo.bean.CountryEnum;
import fr.exanpe.t5.lib.services.ExanpeLibraryModule;

@SubModule(ExanpeLibraryModule.class)
public class AppModule
{
    public static void contributeApplicationDefaults(MappedConfiguration<String, String> configuration)
    {
        configuration.add(SymbolConstants.PRODUCTION_MODE, "false");
    }

    public static void contributeTypeCoercer(@SuppressWarnings("rawtypes")
    Configuration<CoercionTuple> configuration)
    {
        configuration.add(CoercionTuple.create(String.class, CountryEnum.class, StringToEnumCoercion.create(CountryEnum.class)));

        // ColorPicker
        Coercion<CountryEnum, String> coercionStringColor = new Coercion<CountryEnum, String>()
        {
            public String coerce(CountryEnum input)
            {
                return input.toString();
            }
        };
    }

    public static void bind(ServiceBinder binder)
    {
        binder.bind(DataService.class, DataService.class);
    }
}
