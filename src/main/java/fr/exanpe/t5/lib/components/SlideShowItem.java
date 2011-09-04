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

package fr.exanpe.t5.lib.components;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Environment;

import fr.exanpe.t5.lib.model.SlideShowInternalModel;

/**
 * Item displayed inside a {@link SlideShow}.<br/>
 * The content of this item can be either an image with its associated title or the body of the
 * component.<br/>
 * 
 * @see SlideShow
 * @author jmaupoux
 */
public class SlideShowItem
{

    /**
     * Image displayed as part of the item
     */
    @Property
    @Parameter(required = false, defaultPrefix = BindingConstants.ASSET)
    @SuppressWarnings("unused")
    private Asset image;

    /**
     * Title of the image displayed as part of the item
     */
    @Property
    @Parameter(required = false, defaultPrefix = BindingConstants.MESSAGE)
    @SuppressWarnings("unused")
    private String title;

    @Inject
    private Environment environment;

    @Property
    @SuppressWarnings("unused")
    private int height;

    @SetupRender
    public void validate()
    {
        SlideShowInternalModel marker = environment.peek(SlideShowInternalModel.class);

        if (marker == null) { throw new IllegalStateException("SlideShowItem must be nested into a SlideShow component"); }

        height = marker.getHeight();
    }
}
