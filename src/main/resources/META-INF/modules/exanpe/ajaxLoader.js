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
		"yui/2.9.0/element-min",
		"yui/2.9.0/container-min",
		"yui/2.9.0/connection-min",
		"yui/2.9.0/json-min",
		"yui/2.9.0/animation-min"], function(ajaxLoader) {
	
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
	function ajaxLoaderBuilder(data) {
		var ajaxLoader = new Exanpe.AjaxLoader(data.id, data.message, data.image, data.width, data.url, data.autoLoad, data.showPanel, data.highlight);
		window[data.id] = ajaxLoader;
		ajaxLoader._init();
	};
	
	return {
		init: ajaxLoaderBuilder
	}
	
});