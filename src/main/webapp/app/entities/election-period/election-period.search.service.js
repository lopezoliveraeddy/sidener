(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .factory('ElectionPeriodSearch', ElectionPeriodSearch);

    ElectionPeriodSearch.$inject = ['$resource'];

    function ElectionPeriodSearch($resource) {
        var resourceUrl =  'api/_search/election-periods/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
