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
		"yui/2.9.0/connection-min",
		"yui/2.9.0/json-min"], function(psc) {
	
	/** 
	 * Constructor
	 * @class Represents a PasswordStrengthChecker.
	 * @param {String} id the id of the password field to bind on
	 * @param {Boolean} ajax whether the password is checked in Ajax mode, or not 
	 * @param {String} javascriptChecker the javascript function provided by user to check the password client-side
	 * @param {int} min the number of caracters before triggering Ajax request (used only within ajax parameter set to true)
	 * @param {String} url the url to submit the selected value
	 * @param {Object.<string, string>} messages the messages used to display visual feedback
	 */
	Exanpe.PasswordStrengthChecker = function(id, ajax, javascriptChecker, min, url, messages){
		/**
		 * Password field id
		 */
		this.id = id;
		
		/**
		 * Is ajax mode or not
		 */
		this.ajax = ajax;
		
		/**
		 * The function used client-side for checking the user password.
		 * MUST return an element of Exanpe.PasswordStrengthChecker.Complexity enum
		 */
		this.javascriptChecker = javascriptChecker;
		
		/**
		 * The number of caracters before triggering ajax request
		 */
		this.min = min;
		
		/**
		 * The messages used for this component
		 */
		this.messages = messages;
		
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
	 * Enum for password complexity
	 * @enum {String}
	 * @static
	 */
	Exanpe.PasswordStrengthChecker.Complexity = {
		VERY_WEAK: "veryweak",
		WEAK: "weak",
		STRONG: "strong",
		STRONGEST: "strongest"
	};

	/**
	 * Password Strenght Checker prefix
	 * @constant
	 * @static
	 * @private
	 */
	Exanpe.PasswordStrengthChecker.PSC_PREFIX = "exanpe-psc-";

	/**
	 * Tag Meter CSS Prefix
	 * @constant
	 * @static
	 * @private
	 */
	Exanpe.PasswordStrengthChecker.TAG_METER_CSS_PREFIX = "exanpe-psc-tag-meter-";

	/**
	 * Tag Meter CSS class
	 * @constant
	 * @static
	 * @private
	 */
	Exanpe.PasswordStrengthChecker.DEFAULT_TAG_METER_CSS_CLASS = "exanpe-psc-tag";

	/**
	 * Request parameter used to check the password value in Ajax mode
	 * @see Java PasswordStrengthChecker.PARAM_NAME
	 * @constant
	 * @static
	 * @private
	 */
	Exanpe.PasswordStrengthChecker.PARAM_NAME = "pwd";

	/**
	 * Get the Password field element the mixin is attached to
	 * 
	 * @param {String} id the id of the password field
	 * @return {HTMLElement} the DOM element corresponding to the password field
	 */
	Exanpe.PasswordStrengthChecker.prototype.getPasswordFieldEl = function(id){
		return YAHOO.util.Dom.get(id);
	};

	/**
	 * Get the unique id of the Tapestry mixin
	 * @return {String} the id of the mixin
	 */
	Exanpe.PasswordStrengthChecker.prototype.getMixinId = function(){
		return Exanpe.PasswordStrengthChecker.PSC_PREFIX + this.id;
	};

	/**
	 * Display the password meter
	 * @private
	 */
	Exanpe.PasswordStrengthChecker.prototype._display = function() {
		var overlay = new YAHOO.widget.Overlay(this.getMixinId(), {visible: true}); 
		var region = YAHOO.util.Dom.getRegion(this.id);
		// Set the x and y positions simultaneously
		overlay.cfg.setProperty("xy", [region.right, region.top]);
		// Render the overlay
		overlay.render();
	};

	/**
	 * Compute the visual feedback.
	 * The complexity policy (strength) of the password is built upon your own formulas by providing 
	 * client-side rules (javascriptChecker) or server-side rules (ajaxChecker).
	 * @param {String} strength the strength of the password.
	 * @private
	 */
	Exanpe.PasswordStrengthChecker.prototype._compute = function(/**String */ strength) {
		
		var css = "";
		var level = 0;
		var message;
		
		if (strength) {
			css = Exanpe.PasswordStrengthChecker.TAG_METER_CSS_PREFIX + strength;
		}
		
		switch (strength) {
			case Exanpe.PasswordStrengthChecker.Complexity.VERY_WEAK:
				message = this.messages.veryweak;
				level = 0;
				break;
			case Exanpe.PasswordStrengthChecker.Complexity.WEAK:
				message = this.messages.weak;
				level = 1;
				break;	
			case Exanpe.PasswordStrengthChecker.Complexity.STRONG:
				message = this.messages.strong;
				level = 2;
				break;		
			case Exanpe.PasswordStrengthChecker.Complexity.STRONGEST:
				message = this.messages.strongest;
				level = 3;
				break;
			default:
				message = this.messages.disclaimer;
				break;
		}

		var disclaimerPlaceholder = YAHOO.util.Dom.get(Exanpe.PasswordStrengthChecker.PSC_PREFIX + this.id + "-msg");
		disclaimerPlaceholder.innerHTML = message;
		
		// draw the visual result
		for (i = 0; i < 4; i++) {
			var tag = YAHOO.util.Dom.get(Exanpe.PasswordStrengthChecker.PSC_PREFIX + this.id + "-tag-" + i);
			if (tag) {
				tag.className = "";
				YAHOO.util.Dom.addClass(tag, Exanpe.PasswordStrengthChecker.DEFAULT_TAG_METER_CSS_CLASS);
				if (i <= level) {
					YAHOO.util.Dom.addClass(tag, css);
				}
			}
		}
		
	};

	/**
	* Called on Ajax update success.
	* Does nothing by default, override to define your own action.
	* @event
	*/
	Exanpe.PasswordStrengthChecker.prototype.afterAjaxCheckerSuccess = function(){

	};

	/**
	* Called on Ajax update failure.
	* Does nothing by default, override to define your own action.
	* @event
	*/
	Exanpe.PasswordStrengthChecker.prototype.afterAjaxCheckerFailure = function(){

	};

	/**
	 * Execute an Ajax request for checking the strength of the password
	 * @param {String} password the password to check
	 * @private
	 */
	Exanpe.PasswordStrengthChecker.prototype._ajaxChecker = function (password){ 	
		
		var psc = this;
		
		// Ajax Failure handler
		var failureHandler = function(o){
			psc.request = null;
			Exanpe.Log.error("Could not process ajax check on password provided");
			psc.afterAjaxCheckerFailure();
		};

		// Ajax Success handler
		var successHandler = function(o){
			var json = YAHOO.lang.JSON.parse(o.responseText);
			var strength = json.result;
			if (strength){
				psc._compute(strength);
			}
			psc.afterAjaxCheckerSuccess();
		};

		// Callback objects
		var callback = 
		{
			success: successHandler,
			failure: failureHandler
		};

		var request = YAHOO.util.Connect.asyncRequest(
				"GET",
				Exanpe.Utils.addQueryParam(this.url, Exanpe.PasswordStrengthChecker.PARAM_NAME, password),
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
	 * Method used to check the complexity of the password: client-side (ajax parameter 
	 * is set to false) or server-side (ajax parameter is set to true)
	 * @private
	 */
	Exanpe.PasswordStrengthChecker.prototype._checkPasswordStrength = function() {
		var passwordField = this.getPasswordFieldEl(this.id)
		var passwordValue = passwordField.value;
		var strength = "";
		// Initialize password meter
		this._compute(strength);

		// By default, javascript mode
		if (this.javascriptChecker) {
			if (passwordValue != "") {
				var method = eval(this.javascriptChecker);
				strength = method(passwordValue);
				this._compute(strength);
			}
		}

		// else, Ajax mode
		if (this.ajax) {
			if (passwordValue.length >= this.min) {
				this._ajaxChecker(passwordValue);
			}
		}
	};

	/**
	 * Initialize the component
	 * @private
	 */
	Exanpe.PasswordStrengthChecker.prototype._init = function() {
		var passwordField = this.getPasswordFieldEl(this.id)
		YAHOO.util.Event.addListener(this.id, "keyup", Exanpe.PasswordStrengthChecker.prototype._checkPasswordStrength, this, true);
		// Display the password meter
		this._display();
		// Compute the password complexity
		this._compute();
	};

	/**
	 * Initialize the password strength checker mixin
	 * @param {Object} data the json data coming from Java class initialization
	 * @private
	 * @static
	 */
	function passwordStrengthCheckerBuilder(data) {
		var psc = new Exanpe.PasswordStrengthChecker(data.id, data.ajax, data.javascriptChecker, data.min, data.url, YAHOO.lang.JSON.parse(data.messages));
		psc._init();
		window[data.id] = psc;
	};
	
	return {
		init: passwordStrengthCheckerBuilder
	}
	
});