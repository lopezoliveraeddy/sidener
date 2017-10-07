(function() {
    'use strict';
    angular
        .module('sidenerApp')
        .factory('ElectionPeriod', ElectionPeriod);

    ElectionPeriod.$inject = ['$resource', 'DateUtils'];

    function ElectionPeriod ($resource, DateUtils) {
        var resourceUrl =  'api/election-periods/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.start = DateUtils.convertDateTimeFromServer(data.start);
                        data.end = DateUtils.convertDateTimeFromServer(data.end);
                        data.createdDate = DateUtils.convertDateTimeFromServer(data.createdDate);
                        data.updatedDate = DateUtils.convertDateTimeFromServer(data.updatedDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
