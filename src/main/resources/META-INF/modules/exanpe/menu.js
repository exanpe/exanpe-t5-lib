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
		"yui/2.9.0/menu-min"], function(menu) {

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
	function menuBuilder(data) {
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
	
	return {
		init: menuBuilder
	}
	
});