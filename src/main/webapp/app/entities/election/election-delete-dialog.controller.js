(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('ElectionDeleteController',ElectionDeleteController);

    ElectionDeleteController.$inject = ['$uibModalInstance', 'entity', 'Election'];

    function ElectionDeleteController($uibModalInstance, entity, Election) {
        var vm = this;

        vm.election = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Election.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
