(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .factory('DetectorCausalsSearch', DetectorCausalsSearch);

    DetectorCausalsSearch.$inject = ['$resource'];

    function DetectorCausalsSearch($resource) {
        var resourceUrl =  'api/_search/detector-causals/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
