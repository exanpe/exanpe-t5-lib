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
		"yui/2.9.0/container-min",
		"yui/2.9.0/menu-min"], function(menuBar) {

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
	function menuBarBuilder(data) {
		var yui = new YAHOO.widget.MenuBar(data.id, {
			hidedelay:500,
			autosubmenudisplay: (data.eventType == "hover"),
			lazyload: true
		});
		
		var menuBar = new Exanpe.MenuBar(data.id, yui);
		menuBar._init();
		window[data.id] = menuBar;
	};

	return {
		init: menuBarBuilder
	}
	
});