(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('ElectionDetailController', ElectionDetailController);

    ElectionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Election', 'State', 'ElectionType', 'ElectionPeriod', 'PoliticalParty', 'IndependentCandidate', 'Coalition', 'Causal'];

    function ElectionDetailController($scope, $rootScope, $stateParams, previousState, entity, Election, State, ElectionType, ElectionPeriod, PoliticalParty, IndependentCandidate, Coalition, Causal) {
        var vm = this;

        vm.election = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('sidenerApp:electionUpdate', function(event, result) {
            vm.election = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
