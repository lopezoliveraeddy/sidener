(function() {
    'use strict';
    angular
        .module('sidenerApp')
        .factory('ElectionPollingPlacesWonLose', ElectionPollingPlacesWonLose);

    ElectionPollingPlacesWonLose.$inject = ['$resource', 'DateUtils'];

    function ElectionPollingPlacesWonLose ($resource, DateUtils) {
        var resourceUrl =  'api/recount/:idDistrict/polling-places-won-lose';

        return $resource(resourceUrl, {}, {
            'get': {
                method: 'GET', isArray: false,
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            }
        });
    }
})();
