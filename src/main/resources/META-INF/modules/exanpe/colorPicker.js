/*
 * Copyright 2013 EXANPE <exanpe@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

define(["exanpe/common",
		"yui/2.9.0/utilities", 
		"yui/2.9.0/slider-min",
		"yui/2.9.0/colorpicker-min",
		"yui/2.9.0/container-min",
		"yui/2.9.0/button-min"], function(colorPicker) {

	/** 
	 * Constructor
	 * @class Represents a Color Picker. Can be accessed through JavaScript by its id.
	 * @param {String} id the id of the slide show
	 * @param {YUI} yuipanel the YUIPanel displaying the color picker object wrapped
	 * @param {YUI} yuicolor the YUI ColorPicker object wrapped
	 */
	Exanpe.ColorPicker = function(id, yuipanel, yuicolor){
		/**
		 * id of the instance
		 */
		this.id = id;
		
		/**
		 * yui object of the panel displaying the color picker
		 */
		this.yuipanel = yuipanel;
		
		/**
		 * yui object of the color picker
		 */
		this.yui = yuicolor;
		
		var cp = this;
		
		yuicolor.on("rgbChange", function(o){
			cp.setValue(o.newValue);
		});
		
		this._init();
	};

	/**
	 * Input element suffix
	 * @static
	 * @constant
	 * @private
	 */
	Exanpe.ColorPicker.INPUT_SUFFIX = "_input";

	/**
	 * Hidden element suffix
	 * @static
	 * @constant
	 * @private
	 */
	Exanpe.ColorPicker.HIDDEN_SUFFIX = "_hidden";

	/**
	 * Img element suffix
	 * @static
	 * @constant
	 * @private
	 */
	Exanpe.ColorPicker.IMG_SUFFIX = "_img";

	/**
	 * Popup element suffix
	 * @static
	 * @constant
	 * @private
	 */
	Exanpe.ColorPicker.POPUP_SUFFIX = "_popup";

	/**
	 * Color picker main element suffix
	 * @static
	 * @constant
	 * @private
	 */
	Exanpe.ColorPicker.COLORPICKER_SUFFIX = "_colorpicker";

	/**
	 * Color picker button suffix
	 * @static
	 * @constant
	 * @private
	 */
	Exanpe.ColorPicker.BUTTON_SUFFIX = "_button";

	/**
	 * Init the color picker if a value is provided on load by server side
	 * @private
	 */
	Exanpe.ColorPicker.prototype._init = function(){
		var value = this.getHiddenEl().value;
		if(value && value !== ""){
			this.getInputEl().value = value;
			this.yui.setValue(YAHOO.util.Color.hex2rgb(value), true);//do not fire the event
		}
		
		var buttonId = this.id + Exanpe.ColorPicker.BUTTON_SUFFIX;
		var colorPickerId = this.id + Exanpe.ColorPicker.COLORPICKER_SUFFIX;
		var button = new YAHOO.widget.Button({
		    id: buttonId, 
		    type: "button", 
		    label: this.yuipanel.closeText,
		    container: colorPickerId,
		    onclick: { fn: this.hide }
		});
		button.addClass("exanpe-button");
		YAHOO.util.Event.addListener(buttonId, "click", Exanpe.ColorPicker.prototype.hide, this, true);
	};

	/**
	 * Show the color picker
	 */
	Exanpe.ColorPicker.prototype.show = function(){
		this.yuipanel.show();
	};

	/**
	 * Reset the color picker content
	 */
	Exanpe.ColorPicker.prototype.reset = function(){
		this.yui.setValue([255, 255, 255], true);//do not fire the event
		this.getInputEl().value = "";
		this.getHiddenEl().value = "";
	};

	/**
	 * Hide the color picker
	 */
	Exanpe.ColorPicker.prototype.hide = function(){
		this.yuipanel.hide();
	};

	/**
	 * Set a value for the color picker
	 * @param {Array} rgbValue the value to set as an array [int, int, int] for [R, G, B]
	 */
	Exanpe.ColorPicker.prototype.setValue = function(rgbValue){
		var hexValue = YAHOO.util.Color.rgb2hex(rgbValue);
		
		this.getInputEl().value = hexValue;
		this.getHiddenEl().value = hexValue;
	};

	/**
	 * Dom utility function
	 * @private
	 */
	Exanpe.ColorPicker.prototype.getHiddenEl = function(){
		return YAHOO.util.Dom.get(this.id+Exanpe.ColorPicker.HIDDEN_SUFFIX);
	};

	/**
	 * Dom utility function
	 * @private
	 */
	Exanpe.ColorPicker.prototype.getInputEl = function(){
		return YAHOO.util.Dom.get(this.id+Exanpe.ColorPicker.INPUT_SUFFIX);
	};

	/**
	 * Initializes the color picker on dom load
	 * @param {Object} data the json data coming from Java class initialization
	 * @private
	 * @static
	 */
	function colorPickerBuilder(data) {
		
		var colorPickerId = data.id+Exanpe.ColorPicker.COLORPICKER_SUFFIX;
		var panelId = data.id+Exanpe.ColorPicker.POPUP_SUFFIX;
		var imgIg = data.id+Exanpe.ColorPicker.IMG_SUFFIX;
		
		var picker = new YAHOO.widget.ColorPicker(colorPickerId, {
			showrgbcontrols: false,
			showhsvcontrols: false,
			showhexsummary: false,
			showhexcontrols: false
		});
		
		var popup = new YAHOO.widget.Panel(panelId, 
					{ visible:false, 
					  draggable:false, 
					  close:true,
					  strings: {close:""},
					  context : [imgIg, "tl", "tr"]
					  } );
		
		popup.closeText = data.closeText;
		popup.setBody(YAHOO.util.Dom.get(colorPickerId));
		popup.render();
		
		var colorPicker = new Exanpe.ColorPicker(data.id, popup, picker);
		window[data.id] = colorPicker;
	};
	
	return {
		init: colorPickerBuilder
	}
	
});