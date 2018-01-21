(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('PollingPlaceDialogController', PollingPlaceDialogController);

    PollingPlaceDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'DataUtils', 'entity', 'PollingPlace', 'Archive', 'Election', 'District', 'Causal'];

    function PollingPlaceDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, DataUtils, entity, PollingPlace, Archive, Election, District, Causal) {
        var vm = this;

        vm.pollingPlace = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.recordcounts = Archive.query({filter: 'pollingplace-is-null'});
        $q.all([vm.pollingPlace.$promise, vm.recordcounts.$promise]).then(function() {
            if (!vm.pollingPlace.recordCountId) {
                return $q.reject();
            }
            return Archive.get({id : vm.pollingPlace.recordCountId}).$promise;
        }).then(function(recordCount) {
            vm.recordcounts.push(recordCount);
        });
        vm.elections = Election.query();
        vm.districts = District.query();
        vm.causals = Causal.query();

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

        vm.datePickerOpenStatus.createdDate = false;
        vm.datePickerOpenStatus.updatedDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
