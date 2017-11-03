(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('ElectionDialogController', ElectionDialogController);

    ElectionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Election', 'ElectionType', 'PoliticalParty', 'Coalition', 'IndependentCandidate', 'Causal', 'User'];

    function ElectionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Election, ElectionType, PoliticalParty, Coalition, IndependentCandidate, Causal, User) {
        var vm = this;

        vm.election = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.electiontypes = ElectionType.query();
        vm.politicalparties = PoliticalParty.query();
        vm.coalitions = Coalition.query();
        vm.independentcandidates = IndependentCandidate.query();
        vm.causals = Causal.query();
        vm.users = User.query();

        ini();

        function ini() {
            if(vm.election.politicalPartyAsociatedId !== null) {
                $scope.checked = function() {
                    return 'politicalPartyAsociated';
                }
            }
            if(vm.election.coalitionAsociatedId !== null) {
                $scope.checked = function() {
                    return 'coalitionAsociated';
                }
            }
            if(vm.election.independentCandidateAsociatedId !== null) {
                $scope.checked = function() {
                    return 'independentCandidateAsociated';
                }
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

        vm.datePickerOpenStatus.dateElection = false;
        vm.datePickerOpenStatus.createdDate = false;
        vm.datePickerOpenStatus.updatedDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }

        $scope.change = function(value) {
            console.log(value);
            if(value === 'politicalPartyAsociated') {
                vm.election.coalitionAsociatedId = null;
                vm.election.coalitionAsociatedName = null;
                vm.election.independentCandidateAsociatedId = null;
                vm.election.independentCandidateAsociatedName = null;
            }
            else if(value === 'coalitionAsociated') {
                vm.election.politicalPartyAsociatedId = null;
                vm.election.politicalPartyAsociatedName = null;
                vm.election.independentCandidateAsociatedId = null;
                vm.election.independentCandidateAsociatedName = null;
            }
            else if(value === 'independentCandidateAsociated') {
                vm.election.politicalPartyAsociatedId = null;
                vm.election.politicalPartyAsociatedName = null;
                vm.election.coalitionAsociatedId = null;
                vm.election.coalitionAsociatedName = null;
            }
        }
    }
})();
