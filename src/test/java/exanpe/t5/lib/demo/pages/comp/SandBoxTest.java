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

package exanpe.t5.lib.demo.pages.comp;

import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionAttribute;
import org.apache.tapestry5.annotations.SetupRender;

import exanpe.t5.lib.demo.bean.AjaxFormLoopEngine;
import exanpe.t5.lib.demo.bean.User;

public class SandBoxTest
{

    private static int id = 0;

    @SessionAttribute
    @Property
    private AjaxFormLoopEngine<User> engine;

    @Property
    private User iterator;

    @SetupRender
    void init()
    {
        if (engine == null)
        {
            engine = new AjaxFormLoopEngine<User>()
            {
                @Override
                public ValueEncoder<User> getEncoder()
                {
                    return new ValueEncoder<User>()
                    {

                        public String toClient(User value)
                        {
                            return value.getId() + "";
                        }

                        public User toValue(String keyAsString)
                        {

                            for (User user : engine.getList())
                            {
                                if (Long.parseLong(keyAsString) == user.getId()) { return user; }
                            }
                            throw new IllegalArgumentException("Received key \"" + keyAsString + "\" which has no counterpart in this users collection");
                        }
                    };
                }

                @Override
                public User instanciate()
                {
                    User u = new User();
                    u.setId(++id);
                    return u;
                }

            };
        }
    }

    public User onAddRowFromDynauser()
    {
        return engine.createNew();
    }

    public void onRemoveRowFromDynauser(User u)
    {
        engine.remove(u);
    }
}
