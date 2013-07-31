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
		"yui/2.9.0/yahoo-dom-event"], function(verticalMenu) {

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
	function verticalMenuBuilder(data) {
		var verticalMenu = new Exanpe.VerticalMenu(data.id, data.selectedId);

		for(var i=0;i<data.items.length;i++){
			verticalMenu._initMenu(data.items[i].id);
		}	
		window[data.id] = verticalMenu;
	};
	
	return {
		init: verticalMenuBuilder
	}
	
});