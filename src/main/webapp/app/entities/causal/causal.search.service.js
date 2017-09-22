(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .factory('CausalSearch', CausalSearch);

    CausalSearch.$inject = ['$resource'];

    function CausalSearch($resource) {
        var resourceUrl =  'api/_search/causals/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
