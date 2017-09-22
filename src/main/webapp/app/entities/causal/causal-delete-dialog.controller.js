(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('CausalDeleteController',CausalDeleteController);

    CausalDeleteController.$inject = ['$uibModalInstance', 'entity', 'Causal'];

    function CausalDeleteController($uibModalInstance, entity, Causal) {
        var vm = this;

        vm.causal = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Causal.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
