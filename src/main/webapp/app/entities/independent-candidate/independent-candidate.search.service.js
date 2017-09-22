(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .factory('IndependentCandidateSearch', IndependentCandidateSearch);

    IndependentCandidateSearch.$inject = ['$resource'];

    function IndependentCandidateSearch($resource) {
        var resourceUrl =  'api/_search/independent-candidates/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
