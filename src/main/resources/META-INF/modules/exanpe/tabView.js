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
		"yui/2.9.0/yahoo-dom-event"], function(tabView) {

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
	function tabViewBuilder(data) {
		var tabView = new Exanpe.TabView(data.id, data.selectedId);

		for(var i=0;i<data.tabs.length;i++){
			tabView.initTab(data.tabs[i].id);
		}
		
		window[data.id] = tabView;
	};
	
	return {
		init: tabViewBuilder
	}
	
});