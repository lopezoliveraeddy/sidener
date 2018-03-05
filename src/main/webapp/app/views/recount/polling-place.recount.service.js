(function() {
    'use strict';
    angular
        .module('sidenerApp')
        .factory('DistrictRecountPollingPlaces', DistrictRecountPollingPlaces);

    DistrictRecountPollingPlaces.$inject = ['$resource', 'DateUtils'];

    function DistrictRecountPollingPlaces ($resource, DateUtils) {
        var resourceUrl =  'api/recount/:idDistrict/polling-places';

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
