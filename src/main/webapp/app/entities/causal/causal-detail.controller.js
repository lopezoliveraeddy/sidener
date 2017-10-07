(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('CausalDetailController', CausalDetailController);

    CausalDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Causal', 'CausalDescription'];

    function CausalDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Causal, CausalDescription) {
        var vm = this;

        vm.causal = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('sidenerApp:causalUpdate', function(event, result) {
            vm.causal = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
