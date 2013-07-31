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
		"yui/2.9.0/animation-min"], function(accordion) {
	
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
	function accordionBuilder(data) {
		var accordion = new Exanpe.Accordion(data.id, data.multiple===true, data.eventType, data.duration);
		window[data.id] = accordion;
		
		var itemArray = data.items.split(",");
		
		for(var i=0; i < itemArray.length; i++){
			accordion._initItem(itemArray[i]);
		}
	};
	
	return {
		init: accordionBuilder
	}
	
});