(function() {
    'use strict';

    angular
        .module('sidenerApp')
        .controller('VoteDialogController', VoteDialogController);

    VoteDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Vote', 'Election', 'PoliticalParty', 'IndependentCandidate', 'Coalition', 'PollingPlace'];

    function VoteDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Vote, Election, PoliticalParty, IndependentCandidate, Coalition, PollingPlace) {
        var vm = this;

        vm.vote = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.elections = Election.query();
        vm.politicalparties = PoliticalParty.query();
        vm.independentcandidates = IndependentCandidate.query();
        vm.coalitions = Coalition.query();
        vm.pollingplaces = PollingPlace.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.vote.id !== null) {
                Vote.update(vm.vote, onSaveSuccess, onSaveError);
            } else {
                Vote.save(vm.vote, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('sidenerApp:voteUpdate', result);
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
