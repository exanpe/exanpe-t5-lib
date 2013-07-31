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
		"yui/2.9.0/animation-min",
		"yui/2.9.0/container-min"], function(tooltip) {

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
	function tooltipBuilder(data) {
		var yuiTooltip = new YAHOO.widget.Tooltip(Exanpe.Tooltip.PREFIX_ID + data.id, {
		    context: data.id,
		    text: data.text,
		    zIndex: Exanpe.Tooltip.ZINDEX
		}); 
		var tooltip = new Exanpe.Tooltip(data.id, yuiTooltip);
		window[data.id] = tooltip;
	};
	
	return {
		init: tooltipBuilder
	}
	
});