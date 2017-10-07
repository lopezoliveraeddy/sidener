(function() {
    'use strict';
    angular
        .module('sidenerApp')
        .factory('Causal', Causal);

    Causal.$inject = ['$resource', 'DateUtils'];

    function Causal ($resource, DateUtils) {
        var resourceUrl =  'api/causals/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.createdDate = DateUtils.convertDateTimeFromServer(data.createdDate);
                        data.updatedDate = DateUtils.convertDateTimeFromServer(data.updatedDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
