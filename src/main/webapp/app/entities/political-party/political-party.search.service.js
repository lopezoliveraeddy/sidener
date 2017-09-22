(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .factory('PoliticalPartySearch', PoliticalPartySearch);

    PoliticalPartySearch.$inject = ['$resource'];

    function PoliticalPartySearch($resource) {
        var resourceUrl =  'api/_search/political-parties/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
