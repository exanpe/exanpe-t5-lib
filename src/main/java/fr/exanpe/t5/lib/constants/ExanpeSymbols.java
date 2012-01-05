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
package fr.exanpe.t5.lib.constants;

import org.apache.tapestry5.ioc.annotations.Symbol;

import fr.exanpe.t5.lib.annotation.ContextPageReset;

/**
 * Exanpe custom {@link Symbol} elements.
 * 
 * @author lguerin
 */
public class ExanpeSymbols
{
    /**
     * Base path for Exanpe asset contents.
     */
    public static final String ASSET_BASE = "exanpe.asset-base";

    /**
     * Base YUI location<br/>
     * Override to import your own distribution of YUI.<br/>
     * The base must be defined according to the yui/build root folder in the distribution.<br/>
     * Example : if base is "com/test", then the path "com/test/yahoo-dom-event/yahoo-dom-event.js"
     * HAS to exists.
     */
    public static final String YUI2_BASE = "exanpe.yui2-base";

    /**
     * Context page reset marker in URL.<br/>
     * Default is reset.
     * When CONTEXT_PAGE_RESET_MARKER will be found in the URL, it will be removed from context and
     * will trigger the methods annotated with {@link ContextPageReset} or with signature
     * "void contextReset()"
     * 
     * @see ContextPageReset
     */
    public static final String CONTEXT_PAGE_RESET_MARKER = "exanpe.context-page-reset-marker";

    /**
     * Google Maps API V3 - Business client ID
     */
    public static final String GMAP_V3_BUSINESS_CLIENT_ID = "exanpe.gmap-v3-client-id";

    /**
     * Google Maps API V3 - version
     */
    public static final String GMAP_V3_VERSION = "exanpe.gmap-v3-version";
}
