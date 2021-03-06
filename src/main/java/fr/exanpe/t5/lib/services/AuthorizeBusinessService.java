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

package fr.exanpe.t5.lib.services;

/**
 * This interface defines the services offered to all the library authorization elements
 * (component/mixin/worker).
 * 
 * @author jmaupoux
 * @since 1.2
 */
public interface AuthorizeBusinessService
{
    /**
     * Apply the "any" rule
     * 
     * @param any the roles to restrict on
     * @return true to grant access, false otherwise
     */
    public boolean applyAny(String[] any);

    /**
     * Apply the "all" rule
     * 
     * @param any the roles to restrict on
     * @return true to grant access, false otherwise
     */
    public boolean applyAll(String[] all);

    /**
     * Apply the "any" rule
     * 
     * @param any the roles to restrict on
     * @return true to grant access, false otherwise
     */
    public boolean applyNot(String[] not);
}
