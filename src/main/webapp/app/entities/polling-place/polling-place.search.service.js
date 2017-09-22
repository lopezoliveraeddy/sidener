(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .factory('PollingPlaceSearch', PollingPlaceSearch);

    PollingPlaceSearch.$inject = ['$resource'];

    function PollingPlaceSearch($resource) {
        var resourceUrl =  'api/_search/polling-places/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
