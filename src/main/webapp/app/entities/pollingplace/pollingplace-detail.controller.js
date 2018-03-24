(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('PollingplaceDetailController', PollingplaceDetailController);

    PollingplaceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Pollingplace', 'Archive', 'Election', 'District', 'Causal'];

    function PollingplaceDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Pollingplace, Archive, Election, District, Causal) {
        var vm = this;

        vm.pollingplace = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('sidenerApp:pollingplaceUpdate', function(event, result) {
            vm.pollingplace = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
