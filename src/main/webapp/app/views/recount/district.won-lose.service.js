(function() {
    'use strict';
    angular
        .module('sidenerApp')
        .factory('ElectionDistrictsWonLose', ElectionDistrictsWonLose);

    ElectionDistrictsWonLose.$inject = ['$resource', 'DateUtils'];

    function ElectionDistrictsWonLose ($resource, DateUtils) {
        var resourceUrl =  'api/recount/:idElection/districts-won/:districtWon';

        return $resource(resourceUrl, {}, {
            'get': {
                method: 'GET', isArray: false,
                transformResponse: function (data) {
                    return data;
                }
            }
        });
    }
})();
