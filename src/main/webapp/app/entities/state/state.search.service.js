(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .factory('StateSearch', StateSearch);

    StateSearch.$inject = ['$resource'];

    function StateSearch($resource) {
        var resourceUrl =  'api/_search/states/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
