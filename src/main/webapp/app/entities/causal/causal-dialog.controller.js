(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('CausalDialogController', CausalDialogController);

    CausalDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Causal', 'CausalDescription'];

    function CausalDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Causal, CausalDescription) {
        var vm = this;

        vm.causal = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.causaldescriptions = CausalDescription.query();

        vm.causalDescriptionTrash = []; // Elementos desvinculados a eliminar
        vm.deleteDescription = deleteDescription;

        ini();

        function ini() {
            if(vm.causal.id == null) {
                vm.causal.published = true;
            }
        }

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        $scope.addNewDescription = function() {
            vm.causal.causalDescriptions.push({ 'id': null });
        };

        function deleteDescription (index,id) {
            vm.causalDescriptionTrash.push(id);
            vm.causal.causalDescriptions.splice(index,1);
        }

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.causal.id !== null) {
                vm.causal.updatedDate = new Date();
                Causal.update(vm.causal, onSaveSuccess, onSaveError);

            } else {
                vm.causal.createdDate = new Date();
                vm.causal.updatedDate = new Date();
                Causal.save(vm.causal, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('sidenerApp:causalUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
            // Eliminamos descripciones desvinculadas
            for(var i = 0; i < vm.causalDescriptionTrash.length; i++) {
                if(vm.causalDescriptionTrash[i] !== null) {
                    CausalDescription.delete({id: vm.causalDescriptionTrash[i]}, onDeleteCausalDescriptionSuccess, onDeleteCausalDescriptionError);
                }
            }
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        function onDeleteCausalDescriptionSuccess (result) {}

        function onDeleteCausalDescriptionError() {}



        vm.datePickerOpenStatus.createdDate = false;
        vm.datePickerOpenStatus.updatedDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
