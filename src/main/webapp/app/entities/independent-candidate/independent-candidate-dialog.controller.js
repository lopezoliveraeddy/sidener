(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('IndependentCandidateDialogController', IndependentCandidateDialogController);

    IndependentCandidateDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'IndependentCandidate'];

    function IndependentCandidateDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, IndependentCandidate) {
        var vm = this;

        vm.independentCandidate = entity;
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
            if (vm.independentCandidate.id !== null) {
                IndependentCandidate.update(vm.independentCandidate, onSaveSuccess, onSaveError);
            } else {
                IndependentCandidate.save(vm.independentCandidate, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('sidenerApp:independentCandidateUpdate', result);
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
