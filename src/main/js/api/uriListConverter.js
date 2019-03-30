/**
 * @author Greg Turnquist
 */
/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
// uriListConverter.js is written by: https://spring.io/guides/tutorials/react-and-spring-data-rest/

define(function() {
    'use strict';

    /* Convert a single or array of resources into "URI1\nURI2\nURI3..." */
    return {
        read: function(str /*, opts */) {
            return str.split('\n');
        },
        write: function(obj /*, opts */) {
            // If this is an Array, extract the self URI and then join using a newline
            if (obj instanceof Array) {
                return obj.map(resource => resource._links.self.href).join('\n');
            } else { // otherwise, just return the self URI
                return obj._links.self.href;
            }
        }
    };

});
