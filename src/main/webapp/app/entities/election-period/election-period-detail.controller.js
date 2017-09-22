(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('ElectionPeriodDetailController', ElectionPeriodDetailController);

    ElectionPeriodDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ElectionPeriod'];

    function ElectionPeriodDetailController($scope, $rootScope, $stateParams, previousState, entity, ElectionPeriod) {
        var vm = this;

        vm.electionPeriod = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('sidenerApp:electionPeriodUpdate', function(event, result) {
            vm.electionPeriod = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
