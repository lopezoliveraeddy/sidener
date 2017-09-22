(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('StateDetailController', StateDetailController);

    StateDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'State'];

    function StateDetailController($scope, $rootScope, $stateParams, previousState, entity, State) {
        var vm = this;

        vm.state = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('sidenerApp:stateUpdate', function(event, result) {
            vm.state = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
