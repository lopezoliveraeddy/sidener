(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('ElectionTypeDialogController', ElectionTypeDialogController);

    ElectionTypeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ElectionType'];

    function ElectionTypeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ElectionType) {
        var vm = this;

        vm.electionType = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.electionType.id !== null) {
                ElectionType.update(vm.electionType, onSaveSuccess, onSaveError);
            } else {
                ElectionType.save(vm.electionType, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('sidenerApp:electionTypeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.created = false;
        vm.datePickerOpenStatus.updated = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
