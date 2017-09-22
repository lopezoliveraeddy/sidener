(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .factory('CoalitionSearch', CoalitionSearch);

    CoalitionSearch.$inject = ['$resource'];

    function CoalitionSearch($resource) {
        var resourceUrl =  'api/_search/coalitions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
