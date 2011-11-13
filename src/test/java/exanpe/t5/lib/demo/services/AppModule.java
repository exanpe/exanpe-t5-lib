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
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.apache.tapestry5.services.ValueEncoderFactory;
import org.apache.tapestry5.services.ValueEncoderSource;

import exanpe.t5.lib.demo.bean.Country;
import exanpe.t5.lib.demo.encoders.CountryEncoder;
import fr.exanpe.t5.lib.services.ExanpeLibraryModule;

@SubModule(ExanpeLibraryModule.class)
public class AppModule
{
    public static void contributeApplicationDefaults(MappedConfiguration<String, String> configuration)
    {
        configuration.add(SymbolConstants.PRODUCTION_MODE, "false");
    }

    @Contribute(ValueEncoderSource.class)
    public static void provideCountryEncoder(MappedConfiguration<Class, ValueEncoderFactory> configuration, final DataService dataService)
    {
        ValueEncoderFactory<Country> factory = new ValueEncoderFactory<Country>()
        {
            public ValueEncoder<Country> create(Class<Country> clazz)
            {
                return new CountryEncoder(dataService);
            }
        };
        configuration.add(Country.class, factory);
    }

    public static void bind(ServiceBinder binder)
    {
        binder.bind(DataService.class, DataService.class);
    }
}
