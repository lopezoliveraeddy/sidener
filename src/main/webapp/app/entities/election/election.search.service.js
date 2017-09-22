(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .factory('ElectionSearch', ElectionSearch);

    ElectionSearch.$inject = ['$resource'];

    function ElectionSearch($resource) {
        var resourceUrl =  'api/_search/elections/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
