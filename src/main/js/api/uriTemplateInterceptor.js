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
// uriTemplateInterceptor.js is written by: https://spring.io/guides/tutorials/react-and-spring-data-rest/
define(function(require) {
    'use strict';

    const interceptor = require('rest/interceptor');

    return interceptor({
        request: function(request /*, config, meta */) {
            /* If the URI is a URI Template per RFC 6570 (http://tools.ietf.org/html/rfc6570), trim out the template part */
            if (request.path.indexOf('{') === -1) {
                return request;
            } else {
                request.path = request.path.split('{')[0];
                return request;
            }
        }
    });

});
