(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('PollingPlaceDialogController', PollingPlaceDialogController);

    PollingPlaceDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'PollingPlace', 'District'];

    function PollingPlaceDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, PollingPlace, District) {
        var vm = this;

        vm.pollingPlace = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.districts = District.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.pollingPlace.id !== null) {
                PollingPlace.update(vm.pollingPlace, onSaveSuccess, onSaveError);
            } else {
                PollingPlace.save(vm.pollingPlace, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('sidenerApp:pollingPlaceUpdate', result);
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
