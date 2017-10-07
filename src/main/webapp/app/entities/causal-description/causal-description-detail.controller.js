(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('CausalDescriptionDetailController', CausalDescriptionDetailController);

    CausalDescriptionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'CausalDescription'];

    function CausalDescriptionDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, CausalDescription) {
        var vm = this;

        vm.causalDescription = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('sidenerApp:causalDescriptionUpdate', function(event, result) {
            vm.causalDescription = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
