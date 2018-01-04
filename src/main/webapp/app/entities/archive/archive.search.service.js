(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .factory('ArchiveSearch', ArchiveSearch);

    ArchiveSearch.$inject = ['$resource'];

    function ArchiveSearch($resource) {
        var resourceUrl =  'api/_search/archives/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
