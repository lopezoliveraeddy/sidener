(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('VoteDetailController', VoteDetailController);

    VoteDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Vote', 'PoliticalParty', 'IndependentCandidate', 'Coalition', 'PollingPlace'];

    function VoteDetailController($scope, $rootScope, $stateParams, previousState, entity, Vote, PoliticalParty, IndependentCandidate, Coalition, PollingPlace) {
        var vm = this;

        vm.vote = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('sidenerApp:voteUpdate', function(event, result) {
            vm.vote = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
