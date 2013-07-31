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
		"yui/2.9.0/element-min",
		"yui/2.9.0/carousel-min"], function(slideShow) {

	/** 
	 * Constructor
	 * @class Represents a SlideShow. Can be accessed through JavaScript by its id.
	 * @param {String} id the id of the slide show
	 * @param {YUI} yui the YUI carousel object wrapped
	 */
	Exanpe.SlideShow = function(id, yui){
		/**
		 * this instance id
		 */
		this.id = id;
		
		/**
		 * this wrapped yui carousel instance
		 */
		this.yui = yui;
	};

	/**
	 * Initializes a slide show on dom load
	 * @param {Object} data the json data coming from Java class initialization
	 * @private
	 * @static
	 */
	function slideShowBuilder(data) {
		var carousel = new YAHOO.widget.Carousel(data.id, {
		    isCircular: data.circular,
		    numVisible : data.numVisible,
		    animation : {speed : 0.5},
		    autoPlayInterval : data.autoPlayMillis
		});
		carousel.render();
		
		if(data.autoPlayMillis !== 0){
			carousel.startAutoPlay();
		}
			
		window[data.id] = carousel;
	};
		
	return {
		init: slideShowBuilder
	}
	
});