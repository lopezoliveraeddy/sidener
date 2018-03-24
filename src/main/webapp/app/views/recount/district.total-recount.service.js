(function() {
    'use strict';
    angular
        .module('sidenerApp')
        .factory('ElectionDistrictsRecount', ElectionDistrictsRecount);

    ElectionDistrictsRecount.$inject = ['$resource', 'DateUtils'];

    function ElectionDistrictsRecount ($resource, DateUtils) {
        var resourceUrl =  '/api/recount/:idElection/districtsrecount';

        return $resource(resourceUrl, {}, {
            'get': {
                method: 'GET', isArray: true,
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
