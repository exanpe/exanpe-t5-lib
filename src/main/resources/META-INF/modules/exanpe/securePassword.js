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
		"yui/2.9.0/yahoo-dom-event"], function(securePassword) {

	/** 
	 * Constructor
	 * @class Represents a Secure Password. Can be accessed through JavaScript by its id.
	 * @param {String} id the id of the main accordion element
	 * @param {Object} positionnings the number positionning
	 */
	Exanpe.SecurePassword = function(id, positionnings){
		/**
		 * id of the instance
		 */
		this.id = id;
		
		/**
		 * positionning object
		 */
		this.positionnings = positionnings;
		this.timeoutHover = undefined;
	};

	/**
	 * Initialize the component from server value
	 * @private
	 */
	Exanpe.SecurePassword.prototype._init = function(){
		if(this.getHiddenEl().value && this.getHiddenEl() !== ""){
			this.getInputEl().value = this.getHiddenEl().value; 
		}
	};

	/**
	 * Input element suffix
	 * @static
	 * @constant
	 * @private
	 */
	Exanpe.SecurePassword.INPUT_SUFFIX = "_input";

	/**
	 * Hidden element suffix
	 * @static
	 * @constant
	 * @private
	 */
	Exanpe.SecurePassword.HIDDEN_SUFFIX = "_hidden";

	/**
	 * Time to wait on hovering, before recording the number. In milliseconds.
	 * @static
	 * @constant
	 */
	Exanpe.SecurePassword.HOVER_WAIT = 1000;

	/**
	 * Method canceling a hover in progress
	 * @param {Event} e the event of cancel
	 * @private
	 */
	Exanpe.SecurePassword.prototype._cancelHover = function(e){
		window.clearTimeout(this.timeoutHover);
		this.timeoutHover = null;
	};

	/**
	 * Method triggered on hover start
	 * @param {Event} e the event of hovering
	 * @private
	 */
	Exanpe.SecurePassword.prototype._hover = function(e){

		var value = this.positionnings[YAHOO.util.Event.getTarget(e).id];
		
		YAHOO.util.Event.addListener(YAHOO.util.Event.getTarget(e).id, "mouseout", Exanpe.SecurePassword.prototype._cancelHover, this, true);
		if(this.timeoutHover){
			this._cancelHover();
		}
		
		var obj = this;
		
		this.timeoutHover = window.setTimeout(function(){
			obj._processRecording(value);
			obj = null;
			value = null;
		}, Exanpe.SecurePassword.HOVER_WAIT);	
		
	};

	/**
	 * Method triggered on click
	 * @param {Event} e the event of hovering
	 * @private
	 */
	Exanpe.SecurePassword.prototype._click = function(e){
		var value = this.positionnings[YAHOO.util.Event.getTarget(e).id];
		this._processRecording(value);
	};


	/**
	 * Method actually performing the record of a value
	 * @param {char} value the character to record
	 * @private
	 */
	Exanpe.SecurePassword.prototype._processRecording = function(value){
		var inputEl = this.getInputEl();
		var pwdEl = this.getHiddenEl();
		
		if(!inputEl.value){
			inputEl.value = value;
			pwdEl.value = inputEl.value;
			return;
		}
		
		if(inputEl.size && inputEl.value.length >= inputEl.size){
			return;
		}
		
		inputEl.value = inputEl.value + value;
		pwdEl.value = inputEl.value;
		pwdEl = null;
		inputEl = null;
	};

	/**
	 * Reset the value of the component 
	 */
	Exanpe.SecurePassword.prototype.reset = function(){
		this.getInputEl().value = "";
		this.getHiddenEl().value = "";
	};

	/**
	 * Return the input element for the component
	 * @returns {HTMLElement} the input element
	 * @private
	 */
	Exanpe.SecurePassword.prototype.getInputEl = function(){
		return YAHOO.util.Dom.get(this.id+Exanpe.SecurePassword.INPUT_SUFFIX);
	};

	/**
	 * Return the hidden field element for the component, actually storing the password
	 * @returns {HTMLElement} the hidden field element
	 * @private
	 */
	Exanpe.SecurePassword.prototype.getHiddenEl = function(){
		return YAHOO.util.Dom.get(this.id+Exanpe.SecurePassword.HIDDEN_SUFFIX);
	};

	/**
	 * Initializes a secure password on dom load
	 * @param {Object} data the json data coming from Java class initialization
	 * @private
	 * @static
	 */
	function securePasswordBuilder(data) {

		var securepwd = new Exanpe.SecurePassword(data.id, data.positionnings);
		window[data.id] = securepwd;
		
		var listener = Exanpe.SecurePassword.prototype._click;
		if(data.eventType == "hover"){
			listener = Exanpe.SecurePassword.prototype._hover;
			data.eventType = "mouseover";
		}

		var toBind = YAHOO.util.Dom.getElementsBy(function(){return true;}, "td", YAHOO.util.Dom.get(data.id));

		for(var i=0;i<toBind.length;i++){
			YAHOO.util.Event.addListener(YAHOO.util.Dom.get(toBind[i]), data.eventType, listener, securepwd, true);
		}
		
		securepwd._init();
	};

	return {
		init: securePasswordBuilder
	}
	
});