(function() {
    'use strict';
    angular
        .module('sidenerApp')
        .factory('CausalType', CausalType);

    CausalType.$inject = ['$resource', 'DateUtils'];

    function CausalType ($resource) {
        var resourceUrl =  'api/causals/type/:typeCausal/:subTypeCausal';

        return $resource(resourceUrl, {}, {
            'get': {
                method: 'GET',
                isArray: true,
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
