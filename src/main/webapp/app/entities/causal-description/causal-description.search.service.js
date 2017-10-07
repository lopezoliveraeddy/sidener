(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .factory('CausalDescriptionSearch', CausalDescriptionSearch);

    CausalDescriptionSearch.$inject = ['$resource'];

    function CausalDescriptionSearch($resource) {
        var resourceUrl =  'api/_search/causal-descriptions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
