/*
 * Copyright 2011 EXANPE <exanpe@gmail.com>
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

/** Global item repository */
if(!Exanpe){
	var Exanpe = {};
}

/**
 * Utility class to log. Uses YAHOO.log as its basic usage to console.
 * @class Allow the framework to log.
 * @static
 */
Exanpe.Log = {};


/**
 * Log a message in error level
 * @param {String} message the message to log.
 * @static
 */
Exanpe.Log.error = function(message){
	if ( typeof console == 'object' && console.error){
		console.error(message);
	}
};

/**
 * Utility class.
 * @class Allow the framework to factorize common functions.
 * @static
 */
Exanpe.Utils = {};


/**
 * Add a Query parameter to a given url
 * @param {String} url the url to handle
 * @param {String} param the query param name to add
 * @param {String} value the query param value
 * @return {String} the url with the added query parameter.
 * @static
 */
Exanpe.Utils.addQueryParam = function(url, param, value) {
	var separator = "?";
	if (url.indexOf(separator) > 0) {
		separator = "&";
	}
	return (url + separator + param + "=" + value);
};