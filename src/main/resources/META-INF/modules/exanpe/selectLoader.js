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
		"yui/2.9.0/connection-min"], function(selectLoader) {

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
			Exanpe.Utils.addQueryParam(this.url, Exanpe.SelectLoader.PARAM_NAME, value),
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
	function selectLoaderBuilder(data) {
		var sl = new Exanpe.SelectLoader(data.id, data.targetId, data.url);
		sl._init();
		window[data.id] = sl;
	};
	
	
	return {
		init: selectLoaderBuilder
	}
	
});