(function() {
    'use strict';
    angular
        .module('sidenerApp')
        .factory('Pollingplace', Pollingplace);

    Pollingplace.$inject = ['$resource', 'DateUtils'];

    function Pollingplace ($resource, DateUtils) {
        var resourceUrl =  'api/pollingplaces/:id';

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
