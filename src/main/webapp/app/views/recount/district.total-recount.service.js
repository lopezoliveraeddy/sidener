(function() {
    'use strict';
    angular
        .module('sidenerApp')
        .factory('ElectionDistrictsWonLose', ElectionDistrictsWonLose);

    ElectionDistrictsWonLose.$inject = ['$resource', 'DateUtils'];

    function ElectionDistrictsWonLose ($resource, DateUtils) {
        var resourceUrl =  'api/recount/:idElection/districts-won-lose';

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
