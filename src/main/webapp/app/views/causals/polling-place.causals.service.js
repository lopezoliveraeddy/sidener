(function() {
    'use strict';
    angular
        .module('sidenerApp')
        .factory('DistrictCausalsPollingPlaces', DistrictCausalsPollingPlaces);

    DistrictCausalsPollingPlaces.$inject = ['$resource', 'DateUtils'];

    function DistrictCausalsPollingPlaces ($resource, DateUtils) {
        var resourceUrl =  '/api/recount/:id/polling-places';

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
