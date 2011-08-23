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
