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
