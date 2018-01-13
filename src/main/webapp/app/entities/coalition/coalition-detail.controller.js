(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('CoalitionDetailController', CoalitionDetailController);

    CoalitionDetailController.$inject = ['$scope', '$rootScope', 'previousState', 'entity', 'PublicArchive'];

    function CoalitionDetailController($scope, $rootScope, previousState, entity, PublicArchive) {
        var vm = this;

        vm.coalition = entity;
        vm.previousState = previousState.name;

        vm.image = [];

        var unsubscribe = $rootScope.$on('sidenerApp:coalitionUpdate', function(event, result) {
            vm.coalition = result;
        });
        $scope.$on('$destroy', unsubscribe);

        ini();

        function ini() {
            if(vm.coalition.imageId !== null) {
                PublicArchive.get({id: vm.coalition.imageId}, onSuccess, onError);
            }
        }
        function onSuccess(data, headers) {
            vm.image = data;
            var arrayTmp = vm.image.path.split("/files");
            if(arrayTmp.length > 1) {
                vm.image.path = arrayTmp[arrayTmp.length - 1];
            }
        }
        function onError() {
        }
    }
})();
