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
