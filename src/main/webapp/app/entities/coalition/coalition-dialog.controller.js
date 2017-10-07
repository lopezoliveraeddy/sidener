(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('CoalitionDialogController', CoalitionDialogController);

    CoalitionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Coalition', 'PoliticalParty'];

    function CoalitionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Coalition, PoliticalParty) {
        var vm = this;

        vm.coalition = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.politicalparties = PoliticalParty.query();

        ini();

        function ini() {
            if(vm.coalition.id == null) {
                vm.coalition.published = true;
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
            if (vm.coalition.id !== null) {
                vm.coalition.updatedDate = new Date();
                Coalition.update(vm.coalition, onSaveSuccess, onSaveError);
            } else {
                vm.coalition.createdDate = new Date();
                vm.coalition.updatedDate = new Date();
                Coalition.save(vm.coalition, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('sidenerApp:coalitionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.createdDate = false;
        vm.datePickerOpenStatus.updatedDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
