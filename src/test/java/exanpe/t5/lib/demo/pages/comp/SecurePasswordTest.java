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
package exanpe.t5.lib.demo.pages.comp;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;

import fr.exanpe.t5.lib.components.HideablePanel;

/**
 * The demo page for the {@link HideablePanel} component.
 * 
 * @author jmaupoux
 */
public class SecurePasswordTest{
	
	@Property
	@Persist
	private String spwd;
	
	@Property
	private String spwd2;
	
	@Property
	private String spwd3;

	@OnEvent(value = EventConstants.SUBMIT)
	public void display(){
		System.out.println("1::"+spwd);
		System.out.println("2::"+spwd2);
		System.out.println("3::"+spwd3);
	}
	
}
