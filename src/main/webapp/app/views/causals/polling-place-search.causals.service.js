(function() {
    'use strict';
    angular
        .module('sidenerApp')
        .factory('DistrictCausalsSearchPollingPlaces', DistrictCausalsSearchPollingPlaces);

    DistrictCausalsSearchPollingPlaces.$inject = ['$resource', 'DateUtils'];

    function DistrictCausalsSearchPollingPlaces ($resource, DateUtils) {
        var resourceUrl =  '/api/_search/detector-causals';

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
