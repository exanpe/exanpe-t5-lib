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
		"yui/2.9.0/yahoo-dom-event", 
		"yui/2.9.0/dragdrop-min",
		"yui/2.9.0/slider-min",
		"yui/2.9.0/connection-min",
		"yui/2.9.0/json-min"], function(slider) {
	
	/** 
	 * Constructor
	 * @class Represents a Slider. Can be accessed through JavaScript by its id.
	 * @param {String} id the id of the slider element
	 * @param {int} min the minimum slider value
	 * @param {int} max the maximum slider value
	 * @param {String} displayId the HTML element id to display the value, or null if no display
	 * @param {Boolean} ajax whether the Slider update is executed in Ajax mode, or not
	 * @param {String} url the url used to execute AJAX request
	 * @param {String} zone the Tapestry zone element to update at the end of the ajax request
	 */
	Exanpe.Slider = function(id, min, max, displayId, ajax, url, zone){
		/**
		 * The id of the instance
		 */
		this.id = id;
		
		/**
		 * The minimum slider value
		 */
		this.min = min;
		
		/**
		 * The maximum slider value
		 */
		this.max = max;
		
		/**
		 * The HTML id element to display the value, or null
		 */
		this.displayId = displayId;
		
		/**
		 * Pixels number for 1 unit
		 */
		this.unit = Exanpe.Slider.LENGTH/(this.max-this.min);
		
		/**
		 * This wrapped yui slider instance
		 */
		this.yui = null;
		
		/**
		 * Is ajax mode or not
		 */
		this.ajax = ajax;
		
		/**
		 * Used to execute an AJAX request for updating the Slider
		 */
		this.url = url;
		
		/**
		 * Used to update a Tapestry zone element
		 */
		this.zone = zone;
		
		/**
		 * Current request
		 */
		this.request = null;
	};

	/**
	 * Horizontal constant
	 * @constant
	 * @static
	 * @private
	 */
	Exanpe.Slider.ORIENTATION_HORIZONTAL = "horizontal";

	/**
	 * Vertical constant
	 * @constant
	 * @static
	 * @private
	 */
	Exanpe.Slider.ORIENTATION_VERTICAL = "vertical";

	/**
	 * Slider image prefix
	 * @constant
	 * @static
	 * @private
	 */
	Exanpe.Slider.SLIDER_IMAGE_PREFIX = "slider-img-";

	/**
	 * Slider thumb prefix
	 * @constant
	 * @static
	 * @private
	 */
	Exanpe.Slider.SLIDER_THUMB_PREFIX = "slider-thumb-";

	/**
	 * Slider background prefix
	 * @constant
	 * @static
	 * @private
	 */
	Exanpe.Slider.SLIDER_BACKGROUND_PREFIX = "slider-bg-";

	/**
	 * Request parameter used to send the slide offset value in Ajax mode
	 * @see Java Slider.PARAM_NAME
	 * @constant
	 * @static
	 * @private
	 */
	Exanpe.Slider.PARAM_NAME = "value";

	/**
	 * Slider length (width of height depanding on orientation)
	 * @constant
	 * @static
	 * @private
	 */
	Exanpe.Slider.LENGTH = 200;

	/**
	 * Set the value of the slider.
	 * @param {int} value the value to set
	 * @param {Boolean} skipSlider whether the slider value update should me skipped. Set to true if the value initially comes from the YUI component.
	 */
	Exanpe.Slider.prototype.setValue = function(value, skipSlider) {
		if(value < this.min || value > this.max){
			Exanpe.Log.error("Incorrect value set:"+value+". Min:"+this.min+"/Max:"+this.max);
			return ;
		}
		if(this._getDisplayEl()){
			this._getDisplayEl().innerHTML = ""+value;
		}
		if(skipSlider !== true){
			this.yui.setValue(this._valueToPixel(value), true, false, true);
		}
		this.value = value;
		this._getHiddenEl().value = value;
	};

	/**
	 * Get the value of the slider.
	 * @return {int} the value of the slider
	 */
	Exanpe.Slider.prototype.getValue = function(value) {
		return this.value;
	};

	/**
	 * Convert a value to a yui pixel value.
	 * @param {int} value the value to set
	 * @return a value to the pixel unit of YUI
	 * @private
	 */
	Exanpe.Slider.prototype._valueToPixel = function(value) {
		var zero = (-this.min)*this.unit;
		return zero + value*this.unit;
	};

	/**
	 * Return a value from the pixel of YUI
	 * @param {int} value the value to set
	 * @return a value from the pixel of YUI
	 * @private
	 */
	Exanpe.Slider.prototype._pixelToValue = function(value) {
		var zero = (-this.min)*this.unit;
		return (value - zero)/this.unit;
	};

	/**
	 * Get the input element
	 * @return {HTMLElement} the input hidden element 
	 * @private
	 */
	Exanpe.Slider.prototype._getHiddenEl = function() {
		return YAHOO.util.Dom.get(this.id);
	};

	/**
	 * Get the input element
	 * @return {HTMLElement} the input hidden element 
	 * @private
	 */
	Exanpe.Slider.prototype._getDisplayEl = function() {
		return YAHOO.util.Dom.get(this.displayId);
	};

	/**
	 * Execute an Ajax request to update the slider value
	 * @private
	 */
	Exanpe.Slider.prototype._ajaxSliderUpdate = function (){ 	
		
		var slider = this;
		
		// Ajax Failure handler
		var failureHandler = function(o){
			slider.request = null;
			Exanpe.Log.error("Could not process ajax Slider update");
		};

		// Ajax Success handler
		var successHandler = function(o){
			if (slider.zone){
				var zone = YAHOO.util.Dom.get(slider.zone);
				var json = YAHOO.lang.JSON.parse(o.responseText);
				var result = json.content;
				if (result){
					zone.innerHTML = result;
					Tapestry.ElementEffect.highlight(zone);
				}
			}
		};

		// Callback objects
		var callback = 
		{
			success: successHandler,
			failure: failureHandler
		};

		var request = YAHOO.util.Connect.asyncRequest(
				"GET",
				Exanpe.Utils.addQueryParam(this.url, Exanpe.Slider.PARAM_NAME, this.value),
				callback, 
				null
		);
		
		// Ajax protection
		if(YAHOO.util.Connect.isCallInProgress(this.request)){
		    YAHOO.util.Connect.abort(request);
		}
		else {
			this.request = request;
		}
	};

	/**
	 * Initialize the YUI Slider component.
	 * @private
	 */
	Exanpe.Slider.prototype._init = function() {
		var hiddenSliderInput = this._getHiddenEl();

		// Set the current value
		if(hiddenSliderInput.value && hiddenSliderInput.value !== ""){
			this.setValue(hiddenSliderInput.value);
		}

	    // Animate slider
	    this.yui.animate = true; 

	    var slider = this;
	    
		// Observe slider
		this.yui.subscribe("change", function (newOffset) {
			slider.setValue(slider._pixelToValue(newOffset), true);
		});
		if(this.ajax){
			this.yui.subscribe("slideEnd", Exanpe.Slider.prototype._ajaxSliderUpdate, this, true);
		}
	};

	/**
	 * Initialize Slider component on DOM load
	 * @param {Object} data the json data coming from Java class initialization
	 * @private
	 * @static
	 */
	function sliderBuilder(data) {
		var slider = new Exanpe.Slider(data.id, data.min, data.max, data.displayId, data.ajax, data.url, data.zone);
		
		// The slider can move min pixels up or left 
		var topLeftConstraint = 0; 
		// The slider can move max pixels down or right 
		var bottomRightConstraint = Exanpe.Slider.LENGTH;
		// Interval
	    var interval = data.interval;
		
		var yuiSlider = undefined;
		var sliderImage = YAHOO.util.Dom.get(Exanpe.Slider.SLIDER_IMAGE_PREFIX + data.id);
	    
		if (data.orientation == Exanpe.Slider.ORIENTATION_HORIZONTAL)
		{
			yuiSlider = YAHOO.widget.Slider.getHorizSlider(Exanpe.Slider.SLIDER_BACKGROUND_PREFIX + data.id, Exanpe.Slider.SLIDER_THUMB_PREFIX + data.id, topLeftConstraint, bottomRightConstraint, interval*slider.unit);
			sliderImage.setAttribute("src", data.horizontalCursorImage);
		}

		if (data.orientation == Exanpe.Slider.ORIENTATION_VERTICAL)
		{
			yuiSlider = YAHOO.widget.Slider.getVertSlider(Exanpe.Slider.SLIDER_BACKGROUND_PREFIX + data.id, Exanpe.Slider.SLIDER_THUMB_PREFIX + data.id, topLeftConstraint, bottomRightConstraint, interval*slider.unit);
			sliderImage.setAttribute("src", data.verticalCursorImage);
		}
		
		slider.yui = yuiSlider;
		
		window[data.id] = slider;
		slider._init();
	};
	
	return {
		init: sliderBuilder
	}
	
});