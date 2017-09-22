(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('PoliticalPartyDetailController', PoliticalPartyDetailController);

    PoliticalPartyDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'PoliticalParty'];

    function PoliticalPartyDetailController($scope, $rootScope, $stateParams, previousState, entity, PoliticalParty) {
        var vm = this;

        vm.politicalParty = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('sidenerApp:politicalPartyUpdate', function(event, result) {
            vm.politicalParty = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
