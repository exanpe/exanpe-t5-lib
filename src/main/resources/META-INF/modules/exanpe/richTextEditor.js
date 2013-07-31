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
		"yui/2.9.0/container_core-min",
		"yui/2.9.0/menu-min",
		"yui/2.9.0/button-min",
		"yui/2.9.0/editor-min",
		"yui/2.9.0/json-min"], function(rte) {

	/** 
	 * Constructor
	 * @class Represents a RichTextEditor object
	 * @param {String} id the id of the RichTextEditor Mixin container element
	 * @param {String} title the title of the dialog box
	 * @param {boolean} collapse indicating if the the toolbar should have a collapse button or not
	 * @param {Array} include list of buttons to include : only theses buttons will be retained
	 * @param {Array} exclude list of buttons to exclude
	 * @param {Object.<string, string>} messages the messages used to display Toolbar buttons labels 
	 */
	Exanpe.RichTextEditor = function(id, title, width, height, autofocus, collapse, include, exclude, messages) {
		/**
		 * The id of the instance
		 */
		this.id = id;
			
		/**
		 * Editor title
		 */
		this.title = title;
		
		/**
		 * Editor textarea width
		 */
		this.width = width;
		
		/**
		 * Editor textarea height
		 */
		this.height = height;
		
		/**
		 * List of buttons to include (and only these buttons)
		 */
		this.include = include;
		
		/**
		 * List of buttons to exclude
		 */
		this.exclude = exclude;
		
		/**
		 * Autofocus the textarea on display or not
		 */
		this.autofocus = autofocus;
		
		/**
		 * Collapse/Expand the toolbar
		 */
		this.collapse = collapse;
		
		/**
		 * YUI Editor config object
		 */
		this.config = null;
		
		/**
		 * List of available buttons
		 */
		this.buttons = {};
		
		/**
		 * The wrapped YUI Editor widget
		 */
		this.yui = null;
		
		/**
		 * Messages for Toolbar buttons labels
		 */
		this.messages = messages;
	};

	/**
	 * Get the YUI toolbar object of the RichTextEditor, ONCE loaded.
	 * @returns {Object} the json object used to configure the toolbar
	 */
	Exanpe.RichTextEditor.prototype.getToolbar = function() {
		return this.yui.toolbar;
	};

	/**
	 * Get the configuration object of the RichTextEditor
	 * @returns {Object} the json object used to configure the RTE
	 */
	Exanpe.RichTextEditor.prototype.getConfig = function() {
		return this.config;
	};

	/**
	 * Load the list of availables buttons for Rich Text Editor
	 * @private
	 */
	Exanpe.RichTextEditor.prototype._loadAvailableButtons = function() {
		this.buttons['separator'] = { type: 'separator' };
		this.buttons['bold'] = { type: 'push', label: this.messages.bold, value: 'bold' }; 
		this.buttons['italic'] = { type: 'push', label: this.messages.italic, value: 'italic' };
		this.buttons['underline'] = { type: 'push', label: this.messages.underline, value: 'underline' }; 
		this.buttons['justifyleft'] = { type: 'push', label: this.messages.justifyleft, value: 'justifyleft' };
		this.buttons['justifycenter'] = { type: 'push', label: this.messages.justifycenter, value: 'justifycenter' };
		this.buttons['justifyright'] = { type: 'push', label: this.messages.justifyright, value: 'justifyright' };
		this.buttons['createlink'] = { type: 'push', label: this.messages.createlink, value: 'createlink', disabled: true };
		this.buttons['insertunorderedlist'] = { type: 'push', label: this.messages.insertunorderedlist, value: 'insertunorderedlist' };
		this.buttons['insertorderedlist'] = { type: 'push', label: this.messages.insertorderedlist, value: 'insertorderedlist' };
		this.buttons['undo'] = { type: 'push', label: this.messages.undo, value: 'undo', disabled: true };
		this.buttons['redo'] = { type: 'push', label: this.messages.redo, value: 'redo', disabled: true };
		this.buttons['heading'] =  { type: 'select', label: this.messages.heading, value: 'heading', disabled: true,
						            menu: [
						                   { text: this.messages.none, value: 'none', checked: true },
						                   { text: this.messages.h1, value: 'h1' },
						                   { text: this.messages.h2, value: 'h2' },
						                   { text: this.messages.h3, value: 'h3' },
						                   { text: this.messages.h4, value: 'h4' },
						                   { text: this.messages.h5, value: 'h5' },
						                   { text: this.messages.h6, value: 'h6' }
						               ]
						           };
		this.buttons['fontsize'] = { type: 'spin', label: '13', value: 'fontsize', range: [ 8, 72 ], disabled: true };
		this.buttons['forecolor'] = { type: 'color', label: this.messages.forecolor, value: 'forecolor', disabled: true };
		this.buttons['backcolor'] = { type: 'color', label: this.messages.backcolor, value: 'backcolor', disabled: true };
	};


	/**
	 * Return the container of the editor
	 * @return {HTMLElement} the DOM element corresponding to the editor container
	 */
	Exanpe.RichTextEditor.prototype.getContainer = function() {
		return YAHOO.util.Dom.get(this.id + "_container");
	};

	/**
	 * Configure the default toolbar of the YUI Editor
	 * @private
	 */
	Exanpe.RichTextEditor.prototype._configDefaultToolbar = function() {
		this.config = {
	            height: this.height + 'px',
	            width: this.width + 'px',
	            dompath: false,            
	            focusAtStart: this.autofocus,
	            toolbar: {
	            	titlebar: this.title,
	            	collapse: this.collapse,
	                buttons: [
		                { group: 'alignment',
			                      buttons: [
			                          this.buttons['bold'],
			                          this.buttons['italic'],
			                          this.buttons['underline'],
			                          this.buttons['separator'],                                  
			                          this.buttons['justifyleft'],
			                          this.buttons['justifycenter'],
			                          this.buttons['justifyright'],
			                          this.buttons['separator'],                                  
			                          this.buttons['createlink']
			                      ]
		                },                          
		                { group: 'indentlist',
		                    buttons: [
		                        this.buttons['insertunorderedlist'],
		                        this.buttons['insertorderedlist']
		                    ]
		                },               
	                    { group: 'undoredo',
	                    		buttons: [
				                          this.buttons['undo'],
				                          this.buttons['redo']
				                ]
	                    },
	                    { group: 'parastyle',
	                        buttons: [
	                                  	  this.buttons['heading']
	                        ]
	                    },
	                    { group: 'textstyle',
	                        buttons: [
	                            this.buttons['fontsize'],
	                            this.buttons['separator'],
	                            this.buttons['forecolor'],
	                            this.buttons['backcolor']
	                        ]
	                    }
	                ]
	            }            
	    };
	};

	/**
	 * Get the parent form of the current textarea.
	 * @return {HTMLElement} the DOM element corresponding to the parent form
	 * @private
	 */
	Exanpe.RichTextEditor.prototype.getParentForm = function() {
		return YAHOO.util.Dom.getAncestorByTagName(this.id, "form");
	};

	/**
	 * This will trigger the editors save handler and place the new content back into the textarea 
	 * before the form is submitted.
	 * A listener is attached on the native Tapestry.FORM_PREPARE_FOR_SUBMIT_EVENT event to the 
	 * textarea parent form.
	 */
	Exanpe.RichTextEditor.prototype.save = function() {
		var yui = this.yui;
		this.getParentForm().on(Tapestry.FORM_PREPARE_FOR_SUBMIT_EVENT, function() {yui.saveHTML();});
	};


	/**
	 * Build the Toolbar with buttons choosed by user within "include" parameter.
	 * Only these buttons will be retained.
	 * @private
	 */
	Exanpe.RichTextEditor.prototype._includeToolbarButtons = function() {
		var len = this.include.length;
		if (len > 0) {
			this.config['toolbar']['buttons'] = [];
			var custom = {};
			custom['group'] = 'user_include';
			custom['buttons'] = [];
			for (var i=0; i < len; i++) {
				var customButton = this.include[i];
				if (this.buttons[customButton]) {
					custom['buttons'].push(this.buttons[customButton]);
				}
				else {
					Exanpe.Log.error("Button: '" + customButton + "' does not exist for Exanpe Rich Text Editor.");
				}
			}
			this.config['toolbar']['buttons'].push(custom);
		}
	};

	/**
	 * Exclude the buttons contained into "exclude" parameter from the default toolbar
	 * @private
	 */
	Exanpe.RichTextEditor.prototype._excludeToolbarButtons = function() {
		var groups = this.config['toolbar']['buttons'];
		var i, j;
		for(i=0; i<groups.length; i++) {
			var group = this.config['toolbar']['buttons'][i];
			var buttons = group['buttons'];
			var len = buttons.length;
			for (j=0; j < len; j++) {
				var button = this.config['toolbar']['buttons'][i]['buttons'][j];
				if (this.exclude.indexOf(button.value) >= 0) {
					this.config['toolbar']['buttons'][i]['buttons'][j] = '';
				}
			}
		}
	};

	/**
	 * Called before rendering RichTextEditor component
	 * Does nothing by default, override to define your own action.
	 */
	Exanpe.RichTextEditor.prototype.beforeRenderRichTextEditor = function() {
		
	};

	/**
	 * Load the mixin
	 * @private
	 */
	Exanpe.RichTextEditor.prototype._init = function(){
		// Load available Toolbar buttons
		this._loadAvailableButtons();
		
		// Toolbar config
		this._configDefaultToolbar();
		
		// Include buttons only
		this._includeToolbarButtons();
		
		// Exclude buttons
		this._excludeToolbarButtons();
		
		// Init editor
		this.yui = new YAHOO.widget.Editor(this.id, this.config);
		this.yui.wrapper = this;

		// JS Handler
		this.yui.on('toolbarLoaded', function() {
			this.wrapper.beforeRenderRichTextEditor();
		}, this.yui, true);

		// Render editor and replace the HTML textarea
		this.yui.render();
		
		// Save html content on submit event
		this.save();
	};

	/**
	 * Initializes the RichTextEditor mixin on DOM load
	 * @param {Object} data the json data coming from Java class initialization
	 * @private
	 * @static
	 */
	function richTextEditorBuilder(data) {
		var rte = new Exanpe.RichTextEditor(data.id, data.title, data.width, data.height, data.autofocus, data.collapse===true, YAHOO.lang.JSON.parse(data.include), YAHOO.lang.JSON.parse(data.exclude), YAHOO.lang.JSON.parse(data.messages));
		rte._init();
		window[data.id] = rte;
	};

	return {
		init: richTextEditorBuilder
	}
	
});