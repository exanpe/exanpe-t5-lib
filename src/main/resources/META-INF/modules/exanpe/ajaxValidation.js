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
		"yui/2.9.0/connection-min",
		"yui/2.9.0/json-min"], function(ajaxValidation) {

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
				Exanpe.Utils.addQueryParam(this.url, Exanpe.AjaxValidation.PARAM_NAME, YAHOO.util.Dom.get(this.id).value),
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
	function ajaxValidationBuilder(data) {
		var av = new Exanpe.AjaxValidation(data.id, data.url, data.message);
		av._init();
		window[av.id] = av;
	};
	
	
	return {
		init: ajaxValidationBuilder
	}
	
});