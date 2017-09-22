(function() {
    'use strict';
    angular
        .module('sidenerApp')
        .factory('State', State);

    State.$inject = ['$resource', 'DateUtils'];

    function State ($resource, DateUtils) {
        var resourceUrl =  'api/states/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.created = DateUtils.convertDateTimeFromServer(data.created);
                        data.updated = DateUtils.convertDateTimeFromServer(data.updated);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
