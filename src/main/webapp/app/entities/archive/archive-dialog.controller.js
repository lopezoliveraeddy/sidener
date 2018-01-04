(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('ArchiveDialogController', ArchiveDialogController);

    ArchiveDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Archive'];

    function ArchiveDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Archive) {
        var vm = this;

        vm.archive = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.archive.id !== null) {
                Archive.update(vm.archive, onSaveSuccess, onSaveError);
            } else {
                Archive.save(vm.archive, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('sidenerApp:archiveUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
