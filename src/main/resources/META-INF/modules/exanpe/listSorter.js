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
		"yui/2.9.0/animation-min",
		"yui/2.9.0/dragdrop-min",
		"yui/2.9.0/json-min",
		"yui/2.9.0/connection-min"], function(listSorter) {

	/** 
	 * Constructor
	 * @class Represents a ListSorter component. Can be accessed through JavaScript by its id.
	 * @param {String} id the id of the component
	 * @param {String} urlSave the url used to save the new order
	 */
	Exanpe.ListSorter = function(id, urlSave){
		this.id = id;
		this.urlSave = urlSave;
	};

	/**
	 * New order parameter for save
	 * @const
	 * @static
	 * @private
	 */
	Exanpe.ListSorter.PARAM_NEW_ORDER = "permutations";

	/**
	 * Init the markup of the component
	 * @private
	 */
	Exanpe.ListSorter.prototype._init = function(){
		var ul = this.getListRoot();
		var lis = this.getListElements();
		
		new YAHOO.util.DDTarget(ul.id);
		
	    for (var i=0; i<lis.length ;i++) {
	        new Exanpe.ListSorter.Draggable(lis[i].id, this.id, this.getListRoot().id);
	    }
	    
	    var lst = this;
	    
	    var form = this.getParentForm();
	    
	    form.observe(Tapestry.FORM_PREPARE_FOR_SUBMIT_EVENT, function(){
	    	YAHOO.util.Dom.get(lst.id+"_input").value = lst.defaultStringify(lst.getListOrder());
	    });
	    
	    this.initiateOrder();
	};


	/**
	 * Called before triggering an ajax save
	 * Does nothing by default except returing true, override to define your own behavior.
	 * @returns {boolean} false to cancel validation
	 */
	Exanpe.ListSorter.prototype.beforeSave = function(){
		return true;
	};

	/**
	 * Called after triggering an ajax save
	 * Does nothing by default, override to define your own behavior.
	 */
	Exanpe.ListSorter.prototype.afterSave = function(){
		return true;
	};

	/**
	 * Save the order on server side, using an Ajax request
	 */
	Exanpe.ListSorter.prototype.save = function(){
		if(this.beforeSave() == false){
			return;
		}
		
		var order = this.getListOrder();
		
		var sorter = this;
		
		// Ajax Failure handler
		var failureHandler = function(o){
			sorter.request = null;
			Exanpe.Log.error("Could not process ListSorter update");
		};

		// Ajax Success handler
		var successHandler = function(o){
			
		};

		// Callback objects
		var callback = 
		{
			success: successHandler,
			failure: failureHandler
		};

		var request = YAHOO.util.Connect.asyncRequest(
				"GET",
				Exanpe.Utils.addQueryParam(this.urlSave, Exanpe.ListSorter.PARAM_NEW_ORDER, this.defaultStringify(order)),
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
		
		this.initiateOrder();
		
		this.afterSave();
	};

	/**
	 * Return the component's parent form
	 * @return {HTMLElement} the parent form
	 */
	Exanpe.ListSorter.prototype.getParentForm = function(){
		return YAHOO.util.Dom.getAncestorByTagName(this.getListRoot(), "form");
	};

	/**
	 * Return the component's list root
	 * @return {HTMLElement} the parent form
	 */
	Exanpe.ListSorter.prototype.getListRoot = function(){
		return YAHOO.util.Dom.get(this.id+"_ul");
	};

	/**
	 * Get the current list order
	 * This order is returned as an array, with at position 0 the index of the element
	 * @return {Array} the array of order
	 */
	Exanpe.ListSorter.prototype.getListOrder = function(){
		var lis = this.getListElements();
		var list = [];

		for(var i=0;i<lis.length;i++){
			list.push(lis[i].initialPosition);
		}
		
		return list;
	};

	/**
	 * Stringify an array, not using .toJSON object function
	 * @param {Object} o the object to stringify
	 * @return {String} the element stringified, without quotes
	 */
	Exanpe.ListSorter.prototype.defaultStringify = function(o){
		var backupToJSON = o.toJSON; 
		if(backupToJSON){
			o.toJSON = null;
		}
		
		var res = YAHOO.lang.JSON.stringify(o);
		
		if(backupToJSON){
			o.toJSON = backupToJSON;
		}
		
		return res;
	}

	/**
	 * Initiate the list positions
	 * @private
	 */
	Exanpe.ListSorter.prototype.initiateOrder = function(){
		var lis = this.getListElements();

		for(var i=0;i<lis.length;i++){
			lis[i].initialPosition = i;
		}
	};

	/**
	 * Return the elements of this list
	 * @return {Array} the elements of this list, as HTMLElement array
	 * @private
	 */
	Exanpe.ListSorter.prototype.getListElements = function(){
		var id = this.id;
		
		var result = YAHOO.util.Dom.getElementsBy(function(el){return el.id.indexOf(id)==0;}, "li", this.getListRoot().id);
		id = null;
		
		return result;
	};

	/**
	 * Class applied to all elements draggable
	 * @private
	 */
	Exanpe.ListSorter.Draggable =  function(id, sGroup, containerId) {

		Exanpe.ListSorter.Draggable.superclass.constructor.call(this, id, sGroup);
		
	    var el = this.getDragEl();
	    YAHOO.util.Dom.setStyle(el, "opacity", 0.67); // The proxy is slightly transparent

	    this.goingUp = false;
	    this.lastY = 0;
	    
	    this.containerId = YAHOO.util.Dom.get(containerId);
	    
	    this.initConstraints();
	};

	if(YAHOO && YAHOO.util && YAHOO.util.DDProxy){
		YAHOO.extend(Exanpe.ListSorter.Draggable, YAHOO.util.DDProxy, {
		
			initConstraints: function() {
		        var region = YAHOO.util.Dom.getRegion(this.containerId);
		
		        var el = this.getEl();
		
		        var xy = YAHOO.util.Dom.getXY(el);
		
		        var width = parseInt(YAHOO.util.Dom.getStyle(el, 'width'), 10);
		        var height = parseInt(YAHOO.util.Dom.getStyle(el, 'height'), 10);
		
		        var left = xy[0] - region.left;
		
		        var right = region.right - xy[0] - width;
		
		        var top = xy[1] - region.top;
		
		        var bottom = region.bottom - xy[1] - height;
		
		        this.setXConstraint(left, right);
		        this.setYConstraint(top, bottom);
		    },
			
		    startDrag: function(x, y) {
		        // make the proxy look like the source element
		        var dragEl = this.getDragEl();
		        var clickEl = this.getEl();
		        YAHOO.util.Dom.setStyle(clickEl, "visibility", "hidden");
		
		        dragEl.innerHTML = clickEl.innerHTML;
		
		        YAHOO.util.Dom.setStyle(dragEl, "color", YAHOO.util.Dom.getStyle(clickEl, "color"));
		        YAHOO.util.Dom.setStyle(dragEl, "backgroundColor", YAHOO.util.Dom.getStyle(clickEl, "backgroundColor"));
		        YAHOO.util.Dom.setStyle(dragEl, "border", "2px solid #808080");
		    },
		
		    endDrag: function(e) {
		
		        var srcEl = this.getEl();
		        var proxy = this.getDragEl();
		
		        // Show the proxy element and animate it to the src element's location
		        YAHOO.util.Dom.setStyle(proxy, "visibility", "");
		        var a = new YAHOO.util.Motion( 
		            proxy, { 
		                points: { 
		                    to: YAHOO.util.Dom.getXY(srcEl)
		                }
		            }, 
		            0.2, 
		            YAHOO.util.Easing.easeOut 
		        );
		        var proxyid = proxy.id;
		        var thisid = this.id;
		
		        // Hide the proxy and show the source element when finished with the animation
		        a.onComplete.subscribe(function() {
		                YAHOO.util.Dom.setStyle(proxyid, "visibility", "hidden");
		                YAHOO.util.Dom.setStyle(thisid, "visibility", "");
		            });
		        a.animate();
		    },
		
		    onDragDrop: function(e, id) {
		
		        // If there is one drop interaction, the li was dropped either on the list,
		        // or it was dropped on the current location of the source element.
		        if (YAHOO.util.DragDropMgr.interactionInfo.drop.length === 1) {
		
		            // The position of the cursor at the time of the drop (YAHOO.util.Point)
		            var pt = YAHOO.util.DragDropMgr.interactionInfo.point; 
		
		            // The region occupied by the source element at the time of the drop
		            var region = YAHOO.util.DragDropMgr.interactionInfo.sourceRegion; 
		
		            // Check to see if we are over the source element's location.  We will
		            // append to the bottom of the list once we are sure it was a drop in
		            // the negative space (the area of the list without any list items)
		            if (!region.intersect(pt)) {
		                var destEl = YAHOO.util.Dom.get(id);
		                var destDD = YAHOO.util.DragDropMgr.getDDById(id);
		                destEl.appendChild(this.getEl());
		                destDD.isEmpty = false;
		                YAHOO.util.DragDropMgr.refreshCache();
		            }
		
		        }
		    },
		
		    onDrag: function(e) {
		
		        // Keep track of the direction of the drag for use during onDragOver
		        var y = YAHOO.util.Event.getPageY(e);
		
		        if (y < this.lastY) {
		            this.goingUp = true;
		        } else if (y > this.lastY) {
		            this.goingUp = false;
		        }
		
		        this.lastY = y;
		    },
		
		    onDragOver: function(e, id) {
		    
		        var srcEl = this.getEl();
		        var destEl = YAHOO.util.Dom.get(id);
		
		        // We are only concerned with list items, we ignore the dragover
		        // notifications for the list.
		        if (destEl.nodeName.toLowerCase() == "li") {
		            var orig_p = srcEl.parentNode;
		            var p = destEl.parentNode;
		
		            if (this.goingUp) {
		                p.insertBefore(srcEl, destEl); // insert above
		            } else {
		                p.insertBefore(srcEl, destEl.nextSibling); // insert below
		            }
		
		            YAHOO.util.DragDropMgr.refreshCache();
		        }
		    }
		});
	}
	
	/**
	 * Initializes the ListSorter component on DOM load
	 * @param {Object} data the json data coming from Java class initialization
	 * @private
	 * @static
	 */
	function listSorterBuilder(data) {
		var listSorter = new Exanpe.ListSorter(data.id, data.urlSave);
		listSorter._init();
		window[data.id] = listSorter;
	};

	
	return {
		init: listSorterBuilder
	}
	
});