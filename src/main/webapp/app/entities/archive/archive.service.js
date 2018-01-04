(function() {
    'use strict';
    angular
        .module('sidenerApp')
        .factory('Archive', Archive);

    Archive.$inject = ['$resource'];

    function Archive ($resource) {
        var resourceUrl =  'api/archives/:id';

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
