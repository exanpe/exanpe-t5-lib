package exanpe.t5.lib.demo.bean;

public enum CountryEnum
{
    FRANCE(FranceCityEnum.class), UK(UKCityEnum.class), USA(USACityEnum.class);

    private Class<? extends Enum<?>> c;

    private CountryEnum(Class<? extends Enum<?>> c)
    {
        this.c = c;
    }

    public Class<? extends Enum<?>> getRelatedEnum()
    {
        return c;
    }
}
