(function() {
    'use strict';
    angular
        .module('sidenerApp')
        .factory('Vote', Vote);

    Vote.$inject = ['$resource', 'DateUtils'];

    function Vote ($resource, DateUtils) {
        var resourceUrl =  'api/votes/:id';

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
