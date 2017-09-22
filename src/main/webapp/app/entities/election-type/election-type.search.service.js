(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .factory('ElectionTypeSearch', ElectionTypeSearch);

    ElectionTypeSearch.$inject = ['$resource'];

    function ElectionTypeSearch($resource) {
        var resourceUrl =  'api/_search/election-types/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
