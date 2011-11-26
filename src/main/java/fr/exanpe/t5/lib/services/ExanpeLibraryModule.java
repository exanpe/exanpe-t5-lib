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
package fr.exanpe.t5.lib.services;

import java.awt.Color;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ObjectLocator;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.services.Coercion;
import org.apache.tapestry5.ioc.services.CoercionTuple;
import org.apache.tapestry5.services.ComponentClassResolver;
import org.apache.tapestry5.services.ComponentClassTransformWorker;
import org.apache.tapestry5.services.InjectionProvider;
import org.apache.tapestry5.services.LibraryMapping;
import org.apache.tapestry5.util.StringToEnumCoercion;
import org.slf4j.Logger;

import fr.exanpe.t5.lib.constants.AccordionEventTypeEnum;
import fr.exanpe.t5.lib.constants.DialogRenderModeEnum;
import fr.exanpe.t5.lib.constants.ExanpeSymbols;
import fr.exanpe.t5.lib.constants.MenuEventTypeEnum;
import fr.exanpe.t5.lib.constants.PasswordStrengthCheckerTypeEnum;
import fr.exanpe.t5.lib.constants.SecurePasswordEventTypeEnum;
import fr.exanpe.t5.lib.constants.SliderOrientationTypeEnum;
import fr.exanpe.t5.lib.internal.AuthorizeWorker;
import fr.exanpe.t5.lib.services.impl.AuthorizeBusinessServiceImpl;

/**
 * The Tapestry Module for Exanpe Library.
 * 
 * @author lguerin
 */
public class ExanpeLibraryModule
{
    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration, Logger log)
    {
        // Mapping for exanpe prexix
        configuration.add(new LibraryMapping("exanpe", "fr.exanpe.t5.lib"));
        log.info("Registering Exanpe library");
    }

    public static void contributeTypeCoercer(@SuppressWarnings("rawtypes")
    Configuration<CoercionTuple> configuration)
    {
        configuration
                .add(CoercionTuple.create(String.class, SecurePasswordEventTypeEnum.class, StringToEnumCoercion.create(SecurePasswordEventTypeEnum.class)));
        configuration.add(CoercionTuple.create(String.class, AccordionEventTypeEnum.class, StringToEnumCoercion.create(AccordionEventTypeEnum.class)));
        configuration.add(CoercionTuple.create(String.class, DialogRenderModeEnum.class, StringToEnumCoercion.create(DialogRenderModeEnum.class)));
        configuration.add(CoercionTuple.create(String.class, SliderOrientationTypeEnum.class, StringToEnumCoercion.create(SliderOrientationTypeEnum.class)));
        configuration.add(CoercionTuple.create(String.class, MenuEventTypeEnum.class, StringToEnumCoercion.create(MenuEventTypeEnum.class)));
        configuration.add(CoercionTuple.create(
                String.class,
                PasswordStrengthCheckerTypeEnum.class,
                StringToEnumCoercion.create(PasswordStrengthCheckerTypeEnum.class)));

        // ColorPicker
        Coercion<String, Color> coercionStringColor = new Coercion<String, Color>()
        {
            public Color coerce(String input)
            {
                if (StringUtils.isEmpty(input))
                    return null;
                return Color.decode("0x" + input);
            }
        };

        configuration.add(new CoercionTuple<String, Color>(String.class, Color.class, coercionStringColor));

        Coercion<Color, String> coercionColorString = new Coercion<Color, String>()
        {
            public String coerce(Color input)
            {
                if (input == null)
                    return null;

                String rgb = Integer.toHexString(input.getRGB());
                rgb = rgb.substring(2, rgb.length());
                return rgb;
            }
        };

        configuration.add(new CoercionTuple<Color, String>(Color.class, String.class, coercionColorString));
    }

    public static void contributeFactoryDefaults(MappedConfiguration<String, String> configuration)
    {
        configuration.add(ExanpeSymbols.ASSET_BASE, "classpath:fr/exanpe/t5/lib/components");
        configuration.add(ExanpeSymbols.YUI2_BASE, "classpath:fr/exanpe/t5/lib/external/js/yui/2.9.0/");
    }

    public static void contributeComponentClassTransformWorker(OrderedConfiguration<ComponentClassTransformWorker> configuration, ObjectLocator locator,
            InjectionProvider injectionProvider, ComponentClassResolver resolver)
    {
        configuration.addInstance("AuthorizeWorker", AuthorizeWorker.class, "before:OnEvent");
    }

    public static void bind(ServiceBinder binder)
    {
        binder.bind(ExanpeComponentService.class, ExanpeComponentService.class);
        binder.bind(AuthorizeBusinessService.class, AuthorizeBusinessServiceImpl.class);
    }
}
