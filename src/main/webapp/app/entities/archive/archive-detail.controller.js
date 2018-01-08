(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('ArchiveDetailController', ArchiveDetailController);

    ArchiveDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Archive'];

    function ArchiveDetailController($scope, $rootScope, $stateParams, previousState, entity, Archive) {
        var vm = this;

        vm.archive = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('sidenerApp:archiveUpdate', function(event, result) {
            vm.archive = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
