(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('CoalitionDeleteController',CoalitionDeleteController);

    CoalitionDeleteController.$inject = ['$uibModalInstance', 'entity', 'Coalition'];

    function CoalitionDeleteController($uibModalInstance, entity, Coalition) {
        var vm = this;

        vm.coalition = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Coalition.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
