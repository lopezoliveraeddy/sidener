(function() {
    'use strict';
    angular
        .module('sidenerApp')
        .factory('GenerateDistrict', GenerateDistrict);

    GenerateDistrict.$inject = ['$resource', 'DateUtils'];

    function GenerateDistrict ($resource, DateUtils) {
        var resourceUrl =  '/api/elections/:id/polling-places';

        return $resource(resourceUrl, {}, {
            'get': {
                method: 'GET', isArray: true,
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateElection = DateUtils.convertDateTimeFromServer(data.dateElection);
                        data.createdDate = DateUtils.convertDateTimeFromServer(data.createdDate);
                        data.updatedDate = DateUtils.convertDateTimeFromServer(data.updatedDate);
                    }
                    return data;
                }
            }
        });
    }

})();