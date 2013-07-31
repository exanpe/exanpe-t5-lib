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
		"yui/2.9.0/dragdrop-min", 
		"yui/2.9.0/container-min", 
		"yui/2.9.0/connection-min"], function(dialog) {
	
	/** 
	 * Constructor
	 * @class Represents a Dialog component
	 * @param {String} id the id of the Dialog Mixin container element
	 * @param {String} message the message of the dialog box
	 * @param {String} targetHtmlId the HTML id used to display the content of the Dialog box instead of message parameter
	 * @param {String} title the title of the dialog box
	 * @param {String} renderMode the render mode to used for displaying Dialog (RENDER_MODE_INFO or RENDER_MODE_CONFIRM)
	 * @param {int} width the with of the dialog box, in pixel
	 * @param {YUI}    yui the YUI Dialog object wrapped
	 */
	Exanpe.Dialog = function(id, message, targetHtmlId, title, renderMode, width, yui) {
		/**
		 * The id of the instance
		 */
		this.id = id;
		
		/**
		 * The Dialog message
		 */
		this.message = message;
		
		/**
		 * Html id used to display Dialog content, instead of message
		 */
		this.targetHtmlId = targetHtmlId;
		
		/**
		 * The Dialog title
		 */
		this.title = title;
		
		/**
		 * The Dialog render mode
		 */
		this.renderMode = renderMode;
		
		/**
		 * The width of the Dialog box
		 */
		this.width = width;
		
		/**
		 * The wrapped yui dialog instance
		 */
		this.yui = yui;
	};

	/**
	 * Disabled CSS class
	 * @constant
	 * @private
	 * @static
	 */
	Exanpe.Dialog.DISABLE_CLASS = "exanpe-dialog-disable";

	/**
	 * CONFIRM mode constant
	 * @constant
	 * @private
	 * @static
	 */
	Exanpe.Dialog.RENDER_MODE_CONFIRM = "confirm";

	/**
	 * INFO mode constant
	 * @constant
	 * @private
	 * @static
	 */
	Exanpe.Dialog.RENDER_MODE_INFO = "info";

	/**
	 * Dialog component id prefix
	 * @see Java Dialog.DIALOG_ID_PREFIX
	 * @constant
	 * @static
	 * @private
	 */
	Exanpe.Dialog.ID_PREFIX = "exanpe-dialog-";

	/**
	 * Dialog YUI id prefix
	 * @constant
	 * @static
	 * @private
	 */
	Exanpe.Dialog.YUI_ID_PREFIX = "exanpe-dialog-yui-";

	/**
	 * Display the dialog box
	 * @param {Event} e the triggered event
	 */
	Exanpe.Dialog.prototype.show = function(e) {
		this.beforeDialogShow();
		if (!YAHOO.util.Dom.hasClass(this.id, Exanpe.Dialog.DISABLE_CLASS))
		{
			this._hideDialogErrorEl();
			this.yui.show();
		}
	};

	/**
	 * Hide the dialog box
	 */
	Exanpe.Dialog.prototype.hide = function() {
		this.yui.hide();
	};

	/**
	 * Set the width of the dialog box
	 * @param {int} width the width to set
	 */
	Exanpe.Dialog.prototype.setWidth = function(width) {
		this.yui.cfg.queueProperty("width", width+"px");
	};

	/**
	 * Set the message of the dialog box
	 * @param {String} message the message to set
	 */
	Exanpe.Dialog.prototype.setMessage = function(message) {
		this.yui.cfg.queueProperty("text", message);
	};

	/**
	 * Execute the redirection after validation
	 * @private
	 */
	Exanpe.Dialog.prototype._doAction = function() {
		var el = YAHOO.util.Dom.get(this.id);
		
		// Submit button
		if (el.click) {
			el.click();
			return;
		}
		// Link
		if (el.href)
		{
			window.location = el.href;
		}
		else 
		{
			Exanpe.Log.error("Dialog CONFIRM mode must only be applied on link or submit elements");
			this.hide();
		}
	};

	/**
	 * Generate the message displayed into Dialog.
	 * @param {String} message the message to display
	 * @private
	 */
	Exanpe.Dialog.prototype._generateDialogMessage = function(message) {
		// Placeholder for error message
		var dialogMessage = "<div id='dialogError-" + this.id + "' class='exanpe-dialog-error'></div>";
		
		dialogMessage += message;
		this.setMessage(dialogMessage);
	}

	/**
	 * Dom method utility
	 * @private
	 */
	Exanpe.Dialog.prototype.getDialogErrorEl = function(){
		return YAHOO.util.Dom.get("dialogError-" + this.id);
	};

	/**
	 * Hide the Dialog error message
	 * @private
	 */
	Exanpe.Dialog.prototype._hideDialogErrorEl = function(){
		YAHOO.util.Dom.setStyle(this.getDialogErrorEl(), "display", "none");
	};

	/**
	 * Called before showing Dialog box
	 * Does nothing by default, override to define your own action.
	 */
	Exanpe.Dialog.prototype.beforeDialogShow = function() {

	};

	/**
	 * Called before process the Dialog validation
	 * Does nothing by default, override to define your own action.
	 */
	Exanpe.Dialog.prototype.beforeDialogValidation = function() {
		
	};

	/**
	 * Called after process the Dialog validation
	 * Does nothing by default, override to define your own action.
	 */
	Exanpe.Dialog.prototype.afterDialogValidation = function() {
		
	};

	/**
	 * Display an error message into the Dialog box
	 * @param {String} errorMsg the errorMsg to display 
	 * @private
	 */
	Exanpe.Dialog.prototype._displayDialogErrorMessage =  function(errorMsg) {
		var dialogErrorEl = this.getDialogErrorEl();
		dialogErrorEl.innerHTML = errorMsg;
		YAHOO.util.Dom.setStyle(dialogErrorEl, "display", "block");
	};

	/**
	 * Initialize the YUI Dialog component
	 * @private
	 */
	Exanpe.Dialog.prototype._init = function() {

		// Define event handlers for Dialog
		this.yui.wrapper = this;
		var handleYes = function() {
			// Before validation
			var errorMsg = this.wrapper.beforeDialogValidation();
			
			if (!errorMsg) {
				this.wrapper.hide();
				this.wrapper._doAction();
				
				// After validation
				this.wrapper.afterDialogValidation();
			}
			else {
				// The error is displayed into Dialog and the event is stoped
				this.wrapper._displayDialogErrorMessage(errorMsg);
			}
		};
		var handleNoOrOk = function() {
			this.wrapper.hide();
		};
		
		// Adding buttons
		var buttons = [];
		if (this.renderMode == Exanpe.Dialog.RENDER_MODE_CONFIRM)
		{
			buttons = [ { text:this.yui.yesLabelButton, handler:handleYes, isDefault:true },
			 		   { text:this.yui.noLabelButton,  handler:handleNoOrOk } ];
		}
		if (this.renderMode == Exanpe.Dialog.RENDER_MODE_INFO)
		{
			buttons = [ { text:this.yui.okLabelButton,  handler:handleNoOrOk } ];
		}
		
		// Configuration
		this.yui.cfg.queueProperty("buttons", buttons);
		this.setWidth(this.width);

		// The content of the Dialog
		if (this.targetHtmlId) {		
			var target = YAHOO.util.Dom.get(this.targetHtmlId);	
			YAHOO.util.Dom.setStyle(this.targetHtmlId, "display", "none");
			this._generateDialogMessage(target.innerHTML);
		}
		else {
			this._generateDialogMessage(this.message)
		}
		
		// Render the Dialog
		this.yui.setHeader(this.title);
		this.yui.render(Exanpe.Dialog.ID_PREFIX + this.id);

		YAHOO.util.Event.addListener(this.id, "mousedown", Exanpe.Dialog.prototype.show, this, true);
		YAHOO.util.Event.on(this.id, "click", function(e) {
			YAHOO.util.Event.stopPropagation(e);
		});		
		
	};

	/**
	 * Initializes the dialog component on dom load
	 * @param {Object} data the json data coming from Java class initialization
	 * @private
	 * @static
	 */
	function dialogBuilder(data) {
		var yuiDialog =  new YAHOO.widget.SimpleDialog(Exanpe.Dialog.YUI_ID_PREFIX + data.id, 
					 { fixedcenter: true,
					   visible: false,
					   draggable: true,
					   close: true,
					   constraintoviewport: true,
					   modal: true
					 } );
			yuiDialog.yesLabelButton = data.yesLabelButton;
			yuiDialog.noLabelButton = data.noLabelButton;
			yuiDialog.okLabelButton = data.okLabelButton;
		
		var dialog = new Exanpe.Dialog(data.id, data.message, data.targetHtmlId, data.title, data.renderMode, data.width, yuiDialog);
		window[data.id] = dialog;
		dialog._init();
	};	
	
	return {
		init: dialogBuilder
	}
	
});