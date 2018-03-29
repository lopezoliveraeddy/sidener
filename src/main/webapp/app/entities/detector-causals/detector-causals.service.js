(function() {
    'use strict';
    angular
        .module('sidenerApp')
        .factory('DetectorCausals', DetectorCausals);

    DetectorCausals.$inject = ['$resource'];

    function DetectorCausals ($resource) {
        var resourceUrl =  'api/detector-causals/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
