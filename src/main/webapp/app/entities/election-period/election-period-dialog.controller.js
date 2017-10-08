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
                vm.electionPeriod.updatedDate = new Date();
                ElectionPeriod.update(vm.electionPeriod, onSaveSuccess, onSaveError);
            } else {
                vm.electionPeriod.createdDate = new Date();
                vm.electionPeriod.updatedDate = new Date();
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

        vm.datePickerOpenStatus.startDate = false;
        vm.datePickerOpenStatus.endDate = false;
        vm.datePickerOpenStatus.createdDate = false;
        vm.datePickerOpenStatus.updatedDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
