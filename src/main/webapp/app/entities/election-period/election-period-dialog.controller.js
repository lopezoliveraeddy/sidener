(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('ElectionPeriodDialogController', ElectionPeriodDialogController);

    ElectionPeriodDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ElectionPeriod'];

    function ElectionPeriodDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ElectionPeriod) {
        var vm = this;

        vm.electionPeriod = entity;
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
            if (vm.electionPeriod.id !== null) {
                ElectionPeriod.update(vm.electionPeriod, onSaveSuccess, onSaveError);
            } else {
                ElectionPeriod.save(vm.electionPeriod, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('sidenerApp:electionPeriodUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.start = false;
        vm.datePickerOpenStatus.end = false;
        vm.datePickerOpenStatus.created = false;
        vm.datePickerOpenStatus.updated = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
