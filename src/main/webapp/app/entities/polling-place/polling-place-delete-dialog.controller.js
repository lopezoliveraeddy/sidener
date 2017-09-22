(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('PollingPlaceDeleteController',PollingPlaceDeleteController);

    PollingPlaceDeleteController.$inject = ['$uibModalInstance', 'entity', 'PollingPlace'];

    function PollingPlaceDeleteController($uibModalInstance, entity, PollingPlace) {
        var vm = this;

        vm.pollingPlace = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PollingPlace.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
