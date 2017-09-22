(function() {
    'use strict';
    angular
        .module('sidenerApp')
        .factory('PollingPlace', PollingPlace);

    PollingPlace.$inject = ['$resource', 'DateUtils'];

    function PollingPlace ($resource, DateUtils) {
        var resourceUrl =  'api/polling-places/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.created = DateUtils.convertDateTimeFromServer(data.created);
                        data.updated = DateUtils.convertDateTimeFromServer(data.updated);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
