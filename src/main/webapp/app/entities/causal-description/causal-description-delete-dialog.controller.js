(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('CausalDescriptionDeleteController',CausalDescriptionDeleteController);

    CausalDescriptionDeleteController.$inject = ['$uibModalInstance', 'entity', 'CausalDescription'];

    function CausalDescriptionDeleteController($uibModalInstance, entity, CausalDescription) {
        var vm = this;

        vm.causalDescription = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CausalDescription.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
