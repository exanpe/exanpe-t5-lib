package exanpe.t5.lib.demo.services;

import java.util.ArrayList;
import java.util.List;

import exanpe.t5.lib.demo.bean.CountryEnum;
import exanpe.t5.lib.demo.bean.User;

public class DataService
{
    private User createUser(int id, String name, String firstName, int age)
    {
        User u = new User();
        u.setId(id);
        u.setLastName(name);
        u.setFirstName(firstName);
        u.setAge(age);
        return u;
    }

    public List<User> getListUsers()
    {
        List<User> users = new ArrayList<User>();

        for (int i = 0; i < 50; i++)
            users.add(createUser(i, "Name " + i, "First Name " + i, 20 + i));

        return users;
    }

    public List<User> getListUsersWithLatence(int latence) throws InterruptedException
    {
        List<User> users = new ArrayList<User>();

        for (int i = 0; i < 10; i++)
        {
            users.add(createUser(i, "Name " + i, "First Name " + i, 20 + i));
        }
        Thread.sleep(latence);
        return users;
    }

    public List<String> getListString()
    {
        List<String> list = new ArrayList<String>();

        for (int i = 0; i < 30; i++)
            list.add("Hello World " + i);

        return list;
    }

    public Class<? extends Enum<?>> getCityFromCountry(CountryEnum e)
    {
        return e.getRelatedEnum();
    }
}
