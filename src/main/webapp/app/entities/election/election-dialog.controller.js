(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('ElectionDialogController', ElectionDialogController);

    ElectionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Election', 'ElectionType', 'ElectionPeriod', 'PoliticalParty', 'Coalition', 'IndependentCandidate', 'Causal'];

    function ElectionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Election, ElectionType, ElectionPeriod, PoliticalParty, Coalition, IndependentCandidate, Causal) {
        var vm = this;

        vm.election = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.electiontypes = ElectionType.query();
        vm.electionperiods = ElectionPeriod.query();
        vm.politicalparties = PoliticalParty.query();
        vm.coalitions = Coalition.query();
        vm.independentcandidates = IndependentCandidate.query();
        vm.causals = Causal.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.election.id !== null) {
                Election.update(vm.election, onSaveSuccess, onSaveError);
            } else {
                Election.save(vm.election, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('sidenerApp:electionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date = false;
        vm.datePickerOpenStatus.created = false;
        vm.datePickerOpenStatus.updated = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
