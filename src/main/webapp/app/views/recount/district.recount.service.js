(function() {
    'use strict';
    angular
        .module('sidenerApp')
        .factory('ElectionRecountDistrict', ElectionRecountDistrict);

    ElectionRecountDistrict.$inject = ['$resource', 'DateUtils'];

    function ElectionRecountDistrict ($resource, DateUtils) {
        var resourceUrl =  'api/elections/:id/districts';

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
