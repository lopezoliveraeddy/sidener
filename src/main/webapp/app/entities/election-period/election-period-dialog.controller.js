(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('ElectionPeriodDialogController', ElectionPeriodDialogController);

    ElectionPeriodDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$filter', 'entity', 'ElectionPeriod'];

    function ElectionPeriodDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $filter, entity, ElectionPeriod) {
        var vm = this;

        vm.electionPeriod = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        ini();

        function ini() {
            if(vm.electionPeriod.id == null) {
                vm.electionPeriod.published = true;
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
            vm.electionPeriod.name = $filter('date')(vm.electionPeriod.start, 'yyyy') + "-" + $filter('date')(vm.electionPeriod.end, 'yyyy');

            if (vm.electionPeriod.id !== null) {
                vm.electionPeriod.updated = new Date();
                ElectionPeriod.update(vm.electionPeriod, onSaveSuccess, onSaveError);
            } else {
                vm.electionPeriod.created = new Date();
                vm.electionPeriod.updated = new Date();
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
