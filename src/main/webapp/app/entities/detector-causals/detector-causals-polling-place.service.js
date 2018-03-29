(function() {
    'use strict';
    angular
        .module('sidenerApp')
        .factory('DetectorCausalsPollingPlace', DetectorCausalsPollingPlace);

    DetectorCausalsPollingPlace.$inject = ['$resource', 'DateUtils'];

    function DetectorCausalsPollingPlace ($resource) {
        var resourceUrl =  'api/detector-causals/polling-place/:idPollingPlace/causal/:idCausal';

        return $resource(resourceUrl, {}, {
            'get': {
                method: 'GET',
                isArray: false,
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
