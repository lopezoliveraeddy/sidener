(function () {
    'use strict';
    angular
        .module('sidenerApp')
        .factory('DemandDownloadPolling', DemandDownloadPolling);

    DemandDownloadPolling.$inject = ['$http', '$q', 'AuthServerProvider'];

    function DemandDownloadPolling($http, $q, AuthServerProvider) {
        return {
            get: get
        }

        function get(file) {
            console.log(file);
            var deferred = $q.defer();
            var url = "api/file/demandpolling/?districts=" + file;
            var token = AuthServerProvider.getToken();
            console.log(token);
            if (token ) {
                var config = {
                    headers: {
                        'Authorization': 'Bearer ' + token
                    }
                }
                $http.post(url, config, {responseType: 'arraybuffer'}).then(function successCallback(response) {
                    deferred.resolve(response);
                }, function errorCallback(error) {
                    deferred.reject(error);
                });
            } else {
                deferred.reject("No tiene permisos para acceder al recurso");
            }
            return deferred.promise;
        }
    }
})();
