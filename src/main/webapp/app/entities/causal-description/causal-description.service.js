(function() {
    'use strict';
    angular
        .module('sidenerApp')
        .factory('CausalDescription', CausalDescription);

    CausalDescription.$inject = ['$resource'];

    function CausalDescription ($resource) {
        var resourceUrl =  'api/causal-descriptions/:id';

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
