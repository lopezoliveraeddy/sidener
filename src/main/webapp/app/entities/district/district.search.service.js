(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .factory('DistrictSearch', DistrictSearch);

    DistrictSearch.$inject = ['$resource'];

    function DistrictSearch($resource) {
        var resourceUrl =  'api/_search/districts/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
