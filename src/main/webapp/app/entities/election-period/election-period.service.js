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
                        data.startDate = DateUtils.convertDateTimeFromServer(data.startDate);
                        data.endDate = DateUtils.convertDateTimeFromServer(data.endDate);
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
