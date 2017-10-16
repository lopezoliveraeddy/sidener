(function() {
    'use strict';
    angular
        .module('sidenerApp')
        .factory('ElectionDistrict', ElectionDistrict);

    ElectionDistrict.$inject = ['$resource', 'DateUtils'];

    function ElectionDistrict ($resource, DateUtils) {
        var resourceUrl =  'api/elections/districts/:id';

        return $resource(resourceUrl, {}, {
            'get': {
                method: 'GET', isArray: true,
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.createdDate = DateUtils.convertDateTimeFromServer(data.createdDate);
                        data.updatedDate = DateUtils.convertDateTimeFromServer(data.updatedDate);
                    }
                    return data;
                }
            }
        });
    }
})();
