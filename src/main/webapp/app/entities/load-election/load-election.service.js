(function() {
    'use strict';
    angular
        .module('sidenerApp')
        .factory('LoadElection', LoadElection);

    LoadElection.$inject = ['$resource', 'DateUtils'];

    function LoadElection ($resource, DateUtils) {
        var resourceUrl =  'api/election';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateElection = DateUtils.convertDateTimeFromServer(data.dateElection);
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
