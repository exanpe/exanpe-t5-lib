package fr.exanpe.t5.lib.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.Asset;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import fr.exanpe.t5.lib.constants.SecurePasswordEventTypeEnum;
import fr.exanpe.t5.lib.services.ExanpeComponentService;

/**
 * Display a panel allowing a user to enter a password by clicking or hovering numbers.<br/>
 * This component must be enclosed into a <t:form> component.<br/>
 * JavaScript : This component is bound to a class Exanpe.SecurePassword.<br/>
 * CSS : This component is bound to a class exanpe-spwd.<br/>
 * 
 * @author jmaupoux
 */
@Import(library =
{ "${exanpe.yui2-base}/yahoo-dom-event/yahoo-dom-event.js", "js/exanpe-t5-lib.js" }, stylesheet =
{ "css/exanpe-t5-lib-core.css", "css/exanpe-t5-lib-skin.css" })
@SupportsInformalParameters
public class SecurePassword implements ClientElement
{

    /**
     * Defines the width of the grid where are randomly positioned the numbers (column number).
     */
    @Property
    @Parameter(defaultPrefix = BindingConstants.LITERAL, required = true, allowNull = false, value = "4")
    private int gridWidth;

    /**
     * Defines the height of the grid where are randomly positioned the numbers (row number).
     */
    @Property
    @Parameter(defaultPrefix = BindingConstants.LITERAL, required = true, allowNull = false, value = "4")
    private int gridHeight;

    /**
     * Defines the max length of the input password field.
     */
    @Property
    @Parameter(defaultPrefix = BindingConstants.LITERAL, required = false)
    @SuppressWarnings("unused")
    private Integer maxlength;

    /**
     * Defines the set of characters to display in the password grid.
     */
    @Property
    @Parameter(defaultPrefix = BindingConstants.LITERAL, required = true, allowNull = false, value = "0123456789")
    private String characters;

    /**
     * Defines the event triggering the number.
     * 
     * @see SecurePasswordEventTypeEnum
     */
    @Property
    @Parameter(defaultPrefix = BindingConstants.LITERAL, required = true, allowNull = false, value = "click")
    private SecurePasswordEventTypeEnum eventType;

    /**
     * Defines the asset displayed to clear the password field.
     */
    @Property
    @Parameter(defaultPrefix = BindingConstants.ASSET, required = true, allowNull = false, value = "img/spwd/reset.png")
    @SuppressWarnings("unused")
    private Asset resetImage;

    /**
     * Defined the name of the value to set the password in the page.
     */
    @Property
    @Parameter(autoconnect = true)
    @SuppressWarnings("unused")
    private String value;

    @Property
    private String uniqueId;

    /**
     * Allow to iterate through a dynamic number of elements, as 1..${} fails in t:loop
     */
    @Property
    private java.util.List<String> dummyRowList;

    /**
     * Allow to iterate through a dynamic number of elements, as 1..${} fails in t:loop
     */
    @Property
    private java.util.List<String> dummyColumnList;

    /**
     * the index of the current row
     */
    @Property
    private int gridRowIndex;

    /**
     * the index of the current column
     */
    @Property
    private int gridColumnIndex;

    private static final String ROOT_CSS_CLASS = "exanpe-spwd";

    @Inject
    private JavaScriptSupport javaScriptSupport;

    @Inject
    private ComponentResources resources;

    @Inject
    private ExanpeComponentService ecservice;

    /**
     * Stores as instant access a case id and its value associated
     * Key = id de la case
     * Value = valeur de binding
     */
    private Map<String, String> positionningMap;

    /**
     * Stores the content of each case in reading order (left to right, then up to down)
     */
    private List<String> positionning;

    @SetupRender
    void init()
    {
        uniqueId = javaScriptSupport.allocateClientId(resources);

        // Build the positionning list, to position items on the grid
        positionning = new LinkedList<String>(Arrays.asList(characters.split("")));
        positionning.remove(0);

        int gridNumber = gridWidth * gridHeight;

        for (int i = characters.length(); i < gridNumber; i++)
            positionning.add("");

        Collections.shuffle(positionning);

        // Build a dummy list to iterate over
        dummyRowList = new ArrayList<String>(gridHeight);
        for (int i = 0; i < gridHeight; ++i)
            dummyRowList.add("");

        dummyColumnList = new ArrayList<String>(gridWidth);
        for (int i = 0; i < gridWidth; ++i)
            dummyColumnList.add("");

        positionningMap = new HashMap<String, String>();
    }

    @BeginRender
    void begin(MarkupWriter writer)
    {
        Element e = writer.element("div");

        e.attribute("id", getClientId());
        resources.renderInformalParameters(writer);

        ecservice.reorderCSSClassDeclaration(e, ROOT_CSS_CLASS);
    }

    @AfterRender
    void end(MarkupWriter writer)
    {
        writer.end();

        JSONObject data = buildJSONData();

        javaScriptSupport.addInitializerCall("securePasswordBuilder", data);
    }

    private JSONObject buildJSONData()
    {
        JSONObject positionningMapJSON = new JSONObject();

        Iterator<Map.Entry<String, String>> ite = positionningMap.entrySet().iterator();

        while (ite.hasNext())
        {
            Map.Entry<String, String> next = ite.next();
            positionningMapJSON.accumulate(next.getKey(), next.getValue());
        }

        JSONObject data = new JSONObject();
        data.accumulate("id", getClientId());
        data.accumulate("eventType", eventType.toString().toLowerCase());
        data.accumulate("positionnings", positionningMapJSON);

        return data;
    }

    /**
     * Return true if a case is empty
     * 
     * @return true if a case is empty
     */
    public boolean isCaseEmpty()
    {
        return StringUtils.isEmpty(positionningMap.get(getClientId() + gridRowIndex + gridColumnIndex));
    }

    /**
     * Return the value of the current case, or an empty String if no character is in the case
     * 
     * @return the value of the current case
     */
    public String getCaseValue()
    {
        return positionning.get(gridRowIndex * gridWidth + gridColumnIndex);
    }

    /**
     * Return the id of a case
     * 
     * @return the id of the current case
     */
    public String getCaseId()
    {
        String id = getClientId() + gridRowIndex + gridColumnIndex;
        positionningMap.put(id, getCaseValue());
        return id;
    }

    public String getClientId()
    {
        return uniqueId;
    }
}
