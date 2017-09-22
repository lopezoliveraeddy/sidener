(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('PollingPlaceDetailController', PollingPlaceDetailController);

    PollingPlaceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'PollingPlace', 'District'];

    function PollingPlaceDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, PollingPlace, District) {
        var vm = this;

        vm.pollingPlace = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('sidenerApp:pollingPlaceUpdate', function(event, result) {
            vm.pollingPlace = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
