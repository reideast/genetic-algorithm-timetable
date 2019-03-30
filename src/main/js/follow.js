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
// Follow.js is written by: https://spring.io/guides/tutorials/react-and-spring-data-rest/
// It will be called to make AJAX calls to the REST API
// (Although the actual server calls are made through client.js, which is passed in here through the {@link api} parameter)
// It is needed by the frontend to follow HATEOAS links

// Example structure of a relArray: [ { rel: 'schedule', params: { size: pageSize } }, ... ]
// rel is the sub-path to follow, i.e. if rootPath='/api' then this relArray item corresponds to: /api/schedule
// an item in relArray could be: just be a string, or an object (with .rel corresponding to that string)
module.exports = function follow(api, rootPath, relArray) {
    const root = api({
        method: 'GET',
        path: rootPath
    });

    return relArray.reduce(function(root, arrayItem) {
        const rel = typeof arrayItem === 'string' ? arrayItem : arrayItem.rel;
        return traverseNext(root, rel, arrayItem);
    }, root);

    function traverseNext(root, rel, arrayItem) {
        return root.then(function(response) {
            if (hasEmbeddedRel(response.entity, rel)) { // We found the item with the rel were were looking for by making the AJAX call specified in `root`
                return response.entity._embedded[rel];
            }

            if (!response.entity._links) {
                return [];
            }

            // The rel wasn't in this AJAX result, so follow the link (labelled `rel`) in the AJAX result
            if (typeof arrayItem === 'string') {
                return api({
                    method: 'GET',
                    path: response.entity._links[rel].href
                });
            } else {
                return api({
                    method: 'GET',
                    path: response.entity._links[rel].href,
                    params: arrayItem.params
                });
            }
        });
    }

    function hasEmbeddedRel(entity, rel) {
        return entity._embedded && entity._embedded.hasOwnProperty(rel);
    }
};
