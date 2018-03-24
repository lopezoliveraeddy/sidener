(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('PollingplaceDialogController', PollingplaceDialogController);

    PollingplaceDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'DataUtils', 'entity', 'Pollingplace', 'Archive', 'Election', 'District', 'Causal'];

    function PollingplaceDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, DataUtils, entity, Pollingplace, Archive, Election, District, Causal) {
        var vm = this;

        vm.pollingplace = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.recordcounts = Archive.query({filter: 'pollingplace-is-null'});
        $q.all([vm.pollingplace.$promise, vm.recordcounts.$promise]).then(function() {
            if (!vm.pollingplace.recordCountId) {
                return $q.reject();
            }
            return Archive.get({id : vm.pollingplace.recordCountId}).$promise;
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
            if (vm.pollingplace.id !== null) {
                Pollingplace.update(vm.pollingplace, onSaveSuccess, onSaveError);
            } else {
                Pollingplace.save(vm.pollingplace, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('sidenerApp:pollingplaceUpdate', result);
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
