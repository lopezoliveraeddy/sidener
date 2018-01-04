(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('ArchiveDeleteController',ArchiveDeleteController);

    ArchiveDeleteController.$inject = ['$uibModalInstance', 'entity', 'Archive'];

    function ArchiveDeleteController($uibModalInstance, entity, Archive) {
        var vm = this;

        vm.archive = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Archive.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
