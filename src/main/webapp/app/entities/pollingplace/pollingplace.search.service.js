(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .factory('PollingplaceSearch', PollingplaceSearch);

    PollingplaceSearch.$inject = ['$resource'];

    function PollingplaceSearch($resource) {
        var resourceUrl =  'api/_search/pollingplaces/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
