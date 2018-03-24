(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('PollingplaceDeleteController',PollingplaceDeleteController);

    PollingplaceDeleteController.$inject = ['$uibModalInstance', 'entity', 'Pollingplace'];

    function PollingplaceDeleteController($uibModalInstance, entity, Pollingplace) {
        var vm = this;

        vm.pollingplace = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Pollingplace.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
