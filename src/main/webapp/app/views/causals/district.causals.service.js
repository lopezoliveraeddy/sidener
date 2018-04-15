(function() {
    'use strict';
    angular
        .module('sidenerApp')
        .factory('ElectionCausalsDistrict', ElectionCausalsDistrict);

    ElectionCausalsDistrict.$inject = ['$resource', 'DateUtils'];

    function ElectionCausalsDistrict ($resource, DateUtils) {
        var resourceUrl =  'api/nullity/:idElection/districts';

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
