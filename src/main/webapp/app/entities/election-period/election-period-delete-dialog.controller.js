(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('ElectionPeriodDeleteController',ElectionPeriodDeleteController);

    ElectionPeriodDeleteController.$inject = ['$uibModalInstance', 'entity', 'ElectionPeriod'];

    function ElectionPeriodDeleteController($uibModalInstance, entity, ElectionPeriod) {
        var vm = this;

        vm.electionPeriod = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ElectionPeriod.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
