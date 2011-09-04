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
