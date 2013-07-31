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
		"yui/2.9.0/animation-min"], function(hideablePanel) {

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
	function hideablePanelBuilder(data) {
		var hdp = new Exanpe.HideablePanel(data.id, data.hideWidth, data.duration);
		window[hdp.id] = hdp;
	};

	return {
		init: hideablePanelBuilder
	}
});