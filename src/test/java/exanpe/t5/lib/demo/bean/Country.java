package exanpe.t5.lib.demo.bean;

public class Country
{
    private String id;
    private String name;

    public Country(String id, String name)
    {
        super();
        this.id = id;
        this.name = name;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
