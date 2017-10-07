(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('CausalDescriptionDialogController', CausalDescriptionDialogController);

    CausalDescriptionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'CausalDescription'];

    function CausalDescriptionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, CausalDescription) {
        var vm = this;

        vm.causalDescription = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.causalDescription.id !== null) {
                CausalDescription.update(vm.causalDescription, onSaveSuccess, onSaveError);
            } else {
                CausalDescription.save(vm.causalDescription, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('sidenerApp:causalDescriptionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
