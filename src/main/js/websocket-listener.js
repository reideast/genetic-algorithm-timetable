'use strict';

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
// websocket-listener.js is written by: https://spring.io/guides/tutorials/react-and-spring-data-rest/

const SockJS = require('sockjs-client'); // <1>
require('stompjs'); // <2>

function register(registrations) {
    const backedEndpoint = '/geneticalgorithm';
    const socket = SockJS(backedEndpoint); // <3>
    const stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        registrations.forEach(function (registration) { // <4>
            stompClient.subscribe(registration.route, registration.callback);
        });
    });
}

module.exports = {
    register: register
};
