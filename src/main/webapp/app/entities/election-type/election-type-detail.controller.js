(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('ElectionTypeDetailController', ElectionTypeDetailController);

    ElectionTypeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ElectionType'];

    function ElectionTypeDetailController($scope, $rootScope, $stateParams, previousState, entity, ElectionType) {
        var vm = this;

        vm.electionType = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('sidenerApp:electionTypeUpdate', function(event, result) {
            vm.electionType = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
