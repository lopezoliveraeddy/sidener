(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('CoalitionDetailController', CoalitionDetailController);

    CoalitionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Coalition', 'Archive', 'PoliticalParty'];

    function CoalitionDetailController($scope, $rootScope, $stateParams, previousState, entity, Coalition, Archive, PoliticalParty) {
        var vm = this;

        vm.coalition = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('sidenerApp:coalitionUpdate', function(event, result) {
            vm.coalition = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
