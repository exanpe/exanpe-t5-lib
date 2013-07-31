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
		"yui/2.9.0/json-min"], function(gMap) {
	
	/** 
	 * Constructor
	 * @class Represents a GoogleMap component. Can be accessed through JavaScript by its id.
	 * @param {String} id the id of the component
	 * @param {String} latitude the latitude position used to center the map
	 * @param {String} longitude the longitude position used to center the map 
	 * @param {String} mapType the type of map to display
	 * @param {int} zoom the initial Map zoom level
	 * @param {boolean} draggable indicating if the map is draggable or not
	 * @param {Object[]} markers the markers to display into the map
	 * @param {Object[]} polyPoints the points used to display polyline into the map
	 * @param {String} polyStrokeColor the color of Polyline/Polygon lines
	 * @param {String} polyStrokeOpacity the opacity Polyline/Polygon line's color
	 * @param {String} polyStrokeWeight the weight of Polyline/Polygo line's in pixels.
	 * @param {boolean} polygon draw a Polygon instead of Polyline (default) if set to true
	 */
	Exanpe.GMap = function(id, latitude, longitude, mapType, zoom, draggable, markers, polyPoints, polyStrokeColor, polyStrokeOpacity, polyStrokeWeight, polygon){
		
		/**
		 * Id of the instance
		 */
		this.id = id;
		
		/**
		 * Initial Latitude used to center the map
		 */
		this.latitude = latitude;
		
		/**
		 * Initial Longitude used to center the map
		 */
		this.longitude = longitude;
		
		/**
		 * The type of map to display (ROADMAP, HYBRID, ...)
		 */
		this.mapType = mapType;
		
		/**
		 * Initial Map zoom level
		 */
		this.zoom = zoom;
		
		/**
		 * If the map is draggable or not
		 */
		this.draggable = draggable;
		
		/**
		 * Google Map options
		 */
		this.options = null;
		
		/**
		 * Google Map info window object
		 */
		this.infoWindow = null;
		
		/**
		 * Google Map markers to display
		 */
		this.markers = markers;
		
		/**
		 * Google Map points used by Polyline
		 */
		this.polyPoints = polyPoints;
		
		/**
		 * Color of Polyline/Polygon lines
		 */
		this.polyStrokeColor = polyStrokeColor;
		
		/**
		 * Opacity of Polyline/Polygon line's color
		 */
		this.polyStrokeOpacity = polyStrokeOpacity;
		
		/**
		 * Weight of Polyline/Polygo line's in pixels.
		 */
		this.polyStrokeWeight = polyStrokeWeight;
		
		/**
		 * Draw a Polygon instead of Polyline (default) if set to true
		 */
		this.polygon = polygon;
		
		/**
		 * Google Map object
		 */
		this.map = null;
		
		/**
		 * Google Map Poly object
		 */
		this.poly = null;
	};

	/**
	 * GoogleMap prefix
	 * @constant
	 * @static
	 * @private
	 */
	Exanpe.GMap.GOOGLE_MAP_PREFIX = "exanpe-gmap-";

	/**
	 * Dom method utility
	 * @private
	 */
	Exanpe.GMap.prototype.getMapContainerEl = function() {
		return YAHOO.util.Dom.get(Exanpe.GMap.GOOGLE_MAP_PREFIX + this.id);
	};

	/** 
	 * Initializes the Map object
	 * @private
	 */
	Exanpe.GMap.prototype._initMap = function(){
		// Init windows
		this.infoWindow = new google.maps.InfoWindow();
		
		// Options
		var position = new google.maps.LatLng(this.latitude, this.longitude);
		this.options = {
				center: position,
				zoom: this.zoom,					
				draggable: this.draggable,
				mapTypeId: eval("google.maps.MapTypeId." + this.mapType),
				mapTypeControl: true,
				mapTypeControlOptions: {
				  style: google.maps.MapTypeControlStyle.DROPDOWN_MENU
				},					
				navigationControl: true,
				navigationControlOptions: {
				  style: google.maps.NavigationControlStyle.SMALL
				}
			};		
		
		// Init map
		this.map = new google.maps.Map(this.getMapContainerEl(), this.options);
	}

	/** 
	 * Initializes the Poly object
	 * @private
	 */
	Exanpe.GMap.prototype._initPoly = function(){
		  var polyOptions = {
				    strokeColor: this.polyStrokeColor,
				    strokeOpacity: this.polyStrokeOpacity,
				    strokeWeight: this.polyStrokeWeight
		  }
		  
		  if (this.polygon) {
			  this.poly = new google.maps.Polygon(polyOptions);
		  }
		  else {
			  this.poly = new google.maps.Polyline(polyOptions);  
		  }
		  
		  this.poly.setMap(this.map);
	};

	/** 
	 * Initializes the GMap Markers
	 * @private
	 */
	Exanpe.GMap.prototype._initMarker = function(marker) {
		// Add Marker
		var markerPosition = new google.maps.LatLng(marker.latitude, marker.longitude);
		var gmarker = new google.maps.Marker({
				position: markerPosition,
				map: this.map,
				icon : marker.icon
			}
		);	
		
		// Add Marker event
		var info = null
		var map = this.map;
		var iw = this.infoWindow;
		if (marker.info) {
			info = marker.info;
		}
		
		// Marker listener
		google.maps.event.addListener(gmarker, 'click', function() {
				if (info) {
					iw.setContent(info);
				}
				else {
					iw.close();
					iw.setContent("");
				}
				iw.open(map, this);
				map.panTo(markerPosition);
			}
		);
		
		// Marker link event
		var mapItem = YAHOO.util.Dom.get(marker.id);
		YAHOO.util.Event.addListener(mapItem, "click", function() {
			if (info) {
				iw.setContent(info);
			}
			else {
				iw.close();
				iw.setContent("");
			}
			iw.open(map, gmarker);
			map.panTo(markerPosition);
		});
		
	};

	/** 
	 * Initializes the Poly object
	 * @private
	 */
	Exanpe.GMap.prototype._initPolyPoint = function(point) {
		// Add new Point to the poly path
		var pointPosition = new google.maps.LatLng(point.latitude, point.longitude);
		var path = this.poly.getPath();
		path.push(pointPosition);
	};

	/** 
	 * Prepare map and items to display
	 * @private
	 */
	Exanpe.GMap.prototype._init = function(){
		// Init map
		this._initMap();

		// Init markers
		for (var i = 0; i < this.markers.length; i++) {
			var marker = this.markers[i];
			this._initMarker(marker);
		}
		
		// Init Polyline / Polygon
		if (this.polyPoints.length > 0) {
			this._initPoly();
		}
		for (var i = 0; i < this.polyPoints.length; i++) {
			var point = this.polyPoints[i];
			this._initPolyPoint(point);
		}
	};

	/**
	 * Initializes the GoogleMap component on DOM load
	 * @param {Object} data the json data coming from Java class initialization
	 * @private
	 * @static
	 */
	function gMapBuilder(data) {
		var gMap = new Exanpe.GMap(data.id, data.latitude, data.longitude, data.mapType, data.zoom, data.draggable===true, YAHOO.lang.JSON.parse(data.markers), YAHOO.lang.JSON.parse(data.polyPoints), data.polyStrokeColor, data.polyStrokeOpacity, data.polyStrokeWeight, data.polygon===true);
		gMap._init();
		window[data.id] = gMap;
	};
	
	return {
		init: gMapBuilder
	}
	
});