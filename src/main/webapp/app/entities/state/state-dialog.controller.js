(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('StateDialogController', StateDialogController);

    StateDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'State'];

    function StateDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, State) {
        var vm = this;

        vm.state = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        ini();

        function ini() {
            if(vm.state.id == null) {
                vm.state.published = true;
            }
        }

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.state.id !== null) {
                vm.state.updated = new Date();
                State.update(vm.state, onSaveSuccess, onSaveError);
            } else {
                vm.state.created = new Date();
                vm.state.updated = new Date();
                State.save(vm.state, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('sidenerApp:stateUpdate', result);
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
