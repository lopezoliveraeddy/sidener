(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('DetectorCausalsDetailController', DetectorCausalsDetailController);

    DetectorCausalsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'DetectorCausals'];

    function DetectorCausalsDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, DetectorCausals) {
        var vm = this;

        vm.detectorCausals = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('sidenerApp:detectorCausalsUpdate', function(event, result) {
            vm.detectorCausals = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
