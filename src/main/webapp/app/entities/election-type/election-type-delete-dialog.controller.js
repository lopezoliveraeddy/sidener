(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('ElectionTypeDeleteController',ElectionTypeDeleteController);

    ElectionTypeDeleteController.$inject = ['$uibModalInstance', 'entity', 'ElectionType'];

    function ElectionTypeDeleteController($uibModalInstance, entity, ElectionType) {
        var vm = this;

        vm.electionType = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ElectionType.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
