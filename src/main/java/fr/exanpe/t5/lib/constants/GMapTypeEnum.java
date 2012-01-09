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

/**
 * Types of maps you can display using the Google Maps Javascript API.
 * 
 * @author lguerin
 * @since 1.2
 */
public enum GMapTypeEnum
{
    /**
     * Displays the normal, default 2D tiles of Google Maps
     */
    ROADMAP,
    /**
     * Displays photographic tiles
     */
    SATELLITE,
    /**
     * Displays a mix of photographic tiles and a tile layer for prominent features (roads, city
     * names)
     */
    HYBRID,
    /**
     * Displays physical relief tiles for displaying elevation and water features (mountains,
     * rivers, etc.)
     */
    TERRAIN;

    public String toString()
    {
        return super.toString().toUpperCase();
    };
}
