(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('ElectionDetailController', ElectionDetailController);

    ElectionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Election', 'ElectionType', 'PoliticalParty', 'Coalition', 'IndependentCandidate', 'Causal', 'User'];

    function ElectionDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Election, ElectionType, PoliticalParty, Coalition, IndependentCandidate, Causal, User) {
        var vm = this;

        vm.election = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('sidenerApp:electionUpdate', function(event, result) {
            vm.election = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
