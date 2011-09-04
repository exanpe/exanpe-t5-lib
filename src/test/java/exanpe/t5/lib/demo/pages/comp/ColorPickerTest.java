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

import java.awt.Color;

import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;

import fr.exanpe.t5.lib.components.Tooltip;

/**
 * The demo page for {@link Tooltip} component
 * 
 * @author lguerin
 */
public class ColorPickerTest
{
    @Property
    @Persist
    private Color color;

    @Property
    @Persist
    private Color color2;

    public void onSubmit()
    {
        if (color != null)
            System.out.println("submit//" + Integer.toHexString(color.getRGB()));
        else
            System.out.println("submit null");
    }

}
