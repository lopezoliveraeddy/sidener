(function() {
    'use strict';
    angular
        .module('sidenerApp')
        .factory('DistrictPollingPlaces', DistrictPollingPlaces);

    DistrictPollingPlaces.$inject = ['$resource', 'DateUtils'];

    function DistrictPollingPlaces ($resource, DateUtils) {
        var resourceUrl =  '/api/districts/:id/polling-places';

        return $resource(resourceUrl, {}, {
            'get': {
                method: 'GET', isArray: true,
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.createdDate = DateUtils.convertDateTimeFromServer(data.createdDate);
                        data.updatedDate = DateUtils.convertDateTimeFromServer(data.updatedDate);
                    }
                    return data;
                }
            }
        });
    }
})();
