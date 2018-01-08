(function() {
    'use strict';
    angular
        .module('sidenerApp')
        .factory('PublicArchive', PublicArchive);

    PublicArchive.$inject = ['$resource'];

    function PublicArchive ($resource) {
        var resourceUrl =  'public/api/archives/:id';

        return $resource(resourceUrl, {}, {
            'get': {
                method: 'GET',
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
