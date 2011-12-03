/*
 * Copyright 2011 EXANPE <exanpe@gmail.com>
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

/** Global item repository */
if(!Exanpe){
	var Exanpe = {};
}

/**
 * Utility class to log. Uses YAHOO.log as its basic usage to console.
 * @class Allow the framework to log.
 * @static
 */
Exanpe.Log = {};


/**
 * Log a message in error level
 * @param {String} message the message to log.
 * @static
 */
Exanpe.Log.error = function(message){
	if ( typeof console == 'object' && console.error){
		console.error(message);
	}
};

/** Accordion */

/** 
 * Constructor
 * @class Represents an Accordion. Can be accessed through JavaScript by its id.
 * @param {String} id the id of the main accordion element
 * @param {boolean} multiple if multiple items can be opened at once
 * @param {String} eventType the type of event that trigger the accordion
 * @param {float} duration the speed of accordion animation
 */
Exanpe.Accordion = function(id, multiple, eventType, duration){
	
	/**
	 * id of the accordion
	 */
	this.id = id;
	
	/**
	 * Multiple item visible feature
	 */
	this.multiple = multiple;
	
	/**
	 * Shows on item on over instead of click
	 */
	this.eventType = eventType;
	
	/**
	 * Duration for animation
	 */
	this.duration = duration;
	
	/**
	 * Current item opened
	 */
	this.currentItem = null;
	
	/**
	 * Marker defining if an animation is on
	 */
	this.animating = false;
};

/**
 * Define the Hide animation attributes
 * @static
 * @private
 */
Exanpe.Accordion.HIDE_ANIM_ATT = {
		height : {to : 1, unit : "px"}		
};

/**
 * CSS class to apply when item opened
 * @constant
 * @static
 * @private
 */
Exanpe.Accordion.CSS_OPENED = "opened";

/**
 * CSS class to apply when item closed
 * @constant
 * @static
 * @private
 */
Exanpe.Accordion.CSS_CLOSED = "closed";

/**
 * Action triggered on an item
 * @param {Event} e the event
 * @private
 */
Exanpe.Accordion.prototype._action = function(e){
	
	var itemId = YAHOO.util.Dom.getAncestorByTagName(YAHOO.util.Event.getTarget(e), "li").id;
	
	if(this.isOpened(itemId)){
		if(this.eventType == "click"){
			this.close(itemId);
		}
	}
	else{
		this.open(itemId);
	}
	return false;
};

/**
 * Open an item in this accordion
 * @example accordionId.open("item1")
 * @param {string} id the id of the item of the accordion to open
 * @returns {boolean} true if open occurred, false otherwise
 */
Exanpe.Accordion.prototype.open = function(id){
	if(this.animating){
		return false;
	}
	
	if(this.isOpened(id)){
		return false;
	}
	
	if(!this.multiple && this.currentItem && this.currentItem != id){
		this.close(this.currentItem);
	}
	
	if(!this.multiple){
		this.currentItem = id;
	}
	
	var contentEl = this.getContentEl(id);
	
	//open to get height : fail if hidden
	YAHOO.util.Dom.setStyle(contentEl,"display","block");
	
	var height = this.getContentElHeight(contentEl);
	
	YAHOO.util.Dom.setStyle(contentEl,"visibility","visible");
	YAHOO.util.Dom.setStyle(contentEl,"height","1px");
	
	var atts = {
			height : {from : 1, to : height}
	};

	var acc = this;
	var anim = new YAHOO.util.Anim(contentEl, atts, this.duration);
	anim.onComplete.subscribe(function(){
		YAHOO.util.Dom.setStyle(contentEl,"display","");
		acc.animating = false;
	});
	
	
	YAHOO.util.Dom.removeClass(id, Exanpe.Accordion.CSS_CLOSED);
	YAHOO.util.Dom.addClass(id, Exanpe.Accordion.CSS_OPENED);
	
	this.animating = true;
	anim.animate();
	
	return true;
};


/**
 * Hides an item
 * @param {string} id the id of the item to hide
 * @returns {boolean} true if close occurred, false otherwise
 */
Exanpe.Accordion.prototype.close = function(id){
	if(this.animating){
		return false;
	}
	
	if(!this.isOpened(id)){
		return false;
	}
	
	if(!this.multiple && this.currentItem != id){
		return false;
	}
		
	var contentEl = this.getContentEl(id);
	
	var height = this.getContentElHeight(contentEl);
	
	var acc = this;
	var anim = new YAHOO.util.Anim(contentEl, Exanpe.Accordion.HIDE_ANIM_ATT, this.duration);
	anim.onComplete.subscribe(function(){
		YAHOO.util.Dom.removeClass(id, Exanpe.Accordion.CSS_OPENED);
		YAHOO.util.Dom.addClass(id, Exanpe.Accordion.CSS_CLOSED);
		YAHOO.util.Dom.setStyle(contentEl,"height",height+"px");
		acc.animating = false;
	});

	this.animating = true;
	anim.animate();
	
	this.currentItem = null;
	
	return true;
};

/**
 * Determines if an item is opened
 * @param {string} id the id of the item
 * @return {boolean} true if the item is opened, false otherwise
 */
Exanpe.Accordion.prototype.isOpened = function(id){
	return YAHOO.util.Dom.hasClass(id, Exanpe.Accordion.CSS_OPENED);
};

/**
 * Get the DOM element for the title item id
 * @param {string} itemId the item id
 * @return {HTMLElement} the DOM element corresponding to the title
 * @private
 */
Exanpe.Accordion.prototype.getTitleEl = function(itemId){
	return YAHOO.util.Dom.getElementBy(function(){return true;}, "a", itemId);
};

/**
 * Get the DOM element for the content item id
 * @param {string} itemId the item id
 * @return {HTMLElement} the DOM element corresponding to the content
 * @private
 */
Exanpe.Accordion.prototype.getContentEl = function(itemId){
	return YAHOO.util.Dom.getElementBy(function(){return true;}, "div", itemId);
};

/**
 * Get the content element from the item id
 * Should be a Helper static class...
 * @param {HTMLElement} contentEl the dom element
 * @return {int} the height of the element, or null
 * @private
 */
Exanpe.Accordion.prototype.getContentElHeight = function(contentEl){
	var region = YAHOO.util.Dom.getRegion(contentEl);
	if(!region){
		return null;
	}
	return (region.bottom - region.top);
};

/**
 * Initialize the events on an item for the accordion
 * @param {string} itemId the item id
 * @private
 */
Exanpe.Accordion.prototype._initItem = function(id){
	var event = (this.eventType == "click" )?"click":"mouseover";
	YAHOO.util.Event.addListener(this.getTitleEl(id), event, Exanpe.Accordion.prototype._action, this, true);
	
	if(!this.multiple && this.isOpened(id)){
		this.currentItem = id;
	}
};


/**
 * Initializes an accordion on DOM load
 * @param {Object} data the json data coming from Java class initialization
 * @static
 * @private
 */
Tapestry.Initializer.accordionBuilder = function(data){
	var accordion = new Exanpe.Accordion(data.id, data.multiple===true, data.eventType, data.duration);
	window[data.id] = accordion;
	
	var itemArray = data.items.split(",");
	
	for(var i=0; i < itemArray.length; i++){
		accordion._initItem(itemArray[i]);
	}
};


/** HideablePanel **/

/** 
 * Constructor
 * @class Represents an Hideable Panel. Can be accessed through JavaScript by its id.
 * @param {String} id the id of the main accordion element
 * @param {int} hideWidth the width of the hideable panel
 * @param {float} duration the speed of hidding animation
 */
Exanpe.HideablePanel = function(id, hideWidth, duration){
	/**
	 * id of the instance
	 */
	this.id = id;
	
	/**
	 * duration of the animation
	 */
	this.duration = duration;
	
	/**
	 * state of the panel
	 */
	this.hidden = false;
	
	var attributesHide = { 
        width: { to: 0 }
    }; 
	this.animHide = new YAHOO.util.Anim(this.id+'_hidepart', attributesHide, this.duration);
	this.animHide.onComplete.subscribe( function(){
		YAHOO.util.Dom.addClass(id, 'hidden');
	});
	
	var attributesShow = { 
        width: { to: hideWidth  ,unit: 'px'}
    }; 
	this.animShow = new YAHOO.util.Anim(this.id+'_hidepart', attributesShow, this.duration); 
	this.animShow.onComplete.subscribe( function(){
		YAHOO.util.Dom.removeClass(id, 'hidden');
	});
	
	var hideBar = this.getHideBarEl();
	
	YAHOO.util.Event.addListener(hideBar, "click", Exanpe.HideablePanel.prototype.reverse, null, this);
    YAHOO.util.Event.addListener(hideBar, "mouseover", function(ev, id){YAHOO.util.Dom.addClass(hideBar, 'hidebarHover');}, this.id);
    YAHOO.util.Event.addListener(hideBar, "mouseout", function(ev, id){YAHOO.util.Dom.removeClass(hideBar, 'hidebarHover');}, this.id);
};

/**
 * Method to get the DOM element of the hide bar
 * @return {HTMLElement} the hidding bar
 * @private 
 */
Exanpe.HideablePanel.prototype.getHideBarEl = function(){
	return YAHOO.util.Dom.get(this.id+"_hidebar");
};

/**
 * Reverse the visibility of the panel
 * @return {boolean} true if the panel gets visible, false if it gets hidden
 */
Exanpe.HideablePanel.prototype.reverse = function(){
	if(this.hidden){
		this.hidden = false;
		this.animShow.animate();
		this.afterShow();
		return true;
	}else{
		this.hidden = true;
		this.animHide.animate();
		this.afterHide();
		return false;
	}	
};

/**
 * Returns the visibility of the panel
 * @return {boolean} true if the panel is visible, false otherwise
 */
Exanpe.HideablePanel.prototype.isVisible = function(){
	return !this.hidden;
};

/**
 * Called after hide.
 * Does nothing by default, override to define your own action.
 * @event
 */
Exanpe.HideablePanel.prototype.afterHide = function(){
	
};

/**
 * Called after show.
 * Does nothing by default, override to define your own action.
 * @event
 */
Exanpe.HideablePanel.prototype.afterShow = function(){
	
};


/**
 * Initializes an hideable panel on dom load
 * @param {Object} data the json data coming from Java class initialization
 * @private
 * @static
 */
Tapestry.Initializer.hideablePanelBuilder = function(data){
	var hdp = new Exanpe.HideablePanel(data.id, data.hideWidth, data.duration);
	window[hdp.id] = hdp;
};



/** Secure Password **/

/** 
 * Constructor
 * @class Represents a Secure Password. Can be accessed through JavaScript by its id.
 * @param {String} id the id of the main accordion element
 * @param {Object} positionnings the number positionning
 */
Exanpe.SecurePassword = function(id, positionnings){
	/**
	 * id of the instance
	 */
	this.id = id;
	
	/**
	 * positionning object
	 */
	this.positionnings = positionnings;
	this.timeoutHover = undefined;
};

/**
 * Initialize the component from server value
 * @private
 */
Exanpe.SecurePassword.prototype._init = function(){
	if(this.getHiddenEl().value && this.getHiddenEl() !== ""){
		this.getInputEl().value = this.getHiddenEl().value; 
	}
};

/**
 * Input element suffix
 * @static
 * @constant
 * @private
 */
Exanpe.SecurePassword.INPUT_SUFFIX = "_input";

/**
 * Hidden element suffix
 * @static
 * @constant
 * @private
 */
Exanpe.SecurePassword.HIDDEN_SUFFIX = "_hidden";

/**
 * Time to wait on hovering, before recording the number. In milliseconds.
 * @static
 * @constant
 */
Exanpe.SecurePassword.HOVER_WAIT = 1000;

/**
 * Method canceling a hover in progress
 * @param {Event} e the event of cancel
 * @private
 */
Exanpe.SecurePassword.prototype._cancelHover = function(e){
	window.clearTimeout(this.timeoutHover);
	this.timeoutHover = null;
};

/**
 * Method triggered on hover start
 * @param {Event} e the event of hovering
 * @private
 */
Exanpe.SecurePassword.prototype._hover = function(e){

	var value = this.positionnings[YAHOO.util.Event.getTarget(e).id];
	
	YAHOO.util.Event.addListener(YAHOO.util.Event.getTarget(e).id, "mouseout", Exanpe.SecurePassword.prototype._cancelHover, this, true);
	if(this.timeoutHover){
		this._cancelHover();
	}
	
	var obj = this;
	
	this.timeoutHover = window.setTimeout(function(){
		obj._processRecording(value);
		obj = null;
		value = null;
	}, Exanpe.SecurePassword.HOVER_WAIT);	
	
};

/**
 * Method triggered on click
 * @param {Event} e the event of hovering
 * @private
 */
Exanpe.SecurePassword.prototype._click = function(e){
	var value = this.positionnings[YAHOO.util.Event.getTarget(e).id];
	this._processRecording(value);
};


/**
 * Method actually performing the record of a value
 * @param {char} value the character to record
 * @private
 */
Exanpe.SecurePassword.prototype._processRecording = function(value){
	var inputEl = this.getInputEl();
	var pwdEl = this.getHiddenEl();
	
	if(!inputEl.value){
		inputEl.value = value;
		pwdEl.value = inputEl.value;
		return;
	}
	
	if(inputEl.size && inputEl.value.length >= inputEl.size){
		return;
	}
	
	inputEl.value = inputEl.value + value;
	pwdEl.value = inputEl.value;
	pwdEl = null;
	inputEl = null;
};

/**
 * Reset the value of the component 
 */
Exanpe.SecurePassword.prototype.reset = function(){
	this.getInputEl().value = "";
	this.getHiddenEl().value = "";
};

/**
 * Return the input element for the component
 * @returns {HTMLElement} the input element
 * @private
 */
Exanpe.SecurePassword.prototype.getInputEl = function(){
	return YAHOO.util.Dom.get(this.id+Exanpe.SecurePassword.INPUT_SUFFIX);
};

/**
 * Return the hidden field element for the component, actually storing the password
 * @returns {HTMLElement} the hidden field element
 * @private
 */
Exanpe.SecurePassword.prototype.getHiddenEl = function(){
	return YAHOO.util.Dom.get(this.id+Exanpe.SecurePassword.HIDDEN_SUFFIX);
};

/**
 * Initializes a secure password on dom load
 * @param {Object} data the json data coming from Java class initialization
 * @private
 * @static
 */
Tapestry.Initializer.securePasswordBuilder = function(data){

	var securepwd = new Exanpe.SecurePassword(data.id, data.positionnings);
	window[data.id] = securepwd;
	
	var listener = Exanpe.SecurePassword.prototype._click;
	if(data.eventType == "hover"){
		listener = Exanpe.SecurePassword.prototype._hover;
		data.eventType = "mouseover";
	}

	var toBind = YAHOO.util.Dom.getElementsBy(function(){return true;}, "td", YAHOO.util.Dom.get(data.id));

	for(var i=0;i<toBind.length;i++){
		YAHOO.util.Event.addListener(YAHOO.util.Dom.get(toBind[i]), data.eventType, listener, securepwd, true);
	}
	
	securepwd._init();
};

/** Slider component **/

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
			this.url + "?" + Exanpe.Slider.PARAM_NAME + "=" + this.value, 
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
Tapestry.Initializer.sliderBuilder = function(data){
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


/** 
 * Constructor
 * @class Represents a SlideShow. Can be accessed through JavaScript by its id.
 * @param {String} id the id of the slide show
 * @param {YUI} yui the YUI carousel object wrapped
 */
Exanpe.SlideShow = function(id, yui){
	/**
	 * this instance id
	 */
	this.id = id;
	
	/**
	 * this wrapped yui carousel instance
	 */
	this.yui = yui;
};

/**
 * Initializes a slide show on dom load
 * @param {Object} data the json data coming from Java class initialization
 * @private
 * @static
 */
Tapestry.Initializer.slideShowBuilder = function(data){
	var carousel = new YAHOO.widget.Carousel(data.id, {
	    isCircular: data.circular,
	    numVisible : data.numVisible,
	    animation : {speed : 0.5},
	    autoPlayInterval : data.autoPlayMillis
	});
	carousel.render();
	
	if(data.autoPlayMillis !== 0){
		carousel.startAutoPlay();
	}
		
	window[data.id] = carousel;
};


/** 
 * Constructor
 * @class Represents a TabView. Can be accessed through JavaScript by its id.
 * @param {String} id the id of the tab view
 * @param {String} selectedId the selected tab id
 */
Exanpe.TabView = function(id, selectedId){
	/**
	 * id of the instance
	 */
	this.id = id;
	
	/**
	 * id of the selected tab
	 */
	this.selectedId = selectedId;
} ;

/** 
 * Adds a tab to the tab view, during initialization
 * Get based on HTML elements ids norming to structure view and work
 * @param {String} id the id of the tab.
 * @private
 */
Exanpe.TabView.prototype.initTab = function(id){
	var titleEl = this.getTabTitleEl(id);
	this.getTabTitleContainer().appendChild(titleEl);
	
	var contentEl = this.getTabContentEl(id);
	this.getTabContentContainer().appendChild(contentEl);
};

/** 
 * Show a tab. Does nothing if tab is already shown.
 * Call this method only if target tab is already loaded.
 * @param {String} id the id of the tab.
 */
Exanpe.TabView.prototype.show = function(id){
	if(this.selectedId == id){
		return;
	}
	this.hide(this.selectedId);
	
	this.selectedId = id;
	
	YAHOO.util.Dom.removeClass(this.getTabContentEl(id), "exanpe-tab-hidden");
	YAHOO.util.Dom.removeClass(YAHOO.util.Dom.getElementBy(function(){return true;}, "a" ,this.getTabTitleEl(id)), "exanpe-tab-hidden");
};

/** 
 * Hide a tab. Does nothing if tab is already hidden.
 * @param {String} id the id of the tab.
 * @private
 */
Exanpe.TabView.prototype.hide = function(id){
	if(this.selectedId != id){
		return;
	}
	YAHOO.util.Dom.addClass(this.getTabContentEl(id), "exanpe-tab-hidden");
	YAHOO.util.Dom.addClass(YAHOO.util.Dom.getElementBy(function(){return true;}, "a" ,this.getTabTitleEl(id)), "exanpe-tab-hidden");
};

/** 
 * Returns the state of a tab.
 * @param {String} id the id of the tab to check.
 * @returns {boolean} true if the id is visible, false otherwise
 */
Exanpe.TabView.prototype.isTabVisible = function(id){
	return this.selectedId == id;
};

/**
 * Dom method utility
 * @private
 */
Exanpe.TabView.prototype.getTabTitleEl = function(id){
	return YAHOO.util.Dom.get("_"+id+"_title");
};

/**
 * Dom method utility
 * @private
 */
Exanpe.TabView.prototype.getTabContentEl = function(id){
	return YAHOO.util.Dom.get("_"+id+"_content");
};

/**
 * Dom method utility
 * @private
 */
Exanpe.TabView.prototype.getTabTitleContainer = function(){
	return YAHOO.util.Dom.get(this.id+"_header_hook");
};

/**
 * Dom method utility
 * @private
 */
Exanpe.TabView.prototype.getTabContentContainer = function(){
	return YAHOO.util.Dom.get(this.id+"_content_hook");
};

/**
 * Initializes a tab view on dom load
 * @param {Object} data the json data coming from Java class initialization
 * @private
 * @static
 */
Tapestry.Initializer.tabViewBuilder = function(data){
	var tabView = new Exanpe.TabView(data.id, data.selectedId);

	for(var i=0;i<data.tabs.length;i++){
		tabView.initTab(data.tabs[i].id);
	}
	
	window[data.id] = tabView;
};

/** Tooltip component **/

/** 
 * Constructor
 * @class Represent a tooltip component. Can be accessed through its id.
 * @param {String} id the id of the Tooltip element
 * @param {YUI} yui the YUI Tooltip object wrapped 
 */
Exanpe.Tooltip = function(id, yui) {
	/**
	 * The id of the instance
	 */
	this.id = id;
	
	/**
	 * this wrapped yui tooltip instance
	 */
	this.yui = yui;
	
	this._init();
};

/**
 * The css wrapper and root class
 * @private
 * @static
 * @constant
 */
Exanpe.Tooltip.CSS_WRAPPER = "exanpe-tooltip";

/**
 * The tooltip element prefix id
 * @private
 * @static
 * @constant
 */
Exanpe.Tooltip.PREFIX_ID = "exanpe-tooltip-";

/**
 * The zindex of the tooltip components
 * @private
 * @static
 * @constant
 */
Exanpe.Tooltip.ZINDEX = 999;

/**
 * Initialize YUI Tooltip component.
 * @private
 */
Exanpe.Tooltip.prototype._init = function() {
	var tooltipId = Exanpe.Tooltip.PREFIX_ID + this.id;
	this.yui.contextMouseOverEvent.subscribe( 
				    function() { 
				    	YAHOO.util.Dom.setStyle(tooltipId, "position", "absolute");
				    } 
				); 
	YAHOO.util.Dom.addClass(tooltipId, Exanpe.Tooltip.CSS_WRAPPER);
};

/**
 * Initializes a Tooltip on dom load
 * @param {Object} data the json data coming from Java class initialization
 * @private
 * @static
 */
Tapestry.Initializer.tooltipBuilder = function(data){
	var yuiTooltip = new YAHOO.widget.Tooltip(Exanpe.Tooltip.PREFIX_ID + data.id, {
	    context: data.id,
	    text: data.text,
	    zIndex: Exanpe.Tooltip.ZINDEX
	}); 
	var tooltip = new Exanpe.Tooltip(data.id, yuiTooltip);
	window[data.id] = tooltip;
};

/** Dialog mixin **/

/** 
 * Constructor
 * @class Represents a Dialog component
 * @param {String} id the id of the Dialog Mixin container element
 * @param {String} message the message of the dialog box
 * @param {String} targetHtmlId the HTML id used to display the content of the Dialog box instead of message parameter
 * @param {String} title the title of the dialog box
 * @param {String} renderMode the render mode to used for displaying Dialog (RENDER_MODE_INFO or RENDER_MODE_CONFIRM)
 * @param {int} width the with of the dialog box, in pixel
 * @param {YUI}    yui the YUI Dialog object wrapped
 */
Exanpe.Dialog = function(id, message, targetHtmlId, title, renderMode, width, yui) {
	/**
	 * The id of the instance
	 */
	this.id = id;
	
	/**
	 * The Dialog message
	 */
	this.message = message;
	
	/**
	 * Html id used to display Dialog content, instead of message
	 */
	this.targetHtmlId = targetHtmlId;
	
	/**
	 * The Dialog title
	 */
	this.title = title;
	
	/**
	 * The Dialog render mode
	 */
	this.renderMode = renderMode;
	
	/**
	 * The width of the Dialog box
	 */
	this.width = width;
	
	/**
	 * The wrapped yui dialog instance
	 */
	this.yui = yui;
};

/**
 * Disabled CSS class
 * @constant
 * @private
 * @static
 */
Exanpe.Dialog.DISABLE_CLASS = "exanpe-dialog-disable";

/**
 * CONFIRM mode constant
 * @constant
 * @private
 * @static
 */
Exanpe.Dialog.RENDER_MODE_CONFIRM = "confirm";

/**
 * INFO mode constant
 * @constant
 * @private
 * @static
 */
Exanpe.Dialog.RENDER_MODE_INFO = "info";

/**
 * Dialog component id prefix
 * @see Java Dialog.DIALOG_ID_PREFIX
 * @constant
 * @static
 * @private
 */
Exanpe.Dialog.ID_PREFIX = "exanpe-dialog-";

/**
 * Dialog YUI id prefix
 * @constant
 * @static
 * @private
 */
Exanpe.Dialog.YUI_ID_PREFIX = "exanpe-dialog-yui-";

/**
 * Display the dialog box
 * @param {Event} e the triggered event
 */
Exanpe.Dialog.prototype.show = function(e) {
	this._hideDialogErrorEl();
	this.yui.show();
};

/**
 * Hide the dialog box
 */
Exanpe.Dialog.prototype.hide = function() {
	this.yui.hide();
};

/**
 * Set the width of the dialog box
 * @param {int} width the width to set
 */
Exanpe.Dialog.prototype.setWidth = function(width) {
	this.yui.cfg.queueProperty("width", width+"px");
};

/**
 * Set the message of the dialog box
 * @param {String} message the message to set
 */
Exanpe.Dialog.prototype.setMessage = function(message) {
	this.yui.cfg.queueProperty("text", message);
};

/**
 * Execute the redirection after validation
 * @private
 */
Exanpe.Dialog.prototype._doAction = function() {
	var el = YAHOO.util.Dom.get(this.id);
	
	// Submit button
	if (el.click) {
		el.click();
		return;
	}
	// Link
	if (el.href)
	{
		window.location = el.href;
	}
	else 
	{
		Exanpe.Log.error("Dialog CONFIRM mode must only be applied on link or submit elements");
		this.hide();
	}
};

/**
 * Generate the message displayed into Dialog.
 * @param {String} message the message to display
 * @private
 */
Exanpe.Dialog.prototype._generateDialogMessage = function(message) {
	// Placeholder for error message
	var dialogMessage = "<div id='dialogError-" + this.id + "' class='exanpe-dialog-error'></div>";
	
	dialogMessage += message;
	this.setMessage(dialogMessage);
}

/**
 * Dom method utility
 * @private
 */
Exanpe.Dialog.prototype.getDialogErrorEl = function(){
	return YAHOO.util.Dom.get("dialogError-" + this.id);
};

/**
 * Hide the Dialog error message
 * @private
 */
Exanpe.Dialog.prototype._hideDialogErrorEl = function(){
	YAHOO.util.Dom.setStyle(this.getDialogErrorEl(), "display", "none");
};

/**
 * Called before process the Dialog validation
 * Does nothing by default, override to define your own action.
 */
Exanpe.Dialog.prototype.beforeDialogValidation = function() {
	
};

/**
 * Called after process the Dialog validation
 * Does nothing by default, override to define your own action.
 */
Exanpe.Dialog.prototype.afterDialogValidation = function() {
	
};

/**
 * Display an error message into the Dialog box
 * @param {String} errorMsg the errorMsg to display 
 * @private
 */
Exanpe.Dialog.prototype._displayDialogErrorMessage =  function(errorMsg) {
	var dialogErrorEl = this.getDialogErrorEl();
	dialogErrorEl.innerHTML = errorMsg;
	YAHOO.util.Dom.setStyle(dialogErrorEl, "display", "block");
};

/**
 * Initialize the YUI Dialog component
 * @private
 */
Exanpe.Dialog.prototype._init = function() {

	// Define event handlers for Dialog
	this.yui.wrapper = this;
	var handleYes = function() {
		// Before validation
		var errorMsg = this.wrapper.beforeDialogValidation();
		
		if (!errorMsg) {
			this.wrapper.hide();
			this.wrapper._doAction();
			
			// After validation
			this.wrapper.afterDialogValidation();
		}
		else {
			// The error is displayed into Dialog and the event is stoped
			this.wrapper._displayDialogErrorMessage(errorMsg);
		}
	};
	var handleNoOrOk = function() {
		this.wrapper.hide();
	};
	
	// Adding buttons
	var buttons = [];
	if (this.renderMode == Exanpe.Dialog.RENDER_MODE_CONFIRM)
	{
		buttons = [ { text:this.yui.yesLabelButton, handler:handleYes, isDefault:true },
		 		   { text:this.yui.noLabelButton,  handler:handleNoOrOk } ];
	}
	if (this.renderMode == Exanpe.Dialog.RENDER_MODE_INFO)
	{
		buttons = [ { text:this.yui.okLabelButton,  handler:handleNoOrOk } ];
	}
	
	// Configuration
	this.yui.cfg.queueProperty("buttons", buttons);
	this.setWidth(this.width);

	// The content of the Dialog
	if (this.targetHtmlId) {		
		var target = YAHOO.util.Dom.get(this.targetHtmlId);	
		YAHOO.util.Dom.setStyle(this.targetHtmlId, "display", "none");
		this._generateDialogMessage(target.innerHTML);
	}
	else {
		this._generateDialogMessage(this.message)
	}
	
	// Render the Dialog
	this.yui.setHeader(this.title);
	this.yui.render(Exanpe.Dialog.ID_PREFIX + this.id);

	if (!YAHOO.util.Dom.hasClass(this.id, Exanpe.Dialog.DISABLE_CLASS))
	{
		YAHOO.util.Event.addListener(this.id, "mousedown", Exanpe.Dialog.prototype.show, this, true);
		YAHOO.util.Event.on(this.id, "click", function(e) {
			YAHOO.util.Event.stopPropagation(e);
		});		
	}
};

/**
 * Initializes the dialog component on dom load
 * @param {Object} data the json data coming from Java class initialization
 * @private
 * @static
 */
Tapestry.Initializer.dialogBuilder = function(data){
	var yuiDialog =  new YAHOO.widget.SimpleDialog(Exanpe.Dialog.YUI_ID_PREFIX + data.id, 
				 { fixedcenter: true,
				   visible: false,
				   draggable: true,
				   close: true,
				   constraintoviewport: true,
				   modal: true
				 } );
		yuiDialog.yesLabelButton = data.yesLabelButton;
		yuiDialog.noLabelButton = data.noLabelButton;
		yuiDialog.okLabelButton = data.okLabelButton;
	
	var dialog = new Exanpe.Dialog(data.id, data.message, data.targetHtmlId, data.title, data.renderMode, data.width, yuiDialog);
	window[data.id] = dialog;
	dialog._init();
};

/** Ajaxloader component **/

/** 
 * Constructor
 * @class Represent an AjaxLoader component. Can be accessed through its id
 * @param {String} id the id of the AjaxLoader component element
 * @param {String} message the message displayed to the user during asynchronous loading
 * @param {String} image the image url displayed during loading
 * @param {String} width the width, in pixel, of the panel used to display loading informations
 * @param {String} url the url used to execute AJAX request.
 * @param {Boolean} autoLoad to Load the URL or not on dom ready.
 * @param {Boolean} showPanel to show the panel on request, or not.
 * @param {Boolean} highlight to highlight result, after loading, or not.
 */
Exanpe.AjaxLoader = function(id, message, image, width, url, autoLoad, showPanel, highlight) {
	/**
	 * The id of the instance
	 */
	this.id = id;
	
	/**
	 * Message displayed during loading
	 */
	this.message = message;
	
	/**
	 * Image displayed during loading
	 */
	this.image = image;
	
	/**
	 * The width of the Panel
	 */
	this.width = width;
	
	/**
	 * Used to execute AJAX request for loading content
	 */ 
	this.url = url;
	
	/**
	 * Load content on DOM ready or not
	 */
	this.autoLoad = autoLoad;
	
	/**
	 * Show panel during loading or not
	 */
	this.showPanel = showPanel;
	
	/**
	 * Panel shown on ajax request
	 */
	this.yuiPanel = null;
	
	/**
	 * Highlight the result, after loading, or not.
	 */
	this.highlight = highlight;
	
	/**
	 * Current request
	 */
	this.request = null;
};

/**
 * AjaxLoader Panel element id prefix
 * @constant
 * @static
 * @private
 */
Exanpe.AjaxLoader.PANEL_ID_PREFIX = "exanpe-ajaxloader-panel-";

/**
 * AjaxLoader Panel content element id prefix
 * @constant
 * @static
 * @private
 */
Exanpe.AjaxLoader.PANEL_CONTENT_ID_PREFIX = "exanpe-ajaxloader-panel-content-";

/**
 * Method to get the id of the Panel element used by YUI.
 * @return {String} the Panel element id
 * @private
 */
Exanpe.AjaxLoader.prototype.getPanelId = function(){
	return Exanpe.AjaxLoader.PANEL_ID_PREFIX + this.id;
};

/**
 * Method to get the id of the Panel content element used to display loading message.
 * @return {String} the Panel content element id
 * @private
 */
Exanpe.AjaxLoader.prototype.getPanelContentId = function(){
	return Exanpe.AjaxLoader.PANEL_CONTENT_ID_PREFIX + this.id;
};

/**
 * Get the response element from the item id
 * 
 * @param {String} itemId the item id
 * @return {HTMLElement} the DOM element corresponding to the response
 */
Exanpe.AjaxLoader.prototype.getResponseEl = function(itemId){
	return YAHOO.util.Dom.get("exanpe-ajaxloader-response-" + itemId);
};

/**
 * Highlight the result after loading, or not (fade-in animation effect).
 * @param {int} start the opacity value used to begin animation on the target result
 * @param {int} end the opacity value used to end animation on the target result
 * @param {float} duration the speed of animation
 * @private
 */
Exanpe.AjaxLoader.prototype._highlight = function(start, end, duration){
	
	// Target DIV to highlight
	var target = this.getResponseEl(this.id);
	
	YAHOO.util.Dom.setStyle(target, 'opacity', start);
	var anim = new YAHOO.util.Anim(target, 
				{opacity: {from: start, to: end }}, 
				duration, 
				YAHOO.util.Easing.easeIn);
	
	// Fire animation
	anim.animate();
};

/**
 * Execute the Ajax query responsible for loading the body of the component.
 * Can be executed on demand.
 */
Exanpe.AjaxLoader.prototype.load = function()
{
	var showPanel = this.showPanel;
	var panel = this.yuiPanel;//or null if none
	var ajaxLoader = this;
	var highlight = this.highlight;
	
	// Target DIV to update
	var target = this.getResponseEl(this.id);
		
	// Ajax Failure handler
	var failureHandler = function(o) 
	{
		ajaxLoader.request = null;
		Exanpe.Log.error("Could not process request in order to get the AjaxLoder content.");
		ajaxLoader.afterUpdateFailure();
	};
	
	// Ajax Success handler
	var successHandler = function(o) 
	{
		var json = YAHOO.lang.JSON.parse(o.responseText);
		var result = json.content;
		target.innerHTML = result;
		if(showPanel){
			panel.hide();
		}
		
		if(highlight){
			ajaxLoader._highlight(0, 1, 1);
		}
		ajaxLoader.afterUpdateSuccess();
	};
	
	// Callback objects
	var callback = 
	{
		success: successHandler,
		failure: failureHandler,
		cache: false
	};	
	
	// Show Panel
	if(this.showPanel){
		panel.show();
	}

	// Execute Ajax request
	var request = YAHOO.util.Connect.asyncRequest(
			"GET",
			this.url, 
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
 * Called on Ajax update success.
 * Does nothing by default, override to define your own action.
 * @event
 */
Exanpe.AjaxLoader.prototype.afterUpdateSuccess = function(){
	
};

/**
 * Called on Ajax update failure.
 * Does nothing by default, override to define your own action.
 * @event
 */
Exanpe.AjaxLoader.prototype.afterUpdateFailure = function(){
	
};

/**
 * Initializes the panel used to display information during loading event.
 * @private
 */
Exanpe.AjaxLoader.prototype._init = function() {
	
	// The Panel content element display the loading message
	var panelContentEl = YAHOO.util.Dom.get(this.getPanelContentId());
	
	if(this.showPanel){
		// Initialise YUI Panel
		this.yuiPanel = new YAHOO.widget.Panel(this.getPanelId(), 
				 { width: this.width + "px",
			   	   fixedcenter: true,
			   	   visible: false,
			   	   draggable: false,
			   	   close: false,
			   	   constraintoviewport: true,
			   	   modal: true,
			   	   effect:{effect:YAHOO.widget.ContainerEffect.FADE, duration:0.5} 
			 });
		
		// Render the panel
		panelContentEl.innerHTML = "<img src='" + this.image + "' alt='' border='' /> <br />" + this.message;
		this.yuiPanel.render(this.id);
	}
	else{
		YAHOO.util.Dom.setStyle(panelContentEl,"visibility","hidden");		
		YAHOO.util.Dom.setStyle(panelContentEl,"position","absolute");
	}
	
	if(this.autoLoad){
		YAHOO.util.Event.onDOMReady(Exanpe.AjaxLoader.prototype.load, this, true);
	}
};

/**
 * Initialize an AjaxLoader on DOM load
 * @param data the json data
 * @private
 * @static
 */
Tapestry.Initializer.ajaxLoaderBuilder = function(data){
	var ajaxLoader = new Exanpe.AjaxLoader(data.id, data.message, data.image, data.width, data.url, data.autoLoad, data.showPanel, data.highlight);
	window[data.id] = ajaxLoader;
	ajaxLoader._init();
};

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
Tapestry.Initializer.colorPickerBuilder = function(data){
	
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

/** 
 * Constructor
 * @class Represents a SelectLoader.
 * @param {String} id the id of the select to bind on
 * @param {String} targetId the target select id
 * @param {String} url the url to submit the selected value
 */
Exanpe.SelectLoader = function(id, targetId, url){
	/**
	 * Select source id
	 */
	this.id = id;
	
	/**
	 * Target select id
	 */
	this.targetId = targetId;
	
	/**
	 * url to submit to
	 */
	this.url = url;
	
	/**
	 * Current request
	 */
	this.request = null;
};

/**
 * Request parameter sending the selected value 
 * @see Java SelectLoader.PARAM_NAME
 * @constant
 * @static
 * @private
 */
Exanpe.SelectLoader.PARAM_NAME = "value";

/**
 * Initializes the component
 * @private
 */
Exanpe.SelectLoader.prototype._init = function(){
	YAHOO.util.Event.addListener(this.id, "change", Exanpe.SelectLoader.prototype._triggerLoading, this, true);
};

/**
 * Triggers the select loading 
 * @event
 * @private
 */
Exanpe.SelectLoader.prototype._triggerLoading = function (){
	var select = YAHOO.util.Dom.get(this.id);
	var value = select.options[select.selectedIndex].value;	
	select = null;
	
	this.executeRequest(value);
};


/**
 * Triggers the select loading 
 * @param {String} value the selected value
 */
Exanpe.SelectLoader.prototype.executeRequest = function (value){
	
	var selectLoader = this;
	
	if(this.request){
		YAHOO.util.Connect.abort(this.request);
	}
	
	// Ajax Failure handler
	var failureHandler = function(o) 
	{
		selectLoader.request = null;
		Exanpe.Log.error("Could not get the SelectLoader target's content. Fail id:"+selectLoader.id);
		selectLoader.afterUpdateFailure();
	};
	
	// Ajax Success handler
	var successHandler = function(resp) 
	{
		selectLoader.request = null;
		var content = resp.responseText;
		
		var targetSelect = document.getElementById(selectLoader.targetId);
		
		var xmlDoc;
		if (window.DOMParser){
		  parser=new DOMParser();
		  xmlDoc=parser.parseFromString(content,"text/xml");
		}else{ // Internet Explorer
		  xmlDoc=new ActiveXObject("Microsoft.XMLDOM");
		  xmlDoc.async="false";
		  xmlDoc.loadXML(content);
        } 
		
		targetSelect.options.length = 0;
		xmlDoc = xmlDoc.firstChild;//go to root
		
		for(var i=0;i<xmlDoc.childNodes.length;i++){

			var cn = xmlDoc.childNodes[i];
			if(cn.nodeType==1 && cn.nodeName=="option"){
				var txt;
				if(window.DOMParser){
					txt = cn.textContent;
				}else{
					txt = cn.text;
				}

				var o = new Option(txt, cn.getAttribute("value"));
				targetSelect.options.add(o);
			}
		}
		selectLoader.afterUpdateSuccess();
	};//end success
	
	// Callback objects
	var callback = 
	{
		success: successHandler,
		failure: failureHandler
	};
	
	this.request = YAHOO.util.Connect.asyncRequest(
		"GET",
		this.url+"?"+Exanpe.SelectLoader.PARAM_NAME+"="+value, 
		callback, 
		null
	);
};


/**
 * Called on Ajax update success.
 * Does nothing by default, override to define your own action.
 * @event
 */
Exanpe.SelectLoader.prototype.afterUpdateSuccess = function(){
	
};

/**
 * Called on Ajax update failure.
 * Does nothing by default, override to define your own action.
 * @event
 */
Exanpe.SelectLoader.prototype.afterUpdateFailure = function(){
	
};

/**
 * Initializes the select loader
 * @param {Object} data the json data coming from Java class initialization
 * @private
 * @static
 */
Tapestry.Initializer.selectLoaderBuilder = function(data){
	var sl = new Exanpe.SelectLoader(data.id, data.targetId, data.url);
	sl._init();
	window[data.id] = sl;
};


/** 
 * Constructor
 * @class Represents a MenuBar.
 * @param {String} id the id of the menu bar
 * @param {YUI} yui the yui MenuBar object wrapped
 */
Exanpe.MenuBar = function(id, yui){
	/**
	 * id
	 */
	this.id = id;
	
	/**
	 * yui wrapped
	 */
	this.yui = yui;
};

/**
 * Initialize the element with yui wrapping
 * @private
 */
Exanpe.MenuBar.prototype._init = function(){
	Exanpe.Menu._initDisabled(this.yui);
	this.yui.render();
};

/**
 * Initializes the menuBar
 * @param {Object} data the json data coming from Java class initialization
 * @private
 * @static
 */
Tapestry.Initializer.menuBarBuilder = function(data){
	var yui = new YAHOO.widget.MenuBar(data.id, {
		hidedelay:500,
		autosubmenudisplay: (data.eventType == "hover"),
		lazyload: true
	});
	
	var menuBar = new Exanpe.MenuBar(data.id, yui);
	menuBar._init();
	window[data.id] = menuBar;
};


/** 
 * Constructor
 * @class Represents a Menu.
 * @param {String} id the id of the menu bar
 * @param {YUI} yui the yui Menu object wrapped
 */
Exanpe.Menu = function(id, yui){
	/**
	 * id
	 */
	this.id = id;
	
	/**
	 * yui wrapped
	 */
	this.yui = yui;
};

/**
 * Initialize the element with yui wrapping
 * @private
 */
Exanpe.Menu.prototype._init = function(){
	Exanpe.Menu._initDisabled(this.yui);
	this.yui.render();
};


Exanpe.Menu._initDisabled = function(yuimenu){
	var disableMenuItems = function () {
		
		var aItems = this.getItems();
		var	oItem;
		var i;
		if (aItems) {
			for(var i=0;i<aItems.length;i++){
				oItem = aItems[i];
				if (YAHOO.util.Dom.hasClass(oItem.element, "yuimenuitem-disabled")) {
					oItem.cfg.setProperty("disabled", true);
				}
				if (YAHOO.util.Dom.hasClass(oItem.element, "yuimenubaritem-disabled")) {
					oItem.cfg.setProperty("disabled", true);
				}
			}
		}
	};
	yuimenu.subscribe("render", disableMenuItems);
};

/**
 * Show the menu
 */
Exanpe.Menu.prototype.show = function(){
	this.yui.show();
};

/**
 * Hide the menu
 */
Exanpe.Menu.prototype.hide = function(){
	this.yui.hide();
};

/**
 * Initializes the menu
 * @param {Object} data the json data coming from Java class initialization
 * @private
 * @static
 */
Tapestry.Initializer.menuBuilder = function(data){
	var context = null;
	if(data.targetHtmlId){
		context = [];
		context[0] = data.targetHtmlId;
		context[1] = "tl";
		context[2] = "br";
	}
		
	
	var yui = new YAHOO.widget.Menu(data.id, {
		hidedelay:500,
		autosubmenudisplay: true,
		lazyload: true,
		"context" : context,
		position : "dynamic"
	});
	
	var menu = new Exanpe.Menu(data.id, yui);
	
	//init on dom render to bind on HTML element
	menu._init();
	
	window[data.id] = menu;
};

/** 
 * Constructor
 * @class Represents a PasswordStrengthChecker.
 * @param {String} id the id of the password field to bind on
 * @param {Boolean} ajax whether the password is checked in Ajax mode, or not 
 * @param {String} javascriptChecker the javascript function provided by user to check the password client-side
 * @param {int} min the number of caracters before triggering Ajax request (used only within ajax parameter set to true)
 * @param {String} url the url to submit the selected value
 * @param {Object.<string, string>} messages the messages used to display visual feedback
 */
Exanpe.PasswordStrengthChecker = function(id, ajax, javascriptChecker, min, url, messages){
	/**
	 * Password field id
	 */
	this.id = id;
	
	/**
	 * Is ajax mode or not
	 */
	this.ajax = ajax;
	
	/**
	 * The function used client-side for checking the user password.
	 * MUST return an element of Exanpe.PasswordStrengthChecker.Complexity enum
	 */
	this.javascriptChecker = javascriptChecker;
	
	/**
	 * The number of caracters before triggering ajax request
	 */
	this.min = min;
	
	/**
	 * The messages used for this component
	 */
	this.messages = messages;
	
	/**
	 * url to submit to
	 */
	this.url = url;
	
	/**
	 * Current request
	 */
	this.request = null;
};

/**
 * Enum for password complexity
 * @enum {String}
 * @static
 */
Exanpe.PasswordStrengthChecker.Complexity = {
	VERY_WEAK: "veryweak",
	WEAK: "weak",
	STRONG: "strong",
	STRONGEST: "strongest"
};

/**
 * Password Strenght Checker prefix
 * @constant
 * @static
 * @private
 */
Exanpe.PasswordStrengthChecker.PSC_PREFIX = "exanpe-psc-";

/**
 * Tag Meter CSS Prefix
 * @constant
 * @static
 * @private
 */
Exanpe.PasswordStrengthChecker.TAG_METER_CSS_PREFIX = "exanpe-psc-tag-meter-";

/**
 * Tag Meter CSS class
 * @constant
 * @static
 * @private
 */
Exanpe.PasswordStrengthChecker.DEFAULT_TAG_METER_CSS_CLASS = "exanpe-psc-tag";

/**
 * Request parameter used to check the password value in Ajax mode
 * @see Java PasswordStrengthChecker.PARAM_NAME
 * @constant
 * @static
 * @private
 */
Exanpe.PasswordStrengthChecker.PARAM_NAME = "pwd";

/**
 * Get the Password field element the mixin is attached to
 * 
 * @param {String} id the id of the password field
 * @return {HTMLElement} the DOM element corresponding to the password field
 */
Exanpe.PasswordStrengthChecker.prototype.getPasswordFieldEl = function(id){
	return YAHOO.util.Dom.get(id);
};

/**
 * Get the unique id of the Tapestry mixin
 * @return {String} the id of the mixin
 */
Exanpe.PasswordStrengthChecker.prototype.getMixinId = function(){
	return Exanpe.PasswordStrengthChecker.PSC_PREFIX + this.id;
};

/**
 * Display the password meter
 * @private
 */
Exanpe.PasswordStrengthChecker.prototype._display = function() {
	var overlay = new YAHOO.widget.Overlay(this.getMixinId(), {visible: true}); 
	var region = YAHOO.util.Dom.getRegion(this.id);
	// Set the x and y positions simultaneously
	overlay.cfg.setProperty("xy", [region.right, region.top]);
	// Render the overlay
	overlay.render();
};

/**
 * Compute the visual feedback.
 * The complexity policy (strength) of the password is built upon your own formulas by providing 
 * client-side rules (javascriptChecker) or server-side rules (ajaxChecker).
 * @param {String} strength the strength of the password.
 * @private
 */
Exanpe.PasswordStrengthChecker.prototype._compute = function(/**String */ strength) {
	
	var css = "";
	var level = 0;
	var message;
	
	if (strength) {
		css = Exanpe.PasswordStrengthChecker.TAG_METER_CSS_PREFIX + strength;
	}
	
	switch (strength) {
		case Exanpe.PasswordStrengthChecker.Complexity.VERY_WEAK:
			message = this.messages.veryweak;
			level = 0;
			break;
		case Exanpe.PasswordStrengthChecker.Complexity.WEAK:
			message = this.messages.weak;
			level = 1;
			break;	
		case Exanpe.PasswordStrengthChecker.Complexity.STRONG:
			message = this.messages.strong;
			level = 2;
			break;		
		case Exanpe.PasswordStrengthChecker.Complexity.STRONGEST:
			message = this.messages.strongest;
			level = 3;
			break;
		default:
			message = this.messages.disclaimer;
			break;
	}

	var disclaimerPlaceholder = YAHOO.util.Dom.get(Exanpe.PasswordStrengthChecker.PSC_PREFIX + this.id + "-msg");
	disclaimerPlaceholder.innerHTML = message;
	
	// draw the visual result
	for (i = 0; i < 4; i++) {
		var tag = YAHOO.util.Dom.get(Exanpe.PasswordStrengthChecker.PSC_PREFIX + this.id + "-tag-" + i);
		if (tag) {
			tag.className = "";
			YAHOO.util.Dom.addClass(tag, Exanpe.PasswordStrengthChecker.DEFAULT_TAG_METER_CSS_CLASS);
			if (i <= level) {
				YAHOO.util.Dom.addClass(tag, css);
			}
		}
	}
	
};

/**
* Called on Ajax update success.
* Does nothing by default, override to define your own action.
* @event
*/
Exanpe.PasswordStrengthChecker.prototype.afterAjaxCheckerSuccess = function(){

};

/**
* Called on Ajax update failure.
* Does nothing by default, override to define your own action.
* @event
*/
Exanpe.PasswordStrengthChecker.prototype.afterAjaxCheckerFailure = function(){

};

/**
 * Execute an Ajax request for checking the strength of the password
 * @param {String} password the password to check
 * @private
 */
Exanpe.PasswordStrengthChecker.prototype._ajaxChecker = function (password){ 	
	
	var psc = this;
	
	// Ajax Failure handler
	var failureHandler = function(o){
		psc.request = null;
		Exanpe.Log.error("Could not process ajax check on password provided");
		psc.afterAjaxCheckerFailure();
	};

	// Ajax Success handler
	var successHandler = function(o){
		var json = YAHOO.lang.JSON.parse(o.responseText);
		var strength = json.result;
		if (strength){
			psc._compute(strength);
		}
		psc.afterAjaxCheckerSuccess();
	};

	// Callback objects
	var callback = 
	{
		success: successHandler,
		failure: failureHandler
	};

	var request = YAHOO.util.Connect.asyncRequest(
			"GET",
			this.url + "?" + Exanpe.PasswordStrengthChecker.PARAM_NAME + "=" + password, 
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
 * Method used to check the complexity of the password: client-side (ajax parameter 
 * is set to false) or server-side (ajax parameter is set to true)
 * @private
 */
Exanpe.PasswordStrengthChecker.prototype._checkPasswordStrength = function() {
	var passwordField = this.getPasswordFieldEl(this.id)
	var passwordValue = passwordField.value;
	var strength = "";
	// Initialize password meter
	this._compute(strength);

	// By default, javascript mode
	if (this.javascriptChecker) {
		if (passwordValue != "") {
			var method = eval(this.javascriptChecker);
			strength = method(passwordValue);
			this._compute(strength);
		}
	}

	// else, Ajax mode
	if (this.ajax) {
		if (passwordValue.length >= this.min) {
			this._ajaxChecker(passwordValue);
		}
	}
};

/**
 * Initialize the component
 * @private
 */
Exanpe.PasswordStrengthChecker.prototype._init = function() {
	var passwordField = this.getPasswordFieldEl(this.id)
	YAHOO.util.Event.addListener(this.id, "keyup", Exanpe.PasswordStrengthChecker.prototype._checkPasswordStrength, this, true);
	// Display the password meter
	this._display();
	// Compute the password complexity
	this._compute();
};

/**
 * Initialize the password strength checker mixin
 * @param {Object} data the json data coming from Java class initialization
 * @private
 * @static
 */
Tapestry.Initializer.passwordStrengthCheckerBuilder = function(data){
	var psc = new Exanpe.PasswordStrengthChecker(data.id, data.ajax, data.javascriptChecker, data.min, data.url, YAHOO.lang.JSON.parse(data.messages));
	psc._init();
	window[data.id] = psc;
};

/** Ajax Validation **/

/** 
 * Constructor
 * @class Represents an AjaxValidation.
 * @param {String} id the id of the textfield to bind on
 * @param {String} url the url to submit the selected value
 * @param {String} message the message to display on error
 */
Exanpe.AjaxValidation = function(id, url, message){
	this.id = id;
	this.url = url;
	this.message = message;
};

/**
 * Constant for param name sent
 * @static
 * @constant
 */
Exanpe.AjaxValidation.PARAM_NAME = "value";



/**
 * Initialize the component.
 * @private
 */
Exanpe.AjaxValidation.prototype._init = function(){
	var el = YAHOO.util.Dom.get(this.id);
	var av = this;
	
	YAHOO.util.Event.addListener(el, "blur", Exanpe.AjaxValidation.prototype.validate, this, true);
	
	el = null;
};

/**
 * Called before triggering an ajax validation
 * Does nothing by default except returing true, override to define your own behavior.
 * @returns {boolean} false to cancel validation
 */
Exanpe.AjaxValidation.prototype.beforeValidation = function(){
	return true;
};

/**
 * Called when unable to process the ajax validation.
 * Does nothing by default, override to define your own action.
 * @event
 */
Exanpe.AjaxValidation.prototype.afterValidationError = function(){
	
};

/**
 * Called after a successful validation
 * Does nothing by default, override to define your own action.
 * @event
 */
Exanpe.AjaxValidation.prototype.onValidationCorrect = function(){
	
};

/**
 * Called after an unsuccessful validation
 * Does nothing by default, override to define your own action.
 * @event
 */
Exanpe.AjaxValidation.prototype.onValidationIncorrect = function(){
	
};

/**
 * Validate the textfield content.
 */
Exanpe.AjaxValidation.prototype.validate = function(){
	var value = YAHOO.util.Dom.get(this.id).value;
	if(!value || value ==""){
		return;
	}
	
	if(this.beforeValidation() == false){
		return;
	}
	var av = this;
	// Ajax Failure handler
	var failureHandler = function(o){
		av.request = null;
		Exanpe.Log.error("Could not process ajax validation");
		av.afterValidationError();
	};
	
	// Ajax Success handler
	var successHandler = function(o){
		if(!o.responseText){
			this.onValidationIncorrect();
			return;
		}
		
		var json = YAHOO.lang.JSON.parse(o.responseText);
		
		if(json.result == true){
			if(YAHOO.util.Dom.get(av.id).isInError == true){
				YAHOO.util.Dom.get(av.id).removeDecorations();
			}
			document.getElementById(av.id).isInError = false;
			av.onValidationCorrect();
		}else{
			document.getElementById(av.id).isInError = true;
			YAHOO.util.Dom.get(av.id).showValidationMessage(av.message);
			av.onValidationIncorrect();
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
			this.url + "?" + Exanpe.AjaxValidation.PARAM_NAME + "=" + YAHOO.util.Dom.get(this.id).value, 
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
 * Initialize the ajax validation mixin
 * @param {Object} data the json data coming from Java class initialization
 * @private
 * @static
 */
Tapestry.Initializer.ajaxValidationBuilder = function(data){
	var av = new Exanpe.AjaxValidation(data.id, data.url, data.message);
	av._init();
	window[av.id] = av;
};

/** 
 * Constructor
 * @class Represents a VerticalMenu. Can be accessed through JavaScript by its id.
 * @param {String} id the id of the menu
 * @param {String} selectedId the selected item in the menu
 */
Exanpe.VerticalMenu = function(id, selectedId){
	
	/**
	 * id of the instance
	 */
	this.id = id;
	
	/**
	 * id of the selected menu item
	 */
	this.selectedId = selectedId;
};

/**
 * CSS class for closed menu item
 * @constant
 * @static
 * @private
 */
Exanpe.VerticalMenu.CSS_VMENU_CLOSED = "exanpe-vmenu-closed";

/** 
 * Prepare an item of the menu, during initialization phase
 * @param {String} id the id of the menu item.
 * @private
 */
Exanpe.VerticalMenu.prototype._initMenu = function(id){
	var contentEl = this.getMenuItemContentEl(id);
	this.getMenuItemContentContainer(id).appendChild(contentEl);
	this.openOrCloseMenu(id);
};

/** 
 * Open or close the content of a menu item. 
 * Open the menu if  menu item is selected, close it otherwise.
 * @param {String} id the id of the menu item.
 */
Exanpe.VerticalMenu.prototype.openOrCloseMenu = function(id){	
	if (this.isMenuItemSelected(id)) {
		this._open(id);
	}
	else {		
		this._close(id);
	}
};

/**
 * Open a menu item content.
 * @param {String} id the id of the menu item.
 * @private
 */
Exanpe.VerticalMenu.prototype._open = function(id){
	this.selectedId = id;
	YAHOO.util.Dom.removeClass(this.getMenuItemContentContainer(id), Exanpe.VerticalMenu.CSS_VMENU_CLOSED);
};

/** 
 * Close a menu item content.
 * @param {String} id the id of the menu item.
 * @private
 */
Exanpe.VerticalMenu.prototype._close = function(id){
	YAHOO.util.Dom.addClass(this.getMenuItemContentContainer(id), Exanpe.VerticalMenu.CSS_VMENU_CLOSED);
};

/** 
 * Returns the state of a menu item.
 * @param {String} id the id of the menu item to check.
 * @returns {boolean} true if the menu item is selected, false otherwise
 */
Exanpe.VerticalMenu.prototype.isMenuItemSelected = function(id){
	return this.selectedId == id;
};

/**
 * Dom method utility
 * @private
 */
Exanpe.VerticalMenu.prototype.getMenuItemContentEl = function(id){
	return YAHOO.util.Dom.get("exanpe-vmenuitem-content-" + id);
};

/**
 * Dom method utility
 * @private
 */
Exanpe.VerticalMenu.prototype.getMenuItemContentContainer = function(id){
	return YAHOO.util.Dom.get("exanpe-vmenu-content-" + id);
};

/**
 * Initializes the Vertical Menu on DOM load
 * @param {Object} data the json data coming from Java class initialization
 * @private
 * @static
 */
Tapestry.Initializer.verticalMenuBuilder = function(data){
	var verticalMenu = new Exanpe.VerticalMenu(data.id, data.selectedId);

	for(var i=0;i<data.items.length;i++){
		verticalMenu._initMenu(data.items[i].id);
	}	
	window[data.id] = verticalMenu;
};

/**
 * On submit handler
 * @private
 */
Tapestry.Validator.ajaxValidator = function(field, message, value){
	field.addValidator(function(){
		if(field.isInError){
			throw message;
		}
	});
};

/** Google Map **/

/** 
 * Constructor
 * @class Represents a GoogleMap component. Can be accessed through JavaScript by its id.
 * @param {String} id the id of the component
 * @param {String} position the initial position used to center the map
 * @param {Object[]} items the items to display into the map
 */
Exanpe.GoogleMap = function(id, position, items){
	
	/**
	 * Id of the instance
	 */
	this.id = id;
	
	/**
	 * Initial position in Latitude/Longitude format (Ex. "48.869722,2.3075")
	 */
	this.position = position;
	
	/**
	 * Google Map object
	 */
	this.map = null;
	
	/**
	 * Google Map options
	 */
	this.options = null;
	
	/**
	 * Google Map info window object
	 */
	this.infoWindow = null;
	
	/**
	 * Google Map items to display
	 */
	this.items = items;
};

/**
 * GoogleMap prefix
 * @constant
 * @static
 * @private
 */
Exanpe.GoogleMap.GOOGLE_MAP_PREFIX = "exanpe-gmap-";

/**
 * Dom method utility
 * @private
 */
Exanpe.GoogleMap.prototype.getMapContainerEl = function() {
	return YAHOO.util.Dom.get(Exanpe.GoogleMap.GOOGLE_MAP_PREFIX + this.id);
};

/**
 * Called before initialization of the Google Map
 * Does nothing by default, override to define your own action.
 */
Exanpe.GoogleMap.prototype.beforeGoogleMapInit = function() {

};

/**
 * Called after Google Map component initialization
 * Does nothing by default, override to define your own action.
 */
Exanpe.GoogleMap.prototype.afterGoogleMapInit = function() {
	
};

/** 
 * Return latitude from the position
 * @param {String} position the position attribute
 * @private
 */
Exanpe.GoogleMap.prototype.getLatitude = function(position){
	return position.split(',')[0];
};

/** 
 * Return longitude from the position
 * @param {String} position the position attribute
 * @private
 */
Exanpe.GoogleMap.prototype.getLongitude = function(position){
	return position.split(',')[1];
};

/** 
 * Initializes the Map object
 * @private
 */
Exanpe.GoogleMap.prototype._initMap = function(){
	// Init windows
	this.infoWindow = new google.maps.InfoWindow();
	
	// Options
	var position = new google.maps.LatLng(this.getLatitude(this.position), this.getLongitude(this.position));
	this.options = {
			center: position,
			zoom: 15,					
			mapTypeId: google.maps.MapTypeId.ROADMAP,
			mapTypeControl: true,
			mapTypeControlOptions: {
			  style: google.maps.MapTypeControlStyle.DROPDOWN_MENU
			},					
			navigationControl: true,
			navigationControlOptions: {
			  style: google.maps.NavigationControlStyle.SMALL
			}
		};	
	
	// Javascript handler before map initialization
	this.beforeGoogleMapInit();
	
	// Init map
	this.map = new google.maps.Map(this.getMapContainerEl(), this.options);
}

/** 
 * Initializes the Map items objects
 * @private
 */
Exanpe.GoogleMap.prototype._initMapItem = function(item) {
	// Add Marker
	var markerPosition = new google.maps.LatLng(this.getLatitude(item.position), this.getLongitude(item.position));
	var marker = new google.maps.Marker({
			position: markerPosition,
			map: this.map,
			icon : item.icon
		}
	);	
	
	// Add Marker event
	var info = null
	var map = this.map;
	var iw = this.infoWindow;
	if (item.info) {
		info = item.info;
	}
	
	// Marker listener
	google.maps.event.addListener(marker, 'click', function() {
			iw.setContent(info);
			iw.open(map, this);
			map.panTo(markerPosition);
		}
	);
	
	// Item link event
	var mapItem = YAHOO.util.Dom.get(item.id);
	YAHOO.util.Event.addListener(mapItem, "click", function() {
		map.panTo(markerPosition);
		iw.setContent(info);
		iw.open(map, marker);
	});
	
};

/** 
 * Prepare map and items to display
 * @private
 */
Exanpe.GoogleMap.prototype._init = function(){
	// Init map
	this._initMap();
	
	// Init items
	for (var i = 0; i < this.items.length; i++) {
		var item = this.items[i];
		this._initMapItem(item);
	}
	
	// Javascript handler after component initialization
	this.afterGoogleMapInit();
};

/**
 * Initializes the GoogleMap component on DOM load
 * @param {Object} data the json data coming from Java class initialization
 * @private
 * @static
 */
Tapestry.Initializer.googleMapBuilder = function(data){
	var googleMap = new Exanpe.GoogleMap(data.id, data.position, YAHOO.lang.JSON.parse(data.items));
	googleMap._init();
	window[data.id] = googleMap;
};

/** List Sorter **/

/** 
 * Constructor
 * @class Represents a ListSorter component. Can be accessed through JavaScript by its id.
 * @param {String} id the id of the component
 * @param {String} urlSave the url used to save the new order
 */
Exanpe.ListSorter = function(id, urlSave){
	this.id = id;
	this.urlSave = urlSave;
};

/**
 * New order parameter for save
 * @const
 * @static
 * @private
 */
Exanpe.ListSorter.PARAM_NEW_ORDER = "order";

/**
 * Init the markup of the component
 * @private
 */
Exanpe.ListSorter.prototype._init = function(){
	var ul = this.getListRoot();
	var lis = this.getListElements();
	
	new YAHOO.util.DDTarget(ul.id);
	
    for (var i=0; i<lis.length ;i++) {
        new Exanpe.ListSorter.Draggable(lis[i].id, this.id, this.id);
    }
    
    var lst = this;
    
    var form = this.getParentForm();
    
    form.observe(Tapestry.FORM_PREPARE_FOR_SUBMIT_EVENT, function(){
    	YAHOO.util.Dom.get(lst.id+"_input").value = YAHOO.lang.JSON.stringify(lst.getListOrder());
    });
    
    this.initiateOrder();
};

/**
 * Save the order on server side, using an Ajax request
 */
Exanpe.ListSorter.prototype.save = function(){
	var order = this.getListOrder();
	
	var sorter = this;
	
	// Ajax Failure handler
	var failureHandler = function(o){
		sorter.request = null;
		Exanpe.Log.error("Could not process ListSorter update");
	};

	// Ajax Success handler
	var successHandler = function(o){
		
	};

	// Callback objects
	var callback = 
	{
		success: successHandler,
		failure: failureHandler
	};

	var request = YAHOO.util.Connect.asyncRequest(
			"GET",
			this.urlSave + "?" + Exanpe.ListSorter.PARAM_NEW_ORDER + "=" + YAHOO.lang.JSON.stringify(order), 
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
	
	this.initiateOrder();
};

/**
 * Return the component's parent form
 * @return {HTMLElement} the parent form
 */
Exanpe.ListSorter.prototype.getParentForm = function(){
	return YAHOO.util.Dom.getAncestorByTagName(this.getListRoot(), "form");
};

/**
 * Return the component's list root
 * @return {HTMLElement} the parent form
 */
Exanpe.ListSorter.prototype.getListRoot = function(){
	return YAHOO.util.Dom.get(this.id+"_ul");
};

/**
 * Get the current list order
 * This order is returned as an array, with at position 0 the index of the element
 * @return {Array} the array of order
 */
Exanpe.ListSorter.prototype.getListOrder = function(){
	var lis = this.getListElements();
	var list = [];

	for(var i=0;i<lis.length;i++){
		list.push(lis[i].initialPosition);
	}
	
	return list;
};

/**
 * Initiate the list positions
 * @private
 */
Exanpe.ListSorter.prototype.initiateOrder = function(){
	var lis = this.getListElements();

	for(var i=0;i<lis.length;i++){
		lis[i].initialPosition = i;
	}
};

/**
 * Return the elements of this list
 * @return {Array} the elements of this list, as HTMLElement array
 */
Exanpe.ListSorter.prototype.getListElements = function(){
	var id = this.id;
	
	var result = YAHOO.util.Dom.getElementsBy(function(el){return el.id.indexOf(id)==0;}, "li", this.getListRoot().id);
	id = null;
	
	return result;
};

Exanpe.ListSorter.Draggable =  function(id, sGroup, containerId) {

	Exanpe.ListSorter.Draggable.superclass.constructor.call(this, id, sGroup);
	
    var el = this.getDragEl();
    YAHOO.util.Dom.setStyle(el, "opacity", 0.67); // The proxy is slightly transparent

    this.goingUp = false;
    this.lastY = 0;
    
    this.containerId = YAHOO.util.Dom.get(containerId);
    
    this.initConstraints();
};

if(YAHOO && YAHOO.util && YAHOO.util.DDProxy){
	YAHOO.extend(Exanpe.ListSorter.Draggable, YAHOO.util.DDProxy, {
	
		initConstraints: function() {
	        var region = YAHOO.util.Dom.getRegion(this.containerId);
	
	        var el = this.getEl();
	
	        var xy = YAHOO.util.Dom.getXY(el);
	
	        var width = parseInt(YAHOO.util.Dom.getStyle(el, 'width'), 10);
	        var height = parseInt(YAHOO.util.Dom.getStyle(el, 'height'), 10);
	
	        var left = xy[0] - region.left;
	
	        var right = region.right - xy[0] - width;
	
	        var top = xy[1] - region.top;
	
	        var bottom = region.bottom - xy[1] - height;
	
	        this.setXConstraint(left, right);
	        this.setYConstraint(top, bottom);
	    },
		
	    startDrag: function(x, y) {
	        // make the proxy look like the source element
	        var dragEl = this.getDragEl();
	        var clickEl = this.getEl();
	        YAHOO.util.Dom.setStyle(clickEl, "visibility", "hidden");
	
	        dragEl.innerHTML = clickEl.innerHTML;
	
	        YAHOO.util.Dom.setStyle(dragEl, "color", YAHOO.util.Dom.getStyle(clickEl, "color"));
	        YAHOO.util.Dom.setStyle(dragEl, "backgroundColor", YAHOO.util.Dom.getStyle(clickEl, "backgroundColor"));
	        YAHOO.util.Dom.setStyle(dragEl, "border", "2px solid gray");
	    },
	
	    endDrag: function(e) {
	
	        var srcEl = this.getEl();
	        var proxy = this.getDragEl();
	
	        // Show the proxy element and animate it to the src element's location
	        YAHOO.util.Dom.setStyle(proxy, "visibility", "");
	        var a = new YAHOO.util.Motion( 
	            proxy, { 
	                points: { 
	                    to: YAHOO.util.Dom.getXY(srcEl)
	                }
	            }, 
	            0.2, 
	            YAHOO.util.Easing.easeOut 
	        );
	        var proxyid = proxy.id;
	        var thisid = this.id;
	
	        // Hide the proxy and show the source element when finished with the animation
	        a.onComplete.subscribe(function() {
	                YAHOO.util.Dom.setStyle(proxyid, "visibility", "hidden");
	                YAHOO.util.Dom.setStyle(thisid, "visibility", "");
	            });
	        a.animate();
	    },
	
	    onDragDrop: function(e, id) {
	
	        // If there is one drop interaction, the li was dropped either on the list,
	        // or it was dropped on the current location of the source element.
	        if (YAHOO.util.DragDropMgr.interactionInfo.drop.length === 1) {
	
	            // The position of the cursor at the time of the drop (YAHOO.util.Point)
	            var pt = YAHOO.util.DragDropMgr.interactionInfo.point; 
	
	            // The region occupied by the source element at the time of the drop
	            var region = YAHOO.util.DragDropMgr.interactionInfo.sourceRegion; 
	
	            // Check to see if we are over the source element's location.  We will
	            // append to the bottom of the list once we are sure it was a drop in
	            // the negative space (the area of the list without any list items)
	            if (!region.intersect(pt)) {
	                var destEl = YAHOO.util.Dom.get(id);
	                var destDD = YAHOO.util.DragDropMgr.getDDById(id);
	                destEl.appendChild(this.getEl());
	                destDD.isEmpty = false;
	                YAHOO.util.DragDropMgr.refreshCache();
	            }
	
	        }
	    },
	
	    onDrag: function(e) {
	
	        // Keep track of the direction of the drag for use during onDragOver
	        var y = YAHOO.util.Event.getPageY(e);
	
	        if (y < this.lastY) {
	            this.goingUp = true;
	        } else if (y > this.lastY) {
	            this.goingUp = false;
	        }
	
	        this.lastY = y;
	    },
	
	    onDragOver: function(e, id) {
	    
	        var srcEl = this.getEl();
	        var destEl = YAHOO.util.Dom.get(id);
	
	        // We are only concerned with list items, we ignore the dragover
	        // notifications for the list.
	        if (destEl.nodeName.toLowerCase() == "li") {
	            var orig_p = srcEl.parentNode;
	            var p = destEl.parentNode;
	
	            if (this.goingUp) {
	                p.insertBefore(srcEl, destEl); // insert above
	            } else {
	                p.insertBefore(srcEl, destEl.nextSibling); // insert below
	            }
	
	            YAHOO.util.DragDropMgr.refreshCache();
	        }
	    }
	});
}
/**
 * Initializes the ListSorter component on DOM load
 * @param {Object} data the json data coming from Java class initialization
 * @private
 * @static
 */
Tapestry.Initializer.listSorterBuilder = function(data){
	var listSorter = new Exanpe.ListSorter(data.id, data.urlSave);
	listSorter._init();
	window[data.id] = listSorter;
};


/** Rich Text Editor **/

/** 
 * Constructor
 * @class Represents a RichTextEditor object
 * @param {String} id the id of the RichTextEditor Mixin container element
 * @param {String} title the title of the dialog box
 * @param {boolean} collapse if the Editor toolbar can be collapsed or not  
 */
Exanpe.RichTextEditor = function(id, title, width, height, autofocus, collapse) {
	/**
	 * The id of the instance
	 */
	this.id = id;
		
	/**
	 * Editor title
	 */
	this.title = title;
	
	/**
	 * Editor textarea width
	 */
	this.width = width;
	
	/**
	 * Editor textarea height
	 */
	this.height = height;
	
	/**
	 * Autofocus the textarea on display or not
	 */
	this.autofocus = autofocus;
	
	/**
	 * Collapse/Expand Toolbar
	 */
	this.collapse = collapse;
	
	/**
	 * YUI Editor toolbar
	 */
	this.toolbar = null
	
	/**
	 * The wrapped YUI Editor widget
	 */
	this.yui = null;
};

/**
 * Get the toolbar object of the RichTextEditor
 * @returns {Object} the json object used to configure the toolbar
 */
Exanpe.RichTextEditor.prototype.getToolbar = function() {
	return this.toolbar;
};

/**
 * Set the toolbar object of the RichTextEditor
 * @param {Object} the json object used to configure the toolbar
 */
Exanpe.RichTextEditor.prototype.setToolbar = function(toolbar) {
	this.toolbar = toolbar;
};

/**
 * Configure the default toolbar of the YUI Editor
 * @private
 */
Exanpe.RichTextEditor.prototype._configToolbar = function() {
	this.toolbar = {
            height: this.height + 'px',
            width: this.width + 'px',
            dompath: false,            
            collapse: this.collapse,
            focusAtStart: this.autofocus,
            toolbar: {
            	titlebar: this.title,
                buttons: [
                    { group: 'parastyle',
                              buttons: [
                              { type: 'select', label: 'Normal', value: 'heading', disabled: true,
                                  menu: [
                                      { text: 'Normal', value: 'none', checked: true },
                                      { text: 'Header 1', value: 'h1' },
                                      { text: 'Header 2', value: 'h2' },
                                      { text: 'Header 3', value: 'h3' },
                                      { text: 'Header 4', value: 'h4' },
                                      { text: 'Header 5', value: 'h5' },
                                      { text: 'Header 6', value: 'h6' }
                                  ]
                              }
                              ]
                    },                           
                    { group: 'textstyle',
                        buttons: [
                            { type: 'select', label: 'Arial', value: 'fontname', disabled: true,
                                menu: [
                                    { text: 'Arial', checked: true },
                                    { text: 'Arial Black' },
                                    { text: 'Comic Sans MS' },
                                    { text: 'Courier New' },
                                    { text: 'Lucida Console' },
                                    { text: 'Tahoma' },
                                    { text: 'Times New Roman' },
                                    { text: 'Trebuchet MS' },
                                    { text: 'Verdana' }
                                ]
                            },
                            { type: 'separator' },
                            { type: 'spin', label: '13', value: 'fontsize', range: [ 8, 72 ], disabled: true },
                            { type: 'separator' },
                            { type: 'color', label: 'Font Color', value: 'forecolor', disabled: true },
                            { type: 'color', label: 'Background Color', value: 'backcolor', disabled: true }
                        ]
                    },
                    { type: 'separator' },
                    { group: 'alignment',
                        buttons: [
                            { type: 'push', label: 'Bold', value: 'bold' },
                            { type: 'push', label: 'Italic', value: 'italic' },
                            { type: 'push', label: 'Underline', value: 'underline' },
                            { type: 'separator' },                                  
                            { type: 'push', label: 'Align Left CTRL + SHIFT + [', value: 'justifyleft' },
                            { type: 'push', label: 'Align Center CTRL + SHIFT + |', value: 'justifycenter' },
                            { type: 'push', label: 'Align Right CTRL + SHIFT + ]', value: 'justifyright' },
                            { type: 'push', label: 'Justify', value: 'justifyfull' },
                            { type: 'separator' },                                  
                            { type: 'push', label: 'HTML Link CTRL + SHIFT + L', value: 'createlink', disabled: true }
                        ]
                    },
                ]
            }            
    };
};

/**
 * Get the parent form of the current textarea.
 * @return {HTMLElement} the DOM element corresponding to the parent form
 * @private
 */
Exanpe.RichTextEditor.prototype.getParentForm = function() {
	var currentEl = YAHOO.util.Dom.get(this.id);
	return currentEl.form;
};

/**
 * This will trigger the editors save handler and place the new content back into the textarea 
 * before the form is submitted.
 * A listener is attached on the native Tapestry.FORM_PREPARE_FOR_SUBMIT_EVENT event to the 
 * textarea parent form.
 */
Exanpe.RichTextEditor.prototype.save = function() {
	var yui = this.yui;
	Event.on(this.getParentForm(), Tapestry.FORM_PREPARE_FOR_SUBMIT_EVENT, function() {yui.saveHTML();});
};

/**
 * Called before rendering RichTextEditor component
 * Does nothing by default, override to define your own action.
 */
Exanpe.RichTextEditor.prototype.beforeRenderRichTextEditor = function() {

};

/**
 * Called after rendering RichTextEditor component
 * Does nothing by default, override to define your own action.
 */
Exanpe.RichTextEditor.prototype.afterRenderRichTextEditor = function() {
	
};

/**
 * Load the mixin
 * @private
 */
Exanpe.RichTextEditor.prototype._init = function() {
	// Toolbar config
	this._configToolbar();
	
	// JS Handler
	this.beforeRenderRichTextEditor();

	// Init editor and replace the HTML textarea
	this.yui = new YAHOO.widget.Editor(this.id, this.toolbar);
	this.yui.render();
	
	// Save html content on submit event
	this.save();
	
	// JS Handler
	this.afterRenderRichTextEditor();
};

/**
 * Initializes the RichTextEditor mixin on DOM load
 * @param {Object} data the json data coming from Java class initialization
 * @private
 * @static
 */
Tapestry.Initializer.richTextEditorBuilder = function(data){
	var rte = new Exanpe.RichTextEditor(data.id, data.title, data.width, data.height, data.autofocus, data.collapse);
	rte._init();
	window[data.id] = rte;
};
