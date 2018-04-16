(function() {
    'use strict';
    angular
        .module('sidenerApp')
        .factory('DistrictPollingPlacesWonLose', DistrictPollingPlacesWonLose);

    DistrictPollingPlacesWonLose.$inject = ['$resource', 'DateUtils'];

    function DistrictPollingPlacesWonLose ($resource, DateUtils) {
        var resourceUrl =  '/api/nullity/:idDistrict/polling-places/:pollingPlaceWon';

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
