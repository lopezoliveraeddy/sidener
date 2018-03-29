(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('DetectorCausalsDeleteController',DetectorCausalsDeleteController);

    DetectorCausalsDeleteController.$inject = ['$uibModalInstance', 'entity', 'DetectorCausals'];

    function DetectorCausalsDeleteController($uibModalInstance, entity, DetectorCausals) {
        var vm = this;

        vm.detectorCausals = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            DetectorCausals.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
